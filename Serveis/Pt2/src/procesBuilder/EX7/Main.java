package procesBuilder.EX7;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {


        try {

            String nomArxiu = "hola";
            ProcessBuilder processBuilderNotes = new ProcessBuilder("gedit", nomArxiu + ".txt");

            for (int i= 0; i < 3; i++){
                processBuilderNotes.start();
                nomArxiu = "hola" + i;
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
