package de.thuerwaechter.gol;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class Point {
    private final int x;
    private final int y;

    public Point(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Point plusX(final int diffX){
        return new Point(x+diffX, y);
    }

    public Point plusY(final int diffY){
        return new Point(x, y+diffY);
    }

    public Point plusXY(final int diffX, final int diffY) {
        return new Point(x+diffX, y+diffY);
    }

    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Point)) {
            return false;
        }

        final Point point = (Point) other;

        if (x != point.x) {
            return false;
        }
        if (y != point.y) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
