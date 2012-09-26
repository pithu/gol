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

package de.thuerwaechter.gol.util;

import java.util.ArrayList;

/** @author <a href="pts@thuerwaechter.de">pithu</a> */
public class History<T>{
    private final int size;
    private final ArrayList<T> queue = new ArrayList<T>();

    public History(final int size) {
        this.size = size;
    }

    public synchronized void add(T value){
        queue.add(value);
        if(queue.size()>size){
            queue.remove(0);
        }
    }

    public synchronized T get(int generation){
        int idx = queue.size()-1-generation;
        return queue.get(idx);
    }

}
