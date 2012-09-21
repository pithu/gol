package de.thuerwaechter.gol;

public class CellBuilder {
    private Cell.CELL_STATE cellState;
    private boolean changed;
    private Point point;

    public CellBuilder setCellState(final Cell.CELL_STATE _cellState) {
        cellState = _cellState;
        return this;
    }

    public CellBuilder setChanged(final boolean _changed) {
        changed = _changed;
        return this;
    }

    public CellBuilder setPoint(final Point _point) {
        point = _point;
        return this;
    }

    public Cell createCell() {
        return new Cell(cellState, changed, point);
    }

    public static Cell newCell(final Point point){
        return new Cell(Cell.CELL_STATE.ALIVE, true, point);
    }

    public static Cell newCell(final int x, final int y) {
        return new Cell(Cell.CELL_STATE.ALIVE, true, new Point(x,y));
    }

    public static Cell newDeadCell(final Point point){
        return new Cell(Cell.CELL_STATE.DEAD, true, point);
    }


}