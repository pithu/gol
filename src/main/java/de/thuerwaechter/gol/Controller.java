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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.CellPoint;
import de.thuerwaechter.gol.model.CellState;
import de.thuerwaechter.gol.model.Model;

/**
 *
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class Controller {
    private Model currentModel;
    private int nrOfGeneration;
    private CellSuccessorStateStrategy cellSuccessorStateStrategy;

    public Controller(final Model model) {
        currentModel = model;
        nrOfGeneration = 0;
        cellSuccessorStateStrategy = new ConwaysCellSuccessorStateStrategy(new Integer[]{2,3}, new Integer[]{3});
    }

    public Controller(final Model model, final Integer nrOfAliveNeighboursMakesALifeCellDie[], final Integer nrOfAliveNeighboursMakesADeadCellAlive[]) {
        currentModel = model;
        nrOfGeneration = 0;
        cellSuccessorStateStrategy = new ConwaysCellSuccessorStateStrategy(nrOfAliveNeighboursMakesALifeCellDie, nrOfAliveNeighboursMakesADeadCellAlive);
    }

    public boolean modelHasNextGeneration() {
        return currentModel.isChanged();
    }

    public Model getModel() {
        return currentModel;
    }

    public int getNrOfGeneration() {
        return nrOfGeneration;
    }

    public void processNextGeneration() {
        final Map<CellPoint, CellState> successorModel = new HashMap<CellPoint, CellState>();
        Collection<Cell> cells = currentModel.getCellMap();
        for(Cell cell : cells){
            final CellState nextCellState = cellSuccessorStateStrategy.calculateSuccessorState(
                    cell, currentModel.getEightNeighbours(cell));
            successorModel.put(cell.getPoint(), nextCellState);
        }
        currentModel.resetChangedState();
        for(Map.Entry<CellPoint, CellState> entry : successorModel.entrySet()){
            currentModel.putCell(entry.getKey(), entry.getValue());
        }
        currentModel.populateNeighbours();
        nrOfGeneration++;
    }

    private static interface CellSuccessorStateStrategy {
        CellState calculateSuccessorState(Cell cell, Collection<Cell> neighbours);
    }

    private static class ConwaysCellSuccessorStateStrategy implements CellSuccessorStateStrategy {
        private final Set<Integer> nrOfAliveNeighboursMakesALifeCellDie;
        private final Set<Integer> nrOfAliveNeighboursMakesADeadCellAlive;

        private ConwaysCellSuccessorStateStrategy(final Integer nrOfAliveNeighboursMakesALifeCellDie[], final Integer nrOfAliveNeighboursMakesADeadCellAlive[]) {
            this.nrOfAliveNeighboursMakesALifeCellDie = new HashSet<Integer>(Arrays.asList(nrOfAliveNeighboursMakesALifeCellDie));
            this.nrOfAliveNeighboursMakesADeadCellAlive = new HashSet<Integer>(Arrays.asList(nrOfAliveNeighboursMakesADeadCellAlive));
        }

        public CellState calculateSuccessorState(final Cell cell, final Collection<Cell> neighbours) {
            int numberOfAliveNeighbours=0;
            for(Cell neighbour : neighbours){
                if(neighbour.isAlive()){
                    numberOfAliveNeighbours++;
                }
            }
            if(cell.isAlive()){
                if(nrOfAliveNeighboursMakesALifeCellDie.contains(numberOfAliveNeighbours)){
                    return CellState.ALIVE_UNCHANGED;
                } else {
                    return CellState.DEAD_CHANGED;
                }
            }
           else {
                if(nrOfAliveNeighboursMakesADeadCellAlive.contains(numberOfAliveNeighbours)){
                    return CellState.ALIVE_CHANGED;
                } else {
                    return CellState.DEAD_UNCHANGED;
                }
            }
        }
    }
}
