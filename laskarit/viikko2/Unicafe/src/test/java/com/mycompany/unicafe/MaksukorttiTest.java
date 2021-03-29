package com.mycompany.unicafe;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MaksukorttiTest {

    Maksukortti kortti;
    Maksukortti alleSaldon;

    @Before
    public void setUp() {
        alleSaldon = new Maksukortti(10);
        kortti = new Maksukortti(10);
    }

    @Test
    public void luotuKorttiOlemassa() {
        assertTrue(kortti!=null);      
    }
    @Test
    public void SaldoOikein(){
        assertEquals(10, kortti.saldo());      
    }
    
    @Test
    public void LataaRahaa(){
        kortti.lataaRahaa(10);
        assertEquals(20, kortti.saldo());
    }
    
    @Test
    public void SaldoVahenee(){
        kortti.otaRahaa(5);
        assertEquals(5, kortti.saldo());
    }
    
    @Test
    public void SaldoEiMuutuJosRahaEiRiita(){
        kortti.otaRahaa(15);
        assertEquals(10, kortti.saldo());
    }
    
    @Test
    public void FalseJosEiRiitaTrueJosRiittaa(){
        assertTrue(alleSaldon.otaRahaa(9));
        assertTrue(kortti.otaRahaa(15)==false);
    }
    
    @Test
    public void KortinTulosteOikein(){
        assertEquals("saldo: 0.10", kortti.toString());
    }
        
}
