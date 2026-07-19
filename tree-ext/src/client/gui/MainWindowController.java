package client.gui;

import javafx.scene.layout.HBox;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller della finestra principale dell'applicazione client.
 * Gestisce la comunicazione con il server e l'interazione con l'utente
 * tramite l'interfaccia grafica JavaFX.
 */
public class MainWindowController {
	
	/**
	 * Costruttore di default.
	 */
	public MainWindowController() {}

    @FXML private TextField serverField;
    @FXML private TextField portField;
    @FXML private TextField tableField;
    @FXML private Button connectButton;
    @FXML private Button learnButton;
    @FXML private Button loadButton;
    @FXML private Button predictButton;
    @FXML private TextArea logArea;
    @FXML private ComboBox<String> choiceBox;
    @FXML private HBox predictionBox;
    @FXML private Label resultLabel;
    @FXML private Button resetButton;
    @FXML private Button disconnectButton;
    @FXML private TextArea rulesArea;

    /** Socket per la comunicazione con il server. */
    private Socket socket;
    /** Stream per la ricezione di oggetti dal server. */
    private ObjectInputStream in;
    /** Stream per l'invio di oggetti al server. */
    private ObjectOutputStream out;
    /** Flag che indica se l'albero è pronto per la predizione. */
    private volatile boolean treeReady = false;

