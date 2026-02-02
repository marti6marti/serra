package PT10.Servidor_combersacio_exit_multifil;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client1 {
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

    public void comunicacio() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
            Scanner scanner = new Scanner(System.in);

            while (true) {
                System.out.print("Tu: ");
                String missatge = scanner.nextLine();

                writer.write(missatge);
                writer.newLine();
                writer.flush();

                if (missatge.equalsIgnoreCase("exit")) {
                    break;
                }

                String resposta = reader.readLine();
                System.out.println("Servidor: " + resposta);
            }

            scanner.close();
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
        Client1 client1 = new Client1();
        client1.connecta("127.0.0.1", 6000);
        client1.comunicacio();
        client1.tancaConnexio();
    }
}