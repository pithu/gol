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

package de.thuerwaechter.gol.util;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class MathPoint {
    public final int x;
    public final int y;

    public MathPoint(final int x, final int y) {
        this.x = x;
        this.y = y;
    }
    public MathPoint(final MathPoint point) {
        this.x = point.getX();
        this.y = point.getY();
    }

    public MathPoint plus(final MathPoint point){
        return new MathPoint(x + point.getX(), y + point.getY());
    }

    public MathPoint plus(final int x, final int y){
        return new MathPoint(this.x + x, this.y + y);
    }

    public MathPoint minus(final MathPoint point){
        return new MathPoint(x - point.getX(), y - point.getY());
    }

    public MathPoint minus(final int x, final int y){
        return new MathPoint(this.x - x, this.y - y);
    }

    public MathPoint divide(final int scaleFactor){
        return new MathPoint(x/scaleFactor, y/scaleFactor);
    }

    public MathPoint multiply(final int scaleFactor){
        return new MathPoint(x*scaleFactor, y*scaleFactor);
    }

    public MathPoint snapToGrid(final int scaleFactor){
        return new MathPoint(this.x - this.x % scaleFactor, this.y - this.y % scaleFactor);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MathPoint)) {
            return false;
        }

        final MathPoint mathPoint = (MathPoint) o;

        if (x != mathPoint.x) {
            return false;
        }
        if (y != mathPoint.y) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = x;
        result = 31 * result + y;
        return result;
    }

    @Override
    public String toString() {
        return "MathPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
