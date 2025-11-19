package paswords;

import java.util.Scanner;

public class Validador {
    static void main() {
        Scanner scanner = new Scanner(System.in);
        String contra = scanner.next();
        if(contra.length() >= 5){
            System.out.println("VALIDA");
            System.exit(0);
        }else{
            System.out.println("INVALIDA");
            System.exit(100);
        }
    }
}
