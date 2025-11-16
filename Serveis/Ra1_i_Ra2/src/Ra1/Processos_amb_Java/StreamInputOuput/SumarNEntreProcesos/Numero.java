package Ra1.Processos_amb_Java.StreamInputOuput.SumarNEntreProcesos;


import java.util.Random;

public class Numero {
    public static void main(String[] args) {
        int num = new Random().nextInt(100);  // 0..99
        System.out.println(num);
    }
}

