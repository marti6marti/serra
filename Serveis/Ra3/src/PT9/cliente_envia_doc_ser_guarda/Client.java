package PT9.cliente_envia_doc_ser_guarda;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;

    public void connecta(String ip, int port) {
        try {
            socket = new Socket(ip, port);
            System.out.println("Connectat al servidor!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviaFitxer(String path) {
        try {
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            File fitxer = new File(path);

            // 1. Envia nom del fitxer
            dos.writeUTF(fitxer.getName());

            // 2. Envia mida del fitxer
            dos.writeLong(fitxer.length());

            // 3. Envia contingut
            FileInputStream fis = new FileInputStream(fitxer);
            byte[] buffer = new byte[4096];
            int bytesLlegits;

            while ((bytesLlegits = fis.read(buffer)) != -1) {
                dos.write(buffer, 0, bytesLlegits);
            }

            fis.close();
            dos.flush();
            System.out.println("Fitxer enviat!");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Client client = new Client();
        client.connecta("127.0.0.1", 6000);
        client.enviaFitxer("prova.txt");  // crea este fichero antes
    }
}