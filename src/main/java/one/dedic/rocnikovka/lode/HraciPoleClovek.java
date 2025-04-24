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
import java.util.logging.Level;
import java.util.logging.Logger;
import static one.dedic.rocnikovka.lode.Pomucky.maskaLodi;
import static one.dedic.rocnikovka.lode.Pomucky.prectiVstup;
import static one.dedic.rocnikovka.lode.Pomucky.vycistiTerminal;
import static one.dedic.rocnikovka.lode.SeznamLodi.POCTYLODI;
import static one.dedic.rocnikovka.lode.SeznamLodi.lode;

/**
 *
 * @author aja
 */
public class HraciPoleClovek {

    private final TextGraphics graphics;
    private final TextGraphics hrac;
    private final TextGraphics hraciPole;
    private Screen screen;
    private Bunka[][] hracPole = new Bunka[10][10];
    private SeznamUmistenychLodi uLode = new SeznamUmistenychLodi();
    private int[] poctyULodi = {0, 0, 0, 0, 0};
    TextGraphics text;

    public HraciPoleClovek(TextGraphics graphics, Screen screen) {
        this.graphics = graphics;
        this.hrac = graphics.newTextGraphics(
                new TerminalPosition(0, 0), new TerminalSize(60, 50));
        this.hraciPole = hrac.newTextGraphics(new TerminalPosition(0, 0), new TerminalSize(24, 15));
        this.screen = screen;
        this.text = hrac.newTextGraphics(new TerminalPosition(25, 0), new TerminalSize(45, 50));
        ramecek();
    }

    public void ramecek() {

        for (int a = 0; a < 10; a++) {
            hraciPole.putString(a + 1 < 10 ? 1 : 0, a + 2, a + 1 + "");
        }
        for (int b = 0; b < 20; b += 2) {
            hraciPole.putString(b + 4, 0, Character.toString((char) (b / 2 + 'A')));
        }
        hraciPole.drawLine(2, 2, 2, 11, Symbols.SINGLE_LINE_VERTICAL);
        hraciPole.drawLine(23, 2, 23, 12, Symbols.SINGLE_LINE_VERTICAL);
        hraciPole.drawLine(3, 1, 22, 1, Symbols.SINGLE_LINE_HORIZONTAL);
        hraciPole.drawLine(3, 12, 22, 12, Symbols.SINGLE_LINE_HORIZONTAL);
        hraciPole.putString(2, 1, Character.toString(Symbols.SINGLE_LINE_TOP_LEFT_CORNER));
        hraciPole.putString(23, 1, Character.toString(Symbols.SINGLE_LINE_TOP_RIGHT_CORNER));
        hraciPole.putString(2, 12, Character.toString(Symbols.SINGLE_LINE_BOTTOM_LEFT_CORNER));
        hraciPole.putString(23, 12, Character.toString(Symbols.SINGLE_LINE_BOTTOM_RIGHT_CORNER));
    }

    private String vyzvaAVstup(int x, int y, String vyzva) throws IOException {
        TextGraphicsWriter writer = new TextGraphicsWriter(text);
        writer.setCursorPosition(new TerminalPosition(0, 0));
        writer.putString(vyzva);
        return Pomucky.prectiVstup(writer.getCursorPosition(), text, screen);
    }

