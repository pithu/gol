package de.thuerwaechter.gol;

import java.util.List;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
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
        final Model successorModel = currentModel.newEmptyModelWithSameSize();
        List<Cell> allCells = currentModel.getAliveCellsWithNeighbours();
        for(Cell cell : allCells){
            final Cell.CELL_STATE nextCellState = successorStateStrategy.calculateSuccessorState(cell, currentModel.getNeighbours(cell));
            successorModel.putCell(cell.newSuccessorCell(nextCellState));
        }
        currentModel = successorModel;
        generation++;
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
            if(numberOfAliveNeighbours >= 2 && numberOfAliveNeighbours <= 3){
                return Cell.CELL_STATE.ALIVE;
            } else {
                return Cell.CELL_STATE.DEAD;
            }
        }
    }

}
