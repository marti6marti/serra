package Ra1.Processos_amb_Java.StreamInputOuput;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.File;

public class Lector {
    public static void main(String[] args) {
        try {
            // Nom de la classe a executar (nom complet amb package)
            String className = Escriptor.class.getName();

            // Carpeta on es troben les classes compilades
            File classDir = new File(Escriptor.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String classPath = classDir.getAbsolutePath();

            // Ruta de l'executable de Java
            String javaExec = "java";

            System.out.println("ClassPath detectat: " + classPath);
            System.out.println("ClassName detectat: " + className);

            // Crear el procés amb ProcessBuilder
            ProcessBuilder pb = new ProcessBuilder(javaExec, "-classpath", classPath, className);
            pb.redirectErrorStream(true);

            // Llançar el procés
            Process process = pb.start();

            // Llegir la sortida del procés
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            System.out.println("\nLector: llegint la sortida de l’Escriptor...\n");

            while ((line = reader.readLine()) != null) {
                System.out.println("Rebut -> " + line);
            }

            // Esperar que acabi el procés
            int exitCode = process.waitFor();
            System.out.println("\nProcés finalitzat amb codi: " + exitCode);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
