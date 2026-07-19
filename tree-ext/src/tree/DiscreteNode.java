package tree;

import java.io.Serializable;
import data.Data;
import data.DiscreteAttribute;

/**
 * Classe che modella un nodo di split per attributi discreti.
 * Estende SplitNode per gestire attributi categorici.
 */
public class DiscreteNode extends SplitNode implements Serializable {

    /**
     * Costruttore che inizializza il nodo per un attributo discreto.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     * @param attribute attributo discreto rispetto al quale effettuare lo split.
     */
    public DiscreteNode(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, DiscreteAttribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    }

    /**
     * Verifica la condizione di split per un valore discreto.
     * @param value valore da verificare.
     * @return indice del figlio corrispondente al valore.
     */
    public int testCondition(Object value) {
        int i = 0;
        for (SplitInfo info : mapSplit) {
            if (value.equals(info.getSplitValue())) return i;
            i++;
        }
        return -1;
    }
}