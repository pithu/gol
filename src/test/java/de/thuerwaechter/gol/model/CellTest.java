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

import org.junit.Test;

import static junit.framework.Assert.*;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public class CellTest {
    @Test
    public void testBuilder(){
        Cell c1 = Cell.newCell(42, 24);
        assertTrue(c1.isAlive());
        assertTrue(c1.isChanged());
        assertEquals(new CellPoint(42,24), c1.getPoint());

        c1 = Cell.newDeadCell(42, 24);
        assertFalse(c1.isAlive());
        assertTrue(c1.isChanged());
        assertEquals(new CellPoint(42,24), c1.getPoint());

        c1 = Cell.newDeadUnchangedCell(42, 24);
        assertFalse(c1.isAlive());
        assertFalse(c1.isChanged());
        assertEquals(new CellPoint(42,24), c1.getPoint());

        c1 = new Cell(new CellPoint(42, 24), CellState.ALIVE_UNCHANGED);
        assertTrue(c1.isAlive());
        assertFalse(c1.isChanged());
        assertEquals(new CellPoint(42,24), c1.getPoint());
    }

    @Test
    public void testEqualsAndHashCode() throws Exception {
        Cell c1 = Cell.newCell(42, 24);
        Cell c2 = Cell.newCell(42, 24);
        assertNotSame(c1, c2);
        assertEquals(c1, c2);
        assertTrue(c1.hashCode() == c2.hashCode());

        c1 = Cell.newCell(42, 24);
        c2 = Cell.newCell(42, 42);
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());

        c1 = Cell.newCell(42, 24);
        c2 = Cell.newDeadCell(42, 24);
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());

        c1 = Cell.newCell(42, 24);
        c2 = Cell.newDeadUnchangedCell(42, 24);
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());

        c1 = new Cell(new CellPoint(42, 24), CellState.ALIVE_CHANGED);
        c2 = new Cell(new CellPoint(42, 24), CellState.ALIVE_UNCHANGED);
        assertFalse(c1.equals(c2));
        assertFalse(c1.hashCode() == c2.hashCode());

        c1 = new Cell(new CellPoint(42, 24), CellState.ALIVE_CHANGED);
        c2 = new Cell(new CellPoint(42, 24), CellState.ALIVE_CHANGED);
        assertEquals(c1, c2);
        assertTrue(c1.hashCode() == c2.hashCode());
    }

}
