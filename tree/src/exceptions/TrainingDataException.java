package exceptions;

/**
 * Eccezione che modella un errore nei dati di addestramento.
 * Viene sollevata quando i dati letti dal database non sono validi.
 */
public class TrainingDataException extends Exception {

    /**
     * Costruttore che riceve il messaggio di errore.
     * @param msg messaggio descrittivo dell'errore.
     */
    public TrainingDataException(String msg) {
        super(msg);
    }
}