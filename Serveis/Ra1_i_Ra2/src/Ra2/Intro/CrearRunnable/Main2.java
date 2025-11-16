package Ra2.Intro.CrearRunnable;

public class Main2 {
    public static void main(String[] args) {
        // Creem tres objectes Ratoli (que ara són Runnable, NO Thread)
        Ratoli mickey = new Ratoli("Mickey");
        Ratoli minnie = new Ratoli("Minnie");
        Ratoli jerry = new Ratoli("Jerry");

        // Creem fils passant-los els objectes Runnable i els iniciem
        new Thread(mickey).start();
        new Thread(minnie).start();
        new Thread(jerry).start();
    }
}