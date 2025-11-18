package Ra2.ComunicacioThreads.Ex11PoisonPill;

import java.util.concurrent.atomic.AtomicInteger;

public class Producer implements Runnable {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int num;
    private SynchStack stack;

    public Producer() {
        this.num = counter.getAndIncrement();
    }

    public void setStack(SynchStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        char c;
        for (int i = 0; i < 200; i++) {
            // Comprova si el sistema s'està tancant
            if (stack.isShutdown()) {
                System.out.println("Producer " + num + " detecta shutdown, sortint...");
                return;
            }

            c = (char) ((Math.random() * 26) + 'A');
            System.out.println("Producer " + num + ": " + c);
            this.stack.push(c);

            try {
                Thread.sleep((int) (Math.random() * 300));
            } catch (InterruptedException e) {
                System.out.println("Producer " + num + " interromput");
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("✅ Producer " + num + " ha acabat");
    }
}
