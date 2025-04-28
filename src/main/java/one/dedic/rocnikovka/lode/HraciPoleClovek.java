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
    UlozenaHra ulozenaHra;
    private static final int ZACATEK_HRACPOLE_X = 3;
    private static final int ZACATEK_HRACPOLE_Y = 2;

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

    

    public Lod vybraniLodi() throws IOException {
        boolean ok = true;
        StringBuilder sb = new StringBuilder();
        String vstup = "";

        String vystup = "";
        vycistiTerminal(text);
        text.setForegroundColor(TextColor.ANSI.DEFAULT);
        int velikostLode = 0;
        while (ok) {
            //TODO : reset
            /*
//            text.putString(0, 0, vystup);
            screen.refresh();
//            vstup = Pomucky.prectiVstup(0 + vystup.length(), 0, text, screen);
            vstup = Pomucky.prectiVstup(writer.getCursorPosition(), text, screen);
             */
            vstup = Pomucky.vyzvaAVstup(0, 0, "Napis velikost lodi (u - ulozeni, n - nahrani)", text, screen);
            int pozice = 0;
            int pocet = 0;
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
                    dopocitaniLodi();
                    if (hra.getStavPocitace() != null) {
                        this.ulozenaHra = hra;
                        return null;
                    }
                    // uLode = Pomucky.nahravani(false);
                } catch (IOException vyjimka) {
                    vycistiTerminal(text);
                    text.setForegroundColor(TextColor.ANSI.RED);
                    text.putString(0, 0, vyjimka.getMessage());
                    
                    continue;
                }
                // TODO REVIEW: Vypada to, ze bude nasledovat hlaska " pro pokracovani zmackni enter". Je to v poradku ?
            } else {
            vystup = "Mel by jsi az moc lodi ";
            sb.append(vystup);
            
            switch (vstup) {
                case "1": {
                    if (poctyULodi[0] < POCTYLODI[0]) {
                        pozice = SeznamLodi.POZICE_JEDNICKA;
                        pocet = SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA;
                        ok = false;
                        poctyULodi[0]++;
                        break;
                    } else {
                        // TODO REVIEW: Vysledny MOZNA bude: "Mel by jsi az moc lodi jedna pro pokracovani zmackni enter"
                        // a/ gramatika
                        // b/ opravdu je treba rozdelit hlasku chyby "Mel by jsi az moc lodi ... pro pokracovani zmackni enter"
                        //    do 2 prikazu "tak daleko" od sebe ?
                        sb.append("jedna");
                        break;
                    }
                }
                case "2": {
                    if (poctyULodi[1] < POCTYLODI[1]) {
                        pozice = SeznamLodi.POZICE_DVOJKA;
                        pocet = SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA;
                        ok = false;
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
                        ok = false;
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
                        ok = false;
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
                        ok = false;
                        poctyULodi[4]++;

                        break;
                    } else {
                        sb.append("pet");
                        break;
                    }

                }
                default: {
                    // TODO REVIEW: zde se napise:
                    // "Mel by jsi az moc lodi  pro pokracovani zmackni enter" - ale uzivatel
                    // napsal "necislo", takze ted nevi, co vlastne udelal spatne.
                    vycistiTerminal(text);
                    text.setForegroundColor(TextColor.ANSI.RED);
                    sb.append(" pro pokracovani zmackni enter");
                    // TODO REVIEW: pouzit vyzvaAVstup, zalamuje text.
                    text.putString(0, 0, sb.toString());
                    // TODO: text namisto graphics
                    prectiVstup(sb.length(), 0, graphics, screen);
                    text.setForegroundColor(TextColor.ANSI.DEFAULT);
                    // TODO REVIEW: break zpusobi preruse `switch' ale znovu vypise chybovou hlasku.
                    break;
                }
                
            

                    }   
                }
            }
        
            if (!ok) {
                // TODO REVIEW: tento kod se pousti tehdy a jen tehdy, kdyz vzapeti skonci
                // obklopujici cyklus `while'. Dalsi kod ve `while` jiz nenasleduje (cely zbytek je 
                // v else vetvi). Je prehlednejsi umistit kod BEZ podminky ZA konec cyklu:
                // while (podminka) {
                // ...
                //    if (!podminka) {
                //      .// kod
                //    }
                // }
                // ---->
                // while (podminka) {
                // ...
                // }
                // kod
                
                int delka = 0;
                int radek = 0;
                int poradi = 1;

                for (int a = 0; a < pocet; a++) {
                    if (delka * 2 + a * 2 + lode.get(a + pozice).getVizual()[0].length*2 >= 35) {
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
                // TODO REVIEW: hodnota 'sb' neni nijak pouzita pro vyzvu/vstup.
                sb.append("pro pokracovani zmackni enter");
                Pomucky.vyzvaAVstup(0, 0, vystup, text, screen);
                // TODO REVIEW: vyzvaAVstup ceka na vstup. Pak se ZNOVU ceka na vstup.                
                screen.refresh();
                prectiVstup(vystup.length(), 0, graphics, screen);
                text.setForegroundColor(TextColor.ANSI.DEFAULT);
                // TODO REVIEW: Pokud uzivatel zada "1" a ma moc lodi, a nasledne OPET "1"
                // bude se v `sb' dale pridavat " jedna" " jedna" ... a postupne se vypisovat vice a vice
            }
        }
        sb = new StringBuilder();
        ok = true;
        Pomucky.vycistiTerminal(0, 0, 2, 35, text);

        ok = true;
        Lod vybranaLod;
        int cislo = 0;
        while (ok) {
            // TODO REVIEW: pro vyzvu a nasledny vstup mame funkci, ta zaroven resi umisteni
            // kurzoru podle delky vyzvy v parametru. Prirazeni do pomocne promenne je pak zbytecne.
            vystup = "Vyber si tvar lode";
            text.putString(0, 0, vystup);
            screen.refresh();
            vstup = Pomucky.prectiVstup(new TerminalPosition(vystup.length(), 0), text, screen);
            // TODO REVIEW: Nevyzkouseny kod: parseInt muze hazet NumberFormatException pro ne-cisla,
            // neni osetreno.
            int tvarLode = Integer.parseInt(vstup) - 1;
            switch (velikostLode) {
                case 1: {
                    // TODO REVIEW: pocty lod konkretniho typu jsou PREDPOCITANE v Pomucky.POCTY_TYPU.
                    // Indexy 'bloku' tvaru lodi daneho typu jsou pripravene v Pomucky.ZACATKY  
                    if (tvarLode > SeznamLodi.POZICE_DVOJKA - SeznamLodi.POZICE_JEDNICKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_JEDNICKA;
                    break;
                }
                case 2: {
                    if (tvarLode > SeznamLodi.POZICE_TROJKA - SeznamLodi.POZICE_DVOJKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_DVOJKA;
                    break;
                }
                case 3: {
                    if (tvarLode > SeznamLodi.POZICE_CTYRKA - SeznamLodi.POZICE_TROJKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_TROJKA;
                    break;
                }
                case 4: {
                    if (tvarLode > SeznamLodi.POZICE_PETKA - SeznamLodi.POZICE_CTYRKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_CTYRKA;
                    break;
                }
                case 5: {
                    if (tvarLode >= lode.size() - SeznamLodi.POZICE_PETKA) {
                        sb.append("Vybral jsi lod, ktera neexistuje (vetsi cislo, nez je pocet lodi)");
                        break;
                    }
                    ok = false;
                    cislo = SeznamLodi.POZICE_PETKA;
                    break;
                }
            }
            if (ok) {
                vycistiTerminal(text);
                text.setForegroundColor(TextColor.ANSI.RED);
                sb.append(" pro pokracovani zmackni enter");
                // TODO REVIEW: 2x cteni vstupu.
                Pomucky.vyzvaAVstup(0, 0, sb.toString(), text, screen);
                prectiVstup(sb.length(), 0, graphics, screen);

                text.setForegroundColor(TextColor.ANSI.DEFAULT);
            }
        }
        Pomucky.vycistiTerminal(text);
        text.putString(0, 0, "Vybral jsi si lod" + vstup + ".");
        // TODO REVIEW: opakovany vypocet s Integer.parseInt
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

                // TODO REVIEW: hodnota vstup1 je uz PO odecteni 'a'. 
                if ((vstup1 - 'a') > 10 || (vstup1 - 97) < 0) {
                    sb.append("Mas moc velke/male cislo sloupce");
                }

                if (vstup2 - 1 > 10 || vstup2 - 1 < 0) {
                    // TODO REVIEW: V pripade spatneho zadani obou casti souradnic spatne
                    // bude hlaska "Mas moc velke/male cislo sloupceMas moc velke/male cislo radku"
                    sb.append("Mas moc velke/male cislo radku");
                }

                // TODO REVIEW: ani jednou nevyzkouseny kod: vystup je vzdy "", pripadna chyba
                // se akumuluje v "sb".
                if (!vystup.isEmpty()) {
                    vycistiTerminal(text);
                    text.setForegroundColor(TextColor.ANSI.RED);
                    sb.append(" pro pokracovani zmackni enter");
                    text.putString(0, 0, sb.toString());
                    prectiVstup(sb.length(), 0, graphics, screen);
                    text.setForegroundColor(TextColor.ANSI.DEFAULT);
                } else {
                    // TODO REVIEW: Opravdu se do sloupce zapise poradi sloupce 0-9 ?? 
                    radek = vstup2 - 1;
                    sloupec = vstup1;
                    break;
                }
            }
            sb = new StringBuilder();
            vycistiTerminal(text);
            Bunka[][] maska = maskaLodi(pLod);
            if (!Pomucky.prekryvani(pLod, uLode.hraciPole, sloupec, radek)) {
                vycistiTerminal(text);
                // TODO REIVEW: proc se tu pouziva "sb" pro postupne vytvareni hlaseni, kdyz 
                // tento blok osetruje zcela novou podminku ?
                sb.append("prekyvaji se ti lode/lod s okolim jine lode");
                text.setForegroundColor(TextColor.ANSI.RED);
                sb.append(" pro pokracovani zmackni enter");
                // TODO REVIEW: 2x cteni vstupu
                Pomucky.vyzvaAVstup(0, 0, sb.toString(), text, screen);
                prectiVstup(sb.length(), 0, graphics, screen);
                text.setForegroundColor(TextColor.ANSI.DEFAULT);
            } else {
                // TODO REVIEW: citelnejsi je finalni kopii a umisteni lode umistit ZA cyklus kontrolujici platnost vstupu.
                Pomucky.kopiePoleDoPole(sloupec - 1, radek - 1, maska, uLode.hraciPole);
                Pomucky.vytisknuti2DPole(uLode.hraciPole, hrac, false, ZACATEK_HRACPOLE_X, ZACATEK_HRACPOLE_Y);
                //TODO : pridat moznost rotace
                uLode.pridaniDoSeznamu(new UmistenaLod(pLod, sloupec, radek, 0));
                hodnota = false;
            }

        }
    }

    public void umisteniLodi() throws IOException {
        // TODO REVIEW: Obsahy poli NEJDE porovnavat pomoci ==, rovnitko porovna
        // zda jsou pole 'stejny objekt' (stejna reference) ! 
        while (!(Arrays.equals(poctyULodi, POCTYLODI) )) {
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
