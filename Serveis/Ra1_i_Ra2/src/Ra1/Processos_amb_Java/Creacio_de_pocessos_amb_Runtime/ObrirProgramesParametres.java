package Ra1.Processos_amb_Java.Creacio_de_pocessos_amb_Runtime;

import java.io.IOException;

public class ObrirProgramesParametres {
    public static void main(String[] args) {
        try {
            // Obrir Chrome amb una pàgina web específica
            Process process1 = Runtime.getRuntime().exec("chrome https://www.google.com");  // Windows
            // Process process1 = Runtime.getRuntime().exec("google-chrome https://www.google.com");  // Linux

            System.out.println("S'ha obert Chrome amb Google");

            // Obrir Notepad amb un fitxer (si existeix)
            Process process2 = Runtime.getRuntime().exec("notepad prova.txt");  // Windows
            // Process process2 = Runtime.getRuntime().exec("gedit prova.txt");  // Linux

            System.out.println("S'ha obert Notepad amb prova.txt");

            System.out.println("Aplicacions llançades amb paràmetres!");

        } catch (IOException e) {
            System.err.println("Error en obrir les aplicacions: " + e.getMessage());
            e.printStackTrace();
        }
    }
}