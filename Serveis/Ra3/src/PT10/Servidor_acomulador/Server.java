package PT10.Servidor_acomulador;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private ServerSocket serverSocket;
    public static final int PORT = 6000;


    public static AtomicInteger contador = new AtomicInteger(0);
    public static AtomicInteger suma = new AtomicInteger(0);

    public void iniciaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                //contador
                int numClient = contador.incrementAndGet();
                System.out.println("Client " + numClient + " connectat!");


                //aixeques un gestor per cada client
                Gestor gestor = new Gestor(socket, numClient);
                new Thread(gestor).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciaServei();
    }
}