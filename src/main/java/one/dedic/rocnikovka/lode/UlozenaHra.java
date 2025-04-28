/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package one.dedic.rocnikovka.lode;

/**
 *
 * @author aja
 */
public class UlozenaHra {
    SeznamUmistenychLodi clovek;
    SeznamUmistenychLodi pocitac;
    StavPocitace stavPocitace;

    public SeznamUmistenychLodi getClovek() {
        return clovek;
    }

    public void setClovek(SeznamUmistenychLodi clovek) {
        this.clovek = clovek;
    }

    public SeznamUmistenychLodi getPocitac() {
        return pocitac;
    }

    public void setPocitac(SeznamUmistenychLodi pocitac) {
        this.pocitac = pocitac;
    }

    public StavPocitace getStavPocitace() {
        return stavPocitace;
    }

    public void setStavPocitace(StavPocitace stavPocitace) {
        this.stavPocitace = stavPocitace;
    }
    
    
}
