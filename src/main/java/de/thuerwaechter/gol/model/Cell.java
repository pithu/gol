package de.thuerwaechter.gol.model;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Cell {
    public enum CELL_STATE {DEAD, ALIVE}

    private final CELL_STATE cellState;
    private final boolean changed;
    private final Point point;

    protected Cell(final CELL_STATE alive, final boolean changed, final Point point) {
        this.cellState = alive;
        this.changed = changed;
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public boolean isAlive() {
        return cellState == CELL_STATE.ALIVE;
    }

    public CELL_STATE getCellState() {
        return cellState;
    }

    public boolean isChanged() {
        return changed;
    }

    public Cell newSuccessorCell(final CELL_STATE nextState){
        return new CellBuilder()
                .setChanged(cellState != nextState)
                .setCellState(nextState)
                .setPoint(point).createCell();
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Cell)) {
            return false;
        }

        final Cell cell = (Cell) other;

        if (cellState != cell.cellState) {
            return false;
        }
        if (changed != cell.changed) {
            return false;
        }
        if (point != null ? !point.equals(cell.point) : cell.point != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = (cellState == CELL_STATE.ALIVE ? 1 : 0);
        result = 31 * result + (changed ? 1 : 0);
        result = 31 * result + (point != null ? point.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "alive=" + cellState +
                ", changed=" + changed +
                ", point=" + point +
                '}';
    }

 }
