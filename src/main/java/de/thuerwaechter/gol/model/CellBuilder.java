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

public class CellBuilder {
    private Cell.CELL_STATE cellState;
    private boolean changed;
    private Point point;

    public CellBuilder setCell(final Cell _cell) {
        cellState = _cell.getCellState();
        changed = _cell.isChanged();
        point = _cell.getPoint();
        return this;
    }

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