import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String texto = sc.next();

        if (texto == "BYE"){
            System.exit(0);
        }
        if (texto.equals("ERROR")) {
            System.exit(200);
        }
    }
}
