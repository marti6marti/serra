package Ra1.Processos_amb_Java.StreamInputOuput;

public class Escriptor {
    public static void main(String[] args) {
        try {
            for (int i = 1; i <= 5; i++) {
                System.out.println("Missatge número " + i);
                Thread.sleep(1000); // pausa d'1 segon entre missatges
            }
            System.out.println("Final de l’escriptura.");
        } catch (InterruptedException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
