package data;

import java.io.Serializable;

/**
 * Classe che modella un attributo continuo (numerico) del dataset.
 * Estende la classe Attribute per attributi con valori reali.
 */
public class ContinuousAttribute extends Attribute implements Serializable {

    /**
     * Costruttore che inizializza nome e indice dell'attributo continuo.
     * @param name nome dell'attributo.
     * @param index indice dell'attributo.
     */
    public ContinuousAttribute(String name, int index) {
        super(name, index);
    }
}