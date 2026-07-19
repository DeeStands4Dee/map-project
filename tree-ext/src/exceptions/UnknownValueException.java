package exceptions;

/**
 * Eccezione che modella una risposta non valida durante la predizione.
 * Viene sollevata quando l'utente fornisce un valore non ammesso.
 */
public class UnknownValueException extends Exception {

    /**
     * Costruttore che riceve il messaggio di errore.
     * @param msg messaggio descrittivo dell'errore.
     */
    public UnknownValueException(String msg) {
        super(msg);
    }
}