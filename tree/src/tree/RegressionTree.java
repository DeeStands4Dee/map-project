package tree;

import java.io.*;
import java.util.TreeSet;

import utility.keyboard;
import exceptions.UnknownValueException;
import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;

public class RegressionTree implements Serializable {

    private Node root;
    private RegressionTree childTree[];

    public RegressionTree() {
    }

    public RegressionTree(Data trainingSet) {
        learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1,
                trainingSet.getNumberOfExamples() / 10);
    }

    private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
        return (end - begin + 1) <= numberOfExamplesPerLeaf;
    }

    private SplitNode determineBestSplitNode(Data trainingSet, int begin, int end) {
        TreeSet<SplitNode> ts = new TreeSet<SplitNode>();
        SplitNode currentNode = null;

        for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
            Attribute a = trainingSet.getExplanatoryAttribute(i);
            if (a instanceof DiscreteAttribute) {
                DiscreteAttribute attribute = (DiscreteAttribute) trainingSet.getExplanatoryAttribute(i);
                trainingSet.sort(attribute, begin, end);
                currentNode = new DiscreteNode(trainingSet, begin, end, attribute);
            } else {
                ContinuousAttribute attribute = (ContinuousAttribute) trainingSet.getExplanatoryAttribute(i);
                trainingSet.sort(attribute, begin, end);
                currentNode = new ContinuousNode(trainingSet, begin, end, attribute);
            }
            if (currentNode.getNumberOfChildren() > 0) {
                ts.add(currentNode);
            }
        }

        SplitNode bestNode = ts.first();
        trainingSet.sort(bestNode.getAttribute(), begin, end);
        return bestNode;
    }

    private void learnTree(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
        if (isLeaf(trainingSet, begin, end, numberOfExamplesPerLeaf)) {
            root = new LeafNode(trainingSet, begin, end);
        } else {
            SplitNode bestNode = determineBestSplitNode(trainingSet, begin, end);
            if (bestNode.getNumberOfChildren() == 0) {
                root = new LeafNode(trainingSet, begin, end);
            } else if (bestNode.getNumberOfChildren() == 1 &&
                    bestNode.getSplitInfo(0).getBeginIndex() == begin &&
                    bestNode.getSplitInfo(0).getEndIndex() == end) {
                root = new LeafNode(trainingSet, begin, end);
            } else {
                root = bestNode;
                childTree = new RegressionTree[bestNode.getNumberOfChildren()];
                for (int i = 0; i < bestNode.getNumberOfChildren(); i++) {
                    childTree[i] = new RegressionTree();
                    childTree[i].learnTree(trainingSet,
                            bestNode.getSplitInfo(i).getBeginIndex(),
                            bestNode.getSplitInfo(i).getEndIndex(),
                            numberOfExamplesPerLeaf);
                }
            }
        }
    }

    public String toString() {
        String tree = root.toString() + "\n";
        if (root instanceof SplitNode) {
            for (int i = 0; i < childTree.length; i++) {
                tree += childTree[i].toString();
            }
        }
        return tree;
    }

    public String printTree() {
        return "\n********* TREE **********\n\n" + toString() + "\n*************************\n";
    }

    public void printRules() {
        System.out.println("\n********* RULES **********\n");
        printRules("");
        System.out.println("\n*************************\n");
    }

    private void printRules(String current) {
        if (root instanceof LeafNode) {
            System.out.println(current + " ==> Class=" + ((LeafNode) root).getPredictedClassValue());
        } else {
            SplitNode splitNode = (SplitNode) root;
            for (int i = 0; i < childTree.length; i++) {
                String info;
                if (root instanceof DiscreteNode) {
                    info = splitNode.getAttribute().getName() + "=" + splitNode.getSplitInfo(i).getSplitValue();
                } else {
                    info = splitNode.getAttribute().getName() + splitNode.getSplitInfo(i).getComparator()
                            + splitNode.getSplitInfo(i).getSplitValue();
                }
                String condition = current.isEmpty() ? info : current + " AND " + info;
                childTree[i].printRules(condition);
            }
        }
    }

    public Double predictClass() throws UnknownValueException {
        if (root instanceof LeafNode) {
            System.out.println(((LeafNode) root).getPredictedClassValue());
            return ((LeafNode) root).getPredictedClassValue();
        } else {
            SplitNode splitNode = (SplitNode) root;
            for (int i = 0; i < splitNode.getNumberOfChildren(); i++) {
                System.out.println(i + ":" + splitNode.getAttribute().getName() +
                        splitNode.getSplitInfo(i).getComparator() +
                        splitNode.getSplitInfo(i).getSplitValue());
            }
            int risp = keyboard.readInt();
            if (risp == -1 || risp >= root.getNumberOfChildren()) {
                throw new UnknownValueException("The answer should be an integer between 0 and "
                        + (root.getNumberOfChildren() - 1) + "!");
            } else {
                return childTree[risp].predictClass();
            }
        }
    }

    public void salva(String nomeFile) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
        out.writeObject(this);
        out.close();
    }

    public static RegressionTree carica(String nomeFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
        RegressionTree tree = (RegressionTree) in.readObject();
        in.close();
        return tree;
    }
}