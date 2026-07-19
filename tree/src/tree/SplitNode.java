package tree;

import java.util.ArrayList;
import java.util.List;
import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import java.io.Serializable;

/**
 * Classe astratta che modella un nodo interno dell'albero di regressione.
 * Contiene le informazioni sullo split e i riferimenti ai nodi figli.
 */
public abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {

    /**
     * Classe interna che modella le informazioni di uno split.
     */
    class SplitInfo implements Serializable {
        /** Valore dello split. */
        private Object splitValue;
        /** Indice iniziale degli esempi del figlio. */
        private int beginIndex;
        /** Indice finale degli esempi del figlio. */
        private int endIndex;
        /** Numero del figlio. */
        private int numberChild;
        /** Operatore di confronto dello split. */
        private String comparator = "=";

        /**
         * Costruttore per split discreti.
         * @param splitValue valore dello split.
         * @param beginIndex indice iniziale.
         * @param endIndex indice finale.
         * @param numberChild numero del figlio.
         */
        SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
            this.splitValue = splitValue;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.numberChild = numberChild;
        }

        /**
         * Costruttore per split continui.
         * @param splitValue valore dello split.
         * @param beginIndex indice iniziale.
         * @param endIndex indice finale.
         * @param numberChild numero del figlio.
         * @param comparator operatore di confronto.
         */
        SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
            this.splitValue = splitValue;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.numberChild = numberChild;
            this.comparator = comparator;
        }

        /**
         * Restituisce il valore dello split.
         * @return valore dello split.
         */
        Object getSplitValue() {
            return splitValue;
        }

        /**
         * Restituisce l'operatore di confronto.
         * @return operatore di confronto.
         */
        String getComparator() {
            return comparator;
        }

        /**
         * Restituisce l'indice iniziale degli esempi del figlio.
         * @return indice iniziale.
         */
        int getBeginIndex() {
            return beginIndex;
        }

        /**
         * Restituisce l'indice finale degli esempi del figlio.
         * @return indice finale.
         */
        int getEndIndex() {
            return endIndex;
        }

        /**
         * Restituisce una rappresentazione testuale dello split.
         * @return stringa descrittiva dello split.
         */
        public String toString() {
            return "[Examples:" + beginIndex + "-" + endIndex + "]" +
                    " child=" + numberChild +
                    " splitValue=" + splitValue +
                    " comparator=" + comparator;
        }
    }

    /** Attributo rispetto al quale viene effettuato lo split. */
    private Attribute attribute;
    /** Lista delle informazioni sugli split. */
    List<SplitInfo> mapSplit = null;
    /** Varianza dello split. */
    private double splitVariance;

    /**
     * Costruttore che inizializza il nodo di split.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     * @param attribute attributo rispetto al quale effettuare lo split.
     */
    SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex);
        this.attribute = attribute;
        mapSplit = new ArrayList<SplitInfo>();
        setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
        splitVariance = 0;
        if (mapSplit != null) {
            for (int i = 0; i < mapSplit.size(); i++) {
                int begin = mapSplit.get(i).beginIndex;
                int end = mapSplit.get(i).endIndex;
                for (int j = begin; j <= end; j++) {
                    double diff = trainingSet.getClassValue(j) - mean(trainingSet, begin, end);
                    splitVariance += diff * diff;
                }
            }
        }
    }

    /**
     * Calcola la media dei valori della classe in un intervallo.
     * @param trainingSet dataset di addestramento.
     * @param begin indice iniziale.
     * @param end indice finale.
     * @return media dei valori della classe.
     */
    private double mean(Data trainingSet, int begin, int end) {
        double sum = 0;
        for (int i = begin; i <= end; i++) {
            sum += trainingSet.getClassValue(i);
        }
        return sum / (end - begin + 1);
    }

    /**
     * Calcola le informazioni sullo split per un attributo discreto.
     * @param trainingSet dataset di addestramento.
     * @param beginExampleIndex indice del primo esempio.
     * @param endExampleIndex indice dell'ultimo esempio.
     * @param attribute attributo rispetto al quale effettuare lo split.
     */
    void setSplitInfo(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, Attribute attribute) {
        DiscreteAttribute discAttribute = (DiscreteAttribute) attribute;
        trainingSet.sort(discAttribute, beginExampleIndex, endExampleIndex);
        String currentValue = (String) trainingSet.getExplanatoryValue(
                beginExampleIndex, discAttribute.getIndex());
        int beginPartition = beginExampleIndex;
        int child = 0;
        for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
            String val = (String) trainingSet.getExplanatoryValue(i, discAttribute.getIndex());
            if (!val.equals(currentValue)) {
                mapSplit.add(new SplitInfo(currentValue, beginPartition, i - 1, child));
                child++;
                currentValue = val;
                beginPartition = i;
            }
        }
        mapSplit.add(new SplitInfo(currentValue, beginPartition, endExampleIndex, child));
    }

    /**
     * Confronta due nodi di split in base alla varianza.
     * @param o nodo con cui confrontare.
     * @return -1, 0 o 1 a seconda della varianza.
     */
    @Override
    public int compareTo(SplitNode o) {
        if (this.splitVariance == o.splitVariance) return 0;
        else if (this.splitVariance < o.splitVariance) return -1;
        else return 1;
    }

    /**
     * Verifica la condizione di split per un valore.
     * @param value valore da verificare.
     * @return indice del figlio corrispondente.
     */
    abstract int testCondition(Object value);

    /**
     * Restituisce l'attributo rispetto al quale viene effettuato lo split.
     * @return attributo dello split.
     */
    Attribute getAttribute() {
        return attribute;
    }

    /**
     * Restituisce la varianza dello split.
     * @return varianza dello split.
     */
    public double getSplitVariance() {
        return splitVariance;
    }

    /**
     * Restituisce il numero di figli del nodo.
     * @return numero di figli.
     */
    public int getNumberOfChildren() {
        if (mapSplit == null) return 0;
        return mapSplit.size();
    }

    /**
     * Restituisce le informazioni sullo split per un figlio.
     * @param child indice del figlio.
     * @return oggetto SplitInfo del figlio.
     */
    SplitInfo getSplitInfo(int child) {
        return mapSplit.get(child);
    }

    /**
     * Formula la query per la predizione.
     * @return stringa con nome attributo e operatore.
     */
    String formulateQuery() {
        return attribute.getName() + "=";
    }

    /**
     * Restituisce una rappresentazione testuale del nodo di split.
     * @return stringa descrittiva del nodo.
     */
    public String toString() {
        String s = "DISCRETE SPLIT : attribute=" + attribute.getName() +
                " Nodo: [Examples:" + getBeginExampleIndex() +
                "-" + getEndExampleIndex() + "]" +
                " variance:" + super.getVariance() + "\n" +
                "Split Variance: " + splitVariance + "\n";
        for (int i = 0; i < mapSplit.size(); i++) {
            s += "child " + i + " split value=" + mapSplit.get(i).splitValue +
                    "[Examples:" + mapSplit.get(i).beginIndex +
                    "-" + mapSplit.get(i).endIndex + "]\n";
        }
        return s;
    }
}