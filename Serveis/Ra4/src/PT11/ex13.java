package PT11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.Scanner;

public class ex13 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Demanar URL a l'usuari
        System.out.print("Escriu una URL (https://...): ");
        String urlString = sc.nextLine().trim();

        try {
            // 2. Crear URI
            URI uri = URI.create(urlString);

            System.out.println("\n*****************************");
            System.out.println("Comprovant: " + uri);
            System.out.println("*****************************");

            // 3. Crear CLIENT amb timeout de CONNEXIÓ
            HttpClient client = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_1_1)
                    .connectTimeout(Duration.ofSeconds(5))
                    .followRedirects(HttpClient.Redirect.NEVER)  // No seguir redireccions (volem detectar-les)
                    .build();

            // 4. Crear REQUEST amb HEAD i timeout de LECTURA
            HttpRequest request = HttpRequest.newBuilder()
                    .HEAD()                                      // HEAD: només headers, no body
                    .uri(uri)
                    .header("User-Agent", "Mozilla/5.0")
                    .timeout(Duration.ofSeconds(5))              // Timeout de lectura
                    .build();

            // 5. Mesurar temps de resposta
            long tempsInici = System.currentTimeMillis();

            // 6. Enviar petició
            HttpResponse<Void> resposta = client.send(
                    request,
                    HttpResponse.BodyHandlers.discarding()       // Descartar body (HEAD no en té)
            );

            long tempsFinal = System.currentTimeMillis();
            long tempsResposta = tempsFinal - tempsInici;

            // 7. Obtenir codi de resposta
            int codi = resposta.statusCode();

            // 8. Mostrar resultats
            System.out.println("\n*****************************");
            System.out.println("RESULTAT");
            System.out.println("*****************************");
            System.out.println("Codi d'estat: " + codi);
            System.out.println("Temps de resposta: " + tempsResposta + " ms");

            // 9. Classificar resposta
            if (codi == 200) {
                System.out.println("\n[OK] El servei està disponible!");

            } else if (codi == 301 || codi == 302) {
                String location = resposta.headers()
                        .firstValue("Location")
                        .orElse("Desconeguda");
                System.out.println("\n[REDIRECT] Redirigit a: " + location);

            } else if (codi >= 400 && codi < 500) {
                System.out.println("\n[ERROR] Error del client (4xx): " + codi);

            } else if (codi >= 500) {
                System.out.println("\n[ERROR] Error del servidor (5xx): " + codi);

            } else {
                System.out.println("\n[INFO] Codi: " + codi);
            }

        } catch (HttpTimeoutException e) {
            System.out.println("\n[NO DISPONIBLE] Timeout: el servidor ha trigat massa.");

        } catch (IOException e) {
            System.out.println("\n[NO DISPONIBLE] Error de connexió: " + e.getMessage());

        } catch (InterruptedException e) {
            System.out.println("\n[NO DISPONIBLE] Connexió interrompuda.");

        } catch (IllegalArgumentException e) {
            System.out.println("\n[ERROR] URL no vàlida: " + e.getMessage());

        } finally {
            sc.close();
        }
    }
}