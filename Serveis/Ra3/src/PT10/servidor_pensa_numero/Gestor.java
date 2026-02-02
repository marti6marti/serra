package PT10.servidor_pensa_numero;

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

            enviarMissatge("Endevina el número (1-3):");

            String missatge;
            while (((missatge = reader.readLine()) != null) && (!Server.jocAcabat.get())) {
                try {
                    int numero = Integer.parseInt(missatge);

                    if (numero == Server.numeroSecret) {
                        Server.jocAcabat.set(true);
                        enviarMissatge("Correcte! Has guanyat!");
                        avisarATots("El client " + numClient + " ha guanyat! Era el " + Server.numeroSecret);
                        break;

                    } else if (numero < Server.numeroSecret) {
                        enviarMissatge("Més gran");

                    } else {
                        enviarMissatge("Més petit");
                    }

                } catch (NumberFormatException e) {
                    enviarMissatge("Error: envia un número vàlid");
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