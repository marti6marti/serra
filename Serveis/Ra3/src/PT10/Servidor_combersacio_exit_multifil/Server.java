package PT10.Servidor_combersacio_exit_multifil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public static final int PORT = 6000;

    public void iniciaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connectat!");

                Gestor gestor = new Gestor(socket);
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