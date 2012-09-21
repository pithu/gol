package de.thuerwaechter.gol;

import org.junit.Test;

import static junit.framework.Assert.*;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class PointTest {
    @Test
    public void testGetterEqualsAndHashCode(){
        Point p1 = new Point(10,20);
        assertEquals(p1.getX(), 10);
        assertEquals(p1.getY(), 20);

        Point p2 = new Point(10,20);
        assertEquals(p1, p2);
        assertTrue(p1.hashCode() == p2.hashCode());

        Point p3 = new Point(20,10);
        assertFalse(p1.equals(p3));
        assertFalse(p1.hashCode() == p3.hashCode());
    }
}
