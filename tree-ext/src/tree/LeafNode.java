package tree;

import java.io.Serializable;
import data.Data;

/**
 * Classe che modella un nodo foglia dell'albero di regressione.
 * Contiene il valore medio della classe per gli esempi che copre.
 */
public class LeafNode extends Node implements Serializable {

    /** Valore medio della classe predetta dal nodo foglia. */
    private double predictedClassValue;

    /**
     * Costruttore che calcola il valore medio della classe.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     */
    public LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
        super(trainingSet, beginExampleIndex, endExampleIndex);
        double sum = 0;
        for (int i = beginExampleIndex; i <= endExampleIndex; i++)
            sum += trainingSet.getClassValue(i);
        predictedClassValue = sum / (endExampleIndex - beginExampleIndex + 1);
    }

    /**
     * Restituisce il valore della classe predetta dal nodo foglia.
     * @return valore medio della classe.
     */
    public double getPredictedClassValue() {
        return predictedClassValue;
    }

    /**
     * Restituisce 0 poiché un nodo foglia non ha figli.
     * @return 0.
     */
    public int getNumberOfChildren() {
        return 0;
    }

    /**
     * Restituisce una rappresentazione testuale del nodo foglia.
     * @return stringa con il valore predetto e gli esempi coperti.
     */
    public String toString() {
        return "LEAF : class=" + predictedClassValue +
                " Nodo: [Examples:" + getBeginExampleIndex() +
                "-" + getEndExampleIndex() + "]" +
                " variance:" + getVariance();
    }
}