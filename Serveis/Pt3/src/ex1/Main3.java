package ex1;

public class Main3 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new ProvaEstats());
        System.out.println("Estat inicial: " + t.getState());

        System.out.println();
        System.out.println("Thread abans de llançar-lo a execució");
        System.out.println(t.getState());
        System.out.println(t.isAlive());

        t.start();

        System.out.println();
        System.out.println("Thread després de llançar-lo a execució");
        System.out.println(t.getState());
        System.out.println(t.isAlive());



        Thread.sleep(500); // Espera mig segon
        System.out.println("Estat durant execució: " + t.getState());
        t.join(); // Opcional si vols esperar que acabi
        System.out.println("Estat final: " + t.getState());





        System.out.println(t.getId());
        System.out.println(t.getName());
        System.out.println(t.getPriority());
        System.out.println(t.getState());
        System.out.println(t.isAlive());
        t.setName("miquel");
        t.setPriority(5);
    }
}

