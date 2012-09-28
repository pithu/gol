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
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class Model {
    public static enum ModelType {INFINITE, FIXED_CUT, FIXED_MIRROR }

    private final Map<CellPoint, CellState> cellMap = new ConcurrentHashMap<CellPoint, CellState>();
    private final ModelMappingStrategy borderStrategy;
    private final ModelType modelType;
    private AtomicBoolean changed = new AtomicBoolean(false);

    private Model(final ModelMappingStrategy borderStrategy, final ModelType modelType) {
        this.borderStrategy = borderStrategy;
        this.modelType = modelType;
    }

    public static Model newInfiniteModel(){
        return new Model(new InfiniteModelStrategy(), ModelType.INFINITE);
    }

    public static Model newFixedSizeCutEdgesModel(final int x, final int y){
        return new Model(new FixSizeCutEdgesStrategy(x,y), ModelType.FIXED_CUT);
    }

    public static Model newFixedSizeMirrorEdgesModel(final int x, final int y){
        return new Model(new FixSizeMirrorEdgesStrategy(x,y), ModelType.FIXED_MIRROR);
    }

    public Model putPattern(final Pattern pattern) {
        for(CellPoint cellPoint : pattern.getCells()){
            putCell(cellPoint, CellState.ALIVE_CHANGED);
        }
        populateNeighbours();
        return this;
    }

    public Collection<Cell> getCellMap() {
        List<Cell> cellList = new ArrayList<Cell>();
        for(Map.Entry<CellPoint, CellState> entry : cellMap.entrySet()){
            cellList.add(new Cell(entry.getKey(), entry.getValue()));
        }
        return cellList;
    }

    public void populateNeighbours() {
        final Collection<Cell> cellValues = getCellMap();
        for(Cell cell : cellValues){
            if(!cell.isAlive()){
                continue;
            }
            for(Cell neighbour : getEightNeighbours(cell)){
                if(cellMap.get(neighbour.getPoint()) == null){
                    cellMap.put(neighbour.getPoint(), neighbour.getCellState());
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
                final CellPoint p = cell.getPoint().plusXY(x, y);
                final Cell c = getCell(p);
                if(c!=null){
                    neighbours.add(c);
                }
            }
        }
        return neighbours;
    }

    public void putCell(final CellPoint cellPoint, final CellState cellState) {
        if(cellState.isChanged()){
            changed.set(true);
        }
        final CellPoint point = borderStrategy.mapPoint(cellPoint);
        if(point == null){
            return;
        } else {
            if(cellState.isAlive() || cellState.isChanged()){
                cellMap.put(point, cellState);
            } else {
                cellMap.remove(point);
            }
        }
    }

    public Cell getCell(final int x, final int y) {
        return getCell(new CellPoint(x,y));
    }

    public Cell getCell(final CellPoint p) {
        final CellPoint point = borderStrategy.mapPoint(p);
        if(point == null){
            return null;
        } else {
            final CellState cellState = cellMap.get(point);
            if(cellState == null){
                return new Cell(point, CellState.DEAD_UNCHANGED);
            } else {
                return new Cell(point, cellState);
            }
        }
    }

    public ModelType getModelType() {
        return modelType;
    }

    public void resetChangedState() {
        changed.set(false);
    }

    public boolean isChanged() {
        return changed.get();
    }

    @Override
    public String toString() {
        return "Model{" +
                "cells=" + cellMap +
                ", borderStrategy=" + borderStrategy +
                ", modelType=" + modelType +
                ", changed=" + changed +
                '}';
    }

    protected static interface ModelMappingStrategy {
        CellPoint mapPoint(final CellPoint p);
    }

    protected static class InfiniteModelStrategy implements ModelMappingStrategy {
        public CellPoint mapPoint(final CellPoint p) {
            return p;
        }
    }

    protected static class FixSizeMirrorEdgesStrategy implements ModelMappingStrategy {
        private final int maxX, maxY;

        protected FixSizeMirrorEdgesStrategy(final int _maxX, final int _maxY) {
            maxX = _maxX;
            maxY = _maxY;
        }

        public CellPoint mapPoint(final CellPoint p) {
            final int x = clip(p.getX(), maxX);
            final int y = clip(p.getY(), maxY);
            return x != p.getX() || y != p.getY() ? new CellPoint(x,y) : p;
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

        public CellPoint mapPoint(final CellPoint p) {
            if(p.getX() >= maxX || p.getY() >= maxY ||p.getX() < 0 || p.getY() < 0 ){
                return null;
            }
            return p;
        }

    }

}
