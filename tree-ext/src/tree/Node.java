package tree;

import java.io.Serializable;
import data.Data;

/**
 * Classe astratta che modella un nodo generico dell'albero di regressione.
 * Ogni nodo contiene informazioni sugli esempi che copre e la loro varianza.
 */
public abstract class Node implements Serializable {

    /** Indice del primo esempio coperto dal nodo. */
    private int beginExampleIndex;
    /** Indice dell'ultimo esempio coperto dal nodo. */
    private int endExampleIndex;
    /** Varianza degli esempi coperti dal nodo. */
    private double variance;

    /**
     * Costruttore che inizializza il nodo calcolando la varianza degli esempi.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     */
    public Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
        this.beginExampleIndex = beginExampleIndex;
        this.endExampleIndex = endExampleIndex;
        double mean = 0;
        for (int i = beginExampleIndex; i <= endExampleIndex; i++)
            mean += trainingSet.getClassValue(i);
        mean /= (endExampleIndex - beginExampleIndex + 1);
        double sumSquared = 0;
        for (int i = beginExampleIndex; i <= endExampleIndex; i++) {
            double diff = trainingSet.getClassValue(i) - mean;
            variance += diff * diff;
        }
    }

    /**
     * Restituisce l'indice del primo esempio coperto dal nodo.
     * @return indice del primo esempio.
     */
    public int getBeginExampleIndex() {
        return beginExampleIndex;
    }

    /**
     * Restituisce l'indice dell'ultimo esempio coperto dal nodo.
     * @return indice dell'ultimo esempio.
     */
    public int getEndExampleIndex() {
        return endExampleIndex;
    }

    /**
     * Restituisce la varianza degli esempi coperti dal nodo.
     * @return varianza del nodo.
     */
    public double getVariance() {
        return variance;
    }

    /**
     * Restituisce il numero di figli del nodo.
     * @return numero di figli.
     */
    public abstract int getNumberOfChildren();

    /**
     * Restituisce una rappresentazione testuale del nodo.
     * @return stringa descrittiva del nodo.
     */
    public abstract String toString();
}