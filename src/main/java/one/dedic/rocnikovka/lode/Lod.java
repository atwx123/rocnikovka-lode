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

/**
 *
 * @author aja
 */
public class Lod {
    private final int velikost;
    private final Bunka [][] vizual;
    private final int id;
    private final String jmeno;

    public Lod(int velikost, Bunka[][] vizual, int id, String jmeno) {
        this.velikost = velikost;
        this.vizual = vizual;
        this.id = id;
        this.jmeno = jmeno != null ? jmeno : Integer.toString(id);
    }

    public int getId() {
        return id;
    }

    public String getJmeno() {
        return jmeno;
    }
    
    public int getVelikost() {
        return velikost;
    }

    public Bunka[][] getVizual() {
        return vizual;
    }

}
