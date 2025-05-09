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

import com.googlecode.lanterna.Symbols;
import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextGraphicsWriter;
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import static one.dedic.rocnikovka.lode.Pomucky.prectiVstup;
import static one.dedic.rocnikovka.lode.Pomucky.vycistiTerminal;
import static one.dedic.rocnikovka.lode.SeznamLodi.POCTYLODI;
import static one.dedic.rocnikovka.lode.SeznamLodi.POCTYTYPU;
import static one.dedic.rocnikovka.lode.SeznamLodi.lode;
import static one.dedic.rocnikovka.lode.Pomucky.maskaLode;

/**
 *
 * @author aja
 */
public class HraciPoleClovek {

    private final TextGraphics graphics;
    private final TextGraphics hrac;
    private final TextGraphics hraciPole;
    private Screen screen;
    private Bunka[][] hracPole;
    private SeznamUmistenychLodi uLode = new SeznamUmistenychLodi();
    private int[] poctyULodi = {0, 0, 0, 0, 0};
    TextGraphics text;
    UlozenaHra ulozenaHra;
    public static final int ZACATEK_HRACPOLE_X = 3;
    public static final int ZACATEK_HRACPOLE_Y = 2;

    public UlozenaHra getUlozenaHra() {
        return ulozenaHra;
    }

    public HraciPoleClovek(TextGraphics graphics, Screen screen) {
        this.graphics = graphics;
        this.hrac = graphics.newTextGraphics(
                new TerminalPosition(0, 0), new TerminalSize(60, 50));
        this.hraciPole = hrac.newTextGraphics(new TerminalPosition(0, 0), new TerminalSize(24, 15));
        this.screen = screen;
        this.text = hrac.newTextGraphics(new TerminalPosition(25, 0), new TerminalSize(45, 50));
        hracPole = uLode.hraciPole;
        ramecek(hraciPole);
    }

    public TextGraphics getHrac() {
        return hrac;
    }

    public TextGraphics getHraciPole() {
        return hraciPole;
    }

    public void ramecek(TextGraphics graphics) {

        for (int a = 0; a < 10; a++) {
            graphics.putString(a + 1 < 10 ? 1 : 0, a + 2, a + 1 + "");
        }
        for (int b = 0; b < 20; b += 2) {
            graphics.putString(b + 4, 0, Character.toString((char) (b / 2 + 'A')));
        }
        graphics.drawLine(2, 2, 2, 11, Symbols.SINGLE_LINE_VERTICAL);
        graphics.drawLine(23, 2, 23, 12, Symbols.SINGLE_LINE_VERTICAL);
        graphics.drawLine(3, 1, 22, 1, Symbols.SINGLE_LINE_HORIZONTAL);
        graphics.drawLine(3, 12, 22, 12, Symbols.SINGLE_LINE_HORIZONTAL);
        graphics.putString(2, 1, Character.toString(Symbols.SINGLE_LINE_TOP_LEFT_CORNER));
        graphics.putString(23, 1, Character.toString(Symbols.SINGLE_LINE_TOP_RIGHT_CORNER));
        graphics.putString(2, 12, Character.toString(Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER));
        graphics.putString(23, 12, Character.toString(Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER));
    }

