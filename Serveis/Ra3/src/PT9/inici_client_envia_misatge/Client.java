package PT9.inici_client_envia_misatge;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private InputStream input;
    private OutputStream output;

    public void connecta(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            input = socket.getInputStream();
            output = socket.getOutputStream();
            System.out.println("Connectat al servidor!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviaMissatge(String missatge) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            writer.write(missatge);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String llegeixResposta() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
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
        Client client = new Client();
        client.connecta("127.0.0.1", 6000);

        client.enviaMissatge("hola que tal");

        String resposta = client.llegeixResposta();
        System.out.println("Servidor diu: " + resposta);

        client.tancaConnexio();
    }
}