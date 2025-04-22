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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static one.dedic.rocnikovka.lode.Bunka.LOD;
import static one.dedic.rocnikovka.lode.Bunka.VODA;

/**
 *
 * @author aja
 */
public class SeznamLodi {

    static Bunka[][] a1b = new Bunka[][]{
        {LOD}
    };
    static Bunka[][] a2b = new Bunka[][]{
        {LOD, LOD}
    };
    static Bunka[][] a3b = new Bunka[][]{
        {LOD, LOD, LOD}
    };
    static Bunka[][] b3b = new Bunka[][]{
        {LOD, LOD},
        {LOD, VODA}
    };
    static Bunka[][] a4b = new Bunka[][]{
        {LOD, LOD, LOD, LOD}
    };
    static Bunka[][] b4b = new Bunka[][]{
        {LOD, VODA, VODA},
        {LOD, LOD, LOD}
    };
    static Bunka[][] c4b = new Bunka[][]{
        {VODA, LOD, VODA},
        {LOD, LOD, LOD}
    };
    static Bunka[][] d4b = new Bunka[][]{
        {LOD, LOD},
        {LOD, LOD}
    };
    static Bunka[][] a5b = new Bunka[][]{
        {LOD, LOD, LOD, LOD, LOD}
    };
    static Bunka[][] b5b = new Bunka[][]{
        {LOD, VODA, VODA, VODA},
        {LOD, LOD, LOD, LOD}
    };
    static Bunka[][] c5b = new Bunka[][]{
        {LOD, VODA, LOD},
        {LOD, LOD, LOD}
    };
    static Bunka[][] d5b = new Bunka[][]{
        {LOD, LOD, VODA},
        {LOD, LOD, LOD}
    };
    static Bunka[][] e5b = new Bunka[][]{
        {VODA, LOD, VODA, VODA},
        {LOD, LOD, LOD, LOD}
    };
    static Bunka[][] f5b = new Bunka[][]{
        {VODA, LOD, VODA},
        {LOD, LOD, LOD},
        {VODA, LOD, VODA}
    };

    public static final Lod a1 = new Lod(1, a1b, 1, null);
    public static final Lod a2 = new Lod(2, a2b, 2, null);
    public static final Lod a3 = new Lod(3, a3b, 3, null);
    public static final Lod b3 = new Lod(3, b3b, 4, null);
    public static final Lod a4 = new Lod(4, a4b, 5, null);
    public static final Lod b4 = new Lod(4, b4b, 6, null);
    public static final Lod c4 = new Lod(4, c4b, 7, null);
    public static final Lod d4 = new Lod(4, d4b, 8, null);
    public static final Lod a5 = new Lod(5, a5b, 9, null);
    public static final Lod b5 = new Lod(5, b5b, 10, null);
    public static final Lod c5 = new Lod(5, c5b, 11, null);
    public static final Lod d5 = new Lod(5, d5b, 12, null);
    public static final Lod e5 = new Lod(5, e5b, 13, null);
    public static final Lod f5 = new Lod(5, f5b, 14, null);

    static List<Lod> lode = new ArrayList<>();

    static {
        Collections.addAll(lode, a1, a2, a3, b3, a4, b4, c4, d4, a5, b5, c5, d5, e5, f5);
    }

    public static final int POZICE_DVOJKA = 1;

    public static final int POZICE_JEDNICKA = 0;
    public static final int POZICE_CTYRKA = 4;

    public static final int POZICE_PETKA = 8;
    public static final int POZICE_TROJKA = 2;
    public static final int[] POCTYTYPU = {
        POZICE_DVOJKA - POZICE_JEDNICKA,
        POZICE_TROJKA - POZICE_DVOJKA,
        POZICE_CTYRKA - POZICE_TROJKA,
        POZICE_PETKA - POZICE_CTYRKA,
        SeznamLodi.lode.size() - POZICE_PETKA
    };
    public static final int[] ZACATKY = {
        POZICE_JEDNICKA,
        POZICE_DVOJKA,
        POZICE_TROJKA,
        POZICE_CTYRKA,
        POZICE_PETKA
    };
    
    public static final int[] POCTYLODI = {
        4, 3, 2, 1, 1
    };
    //TODO: udelat fci na vyhledavani pomoci id
    public static Lod najdiId (int id) {
        for (Lod lod : lode) {
            if (lod.getId() == id) {
                return lod;
            }
        }
        return null;
    }
}
