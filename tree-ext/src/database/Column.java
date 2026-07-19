package database;

/**
 * Classe che modella una colonna di una tabella nel database relazionale.
 * Contiene il nome e il tipo della colonna.
 */
public class Column {

    /** Nome della colonna. */
    private String name;
    /** Tipo della colonna (string o number). */
    private String type;

    /**
     * Costruttore che inizializza nome e tipo della colonna.
     * @param name nome della colonna.
     * @param type tipo della colonna.
     */
    Column(String name, String type) {
        this.name = name;
        this.type = type;
    }

    /**
     * Restituisce il nome della colonna.
     * @return nome della colonna.
     */
    public String getColumnName() {
        return name;
    }

    /**
     * Verifica se la colonna contiene valori numerici.
     * @return true se il tipo è number, false altrimenti.
     */
    public boolean isNumber() {
        return type.equals("number");
    }

    /**
     * Restituisce una rappresentazione testuale della colonna.
     * @return stringa nel formato nome:tipo.
     */
    public String toString() {
        return name + ":" + type;
    }
}