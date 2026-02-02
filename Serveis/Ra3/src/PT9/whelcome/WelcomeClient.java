package PT9.whelcome;

import java.io.*;
import java.net.Socket;

public class WelcomeClient {
    private Socket socket;
    private InputStream input;

    public void connecta(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            input = socket.getInputStream();
            System.out.println("Connectat al servidor!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String llegeixMissatge() {
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
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WelcomeClient client = new WelcomeClient();
        client.connecta("127.0.0.1", 6000);

        String missatge = client.llegeixMissatge();
        System.out.println("El servidor diu: " + missatge);

        client.tancaConnexio();
    }
}