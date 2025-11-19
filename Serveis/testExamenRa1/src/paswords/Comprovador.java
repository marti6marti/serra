package paswords;

import java.io.*;
import java.net.URISyntaxException;

public class Comprovador {
    static void main() throws URISyntaxException, IOException {


        String validadorClassName = Validador.class.getName();
        File validadorClassDir = new File(Validador.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        String validadorClassPath = validadorClassDir.getAbsolutePath();

        String javaExec = "java";


        Process validador1 = new ProcessBuilder(javaExec, "-classpath", validadorClassPath, validadorClassName).start();
        PrintWriter validador1Writer = new PrintWriter(validador1.getOutputStream(), true);

        Process validador2 = new ProcessBuilder(javaExec, "-classpath", validadorClassPath, validadorClassName).start();
        PrintWriter validador2Writer = new PrintWriter(validador2.getOutputStream(), true);

        Process validador3 = new ProcessBuilder(javaExec, "-classpath", validadorClassPath, validadorClassName).start();
        PrintWriter validador3Writer = new PrintWriter(validador3.getOutputStream(), true);


        validador1Writer.println("abc");
        validador2Writer.println("password123");
        validador3Writer.println("1234");

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(validador1.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(validador2.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


        new Thread(() -> {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(validador3.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();


    }
}
