package PT9.inici_client_envia_misatge;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket socket;
    private OutputStream output;
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

            // Input (para leer)
            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Output (para escribir)
            output = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));


            String missatge = reader.readLine();

            String resposta = "Has enviat: " + missatge;

            writer.write(resposta);
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
        Server server = new Server();
        server.iniciaServei();
    }
}