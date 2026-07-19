package tree;

import java.util.ArrayList;
import java.util.List;
import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import java.io.Serializable;

/**
 * Classe che modella un nodo di split per attributi continui.
 * Determina il miglior punto di split minimizzando la varianza.
 */
public class ContinuousNode extends SplitNode implements Serializable {

    /**
     * Costruttore che inizializza il nodo per un attributo continuo.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     * @param attribute attributo continuo rispetto al quale effettuare lo split.
     */
    public ContinuousNode(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, ContinuousAttribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    }

    /**
     * Calcola il miglior split per un attributo continuo minimizzando la varianza.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     * @param attribute attributo rispetto al quale effettuare lo split.
     */
    void setSplitInfo(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, Attribute attribute) {
        mapSplit = new ArrayList<SplitInfo>();
        Double currentSplitValue = (Double) trainingSet.getExplanatoryValue(
                beginExampleIndex, attribute.getIndex());
        double bestInfoVariance = 0;
        List<SplitInfo> bestMapSplit = null;

        for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
            Double value = (Double) trainingSet.getExplanatoryValue(i, attribute.getIndex());
            if (value.doubleValue() != currentSplitValue.doubleValue()) {
                double localVariance = new LeafNode(trainingSet, beginExampleIndex, i - 1).getVariance();
                double candidateSplitVariance = localVariance;
                localVariance = new LeafNode(trainingSet, i, endExampleIndex).getVariance();
                candidateSplitVariance += localVariance;
                if (bestMapSplit == null) {
                    bestMapSplit = new ArrayList<SplitInfo>();
                    bestMapSplit.add(new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
                    bestMapSplit.add(new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
                    bestInfoVariance = candidateSplitVariance;
                } else {
                    if (candidateSplitVariance < bestInfoVariance) {
                        bestInfoVariance = candidateSplitVariance;
                        bestMapSplit.set(0, new SplitInfo(currentSplitValue, beginExampleIndex, i - 1, 0, "<="));
                        bestMapSplit.set(1, new SplitInfo(currentSplitValue, i, endExampleIndex, 1, ">"));
                    }
                }
                currentSplitValue = value;
            }
        }
        if (bestMapSplit != null) {
            mapSplit = bestMapSplit;
        } else {
            mapSplit = new ArrayList<SplitInfo>();
            return;
        }
        if (mapSplit.size() < 2) return;
        if (mapSplit.get(1).getBeginIndex() == mapSplit.get(1).getEndIndex()) {
            mapSplit.remove(1);
        }
    }

    /**
     * Verifica la condizione di split per un valore continuo.
     * @param value valore da verificare.
     * @return 0 se il valore è minore o uguale al punto di split, 1 altrimenti.
     */
    public int testCondition(Object value) {
        Double v = (Double) value;
        Double splitValue = (Double) mapSplit.get(0).getSplitValue();
        if (v <= splitValue) return 0;
        else return 1;
    }

    /**
     * Restituisce una rappresentazione testuale del nodo continuo.
     * @return stringa descrittiva del nodo.
     */
    public String toString() {
        String s = "CONTINUOUS SPLIT : attribute=" + getAttribute().getName() +
                " Nodo: [Examples:" + getBeginExampleIndex() +
                "-" + getEndExampleIndex() + "]" +
                " variance:" + super.getVariance() + "\n" +
                "Split Variance: " + getSplitVariance() + "\n";
        for (int i = 0; i < mapSplit.size(); i++) {
            s += "child " + i + " split value" + mapSplit.get(i).getComparator() +
                    mapSplit.get(i).getSplitValue() +
                    "[Examples:" + mapSplit.get(i).getBeginIndex() +
                    "-" + mapSplit.get(i).getEndIndex() + "]\n";
        }
        return s;
    }
}