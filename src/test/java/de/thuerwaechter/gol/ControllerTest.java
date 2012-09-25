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

package de.thuerwaechter.gol;

import java.util.Arrays;

import de.thuerwaechter.gol.model.Cell;
import de.thuerwaechter.gol.model.CellBuilder;
import de.thuerwaechter.gol.model.Pattern;
import de.thuerwaechter.gol.model.Point;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class ControllerTest {
    @Test
    public void testProcess3DotLine(){
        Controller controller = new Controller(Controller.ModelFactory.newInfiniteModelFactory());
        controller.getModel().putPattern(Pattern.buildPattern(Arrays.asList("----","XXX")));

        assertEquals(15, controller.getModel().getCells().size());
        assertTrue(controller.modelHasNextGeneration());

        controller.processNextGeneration();
        assertTrue(controller.modelHasNextGeneration());
        assertEquals(21, controller.getModel().getCells().size());
        assertEquals(controller.getModel().getCell(0, 0), CellBuilder.newDeadUnchangedCell(new Point(0, 0)));
        assertEquals(controller.getModel().getCell(0, 1), CellBuilder.newDeadCell(new Point(0, 1)));
        assertEquals(controller.getModel().getCell(1,0), CellBuilder.newCell(new Point(1, 0)));
        assertEquals(controller.getModel().getCell(1,1),
                new CellBuilder().setPoint(new Point(1,1)).setCellState(Cell.CELL_STATE.ALIVE).setChanged(false).createCell());
    }

    @Test
    public void testProcessSquare(){
        Controller controller = new Controller(Controller.ModelFactory.newInfiniteModelFactory());
        controller.getModel().putPattern(Pattern.buildPattern(Arrays.asList("XX","XX")));

        assertEquals(16, controller.getModel().getCells().size());
        assertTrue(controller.modelHasNextGeneration());

        controller.processNextGeneration();
        assertFalse(controller.modelHasNextGeneration());
        assertEquals(16, controller.getModel().getCells().size());
        assertEquals(controller.getModel().getCell(3, 3), CellBuilder.newDeadUnchangedCell(new Point(3, 3)));
        assertEquals(controller.getModel().getCell(1,1),
                new CellBuilder().setPoint(new Point(1,1)).setCellState(Cell.CELL_STATE.ALIVE).setChanged(false).createCell());
    }
}
