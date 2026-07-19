package tree;
import data.Data;
import java.io.Serializable;

public class LeafNode extends Node {

    private Double predictedClassValue;

    LeafNode(Data trainingSet, int beginExampleIndex, int endExampleIndex) {
        super(trainingSet, beginExampleIndex, endExampleIndex);
        
        double sum = 0;
        int count = endExampleIndex - beginExampleIndex + 1;
        
        for (int i = beginExampleIndex; i <= endExampleIndex; i++) {
            sum += trainingSet.getClassValue(i);
        }
        
        this.predictedClassValue = sum / count;
    }

    public Double getPredictedClassValue() {
        return predictedClassValue;
    }

    public int getNumberOfChildren() {
        return 0;
    }

    public String toString() {
        return "LEAF : class=" + predictedClassValue + " " + super.toString();
    }
}
