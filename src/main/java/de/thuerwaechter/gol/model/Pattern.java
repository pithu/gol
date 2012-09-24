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

package de.thuerwaechter.gol.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
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