package Ra2.Intro.CrearThread;

public class Main {
    public static void main(String[] args) {
        // Creem tres objectes Ratoli (que ara són Threads)
        Ratoli mickey = new Ratoli("Mickey");
        Ratoli minnie = new Ratoli("Minnie");
        Ratoli jerry = new Ratoli("Jerry");

        // Iniciem l'execució dels fils en paral·lel
        mickey.start();
        minnie.start();
        jerry.start();
    }
}