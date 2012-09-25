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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.Model;
import de.thuerwaechter.gol.model.Pattern;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class SwingMain {
    private static final int PAINT_SPEED = 100;

    private static final int CANVAS_SIZE_X = 500;
    private static final int CANVAS_SIZE_Y = 500;

    private static final Pattern initialPattern = Pattern.TEST;
    private static final Model.MODEL_TYPE modelType = Model.MODEL_TYPE.FIXED_CUT;
    private static int scaleFactor = 10;

    private static Color gridColor = Color.GRAY;
    private static Color gridBoundaryColor = Color.BLACK;
    private static Color backGroundColor = Color.WHITE;
    private static Color aliveChanged = Color.RED;
    private static Color aliveUnChanged = Color.BLACK;
    private static Color deadChanged = Color.LIGHT_GRAY;
    private static Color deadUnChanged = Color.WHITE;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        JFrame f = new JFrame("Game of life alpha");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(CANVAS_SIZE_X, CANVAS_SIZE_Y);
        f.setLocation(30, 30);
        f.setVisible(true);
        f.add(new ControllerPanel());
        System.out.println("Created GUI on EDT? " +
                SwingUtilities.isEventDispatchThread());
    }

    private static class ControllerPanel extends JPanel implements ActionListener {
        private final SwingController swingController = new SwingController();
        private final Timer timer;
        private SwingWorker<Void, Void> worker;

        public ControllerPanel() {
            timer = new Timer(PAINT_SPEED, this);
            timer.start();

            // setBorder(BorderFactory.createLineBorder(Color.black));

            addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e){
                }
            });

            addMouseMotionListener(new MouseAdapter(){
                public void mouseDragged(MouseEvent e){
                }
            });

            addComponentListener(new ComponentListener() {
                public void componentResized(ComponentEvent arg0) {
                    handleResize();
                }

                public void componentMoved(ComponentEvent arg0) {
                }

                public void componentShown(ComponentEvent arg0) {
                }

                public void componentHidden(ComponentEvent arg0) {
                }
            });

        }

        private void handleResize() {
            swingController.handleResize(getWidth(), getHeight());
        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            if(swingController.trigger()){
                repaint();
            }
        }

        public void paintComponent(Graphics g) {
            setBackground(backGroundColor);
            super.paintComponent(g);
            swingController.initialize(getWidth(), getHeight());
            swingController.paint(g);
        }
     }

    private static class SwingController{
        private int gridOffsetX, gridOffsetY;
        private int gridSizeX, gridSizeY;
        private int gridWidth, gridHeight;

        private Controller controller;
        private boolean init = false;
        private SwingWorker<Void, Void> worker;

        public boolean isInit() {
            return init;
        }

        public void initialize(final int panelWidth, final int panelHeight){
            if(init){
                return;
            }
            init = true;

            calculateGridSize(panelWidth, panelHeight);

            controller = new Controller(new Controller.ModelFactory(modelType, gridSizeX, gridSizeY));
            controller.getModel().putPattern(initialPattern.move(gridSizeX/2, gridSizeY/2));
        }

        private void calculateGridSize(final int panelWidth, final int panelHeight) {
            gridOffsetX = scaleFactor;
            gridOffsetY = scaleFactor;
            gridSizeX = (panelWidth-gridOffsetX-2)/scaleFactor;
            gridSizeY = (panelHeight-gridOffsetY-2)/scaleFactor;
            gridWidth = gridSizeX*scaleFactor;
            gridHeight = gridSizeY*scaleFactor;
        }

        private void reCenterGrid(final int panelWidth, final int panelHeight) {
            gridOffsetX = (((panelWidth - gridWidth) / 2) / scaleFactor) * scaleFactor ;
            gridOffsetY = (((panelHeight - gridHeight) / 2) / scaleFactor) * scaleFactor ;
            if(gridOffsetX==0){
                gridOffsetX = scaleFactor;
            }
            if(gridOffsetY==0){
                gridOffsetY = scaleFactor;
            }
        }

        public void handleResize(final int panelWidth, final int panelHeight) {
            if(!init){
                return;
            }
            if(Controller.isFixedModelType(modelType)){
                reCenterGrid(panelWidth, panelHeight);
            } else {
                calculateGridSize(panelWidth, panelHeight);
            }
        }

        public void paint(final Graphics g) {
            paintGrid(g);
            paintModel(g);
        }

        private void paintGrid(final Graphics g) {
             for(int x= gridOffsetX; x<=gridWidth + gridOffsetX; x += scaleFactor){
                g.setColor(gridColor);
                g.drawLine(x, gridOffsetY, x, gridHeight + gridOffsetY);
            }
            for(int y = gridOffsetY; y <= gridHeight + gridOffsetY; y += scaleFactor){
                g.setColor(gridColor);
                g.drawLine(gridOffsetX, y, gridWidth + gridOffsetX, y);
            }
            if(Controller.isFixedModelType(modelType)){
                g.setColor(gridBoundaryColor);
                g.drawRect(gridOffsetX, gridOffsetY, gridWidth, gridHeight);
            }
        }

        public void paintModel(Graphics g){
            final Dot dot = new Dot(gridOffsetX, gridOffsetY, scaleFactor);
            for(Cell cell : controller.getModel().getCells()){
                if(cell.isAlive()){
                    if(cell.isChanged()){
                        dot.setColor(aliveChanged);
                    } else {
                        dot.setColor(aliveUnChanged);
                    }
                } else if(cell.isChanged()){
                    dot.setColor(deadChanged);
                } else {
                    dot.setColor(deadUnChanged);
                }
                dot.setX(cell.getPoint().getX()*scaleFactor);
                dot.setY(cell.getPoint().getY() * scaleFactor);
                dot.paintDot(g);
            }
        }

        public boolean trigger() {
            if(!isInit()){
                return false;
            }

            if(worker==null || worker.isDone()){
                worker = new SwingWorker<Void, Void>(){
                    @Override
                    protected Void doInBackground() throws Exception {
                        if(controller.modelHasNextGeneration()){
                            controller.processNextGeneration();
                        }
                        return null;
                    }
                };
                worker.execute();
                return true;
            }
            return false;
        }
    }

    private static class Dot{
        private int gridOffsetX, gridOffsetY;
        private int x,y, width, height;
        private Color color;

        public Dot(final int gridOffsetX, final int gridOffsetY, final int squareSize){
            this.gridOffsetX = gridOffsetX;
            this.gridOffsetY = gridOffsetY;
            this.width = squareSize;
            this.height = squareSize;
            this.color = backGroundColor;
        }

        public void setX(int x){
            this.x = x;
        }

        public void setY(int y){
            this.y = y;
        }

        public void setColor(final Color color) {
            this.color = color;
        }

        public void paintDot(Graphics g){
            g.setColor(color);
            g.fillRect(x+gridOffsetX, y+gridOffsetY, width, height);
            g.setColor(gridColor);
            g.drawRect(x+gridOffsetX, y+gridOffsetY, width, height);
        }

        @Override
        public String toString() {
            return "Dot{" +
                    "x=" + x +
                    ", y=" + y +
                    ", width=" + width +
                    ", height=" + height +
                    ", color=" + color +
                    '}';
        }
    }
}