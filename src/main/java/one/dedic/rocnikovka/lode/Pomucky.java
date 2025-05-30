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
import com.googlecode.lanterna.graphics.DoublePrintingTextGraphics;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.graphics.TextGraphicsWriter;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import static one.dedic.rocnikovka.lode.Bunka.LOD;
import static one.dedic.rocnikovka.lode.Bunka.POTOPENA;
import static one.dedic.rocnikovka.lode.Bunka.STRELENA;
import static one.dedic.rocnikovka.lode.Bunka.VODA;
import static one.dedic.rocnikovka.lode.Bunka.ZABRANE;

/**
 *
 * @author aja
 */
public class Pomucky {

    public static String prectiVstup(TerminalPosition pos, TextGraphics graphics, Screen screen) throws IOException {
        return prectiVstup(pos.getColumn(), pos.getRow(), graphics, screen);
    }

    public static String prectiVstup(int x, int y, TextGraphics graphics, Screen screen) throws IOException {

        String text = "";
        while (true) {
//         screen.setCursorPosition(new TerminalPosition(x + xr + text.length(), y + yr));
            screen.setCursorPosition(graphics.toScreenPosition(new TerminalPosition(x + text.length(), y)));
            screen.refresh();
            KeyStroke klavesa = screen.readInput();
            if (klavesa.getKeyType() == KeyType.CHARACTER) {
                text += klavesa.getCharacter().toString();
                graphics.putString(x, y, text);

            } else {
                switch (klavesa.getKeyType()) {
                    case EOF: {
                        System.exit(0);
                    }
                    case ENTER: {
                        return text;
                    }
                    case BACKSPACE: {
                        if (text.length() > 0) {
                            text = text.substring(0, text.length() - 1);
                            graphics.putString(x, y, text + " ");
                        }
                        break;

                    }
                    case ESCAPE: {
                        for (int a = 0; a < text.length(); a++) {
                            graphics.putString(x + a, y, " ");
                        }
                        return "";
                    }
                }
            }
        }

    }

    public static void vytiskniLod(int x, int y, Bunka[][] Lod, TextGraphics graphics, boolean hra) {
        TextGraphics plocha = graphics.newTextGraphics(new TerminalPosition(x, y),
                new TerminalSize(graphics.getSize().getColumns() - x, graphics.getSize().getRows() - y));
        TextGraphics dvojita = new DoublePrintingTextGraphics(plocha);
        for (int b = 0; b < Lod.length; b++) {
            for (int a = 0; a < Lod[b].length; a++) {
                switch (Lod[b][a]) {
                    case VODA: {
                        dvojita.putString(a, b, " ");
                        break;
                    }
                    case VEDLE: {
                        dvojita.putString(a, b, Character.toString(Symbols.BULLET));
                        break;
                    }
                    case LOD: {
                        dvojita.putString(a, b, Character.toString(Symbols.BLOCK_SOLID));
                        break;
                    }
                    case STRELENA: {
                        dvojita.putString(a, b, Character.toString(Symbols.BLOCK_MIDDLE));
                        break;
                    }
                    case POTOPENA: {
                        dvojita.putString(a, b, Character.toString(Symbols.BLOCK_SPARSE));
                    }
                    case ZABRANE: {
                        if (!hra) {
                            dvojita.putString(a, b, Character.toString(Symbols.BLOCK_DENSE));
                        }
                    }

                }

            }
        }
    }

    public static Bunka[][] kopiePole(Bunka[][] pole) {
        Bunka[][] novePole = new Bunka[pole.length][pole[0].length];
        for (int a = 0; a < pole.length; a++) {
            for (int b = 0; b < pole[a].length; b++) {
                novePole[a][b] = pole[a][b];
            }
        }
        return novePole;
    }

