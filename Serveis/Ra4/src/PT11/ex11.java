package PT11;


import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Scanner;

public class ex11 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. URL base del diccionari
        String urlBase = "https://ca.wiktionary.org/wiki/";

        // 2. Demanar paraula a l'usuari
        System.out.print("Escriu una paraula: ");
        String paraula = sc.nextLine().trim().toLowerCase();

        try {
            // 3. Codificar la paraula
            String paraulaCodificada = URLEncoder.encode(paraula, StandardCharsets.UTF_8);
            paraulaCodificada = paraulaCodificada.replace("+", "%20");

            // 4. Crear URI completa
            URI uri = URI.create(urlBase + paraulaCodificada);

            System.out.println("\n*****************************");
            System.out.println("Accedint a: " + uri);
            System.out.println("*****************************");

            // 5. Crear el CLIENT
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .followRedirects(HttpClient.Redirect.ALWAYS)
                    .build();

            // 6. Crear la REQUEST
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(uri)
                    .header("User-Agent", "Mozilla/5.0")
                    .build();

            // 7. Enviar i rebre RESPONSE (guardar directament en fitxer)
            HttpResponse<Path> resposta = client.send(
                    request,
                    HttpResponse.BodyHandlers.ofFile(Path.of(paraula + ".html"))
            );

            // 8. Mostrar resultat
            int codi = resposta.statusCode();
            System.out.println("Codi de resposta: " + codi);

            if (codi == 200) {
                System.out.println("\n*****************************");
                System.out.println("RESULTAT");
                System.out.println("*****************************");
                System.out.println("Fitxer creat: " + resposta.body().toAbsolutePath());
            } else if (codi == 404) {
                System.out.println("\n[ERROR] La paraula '" + paraula + "' no existeix.");
            } else {
                System.out.println("\n[ERROR] Codi: " + codi);
            }

        } catch (IOException e) {
            System.out.println("[ERROR] Problema de connexió: " + e.getMessage());
        } catch (InterruptedException e) {
            System.out.println("[ERROR] Connexió interrompuda: " + e.getMessage());
        } finally {
            sc.close();
        }
    }
}