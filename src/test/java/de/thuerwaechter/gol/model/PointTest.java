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

import de.thuerwaechter.gol.model.Point;
import org.junit.Test;

import static junit.framework.Assert.*;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
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
