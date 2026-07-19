package database;

/**
 * Eccezione che modella il fallimento nella connessione al database.
 */
public class DatabaseConnectionException extends Exception {
    
    /**
     * Costruttore che riceve il messaggio di errore.
     * @param msg messaggio descrittivo dell'errore.
     */
    public DatabaseConnectionException(String msg) {
        super(msg);
    }
}