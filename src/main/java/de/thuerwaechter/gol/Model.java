package de.thuerwaechter.gol;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Model {
    private ModelBorderStrategy borderStrategy;
    private final int sizeX;
    private final int sizeY;
    private final int memLimit = 10000;
    private boolean changed = false;

    private final Map<Point, Cell> cells = new HashMap<Point, Cell>();

    public Model(final int sizeX, final int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
//        borderStrategy = new InfiniteBorderStrategy();
        borderStrategy = new MirrorBorderStrategy(0, 0, sizeX, sizeY);
    }

    public Model init(final Pattern pattern) {
        for(Cell cell : pattern.getCells()){
            putCell(cell);
        }
        return this;
    }

    public Collection<Cell> getAliveCellsWithNeighbours() {
        Collection<Cell> focusCells = new HashSet<Cell>();
        for(Cell cell : cells.values()){
            focusCells.add(cell);
            for(Cell neighbour : getNeighbours(cell)){
                if(!focusCells.contains(neighbour)){
                    focusCells.add(neighbour);
                }
            }
        }
        return focusCells;
    }

    public List<Cell> getNeighbours(final Cell cell) {
        List<Cell> neighbours = new ArrayList<Cell>(8);
        for(int x=-1; x <= 1; x++){
            for(int y=-1; y <= 1; y++){
                if(x==0 && y==0){
                    continue;
                }
                final Point p = borderStrategy.mapPoint(cell.getPoint().plusXY(x, y));
                neighbours.add(getCellOrCreateDeadOne(p));
            }
        }
        return neighbours;
    }

    public void putCell(final Cell cell) {
        if(cells.size() >= memLimit){
            throw new IllegalStateException("number of cells in model exceeds limit of " + memLimit);
        }
        if(cell.isChanged()){
            changed = true;
        }
        cells.put(borderStrategy.mapPoint(cell.getPoint()), cell);
    }

    public Cell getCell(final int x, final int y) {
        return cells.get(new Point(x,y));
    }

    public Cell getCell(final Point p) {
        return cells.get(p);
    }

    public Cell getCellOrCreateDeadOne(final Point p) {
        final Cell c = cells.get(p);
        if(c==null){
            return CellBuilder.newDeadCell(p);
        } else {
            return c;
        }
    }

    public boolean isChanged() {
        return changed;
    }

    public int getSizeX() {
        return sizeX;
    }

    public int getSizeY() {
        return sizeY;
    }

    @Override
    public String toString() {
        return "Model{" +
                "sizeX=" + sizeX +
                ", sizeY=" + sizeY +
                ", changed=" + changed +
                ", cells=" + cells +
                '}';
    }

    private static interface ModelBorderStrategy{
        public Point mapPoint(final Point p);
    }

    private static class InfiniteBorderStrategy implements ModelBorderStrategy{
        public Point mapPoint(final Point p) {
            return p;
        }
    }

    private static class MirrorBorderStrategy implements ModelBorderStrategy{
        private final int minX, minY, maxX, maxY;

        private MirrorBorderStrategy(final int _minX, final int _minY, final int _maxX, final int _maxY) {
            minX = _minX;
            minY = _minY;
            maxX = _maxX;
            maxY = _maxY;
        }

        public Point mapPoint(final Point p) {
            final int x = clip(p.getX(), minX, maxX);
            final int y = clip(p.getY(), minY, maxY);
            return x != p.getX() || y != p.getY() ? new Point(x,y) : p;
        }

        private int clip(final int value, final int min, final int max){
            return value < min ? max : value >= max ? min : value;
        }
    }
}
