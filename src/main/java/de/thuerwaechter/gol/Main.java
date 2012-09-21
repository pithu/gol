package de.thuerwaechter.gol;

import java.util.Arrays;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Main {

    private static Point startPoint = new Point(4,7);
    private static Pattern line = Pattern.buildPattern(
            Arrays.asList(startPoint, startPoint.plusX(1), startPoint.plusX(2)));

    public static void main(String[] args) {
        Model model = new Model(7, 20).init(line);
        Canvas canvas = new Canvas(model);
        Controller controller = new Controller(model);

        while (controller.hasNext()){
            controller.processNextGeneration();
            canvas.paintModel(controller.getCurrentModel(), controller.getGeneration());
            if(controller.getGeneration()==500){
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
            for(int x = 0; x < canvasSizeX; x++){
                for (int y = 0; y < canvasSizeY; y++){
                    System.out.print(getCellCharRepresantion(model.getCell(x,y)));
                }
                System.out.println("");
            }
            System.out.println("");
        }

        private char getCellCharRepresantion(final Cell cell) {
            if(cell == null || !cell.isAlive()){
                return '-';
            } else {
                return 'X';
            }
        }
    }

 }
