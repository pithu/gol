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

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public class CellPoint {
    public final int x;
    public final int y;
    
    private final int hashCode;

    public CellPoint(final int x, final int y) {
        this.x = x;
        this.y = y;
        this.hashCode = _hashCode();
    }

    public CellPoint plus(final CellPoint point){
        return new CellPoint(x + point.getX(), y + point.getY());
    }

    public CellPoint plus(final int x, final int y){
        return new CellPoint(this.x + x, this.y + y);
    }

    public CellPoint minus(final CellPoint point){
        return new CellPoint(x - point.getX(), y - point.getY());
    }

    public CellPoint minus(final int x, final int y){
        return new CellPoint(this.x - x, this.y - y);
    }

    public CellPoint divide(final int scaleFactor){
        return new CellPoint(x/scaleFactor, y/scaleFactor);
    }

    public CellPoint multiply(final int scaleFactor){
        return new CellPoint(x*scaleFactor, y*scaleFactor);
    }

    public CellPoint snapToGrid(final int scaleFactor){
        return new CellPoint(this.x - this.x % scaleFactor, this.y - this.y % scaleFactor);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public CellPoint plusX(final int diffX){
        return new CellPoint(x+diffX, y);
    }

    public CellPoint plusY(final int diffY){
        return new CellPoint(x, y+diffY);
    }

    public CellPoint plusXY(final int diffX, final int diffY) {
        return new CellPoint(x+diffX, y+diffY);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof CellPoint)) {
            return false;
        }

        final CellPoint point = (CellPoint) other;

        if (x != point.x) {
            return false;
        }
        if (y != point.y) {
            return false;
        }

        return true;
    }

    private int _hashCode() {
        return 31 * x + y;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
