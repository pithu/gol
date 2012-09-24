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

package de.thuerwaechter.gol;

import java.util.List;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.Model;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public class Controller {
    private Model currentModel;
    private int generation;
    private SuccessorStateStrategy successorStateStrategy;

    public Controller(final Model model) {
        currentModel = model;
        generation = 0;
        successorStateStrategy = new DefaultSuccessorStateStrategy();
    }

    public boolean hasNext() {
        return currentModel.isChanged();
    }

    public Model getCurrentModel() {
        return currentModel;
    }

    public int getGeneration() {
        return generation;
    }

    public void processNextGeneration() {
/*
        final Model successorModel = new Model(currentModel.getSizeX(), currentModel.getSizeY(), true);
        Collection<Cell> allCells = currentModel.getAllFocusedCells();
        for(Cell cell : allCells){
            final Cell.CELL_STATE nextCellState = successorStateStrategy.calculateSuccessorState(cell, currentModel.getNeighbours(cell));
            successorModel.putCell(cell.newSuccessorCell(nextCellState));
        }
        currentModel = successorModel;
        generation++;
*/
    }

    private static interface SuccessorStateStrategy{
        public Cell.CELL_STATE calculateSuccessorState(Cell cell, List<Cell> neighbours);
    }

    private static class DefaultSuccessorStateStrategy implements SuccessorStateStrategy{
        public Cell.CELL_STATE calculateSuccessorState(final Cell cell, final List<Cell> neighbours) {
            int numberOfAliveNeighbours=0;
            for(Cell neighbour : neighbours){
                if(neighbour.isAlive()){
                    numberOfAliveNeighbours++;
                }
            }
            if(cell.isAlive()){
                if(numberOfAliveNeighbours >= 2 && numberOfAliveNeighbours <= 3){
                    return Cell.CELL_STATE.ALIVE;
                } else {
                    return Cell.CELL_STATE.DEAD;
                }
            } else {
                if(numberOfAliveNeighbours==3){
                    return Cell.CELL_STATE.ALIVE;
                } else {
                    return Cell.CELL_STATE.DEAD;
                }
            }
        }
    }

/*
    public Collection<Cell> getAllFocusedCells() {
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
                final Point p = cell.getPoint().plusXY(x, y);
                neighbours.add(getCell(p));
            }
        }
        return neighbours;
    }

*/

}
