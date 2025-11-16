package Ra1.Processos_amb_Java.CreacioAmbJavaLangPrBui;

import java.io.IOException;
import java.util.Map;

public class DadesAmbEntornExec {
    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder proces = new ProcessBuilder();

        //environmetn retorna un hash map amb les seves claus i valors.
        Map<String, String> entorn = proces.environment();

        //x a recórrer el map fem un bucle i llegir les claus i valors
        for(String claus : entorn.keySet()){
            String valors = entorn.get(claus);
            System.out.println( "a)La clau és: " + claus + " ----->  b)El valor és: " + valors);
        }
        //aquest exercici mostra un hash map amb clau i valors sobre les dades del meu ordinador.

    }

}

