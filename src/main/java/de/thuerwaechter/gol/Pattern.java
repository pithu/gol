package de.thuerwaechter.gol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Pattern {
    private static Point startPoint = new Point(2,2);
    public static Pattern LINE_3DOTS = buildPattern(
            Arrays.asList(startPoint, startPoint.plusX(1), startPoint.plusX(2)));

    private final List<Cell> cells;

    public Pattern(final List<Cell> __cells) {
        cells = __cells;
    }

    public List<Cell> getCells() {
        return new ArrayList<Cell>(cells);
    }

    public static Pattern buildPattern(List<Point> points){
        List<Cell> newCells = new ArrayList<Cell>(points.size());
        for(Point point : points){
            newCells.add(CellBuilder.newCell(point));
        }
        return new Pattern(newCells);
    }

    @Override
    public String toString() {
        return "Pattern{" +
                "cells=" + cells +
                '}';
    }
}
