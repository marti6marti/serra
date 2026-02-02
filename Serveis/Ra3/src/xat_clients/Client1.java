package xat_clients;

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

//    public void comunicacio() {
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
//            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
//            Scanner scanner = new Scanner(System.in);
//
//
//            while (true) {
//                System.out.print("Tu: ");
//                String missatge = scanner.nextLine();
//
//                writer.write(missatge);
//                writer.newLine();
//                writer.flush();
//
//                if (missatge.equalsIgnoreCase("exit")) {
//                    break;
//                }
//
//                String resposta = reader.readLine();
//
//                if (resposta == null) {
//                    System.out.println("Servidor ha tancat la connexió");
//                    break;
//                }
//
//                System.out.println(resposta);
//            }
//
//            scanner.close();
//        } catch (IOException e) {
//            System.out.println("Connexió tancada pel servidor");
//        }
//    }


    public void comunicacio() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(output));
        Scanner scanner = new Scanner(System.in);

        // fil de lectura
        new Thread(() -> {
            try {
                String resposta;
                while ((resposta = reader.readLine()) != null) {
                    System.out.println(resposta);
                }
            } catch (IOException e) {
                System.out.println("Connexió tancada");
            }
        }).start();

        // fil principal que escriu
        while (true) {
            int missatge = (int)(Math.random() * 10) + 1;;

            try {
                writer.write(missatge);
                writer.newLine();
                writer.flush();
            } catch (IOException e) {
                break;
            }

        }

        scanner.close();
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