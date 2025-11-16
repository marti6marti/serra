package Ra1.Processos_amb_Java.Codi_de_finalitzacio;

import java.util.Scanner;

public class Main3_if {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Escriu línies de text. Escriu 'BYE' per sortir o 'ERROR' per sortir amb error.");

        while (true) {
            System.out.print("> ");
            String linia = scanner.nextLine();

            if (linia.equals("BYE")) {
                System.out.println("Finalitzant correctament...");
                // Finalització correcta
                System.exit(0);
            } else if (linia.equals("ERROR")) {
                System.out.println("Finalitzant amb error...");
                // Finalització amb codi d'error personalitzat
                System.exit(200);
            } else {
                System.out.println("Has escrit: " + linia);
            }
        }
    }
}
