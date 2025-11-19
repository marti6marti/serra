package apps;

import java.io.IOException;

public class LlansarApp {
    static void main() throws IOException, InterruptedException {

        ProcessBuilder processChrome = new ProcessBuilder( "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe", "https://gmail.com");
        processChrome.start();

        ProcessBuilder processNotepad = new ProcessBuilder("notepad.exe");
        Process mec = processNotepad.start();

        Thread.sleep(2000);

        long pid = mec.pid();
        System.out.println(pid);
        //per tancar processos del mateix tipus amb una vegada és suficient, ja que si els processos q volem matar són el mateix tipus, es tanquen tots a la vegada.
        ProcessBuilder killChrome = new ProcessBuilder("taskkill", "/IM", "chrome.exe", "/F");
        Process processKill = killChrome.start();

        int codiKill = processKill.waitFor();
        System.out.println("Codi de finalització del procés Chrome amb taskkill: " + codiKill);
    }
}
