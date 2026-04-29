public class DiscreteNode extends SplitNode {

    public DiscreteNode(Data trainingSet, int beginExampleIndex, 
                        int endExampleIndex, DiscreteAttribute attribute) {
        super(trainingSet, beginExampleIndex, endExampleIndex, attribute);
    }
void setSplitInfo(Data trainingSet, int beginExampleIndex, 
                      int endExampleIndex, Attribute attribute) {
        
        DiscreteAttribute discAttribute = (DiscreteAttribute) attribute;
        
        // 1. Troviamo il primo valore distinto nella partizione
        String currentValue = (String) trainingSet.getExplanatoryValue(beginExampleIndex, 
                                                                        discAttribute.getIndex());
        int beginPartition = beginExampleIndex;
        int numSplit = 0;
        
        // 2. Contiamo quanti valori distinti ci sono
        for (int i = beginExampleIndex; i <= endExampleIndex; i++) {
            String val = (String) trainingSet.getExplanatoryValue(i, discAttribute.getIndex());
            if (!val.equals(currentValue)) {
                numSplit++;
                currentValue = val;
            }
        }
        numSplit++; // contiamo anche l'ultimo gruppo
        
        // 3. Creiamo l'array mapSplit della dimensione giusta
        mapSplit = new SplitInfo[numSplit];
        
        // 4. Popoliamo mapSplit
        currentValue = (String) trainingSet.getExplanatoryValue(beginExampleIndex, 
                                                                 discAttribute.getIndex());
        beginPartition = beginExampleIndex;
        int child = 0;
        
        for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
            String val = (String) trainingSet.getExplanatoryValue(i, discAttribute.getIndex());
            if (!val.equals(currentValue)) {
                mapSplit[child] = new SplitInfo(currentValue, beginPartition, i - 1, child);
                child++;
                currentValue = val;
                beginPartition = i;
            }
        }
        // ultimo gruppo
        mapSplit[child] = new SplitInfo(currentValue, beginPartition, endExampleIndex, child);
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