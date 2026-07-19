package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiServer {

    private int PORT = 8080;

    public MultiServer(int port) {
        this.PORT = port;
        run();
    }

    private void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server avviato sulla porta " + PORT);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Nuova connessione da: " + socket.getInetAddress());
                try {
                    new ServerOneClient(socket);
                } catch (IOException e) {
                    System.out.println("Errore nella connessione: " + e.getMessage());
                    socket.close();
                }
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        new MultiServer(8080);
    }
}