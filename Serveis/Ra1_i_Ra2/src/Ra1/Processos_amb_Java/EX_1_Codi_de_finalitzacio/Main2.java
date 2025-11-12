package Ra1.Processos_amb_Java.EX_1_Codi_de_finalitzacio;

/*
 * PRÀCTICA 2 — Processos amb Java
 * Exercici 1: Codi de finalització
 * ---------------------------------------------------------
 * Enunciat:
 * Crea un programa que llegeixi línies del teclat i:
 *  - Finalitzi correctament (codi 0) quan llegeixi "BYE"
 *  - Finalitzi amb codi de finalització 200 quan llegeixi "ERROR"
 *  - Mostri per pantalla el text introduït en qualsevol altre cas.
 *
 * Objectiu:
 * Practicar l’ús de System.exit(codi) per controlar el codi de sortida
 * d’un procés Java.
 */

import java.util.Scanner;

public class Main2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Escriu alguna cosa (BYE per sortir, ERROR per error):");

        while (true) {
            String linia = sc.nextLine();

            // Si l’usuari escriu BYE, sortim amb codi 0 (execució correcta)
            if (linia.equalsIgnoreCase("BYE")) {
                System.out.println("Sortint correctament...");
                System.exit(0);
            }

            // Si escriu ERROR, sortim amb codi 200 (error definit per l’usuari)
            else if (linia.equalsIgnoreCase("ERROR")) {
                System.err.println("S'ha produït un error!");
                System.exit(200);
            }

            // En qualsevol altre cas, només mostrem la línia
            else {
                System.out.println("Has escrit: " + linia);
            }
        }
    }
}

