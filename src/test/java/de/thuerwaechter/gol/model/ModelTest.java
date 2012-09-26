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

import org.junit.Test;

import static junit.framework.Assert.*;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public class ModelTest {
    final static CellPoint POINT_00 = new CellPoint(0, 0);
    final static CellPoint POINT_11 = new CellPoint(1, 1);
    final static Cell ALIVE_UNCHANGED_CELL = new Cell(POINT_00, CellState.ALIVE_UNCHANGED);
    final static Cell DEAD_UNCHANGED_CELL = new Cell(POINT_00, CellState.DEAD_UNCHANGED);

    @Test
    public void testIsChanged(){
        Model model = Model.newInfiniteModel();
        assertFalse(model.isChanged());

        model.putCell(POINT_00, CellState.ALIVE_UNCHANGED);
        assertFalse(model.isChanged());

        model.putCell(POINT_00, CellState.DEAD_UNCHANGED);
        assertFalse(model.isChanged());

        model.putCell(POINT_00, CellState.ALIVE_CHANGED);
        assertTrue(model.isChanged());

        model.putCell(POINT_00, CellState.ALIVE_UNCHANGED);
        assertTrue(model.isChanged());
    }

    @Test
    public void testGetAndPut(){
        Model model = Model.newInfiniteModel();

        assertEquals(new Cell(POINT_00, CellState.DEAD_UNCHANGED), model.getCell(POINT_00));

        model.putCell(POINT_00, CellState.ALIVE_CHANGED);
        assertEquals(new Cell(POINT_00, CellState.ALIVE_CHANGED), model.getCell(POINT_00));
        assertEquals(new Cell(POINT_00, CellState.ALIVE_CHANGED), model.getCell(0, 0));
    }

    @Test
    public void testGetCells(){
        Model model = Model.newInfiniteModel();
        assertTrue(model.getCellMap().size() == 0);

        model.putCell(POINT_11, CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(POINT_00, CellState.ALIVE_UNCHANGED);
        assertTrue(model.getCellMap().size()==2);

        model.putCell(POINT_00, CellState.DEAD_UNCHANGED);
        assertTrue(model.getCellMap().size() == 1);
    }

    @Test
    public void testGetAndPutPattern(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("--XXX--"));
        Model model = Model.newInfiniteModel().putPattern(pattern);

        assertTrue(model.isChanged());
        assertEquals(15, model.getCellMap().size());

        assertEquals(model.getCell(new CellPoint(0, 0)), Cell.newDeadUnchangedCell(0, 0));
        assertEquals(model.getCell(new CellPoint(1, 0)), Cell.newDeadUnchangedCell(1, 0));
        assertEquals(model.getCell(new CellPoint(2,0)), Cell.newCell(2,0));
        assertEquals(model.getCell(new CellPoint(3,0)), Cell.newCell(3,0));
        assertEquals(model.getCell(new CellPoint(4, 0)), Cell.newCell(4, 0));
        assertEquals(model.getCell(new CellPoint(5, 0)), Cell.newDeadUnchangedCell(5, 0));
    }

    @Test
    public void testGetNeighbours(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("-XX"));
        Model model = Model.newInfiniteModel().putPattern(pattern);
        Collection<Cell> neighbours = model.getEightNeighbours(model.getCell(new CellPoint(1,0)));

        assertEquals(8, neighbours.size());
        assertTrue(neighbours.contains(Cell.newCell(2, 0)));
        assertTrue(neighbours.contains(Cell.newDeadUnchangedCell(0, 0)));
    }

    @Test
    public void testPopulateNeighbours01(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("-X-"));
        Model model = Model.newInfiniteModel().putPattern(pattern);
        model.populateNeighbours();
        Collection<Cell> cells = model.getCellMap();

        assertEquals(9, cells.size());
        assertTrue(cells.contains(Cell.newCell(1, 0)));
        assertTrue(cells.contains(Cell.newDeadUnchangedCell(0, 0)));
    }

    @Test
    public void testPopulateNeighbours02(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("-X-X-"));
        Model model = Model.newInfiniteModel().putPattern(pattern);
        model.populateNeighbours();
        Collection<Cell> cells = model.getCellMap();

        assertEquals(15, cells.size());
        assertTrue(cells.contains(Cell.newCell(1, 0)));
        assertTrue(cells.contains(Cell.newCell(3, 0)));
        assertTrue(cells.contains(Cell.newDeadUnchangedCell(0, 0)));
        assertTrue(cells.contains(Cell.newDeadUnchangedCell(2, 0)));
    }

    @Test
    public void testInfiniteModel(){
        Model model = Model.newInfiniteModel();
        assertEquals(Model.ModelType.INFINITE, model.getModelType());

        assertTrue(model.getCellMap().size()==0);

        model.putCell(POINT_00, CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(new CellPoint(-1042, 420024), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size() == 2);
        assertEquals(Cell.newCell(-1042, 420024), model.getCell(new CellPoint(-1042, 420024)));
    }

    @Test
    public void testCutEdgesModel(){
        Model model = Model.newFixedSizeCutEdgesModel(10,10);
        assertEquals(Model.ModelType.FIXED_CUT, model.getModelType());

        assertTrue(model.getCellMap().size()==0);

        model.putCell(POINT_00, CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(new CellPoint(-1,-1), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size() == 1);
        assertNull(model.getCell(new CellPoint(-1, -1)));

        model.putCell(new CellPoint(-1,0), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(new CellPoint(10,10), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(new CellPoint(5, 100), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(POINT_11, CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==2);
    }

    @Test
    public void testMirrorEdgesModel(){
        Model model = Model.newFixedSizeMirrorEdgesModel(10,10);
        assertEquals(Model.ModelType.FIXED_MIRROR, model.getModelType());

        assertTrue(model.getCellMap().size()==0);

        model.putCell(POINT_00, CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==1);

        model.putCell(new CellPoint(-1,-1), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size() == 2);
        assertEquals(Cell.newCell(9, 9), model.getCell(new CellPoint(-1, -1)));

        model.putCell(new CellPoint(-1,0), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==3);
        assertEquals(Cell.newCell(9, 0), model.getCell(new CellPoint(-1, 0)));

        model.putCell(new CellPoint(10,10), CellState.DEAD_CHANGED);
        assertTrue(model.getCellMap().size()==3);
        assertEquals(new Cell(POINT_00, CellState.DEAD_CHANGED), model.getCell(new CellPoint(10, 10)));

        model.putCell(new CellPoint(5, 101), CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==4);
        assertEquals(Cell.newCell(5, 1), model.getCell(new CellPoint(5, 101)));

        model.putCell(POINT_11, CellState.ALIVE_CHANGED);
        assertTrue(model.getCellMap().size()==5);
    }



}
