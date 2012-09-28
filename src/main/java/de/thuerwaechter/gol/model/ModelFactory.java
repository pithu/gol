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
public class ModelFactory{
    private final Model.ModelType modelType;
    private final int sizeX, sizeY;

    public ModelFactory(final Model.ModelType modelType, final int sizeX, final int sizeY) {
        this.modelType = modelType;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
    }

    public Model newModel(){
        if(modelType == Model.ModelType.INFINITE){
            return Model.newInfiniteModel();
        } else if (modelType == Model.ModelType.FIXED_CUT){
            return Model.newFixedSizeCutEdgesModel(sizeX, sizeY);
        } else {
            return Model.newFixedSizeMirrorEdgesModel(sizeX, sizeY);
        }
    }
}
