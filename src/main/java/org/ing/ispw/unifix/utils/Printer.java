package org.ing.ispw.unifix.utils;

public class Printer {
    private Printer(){}
    @SuppressWarnings("java:S106")
    public static void print(String messaggio){
        System.out.println(messaggio);
    }
    @SuppressWarnings("java:S106")
    public static void error(String errore){
        System.err.println(errore);
    }
}