package PT9.combersa_fins_exit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private InputStream input;
    private OutputStream output;
    public static final int PORT = 6000;

    public void iniciaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);

            while (true) {
                socket = serverSocket.accept();
                System.out.println("Client connectat!");
                gestionaClient();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gestionaClient() {
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            String missatge;
            while ((missatge = reader.readLine()) != null) {
                if (missatge.equalsIgnoreCase("exit")) {
                    System.out.println("Client desconnectat");
                    break;
                }

                System.out.println("Client diu: " + missatge);
                String resposta = "HAS ENVIAT: " + missatge.toUpperCase();

                writer.write(resposta);
                writer.newLine();
                writer.flush();
            }

            tancaConnexio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tancaConnexio() {
        try {
            input.close();
            output.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciaServei();
    }
}