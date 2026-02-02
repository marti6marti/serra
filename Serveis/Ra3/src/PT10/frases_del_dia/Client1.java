package PT10.frases_del_dia;

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



            System.out.println(reader.readLine());

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

                if (resposta == null) {
                    System.out.println("Servidor ha tancat la connexió");
                    break;
                }

                System.out.println("Servidor: " + resposta);

                if (resposta.contains("guanyat")) {
                    break;
                }
            }

            scanner.close();
        } catch (IOException e) {
            System.out.println("Connexió tancada pel servidor");
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