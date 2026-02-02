package PT11;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class ex8 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        try {
            // 1. Pedir URL al usuario
            System.out.print("Escriu una URL: ");
            String urlString = sc.nextLine();

            // 2. Crear la URL
            URL url = new URL(urlString);

            // 3. Abrir conexión HTTP
            HttpURLConnection conexio = (HttpURLConnection) url.openConnection();

            // 4. Configurar el MeTODO
            conexio.setRequestMethod("GET");

            // 5. Configurar PROPIEDADES (headers de petición)
            conexio.setRequestProperty("User-Agent", "Mozilla/5.0");
            conexio.setRequestProperty("Accept", "text/html");
            conexio.setRequestProperty("Accept-Charset", "UTF-8");

            // 6. Obtener código de respuesta
            int codiResposta = conexio.getResponseCode();
            String missatgeResposta = conexio.getResponseMessage();

            System.out.println("\n*****************************");
            System.out.println("INFORMACIÓ DE LA RESPOSTA");
            System.out.println("*****************************");
            System.out.println("Codi: " + codiResposta);
            System.out.println("Missatge: " + missatgeResposta);

            // 7. Mostrar HEADERS de respuesta
            System.out.println("\n*****************************");
            System.out.println("HEADERS DE RESPOSTA");
            System.out.println("*****************************");
            System.out.println("Content-Type: " + conexio.getHeaderField("Content-Type"));
            System.out.println("Content-Length: " + conexio.getHeaderField("Content-Length"));
            System.out.println("Server: " + conexio.getHeaderField("Server"));
            System.out.println("Date: " + conexio.getHeaderField("Date"));

            // 8. Si es OK (200), leer el contenido
            if (codiResposta == HttpURLConnection.HTTP_OK) {

                System.out.println("\n*****************************");
                System.out.println("CONTINGUT DE LA WEB");
                System.out.println("*****************************");

                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(conexio.getInputStream(), "UTF-8")
                );

                String linia;
                while ((linia = reader.readLine()) != null) {
                    System.out.println(linia);
                }

                reader.close();

            } else if (codiResposta == HttpURLConnection.HTTP_MOVED_PERM ||
                    codiResposta == HttpURLConnection.HTTP_MOVED_TEMP) {
                // 301 o 302 - Redirección
                String novaUbicacio = conexio.getHeaderField("Location");
                System.out.println("\n[REDIRECT] Redirigit a: " + novaUbicacio);

            } else {
                System.out.println("\n[ERROR] No s'ha pogut obtenir el contingut");
            }

            // 9. Cerrar conexión
            conexio.disconnect();

        } catch (IOException e) {
            System.out.println("[ERROR] Problema de connexió: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}
