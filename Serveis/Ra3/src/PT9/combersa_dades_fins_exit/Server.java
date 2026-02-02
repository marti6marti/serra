package PT9.combersa_dades_fins_exit;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDate;
import java.time.LocalTime;

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
            String resposta = "";
            while ((missatge = reader.readLine()) != null) {

                if (missatge.equalsIgnoreCase("exit")) {
                    System.out.println("Client desconnectat");
                    break;
                }

                if (missatge.equalsIgnoreCase("DATA")){
                    resposta = String.valueOf(LocalDate.now());
                }

                if (missatge.equalsIgnoreCase("HORA")){
                    resposta = String.valueOf(LocalTime.now());
                }

                System.out.println("Client diu: " + missatge);


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