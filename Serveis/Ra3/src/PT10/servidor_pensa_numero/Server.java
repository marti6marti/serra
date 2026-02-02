package PT10.servidor_pensa_numero;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private ServerSocket serverSocket;
    public static final int PORT = 6000;


    public static int numeroSecret = (int)(Math.random() * 3) + 1;
    public static AtomicInteger contador = new AtomicInteger(0);
    public static List<Gestor> clients = new ArrayList<>();
    public static AtomicBoolean jocAcabat = new AtomicBoolean(false);



    public void iniciaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);
            System.out.println("Número secret: " + numeroSecret);


            while (!jocAcabat.get()) {
                Socket socket = serverSocket.accept();
                //contador
                int numClient = contador.incrementAndGet();
                System.out.println("Client " + numClient + " connectat!");


                //aixeques un gestor per cada client
                Gestor gestor = new Gestor(socket, numClient);

                //posem a la llista
                clients.add(gestor);

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