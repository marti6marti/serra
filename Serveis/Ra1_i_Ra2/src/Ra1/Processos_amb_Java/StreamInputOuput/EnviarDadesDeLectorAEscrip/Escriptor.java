package Ra1.Processos_amb_Java.StreamInputOuput.EnviarDadesDeLectorAEscrip;


import java.util.Scanner;

public class Escriptor {
    public static void main(String[] args) {
        try {
            Scanner sc = new Scanner(System.in);
            System.out.print("Esperant número de línies: ");
            int linies = sc.nextInt();

            for (int i = 1; i <= linies; i++) {
                System.out.println("Missatge número " + i);
                Thread.sleep(1000); // pausa 1 segon
            }
            System.out.println("Final de l’escriptura.");
            sc.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