    public static Bunka[][] maskaLode(Lod lod) {
        Bunka[][] puvodni = lod.getVizual();
        Bunka[][] maska = new Bunka[puvodni.length + 2][puvodni[0].length + 2];
        ArrayList<Integer> chybejici = new ArrayList<>();
        for (int a = 0; a < puvodni.length; a++) {
            for (int b = 0; b < puvodni[a].length; b++) {
                if (puvodni[a][b] == LOD) {
                    Collections.addAll(chybejici, b, a);
                }
            }
        }
        while (true) {
            if (chybejici.isEmpty()) {
                break;
            }
            int x = chybejici.get(0) + 1;
            int y = chybejici.get(1) + 1;
            chybejici.remove(0);
            chybejici.remove(0);
            maska[y][x] = LOD;
            if (maska[y - 1][x - 1] != LOD) {
                maska[y - 1][x - 1] = ZABRANE;
            }
            if (maska[y - 1][x] != LOD) {
                maska[y - 1][x] = ZABRANE;
            }
            if (maska[y - 1][x + 1] != LOD) {
                maska[y - 1][x + 1] = ZABRANE;
            }
            if (maska[y][x - 1] != LOD) {
                maska[y][x - 1] = ZABRANE;
            }
            if (maska[y][x + 1] != LOD) {
                maska[y][x + 1] = ZABRANE;
            }
            if (maska[y + 1][x - 1] != LOD) {
                maska[y + 1][x - 1] = ZABRANE;
            }
            if (maska[y + 1][x] != LOD) {
                maska[y + 1][x] = ZABRANE;
            }
            if (maska[y + 1][x + 1] != LOD) {
                maska[y + 1][x + 1] = ZABRANE;
            }

        }
        return maska;
    }

    public static boolean prekryvani(Lod lod, Bunka[][] hraciPole, int x, int y) {
        Bunka[][] vLod = lod.getVizual();
        ArrayList<Integer> kde = new ArrayList<>();
        for (int a = 0; a < vLod.length; a++) {
            for (int b = 0; b < vLod[a].length; b++) {
                if (vLod[a][b] == LOD) {
                    Collections.addAll(kde, b, a);
                }
            }
        }
        while (true) {
            if (kde.isEmpty()) {
                return true;
            }
            int tx = kde.get(0) + x;
            if (tx > 10) {
                return false;
            }

            int ty = kde.get(1) + y;
            if (ty > 10) {
                return false;
            }
            kde.remove(0);
            kde.remove(0);
            if (hraciPole[ty][tx] == LOD || hraciPole[ty][tx] == ZABRANE) {
                return false;
            }
        }
    }

    public static void vycistiTerminal(int x, int y, int xv, int yv, TextGraphics text) {
        for (int a = y; a < y + yv; a++) {
            for (int b = x; b < x + xv; b++) {
                text.putString(a, b, " ");
            }
        }
    }

    public static void vycistiTerminal(TextGraphics text) {
        vycistiTerminal(0, 0, text.getSize().getColumns(), text.getSize().getRows(), text);
    }

    public static void kopiePoleDoPole(int x, int y, Bunka[][] maska, Bunka[][] hracPole) {
        for (int a = 0; a < maska.length; a++) {
            for (int b = 0; b < maska[a].length; b++) {
                if ((a + y) < 0 || (a + y) > 9) {
                    continue;
                }
                if ((b + x) < 0 || (b + x) > 9) {
                    continue;
                }
                hracPole[a + y][b + x] = maska[a][b];
            }
        }
    }

    public static void vytisknuti2DPole(Bunka[][] pole, TextGraphics graphics, boolean hra, int x, int y) {
        TextGraphics plocha = graphics.newTextGraphics(new TerminalPosition(x, y),
                new TerminalSize(graphics.getSize().getColumns() - x, graphics.getSize().getRows() - y));
        TextGraphics dvojita = new DoublePrintingTextGraphics(plocha);
        for (int a = 0; a < pole.length; a++) {
            for (int b = 0; b < pole[a].length; b++) {
                Bunka obsah = pole[a][b];
                if (obsah == null) {
                    obsah = Bunka.VODA;
                }
                switch (obsah) {
                    case VODA: {
                        dvojita.putString(b, a, " ");
                        break;
                    }
                    case VEDLE: {
                        dvojita.putString(b, a, Character.toString(Symbols.BULLET));
                        break;
                    }
                    case LOD: {
                        dvojita.setForegroundColor(TextColor.ANSI.GREEN);
                        dvojita.putString(b, a, Character.toString(Symbols.BLOCK_SOLID));
                        dvojita.setForegroundColor(TextColor.ANSI.DEFAULT);
                        break;
                    }
                    case STRELENA: {
                        dvojita.setForegroundColor(TextColor.ANSI.BLUE);
                        dvojita.putString(b, a, Character.toString(Symbols.BLOCK_MIDDLE));
                        dvojita.setForegroundColor(TextColor.ANSI.DEFAULT);
                        break;
                    }
                    case POTOPENA: {
                        dvojita.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
                        dvojita.putString(b, a, Character.toString(Symbols.BLOCK_SPARSE));
                        dvojita.setForegroundColor(TextColor.ANSI.DEFAULT);
                        break;
                    }
                    case ZABRANE: {
                        if (!hra) {
                            dvojita.setForegroundColor(TextColor.ANSI.YELLOW_BRIGHT);
                            dvojita.putString(b, a, Character.toString(Symbols.BLOCK_DENSE));
                            dvojita.setForegroundColor(TextColor.ANSI.DEFAULT);
                            break;
                        }
                        break;

                    }

                }
            }
        }
    }

