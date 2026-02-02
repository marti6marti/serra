package PT11;



import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.List;

public class ex14 {
    public static void main(String[] args) {

        // 1. Llista d'URLs a comprovar
        List<String> llistaUrls = List.of(
                "https://www.google.com",
                "https://github.com",
                "https://ca.wiktionary.org",
                "https://www.oracle.com",
                "https://httpbin.org/status/404",
                "https://httpbin.org/status/500"
        );

        // 2. Comptadors per al resum
        int comptadorOK = 0;
        int comptadorRedirect = 0;
        int comptadorError = 0;
        int comptadorNoDisponible = 0;

        // 3. Crear CLIENT (reutilitzable per totes les peticions)
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        System.out.println("*****************************");
        System.out.println("MONITOR DE SERVEIS WEB");
        System.out.println("*****************************\n");

        // 4. Recórrer cada URL
        for (String url : llistaUrls) {

            try {
                // 5. Crear REQUEST
                HttpRequest request = HttpRequest.newBuilder()
                        .HEAD()
                        .uri(URI.create(url))
                        .header("User-Agent", "Mozilla/5.0")
                        .timeout(Duration.ofSeconds(5))
                        .build();

                // 6. Mesurar temps
                long tempsInici = System.currentTimeMillis();

                // 7. Enviar petició
                HttpResponse<Void> resposta = client.send(
                        request,
                        HttpResponse.BodyHandlers.discarding()
                );

                long tempsResposta = System.currentTimeMillis() - tempsInici;

                // 8. Obtenir codi
                int codi = resposta.statusCode();

                // 9. Classificar i mostrar resultat
                if (codi == 200) {
                    System.out.println("[OK] " + url + " -> " + codi + " (" + tempsResposta + " ms)");
                    comptadorOK++;

                } else if (codi == 301 || codi == 302) {
                    String location = resposta.headers()
                            .firstValue("Location")
                            .orElse("?");
                    System.out.println("[REDIRECT] " + url + " -> " + codi + " -> " + location);
                    comptadorRedirect++;

                } else if (codi >= 400) {
                    System.out.println("[ERROR] " + url + " -> " + codi);
                    comptadorError++;

                } else {
                    System.out.println("[INFO] " + url + " -> " + codi);
                }

            } catch (HttpTimeoutException e) {
                System.out.println("[FAIL] " + url + " -> timeout");
                comptadorNoDisponible++;

            } catch (IOException e) {
                System.out.println("[FAIL] " + url + " -> error de connexió");
                comptadorNoDisponible++;

            } catch (InterruptedException e) {
                System.out.println("[FAIL] " + url + " -> interromput");
                comptadorNoDisponible++;

            } catch (IllegalArgumentException e) {
                System.out.println("[FAIL] " + url + " -> URL no vàlida");
                comptadorNoDisponible++;
            }
        }

        // 10. Mostrar resum final
        System.out.println("\n*****************************");
        System.out.println("RESUM");
        System.out.println("*****************************");
        System.out.println("Total URLs: " + llistaUrls.size());
        System.out.println("OK (200): " + comptadorOK);
        System.out.println("Redirect (301/302): " + comptadorRedirect);
        System.out.println("Error (4xx/5xx): " + comptadorError);
        System.out.println("No disponible: " + comptadorNoDisponible);
    }
}
