/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package one.dedic.rocnikovka.lode;

import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author aja
 */
public class StavPocitace {
    ArrayList<Integer> kamStrilet;
    ArrayList<Integer> potopeni = new ArrayList<>();

    public StavPocitace(ArrayList<Integer> kamStrilet) {
        this.kamStrilet = kamStrilet;
        for (int a = 0; a < 10; a++) {
            for (int b = 0; b < 10; b++) {
                Collections.addAll(this.kamStrilet, a, b);
            }
        }
    }

    
    
    public void strileni () {
        
    }
}
