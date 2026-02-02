package PT11;

import java.net.URL;
import java.net.MalformedURLException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

//Programa que llegeix el contingut d'una URL i el mostra per pantalla.

public class ex6 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix una URL: ");
        String urlString = scanner.nextLine().trim();

        System.out.println("CONTINGUT DE LA URL");

        try {
            URL url = new URL(urlString);

            // Obrim un stream de lectura directament des de la URL
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String linia;
            while ((linia = in.readLine()) != null) {
                System.out.println(linia);
            }

            in.close();
            System.out.println("Lectura feta.");

        } catch (MalformedURLException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println( e.getMessage());
        }
        scanner.close();
    }
}