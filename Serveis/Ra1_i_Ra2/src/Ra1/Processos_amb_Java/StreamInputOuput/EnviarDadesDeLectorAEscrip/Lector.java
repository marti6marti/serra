package Ra1.Processos_amb_Java.StreamInputOuput.EnviarDadesDeLectorAEscrip;

import java.io.*;

public class Lector {
    public static void main(String[] args) {
        try {
            String className = Escriptor.class.getName();
            File classDir = new File(Escriptor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String classPath = classDir.getAbsolutePath();
            String javaExec = "java";

            ProcessBuilder pb = new ProcessBuilder(javaExec, "-classpath", classPath, className);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            // Escriure a l’entrada d’Escriptor
            OutputStream os = process.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true); // true = auto-flush
            int numLinies = 5; // per exemple
            pw.println(numLinies); // enviem el número de línies
            pw.close(); // opcional, tanca l’entrada si ja no enviaràs més dades

            // Llegir la sortida d’Escriptor
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("Lector: llegint la sortida de l’Escriptor...\n");

            while ((line = reader.readLine()) != null) {
                System.out.println("Rebut -> " + line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nProcés finalitzat amb codi: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
