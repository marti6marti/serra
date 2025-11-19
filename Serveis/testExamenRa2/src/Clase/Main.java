package Clase;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Creem un examen");
        Examen examen = new Examen(10);

        Thread tprofesor = new Thread(new Professor(2,1,5,examen));

        Thread talumne1 = new Thread(new Alumne("Marti1",examen,1));
        Thread talumne2 = new Thread(new Alumne("Marti2",examen,2));
        Thread talumne3 = new Thread(new Alumne("Marti3",examen,3));

        tprofesor.start();

        talumne1.start();
        talumne2.start();
        talumne3.start();

        talumne1.join();
        talumne2.join();
        talumne3.join();



    }
}
