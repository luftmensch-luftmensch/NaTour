package com.natour;

import java.util.regex.Pattern;

public class ControlloCampiMock {
    public ControlloCampiMock() { }

    // Visto che il Toast non sono mockuppabili ricopio il metodo della SignUpActivity eliminando le chiamate con i Toast

    // Per questo metodo utilizzo black box wect
    public boolean controlloCampi(String username, String email, String password, String confermaPassword) throws IllegalArgumentException{
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);

        if((username == null) || (email == null) || (password == null) || (confermaPassword == null)){
            throw new IllegalArgumentException();
        }
        if((username.isEmpty()) || (email.isEmpty()) || (password.isEmpty()) || (confermaPassword.isEmpty())){
            return false;
        }
        if(!pattern.matcher(email).matches()){
            return false;
        }

        if (password.length() < 8) {
            return false;
        }

        if(!password.matches("(.*[0-9].*)") | !password.matches("(.*[A-Z].*)") | !password.matches("^(?=.*[_.()$&@]).*$")) {
            return false;
        }

        if(!password.equals(confermaPassword)){
            return false;
        }
        return true;
    }

    // Visto che il Toast non sono mockuppabili ricopio il metodo della LoginActivity eliminando le chiamate con i Toast
    // Metodo che controlla che la nuova password sia valida
    public boolean controlloRecuperoPassword(String nuovaPassword, String codiceConferma){
        if ((nuovaPassword.isEmpty()) || (codiceConferma.isEmpty())) {
            return false;
        }
        if (nuovaPassword.length() < 8) {
            return false;
        }
        if (!nuovaPassword.matches("(.*[0-9].*)") | !nuovaPassword.matches("(.*[A-Z].*)") | !nuovaPassword.matches("^(?=.*[_.()$&@]).*$")) {
            return false;
        }
        return true;
    }
}