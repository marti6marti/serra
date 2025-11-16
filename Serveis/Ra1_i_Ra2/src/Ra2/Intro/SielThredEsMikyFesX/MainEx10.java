package Ra2.Intro.SielThredEsMikyFesX;

public class MainEx10 {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("       MICKEY LOVES MINNIE ❤️");
        System.out.println("═══════════════════════════════════════════════\n");

        // Creem els ratolins
        Ratoli mickey = new Ratoli("Mickey", 3);
        Ratoli minnie = new Ratoli("Minnie", 5);
        Ratoli jerry = new Ratoli("Jerry", 2);

        // Creem els threads i els assignem NOM amb el constructor de Thread
        Thread tMickey = new Thread(mickey, "Mickey");   // ← Nom del thread: "Mickey"
        Thread tMinnie = new Thread(minnie, "Minnie");   // ← Nom del thread: "Minnie"
        Thread tJerry = new Thread(jerry, "Jerry");      // ← Nom del thread: "Jerry"

        System.out.println("🏁 Tots els ratolins comencen a menjar!\n");

        // Iniciem els fils
        tMickey.start();
        tMinnie.start();
        tJerry.start();

        // Esperem que acabin
        tMickey.join();
        tMinnie.join();
        tJerry.join();

        System.out.println("\n═══════════════════════════════════════════════");
        System.out.println("🎉 Tots els ratolins han acabat de menjar!");
        System.out.println("═══════════════════════════════════════════════");
    }
}
