public abstract class Node {

    private static int idNodeCount = 0;
    private int idNode;
    private int beginExampleIndex;
    private int endExampleIndex;
    private double variance;

    Node(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
        this.idNode = idNodeCount++;
        this.beginExampleIndex = beginExampleIndex;
        this.endExampleIndex = endExampleIndex;
        this.variance = computeVariance(trainingSet, beginExampleIndex, endExampleIndex);
    }

    private double computeVariance(Data trainingSet, int begin, int end) {
        double sum = 0;
        double sumSquared = 0;
        int count = end - begin + 1;

        for (int i = begin; i <= end; i++) {
            double value = trainingSet.getClassValue(i);
            sum += value;
            sumSquared += value * value;
        }

        double mean = sum / count;
        double variance = 0;
        for (int i = begin; i <= end; i++) {
            double value = trainingSet.getClassValue(i);
            variance += (value - mean) * (value - mean);
        }
        return variance;
    }
    public int getIdNode() {
        return idNode;
    }

    public int getBeginExampleIndex() {
        return beginExampleIndex;
    }

    public int getEndExampleIndex() {
        return endExampleIndex;
    }

    public double getVariance() {
        return variance;
    }

    public abstract int getNumberOfChildren();

    public String toString() {
        return "Nodo: [Examples:" + beginExampleIndex + "-" + endExampleIndex + "] variance:" + variance;
    }
}