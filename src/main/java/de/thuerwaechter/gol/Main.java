package de.thuerwaechter.gol;

import java.util.Arrays;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Main {

    public static void main(String[] args) {
        Model model = new Model(50, 5).init(Pattern.LINE_3DOTS);

        Canvas canvas = new Canvas(model);

        Controller controller = new Controller(model);
        while (controller.hasNext()){
            controller.processNextGeneration();
            canvas.paintModel(controller.getCurrentModel(), controller.getGeneration());
            if(controller.getGeneration()==5){
                break;
            }
        }
    }

    public static class Canvas {
        private int canvasSizeX;
        private int canvasSizeY;

        public Canvas(final Model model) {
            canvasSizeX = model.getSizeX();
            canvasSizeY = model.getSizeY();
            paintModel(model, 0);
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
            if(cell == null || !cell.isAlive()){
                return '-';
            } else {
                return 'X';
            }
        }
    }

 }
