/*
 * Copyright 2025 aja.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package one.dedic.rocnikovka.lode;

import static one.dedic.rocnikovka.lode.Pomucky.kopiePole;

/**
 *
 * @author aja
 */
public class UmistenaLod {
    private final Lod puvodni;
    private int rotace;
    private int x;
    private int y;
    private Bunka[][] vizual;
    private final int velikost;
    private final int id;
    private final String jmeno;
    

    public UmistenaLod(Lod puvodni, int x, int y, int rotace) {
        this.puvodni = puvodni;
        this.vizual = kopiePole(puvodni.getVizual());
        this.velikost = puvodni.getVelikost();
        this.id = puvodni.getId();
        this.jmeno = puvodni.getJmeno(); 
        this.x = x;
        this.y = y;
        this.rotace = rotace;
    }
    
    
}
