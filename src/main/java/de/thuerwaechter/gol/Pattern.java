package de.thuerwaechter.gol;

import java.util.ArrayList;
import java.util.List;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Pattern {
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
