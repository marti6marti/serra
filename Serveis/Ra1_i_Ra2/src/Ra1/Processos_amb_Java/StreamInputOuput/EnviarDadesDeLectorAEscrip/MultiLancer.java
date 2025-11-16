package Ra1.Processos_amb_Java.StreamInputOuput.EnviarDadesDeLectorAEscrip;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MultiLancer {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Introdueix el nombre de processos Escriptor a llançar: ");
        int numProcessos = scanner.nextInt();

        System.out.print("Introdueix el nombre de línies que ha de escriure cada procés: ");
        int numLinies = scanner.nextInt();

        try {
            String javaExec = "java";

            // Detectem automàticament el classPath i el className
            String className = Escriptor.class.getName();
            File classDir = new File(Escriptor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String classPath = classDir.getAbsolutePath();

            System.out.println("ClassPath: " + classPath);
            System.out.println("ClassName: " + className);

            List<Process> processos = new ArrayList<>();
            List<PrintWriter> writers = new ArrayList<>();

            // Llançar n processos
            for (int i = 0; i < numProcessos; i++) {
                ProcessBuilder pb = new ProcessBuilder(javaExec, "-classpath", classPath, className);
                pb.redirectErrorStream(true); // combina stderr amb stdout
                Process process = pb.start();
                processos.add(process);

                // Guardem el writer per enviar dades
                writers.add(new PrintWriter(new OutputStreamWriter(process.getOutputStream()), true));

                // Thread per llegir la sortida del procés
                final int index = i;
                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                new Thread(() -> {
                    try {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            System.out.println("Procés " + (index + 1) + " -> " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();
            }

            // Enviar el número de línies a cada procés
            for (PrintWriter writer : writers) {
                writer.println(numLinies);
            }

            // Esperar que tots els processos acabin
            for (Process p : processos) {
                p.waitFor();
            }

            System.out.println("Tots els processos han finalitzat.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
