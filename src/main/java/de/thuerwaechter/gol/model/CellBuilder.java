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
    private CellState cellState;
    private CellPoint point;

    public CellBuilder setCell(final Cell _cell) {
        cellState = _cell.getCellState();
        point = _cell.getPoint();
        return this;
    }

    public CellBuilder setCellState(final CellState _cellState) {
        cellState = _cellState;
        return this;
    }

    public CellBuilder setPoint(final CellPoint _point) {
        point = _point;
        return this;
    }

    public Cell createCell() {
        return new Cell(cellState, point);
    }

    public static Cell newCell(final CellPoint point){
        return new Cell(CellState.ALIVE_CHANGED, point);
    }

    public static Cell newCell(final int x, final int y) {
        return newCell(new CellPoint(x,y));
    }

    public static Cell newDeadCell(final CellPoint point){
        return new Cell(CellState.DEAD_CHANGED, point);
    }

    public static Cell newDeadUnchangedCell(final CellPoint point) {
        return new Cell(CellState.DEAD_UNCHANGED, point);
    }
}