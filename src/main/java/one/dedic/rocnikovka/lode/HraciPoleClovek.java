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
        //TODO : reset
        text.putString(0, 0, vystup);
        
        screen.refresh();
        boolean hodnota = true;
        StringBuilder sb = new StringBuilder();
        String vstup = Pomucky.prectiVstup(0 + vystup.length(), 0, 25, 0, text, screen);
        vystup = "Mel by jsi az moc lodi ";
        sb.append(vystup);
        String velikostLode = "";
        while (hodnota) {
        int pozice = 0;
        int pocet = 0;
        switch (vstup) {
            case "1" : {
                if (jedna < 4) {
                    pozice = SeznamLodi.POZICE_JEDNICKA;
                    pocet = SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA;
                    hodnota = false;
                    jedna++;
                    break;
                } else {
                   sb.append("jedna");
                   break;
                }
            }
            case "2" : {
                if (dva < 3) {
                    pozice = SeznamLodi.POZICE_DVOJKA;
                    pocet = SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA;
                    hodnota = false;
                    dva++;
                    break;
                } else {
                    sb.append("dva");
                    break;
                }
            }
            case "3" : {
                if (tri < 2) {
                    pozice = SeznamLodi.POZICE_TROJKA;
                    pocet = SeznamLodi.POZICE_CTYRKA - SeznamLodi.POZICE_TROJKA;
                    hodnota = false;
                    tri++;
                    break;
                     
                } else {
                    sb.append("tri");
                    break;
                }
            }
            case "4" : {
                if (ctyri < 1) {
                    pozice = SeznamLodi.POZICE_CTYRKA;
                    pocet = SeznamLodi.POZICE_PETKA - SeznamLodi.POZICE_CTYRKA;
                    hodnota = false;
                    ctyri++;
                    break;
                    
                } else {
                    sb.append("ctyri");
                    break; 
                }
            }
            case "5": {
                if (pet >= 1) {
                    pozice = SeznamLodi.POZICE_PETKA;
                    pocet = lode.size() - SeznamLodi.POZICE_PETKA;
                    hodnota = false;
                    pet++;
                    
                     break;
                } else {
                    sb.append("pet");
                    break;
                }  
                
                  
            }
            default : {
                text.putString(0, 0, "Neplatny vstup");
                break;
            }
          
        }
        if (!hodnota) {
                    int delka = 0;
                    int radek = 0;
                    int poradi = 1;
                    
                    for (int a = 0; a < pocet; a++) {
                        if (delka >= 35) {
                            delka = 0;
                            radek++;
                        }
                        text.putString(radek * 5, delka, poradi + ".");
                        Pomucky.vytiskniLod(1 + radek * 5, 1 + delka, lode.get(a + pozice).getVizual(), graphics);
                        delka += lode.get(a).getVizual()[0].length;
                        velikostLode = vstup;
                        screen.refresh();
                        poradi ++;
                       
                    } 
                            
                            
    } else {
            text.putString(0, 0, vystup);
        }
        }
    hodnota = true;
    text.putString(0, 35, " ");
    vystup = "Vyber si tvar lode";
    text.putString(0, 0, vystup);
    hodnota = true;
    Lod vybranaLod;
    int cislo = 0;
    while (hodnota) {
    screen.refresh();
    vstup = Pomucky.prectiVstup(0 + vystup.length(), 0, 0, 25, graphics, screen);
    int tvarLode = Integer.parseInt(vstup);
// TODO : nahradit switxh + dodelat pole 
    switch (velikostLode) {
        case "1" : {
            if (tvarLode > SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA) {
                vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                break;
            }
            hodnota = false;
            cislo = SeznamLodi.POZICE_JEDNICKA;
            break;
        }
        case "2": {
           if (tvarLode > SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA) {
               vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                break;
            }
           hodnota = false;
           cislo =  SeznamLodi.POZICE_DVOJKA;
           break;
        }
        case "3": {
             if (tvarLode > SeznamLodi.POZICE_CTYRKA - SeznamLodi.POZICE_TROJKA) {
                vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                break;
            }
             hodnota = false;
            cislo = SeznamLodi.POZICE_TROJKA;
            break;
        }
        case "4" : {
             if (tvarLode > SeznamLodi.POZICE_PETKA - SeznamLodi.POZICE_CTYRKA) {
                vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                break;
            }
             hodnota = false;
            cislo = SeznamLodi.POZICE_CTYRKA;
            break;
        }
        case "5" : {
             if (tvarLode >= lode.size() - SeznamLodi.POZICE_PETKA) {
                vystup = "Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)";
                break;
            }
            hodnota = false;
            cislo = SeznamLodi.POZICE_PETKA;
            break;
        }
    }
       
        text.putString(0, 0, vystup);
    }
    //TODO : vycistit na zacatku kazde fce terminal
            Pomucky.vycistiTerminal(text);
            text.putString(0, 0, "Vybral jsi si lod" + vstup + ".");
            Pomucky.vytiskniLod(0, 1, SeznamLodi.lode.get(cislo + Integer.parseInt(vstup) - 1).getVizual(), graphics);
            screen.refresh();
            vybranaLod = SeznamLodi.lode.get(Integer.parseInt(vstup));
            return vybranaLod;
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
                //TODO : zkontrolovat zaporne indexy
                //TODO : zkontrolovat, jestli lod "nevyleza" z pole
                Pomucky.kopiePoleDoPole(sloupec -1 , radek - 1, maska, hracPole);
                
                //TODO : pridat moznost rotace
                SeznamUmistenychLodi.pridaniDoSeznamu(new UmistenaLod(pLod, sloupec, radek, 0));
                hodnota = false;
            }
            
        }
    }
    
    public void umisteniLodi () throws IOException {
        //TODO : podminka na umisteni VSECH lodi
        umisteniLodi(vybraniLodi());
    }
    //TODO : ukazani, kde by byla lod
    private static final int ZACATEK_HRACPOLE_X = 3;
    private static final int ZACATEK_HRACPOLE_Y = 4;
}






    