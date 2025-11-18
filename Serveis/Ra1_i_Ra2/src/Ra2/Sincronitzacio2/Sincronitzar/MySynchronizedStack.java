package Ra2.Sincronitzacio2.Sincronitzar;

import java.util.Arrays;

public class MySynchronizedStack {
    private int idx = 0;
    private Character[] data = new Character[MAX_SIZE];
    public static final int MAX_SIZE = 12;

    // Push sincronitzat: només un thread pot executar aquest mètode alhora
    // synchronized garanteix accés exclusiu a tot el mètode
    public synchronized void push(Character c) {
        if (idx < data.length) {
            data[idx] = c;
            idx++;
        }
    }

    // Pop sincronitzat: només un thread pot executar aquest mètode alhora
    // synchronized bloqueja l'accés mentre un thread està dins
    public synchronized Character pop() {
        Character value = null;
        if (idx > 0) {
            idx--;
            value = data[idx];
            data[idx] = null;
        }
        return value;
    }

    @Override
    public synchronized String toString() {
        return "MySynchronizedStack{idx=" + idx + ", data=" + Arrays.toString(data) + '}';
    }
}
