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
    final static Cell ALIVE_UNCHANGED_CELL = new CellBuilder().setCellState(CellState.ALIVE_UNCHANGED).setPoint(POINT_00).createCell();
    final static Cell DEAD_UNCHANGED_CELL = new CellBuilder().setCellState(CellState.DEAD_UNCHANGED).setPoint(POINT_00).createCell();

    @Test
    public void testIsChanged(){
        Model model = Model.newInfiniteModel();
        assertFalse(model.isChanged());

        model.putCell(ALIVE_UNCHANGED_CELL);
        assertFalse(model.isChanged());

        model.putCell(DEAD_UNCHANGED_CELL);
        assertFalse(model.isChanged());

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.isChanged());

        model.putCell(ALIVE_UNCHANGED_CELL);
        assertTrue(model.isChanged());
    }

    @Test
    public void testGetAndPut(){
        Model model = Model.newInfiniteModel();

        assertEquals(CellBuilder.newDeadUnchangedCell(POINT_00), model.getCell(POINT_00));

        model.putCell(CellBuilder.newCell(POINT_00));
        assertEquals(CellBuilder.newCell(POINT_00), model.getCell(POINT_00));
        assertEquals(CellBuilder.newCell(POINT_00), model.getCell(0, 0));
    }

    @Test
    public void testGetCells(){
        Model model = Model.newInfiniteModel();
        assertTrue(model.getCells().size() == 0);

        model.putCell(CellBuilder.newCell(POINT_11));
        assertTrue(model.getCells().size()==1);

        model.putCell(ALIVE_UNCHANGED_CELL);
        assertTrue(model.getCells().size()==2);

        model.putCell(DEAD_UNCHANGED_CELL);
        assertTrue(model.getCells().size() == 1);
    }

    @Test
    public void testGetAndPutPattern(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("--XXX--"));
        Model model = Model.newInfiniteModel().putPattern(pattern);

        assertTrue(model.isChanged());
        assertEquals(15, model.getCells().size());

        assertEquals(model.getCell(new CellPoint(0, 0)), CellBuilder.newDeadUnchangedCell(new CellPoint(0, 0)));
        assertEquals(model.getCell(new CellPoint(1, 0)), CellBuilder.newDeadUnchangedCell(new CellPoint(1, 0)));
        assertEquals(model.getCell(new CellPoint(2,0)), CellBuilder.newCell(new CellPoint(2,0)));
        assertEquals(model.getCell(new CellPoint(3,0)), CellBuilder.newCell(new CellPoint(3,0)));
        assertEquals(model.getCell(new CellPoint(4, 0)), CellBuilder.newCell(new CellPoint(4, 0)));
        assertEquals(model.getCell(new CellPoint(5, 0)), CellBuilder.newDeadUnchangedCell(new CellPoint(5, 0)));
    }

    @Test
    public void testGetNeighbours(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("-XX"));
        Model model = Model.newInfiniteModel().putPattern(pattern);
        Collection<Cell> neighbours = model.getEightNeighbours(model.getCell(new CellPoint(1,0)));

        assertEquals(8, neighbours.size());
        assertTrue(neighbours.contains(CellBuilder.newCell(2, 0)));
        assertTrue(neighbours.contains(CellBuilder.newDeadUnchangedCell(new CellPoint(0, 0))));
    }

    @Test
    public void testPopulateNeighbours01(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("-X-"));
        Model model = Model.newInfiniteModel().putPattern(pattern);
        model.populateNeighbours();
        Collection<Cell> cells = model.getCells();

        assertEquals(9, cells.size());
        assertTrue(cells.contains(CellBuilder.newCell(1, 0)));
        assertTrue(cells.contains(CellBuilder.newDeadUnchangedCell(new CellPoint(0, 0))));
    }

    @Test
    public void testPopulateNeighbours02(){
        Pattern pattern = Pattern.buildPattern(Arrays.asList("-X-X-"));
        Model model = Model.newInfiniteModel().putPattern(pattern);
        model.populateNeighbours();
        Collection<Cell> cells = model.getCells();

        assertEquals(15, cells.size());
        assertTrue(cells.contains(CellBuilder.newCell(1, 0)));
        assertTrue(cells.contains(CellBuilder.newCell(3, 0)));
        assertTrue(cells.contains(CellBuilder.newDeadUnchangedCell(new CellPoint(0, 0))));
        assertTrue(cells.contains(CellBuilder.newDeadUnchangedCell(new CellPoint(2, 0))));
    }

    @Test
    public void testInfiniteModel(){
        Model model = Model.newInfiniteModel();
        assertEquals(Model.ModelType.INFINITE, model.getModelType());

        assertTrue(model.getCells().size()==0);

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new CellPoint(-1042, 420024)));
        assertTrue(model.getCells().size() == 2);
        assertEquals(CellBuilder.newCell(new CellPoint(-1042, 420024)), model.getCell(new CellPoint(-1042, 420024)));
    }

    @Test
    public void testCutEdgesModel(){
        Model model = Model.newFixedSizeCutEdgesModel(10,10);
        assertEquals(Model.ModelType.FIXED_CUT, model.getModelType());

        assertTrue(model.getCells().size()==0);

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new CellPoint(-1,-1)));
        assertTrue(model.getCells().size() == 1);
        assertNull(model.getCell(new CellPoint(-1, -1)));

        model.putCell(CellBuilder.newCell(new CellPoint(-1,0)));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new CellPoint(10,10)));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new CellPoint(5, 100)));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(POINT_11));
        assertTrue(model.getCells().size()==2);
    }

    @Test
    public void testMirrorEdgesModel(){
        Model model = Model.newFixedSizeMirrorEdgesModel(10,10);
        assertEquals(Model.ModelType.FIXED_MIRROR, model.getModelType());

        assertTrue(model.getCells().size()==0);

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new CellPoint(-1,-1)));
        assertTrue(model.getCells().size() == 2);
        assertEquals(CellBuilder.newCell(new CellPoint(9, 9)), model.getCell(new CellPoint(-1, -1)));

        model.putCell(CellBuilder.newCell(new CellPoint(-1,0)));
        assertTrue(model.getCells().size()==3);
        assertEquals(CellBuilder.newCell(new CellPoint(9, 0)), model.getCell(new CellPoint(-1, 0)));

        model.putCell(CellBuilder.newDeadCell(new CellPoint(10,10)));
        assertTrue(model.getCells().size()==3);
        assertEquals(CellBuilder.newDeadCell(new CellPoint(0, 0)), model.getCell(new CellPoint(10, 10)));

        model.putCell(CellBuilder.newCell(new CellPoint(5, 101)));
        assertTrue(model.getCells().size()==4);
        assertEquals(CellBuilder.newCell(new CellPoint(5, 1)), model.getCell(new CellPoint(5, 101)));

        model.putCell(CellBuilder.newCell(POINT_11));
        assertTrue(model.getCells().size()==5);
    }



}
