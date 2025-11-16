package Ra1.Processos_amb_Java.CreacioAmbJavaLangPrBui;

import java.io.File;
import java.io.IOException;

public class ProcesEditorText {

    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processNotepad = new ProcessBuilder("notepad.exe"); //crear procés

        File directoriTreball = new File("C:\\Users\\PeP\\Desktop\\serra\\Serveis"); //assignar direc on volem el procés
        processNotepad.directory(directoriTreball);

        Process notePadProcess = processNotepad.start(); //iniciar procés

        System.out.println("El procés notepad està al directori: " + directoriTreball.getAbsolutePath());

    }
}

