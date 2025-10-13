package ex1;

public class ProvaEstats implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("Estat abans de dormir: " + Thread.currentThread().getState());
            Thread.sleep(2000);
            System.out.println("Estat després de dormir: " + Thread.currentThread().getState());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

