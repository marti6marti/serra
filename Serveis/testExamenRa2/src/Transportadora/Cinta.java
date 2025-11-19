package Transportadora;

import java.util.ArrayList;

import java.util.ArrayList;

public class Cinta {
    private ArrayList<Integer> paquetes;
    private int capacidadMaxima;

    public Cinta(int capacidadMaxima) {
        this.capacidadMaxima = capacidadMaxima;
        this.paquetes = new ArrayList<>();
    }


    public synchronized void ponerPaquete(int idProductor, int numeroPaquete) throws InterruptedException {

        while (paquetes.size() >= capacidadMaxima) {//si supera la capacitat: esperar
            System.out.println("Productor " + idProductor + " espera: cinta plena");
            wait();
        }


        paquetes.add(numeroPaquete);
        System.out.println("Productor " + idProductor + " pone paquet " + numeroPaquete + ". Capacitat: " + paquetes.size() + "/" + capacidadMaxima);
        notifyAll(); // Despierta a los que están esperando (consumidores)
    }

    public synchronized int retirarPaquete(int idConsumidor) throws InterruptedException {

        while (paquetes.isEmpty()) {// si no hi ha pacs, espera
            System.out.println("Consumidor " + idConsumidor + " espera: cinta buida");
            wait();
        }


        int paquete = paquetes.remove(0);
        System.out.println("Consumidor " + idConsumidor + " retira paquet " + paquete + ". Capacitat: " + paquetes.size() + "/" + capacidadMaxima);
        notifyAll();
        return paquete;
    }
}