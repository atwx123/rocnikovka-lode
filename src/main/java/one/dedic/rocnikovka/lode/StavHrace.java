/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package one.dedic.rocnikovka.lode;

import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;
import static one.dedic.rocnikovka.lode.Bunka.POTOPENA;
import static one.dedic.rocnikovka.lode.Bunka.STRELENA;
import static one.dedic.rocnikovka.lode.Bunka.VEDLE;
import static one.dedic.rocnikovka.lode.Bunka.VODA;
import static one.dedic.rocnikovka.lode.Pomucky.prectiVstup;

/**
 *
 * @author aja
 */
public class StavHrace {

    Bunka[][] pocitac;
    TextGraphics graphics;
    Screen screen;

    public StavHrace(Bunka[][] pocitac) {
        this.pocitac = pocitac;
    }

    public void setGraphics(TextGraphics graphics) {
        this.graphics = graphics;
    }

    public void setScreen(Screen screen) {
        this.screen = screen;
    }
    
    public void strileni() throws IOException {
        int radek = -1;
        int sloupec = -1;
        StringBuilder sb = new StringBuilder();
        boolean hodnota = true;
        while (hodnota) {
            // TODO REVIEW: cely blok kodu, ktery cte radek:sloupec je (funkcne) totozny
            // s blokem v HraciPoleClovek vc. osetreni chyb atd td.
            while (true) {
                String vystup = "Napis pozici lodi (a1; A1)";
                String vstup = prectiVstup(vystup.length(), 0, graphics, screen);
                Pomucky.vycistiTerminal(0, 0, 12, 35, graphics);
                char vstup1 = vstup.toLowerCase().charAt(0);
                int vstup2 = Integer.parseInt(vstup.substring(1));
                graphics.setForegroundColor(TextColor.ANSI.RED);
                vystup = "";

                // TODO REVIEW: konzistence: v prvni casti podminky 'a', v druhe casti 97 (kod 'a').
                if ((vstup1 - 'a') > 10 || (vstup1 - 97) < 0) {
                    sb.append("Mas moc velke/male cislo sloupce");
                }

                if (vstup2 > 10 || vstup2 < 0) {
                    sb.append("Mas moc velke/male cislo radku");
                }

                if (!vystup.isEmpty()) {
                    graphics.setForegroundColor(TextColor.ANSI.RED);
                    sb.append(" pro pokracovani zmackni enter");
                    graphics.putString(0, 0, sb.toString());
                    prectiVstup(sb.length(), 0, graphics, screen);
                    graphics.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    radek = vstup2;
                    sloupec = vstup1;
                    break;
                }
            }
            Bunka policko = pocitac[radek][sloupec];
            if (policko == null) {
                policko = VODA;
            }

            switch (policko) {
                case ZABRANE: {

                }
                case VODA: {
                    // TODO REVIEW: pozor, zapis do LOKALNI prommene policko, ne do hraciho pole !
                    policko = VEDLE;
                    Pomucky.vycistiTerminal(graphics);
                    graphics.putString(0, 0, "Vedle");
                    screen.refresh();
                    hodnota = false;
                    break;
                }
                case VEDLE: {
                    Pomucky.vycistiTerminal(graphics);
                    Pomucky.vyzvaAVstup(sloupec, radek, "Vytrelil jsi na pozici, kam jsi uz strilel, vedle", graphics, screen);
                    screen.refresh();
                    hodnota = false;
                    break;
                }
                case LOD: {
                    Pomucky.vycistiTerminal(graphics);
                    if (Pomucky.potopena(sloupec, radek, pocitac)) {
                        policko = POTOPENA;
                        graphics.putString(0, 0, "Zasah, potopena, hrajes dal");
                    } else {
                        policko = STRELENA;
                        graphics.putString(0, 0, "Zasah, hrajes dal");
                    }
                    // TODO REVIEW: pri potopeni se oznaci jako 'potopena' pouze JEDINE policko (to naposledy zasazene) ?
                    screen.refresh();
                    break;
                }
                case STRELENA:
                case POTOPENA: {
                    Pomucky.vycistiTerminal(graphics);
                    Pomucky.vyzvaAVstup(sloupec, radek, "Vystrelil jsi na pozici, kde uz jsou trosky lodi, vedle", graphics, screen);
                    screen.refresh();
                    hodnota = false;
                    break;
                }

            }
        }
        if (Pomucky.konecHry(pocitac)) {
            graphics.setForegroundColor(TextColor.ANSI.CYAN);
            Pomucky.vyzvaAVstup(0, 0, "VYHRAL JSI, gratulace (pro ukonceni stiskni enter)", graphics, screen);
            
        }
    }

}
