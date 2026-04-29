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

    abstract void setSplitInfo(Data trainingSet, int beginExampleIndex, 
                               int endExampleIndex, Attribute attribute);

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
        String query = "";
        for (int i = 0; i < mapSplit.length; i++) {
            query += attribute.getName() + mapSplit[i].comparator + mapSplit[i].splitValue;
            if (i < mapSplit.length - 1) {
                query += " OR ";
            }
        }
        return query;
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
