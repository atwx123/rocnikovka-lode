/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package one.dedic.rocnikovka.lode;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;
import static one.dedic.rocnikovka.lode.Bunka.POTOPENA;
import static one.dedic.rocnikovka.lode.Bunka.STRELENA;
import static one.dedic.rocnikovka.lode.Bunka.VEDLE;
import static one.dedic.rocnikovka.lode.Bunka.VODA;
import static one.dedic.rocnikovka.lode.Bunka.ZABRANE;
import static one.dedic.rocnikovka.lode.Pomucky.prectiVstup;

/**
 *
 * @author aja
 */
public class StavHrace {

    Bunka[][] pocitac;
    Bunka[][] clovekH;
    Bunka[][] zasahy = new Bunka[10][10];
    TextGraphics graphics;
    Screen screen;
    HraciPoleClovek clovek;
    TextGraphics hGraphics;
    TextGraphics hPole;
    TextGraphics hText;
    TextGraphics pGraphics;
    UlozenaHra uHra;

    public StavHrace(Bunka[][] pocitac, TextGraphics graphics, Screen screen, HraciPoleClovek clovek, UlozenaHra uHra) {
        this.pocitac = pocitac;
        this.graphics = graphics;
        this.screen = screen;
        this.clovek = clovek;
        this.hGraphics = graphics.newTextGraphics(
                new TerminalPosition(0, 0), new TerminalSize(70, 50));
        this.hPole = hGraphics.newTextGraphics(new TerminalPosition(0, 0), new TerminalSize(24, 50));
        this.hText = hGraphics.newTextGraphics(new TerminalPosition(25, 0), new TerminalSize(45, 50));
        this.pGraphics = graphics.newTextGraphics(new TerminalPosition(70, 0), new TerminalSize(50, 50));
        this.clovekH = clovek.getHracPole();
        for (int a = 0; a < pocitac.length; a++) {
            for (int b = 0; b < pocitac[a].length; b++) {
                if (pocitac[a][b] == null) {
                    pocitac[a][b] = VODA;
                }
                if (pocitac[a][b].jeNestrelene() || pocitac[a][b] == ZABRANE) {
                    zasahy[a][b] = VODA;
                } else {
                    zasahy[a][b] = pocitac[a][b];
                }
            }
        }
        this.uHra = uHra;
    }

    public void setGraphics(TextGraphics graphics) {
        this.graphics = graphics;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }

    public void vytiskniStav(int xp, int yp) throws IOException {
        clovek.ramecek(hPole);
        clovek.ramecek(pGraphics);
        Pomucky.vytisknuti2DPole(zasahy, pGraphics, true, clovek.ZACATEK_HRACPOLE_X, clovek.ZACATEK_HRACPOLE_Y);
        Pomucky.vytisknuti2DPole(clovekH, hPole, true, clovek.ZACATEK_HRACPOLE_X, clovek.ZACATEK_HRACPOLE_Y);
        screen.refresh();
        if (xp != -1 || yp != -1) {
            switch (clovek.getHracPole()[yp][xp]) {
                case VEDLE: {
                    Pomucky.vycistiTerminal(hText);
                    Pomucky.vyzvaAVstup(0, 0, "Pocitac trefil rybu, vedle (enter)", hText, screen);
                    screen.refresh();
                    break;
                }
                case STRELENA: {
                    Pomucky.vycistiTerminal(hText);
                    Pomucky.vyzvaAVstup(0, 0, "Pocitac trefil lod, ale nepotopil (enter)", hText, screen);
                    screen.refresh();
                    break;
                }
                case POTOPENA: {
                    Pomucky.vycistiTerminal(hText);
                    Pomucky.vyzvaAVstup(0, 0, "Pocitac trefil lod, potopil :( (enter)", hText, screen);
                    screen.refresh();
                    break;
                }
            }
        }

    }

