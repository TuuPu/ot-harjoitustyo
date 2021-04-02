/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.unicafe;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author tuukkapuonti
 */
public class KassapaateTest {
    Kassapaate kassa;
    Maksukortti kortti;
    Maksukortti eiRiita;
    
    public KassapaateTest() {
        kassa = new Kassapaate();
        kortti = new Maksukortti(1000);
        eiRiita = new Maksukortti(230);
    }    
    
    @Before
    public void setUp() {
    }   

    @Test
    public void rahaMaaraJaLounaitaMyytyOikein(){
        assertEquals(100000, kassa.kassassaRahaa());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
    }
    
    @Test
    public void MaksuRiittavaJaKassaKasvaa(){
        kassa.syoEdullisesti(240);
        kassa.syoMaukkaasti(400);
        assertEquals(100640, kassa.kassassaRahaa());
    }
    
    @Test
    public void MaksuRiittavaJaLounaatKasvaa(){
        kassa.syoEdullisesti(240);
        kassa.syoMaukkaasti(400);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
   
    @Test
    public void rahaEiRiitaJaKassaPysyySamana(){
        kassa.syoEdullisesti(230);
        kassa.syoMaukkaasti(390);
        assertEquals(100000, kassa.kassassaRahaa());
    }
    
    @Test
    public void kunRahatEiRiitaKaikkiRahatPalautetaanVaihtoRahana(){     
        assertEquals(230, kassa.syoEdullisesti(230));
        assertEquals(390, kassa.syoMaukkaasti(390));
    }
    
    @Test
    public void lounaidenMaaraEiKasvaKunRahatEiRiita(){
        kassa.syoEdullisesti(230);
        kassa.syoMaukkaasti(390);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kortillaRahaaVeloitetaanJaPalautetaanTrue(){
        assertTrue(kassa.syoEdullisesti(kortti));        
        assertTrue(kassa.syoMaukkaasti(kortti));
        assertEquals(360, kortti.saldo());
    }
    
    @Test
    public void kortillaRahaaJaLounaidenMaaraKasvaa(){
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(1, kassa.edullisiaLounaitaMyyty());
        assertEquals(1, kassa.maukkaitaLounaitaMyyty());
    }
    
    @Test
    public void kortillaEiRahaaJaKortinRahamaaraEiMuutu(){
        kassa.syoEdullisesti(eiRiita);
        kassa.syoMaukkaasti(eiRiita);
        assertEquals(230, eiRiita.saldo());
    }
    
    @Test
    public void kortillaEiRahaaJaLounaidenMaaraEiMuutuJaPalauttaaFalse(){
        assertTrue(kassa.syoEdullisesti(eiRiita)==false);
        assertTrue(kassa.syoMaukkaasti(eiRiita)==false);
        assertEquals(0, kassa.edullisiaLounaitaMyyty());
        assertEquals(0, kassa.maukkaitaLounaitaMyyty());       
    }
    
    @Test
    public void kassanRahamaaraPysyySamanaKorttiOstoissa(){
        kassa.syoEdullisesti(kortti);
        kassa.syoMaukkaasti(kortti);
        assertEquals(100000,kassa.kassassaRahaa());
    }
    
    @Test
    public void kortinLatausKasvattaakassaaJaKortinSaldoMuuttuu(){
        kassa.lataaRahaaKortille(kortti, 400);
        kassa.lataaRahaaKortille(eiRiita, -5);
        assertEquals(230, eiRiita.saldo());
        assertEquals(1400, kortti.saldo());
        assertEquals(100400, kassa.kassassaRahaa());
    }
    
}
