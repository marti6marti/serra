package Ra2.ComunicacioThreads.Ex2ProductorConsumidor;

import java.util.Vector;

import java.util.Vector;

public class SynchStack {
    private final Vector<Character> buffer = new Vector<>(400, 200);
    private static final int MAX_SIZE = 250;

    // Afegeix un caràcter a la pila
    // Bloqueja si la pila està plena (MAX_SIZE)
    public synchronized void push(char c) {
        // Espera mentre la pila estigui plena
        while (buffer.size() == MAX_SIZE) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Afegeix l'element
        this.buffer.add(c);

        // Desperta threads que esperen (consumidors que esperaven dades)
        this.notifyAll();
    }

    // Treu un caràcter de la pila
    // Bloqueja si la pila està buida
    public synchronized char pop() {
        // Espera mentre la pila estigui buida
        while (buffer.isEmpty()) {
            try {
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Elimina i retorna l'últim element
        char c = buffer.remove(buffer.size() - 1);

        // Desperta threads que esperen (productors que esperaven espai)
        this.notifyAll();

        return c;
    }
}
