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
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import static one.dedic.rocnikovka.lode.Pomucky.maskaLodi;
import static one.dedic.rocnikovka.lode.Pomucky.prectiVstup;
import static one.dedic.rocnikovka.lode.Pomucky.vycistiTerminal;
import static one.dedic.rocnikovka.lode.SeznamLodi.lode;

/**
 *
 * @author aja
 */
public class HraciPoleClovek  {
    private final TextGraphics graphics;
    private final TextGraphics hrac;
    private final TextGraphics hraciPole;
    private Screen screen;
    private Bunka[][] hracPole = new Bunka[10][10];

    public HraciPoleClovek(TextGraphics graphics, Screen screen) {
        this.graphics = graphics;
        this.hrac = graphics.newTextGraphics(
            new TerminalPosition(0, 0), new TerminalSize(60, 50));
        this.hraciPole = hrac.newTextGraphics(new TerminalPosition(0,0), new TerminalSize (24, 15));
        this.screen = screen;
        ramecek();
    }
    
    public void ramecek () {
        
        for (int a = 0; a < 10; a++) {
            hraciPole.putString(a + 1 < 10 ? 1 : 0, a + 2, a + 1 + "");
        }
        for (int b = 0; b < 20; b+=2) {
            hraciPole.putString(b + 4, 0, Character.toString((char)(b/2 + 'A'))); 
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
    
    public Lod vybraniLodi () throws IOException{
        
        TextGraphics text = hrac.newTextGraphics(new TerminalPosition(25, 0), new TerminalSize(45, 50));
        int pet = 0;
        int ctyri = 0;
        int tri = 0;
        int dva = 0;
        int jedna = 0;
        String vystup = "";
        text.fillRectangle(new TerminalPosition(25, 0), text.getSize(), ' ');
             // TODO: vycistit celou plochu, pouzit text.getSize() pro hranice 
        vystup = "Napis velikost lodi";
        text.putString(0, 0, vystup);
        
        screen.refresh();
        boolean hodnota = true;
        String vstup = Pomucky.prectiVstup(0 + vystup.length(), 0, 25, 0, text, screen);
        vystup = "Mel by jsi az moc lodi ";
        String velikostLode = "";
        while (hodnota) {
        switch (vstup) {
            case "1" : {
                if (jedna < 4) {
                    text.putString(0, 0, "1.");
                    Pomucky.vytiskniLod(1, 1, lode.get(POZICE_JEDNICKA).getVizual(), graphics);
                    jedna++;
                    velikostLode = vstup;
                    screen.refresh();
                    hodnota = false;
                    break;
                } else {
                   text.putString(0, 0, vystup +"jedna");
                   vystup = "Napis velikost lodi";
                   break;
                }
            }
            case "2" : {
                if (dva < 3) {
                    text.putString(0, 0, "1.");
                    Pomucky.vytiskniLod(1, 1, lode.get(POZICE_DVOJKA).getVizual(), graphics);
                    dva++;
                    velikostLode = vstup;
                    screen.refresh();
                    hodnota = false;
                    break;
                } else {
                    text.putString(0, 0, vystup+"dva");
                    vystup = "Napis velikost lodi";
                    break;
                }
            }
            case "3" : {
                if (tri < 2) {
                    int delka = 0;
                    int radek = 0;
                    int poradi = 1;
                    for (int a = POZICE_TROJKA; a <= POZICE_TROJKA + POCET_TROJKA; a++) {
                         if (delka >= 25) {
                            delka = 0;
                            radek ++;
                            
                        }
                       
                        text.putString(radek * 5, delka, poradi + "."); 
                        Pomucky.vytiskniLod(1 + radek * 5, 1 + delka, lode.get(a).getVizual(), graphics);
                        delka += lode.get(a).getVizual()[0].length;
                        tri++;
                        velikostLode = vstup;
                        screen.refresh();
                        poradi++;
                        
                    }   
                    hodnota = false;
                    break;
                     
                } else {
                    text.putString(0, 0, vystup + "tri");
                    vystup = "Napis velikost lodi";
                    break;
                }
            }
            case "4" : {
                if (ctyri < 1) {
                    int delka = 0;
                    int radek = 0;
                    int poradi = 1;
                    for (int a = POZICE_CTYRKA; a <= POZICE_CTYRKA + POCET_CTYRKA; a++) {
                         if (delka >= 25) {
                            delka = 0;
                            radek ++;
                        }
                        text.putString(radek * 5, delka, poradi + "."); 
                        Pomucky.vytiskniLod(1 + radek * 5, 1 + delka, lode.get(a).getVizual(), graphics);
                        delka += lode.get(a).getVizual()[0].length;
                        ctyri++;
                        velikostLode = vstup;
                        screen.refresh();
                        poradi++;
                        
                    }
                    hodnota = false;
                    break;
                    
                } else {
                   text.putString(0, 0, vystup + "ctyri");
                   vystup = "Napis velikost lodi";
                    break; 
                }
            }
            case "5": {
                if (pet >= 1) {
                    int delka = 0;
                    int radek = 0;
                    int poradi = 1;
                    for (int a = POZICE_PETKA; a <= POZICE_PETKA + POCET_PETKA; a++) {
                        if (delka >= 25) {
                            delka = 0;
                            radek++;
                        }
                        text.putString(radek * 5, delka, poradi + ".");
                        Pomucky.vytiskniLod(1 + radek * 5, 1 + delka, lode.get(a).getVizual(), graphics);
                        delka += lode.get(a).getVizual()[0].length;
                        pet++;
                        velikostLode = vstup;
                        screen.refresh();
                        poradi ++;
                       
                    }
                    hodnota = false;
                     break;
                } else {
                    text.putString(0, 0, vystup + "pet");
                    vystup = "Napis velikost lodi";
                    break;
                }  
                
                  
            }
            default : {
                text.putString(0, 0, "Neplatny vstup");
                break;
            }
          
        }
    }
    hodnota = true;
    text.putString(0, 35, " ");
    vystup = "Vyber si tvar lode";
    text.putString(0, 0, vystup);
    while (true) {
    screen.refresh();
    vstup = Pomucky.prectiVstup(0 + vystup.length(), 0, 0, 25, graphics, screen);
    Lod vybranaLod;
    int cislo = 0;

    switch (velikostLode) {
        case "1" : {
            cislo = POZICE_JEDNICKA;
        }
        case "2": {
           cislo = POZICE_DVOJKA;
        }
        case "3": {
            cislo = POZICE_TROJKA;
        }
        case "4" : {
            cislo = POZICE_CTYRKA;
        }
        case "5" : {
            cislo = POZICE_PETKA;
        }
    }
        for (int a = 0; a < 13; a++) {
            text.putString(a, 35, " ");
        }
        text.putString(0, 0, "Vybral jsi si lod" + vstup + ".");
        Pomucky.vytiskniLod(0, 1, SeznamLodi.lode.get(cislo + Integer.parseInt(vstup) - 1).getVizual(), graphics);
        screen.refresh();
        vybranaLod = SeznamLodi.lode.get(2 + Integer.parseInt(vstup));
        return vybranaLod;
    }

    }
    public void umisteniLodi (Lod pLod) throws IOException {

        TextGraphics text = hrac.newTextGraphics(new TerminalPosition(25, 0), new TerminalSize(12, 35));
        text.setForegroundColor(TextColor.ANSI.DEFAULT);
        boolean hodnota = true;
        int sloupec, radek = -1;
        while (hodnota) {
            while (true) {
                String vystup = "Napis pozici lodi (a1; A1)";
                String vstup = prectiVstup (vystup.length(), 0, 0, 25, graphics, screen);
                vycistiTerminal(0, 0, 12, 35, text);
                char vstup1 = vstup.toLowerCase().charAt(0);
                int vstup2 =Integer.parseInt(vstup.substring(1));
                text.setForegroundColor(TextColor.ANSI.RED);
                vystup = "";
                
                if ((vstup1 - 'a') > 10 || (vstup1 - 97) < 0) {
                    vystup = "Mas moc velke/male cislo sloupce";
                }
                
                if (vstup2 > 10 || vstup2 < 0) {
                    vystup = "Mas moc velke/male cislo radku";
                }
                
                //TODO: schvalit prijem erroru klavesou
                //TODO: ruzne barvy erroru, textu
                //TODO: naformatovat
                if (!vystup.isEmpty()) {
                    text.putString(0, 0, vystup);
                    prectiVstup(vystup.length(), 0, 0, 25, graphics, screen);
                } else {
                    radek = vstup2;
                    sloupec = vstup1;
                    break;
                }
            }
            vycistiTerminal(0, 0, 12, 35, text);
            Bunka[][] maska = maskaLodi(pLod);
            if (!Pomucky.prekryvani(pLod, hracPole, sloupec, radek)) {
                text.putString(0, 0, "prekyvaji se ti lode/lod s okolim jine lode");
            } else {
                Pomucky.kopiePoleDoPole(sloupec, radek, maska, hracPole);
                
                //TODO : pridat moznost rotace
                SeznamUmistenychLodi.setSeznamULodi(new UmistenaLod(pLod, sloupec, radek, 0));
                hodnota = false;
            }
            
        }
    }
    //TODO : ukazani, kde by byla lod
    private static final int ZACATEK_HRACPOLE_X = 3;
    private static final int ZACATEK_HRACPOLE_Y = 4;
    private static final int POCET_PETKA = 5;
    private static final int POCET_CTYRKA = 3;
    private static final int POCET_TROJKA = 2;
    private static final int POCET_DVOJKA = 1;
    private static final int POCET_JEDNICKA = 1;
    private static final int POZICE_PETKA = 8;
    private static final int POZICE_CTYRKA = 4;
    private static final int POZICE_TROJKA = 2;
    private static final int POZICE_DVOJKA = 1;
    private static final int POZICE_JEDNICKA = 0; 
}






    