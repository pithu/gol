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
import java.awt.event.MouseWheelEvent;
import javax.swing.*;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.Model;
import de.thuerwaechter.gol.model.Pattern;
import de.thuerwaechter.gol.model.CellPoint;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class SwingMain {
    private static final int PAINT_SPEED = 100;

    private static final int CANVAS_SIZE_X = 500;
    private static final int CANVAS_SIZE_Y = 500;

    private static final Pattern initialPattern = Pattern.TEST;
    private static final Model.ModelType modelType = Model.ModelType.FIXED_MIRROR;
    private static int scaleFactor = 10;

    private static Color gridColor = Color.GRAY;
    private static Color gridBoundaryColor = Color.RED;
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
        f.add(new MainPanel());
        System.out.println("Created GUI on EDT? " +
                SwingUtilities.isEventDispatchThread());
    }

    private static class MainPanel extends JPanel implements ActionListener {
        private final SwingController swingController = new SwingController();
        private final Timer timer;
        private CellPoint lastMouseDraggedPoint;

        public MainPanel() {
            timer = new Timer(PAINT_SPEED, this);
            timer.start();

            setBorder(BorderFactory.createLineBorder(Color.BLACK));

            addMouseListener(new MouseAdapter(){
                public void mousePressed(MouseEvent e){
                    lastMouseDraggedPoint = toCellPoint(e.getPoint());
                }
            });

            addMouseMotionListener(new MouseAdapter(){
                public void mouseDragged(MouseEvent e){
                    if(lastMouseDraggedPoint==null){
                        lastMouseDraggedPoint = toCellPoint(e.getPoint());
                    }
                    swingController.handleDragPanel(toCellPoint(e.getPoint()).minus(lastMouseDraggedPoint));
                    lastMouseDraggedPoint = toCellPoint(e.getPoint());
                }
            });

            addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(final MouseWheelEvent e) {
                    swingController.handleZoomPanel(toCellPoint(e.getPoint()), e.getWheelRotation());
                }
            });

            addComponentListener(new ComponentListener() {
                public void componentResized(ComponentEvent arg0) {
                    swingController.handleResizePanel(new CellPoint(getWidth(), getHeight()));
                }

                public void componentMoved(ComponentEvent arg0) { }

                public void componentShown(ComponentEvent arg0) { }

                public void componentHidden(ComponentEvent arg0) { }
            });
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

        private static CellPoint toCellPoint(final Point point){
            return new CellPoint(point.x, point.y);
        }
    }

    private static class SwingController{
        private CellPoint gridOffset, originOffset, draggedOriginOffset;
        private CellPoint gridNrOfDots, modelNrOfDots;
        private CellPoint gridRect, panelRect;

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
            gridOffset = new CellPoint(0, 0);

            calculateGridSize(new CellPoint(panelWidth, panelHeight));
            setModelNrOfDots();
            centerOriginOffset();

            controller = new Controller(new Controller.ModelFactory(modelType, modelNrOfDots.x, modelNrOfDots.y));
            final CellPoint patterStartPoint = modelNrOfDots.divide(3);
            controller.getModel().putPattern(initialPattern.move(patterStartPoint.x, patterStartPoint.y));
        }

        private void calculateGridSize(final CellPoint rect) {
            panelRect = rect;
            gridNrOfDots = panelRect.minus(gridOffset).divide(scaleFactor).plus(1,1);
            gridRect = gridNrOfDots.multiply(scaleFactor);
        }

        private void setModelNrOfDots() {
            modelNrOfDots = gridNrOfDots.minus(3, 3);
        }

        private void centerOriginOffset() {
            final CellPoint originOffsetNrOfDots = gridNrOfDots.minus(modelNrOfDots).divide(2);
            draggedOriginOffset = originOffset = gridOffset.plus(originOffsetNrOfDots).multiply(scaleFactor);
        }

        private void moveOriginOffset(final CellPoint diff) {
            draggedOriginOffset = draggedOriginOffset.plus(diff);
            originOffset = draggedOriginOffset.snapToGrid(scaleFactor);
        }

        private void zoomOriginOffset(final CellPoint zoomPoint, final int zoomDirection) {
            final int oldScaleFactor = scaleFactor - zoomDirection;
            if(zoomDirection>0){
                draggedOriginOffset = draggedOriginOffset.minus(zoomPoint.divide(oldScaleFactor)).plus(draggedOriginOffset.divide(oldScaleFactor));
            } else {
                draggedOriginOffset = draggedOriginOffset.plus(zoomPoint.divide(oldScaleFactor)).minus(draggedOriginOffset.divide(oldScaleFactor));
            }
            originOffset = draggedOriginOffset.snapToGrid(scaleFactor);
        }

        private void zoomPanel(final CellPoint zoomPoint, final int zoomDirection) {
            if(zoomDirection==0){
                return;
            }
            scaleFactor += zoomDirection;
            if(scaleFactor<2){
                scaleFactor = 2;
            } else {
                calculateGridSize(panelRect);
                zoomOriginOffset(zoomPoint, zoomDirection);
            }
        }

        public void handleResizePanel(final CellPoint rect) {
            calculateGridSize(rect);
        }

        public void handleDragPanel(final CellPoint diff) {
            moveOriginOffset(diff);
        }

        public void handleZoomPanel(final CellPoint zoomPoint, final int zoomDirection) {
            zoomPanel(zoomPoint, zoomDirection * -1);
        }

        public void paint(final Graphics g) {
            paintModel(g);
            paintGrid(g);
        }

        private void paintGrid(final Graphics g) {
             for(int x= gridOffset.x; x<=gridRect.x + gridOffset.x; x += scaleFactor){
                g.setColor(gridColor);
                g.drawLine(x, gridOffset.y, x, gridRect.y + gridOffset.y);
            }
            for(int y = gridOffset.y; y <= gridRect.y + gridOffset.y; y += scaleFactor){
                g.setColor(gridColor);
                g.drawLine(gridOffset.x, y, gridRect.x + gridOffset.x, y);
            }
            if(Controller.isFixedModelType(modelType)){
                g.setColor(gridBoundaryColor);
                final CellPoint borderRect = modelNrOfDots.multiply(scaleFactor);
                g.drawRect(originOffset.x, originOffset.y, borderRect.x, borderRect.y);
            }
        }

        public void paintModel(Graphics g){
            final Dot dot = new Dot(originOffset, scaleFactor);
            for(Cell cell : controller.getModel().getCellMap()){
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
                dot.setPos(new CellPoint(cell.getPoint().getX(), cell.getPoint().getY()).multiply(scaleFactor));
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
        private final CellPoint originOffset, rect;
        private CellPoint pos;
        private Color color;

        public Dot(final CellPoint originOffset, final int squareSize){
            this.originOffset = originOffset;
            this.rect = new CellPoint(squareSize, squareSize);
            this.color = backGroundColor;
        }

        public void setPos(CellPoint pos){
            this.pos = pos;
        }

        public void setColor(final Color color) {
            this.color = color;
        }

        public void paintDot(Graphics g){
            final CellPoint startPoint = pos.plus(originOffset);
            g.setColor(color);
            g.fillRect(startPoint.x, startPoint.y, rect.x, rect.y);
            g.setColor(gridColor);
            g.drawRect(startPoint.x, startPoint.y, rect.x, rect.y);
        }

        @Override
        public String toString() {
            return "Dot{" +
                    "originOffset=" + originOffset +
                    ", rect=" + rect +
                    ", pos=" + pos +
                    ", color=" + color +
                    '}';
        }
    }

}