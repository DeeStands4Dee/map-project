package data;

import java.io.Serializable;

/**
 * Classe astratta che modella un attributo del dataset.
 * Ogni attributo ha un nome e un indice che ne identifica la posizione.
 */
public abstract class Attribute implements Serializable {

    /** Nome dell'attributo. */
    private String name;
    /** Indice dell'attributo nel dataset. */
    private int index;

    /**
     * Costruttore che inizializza nome e indice dell'attributo.
     * @param name nome dell'attributo.
     * @param index indice dell'attributo.
     */
    public Attribute(String name, int index) {
        this.name = name;
        this.index = index;
    }

    /**
     * Restituisce il nome dell'attributo.
     * @return nome dell'attributo.
     */
    public String getName() {
        return name;
    }

    /**
     * Restituisce l'indice dell'attributo.
     * @return indice dell'attributo.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Restituisce una rappresentazione testuale dell'attributo.
     * @return stringa con nome e indice dell'attributo.
     */
    public String toString() {
        return name + "(" + index + ")";
    }
}