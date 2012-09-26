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

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public enum CellState {
    DEAD_CHANGED(false, true),
    DEAD_UNCHANGED(false, false),
    ALIVE_CHANGED(true, true),
    ALIVE_UNCHANGED(true, false);

    private final boolean alive;
    private final boolean changed;

    CellState(final boolean alive, final boolean changed) {
        this.alive = alive;
        this.changed = changed;
    }

    public boolean isAlive() {
        return alive;
    }

    public boolean isChanged() {
        return changed;
    }
}
