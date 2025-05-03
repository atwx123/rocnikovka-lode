/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package one.dedic.rocnikovka.lode;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import static one.dedic.rocnikovka.lode.Bunka.STRELENA;
import static one.dedic.rocnikovka.lode.Bunka.VEDLE;
import static one.dedic.rocnikovka.lode.Bunka.ZABRANE;
import static one.dedic.rocnikovka.lode.Pomucky.najdiDvojici;

/**
 *
 * @author aja
 */
public class StavPocitace {

    ArrayList<Integer> kamStrilet;
    ArrayList<Integer> potopeni = new ArrayList<>();
    Random random = new Random();
    Bunka[][] clovek;
    TextGraphics graphics;
    Screen screen;

    public void setGraphics(TextGraphics graphics) {
        this.graphics = graphics;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public StavPocitace(Bunka[][] clovek) {
        this.clovek = clovek;
        this.kamStrilet = new ArrayList<>();
        for (int a = 0; a < clovek.length; a++) {
            for (int b = 0; b < clovek[a].length; b++) {
                if (clovek[a][b] == null) {
                    continue;
                }
                if (clovek[a][b].jeNestrelene()) {
                    Collections.addAll(this.kamStrilet, b, a);
                }

            }
        }
    }

    public ArrayList<Integer> getPotopeni() {
        return potopeni;
    }

    public void setPotopeni(ArrayList<Integer> potopeni) {
        this.potopeni = potopeni;
    }

    public boolean strileni() {
        boolean ok = true;
        while (ok) {
            int x;
            int y;
            if (potopeni.isEmpty()) {
                int cislo = random.nextInt(kamStrilet.size() - 1);
                if (cislo % 2 != 0) {
                    cislo--;
                }
                x = kamStrilet.remove(cislo);
                y = kamStrilet.remove(cislo);
            } else {
                int cislo = random.nextInt(potopeni.size());
                if (cislo % 2 != 0) {
                    cislo--;
                }

                x = potopeni.remove(cislo);
                y = potopeni.remove(cislo);
                cislo = najdiDvojici(x, y, kamStrilet);
                if (cislo == -1) {
                    continue;
                }
                kamStrilet.remove(cislo);
                kamStrilet.remove(cislo);
            }
            Bunka policko = clovek[y][x];

            switch (policko) {
                case VODA: {
                    clovek[y][x] = VEDLE;
                    ok = false;
                    break;
                }
                case LOD: {
                    if (!Pomucky.potopena(x, y, clovek)) {
                        if ((x - 1) >= 0) {
                            Collections.addAll(potopeni, x - 1, y);
                        }
                        if ((x + 1) < 10) {
                            Collections.addAll(potopeni, x + 1, y);
                        }
                        if ((y - 1) >= 0) {
                            Collections.addAll(potopeni, x, y - 1);
                        }
                        if ((y + 1) < 10) {
                            Collections.addAll(potopeni, x, y + 1);
                        }
                        clovek[y][x] = STRELENA;
                    } else {
                        ArrayList<Integer> tecky = Pomucky.obteckujLod(x, y, clovek);
                        while (!tecky.isEmpty()) {
                            int sloupec = tecky.remove(0);
                            int radek = tecky.remove(0);
                            int vymazat = najdiDvojici(sloupec, radek, kamStrilet);
                            kamStrilet.remove(vymazat);
                            kamStrilet.remove(vymazat);
                        }
                        if (Pomucky.konecHry(clovek)) {
                            return true;
                        }
                    }
                    break;

                }
            }
        }
        return false;
    }
}
