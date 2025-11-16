import java.io.File;

public class Main{
    public static void main(String[] args) {




                String classPath = System.getProperty("user.dir") + "\\out\\production\\ProcessosJava";
                File f = new File(classPath);
                if (f.exists() && f.isDirectory()) {
                    System.out.println("Carpeta trobada: " + f.getAbsolutePath());
                } else {
                    System.out.println("Carpeta NO trobada. Revisa la ruta: " + f.getAbsolutePath());
                }



    }
}
