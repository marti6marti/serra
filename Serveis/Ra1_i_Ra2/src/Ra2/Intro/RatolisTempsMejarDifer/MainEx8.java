package Ra2.Intro.RatolisTempsMejarDifer;

public class MainEx8 {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("═══════════════════════════════════════════════");
        System.out.println("    RATOLINS MENJANT AMB TEMPS VARIABLE");
        System.out.println("═══════════════════════════════════════════════\n");

        // Creem els ratolins directament amb els valors
        Ratoli mickey = new Ratoli("Mickey", 3);
        Ratoli minnie = new Ratoli("Minnie", 5);
        Ratoli jerry = new Ratoli("Jerry", 2);

        // Creem i iniciem els threads
        Thread tMickey = new Thread(mickey);
        Thread tMinnie = new Thread(minnie);
        Thread tJerry = new Thread(jerry);

        System.out.println("🏁 Tots els ratolins comencen a menjar!\n");

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