package PT10.Servidor_acomulador;

import java.io.*;
import java.net.Socket;

public class Gestor implements Runnable {
    private Socket socket;
    private InputStream input;
    private OutputStream output;

    private int numClient;

    public Gestor(Socket socket, int numClient) {
        this.socket = socket;
        this.numClient = numClient;
    }

    @Override
    public void run() {
        try {
            input = socket.getInputStream();
            output = socket.getOutputStream();

            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));

            String missatge;
            while ((missatge = reader.readLine()) != null) {
                if (missatge.equalsIgnoreCase("exit")) {
                    break;
                }

                try {
                    int numero = Integer.parseInt(missatge);
                    int total = Server.suma.addAndGet(numero);

                    String resposta = "Ets el client " + numClient + ". Suma total: " + total;
                    writer.write(resposta);
                    writer.newLine();
                    writer.flush();
                } catch (NumberFormatException e) {
                    writer.write("Error: envia un número vàlid");
                    writer.newLine();
                    writer.flush();
                }
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
}