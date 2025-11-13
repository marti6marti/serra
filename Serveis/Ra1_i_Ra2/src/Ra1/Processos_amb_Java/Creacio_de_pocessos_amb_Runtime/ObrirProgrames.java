package Ra1.Processos_amb_Java.Creacio_de_pocessos_amb_Runtime;

import java.io.IOException;

public class ObrirProgrames {
    public static void main(String[] args) {
        try {
            // Obrir editor de text
            Process process1 = Runtime.getRuntime().exec("notepad");  // Windows
            // Process process1 = Runtime.getRuntime().exec("gedit");  // Linux
            System.out.println("S'ha obert l'editor de text");

            // Obrir calculadora
            Process process2 = Runtime.getRuntime().exec("calc");  // Windows
            // Process process2 = Runtime.getRuntime().exec("gnome-calculator");  // Linux
            System.out.println("S'ha obert la calculadora");

            // Obrir navegador Chrome
            Process process3 = Runtime.getRuntime().exec("chrome");  // Windows
            // Process process3 = Runtime.getRuntime().exec("google-chrome");  // Linux
            System.out.println("S'ha obert el Chrome");

            System.out.println("Tots els programes s'han llançat correctament!");

        } catch (IOException e) {
            System.err.println("Error en obrir l'aplicació: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
