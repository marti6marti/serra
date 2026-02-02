package PT11;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ex15 {
    public static void main(String[] args) {

        // 1. Llista de 10+ URLs
        List<String> llistaUrls = List.of(
                "https://www.google.com",
                "https://github.com",
                "https://ca.wiktionary.org",
                "https://www.oracle.com",
                "https://wikipedia.org",
                "https://www.apple.com",
                "https://www.microsoft.com",
                "https://reddit.com",
                "https://nasa.gov",
                "https://bbc.com",
                "https://httpbin.org/status/404"
        );

        // 2. Comptadors ATÒMICS (segurs per a threads)
        AtomicInteger comptadorOK = new AtomicInteger(0);
        AtomicInteger comptadorRedirect = new AtomicInteger(0);
        AtomicInteger comptadorError = new AtomicInteger(0);
        AtomicInteger comptadorNoDisponible = new AtomicInteger(0);

        // 3. Crear CLIENT (reutilitzable, thread-safe)
        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(5))
                .followRedirects(HttpClient.Redirect.NEVER)
                .build();

        // 4. Llista per guardar els threads
        List<Thread> llistaThreads = new ArrayList<>();

        System.out.println("*****************************");
        System.out.println("PETICIONS EN PARAL·LEL");
        System.out.println("*****************************\n");

        // 5. Temps inicial TOTAL
        long tempsIniciTotal = System.currentTimeMillis();

        // 6. Crear un thread per cada URL
        for (String url : llistaUrls) {

            Thread thread = new Thread(() -> {

                try {
                    // 7. Crear REQUEST
                    HttpRequest request = HttpRequest.newBuilder()
                            .GET()
                            .uri(URI.create(url))
                            .header("User-Agent", "Mozilla/5.0")
                            .timeout(Duration.ofSeconds(5))
                            .build();

                    // 8. Mesurar temps individual
                    long tempsInici = System.currentTimeMillis();

                    // 9. Enviar petició
                    HttpResponse<Void> resposta = client.send(
                            request,
                            HttpResponse.BodyHandlers.discarding()
                    );

                    long tempsResposta = System.currentTimeMillis() - tempsInici;

                    // 10. Obtenir codi
                    int codi = resposta.statusCode();

                    // 11. Classificar i mostrar
                    if (codi == 200) {
                        System.out.println("[OK] " + url + " -> " + codi + " (" + tempsResposta + " ms)");
                        comptadorOK.incrementAndGet();

                    } else if (codi == 301 || codi == 302) {
                        String location = resposta.headers()
                                .firstValue("Location")
                                .orElse("?");
                        System.out.println("[REDIRECT] " + url + " -> " + codi + " -> " + location);
                        comptadorRedirect.incrementAndGet();

                    } else if (codi >= 400) {
                        System.out.println("[ERROR] " + url + " -> " + codi);
                        comptadorError.incrementAndGet();
                    }

                } catch (HttpTimeoutException e) {
                    System.out.println("[FAIL] " + url + " -> timeout");
                    comptadorNoDisponible.incrementAndGet();

                } catch (IOException e) {
                    System.out.println("[FAIL] " + url + " -> error de connexió");
                    comptadorNoDisponible.incrementAndGet();

                } catch (InterruptedException e) {
                    System.out.println("[FAIL] " + url + " -> interromput");
                    comptadorNoDisponible.incrementAndGet();
                }
            });

            // 12. Iniciar el thread
            thread.start();

            // 13. Afegir a la llista
            llistaThreads.add(thread);
        }

        // 14. Esperar que TOTS els threads acabin
        for (Thread thread : llistaThreads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("Thread interromput");
            }
        }

        // 15. Temps total
        long tempsTotalExecucio = System.currentTimeMillis() - tempsIniciTotal;

        // 16. Mostrar resum
        System.out.println("\n*****************************");
        System.out.println("RESUM");
        System.out.println("*****************************");
        System.out.println("Total URLs: " + llistaUrls.size());
        System.out.println("Temps total: " + tempsTotalExecucio + " ms");
        System.out.println("OK (200): " + comptadorOK);
        System.out.println("Redirect (301/302): " + comptadorRedirect);
        System.out.println("Error (4xx/5xx): " + comptadorError);
        System.out.println("No disponible: " + comptadorNoDisponible);
    }
}
