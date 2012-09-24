package de.thuerwaechter.gol.model;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.CellBuilder;
import de.thuerwaechter.gol.model.Point;
import org.junit.Test;

import static junit.framework.Assert.*;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class CellTest {
    @Test
    public void testNewSuccessorCell() throws Exception {
        Cell c1_alive = CellBuilder.newCell(new Point(42, 24));
        assertTrue(c1_alive.isAlive());
        assertTrue(c1_alive.isChanged());
        assertEquals(c1_alive.getPoint(), new Point(42,24));

        Cell c2_dead = c1_alive.newSuccessorCell(Cell.CELL_STATE.DEAD);
        assertNotSame(c2_dead, c1_alive);
        assertFalse(c2_dead.isAlive());
        assertTrue(c2_dead.isChanged());
        assertEquals(c2_dead.getPoint(), new Point(42, 24));

        Cell c3_alive = c2_dead.newSuccessorCell(Cell.CELL_STATE.ALIVE);
        assertNotSame(c2_dead, c3_alive);
        assertTrue(c3_alive.isAlive());
        assertTrue(c3_alive.isChanged());
        assertEquals(c3_alive.getPoint(), new Point(42, 24));

        Cell c4_alive = c1_alive.newSuccessorCell(Cell.CELL_STATE.ALIVE);
        assertNotSame(c4_alive, c1_alive);
        assertTrue(c4_alive.isAlive());
        assertFalse(c4_alive.isChanged());
        assertEquals(c4_alive.getPoint(), new Point(42, 24));

        Cell c5_dead = c2_dead.newSuccessorCell(Cell.CELL_STATE.DEAD);
        assertNotSame(c2_dead, c5_dead);
        assertFalse(c5_dead.isAlive());
        assertFalse(c5_dead.isChanged());
        assertEquals(c5_dead.getPoint(), new Point(42, 24));
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        Cell c1 = CellBuilder.newCell(new Point(42, 24));
        Cell c2 = CellBuilder.newCell(new Point(42, 24));
        assertNotSame(c1, c2);
        assertEquals(c1, c2);
        assertTrue(c1.hashCode() == c2.hashCode());

        c1 = CellBuilder.newCell(new Point(42, 24));
        c2 = CellBuilder.newCell(new Point(42, 42));
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());

        c1 = CellBuilder.newCell(new Point(42, 24));
        c2 = CellBuilder.newDeadCell(new Point(42, 24));
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());

        c1 = new CellBuilder()
                .setCellState(Cell.CELL_STATE.ALIVE)
                .setChanged(false)
                .setPoint(new Point(42, 24)).createCell();
        c1 = new CellBuilder()
                .setCellState(Cell.CELL_STATE.ALIVE)
                .setChanged(true)
                .setPoint(new Point(42, 24)).createCell();
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());
    }

}
