package de.thuerwaechter.gol;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Main {
    private static final int CANVAS_SIZE_X = 50;
    private static final int CANVAS_SIZE_Y = 20;

    public static void main(String[] args) {
        Model model = Model.newFixedSizeMirrorEdgesModel(CANVAS_SIZE_X, CANVAS_SIZE_Y).putPattern(Pattern.GENERATION_54);

        Canvas canvas = new Canvas(model);

        Controller controller = new Controller(model);
        while (controller.hasNext()){
            controller.processNextGeneration();
            canvas.paintModel(controller.getCurrentModel(), controller.getGeneration());
            if(controller.getGeneration()==70){
                break;
            }
        }
    }

    public static class Canvas {
        private int canvasSizeX;
        private int canvasSizeY;

        public Canvas(final Model model) {
            canvasSizeX = CANVAS_SIZE_X;
            canvasSizeY = CANVAS_SIZE_Y;
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
