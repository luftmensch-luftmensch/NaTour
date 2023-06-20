package com.natour;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Metodo per il controllo della validità dei campi
 * <p> <strong>username</strong>: Non deve essere null o vuoto</p>
 * <p> <strong>email</strong>: Non deve essere null o vuoto. Deve rispettare il pattern di una email</p>
 * <p> <strong>password</strong>: Non deve essere null o vuoto. Deve rispettare il pattern  e la regex definita per la password. Deve rispettare una lunghezza minima </p>
 * <p> <strong>confermaPassword</strong>: Deve essere uguale alla password </p>
 */
public class ControlloCampiTest {
    ControlloCampiMock controlloCampiMock;

    @Before
    public void setup(){
       controlloCampiMock = new ControlloCampiMock();
    }


    /*
    Classi di equivalenza:
    + Username valido;
    + Username vuoto;
    + Username null;

    + email valida
    + email vuoto;
    + email null;
    + email che non contiene una @;
    + email che non contiene il dominio;

    + password valida;
    + password vuota;
    + password null;
    + password < 8 caratteri;
    + password senza numeri;
    + password senza caratteri speciali;

    + confermaPassword che corrisponde alla password;
    + confermaPassword che non corrisponde alla password;

    */

    // Test sullo username
    @Test
    public void testRegistrazioneValida(){
        assertTrue(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "Prova123@", "Prova123@"));
    }

    @Test
    public void testUsernameVuoto(){
        assertFalse(controlloCampiMock.controlloCampi("", "valentinobocchetti59@gmail.com", "Prova123@", "Prova123@"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUsernameNull(){
        controlloCampiMock.controlloCampi(null, "valentinobocchetti59@gmail.com", "Prova123@", "Prova123@");
    }

    // Test sulla mail
    @Test
    public void testEmailVuota(){
        assertFalse(controlloCampiMock.controlloCampi("", "", "Prova123@", "Prova123@"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmailNull(){
        controlloCampiMock.controlloCampi("test123", null, "Prova123@", "Prova123@");
    }

    @Test
    public void testEmailSenzaChiocciola(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59gmail.com", "Prova123@", "Prova123@"));
    }

    @Test
    public void testEmailSenzaDominio(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@", "Prova123@", "Prova123@"));
    }

    // Test sulla password
    @Test
    public void testPasswordVuota(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "", "Prova123@"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPasswordNulla(){
        controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", null, "Prova123@");
    }

    @Test
    public void testPasswordBreve(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "P123@", "Prova123@"));
    }

    @Test
    public void testPasswordSenzaNumeri(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "prova", "Prova123@"));
    }

    @Test
    public void testPasswordSenzaCaratteriSpeciali(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "prova12", "Prova123@"));
    }

    @Test
    public void testPasswordSenzaLettereMaiuscole(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "prova123@", "Prova123@"));
    }




    // Test su confermaPassword
    @Test
    public void testPasswordMatchNonValido(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "Prova123@", "Prova1234@"));
    }

    @Test
    public void testConfermaPasswordVuota(){
        assertFalse(controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "Prova123@", ""));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConfermaPasswordNulla(){
        controlloCampiMock.controlloCampi("test123", "valentinobocchetti59@gmail.com", "Prova123@", null);
    }


        // Testing del metodo controlloRecuperoPassword
    @Test
    public void testPasswordValide(){
        assertTrue(controlloCampiMock.controlloRecuperoPassword("Prova123@", "12345"));
    }

        // Test del caso in cui la password sia vuota
    @Test
    public void testPasswordVuotaControlloRecuperoPassword(){
        assertFalse(controlloCampiMock.controlloRecuperoPassword("", "12345"));
    }

        // Test del caso in cui il codice di conferma sia vuoto
    @Test
    public void testCodiceConfermaVuotoControlloRecuperoPassword(){
        assertFalse(controlloCampiMock.controlloRecuperoPassword("Prova123@", ""));
    }

        // Test del caso in cui la password sia troppo corta
    @Test
    public void testPasswordBreveControlloRecuperoPassword(){
        assertFalse(controlloCampiMock.controlloRecuperoPassword("Pro123@", "12345"));
    }

    // Test del caso in cui la password non contenga numeri
    @Test
    public void testPasswordSenzaNumeriControlloRecuperoPassword(){
        assertFalse(controlloCampiMock.controlloRecuperoPassword("Provatest@", "12345"));
    }

    // Test del caso in cui la password non contenga lettere maiuscole
    @Test
    public void testPasswordSenzaMaiuscoleControlloRecuperoPassword(){
        assertFalse(controlloCampiMock.controlloRecuperoPassword("prova123@", "12345"));
    }

    // Test del caso in cui la password non contenga caratteri speciali
    @Test
    public void testPasswordSenzaCaratteriSpecialiControlloRecuperoPassword(){
        assertFalse(controlloCampiMock.controlloRecuperoPassword("Prova1234", "12345"));
    }

}