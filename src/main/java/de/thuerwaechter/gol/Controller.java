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
import java.util.HashSet;
import java.util.Set;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.Model;

/**
 *
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class Controller {
    private ModelFactory modelFactory;
    private Model currentModel;
    private int generation;
    private CellSuccessorStateStrategy cellSuccessorStateStrategy;

    public Controller(final ModelFactory modelFactory) {
        this.modelFactory = modelFactory;
        currentModel = modelFactory.newModel();
        generation = 0;
        cellSuccessorStateStrategy = new ConwaysCellSuccessorStateStrategy(new Integer[]{2,3}, new Integer[]{3});
    }

    public boolean modelHasNextGeneration() {
        return currentModel.isChanged();
    }

    public Model getModel() {
        return currentModel;
    }

    public int getGeneration() {
        return generation;
    }

    public void processNextGeneration() {
        final Model successorModel = modelFactory.newModel();
        Collection<Cell> cellsOfInterest = currentModel.getCellsOfInterest();
        for(Cell cell : cellsOfInterest){
            final Cell.CELL_STATE nextCellState = cellSuccessorStateStrategy.calculateSuccessorState(
                    cell, currentModel.getEightNeighbours(cell));
            successorModel.putCell(cell.newSuccessorCell(nextCellState));
        }
        currentModel = successorModel;
        generation++;
    }

    private static interface CellSuccessorStateStrategy {
        Cell.CELL_STATE calculateSuccessorState(Cell cell, Collection<Cell> neighbours);
    }

    private static class ConwaysCellSuccessorStateStrategy implements CellSuccessorStateStrategy {
        private final Set<Integer> aliveValues;
        private final Set<Integer> deadValues;

        private ConwaysCellSuccessorStateStrategy(final Integer aliveValues[], final Integer deadValues[]) {
            this.aliveValues = new HashSet<Integer>(Arrays.asList(aliveValues));
            this.deadValues = new HashSet<Integer>(Arrays.asList(deadValues));
        }

        public Cell.CELL_STATE calculateSuccessorState(final Cell cell, final Collection<Cell> neighbours) {
            int numberOfAliveNeighbours=0;
            for(Cell neighbour : neighbours){
                if(neighbour.isAlive()){
                    numberOfAliveNeighbours++;
                }
            }
            if(cell.isAlive()){
                if(aliveValues.contains(numberOfAliveNeighbours)){
                    return Cell.CELL_STATE.ALIVE;
                } else {
                    return Cell.CELL_STATE.DEAD;
                }
            }
           else {
                if(deadValues.contains(numberOfAliveNeighbours)){
                    return Cell.CELL_STATE.ALIVE;
                } else {
                    return Cell.CELL_STATE.DEAD;
                }
            }
        }
    }

    public static class ModelFactory{
        private final Model.MODEL_TYPE modelType;
        private final int sizeX, sizeY;

        private ModelFactory(final Model.MODEL_TYPE modelType, final int sizeX, final int sizeY) {
            this.modelType = modelType;
            this.sizeX = sizeX;
            this.sizeY = sizeY;
        }

        public static ModelFactory newInfiniteModelFactory(){
            return new ModelFactory(Model.MODEL_TYPE.INFINITE, 0, 0);
        }

        public static ModelFactory newFixedCutModelFactory(final int x, final int y){
            return new ModelFactory(Model.MODEL_TYPE.FIXED_CUT, x, y);
        }

        public static ModelFactory newFixedMirrorModelFactory(final int x, final int y){
            return new ModelFactory(Model.MODEL_TYPE.FIXED_MIRROR, x, y);
        }

        public Model newModel(){
            if(modelType == Model.MODEL_TYPE.INFINITE){
                return Model.newInfiniteModel();
            } else if (modelType == Model.MODEL_TYPE.FIXED_CUT){
                return Model.newFixedSizeCutEdgesModel(sizeX, sizeY);
            } else {
                return Model.newFixedSizeMirrorEdgesModel(sizeX, sizeY);
            }
        }
    }
}
