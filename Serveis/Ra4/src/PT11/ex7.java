package PT11;

import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ex7 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introdueix URL: ");
        String urlString = scanner.nextLine();

        try {
            URL url = new URL(urlString);
            URLConnection connexio = url.openConnection();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connexio.getInputStream())
            );

            String linia;
            while ((linia = in.readLine()) != null) {
                System.out.println(linia);
            }
            in.close();

        } catch (MalformedURLException e) {
            System.out.println("URL no valida");
        } catch (IOException e) {
            System.out.println("falla conexio");
        }
        scanner.close();
    }
}
