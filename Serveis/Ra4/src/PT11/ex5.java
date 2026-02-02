package PT11;

import java.net.URL;
import java.net.MalformedURLException;
import java.util.Scanner;

//Programa que demana una URL a l'usuari i mostra les parts que la componen.

public class ex5 {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix una URL: ");

        String urlString = scanner.nextLine().trim();

        try {
            URL url = new URL(urlString);
            System.out.println("COMPONENTS DE LA URL");

            System.out.println("Protocol:      " + url.getProtocol());
            System.out.println("Authority:     " + url.getAuthority());
            System.out.println("Host:          " + url.getHost());
            System.out.println("Port:          " + (url.getPort() == -1 ? "(port per defecte)" : url.getPort()));
            System.out.println("Default Port:  " + url.getDefaultPort());
            System.out.println("Path:          " + (url.getPath().isEmpty() ? "(buit)" : url.getPath()));
            System.out.println("Query:         " + (url.getQuery() == null ? "(cap)" : url.getQuery()));
            System.out.println("File:          " + (url.getFile().isEmpty() ? "(buit)" : url.getFile()));
            System.out.println("Ref (anchor):  " + (url.getRef() == null ? "(cap)" : url.getRef()));
            System.out.println("User Info:     " + (url.getUserInfo() == null ? "(cap)" : url.getUserInfo()));

        } catch (MalformedURLException e) {
            System.out.println("ERROR: La URL introduïda no és vàlida.");
        }
        scanner.close();
    }
}
