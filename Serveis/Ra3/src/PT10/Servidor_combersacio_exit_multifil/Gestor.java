package PT10.Servidor_combersacio_exit_multifil;

import java.io.*;
import java.net.Socket;

public class Gestor implements Runnable {
    private Socket socket;
    private InputStream input;
    private OutputStream output;

    public Gestor(Socket socket) {
        this.socket = socket;
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
}
