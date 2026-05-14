public class RegressionTree {

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
        SplitNode bestNode = null;
        double bestVariance = Double.MAX_VALUE;

        for (int i = 0; i < trainingSet.getNumberOfExplanatoryAttributes(); i++) {
            Attribute attribute = trainingSet.getExplanatoryAttribute(i);
            trainingSet.sort(attribute, begin, end);
            DiscreteNode node = new DiscreteNode(trainingSet, begin, end, (DiscreteAttribute) attribute);

            if (node.getVariance() < bestVariance) {
                bestVariance = node.getVariance();
                bestNode = node;
            }
        }

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
                String condition = current.isEmpty()
                        ? splitNode.formulateQuery() + splitNode.getSplitInfo(i).getSplitValue()
                        : current + " AND " + splitNode.formulateQuery() + splitNode.getSplitInfo(i).getSplitValue();
                childTree[i].printRules(condition);
            }
        }
    }

    public static void main(String[] args) {
        Data trainingSet = new Data();
        RegressionTree tree = new RegressionTree(trainingSet);
        tree.printRules();
        System.out.println(tree.printTree());
    }
}