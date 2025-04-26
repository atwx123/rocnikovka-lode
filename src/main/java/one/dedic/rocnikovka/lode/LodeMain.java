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

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.Terminal;
import java.io.IOException;
import java.util.Random;
import static one.dedic.rocnikovka.lode.Bunka.LOD;
import static one.dedic.rocnikovka.lode.Bunka.POTOPENA;
import static one.dedic.rocnikovka.lode.Bunka.STRELENA;
import static one.dedic.rocnikovka.lode.Bunka.VEDLE;

/**
 *
 * @author aja
 */
public class LodeMain {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        Terminal t = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(120, 50)).createTerminal();

        Screen scn = new TerminalScreen(t);

        scn.startScreen();

        TextGraphics graphics = scn.newTextGraphics();
        Pomucky pom = new Pomucky();
        HraciPoleClovek clovek = new HraciPoleClovek(graphics, scn);
        SeznamLodi sLodi = new SeznamLodi();

        //Pomucky.prectiVstup(0, 0, graphics, scn);
//        
//        HraciPolePocitac pocitac = new HraciPolePocitac(scn, graphics);
//       pocitac.opakUmisteniclovek.umisteniLodi(SeznamLodi.lode.get(11));Lodi();
        
        clovek.umisteniLodi();
    }

}