    public static void ukladani(SeznamUmistenychLodi sULodi, boolean hra) throws IOException {
        Properties prop = new Properties();
        ukladani2("clovek", prop, sULodi, hra);
        try (FileWriter writer = new FileWriter(new File("ulozenahra.properties"))) {
            prop.store(writer, "");
        }
    }

    public static void ukladani(UlozenaHra hra) throws IOException {
        Properties prop = new Properties();
        ukladani2("clovek", prop, hra.getClovek(), true);
        ukladani2("pocitac", prop, hra.getPocitac(), true);
        String prefix = "pocitac.dalsiStrely";
        StringBuilder sb = new StringBuilder();
        for (int a = 0; a < hra.stavPocitace.getPotopeni().size(); a++) {
            if (a > 0) {
                sb.append(";");
            }
            sb.append(hra.stavPocitace.getPotopeni().get(a));

        }
        prop.setProperty(prefix, sb.toString());
        try (FileWriter writer = new FileWriter(new File("ulozenahra.properties"))) {
            prop.store(writer, "");
        }
    }

    private static void ukladani2(String prefixSeznamu, Properties prop, SeznamUmistenychLodi sULodi, boolean hra) throws IOException {
        for (int poradiL = 0; poradiL < sULodi.getSeznamULodi().size(); poradiL++) {
            UmistenaLod lod = sULodi.getSeznamULodi().get(poradiL);
            String prefix = prefixSeznamu + ".lod" + Integer.toString(poradiL + 1) + ".";
            prop.setProperty(prefix + "id", Integer.toString(lod.getId()));
            prop.setProperty(prefix + "x", Integer.toString(lod.getX()));
            prop.setProperty(prefix + "y", Integer.toString(lod.getY()));
            prop.setProperty(prefix + "rot", Integer.toString(lod.getRotace()));
        }
        if (hra) {
            for (int poradiP = 0; poradiP < 10; poradiP++) {
                String prefix = prefixSeznamu + ".radek" + Integer.toString(poradiP + 1);
                StringBuilder sb = new StringBuilder();
                for (int b = 0; b < 10; b++) {
                    if (b > 0) {
                        sb.append(',');
                    }
                    if (sULodi.hraciPole[poradiP][b] == null) {
                        sb.append(VODA);
                        continue;
                    }
                    sb.append(sULodi.hraciPole[poradiP][b].name());
                }
                prop.setProperty(prefix, sb.toString());
            }
        }
    }

    public static UlozenaHra nahravani2() throws IOException {
        Properties prop = new Properties();
        SeznamUmistenychLodi clovekLode;
        SeznamUmistenychLodi pocitacLode;
        UlozenaHra hra = new UlozenaHra();
        try (FileReader reader = new FileReader(new File("ulozenahra.properties"))) {
            prop.load(reader);
            clovekLode = nahravaniSeznamu("clovek", prop);
            hra.setClovek(clovekLode);
            pocitacLode = nahravaniSeznamu("pocitac", prop);
            if (pocitacLode.getSeznamULodi().isEmpty()) {
                return hra;
            }
            hra.setPocitac(pocitacLode);
            String obsah = prop.getProperty("pocitac.dalsiStrely");
            StavPocitace stavP = new StavPocitace(clovekLode.hraciPole);
            if (obsah != null && !obsah.isBlank()) {
                ArrayList<Integer> strely = new ArrayList();
                for (String souradnice : obsah.split(";")) {
                    strely.add(Integer.parseInt(souradnice));
                }
                stavP.setPotopeni(strely);
            }
            hra.setStavPocitace(stavP);
        }
        return hra;
    }