    public Lod vybraniLodi() throws IOException {
        boolean ok = true;
        StringBuilder sb = new StringBuilder();
        String vstup = "";

        String vystup = "";
        vycistiTerminal(text);
        text.setForegroundColor(TextColor.ANSI.DEFAULT);
        int velikostLode = 0;
        int pocet = 0;
        int pozice = 0;
        while (ok) {
            text.setForegroundColor(TextColor.ANSI.DEFAULT);
            vycistiTerminal(text);
            screen.refresh();
            //TODO : reset
            vstup = Pomucky.vyzvaAVstup(0, 0, "Napis velikost lodi (u - ulozeni, n - nahrani)", text, screen);
            pozice = 0;
            pocet = 0;
            if (vstup.equals("")) {
                Pomucky.vypisError("neplatny vstup", text, screen);
                continue;
            }
            if (vstup.equals("u")) {
                Pomucky.ukladani(uLode, false);
                Pomucky.vycistiTerminal(text);
                Pomucky.vyzvaAVstup(0, 0, "Hra byla ulozena", text, screen);
                continue;
            } else {

                if (vstup.equals("n")) {
                    try {
                        UlozenaHra hra = Pomucky.nahravani2();
                        uLode = hra.getClovek();
                        hracPole = uLode.hraciPole;
                        dopocitaniLodi();
                        Pomucky.vytisknuti2DPole(hracPole, hraciPole, false, ZACATEK_HRACPOLE_X, ZACATEK_HRACPOLE_Y);
                        this.ulozenaHra = hra;
                        screen.refresh();
                        return null;
                    } catch (IOException vyjimka) {
                        vycistiTerminal(text);
                        text.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
                        text.putString(0, 0, vyjimka.getMessage());

                        continue;
                    }
                } else {
                    try {
                        velikostLode = Integer.parseInt(vstup);
                    } catch (NumberFormatException vyjimka) {
                        Pomucky.vypisError("neplatny vstup (neni cislo)", text, screen);
                        continue;
                    }

                    switch (vstup) {
                        case "1": {
                            if (poctyULodi[0] < POCTYLODI[0]) {
                                pozice = SeznamLodi.POZICE_JEDNICKA;
                                pocet = SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA;
                                ok = false;
                                poctyULodi[0]++;
                                continue;
                            } else {
                                vystup = "jedna";
                                break;
                            }
                        }
                        case "2": {
                            if (poctyULodi[1] < POCTYLODI[1]) {
                                pozice = SeznamLodi.POZICE_DVOJKA;
                                pocet = SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA;
                                ok = false;
                                poctyULodi[1]++;
                                continue;
                            } else {
                                vystup = "dva";
                                break;
                            }
                        }
                        case "3": {
                            if (poctyULodi[2] < POCTYLODI[2]) {
                                pozice = SeznamLodi.POZICE_TROJKA;
                                pocet = SeznamLodi.POZICE_CTYRKA - SeznamLodi.POZICE_TROJKA;
                                ok = false;
                                poctyULodi[2]++;
                                continue;
                            } else {
                                vystup = "tri";
                                break;
                            }
                        }
                        case "4": {
                            if (poctyULodi[3] < POCTYLODI[3]) {
                                pozice = SeznamLodi.POZICE_CTYRKA;
                                pocet = SeznamLodi.POZICE_PETKA - SeznamLodi.POZICE_CTYRKA;
                                ok = false;
                                poctyULodi[3]++;
                                continue;
                            } else {
                                vystup = "ctyri";
                                break;
                            }
                        }
                        case "5": {
                            if (poctyULodi[4] < POCTYLODI[4]) {
                                pozice = SeznamLodi.POZICE_PETKA;
                                pocet = lode.size() - SeznamLodi.POZICE_PETKA;
                                ok = false;
                                poctyULodi[4]++;
                                continue;
                            } else {
                                vystup = "pet";
                                break;
                            }

                        }
                        default: {
                            Pomucky.vypisError("neplatna velikost lodi (1-5)", text, screen);
                            continue;
                        }

                    }
                }
            }
            Pomucky.vypisError("Mel bys az moc lodi velikosti " + vystup, text, screen);
            vystup = "";

        }
        velikostLode = Integer.parseInt(vstup);
        vycistiTerminal(text);
        int delka = 0;
        int radek = 0;
        int poradi = 1;

        sb = new StringBuilder();
        ok = true;
        Pomucky.vycistiTerminal(0, 0, 2, 35, text);

        ok = true;
        Lod vybranaLod;
        int cislo = 0;
        int poziceLodi = 0;
        while (ok) {
            vycistiTerminal(text);
            text.setForegroundColor(TextColor.ANSI.DEFAULT);
            for (int a = 0; a < pocet; a++) {
                if (delka * 2 + a * 2 + lode.get(a + pozice).getVizual()[0].length * 2 >= 35) {
                    delka = 0;
                    radek++;
                }
                text.putString(2 + delka * 2 + a * 2, 2 + radek * 5, poradi + ".");
                Pomucky.vytiskniLod(3 + delka * 2 + a * 2, 3 + radek * 5, lode.get(a + pozice).getVizual(), text, false);
                delka += lode.get(a + pozice).getVizual()[0].length;

                screen.refresh();
                poradi++;

            }
            delka = 0;
            radek = 0;
            poradi = 1;

            vstup = Pomucky.vyzvaAVstup(0, 0, "Vyber si tvar lode", text, screen);
            if (vstup.equals("")) {
                Pomucky.vypisError("neplatny vstup", text, screen);
                continue;
            }
            int tvarLode = -1;
            try {
                tvarLode = Integer.parseInt(vstup) - 1;
            } catch (NumberFormatException vyjimka) {
                Pomucky.vypisError("Neplatny vstup", text, screen);
                continue;
            }

            switch (velikostLode) {
                case 1: {
                    if (tvarLode >= POCTYTYPU[0]) {
                        vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_JEDNICKA;
                    break;
                }
                case 2: {
                    if (tvarLode >= POCTYTYPU[1]) {
                        vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_DVOJKA;
                    break;
                }
                case 3: {
                    if (tvarLode >= POCTYTYPU[2]) {
                        vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_TROJKA;
                    break;
                }
                case 4: {
                    if (tvarLode >= POCTYTYPU[3]) {
                        vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_CTYRKA;
                    break;
                }
                case 5: {
                    if (tvarLode >= POCTYTYPU[4]) {
                        vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_PETKA;
                    break;
                }
            }
            if (ok) {
                Pomucky.vypisError(vystup, text, screen);
            }
            try {
                poziceLodi = cislo + Integer.parseInt(vstup) - 1;
            } catch (NumberFormatException vyjimka) {
                Pomucky.vypisError("neplatny tvar lodi (neni cislo)", text, screen);
                ok = true;
                continue;
            }
        }

        text.putString(0, 0, "Vybral jsi si lod" + vstup + ".");
        Pomucky.vytiskniLod(0, 1, SeznamLodi.lode.get(cislo + Integer.parseInt(vstup) - 1).getVizual(), text, false);
        screen.refresh();
        vybranaLod = SeznamLodi.lode.get(Integer.parseInt(vstup) - 1 + cislo);
        return vybranaLod;
    }

    public Bunka[][] getHracPole() {
        return hracPole;
    }

    public SeznamUmistenychLodi getuLode() {
        return uLode;
    }

    public void umisteniLodi(Lod pLod) throws IOException {

        text.setForegroundColor(TextColor.ANSI.DEFAULT);
        boolean ok = true;
        int sloupec = -1;
        int radek = -1;
        vycistiTerminal(text);
        StringBuilder sb = new StringBuilder();
        Bunka[][] maska = maskaLode(pLod);
        while (ok) {

            while (true) {
                vycistiTerminal(text);
                int vstup2 = 0;
                String vystup = "";
                String vstup = Pomucky.vyzvaAVstup(0, 0, "Napis pozici lodi (a1; A1)", text, screen);
                vycistiTerminal(0, 0, 12, 35, text);
                if (vstup.equals("")) {
                    Pomucky.vypisError("neplatna souradnice", text, screen);
                    continue;
                }
                char vstup1 = vstup.toLowerCase().charAt(0);
                vstup1 -= 'a';
                try {
                    vstup2 = Integer.parseInt(vstup.substring(1));
                } catch (NumberFormatException vyjimka) {
                    Pomucky.vypisError("neplatna souradnice radku (neni cislo)", text, screen);
                    continue;
                }

                vystup = "";

                if ((vstup1 + pLod.getVizual()[0].length) > 10 || (vstup1) < 0) {
                    vystup = ("Mas moc velke/male cislo sloupce");
                }

                if ((vstup2 - 1 + pLod.getVizual().length) > 10 || vstup2 - 1 < 0) {
                    vystup = ("Mas moc velke/male cislo radku");
                }
                if (!vystup.isEmpty()) {
                    Pomucky.vypisError(vystup, text, screen);
                } else {
                    sloupec = vstup1;
                    radek = vstup2 - 1;
                    break;
                }
            }
            sb = new StringBuilder();
            vycistiTerminal(text);
            maska = maskaLode(pLod);
            if (!Pomucky.prekryvani(pLod, uLode.hraciPole, sloupec, radek)) {
                Pomucky.vypisError("prekyvaji se ti lode/lod s okolim jine lode", text, screen);
                continue;
            } else {
                break;
            }

        }
        Pomucky.kopiePoleDoPole(sloupec - 1, radek - 1, maska, uLode.hraciPole);
        Pomucky.vytisknuti2DPole(uLode.hraciPole, hrac, false, ZACATEK_HRACPOLE_X, ZACATEK_HRACPOLE_Y);
        //TODO : pridat moznost rotace
        uLode.pridaniDoSeznamu(new UmistenaLod(pLod, sloupec, radek, 0));
        ok = false;
    }

    public void umisteniLodi() throws IOException {
        while (!(Arrays.equals(poctyULodi, POCTYLODI))) {
            Lod l = vybraniLodi();
            if (l != null) {
                umisteniLodi(l);
            }
        }
    }

    public void dopocitaniLodi() {
        for (int a = 0; a < poctyULodi.length; a++) {
            poctyULodi[a] = 0;
        }
        for (UmistenaLod lod : uLode.getSeznamULodi()) {
            poctyULodi[lod.getVelikost() - 1]++;
        }
    }
    //TODO : ukazani, kde by byla lod

}
