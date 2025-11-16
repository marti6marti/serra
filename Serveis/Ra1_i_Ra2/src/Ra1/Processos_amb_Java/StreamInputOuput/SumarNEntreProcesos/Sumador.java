package Ra1.Processos_amb_Java.StreamInputOuput.SumarNEntreProcesos;

import java.util.Scanner;

public class Sumador {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int suma = 0;

        System.out.println("Sumador: esperant números...");

        while (scanner.hasNextInt()) {
            int num = scanner.nextInt();
            suma += num;
            System.out.println("Suma actual: " + suma);
        }

        System.out.println("Sumador: entrada tancada. Suma final = " + suma);
    }
}
