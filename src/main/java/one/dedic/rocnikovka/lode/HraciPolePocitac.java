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

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.Random;
import static one.dedic.rocnikovka.lode.SeznamLodi.POCTYLODI;
import static one.dedic.rocnikovka.lode.SeznamLodi.ZACATKY;
import static one.dedic.rocnikovka.lode.SeznamLodi.lode;
import static one.dedic.rocnikovka.lode.SeznamLodi.POCTYTYPU;

/**
 *
 * @author aja
 */
public class HraciPolePocitac  {
    SeznamUmistenychLodi sULodi = new SeznamUmistenychLodi();
    private Bunka[][] hraciPole;
    Random random = new Random();
    private Screen screen;
    private TextGraphics graphics;
    

    public HraciPolePocitac(Screen screen, TextGraphics graphics) {
        this.screen = screen;
        this.graphics = graphics;
        this.hraciPole = sULodi.hraciPole;
    }

    public SeznamUmistenychLodi getsULodi() {
        return sULodi;
    }

    public void setsULodi(SeznamUmistenychLodi sULodi) {
        this.sULodi = sULodi;
        this.hraciPole = sULodi.hraciPole;
    }

    public Bunka[][] getHraciPole() {
        return hraciPole;
    }
    
    

    private boolean umisteniLodi()  {
        hraciPole = new Bunka[10][10];
        int[] poctyULodi = {
            0, 0, 0, 0, 0
        };
        for (int velikost = 4; velikost >= 0; velikost--) {
            boolean hodnota = true;
            while (poctyULodi[velikost] < POCTYLODI[velikost]) {
                int kolikratTvar = 0;
                while (hodnota) {
                    if (kolikratTvar > 2) {
                        return false;
                    }
                    int pozice = random.nextInt(POCTYTYPU[velikost]);
                    Lod lod = lode.get(ZACATKY[velikost] + pozice);
                    int kolikrat = 0;
                    while (true) {
                        if (kolikrat > 100) {
                            break;
                        }
                        int x = random.nextInt(10 - lod.getVizual()[0].length);
                        int y = random.nextInt(10 - lod.getVizual().length);
                        if (Pomucky.prekryvani(lod, hraciPole, x, y)) {
                            Pomucky.kopiePoleDoPole(x, y, Pomucky.maskaLodi(lod), hraciPole);
                            // TODO REVIEW: nema se vybrana lod take zaradit do SeznamUmistenychLodi ?
                            hodnota = false;
                            poctyULodi[velikost]++;
                            try {
                            screen.refresh();
                            } catch (IOException vyjimka) {
                                
                            }
                            break;
                        }
                        kolikrat++;
                    }
                    kolikratTvar++;
                }

            }
        }
        return true;

    }
    
    public boolean opakUmisteniLodi () throws IOException {
        for (int a = 0; a < 6; a++) {
            if (umisteniLodi()) {
                return true;
            }
        }
        Pomucky.vyzvaAVstup(0, 0, "Pocitac se az mockrat pokusil o umisteni lodi, restartuj program", graphics, screen);
        return false;
    }
}
