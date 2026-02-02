package PT9.cliente_envia_doc_ser_guarda;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    public static final int PORT = 6000;

    public void iniciaServei() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Servidor iniciat al port: " + PORT);

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Client connectat!");
                gestionaClient(socket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void gestionaClient(Socket socket) {
        try {
            DataInputStream dis = new DataInputStream(socket.getInputStream());

            // 1. Rep nom del fitxer
            String nomFitxer = dis.readUTF();
            System.out.println("Nom del fitxer: " + nomFitxer);

            // 2. Rep mida del fitxer
            long mida = dis.readLong();
            System.out.println("Mida: " + mida + " bytes");

            // 3. Rep contingut i guarda
            FileOutputStream fos = new FileOutputStream("rebut_" + nomFitxer);
            byte[] buffer = new byte[4096];
            int bytesLlegits;
            long totalLlegits = 0;

            while (totalLlegits < mida) {
                bytesLlegits = dis.read(buffer);
                fos.write(buffer, 0, bytesLlegits);
                totalLlegits += bytesLlegits;
            }

            fos.close();
            System.out.println("Fitxer guardat correctament!");

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server();
        server.iniciaServei();
    }
}