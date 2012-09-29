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
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public class Pattern {
    public static Pattern BLANK = buildPattern(Arrays.asList(
            ""
    ));
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
    public static Pattern GLIDER_PRODUCER = buildPatternFromDump(
            "[[0,7],[7,5],[7,4],[4,1],[0,3],[0,4],[0,1],[7,3],[0,2],[7,2],[2,1],[0,6],[6,1],[7,8],[5,1],[7,1],[3,1],[0,5],[7,9],[0,0],[1,1],[7,7],[7,6]]"
    );
    public static Pattern PH = buildPatternFromDump(
            "[[6,1],[17,10],[0,11],[0,1],[2,0],[14,5],[17,9],[11,3],[17,6],[15,5],[5,0],[11,4],[4,5],[0,0],[17,7]," +
                    "[6,2],[17,0],[0,7],[17,8],[0,8],[17,1],[3,0],[6,3],[0,5],[11,1],[6,4],[11,2],[4,0],[11,7],[17,2]," +
                    "[16,5],[11,0],[0,6],[11,10],[0,10],[13,5],[2,5],[0,3],[11,8],[6,5],[12,5],[17,3],[5,5],[3,5]," +
                    "[0,9],[11,9],[11,5],[0,4],[1,0],[17,4],[1,5],[6,0],[11,6],[11,11],[0,2],[17,11],[17,5]]"
    );
    public static Pattern HWSS = buildPatternFromDump(
            "[[5,3],[2,4],[4,0],[0,3],[0,1],[2,0],[6,0],[5,0],[3,0],[6,2],[6,1],[1,0]]"
    );
    public static Pattern HWSS_2 = buildPattern(Arrays.asList(
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

    private final Collection<CellPoint> points;

    public Pattern(final Collection<CellPoint> _points) {
        points = _points;
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

    public static Pattern buildPatternFromDump(String jsonString){
        java.util.regex.Pattern arrayPattern = java.util.regex.Pattern.compile("\\[\\s*(\\d+)\\s*,\\s*(\\d+)\\s*\\]");
        Set<CellPoint> newPoints = new HashSet<CellPoint>();
        Matcher matcher = arrayPattern.matcher(jsonString);
        while(matcher.find()){
            newPoints.add(new CellPoint(Integer.valueOf(matcher.group(1)), Integer.valueOf(matcher.group(2))));
        }
        return new Pattern(newPoints);
    }

    public Set<CellPoint> getPoints() {
        return new HashSet<CellPoint>(points);
    }

    public Pattern move(final int x, final int y){
        Set<CellPoint> movedCells = new HashSet<CellPoint>();
        for(CellPoint cellPoint: points){
            movedCells.add(cellPoint.plusXY(x, y));
        }
        return new Pattern(movedCells);
    }

    public String dumpPattern() {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        for(CellPoint point : points){
            sb.append('[').append(point.x);
            sb.append(',').append(point.y).append(']');
            sb.append(',');
        }
        sb.deleteCharAt(sb.length()-1).append(']');
        return sb.toString();
    }


    @Override
    public String toString() {
        return "Pattern{" +
                "cells=" + points +
                '}';
    }
}
