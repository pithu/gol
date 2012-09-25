/*
 * Copyright (c) 2012. thuerwaechter.de
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package de.thuerwaechter.gol.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class Model {
    public static enum MODEL_TYPE {INFINITE, FIXED_CUT, FIXED_MIRROR }

    private final Map<Point, Cell> cells = new HashMap<Point, Cell>();
    private final ModelMappingStrategy borderStrategy;
    private final MODEL_TYPE  modelType;
    private boolean changed = false;

    private Model(final ModelMappingStrategy borderStrategy, final MODEL_TYPE modelType) {
        this.borderStrategy = borderStrategy;
        this.modelType = modelType;
    }

    public static Model newInfiniteModel(){
        return new Model(new InfiniteModelStrategy(), MODEL_TYPE.INFINITE);
    }

    public static Model newFixedSizeCutEdgesModel(final int x, final int y){
        return new Model(new FixSizeCutEdgesStrategy(x,y), MODEL_TYPE.FIXED_CUT);
    }

    public static Model newFixedSizeMirrorEdgesModel(final int x, final int y){
        return new Model(new FixSizeMirrorEdgesStrategy(x,y), MODEL_TYPE.FIXED_MIRROR);
    }

    public Model putPattern(final Pattern pattern) {
        for(Cell cell : pattern.getCells()){
            putCell(cell);
        }
        populateNeighbours();
        return this;
    }

    public Collection<Cell> getCells() {
        return cells.values();
    }

    public void populateNeighbours() {
        final Collection<Cell> cellValues = new HashSet<Cell>(cells.values());
        for(Cell cell : cellValues){
            if(!cell.isAlive()){
                continue;
            }
            for(Cell neighbour : getEightNeighbours(cell)){
                if(cells.get(neighbour.getPoint()) == null){
                    cells.put(neighbour.getPoint(), neighbour);
                }
            }
        }
    }

    public Collection<Cell> getEightNeighbours(final Cell cell) {
        List<Cell> neighbours = new ArrayList<Cell>(8);
        for(int x=-1; x <= 1; x++){
            for(int y=-1; y <= 1; y++){
                if(x==0 && y==0){
                    continue;
                }
                final Point p = cell.getPoint().plusXY(x, y);
                neighbours.add(getCell(p));
            }
        }
        return neighbours;
    }

    public void putCell(final Cell cell) {
        if(cell.isChanged()){
            changed = true;
        }
        final Point point = borderStrategy.mapPoint(cell.getPoint());
        if(point == null){
            return;
        } else {
            if(cell.isAlive() || cell.isChanged()){
                if(point.equals(cell.getPoint())){
                    cells.put(point, cell);
                } else {
                    cells.put(point, new CellBuilder().setCell(cell).setPoint(point).createCell());
                }
            } else {
                cells.remove(point);
            }
        }
    }

    public Cell getCell(final int x, final int y) {
        return getCell(new Point(x,y));
    }

    public Cell getCell(final Point p) {
        final Point point = borderStrategy.mapPoint(p);
        if(point == null){
            return CellBuilder.newDeadUnchangedCell(p);
        } else {
            final Cell cell = cells.get(point);
            if(cell == null){
                return CellBuilder.newDeadUnchangedCell(p);
            } else {
                return cell;
            }
        }
    }

    public MODEL_TYPE getModelType() {
        return modelType;
    }

    public boolean isChanged() {
        return changed;
    }

    @Override
    public String toString() {
        return "Model{" +
                "cells=" + cells +
                ", borderStrategy=" + borderStrategy +
                ", modelType=" + modelType +
                ", changed=" + changed +
                '}';
    }

    protected static interface ModelMappingStrategy {
        Point mapPoint(final Point p);
    }

    protected static class InfiniteModelStrategy implements ModelMappingStrategy {
        public Point mapPoint(final Point p) {
            return p;
        }
    }

    protected static class FixSizeMirrorEdgesStrategy implements ModelMappingStrategy {
        private final int maxX, maxY;

        protected FixSizeMirrorEdgesStrategy(final int _maxX, final int _maxY) {
            maxX = _maxX;
            maxY = _maxY;
        }

        public Point mapPoint(final Point p) {
            final int x = clip(p.getX(), maxX);
            final int y = clip(p.getY(), maxY);
            return x != p.getX() || y != p.getY() ? new Point(x,y) : p;
        }

        private int clip(final int value, final int max){
            final int clipValue = value % max;
            if(clipValue < 0){
                return max + clipValue;
            } else {
                return clipValue;
            }
        }
    }

    protected static class FixSizeCutEdgesStrategy implements ModelMappingStrategy {
        private final int maxX, maxY;

        protected FixSizeCutEdgesStrategy(final int _maxX, final int _maxY) {
            maxX = _maxX;
            maxY = _maxY;
        }

        public Point mapPoint(final Point p) {
            if(p.getX() >= maxX || p.getY() >= maxY ||p.getX() < 0 || p.getY() < 0 ){
                return null;
            }
            return p;
        }

    }

}
