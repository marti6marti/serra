package Ra1.Processos_amb_Java.Creacio_de_pocessos_amb_Runtime;

import java.io.IOException;

public class RecollirFinalDelProcess {
    public static void main(String[] args) throws IOException, InterruptedException {

        //tenir en compte que el "command" en verd és el que posaríem a la terminal per a executar el procés!
        // en aquest cas només amb notepad ja obrirà el procés del bloc d notes.

        ProcessBuilder chrome = new ProcessBuilder( "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "https://gmail.com");
        //ProcessBuilder chromeGmail = new ProcessBuilder("/usr/bin/google-chrome", "https://gmail.com"); //en linux

        Process processGmail = chrome.start();

        int codiRetorng = processGmail.waitFor();
        System.out.println("El codi del procés finalitzat de Gmail és : " + codiRetorng);

        ProcessBuilder notepad = new ProcessBuilder("notepad.exe");
        Process processNotepad = notepad.start();

        int codiret = processNotepad.waitFor();
        System.out.println("el de notepad es: "+codiret);

        //en linux
      //  ProcessBuilder editor = new ProcessBuilder("/usr/bin/gedit");
     //   Process processNotepad = editor.start();



    }

}