    public static SeznamUmistenychLodi nahravani(boolean hra) throws IOException {
        Properties prop = new Properties();
        try (FileReader reader = new FileReader(new File("ulozenahra.properties"))) {
            prop.load(reader);
            return nahravaniSeznamu("clovek", prop);
        }
    }

    private static SeznamUmistenychLodi nahravaniSeznamu(String prefixSeznamu, Properties prop) throws IOException {
        SeznamUmistenychLodi sULodi = new SeznamUmistenychLodi();
        for (int poradi = 0;; poradi++) {
            String prefix = prefixSeznamu + ".lod" + Integer.toString(poradi + 1) + ".";
            if (prop.getProperty(prefix + "id") == null) {
                break;
            }
            int id = Integer.parseInt(prop.getProperty(prefix + "id"));
            int x = Integer.parseInt(prop.getProperty(prefix + "x"));
            int y = Integer.parseInt(prop.getProperty(prefix + "y"));
            int rotace = Integer.parseInt(prop.getProperty(prefix + "rot"));
            Lod lod = SeznamLodi.najdiId(id);
            if (lod == null) {
                throw new IOException("Spatna data");
            }
            sULodi.pridaniDoSeznamu(new UmistenaLod(lod, x, y, rotace));
            kopiePoleDoPole(x, y, maskaLode(lod), sULodi.hraciPole);

        }
        for (int a = 0; a < 10; a++) {
            String klic = prefixSeznamu + ".radek" + Integer.toString(a + 1);
            Bunka[][] pole = sULodi.hraciPole;
            String obsah = prop.getProperty(klic);
            if (obsah == null) {
                return sULodi;
            }
            String[] hodnoty = obsah.split(",");
            for (int b = 0; b < 10; b++) {
                pole[a][b] = Bunka.valueOf(hodnoty[b]);
            }
        }
        return sULodi;
    }

    public static String vyzvaAVstup(int x, int y, String vyzva, TextGraphics graphics, Screen screen) throws IOException {
        TextGraphicsWriter writer = new TextGraphicsWriter(graphics);
        writer.setCursorPosition(new TerminalPosition(0, 0));
        writer.putString(vyzva + ": ");
        return Pomucky.prectiVstup(writer.getCursorPosition(), graphics, screen);
    }

    static void pridejNoveSouradnice(ArrayList<Integer> seznam, int y, int x) {
        for (int b = 0; b < seznam.size(); b += 2) {
            if (seznam.get(b) == y && seznam.get(b + 1) == x) {
                return;
            }
        }
        seznam.add(y);
        seznam.add(x);
    }

    public static boolean potopena(int sloupec, int radek, Bunka[][] hraciPole, Bunka[][] zasahy) {
        ArrayList<Integer> seznam = new ArrayList<>();
        Collections.addAll(seznam, radek, sloupec);
        for (int a = 0; a < seznam.size(); a += 2) {
            int y = seznam.get(a);
            int x = seznam.get(a + 1);
            if (y - 1 >= 0) {
                if (hraciPole[y - 1][x] == LOD) {
                    return false;
                }
                if (hraciPole[y - 1][x] == STRELENA) {
                    pridejNoveSouradnice(seznam, y - 1, x);
                }

            }
            if (y + 1 < 10) {
                if (hraciPole[y + 1][x] == LOD) {
                    return false;
                }
                if (hraciPole[y + 1][x] == STRELENA) {
                    pridejNoveSouradnice(seznam, y + 1, x);
                }

            }
            if (x - 1 >= 0) {
                if (hraciPole[y][x - 1] == LOD) {
                    return false;
                }
                if (hraciPole[y][x - 1] == STRELENA) {
                    pridejNoveSouradnice(seznam, y, x - 1);
                }
            }
            if (x + 1 < 10) {
                if (hraciPole[y][x + 1] == LOD) {
                    return false;
                }
                if (hraciPole[y][x + 1] == STRELENA) {
                    pridejNoveSouradnice(seznam, y, x + 1);
                }

            }
        }
        while (!seznam.isEmpty()) {
            int y = seznam.remove(0);
            int x = seznam.remove(0);
            if (zasahy != null) {
                zasahy[y][x] = POTOPENA;
            }
            hraciPole[y][x] = POTOPENA;
        }
        return true;
    }

