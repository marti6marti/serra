package PT10.frases_del_dia;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Server {
    private ServerSocket serverSocket;
    public static final int PORT = 6000;

    public static AtomicInteger contador = new AtomicInteger(0);
    public static List<String> frases = new ArrayList<>();

    public void iniciaServei() {
        try {
            carregarFrases();

            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                int numClient = contador.incrementAndGet();
                System.out.println("Client " + numClient + " connectat!");

                Gestor gestor = new Gestor(socket, numClient);
                new Thread(gestor).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void carregarFrases() {
        frases.add("La vida és bella");
        frases.add("Aprofita el dia");
        frases.add("Mai deixis de somiar");
        frases.add("Tot és possible");
        frases.add("Creu en tu mateix");
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciaServei();
    }
}