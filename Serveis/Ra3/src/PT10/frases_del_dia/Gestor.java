package PT10.frases_del_dia;

import java.io.*;
import java.net.Socket;

public class Gestor implements Runnable {
    private Socket socket;
    private OutputStream output;
    private int numClient;

    public Gestor(Socket socket, int numClient) {
        this.socket = socket;
        this.numClient = numClient;
    }

    @Override
    public void run() {
        try {
            output = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            // Frase aleatòria
            int index = (int)(Math.random() * Server.frases.size());
            String frase = Server.frases.get(index);

            writer.write("Client " + numClient + ", la teva frase: " + frase);
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
}