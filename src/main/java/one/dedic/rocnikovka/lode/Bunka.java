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
public enum Bunka {
        /**
         * volne policko, jeste netrefene
         */
        VODA,
        /**
         * sestrelene policko, kde je voda
         */
        VEDLE,
        /**
         * nesestrelene policko s lodi
         */
        ZABRANE, 
        LOD,
        /**
         * trefene policko s lodi
         */
        STRELENA,
        /**
         * policka lodi, kde jsou vsechna jeji policka strelena
         */
        POTOPENA;
        /**
         * zjistuje jestli na miste neni lod
         * @return true - neni lod, false - je lod
         */
    public boolean jeVolno() {
        return this.ordinal() < LOD.ordinal();
    }
    public boolean jeLod () {
        return this.ordinal() >= LOD.ordinal();
    }
}
