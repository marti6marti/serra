package Ra2.ComunicacioThreads.Ex11PoisonPill;

import java.util.Vector;

public class SynchStack {
    private final Vector<Character> buffer = new Vector<>(400, 200);
    private static final int MAX_SIZE = 250;

    // Caràcter especial que indica fi del sistema
    public static final char POISON_PILL = '\0';

    // Flag per indicar que el sistema s'està tancant
    private boolean shutdown = false;

    public synchronized void push(char c) {
        while (buffer.size() == MAX_SIZE && !shutdown) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }

        if (shutdown) {
            return;
        }

        this.buffer.add(c);
        this.notifyAll();
    }

    public synchronized char pop() {
        while (buffer.isEmpty() && !shutdown) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return POISON_PILL;
            }
        }

        // Si el sistema s'està tancant i no hi ha més elements
        if (buffer.isEmpty() && shutdown) {
            return POISON_PILL;
        }

        char c = buffer.remove(buffer.size() - 1);
        this.notifyAll();
        return c;
    }

    // Mètode per tancar el sistema
    public synchronized void shutdown() {
        shutdown = true;
        // Desperta TOTS els threads que esperen
        this.notifyAll();
    }

    public synchronized boolean isShutdown() {
        return shutdown;
    }
}