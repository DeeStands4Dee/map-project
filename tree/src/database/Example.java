package database;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Classe che modella una transazione (riga) letta dalla base di dati.
 * Ogni transazione è una lista di valori oggetto.
 */
public class Example implements Comparable<Example>, Iterable<Object> {

    /** Lista dei valori che compongono la transazione. */
    private List<Object> example = new ArrayList<Object>();

    /**
     * Aggiunge un valore alla transazione.
     * @param o valore da aggiungere.
     */
    public void add(Object o) {
        example.add(o);
    }

    /**
     * Restituisce il valore in posizione i.
     * @param i indice del valore.
     * @return valore in posizione i.
     */
    public Object get(int i) {
        return example.get(i);
    }

    /**
     * Confronta questa transazione con un'altra.
     * @param ex transazione con cui confrontare.
     * @return 0 se uguali, valore negativo o positivo altrimenti.
     */
    public int compareTo(Example ex) {
        int i = 0;
        for (Object o : ex.example) {
            if (!o.equals(this.example.get(i)))
                return ((Comparable) o).compareTo(example.get(i));
            i++;
        }
        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale della transazione.
     * @return stringa con i valori separati da spazio.
     */
    public String toString() {
        String str = "";
        for (Object o : example)
            str += o.toString() + " ";
        return str;
    }

    /**
     * Restituisce un iteratore sulla lista dei valori.
     * @return iteratore sugli elementi della transazione.
     */
    @Override
    public Iterator<Object> iterator() {
        return example.iterator();
    }
}