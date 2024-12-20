package org.ing.ispw.unifix.utils;

public class Progressivo {
    private static int counter = 0; // Variabile statica per mantenere il progressivo

    public static int getNext() {
        return ++counter; // Incrementa e ritorna il valore
    }

    public static void reset() {
        counter = 0; // Resetta il progressivo
    }

    public static int getCurrent() {
        return counter; // Restituisce il valore attuale senza incrementarlo
    }
}
