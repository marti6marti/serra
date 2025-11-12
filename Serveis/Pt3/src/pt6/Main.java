package pt6;

public class Main {
    public static void main(String[] args) {
        Bus bus = new Bus(3);

        for (int i = 1; i <= 8; i++) {
            new Passatger("Passatger-" + i, bus).start();
        }
    }
}

