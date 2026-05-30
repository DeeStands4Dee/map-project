package tree;

import data.Data;
import data.DiscreteAttribute;
import java.io.Serializable;

public class DiscreteNode extends SplitNode implements Serializable {

    public DiscreteNode(Data trainingSet, int beginExampleIndex, 
                        int endExampleIndex, DiscreteAttribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    }

    int testCondition(Object value) {
        for (int i = 0; i < mapSplit.size(); i++) {
    if (value.equals(mapSplit.get(i).getSplitValue())) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        return super.toString();
    }
}