    public Lod vybraniLodi() throws IOException {
        boolean hodnota = true;
        StringBuilder sb = new StringBuilder();
        String vstup = "";

        String vystup = "";
        vycistiTerminal(text);
        text.setForegroundColor(TextColor.ANSI.DEFAULT);
        int velikostLode = 0;
        while (hodnota) {
            vystup = "Napis velikost lodi (u - ulozeni, n - nahrani)";
            //TODO : reset
            /*
//            text.putString(0, 0, vystup);
            screen.refresh();
//            vstup = Pomucky.prectiVstup(0 + vystup.length(), 0, text, screen);
            vstup = Pomucky.prectiVstup(writer.getCursorPosition(), text, screen);
             */
            vstup = vyzvaAVstup(0, 0, vystup);
            if (vstup == "u") {
                Pomucky.ukladani(uLode, false);
            }
            if (vstup == "n") {
                try {
                    uLode = Pomucky.nahravani(false);
                } catch (IOException vyjimka) {
                    vycistiTerminal(text);
                    text.setForegroundColor(TextColor.ANSI.RED);
                    text.putString(0, 0, vyjimka.getMessage());

                }
                dopocitaniLodi();
            }
            vystup = "Mel by jsi az moc lodi ";
            sb.append(vystup);
            int pozice = 0;
            int pocet = 0;
            switch (vstup) {
                case "1": {
                    if (poctyULodi[0] < POCTYLODI[0]) {
                        pozice = SeznamLodi.POZICE_JEDNICKA;
                        pocet = SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA;
                        hodnota = false;
                        poctyULodi[0]++;
                        break;
                    } else {
                        sb.append("jedna");
                        break;
                    }
                }
                case "2": {
                    if (poctyULodi[1] < POCTYLODI[1]) {
                        pozice = SeznamLodi.POZICE_DVOJKA;
                        pocet = SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA;
                        hodnota = false;
                        poctyULodi[1]++;
                        break;
                    } else {
                        sb.append("dva");
                        break;
                    }
                }
                case "3": {
                    if (poctyULodi[2] < POCTYLODI[2]) {
                        pozice = SeznamLodi.POZICE_TROJKA;
                        pocet = SeznamLodi.POZICE_CTYRKA - SeznamLodi.POZICE_TROJKA;
                        hodnota = false;
                        poctyULodi[2]++;
                        break;

                    } else {
                        sb.append("tri");
                        break;
                    }
                }
                case "4": {
                    if (poctyULodi[3] < POCTYLODI[3]) {
                        pozice = SeznamLodi.POZICE_CTYRKA;
                        pocet = SeznamLodi.POZICE_PETKA - SeznamLodi.POZICE_CTYRKA;
                        hodnota = false;
                        poctyULodi[3]++;
                        break;

                    } else {
                        sb.append("ctyri");
                        break;
                    }
                }
                case "5": {
                    if (poctyULodi[4] < POCTYLODI[4]) {
                        pozice = SeznamLodi.POZICE_PETKA;
                        pocet = lode.size() - SeznamLodi.POZICE_PETKA;
                        hodnota = false;
                        poctyULodi[4]++;

                        break;
                    } else {
                        sb.append("pet");
                        break;
                    }

                }
                default: {
                    vycistiTerminal(text);
                    text.setForegroundColor(TextColor.ANSI.RED);
                    sb.append(" pro pokracovani zmackni enter");
                    text.putString(0, 0, sb.toString());
                    prectiVstup(sb.length(), 0, graphics, screen);
                    text.setForegroundColor(TextColor.ANSI.DEFAULT);
                    break;
                }

            }
            if (!hodnota) {
                int delka = 0;
                int radek = 0;
                int poradi = 1;

                for (int a = 0; a < pocet; a++) {
                    if (delka * 2 + a * 2 + lode.get(a + pozice).getVizual()[0].length >= 35) {
                        delka = 0;
                        radek++;
                    }
                    text.putString(2 + delka * 2 + a * 2, 2 + radek * 5, poradi + ".");
                    Pomucky.vytiskniLod(3 + delka * 2 + a * 2, 3 + radek * 5, lode.get(a + pozice).getVizual(), text, false);
                    delka += lode.get(a + pozice).getVizual()[0].length;
                    velikostLode = Integer.parseInt(vstup);
                    screen.refresh();
                    poradi++;

                }

            } else {

                text.setForegroundColor(TextColor.ANSI.RED);
                sb.append("pro pokracovani zmackni enter");
                vyzvaAVstup(0, 0, vystup);
                prectiVstup(vystup.length(), 0, graphics, screen);
                text.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
        }
        sb = new StringBuilder();
        hodnota = true;
        Pomucky.vycistiTerminal(0, 0, 2, 35, text);

        hodnota = true;
        Lod vybranaLod;
        int cislo = 0;
        while (hodnota) {
            vystup = "Vyber si tvar lode";
            text.putString(0, 0, vystup);
            screen.refresh();
            vstup = Pomucky.prectiVstup(new TerminalPosition(vystup.length(), 0), text, screen);
            int tvarLode = Integer.parseInt(vstup);
            switch (velikostLode) {
                case 1: {
                    if (tvarLode > SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    hodnota = false;
                    cislo = SeznamLodi.POZICE_JEDNICKA;
                    break;
                }
                case 2: {
                    if (tvarLode > SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    hodnota = false;
                    cislo = SeznamLodi.POZICE_DVOJKA;
                    break;
                }
                case 3: {
                    if (tvarLode > SeznamLodi.POZICE_CTYRKA - SeznamLodi.POZICE_TROJKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    hodnota = false;
                    cislo = SeznamLodi.POZICE_TROJKA;
                    break;
                }
                case 4: {
                    if (tvarLode > SeznamLodi.POZICE_PETKA - SeznamLodi.POZICE_CTYRKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    hodnota = false;
                    cislo = SeznamLodi.POZICE_CTYRKA;
                    break;
                }
                case 5: {
                    if (tvarLode >= lode.size() - SeznamLodi.POZICE_PETKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    hodnota = false;
                    cislo = SeznamLodi.POZICE_PETKA;
                    break;
                }
            }
            if (hodnota) {
                vycistiTerminal(text);
                text.setForegroundColor(TextColor.ANSI.RED);
                sb.append(" pro pokracovani zmackni enter");
                vyzvaAVstup(0, 0, sb.toString());
                prectiVstup(sb.length(), 0, graphics, screen);

                text.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
        }
        Pomucky.vycistiTerminal(text);
        text.putString(0, 0, "Vybral jsi si lod" + vstup + ".");
        Pomucky.vytiskniLod(0, 1, SeznamLodi.lode.get(cislo + Integer.parseInt(vstup) - 1).getVizual(), text, false);
        screen.refresh();
        vybranaLod = SeznamLodi.lode.get(Integer.parseInt(vstup));
        return vybranaLod;
    }

    public void umisteniLodi(Lod pLod) throws IOException {
        text.setForegroundColor(TextColor.ANSI.DEFAULT);
    boolean hodnota = true;
        int sloupec, radek = -1;
        vycistiTerminal(text);
        StringBuilder sb = new StringBuilder();
        while (hodnota) {
            while (true) {
                String vystup = "Napis pozici lodi (a1; A1)";
                text.putString(0, 0, vystup);
                String vstup = prectiVstup(new TerminalPosition(vystup.length(), 0), text, screen);
                vycistiTerminal(0, 0, 12, 35, text);
                char vstup1 = vstup.toLowerCase().charAt(0);
                vstup1 -= 'a';
                int vstup2 = Integer.parseInt(vstup.substring(1));
                text.setForegroundColor(TextColor.ANSI.RED);
                vystup = "";

                if ((vstup1 - 'a') > 10 || (vstup1 - 97) < 0) {
                    sb.append("Mas moc velke/male cislo sloupce");
                }

                if (vstup2 > 10 || vstup2 < 0) {
                    sb.append("Mas moc velke/male cislo radku");
                }

                if (!vystup.isEmpty()) {
                    vycistiTerminal(text);
                    text.setForegroundColor(TextColor.ANSI.RED);
                    sb.append(" pro pokracovani zmackni enter");
                    text.putString(0, 0, sb.toString());
                    prectiVstup(sb.length(), 0, graphics, screen);
                    text.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    radek = vstup2;
                    sloupec = vstup1;
                    break;
                }
            }
            sb = new StringBuilder();
            vycistiTerminal(text);
            Bunka[][] maska = maskaLodi(pLod);
            if (!Pomucky.prekryvani(pLod, uLode.hraciPole, sloupec, radek)) {
                vycistiTerminal(text);
                sb.append("prekyvaji se ti lode/lod s okolim jine lode");
                text.setForegroundColor(TextColor.ANSI.RED);
                sb.append(" pro pokracovani zmackni enter");
                vyzvaAVstup(0, 0, sb.toString());
                prectiVstup(sb.length(), 0, graphics, screen);
                text.setForegroundColor(TextColor.ANSI.DEFAULT);
            } else {
                Pomucky.kopiePoleDoPole(sloupec - 1, radek - 1, maska, uLode.hraciPole);

                //TODO : pridat moznost rotace
                uLode.pridaniDoSeznamu(new UmistenaLod(pLod, sloupec, radek, 0));
                hodnota = false;
            }

        }
    }

    public void umisteniLodi() throws IOException {
        while (!(poctyULodi == POCTYLODI)) {
            umisteniLodi(vybraniLodi());
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
    private static final int ZACATEK_HRACPOLE_X = 3;
    private static final int ZACATEK_HRACPOLE_Y = 4;
}
