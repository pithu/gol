package de.thuerwaechter.gol.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Pattern {
    public static Pattern LINE_3DOTS = buildPattern(Arrays.asList(
            "XXX"
    ));
    public static Pattern GENERATION_54 = buildPattern(Arrays.asList(
            "XXXX",
            "X--X",
            "X--X",
            "X--X",
            "",
            "X--X",
            "X--X",
            "X--X",
            "X--X",
            "XXXX"
    ));

    private final Set<Cell> cells;

    public Pattern(final Set<Cell> __cells) {
        cells = __cells;
    }

    public Set<Cell> getCells() {
        return new HashSet<Cell>(cells);
    }

    public Set<Cell>  getMovedCells(final int x, final int y){
        Set<Cell> newCells = new HashSet<Cell>();
        for(Cell cell: cells){
            newCells.add(CellBuilder.newCell(cell.getPoint().plusXY(x, y)));
        }
        return newCells;
    }

    public static Pattern buildPattern(List<String> lines){
        Set<Cell> newCells = new HashSet<Cell>();
        int x = 0, y = 0;
        for(String line : lines){
            for(char c : line.toCharArray()) {
                if(c=='X'){
                    newCells.add(CellBuilder.newCell(new Point(x,y)));
                }
                x++;
            }
            x=0;
            y++;
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
