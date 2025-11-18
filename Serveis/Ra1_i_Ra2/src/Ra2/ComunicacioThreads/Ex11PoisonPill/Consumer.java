package Ra2.ComunicacioThreads.Ex11PoisonPill;

import java.util.concurrent.atomic.AtomicInteger;

public class Consumer implements Runnable {
    private static final AtomicInteger counter = new AtomicInteger(0);
    private final int num;
    private SynchStack stack;

    public Consumer() {
        this.num = counter.getAndIncrement();
    }

    public void setStack(SynchStack stack) {
        this.stack = stack;
    }

    @Override
    public void run() {
        char c;
        int consumed = 0;

        while (true) {
            c = this.stack.pop();

            // Detecta poison pill
            if (c == SynchStack.POISON_PILL) {
                System.out.println("Consumer " + num + " ha rebut poison pill, sortint...");
                break;
            }

            System.out.println("Consumer " + num + ": " + c);
            consumed++;

            try {
                Thread.sleep((int) (Math.random() * 300));
            } catch (InterruptedException e) {
                System.out.println("Consumer " + num + " interromput");
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.out.println("✅ Consumer " + num + " ha acabat (" + consumed + " consumits)");
    }
}
