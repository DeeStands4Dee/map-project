package tree;

import java.util.ArrayList;
import java.util.List;
import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import java.io.Serializable;

public class ContinuousNode extends SplitNode implements Serializable {

    public ContinuousNode(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, ContinuousAttribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    }

    void setSplitInfo(Data trainingSet, int beginExampleIndex,
            int endExampleIndex, Attribute attribute) {
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

    public int testCondition(Object value) {
        Double v = (Double) value;
        Double splitValue = (Double) mapSplit.get(0).getSplitValue();
        if (v <= splitValue) return 0;
        else return 1;
    }

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