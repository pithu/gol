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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.Pattern;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class SwingMain {
    private static final int PAINT_SPEED = 100;
    private static final int CANVAS_SIZE_X = 500;
    private static final int CANVAS_SIZE_Y = 500;
    private static final int GRID_OFFSET = 10;
    private static final Pattern PATTERN = Pattern.TEST;
    private static Color gridColor = Color.GRAY;
    private static Color backGroundColor = Color.WHITE;
    private static Color aliveChanged = Color.RED;
    private static Color aliveUnChanged = Color.BLACK;
    private static Color deadChanged = Color.LIGHT_GRAY;
    private static Color deadUnChanged = Color.WHITE;

    private static int scaleFactor = 10;

    public static void main(String[] args) {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Game of life alpha");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(CANVAS_SIZE_X + 2*GRID_OFFSET, CANVAS_SIZE_Y + 5*GRID_OFFSET);
        f.setLocation(30, 30);
        f.setVisible(true);
        f.add(new ControllerPanel());
    }

    private static class ControllerPanel extends JPanel implements ActionListener {
        private final Controller controller;
        private final Timer timer;
        private SwingWorker<Void, Void> worker;

        public ControllerPanel() {
            final int gridSizeX = (CANVAS_SIZE_X)/scaleFactor;
            final int gridSizeY = (CANVAS_SIZE_Y)/scaleFactor;

            controller = new Controller(Controller.ModelFactory.newInfiniteModelFactory());
            controller.getModel().putPattern(PATTERN.move(gridSizeX/2, gridSizeY/2));

            timer = new Timer(PAINT_SPEED, this);
            timer.start();

            setBorder(BorderFactory.createLineBorder(Color.black));

            addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e){
                }
            });

            addMouseMotionListener(new MouseAdapter(){
                public void mouseDragged(MouseEvent e){
                }
            });

        }

        @Override
        public void actionPerformed(final ActionEvent e) {
            repaint();
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
            }
        }

        public void paintComponent(Graphics g) {
            setBackground(backGroundColor);
            super.paintComponent(g);
            paintGrid(g);
            paintModel(g);
        }

        private void paintGrid(final Graphics g) {
            for(int x= GRID_OFFSET; x<=CANVAS_SIZE_X+ GRID_OFFSET; x += scaleFactor){
                g.setColor(gridColor);
                g.drawLine(x, GRID_OFFSET,x,CANVAS_SIZE_Y+ GRID_OFFSET);
            }
            for(int y= GRID_OFFSET; y<=CANVAS_SIZE_Y+ GRID_OFFSET; y += scaleFactor){
                g.setColor(gridColor);
                g.drawLine(GRID_OFFSET,y,CANVAS_SIZE_X+ GRID_OFFSET,y);
            }
        }

        public void paintModel(Graphics g){
            final Dot dot = new Dot(scaleFactor,scaleFactor);
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

    }

    private static class Dot{
        private int x,y, width, height;
        private Color color;

        public Dot(int width, int height){
            this.width = width;
            this.height = height;
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
            g.fillRect(x+GRID_OFFSET, y+GRID_OFFSET, width, height);
            g.setColor(gridColor);
            g.drawRect(x+GRID_OFFSET, y+GRID_OFFSET, width, height);
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