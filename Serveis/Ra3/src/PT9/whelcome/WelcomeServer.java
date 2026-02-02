package PT9.whelcome;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WelcomeServer {
    private ServerSocket serverSocket;
    private Socket socket;
    private OutputStream output;
    private final String WELCOME_MESSAGE = "Benvingut al servidor!";
    public static final int PORT = 6000;

    public void iniciaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);

            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client connectat!");
                gestionaNovaConnexio(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gestionaNovaConnexio(Socket socket) {
        try {
            output = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            writer.write(WELCOME_MESSAGE);
            writer.newLine();
            writer.flush();

            tancaConnexio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tancaConnexio() {
        try {
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WelcomeServer server = new WelcomeServer();
        server.iniciaServei();
    }
}