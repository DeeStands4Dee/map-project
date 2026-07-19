package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Data;
import exceptions.TrainingDataException;
import exceptions.UnknownValueException;
import tree.RegressionTree;

/**
 * Classe che gestisce la comunicazione con un singolo client.
 * Estende Thread per gestire ogni client in modo concorrente.
 * Gestisce le richieste del client per l'apprendimento e la predizione
 * dell'albero di regressione.
 */
public class ServerOneClient extends Thread {

    /** Socket per la comunicazione con il client. */
    private Socket socket;
    /** Stream per la ricezione di oggetti dal client. */
    private ObjectInputStream in;
    /** Stream per l'invio di oggetti al client. */
    private ObjectOutputStream out;
    /** Albero di regressione appreso o caricato. */
    private RegressionTree tree;
    /** Dati di addestramento caricati dal database. */
    private Data trainingSet;
    /** Nome della tabella utilizzata per l'addestramento. */
    private String tableName;

    /**
     * Costruttore che inizializza gli stream e avvia il thread.
     * @param s socket per la comunicazione con il client.
     * @throws IOException se si verifica un errore nell'inizializzazione degli stream.
     */
    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        start();
    }

    /**
     * Metodo run del thread che gestisce le richieste del client.
     * Gestisce i seguenti codici di richiesta:
     * 0 - carica dati dal database
     * 1 - apprendi albero di regressione
     * 2 - carica albero da file
     * 3 - avvia predizione
     * -1 - reset predizione
     */
    public void run() {
        try {
            while (true) {
                int request = (Integer) in.readObject();

                if (request == 0) {
                    tableName = (String) in.readObject();
                    try {
                        trainingSet = new Data(tableName);
                        out.writeObject("OK");
                    } catch (TrainingDataException e) {
                        out.writeObject(e.getMessage());
                    }

                } else if (request == 1) {
                    try {
                        tree = new RegressionTree(trainingSet);
                        tree.salva(tableName + ".dmp");
                        out.writeObject("OK");
                        out.flush();
                        String rules = tree.printTree() + "\n" + tree.getRules();
                        out.writeObject(rules);
                        out.flush();
                    } catch (Exception e) {
                        e.printStackTrace();
                        out.writeObject(e.getMessage());
                        out.flush();
                    }

                } else if (request == 2) {
                    tableName = (String) in.readObject();
                    try {
                        tree = RegressionTree.carica(tableName + ".dmp");
                        out.writeObject("OK");
                        out.flush();
                        out.writeObject(tree.printTree() + "\n" + tree.getRules());
                        out.flush();
                    } catch (Exception e) {
                        out.writeObject(e.getMessage());
                        out.flush();
                    }

                } else if (request == 3) {
                    try {
                        predictClass();
                    } catch (Exception e) {
                        try {
                            out.writeObject("ERR " + e.getMessage());
                            out.flush();
                        } catch (IOException ex) {
                            System.out.println("Errore invio: " + ex.getMessage());
                        }
                    }
                } else if (request == -1) {
                    out.writeObject("OK");
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connessione chiusa: " + e.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Gestisce la predizione della classe per il client.
     * Naviga l'albero di regressione in base alle scelte del client.
     * @throws IOException se si verifica un errore nella comunicazione.
     * @throws ClassNotFoundException se la classe dell'oggetto ricevuto non è trovata.
     */
    private void predictClass() throws IOException, ClassNotFoundException {
        if (tree == null) {
            out.writeObject("ERR Albero non disponibile!");
            return;
        }
        Double result = tree.predictClass(in, out);
        if (result != null) {
            out.writeObject("OK");
            out.writeObject(result.toString());
        }
    }
}