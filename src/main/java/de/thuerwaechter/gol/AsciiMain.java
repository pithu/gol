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

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.Model;
import de.thuerwaechter.gol.model.Pattern;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class AsciiMain {
    private static final int CANVAS_SIZE_X = 50;
    private static final int CANVAS_SIZE_Y = 30;

    public static void main(String[] args) {
        Controller controller = new Controller(Model.newInfiniteModel());
        controller.getModel().putPattern(Pattern.GENERATION_54.move(20,10));

        Canvas canvas = new Canvas(CANVAS_SIZE_X, CANVAS_SIZE_Y);
        canvas.paintModel(controller.getModel(), controller.getNrOfGeneration());

        while (controller.modelHasNextGeneration()){
            controller.processNextGeneration();
            canvas.paintModel(controller.getModel(), controller.getNrOfGeneration());
            if(!controller.modelHasNextGeneration() || controller.getNrOfGeneration()==70){
                break;
            }
        }
    }

    public static class Canvas {
        private int canvasSizeX;
        private int canvasSizeY;

        public Canvas(final int x, final int y) {
            canvasSizeX = x;
            canvasSizeY = y;
        }

        public void paintModel(final Model model, final int generation) {
            System.out.println("");
            System.out.println("Generation: " + generation);
            for (int y = 0; y < canvasSizeY; y++){
                for(int x = 0; x < canvasSizeX; x++){
                     System.out.print(getCellCharRepresentation(model.getCell(x,y)));
                }
                System.out.println("");
            }
            System.out.println("");
        }

        private char getCellCharRepresentation(final Cell cell) {
            if(cell == null){
                return '-';
            } else if(cell.isAlive() && cell.isChanged()) {
                return '0';
            } else if(cell.isAlive() && !cell.isChanged()) {
                return 'O';
            } else if(!cell.isAlive() && cell.isChanged()){
                return '+';
            } else {
                return '-';
            }
        }
    }

 }
