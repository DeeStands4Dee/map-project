package tree;
import data.Attribute;
import data.Data;
import data.DiscreteAttribute;

public abstract class SplitNode extends Node {

    class SplitInfo {
        
        private Object splitValue;
        private int beginIndex;
        private int endIndex;
        private int numberChild;
        private String comparator = "=";

        SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild) {
            this.splitValue = splitValue;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.numberChild = numberChild;
        }

        SplitInfo(Object splitValue, int beginIndex, int endIndex, int numberChild, String comparator) {
            this.splitValue = splitValue;
            this.beginIndex = beginIndex;
            this.endIndex = endIndex;
            this.numberChild = numberChild;
            this.comparator = comparator;
        }

        Object getSplitValue() {
            return splitValue;
        }

        String getComparator() {
            return comparator;
        }
        
        int getBeginIndex() {
            return beginIndex;
        }

        int getEndIndex() {
            return endIndex;
        }

        public String toString() {
            return "[Examples:" + beginIndex + "-" + endIndex + "]" +
                   " child=" + numberChild +
                   " splitValue=" + splitValue +
                   " comparator=" + comparator;
        }
    }
    
    private Attribute attribute;
    SplitInfo mapSplit[];
    private double splitVariance;

    SplitNode(Data trainingSet, int beginExampleIndex, int endExampleIndex, Attribute attribute) {
    super(trainingSet, beginExampleIndex, endExampleIndex);
    this.attribute = attribute;
    setSplitInfo(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    
        
        splitVariance = 0;
        for (int i = 0; i < mapSplit.length; i++) {
            int begin = mapSplit[i].beginIndex;
            int end = mapSplit[i].endIndex;
            for (int j = begin; j <= end; j++) {
                double diff = (Double) trainingSet.getClassValue(j) - mean(trainingSet, begin, end);
                splitVariance += diff * diff;
            }
        }
    }

    private double mean(Data trainingSet, int begin, int end) {
        double sum = 0;
        for (int i = begin; i <= end; i++) {
            sum += (Double) trainingSet.getClassValue(i);
        }
        return sum / (end - begin + 1);
    }

    void setSplitInfo(Data trainingSet, int beginExampleIndex, 
                  int endExampleIndex, Attribute attribute) {
    
    DiscreteAttribute discAttribute = (DiscreteAttribute) attribute;
    
    // Ordina prima di tutto
    trainingSet.sort(discAttribute, beginExampleIndex, endExampleIndex);
    
    String currentValue = (String) trainingSet.getExplanatoryValue(
        beginExampleIndex, discAttribute.getIndex());
    int beginPartition = beginExampleIndex;
    int numSplit = 1;
    
    for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
        String val = (String) trainingSet.getExplanatoryValue(
            i, discAttribute.getIndex());
        if (!val.equals(currentValue)) {
            numSplit++;
            currentValue = val;
        }
    }
    
    mapSplit = new SplitInfo[numSplit];
    
    currentValue = (String) trainingSet.getExplanatoryValue(
        beginExampleIndex, discAttribute.getIndex());
    beginPartition = beginExampleIndex;
    int child = 0;
    
    for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
        String val = (String) trainingSet.getExplanatoryValue(
            i, discAttribute.getIndex());
        if (!val.equals(currentValue)) {
            mapSplit[child] = new SplitInfo(currentValue, beginPartition, i - 1, child);
            child++;
            currentValue = val;
            beginPartition = i;
        }
    }
    mapSplit[child] = new SplitInfo(currentValue, beginPartition, endExampleIndex, child);
}

    abstract int testCondition(Object value);

    Attribute getAttribute() {
        return attribute;
    }

    public double getVariance() {
        return splitVariance;
    }

    public int getNumberOfChildren() {
        return mapSplit.length;
    }

    SplitInfo getSplitInfo(int child) {
        return mapSplit[child];
    }

    String formulateQuery() {
    return attribute.getName() + "=";
}

    public String toString() {
        String s = "DISCRETE SPLIT : attribute=" + attribute.getName() +
                   " Nodo: [Examples:" + getBeginExampleIndex() + 
                   "-" + getEndExampleIndex() + "]" +
                   " variance:" + getVariance() + "\n";
        for (int i = 0; i < mapSplit.length; i++) {
            s += "child " + i + " split value=" + mapSplit[i].splitValue +
                 "[Examples:" + mapSplit[i].beginIndex + 
                 "-" + mapSplit[i].endIndex + "]\n";
        }
        return s;
    }
}
