package de.thuerwaechter.gol.model;

import de.thuerwaechter.gol.model.Model;
import de.thuerwaechter.gol.model.Point;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class ModelMappingStrategyTest {

    @Test
    public void testInfiniteModelStrategy(){
        Model.ModelMappingStrategy ms = new Model.InfiniteModelStrategy();
        final Point p1 = new Point(42, 24);
        final Point p2 = ms.mapPoint(p1);
        assertEquals(p1, p2);
    }

    @Test
    public void testFixedSizeCutEdgesStrategy(){
        Model.ModelMappingStrategy ms = new Model.FixSizeCutEdgesStrategy(100, 50);
        Point p1 = new Point(42, 24);
        Point p2 = ms.mapPoint(p1);
        assertEquals(p1, p2);

        p1 = new Point(100, 24);
        p2 = ms.mapPoint(p1);
        assertNull(p2);

        p1 = new Point(42, 50);
        p2 = ms.mapPoint(p1);
        assertNull(p2);

        p1 = new Point(-1, 24);
        p2 = ms.mapPoint(p1);
        assertNull(p2);

        p1 = new Point(100, -1);
        p2 = ms.mapPoint(p1);
        assertNull(p2);

        p1 = new Point(0, 0);
        p2 = ms.mapPoint(p1);
        assertEquals(p1, p2);
    }

    @Test
    public void testFixedSizeMirrorEdgesStrategy(){
        Model.ModelMappingStrategy ms = new Model.FixSizeMirrorEdgesStrategy(100, 50);
        Point p1 = new Point(42, 24);
        Point p2 = ms.mapPoint(p1);
        assertEquals(p1, p2);

        p1 = new Point(100, 24);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(0, 24), p2);

        p1 = new Point(42, 50);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(42, 0), p2);

        p1 = new Point(-1, 24);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(99, 24), p2);

        p1 = new Point(100, -1);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(0, 49), p2);

        p1 = new Point(0, 0);
        p2 = ms.mapPoint(p1);
        assertEquals(p1, p2);

        p1 = new Point(1042, 2024);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(42, 24), p2);

        p1 = new Point(-5, -5);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(95, 45), p2);

        p1 = new Point(-1005, -2005);
        p2 = ms.mapPoint(p1);
        assertEquals(new Point(95, 45), p2);
    }

}
