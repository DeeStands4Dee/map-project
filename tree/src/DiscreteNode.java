public class DiscreteNode extends SplitNode {

    public DiscreteNode(Data trainingSet, int beginExampleIndex, 
                        int endExampleIndex, DiscreteAttribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    }

    int testCondition(Object value) {
        for (int i = 0; i < mapSplit.length; i++) {
            if (value.equals(mapSplit[i].getSplitValue())) {
                return i;
            }
        }
        return -1;
    }

    public String toString() {
        return super.toString();
    }
}