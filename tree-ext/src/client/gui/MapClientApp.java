package client.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe principale dell'applicazione client con interfaccia grafica JavaFX.
 * Carica il file FXML e avvia la finestra principale dell'applicazione.
 */
public class MapClientApp extends Application {

    /**
     * Metodo di avvio dell'applicazione JavaFX.
     * Carica il layout FXML e configura la finestra principale.
     * @param primaryStage finestra principale dell'applicazione.
     * @throws Exception se si verifica un errore nel caricamento del file FXML.
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource("MainWindow.fxml")
        );
        Parent root = loader.load();
        Scene scene = new Scene(root);
        primaryStage.setTitle("MAP - Regression Tree Client");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(600);
        primaryStage.setMinHeight(400);
        primaryStage.show();
    }

    /**
     * Metodo main che avvia l'applicazione JavaFX.
     * @param args argomenti della riga di comando (non utilizzati).
     */
    public static void main(String[] args) {
        launch(args);
    }
}