package sincronitzarProcesos.escriptor;

public class Lector {
    public static void main(String[] args) {

        String ruta = System.getProperty("user.dir") + "/src";
        String nomArxiu = "hola";
        ProcessBuilder processBuilderEscriptor =  new ProcessBuilder("gedit", "-classpath", nomArxiu, "Escriptor.class");

    }
}