    public static boolean konecHry(Bunka[][] hraciPole) {
        for (int a = 0; a < hraciPole.length; a++) {
            for (int b = 0; b < hraciPole[a].length; b++) {
                if (hraciPole[a][b] == LOD) {
                    return false;
                }
            }
        }
        return true;
    }

    public static int najdiDvojici(int prvni, int druhe, ArrayList<Integer> list) {
        for (int a = 0; a < list.size(); a += 2) {
            if (list.get(a).equals(prvni) && list.get(a + 1).equals(druhe)) {
                return a;
            }
        }
        return -1;
    }

    public static ArrayList<Integer> obteckujLod(int radek, int sloupec, Bunka[][] hraciPole) {
        ArrayList<Integer> seznam = new ArrayList<>();
        ArrayList<Integer> obteckovani = new ArrayList<>();
        Collections.addAll(seznam, radek, sloupec);
        for (int a = 0; a < seznam.size(); a += 2) {
            int x = seznam.get(a);
            int y = seznam.get(a + 1);
            if (y - 1 >= 0) {
                if (hraciPole[y - 1][x].jeLod()) {
                    pridejNoveSouradnice(seznam, x, y - 1);
                } else {
                    pridejNoveSouradnice(obteckovani, x, y - 1);
                    hraciPole[y - 1][x] = ZABRANE;
                }
                if (x - 1 >= 0) {
                    if (hraciPole[y - 1][x - 1].jeLod()) {
                        pridejNoveSouradnice(seznam, x - 1, y - 1);
                    } else {
                        pridejNoveSouradnice(obteckovani, x - 1, y - 1);
                        hraciPole[y - 1][x - 1] = ZABRANE;
                    }
                }
                if (x + 1 < 10) {
                    if (hraciPole[y - 1][x + 1].jeLod()) {
                        pridejNoveSouradnice(seznam, x + 1, y - 1);
                    } else {
                        pridejNoveSouradnice(obteckovani, x + 1, y - 1);
                        hraciPole[y - 1][x + 1] = ZABRANE;
                    }
                }
            }
            if (x - 1 >= 0) {
                if (hraciPole[y][x - 1].jeLod()) {
                    pridejNoveSouradnice(seznam, x - 1, y);
                } else {
                    pridejNoveSouradnice(obteckovani, x - 1, y);
                    hraciPole[y][x - 1] = ZABRANE;
                }
                if (y + 1 > 10) {
                    if (hraciPole[y + 1][x - 1].jeLod()) {
                        pridejNoveSouradnice(seznam, x - 1, y + 1);
                    } else {
                        pridejNoveSouradnice(obteckovani, x - 1, y + 1);
                        hraciPole[y + 1][x - 1] = ZABRANE;
                    }
                }
            }
            if (x + 1 < 10) {
                if (hraciPole[y][x + 1].jeLod()) {
                    pridejNoveSouradnice(seznam, x + 1, y);
                } else {
                    pridejNoveSouradnice(obteckovani, x + 1, y);
                    hraciPole[y][x + 1] = ZABRANE;
                }
                if (y + 1 < 10) {
                    if (hraciPole[y + 1][x + 1].jeLod()) {
                        pridejNoveSouradnice(seznam, x + 1, y + 1);
                    } else {
                        pridejNoveSouradnice(obteckovani, x + 1, y + 1);
                        hraciPole[y + 1][x + 1] = ZABRANE;
                    }
                }
            }
            if (y + 1 < 10) {
                if (hraciPole[y + 1][x].jeLod()) {
                    pridejNoveSouradnice(seznam, x, y + 1);
                } else {
                    pridejNoveSouradnice(obteckovani, x, y + 1);
                    hraciPole[y + 1][x] = ZABRANE;
                }
            }
        }
        return obteckovani;
    }

    public static void vypisError(String chyba, TextGraphics graphics, Screen screen) throws IOException {
        vycistiTerminal(graphics);
        graphics.setForegroundColor(TextColor.ANSI.RED_BRIGHT);
        Pomucky.vyzvaAVstup(0, 0, chyba + ", pro pokracovani zmackni enter", graphics, screen);
        graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
    }
    
}
