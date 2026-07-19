package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import data.Data;
import exceptions.TrainingDataException;
import exceptions.UnknownValueException;
import tree.RegressionTree;

public class ServerOneClient extends Thread {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private RegressionTree tree;
    private Data trainingSet;
    private String tableName;

    public ServerOneClient(Socket s) throws IOException {
        socket = s;
        out = new ObjectOutputStream(socket.getOutputStream());
        in = new ObjectInputStream(socket.getInputStream());
        start();
    }

    public void run() {
        try {
            while (true) {
                int request = (Integer) in.readObject();

                if (request == 0) {
                    tableName = (String) in.readObject();
                    try {
                        trainingSet = new Data(tableName);
                        out.writeObject("OK");
                    } catch (exceptions.TrainingDataException e) {
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
                        e.printStackTrace(); // aggiunge stack trace completo
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
                    // Reset — non fa nulla, il client gestisce il reset
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