package Ra1.Processos_amb_Java.Creacio_de_pocessos_amb_Runtime;

import java.io.IOException;

public class UsDePidiInfoIExitValue {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Llançar Chrome amb Gmail
        ProcessBuilder chromeBuilder = new ProcessBuilder("C:\\Program Files (x86)\\Google\\Chrome\\Application\\chrome.exe", "https://gmail.com");
        Process chromeProcess = chromeBuilder.start();

        // Obtenir PID de Chrome via reflexió (Java 8)
        long chromePid = -1;
        try {
            java.lang.reflect.Field f = chromeProcess.getClass().getDeclaredField("pid");
            f.setAccessible(true);
            chromePid = f.getLong(chromeProcess);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("PID del procés de Chrome: " + chromePid);

//        // Llançar gedit
//        ProcessBuilder geditBuilder = new ProcessBuilder("gedit");
//        Process geditProcess = geditBuilder.start();
//
//        // Obtenir PID de gedit via reflexió
//        long geditPid = -1;
//        try {
//            java.lang.reflect.Field f2 = geditProcess.getClass().getDeclaredField("pid");
//            f2.setAccessible(true);
//            geditPid = f2.getLong(geditProcess);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("PID del procés de gedit: " + geditPid);
//
//        // Esperar 2 segons
//        Thread.sleep(2000);
//
//        // Tancar gedit forçosament
//        geditProcess.destroyForcibly();
//        int geditExit = geditProcess.waitFor();
//        System.out.println("Codi de retorn del procés de gedit: " + geditExit);

        // Opcional: esperar Chrome (només si vols)
        // int chromeExit = chromeProcess.waitFor();
        // System.out.println("Codi de retorn del procés de Chrome: " + chromeExit);
    }


}
