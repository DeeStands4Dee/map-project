package tree;

import java.util.ArrayList;
import java.util.List;
import data.Attribute;
import data.Data;
import data.DiscreteAttribute;
import java.io.Serializable;

public abstract class SplitNode extends Node implements Comparable<SplitNode>, Serializable {
    class SplitInfo implements Serializable {
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
    List<SplitInfo> mapSplit = null;
    private double splitVariance;

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
    private double mean(Data trainingSet, int begin, int end) {
        double sum = 0;
        for (int i = begin; i <= end; i++) {
            sum += trainingSet.getClassValue(i);
        }
        return sum / (end - begin + 1);
    }

    void setSplitInfo(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, Attribute attribute) {
    	
    	mapSplit = new ArrayList<SplitInfo>();

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

    @Override
    public int compareTo(SplitNode o) {
        if (this.splitVariance == o.splitVariance)
            return 0;
        else if (this.splitVariance < o.splitVariance)
            return -1;
        else
            return 1;
    }

    abstract int testCondition(Object value);

    Attribute getAttribute() {
        return attribute;
    }

    public double getSplitVariance() {
        return splitVariance;
    }

    public int getNumberOfChildren() {
        if (mapSplit == null) return 0;
        return mapSplit.size();
    }

    SplitInfo getSplitInfo(int child) {
        return mapSplit.get(child);
    }

    String formulateQuery() {
        return attribute.getName() + "=";
    }

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