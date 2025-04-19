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
import com.googlecode.lanterna.graphics.DoublePrintingTextGraphics;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.input.KeyStroke;
import com.googlecode.lanterna.input.KeyType;
import com.googlecode.lanterna.screen.Screen;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import static one.dedic.rocnikovka.lode.Bunka.LOD;
import static one.dedic.rocnikovka.lode.Bunka.ZABRANE;

/**
 *
 * @author aja
 */
public class Pomucky {
   

    public static String prectiVstup (int x, int y, int xr, int yr, TextGraphics graphics, Screen screen) throws IOException {
        
        String text = "";
        while (true) {
         screen.setCursorPosition(new TerminalPosition(x + xr + text.length(), y + yr));
        KeyStroke klavesa = screen.readInput();
        if (klavesa.getKeyType() == KeyType.CHARACTER) {
            text +=  klavesa.getCharacter().toString();
            graphics.putString(x, y, text);
            
        } else {
           switch (klavesa.getKeyType()) {
               case ENTER: {
                   return text;
               }
               case BACKSPACE: {
                  text = text.substring(0, text.length()-1);
                  graphics.putString(x, y, text + " ");
                  break;
               }
               case ESCAPE : {
                   for (int a = 0; a < text.length(); a++) {
                    graphics.putString(x + a, y, " ");
                    }
                   return null;
               }
           }
        }
        }
        
        
        
    }
    public static void vytiskniLod (int x, int y, Bunka[][] Lod, TextGraphics graphics) {
        TextGraphics plocha = graphics.newTextGraphics(new TerminalPosition(x, y), 
                new TerminalSize(graphics.getSize().getColumns() - x, graphics.getSize().getRows() - y));
        TextGraphics dvojita = new DoublePrintingTextGraphics(plocha);
        for (int a = 0; a < Lod.length; a++) {
            for (int b = 0; b < Lod[a].length; b++) {
                switch (Lod[a][b]) {
                    case VODA: {
                        dvojita.putString(a, b, " ");
                        break;
                    }
                    case VEDLE : {
                        dvojita.putString(a, b, Character.toString(Symbols.BULLET));
                        break;
                    }
                    case LOD : {
                        dvojita.putString(a, b, Character.toString(Symbols.BLOCK_SOLID));
                        break;
                    }
                    case STRELENA : {
                        dvojita.putString(a, b, Character.toString(Symbols.BLOCK_MIDDLE));
                        break;
                    }
                    case POTOPENA : {
                        dvojita.putString(a, b, Character.toString(Symbols.BLOCK_SPARSE));
                    }
                    //TODO: case Zarezervovana + parametr behem hry/umistovani -> neni videt/je videt
                        
                }
                
            }
        }
    }
    
    public static Bunka[][] kopiePole (Bunka[][] pole) {
        Bunka[][] novePole = new Bunka[pole.length][pole[0].length];
        for (int a = 0; a < pole.length; a++) {
            for (int b = 0; b < pole[a].length; a++) {
                novePole[a][b] = pole [a][b];
            }
        }
        return novePole;
    }
    
    public static Bunka[][] maskaLodi (Lod lod) {
        Bunka[][] puvodni = lod.getVizual();
        Bunka[][] maska = new Bunka[puvodni.length+2][puvodni[0].length+2];
        ArrayList <Integer> chybejici = new ArrayList<Integer>();
        for (int a = 0; a < puvodni.length; a++) {
            for (int b = 0; b < puvodni[a].length; b++) {
                if (puvodni[a][b] == LOD) {
                    Collections.addAll(chybejici, a, b);
                }
            }
        }
        while (true) {
            if (chybejici.isEmpty()) {
                break;
            }
            int x = chybejici.get(0)+1;
            int y = chybejici.get(1)+1;
            chybejici.remove(0);
            chybejici.remove(0);
            maska[x][y] = LOD;
            if(maska[x-1][y-1] != LOD) {
               maska[x-1][y-1] = ZABRANE;
            }
            if(maska[x-1][y] != LOD) {
               maska[x-1][y] = ZABRANE;
            }
            if(maska[x-1][y+1] != LOD) {
               maska[x-1][y+1] = ZABRANE;
            }
            if(maska[x][y-1] != LOD) {
               maska[x][y-1] = ZABRANE;
            }
            if(maska[x][y+1] != LOD) {
               maska[x][y+1] = ZABRANE;
            }
             if(maska[x+1][y-1] != LOD) {
               maska[x+1][y-1] = ZABRANE;
            }
             if(maska[x+1][y] != LOD) {
               maska[x+1][y] = ZABRANE;
            }
             if(maska[x+1][y+1] != LOD) {
               maska[x+1][y+1] = ZABRANE;
            }
            
        }
        return maska;
    }
    
    public static boolean prekryvani (Lod lod, Bunka[][] hraciPole, int x, int y) {
        Bunka[][] vLod = lod.getVizual();
        ArrayList<Integer> kde = new ArrayList<>();
        for (int a = 0; a <vLod.length; a++) {
            for (int b = 0; b <vLod.length; b++) {
                if (vLod[a][b] == LOD) {
                    Collections.addAll(kde, a, b);
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
            if (hraciPole[tx][ty] == LOD || hraciPole[tx][ty] == ZABRANE) {
                return false;
            }
        }
    }
    public static void vycistiTerminal (int x, int y, int xv, int yv, TextGraphics text) {
        for (int a = y; a < y+yv; a++) {
            for (int b = x; b < x + xv; b++) {
            text.putString(a, b , " ");
            //TODO : nahradit manualni cisteni terminalu fci
            } 
        }
    }
    public static void vycistiTerminal (TextGraphics text) {
        vycistiTerminal(0, 0, text.getSize().getColumns(), text.getSize().getRows(), text);
    }
    public static void kopiePoleDoPole (int x, int y, Bunka[][] maska, Bunka[][] hracPole) {
        for (int a = 0; a < maska.length; a++) {
            for (int b = 0; b < maska[a].length; b++) {
                if (a + y < 10 && b + x < 10) {
                    hracPole[a + y][b + x] = maska [a][b];
                }
            }
        }
    }
    public static void vytisknuti2DPole (Bunka[][] pole, TextGraphics graphics, boolean hra, int x, int y) {
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
                    case VODA : {
                        dvojita.putString(a + y, b + x, " ");
                        break;
                    } 
                    case VEDLE : {
                        dvojita.putString(a + y, b + x, Character.toString(Symbols.BULLET));
                        break;
                    }
                    case LOD : {
                        dvojita.putString(a + y, b + x, Character.toString(Symbols.BLOCK_SOLID));
                        break;
                    }
                    case STRELENA : {
                        dvojita.putString(a + y, b + x, Character.toString(Symbols.BLOCK_MIDDLE));
                        break;
                    }
                    case POTOPENA : {
                        dvojita.putString(a + y, b + x, Character.toString(Symbols.BLOCK_SPARSE));
                    }
                    case ZABRANE : {
                        dvojita.putString(a + y, b + x, Character.toString(Symbols.BLOCK_DENSE));
                    }
                    
                    
                }
            }
        }
    }

}

