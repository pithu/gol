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
import de.thuerwaechter.gol.model.CellPoint;
import de.thuerwaechter.gol.model.CellState;
import de.thuerwaechter.gol.model.Model;
import de.thuerwaechter.gol.model.Pattern;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * @author <a href="pts@thuerwaechter.de">pithu</a>
 */
public class ControllerTest {
    @Test
    public void testProcess3DotLine(){
        Controller controller = new Controller(Model.newInfiniteModel());
        controller.getModel().putPattern(Pattern.buildPattern(Arrays.asList("----","XXX")));

        assertEquals(15, controller.getModel().getCellMap().size());
        assertTrue(controller.modelHasNextGeneration());

        controller.processNextGeneration();
        assertTrue(controller.modelHasNextGeneration());
        assertEquals(15, controller.getModel().getCellMap().size());
        assertEquals(controller.getModel().getCell(0, 0), Cell.newDeadUnchangedCell(0, 0));
        assertEquals(controller.getModel().getCell(0, 1), Cell.newDeadCell(0, 1));
        assertEquals(controller.getModel().getCell(1,0), Cell.newCell(1, 0));
        assertEquals(controller.getModel().getCell(1, 1),
                new Cell(new CellPoint(1, 1), CellState.ALIVE_UNCHANGED));
    }

    @Test
    public void testProcessSquare(){
        Controller controller = new Controller(Model.newInfiniteModel());
        controller.getModel().putPattern(Pattern.buildPattern(Arrays.asList("XX","XX")));

        assertEquals(16, controller.getModel().getCellMap().size());
        assertTrue(controller.modelHasNextGeneration());

        controller.processNextGeneration();
        assertFalse(controller.modelHasNextGeneration());
        assertEquals(16, controller.getModel().getCellMap().size());
        assertEquals(controller.getModel().getCell(3, 3), Cell.newDeadUnchangedCell(3, 3));
        assertEquals(controller.getModel().getCell(1,1),
                new Cell(new CellPoint(1, 1),CellState.ALIVE_UNCHANGED));
    }

    @Test
    public void testProcessGeneration54(){
        Controller controller = new Controller(Model.newInfiniteModel());
        controller.getModel().putPattern(Pattern.GENERATION_54);

        while(controller.modelHasNextGeneration()){
            controller.processNextGeneration();
            if(controller.getNrOfGeneration()>55){
                fail("nr. of generation exceeded");
            }
        }
        assertEquals(55, controller.getNrOfGeneration());
    }

}
