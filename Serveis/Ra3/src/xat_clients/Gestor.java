package xat_clients;

import java.io.*;
import java.net.Socket;

public class Gestor implements Runnable {

    private Socket socket;
    private InputStream input;
    private OutputStream output;

    private BufferedReader reader;
    private BufferedWriter writer;

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

            reader = new BufferedReader(new InputStreamReader(input));
            writer = new BufferedWriter(new OutputStreamWriter(output));

            String missatge;
            while ((missatge = reader.readLine()) != null) {
                    if (!missatge.equalsIgnoreCase("exit")){
                        avisarATots("[Client " + numClient +"]: " + missatge);
                    } else {
                        Server.clients.remove(this);
                        avisarATots("El client: " + numClient + "s'ha desconectat"
                                + "\n" +
                                "Clients connectats:" + Server.clients.size());
                        break;
                    }
            }

            tancaConnexio();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void avisarATots(String msg) {
        for (Gestor g : Server.clients) {
            if (g != this) {
                g.enviarMissatge(msg);
            }
        }
    }

    public void enviarMissatge(String msg) {
        try {
            writer.write(msg);
            writer.newLine();
            writer.flush();
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