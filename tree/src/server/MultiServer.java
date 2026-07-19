package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Classe che implementa un server multi-client per il processo di data mining.
 * Rimane in ascolto su una porta e istanzia un thread per ogni client connesso.
 */
public class MultiServer {

    /** Porta su cui il server è in ascolto. */
    private int PORT = 8080;

    /**
     * Costruttore che inizializza la porta e avvia il server.
     * @param port porta su cui il server deve essere in ascolto.
     */
    public MultiServer(int port) {
        this.PORT = port;
        run();
    }

    /**
     * Avvia il server e rimane in attesa di connessioni dai client.
     * Per ogni nuova connessione istanzia un oggetto ServerOneClient.
     */
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

    /**
     * Metodo main che avvia il server sulla porta 8080.
     * @param args argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        new MultiServer(8080);
    }
}