package de.thuerwaechter.gol;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/** @author <a href="philipp@thuerwaechter.de">ptur</a> */
public class ModelTest {
    final static Point POINT_00 = new Point(0, 0);
    final static Point POINT_11 = new Point(1, 1);
    final static Cell ALIVE_UNCHANGED_CELL = new CellBuilder().setCellState(Cell.CELL_STATE.ALIVE).setChanged(false).setPoint(POINT_00).createCell();
    final static Cell DEAD_UNCHANGED_CELL = new CellBuilder().setCellState(Cell.CELL_STATE.DEAD).setChanged(false).setPoint(POINT_00).createCell();

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

        assertEquals(CellBuilder.newDeadCell(POINT_00), model.getCell(POINT_00));

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
        assertTrue(model.getCells().size()==3);

        assertEquals(model.getCell(new Point(0, 0)), CellBuilder.newDeadCell(new Point(0, 0)));
        assertEquals(model.getCell(new Point(1, 0)), CellBuilder.newDeadCell(new Point(1, 0)));
        assertEquals(model.getCell(new Point(2,0)), CellBuilder.newCell(new Point(2,0)));
        assertEquals(model.getCell(new Point(3,0)), CellBuilder.newCell(new Point(3,0)));
        assertEquals(model.getCell(new Point(4, 0)), CellBuilder.newCell(new Point(4, 0)));
        assertEquals(model.getCell(new Point(5, 0)), CellBuilder.newDeadCell(new Point(5, 0)));
    }

    @Test
    public void testInfiniteModel(){
        Model model = Model.newInfiniteModel();
        assertEquals(Model.MODEL_TYPE.INFINITE, model.getModelType());

        assertTrue(model.getCells().size()==0);

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new Point(-1042, 420024)));
        assertTrue(model.getCells().size() == 2);
        assertEquals(CellBuilder.newCell(new Point(-1042, 420024)), model.getCell(new Point(-1042, 420024)));
    }

    @Test
    public void testCutEdgesModel(){
        Model model = Model.newFixedSizeCutEdgesModel(10,10);
        assertEquals(Model.MODEL_TYPE.FIXED_CUT, model.getModelType());

        assertTrue(model.getCells().size()==0);

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new Point(-1,-1)));
        assertTrue(model.getCells().size() == 1);
        assertEquals(CellBuilder.newDeadCell(new Point(-1, -1)), model.getCell(new Point(-1, -1)));

        model.putCell(CellBuilder.newCell(new Point(-1,0)));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new Point(10,10)));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new Point(5, 100)));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(POINT_11));
        assertTrue(model.getCells().size()==2);
    }

    @Test
    public void testMirrorEdgesModel(){
        Model model = Model.newFixedSizeMirrorEdgesModel(10,10);
        assertEquals(Model.MODEL_TYPE.FIXED_MIRROR, model.getModelType());

        assertTrue(model.getCells().size()==0);

        model.putCell(CellBuilder.newCell(POINT_00));
        assertTrue(model.getCells().size()==1);

        model.putCell(CellBuilder.newCell(new Point(-1,-1)));
        assertTrue(model.getCells().size() == 2);
        assertEquals(CellBuilder.newCell(new Point(9, 9)), model.getCell(new Point(-1, -1)));

        model.putCell(CellBuilder.newCell(new Point(-1,0)));
        assertTrue(model.getCells().size()==3);
        assertEquals(CellBuilder.newCell(new Point(9, 0)), model.getCell(new Point(-1, 0)));

        model.putCell(CellBuilder.newDeadCell(new Point(10,10)));
        assertTrue(model.getCells().size()==3);
        assertEquals(CellBuilder.newDeadCell(new Point(0, 0)), model.getCell(new Point(10, 10)));

        model.putCell(CellBuilder.newCell(new Point(5, 101)));
        assertTrue(model.getCells().size()==4);
        assertEquals(CellBuilder.newCell(new Point(5, 1)), model.getCell(new Point(5, 101)));

        model.putCell(CellBuilder.newCell(POINT_11));
        assertTrue(model.getCells().size()==5);
    }



}
