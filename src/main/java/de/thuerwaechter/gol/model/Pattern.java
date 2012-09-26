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
            "XXX",
            "X-X",
            "X-X",
            "",
            "X-X",
            "X-X",
            "XXX"
    ));
    public static Pattern GLIDER = buildPattern(Arrays.asList(
            "-X-",
            "--X",
            "XXX"
    ));
    public static Pattern HWSS = buildPattern(Arrays.asList(
            "-XXXXXX",
            "X-----X",
            "------X",
            "X----X-",
            "--X----"
    ));
    public static Pattern TEST = buildPattern(Arrays.asList(
            "-XXXXXX",
            "X-----X",
            "------X",
            "X----X-",
            "-XX----"
    ));
    public static Pattern X = buildPattern(Arrays.asList(
            "X------X",
            "-X----X-",
            "--X--X--",
            "---XX---",
            "--X--X--",
            "-X----X-",
            "X------X"
            ));

    private final Set<CellPoint> cells;

    public Pattern(final Set<CellPoint> __cells) {
        cells = __cells;
    }

    public Set<CellPoint> getCells() {
        return new HashSet<CellPoint>(cells);
    }

    public Pattern move(final int x, final int y){
        Set<CellPoint> movedCells = new HashSet<CellPoint>();
        for(CellPoint cellPoint: cells){
            movedCells.add(cellPoint.plusXY(x, y));
        }
        return new Pattern(movedCells);
    }

    public static Pattern buildPattern(List<String> lines){
        Set<CellPoint> newCells = new HashSet<CellPoint>();
        int x = 0, y = 0;
        for(String line : lines){
            for(char c : line.toCharArray()) {
                if(c=='X'){
                    newCells.add(new CellPoint(x,y));
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
