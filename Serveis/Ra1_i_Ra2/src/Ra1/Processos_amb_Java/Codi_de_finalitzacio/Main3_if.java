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
                System.exit(0);  // Finalització correcta
            } else if (linia.equals("ERROR")) {
                System.out.println("Finalitzant amb error...");
                System.exit(200);  // Finalització amb codi d'error personalitzat
            } else {
                System.out.println("Has escrit: " + linia);
            }
        }
    }
}