    public boolean strileni() throws IOException {
        int radek = -1;
        int sloupec = -1;
        StringBuilder sb = new StringBuilder();
        boolean hodnota = true;
        while (hodnota) {
            vytiskniStav(-1, -1);
            while (true) {
                Pomucky.vycistiTerminal(hText);
                String vstup = Pomucky.vyzvaAVstup(0, 0, "cil strelby (a1; A1)", hText, screen);
                if (vstup.equals("")) {
                    Pomucky.vypisError("Neplatny vstup", hText, screen);
                    continue;
                }
                if (vstup.equals("u")) {
                    uHra.setClovek(clovek.getuLode());
                    Pomucky.vycistiTerminal(hText);
                    Pomucky.vyzvaAVstup(0, 0, "hra byla ulozena", hText, screen);
                    continue;
                }
                if (vstup.equals("*")) {
                    vytiskniKonecHry();
                    Pomucky.vycistiTerminal(hText);
                    Pomucky.vyzvaAVstup(0, 0, "Vzdal jsi hru. Stiskni enter", hText, screen);
                    System.exit(0);
                }
                char vstup1 = vstup.toLowerCase().charAt(0);
                vstup1 -= 'a';
                int vstup2 = -1;
                try {
                    vstup2 = Integer.parseInt(vstup.substring(1)) - 1;
                } catch (NumberFormatException vyjimka) {
                    Pomucky.vypisError("Neplatny vstup (neni cislo)", hText, screen);
                    continue;
                }
                String vystup = "";

                if (vstup1 > 10 || vstup1 < 0) {
                    vystup = ("Mas moc velke/male cislo sloupce");
                }

                if (vstup2 > 10 || vstup2 < 0) {
                    vystup = ("Mas moc velke/male cislo radku");
                }

                if (!vystup.isEmpty()) {
                    Pomucky.vypisError(vystup, hText, screen);
                    continue;
                } else {
                    radek = vstup2;
                    sloupec = vstup1;
                    break;
                }
            }
            Bunka policko = pocitac[radek][sloupec];
            if (policko == null) {
                pocitac[radek][sloupec] = VODA;
            }

            switch (policko) {
                case ZABRANE: {

                }
                case VODA: {
                    pocitac[radek][sloupec] = zasahy[radek][sloupec] = VEDLE;
                    Pomucky.vycistiTerminal(hText);
                    vytiskniStav(-1, -1);
                    Pomucky.vyzvaAVstup(0, 0, "Vedle", hText, screen);
                    hodnota = false;
                    break;
                }
                case VEDLE: {
                    Pomucky.vycistiTerminal(hText);
                    vytiskniStav(-1, -1);
                    Pomucky.vyzvaAVstup(0, 0, "Vytrelil jsi na pozici, kam jsi uz strilel, vedle", hText, screen);
                    hodnota = false;
                    break;
                }
                case LOD: {
                    Pomucky.vycistiTerminal(hText);
                    pocitac[radek][sloupec] = zasahy[radek][sloupec] = STRELENA;
                    if (Pomucky.potopena(sloupec, radek, pocitac, zasahy)) {
                        vytiskniStav(-1, -1);
                        Pomucky.vyzvaAVstup(0, 0, "Zasah, potopena, hrajes dal", hText, screen);
                    } else {
                        Pomucky.vycistiTerminal(hText);
                        vytiskniStav(-1, -1);
                        Pomucky.vyzvaAVstup(0, 0, "Zasah, hrajes dal", hText, screen);
                    }
                    if (Pomucky.konecHry(pocitac)) {
                        return true;

                    }
                    break;
                }
                case STRELENA:
                case POTOPENA: {
                    Pomucky.vycistiTerminal(hText);
                    vytiskniStav(-1, -1);
                    Pomucky.vyzvaAVstup(sloupec, radek, "Vystrelil jsi na pozici, kde uz jsou trosky lodi, vedle", hText, screen);
                    hodnota = false;
                    break;
                }

            }
        }

        return false;
    }

    public void vytiskniKonecHry() throws IOException {
        for (int a = 0; a < zasahy.length; a++) {
            for (int b = 0; b < zasahy[a].length; b++) {
                if (zasahy[a][b] != null && zasahy[a][b] != VODA) {
                    pocitac[a][b] = zasahy[a][b];
                }
            }
        }
        Pomucky.vytisknuti2DPole(pocitac, pGraphics, true, clovek.ZACATEK_HRACPOLE_X, clovek.ZACATEK_HRACPOLE_Y);
        screen.refresh();
    }

}
