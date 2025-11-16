package Ra1.Processos_amb_Java.StreamInputOuput.SumarNEntreProcesos;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Coordinador {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Quants processos Numero vols llançar? ");
            int n = scanner.nextInt();

            // Detectem automàticament className i classPath
            String numeroClassName = Numero.class.getName();
            File numeroClassDir = new File(Numero.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String numeroClassPath = numeroClassDir.getAbsolutePath();

            String sumadorClassName = Sumador.class.getName();
            File sumadorClassDir = new File(Sumador.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String sumadorClassPath = sumadorClassDir.getAbsolutePath();

            // Ruta de l'executable de Java
            String javaExec = "java";

            System.out.println("Sumador ClassPath detectat: " + sumadorClassPath);
            System.out.println("Sumador ClassName detectat: " + sumadorClassName);
            System.out.println("Numero ClassPath detectat: " + numeroClassPath);
            System.out.println("Numero ClassName detectat: " + numeroClassName);

            // Llançar el procés Sumador
            Process sumador = new ProcessBuilder(javaExec, "-classpath", sumadorClassPath, sumadorClassName).start();
            PrintWriter sumadorWriter = new PrintWriter(sumador.getOutputStream(), true);

            // Thread per llegir la sortida del Sumador
            new Thread(() -> {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(sumador.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("[SUMADOR] " + line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }).start();

            List<Process> processosNumero = new ArrayList<>();

            // Llançar processos Numero
            for (int i = 0; i < n; i++) {
                Process p = new ProcessBuilder(javaExec, "-classpath", numeroClassPath, numeroClassName).start();
                processosNumero.add(p);

                int index = i + 1;
                BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));

                // Thread per llegir el número generat i enviar-lo al Sumador
                new Thread(() -> {
                    try {
                        String num = reader.readLine();
                        if (num != null) {
                            System.out.println("Número " + index + " generat: " + num);
                            sumadorWriter.println(num);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            // Esperar que acabin tots els processos Numero
            for (Process p : processosNumero) {
                p.waitFor();
            }

            // Tancar entrada del Sumador perquè acabi
            sumadorWriter.close();
            sumador.waitFor();

            System.out.println("Tots els processos han acabat.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
