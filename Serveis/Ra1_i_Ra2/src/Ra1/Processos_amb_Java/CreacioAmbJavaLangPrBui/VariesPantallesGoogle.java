package Ra1.Processos_amb_Java.CreacioAmbJavaLangPrBui;

import java.io.IOException;

public class VariesPantallesGoogle {
    public static void main(String[] args) throws IOException, InterruptedException {

        ProcessBuilder processChrome = new ProcessBuilder( "C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe", "https://gmail.com");

        for (int i = 0; i < 2; i++) {
            Process processGmail = processChrome.start();
        }

        Thread.sleep(2000);

        //per tancar processos del mateix tipus amb una vegada és suficient, ja que si els processos q volem matar són el mateix tipus, es tanquen tots a la vegada.
        ProcessBuilder killChrome = new ProcessBuilder("taskkill", "/IM", "chrome.exe", "/F");
        Process processKill = killChrome.start();
        int codiKill = processKill.waitFor();
        System.out.println("Codi de finalització del procés Chrome amb taskkill: " + codiKill);



}}
