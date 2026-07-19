package database;

/**
 * Eccezione che modella la restituzione di un result set vuoto
 * a seguito di una interrogazione alla base di dati.
 */
public class EmptySetException extends Exception {

    /**
     * Costruttore che riceve il messaggio di errore.
     * @param msg messaggio descrittivo dell'errore.
     */
    public EmptySetException(String msg) {
        super(msg);
    }
}