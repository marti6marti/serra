package procesBuilder.EX8;

import java.io.File;
import java.io.IOException;


public class main8 {
    public static void main(String[] args) throws IOException {


        try {
            String ruta = System.getProperty("user.dir") + "/src";
            String nomArxiu = "hola";
            ProcessBuilder processBuilderNotes = new ProcessBuilder("gedit", nomArxiu + ".txt");

            processBuilderNotes.directory(new File(ruta));
            processBuilderNotes.start();

            processBuilderNotes.environment();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
