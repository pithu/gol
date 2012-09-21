package de.thuerwaechter.gol;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Model {
    private final int sizeX;
    private final int sizeY;
    private boolean changed = false;

    private final Map<Point, Cell> cells = new HashMap<Point, Cell>();

    public Model(final int sizeX, final int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Model init(final Pattern pattern) {
        for(Cell cell : pattern.getCells()){
            putCell(cell);
        }
        return this;
    }

    public Model newEmptyModelWithSameSize() {
        return new Model(sizeX, sizeY);
    }

    public Cell getCell(final int x, final int y) {
        return cells.get(new Point(x,y));
    }

    public List<Cell> getAliveCellsWithNeighbours() {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public List<Cell> getNeighbours(final Cell cell) {
        return null;  //To change body of created methods use File | Settings | File Templates.
    }

    public void putCell(final Cell cell) {
        if(cell.isChanged()){
            changed = true;
        }
        cells.put(cell.getPoint(), cell);
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
}