    /**
     * Gestisce la connessione al server.
     * Legge indirizzo e porta dai campi di testo e stabilisce la connessione.
     */
    @FXML
    private void handleConnect() {
        new Thread(() -> {
            try {
                String server = serverField.getText();
                int port = Integer.parseInt(portField.getText());
                socket = new Socket(server, port);
                out = new ObjectOutputStream(socket.getOutputStream());
                in = new ObjectInputStream(socket.getInputStream());
                log("Connesso a " + server + ":" + port);
                Platform.runLater(() -> {
                    learnButton.setDisable(false);
                    loadButton.setDisable(false);
                    connectButton.setDisable(true);
                    disconnectButton.setDisable(false);
                });
            } catch (IOException e) {
                log("Errore connessione: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Gestisce la disconnessione dal server.
     * Chiude il socket e reimposta lo stato dell'interfaccia.
     */
    @FXML
    private void handleDisconnect() {
        new Thread(() -> {
            try {
                if (socket != null && !socket.isClosed()) {
                    socket.close();
                }
                log("Disconnesso dal server.");
                treeReady = false;
                Platform.runLater(() -> {
                    connectButton.setDisable(false);
                    disconnectButton.setDisable(true);
                    learnButton.setDisable(true);
                    loadButton.setDisable(true);
                    predictButton.setDisable(true);
                    predictionBox.setDisable(true);
                    choiceBox.getItems().clear();
                    resultLabel.setText("");
                    rulesArea.setText("");
                });
            } catch (IOException e) {
                log("Errore disconnessione: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Gestisce il reset della predizione.
     * Pulisce la ComboBox e il risultato, invia segnale di reset al server.
     */
    @FXML
    private void handleReset() {
        new Thread(() -> {
            try {
                log("--- Reset predizione ---");
                if (out != null) {
                    out.writeObject(-1);
                    in.readObject();
                }
                Platform.runLater(() -> {
                    choiceBox.getItems().clear();
                    predictionBox.setDisable(true);
                    resultLabel.setText("");
                });
            } catch (IOException | ClassNotFoundException e) {
                log("Errore reset: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Gestisce l'apprendimento dell'albero dal database.
     * Invia il nome della tabella al server e riceve le regole dell'albero.
     */
    @FXML
    private void handleLearn() {
        String table = tableField.getText();
        new Thread(() -> {
            try {
                log("Caricamento dati dalla tabella: " + table);
                out.writeObject(0);
                out.writeObject(table);
                String answer = in.readObject().toString();
                if (!answer.equals("OK")) {
                    log("Errore: " + answer);
                    return;
                }
                log("Apprendimento albero...");
                out.writeObject(1);
                answer = in.readObject().toString();
                if (!answer.equals("OK")) {
                    log("Errore: " + answer);
                    return;
                }
                log("Albero appreso con successo!");
                String rules = in.readObject().toString();
                treeReady = true;
                Platform.runLater(() -> {
                    rulesArea.setText(rules);
                    predictButton.setDisable(false);
                });
            } catch (IOException | ClassNotFoundException e) {
                log("Errore: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Gestisce il caricamento dell'albero da file.
     * Invia il nome del file al server e riceve le regole dell'albero.
     */
    @FXML
    private void handleLoad() {
        String table = tableField.getText();
        new Thread(() -> {
            try {
                log("Caricamento albero dal file: " + table + ".dmp");
                out.writeObject(2);
                out.writeObject(table);
                String answer = in.readObject().toString();
                if (!answer.equals("OK")) {
                    log("Errore: " + answer);
                    return;
                }
                log("Albero caricato con successo!");
                String rules = in.readObject().toString();
                treeReady = true;
                Platform.runLater(() -> {
                    rulesArea.setText(rules);
                    predictButton.setDisable(false);
                });
            } catch (IOException | ClassNotFoundException e) {
                log("Errore: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Gestisce l'avvio della predizione.
     * Invia la richiesta al server e mostra le opzioni nella ComboBox.
     */
    @FXML
    private void handlePredict() {
        if (!treeReady) return;
        new Thread(() -> {
            try {
                out.writeObject(3);
                log("Avvio predizione...");
                Platform.runLater(() -> {
                    resultLabel.setText("");
                    predictionBox.setDisable(false);
                    choiceBox.getItems().clear();
                });
                String answer = in.readObject().toString();
                if (answer.equals("QUERY")) {
                    String options = in.readObject().toString();
                    log("Query: " + options);
                    String[] lines = options.trim().split("\n");
                    Platform.runLater(() -> {
                        choiceBox.getItems().clear();
                        for (String line : lines) {
                            choiceBox.getItems().add(line);
                        }
                        choiceBox.getSelectionModel().selectFirst();
                    });
                } else if (answer.equals("OK")) {
                    String result = in.readObject().toString();
                    log("Classe predetta: " + result);
                    Platform.runLater(() -> {
                        resultLabel.setText("Risultato: " + result);
                        predictionBox.setDisable(true);
                    });
                } else {
                    log("Errore: " + answer);
                }
            } catch (IOException | ClassNotFoundException e) {
                log("Errore: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Gestisce la scelta dell'utente nella ComboBox.
     * Invia la scelta al server e riceve la prossima query o il risultato finale.
     */
    @FXML
    private void handleChoice() {
        String selected = choiceBox.getSelectionModel().getSelectedItem();
        if (selected == null) return;
        new Thread(() -> {
            try {
                int index = Integer.parseInt(selected.split(":")[0].trim());
                out.writeObject(Integer.valueOf(index));
                log("Scelta: " + selected);
                String answer = in.readObject().toString();
                if (answer.equals("QUERY")) {
                    String options = in.readObject().toString();
                    log("Query: " + options);
                    String[] lines = options.trim().split("\n");
                    Platform.runLater(() -> {
                        choiceBox.getItems().clear();
                        for (String line : lines) {
                            choiceBox.getItems().add(line);
                        }
                        choiceBox.getSelectionModel().selectFirst();
                    });
                } else if (answer.equals("OK")) {
                    String result = in.readObject().toString();
                    log("Classe predetta: " + result);
                    Platform.runLater(() -> {
                        resultLabel.setText("Risultato: " + result);
                        predictionBox.setDisable(true);
                    });
                } else {
                    log("Errore: " + answer);
                }
            } catch (IOException | ClassNotFoundException e) {
                log("Errore: " + e.getMessage());
            }
        }).start();
    }

    /**
     * Aggiunge un messaggio al log dell'interfaccia grafica.
     * @param msg messaggio da aggiungere al log.
     */
    private void log(String msg) {
        Platform.runLater(() -> logArea.appendText(msg + "\n"));
    }
}
