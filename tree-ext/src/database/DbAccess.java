package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe che gestisce l'accesso alla base di dati MySQL.
 * Fornisce metodi per inizializzare, ottenere e chiudere la connessione.
 */
public class DbAccess {

    /** Nome della classe del driver JDBC per MySQL. */
    private final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";
    /** Prefisso del DBMS per la stringa di connessione. */
    private final String DBMS = "jdbc:mysql";
    /** Indirizzo del server su cui risiede il database. */
    private String SERVER = "localhost";
    /** Nome del database a cui connettersi. */
    private String DATABASE = "MapDB";
    /** Porta su cui MySQL accetta le connessioni. */
    private final int PORT = 3306;
    /** Nome utente per l'accesso al database. */
    private String USER_ID = "MapUser";
    /** Password per l'autenticazione dell'utente. */
    private String PASSWORD = "map";
    /** Oggetto che gestisce la connessione al database. */
    private Connection conn;

    /**
     * Inizializza la connessione al database.
     * Carica il driver MySQL e stabilisce la connessione.
     * @throws DatabaseConnectionException se la connessione fallisce.
     */
    public void initConnection() throws DatabaseConnectionException {
        try {
            Class.forName(DRIVER_CLASS_NAME);
            conn = DriverManager.getConnection(
                DBMS + "://" + SERVER + ":" + PORT + "/" + DATABASE +
                "?user=" + USER_ID + "&password=" + PASSWORD +
                "&serverTimezone=UTC"
            );
        } catch (ClassNotFoundException e) {
            throw new DatabaseConnectionException("Driver non trovato: " + e.getMessage());
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Connessione fallita: " + e.getMessage());
        }
    }

    /**
     * Restituisce la connessione al database.
     * @return oggetto Connection attivo.
     */
    public Connection getConnection() {
        return conn;
    }

    /**
     * Chiude la connessione al database.
     */
    public void closeConnection() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
    /**
     * Costruttore di default.
     */
    public DbAccess() {}
}