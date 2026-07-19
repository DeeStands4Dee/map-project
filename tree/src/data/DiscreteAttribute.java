package data;

import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.io.Serializable;

/**
 * Classe che modella un attributo discreto (categorico) del dataset.
 * Contiene l'insieme dei valori distinti che l'attributo può assumere.
 */
public class DiscreteAttribute extends Attribute implements Iterable<String>, Serializable {

    /** Insieme ordinato dei valori distinti dell'attributo. */
    private Set<String> values = new TreeSet<>();

    /**
     * Costruttore che inizializza nome, indice e valori dell'attributo discreto.
     * @param name nome dell'attributo.
     * @param index indice dell'attributo.
     * @param values insieme dei valori distinti.
     */
    public DiscreteAttribute(String name, int index, Set<String> values) {
        super(name, index);
        this.values = values;
    }

    /**
     * Restituisce il numero di valori distinti dell'attributo.
     * @return numero di valori distinti.
     */
    public int getNumberOfDistinctValues() {
        return values.size();
    }

    /**
     * Restituisce un iteratore sui valori distinti dell'attributo.
     * @return iteratore sui valori distinti.
     */
    @Override
    public Iterator<String> iterator() {
        return values.iterator();
    }
}