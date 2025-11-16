package Ra1.Processos_amb_Java.Creacio_de_pocessos_amb_Runtime;

import java.io.IOException;

public class DormirIPararProcess {
    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder chromeGmail = new ProcessBuilder("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "https://gmail.com");
        //ProcessBuilder chromeGmail = new ProcessBuilder("/usr/bin/google-chrome", "https://gmail.com"); en linux

        Process processGmail = chromeGmail.start();
        int codiRetorn = processGmail.waitFor(); //fa q trigui en començar notepad pq espera a q chrome acabi.
        System.out.println("El codi del procés finalitzat de Gmail és : " + codiRetorn);

        //fer procés i executar notepad.exe
        ProcessBuilder notepad = new ProcessBuilder("notepad.exe");
        // en linux: ProcessBuilder notepad = new ProcessBuilder("/usr/bin/gedit");

        Process processNotepad = notepad.start();
        Thread.sleep(2000); //quant triga en executar-se
        processNotepad.destroyForcibly(); //assegura q el procés es tanca de manera forçosa.
        System.out.println("El procés de notepad.exe s'ha tancat exitosament amb destroy/destroyForcibly.");

        //si volem obtenir el codi de finalitzacio de notepad tb :
        int codiNotepad = processNotepad.waitFor();
        System.out.println("El codi de finalització del procés de notepad.exe és: " + codiNotepad);

    }

}

