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

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class Cell {

    private final CellState cellState;
    private final CellPoint point;

    public Cell(final CellState alive, final CellPoint point) {
        this.cellState = alive;
        this.point = point;
    }

    public Cell newSuccessorCell(final CellState nextState){
        return new CellBuilder()
                .setCellState(nextState)
                .setPoint(point).createCell();
    }

    public CellPoint getPoint() {
        return point;
    }

    public CellState getCellState() {
        return cellState;
    }

    public boolean isAlive() {
        return cellState.isAlive();
    }

    public boolean isChanged() {
        return cellState.isChanged();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Cell)) {
            return false;
        }

        final Cell cell = (Cell) o;

        if (cellState != cell.cellState) {
            return false;
        }
        if (point != null ? !point.equals(cell.point) : cell.point != null) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = cellState != null ? cellState.hashCode() : 0;
        result = 31 * result + (point != null ? point.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Cell{" +
                "cellState=" + cellState +
                ", point=" + point +
                '}';
    }
}
