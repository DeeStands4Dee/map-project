package tree;

import java.io.*;
import java.util.TreeSet;
import utility.keyboard;
import exceptions.UnknownValueException;
import data.Attribute;
import data.ContinuousAttribute;
import data.Data;
import data.DiscreteAttribute;

/**
 * Classe che modella un albero di regressione.
 * Implementa l'apprendimento, la stampa, la serializzazione
 * e la predizione dell'albero di regressione.
 */
public class RegressionTree implements Serializable {

    /** Nodo radice dell'albero. */
    private Node root;
    /** Array di sottoalberi figli. */
    private RegressionTree childTree[];

    /**
     * Costruttore di default.
     */
    public RegressionTree() {
    }

    /**
     * Costruttore che apprende l'albero dal dataset di addestramento.
     * @param trainingSet dataset di addestramento.
     */
    public RegressionTree(Data trainingSet) {
        learnTree(trainingSet, 0, trainingSet.getNumberOfExamples() - 1,
                trainingSet.getNumberOfExamples() / 10);
    }

    /**
     * Verifica se un nodo deve essere una foglia.
     * @param trainingSet dataset di addestramento.
     * @param begin indice iniziale.
     * @param end indice finale.
     * @param numberOfExamplesPerLeaf numero minimo di esempi per foglia.
     * @return true se il nodo deve essere una foglia.
     */
    private boolean isLeaf(Data trainingSet, int begin, int end, int numberOfExamplesPerLeaf) {
        return (end - begin + 1) <= numberOfExamplesPerLeaf;
    }

    /**
     * Determina il miglior nodo di split per un sottoinsieme del dataset.
     * @param trainingSet dataset di addestramento.
     * @param begin indice iniziale.
     * @param end indice finale.
     * @return miglior nodo di split.
     */
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

    /**
     * Apprende ricorsivamente l'albero di regressione.
     * @param trainingSet dataset di addestramento.
     * @param begin indice iniziale.
     * @param end indice finale.
     * @param numberOfExamplesPerLeaf numero minimo di esempi per foglia.
     */
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

    /**
     * Restituisce una rappresentazione testuale dell'albero.
     * @return stringa con la struttura dell'albero.
     */
    public String toString() {
        String tree = root.toString() + "\n";
        if (root instanceof SplitNode) {
            for (int i = 0; i < childTree.length; i++) {
                tree += childTree[i].toString();
            }
        }
        return tree;
    }

    /**
     * Restituisce la struttura dell'albero in formato leggibile.
     * @return stringa con la struttura dell'albero.
     */
    public String printTree() {
        return "\n********* TREE **********\n\n" + toString() + "\n*************************\n";
    }

    /**
     * Stampa le regole dell'albero su console.
     */
    public void printRules() {
        System.out.println("\n********* RULES **********\n");
        printRules("");
        System.out.println("\n*************************\n");
    }

    /**
     * Stampa ricorsivamente le regole dell'albero.
     * @param current regola corrente costruita fino a questo nodo.
     */
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
    
    /**
     * Predice la classe per un nuovo esempio tramite input da tastiera.
     * @return valore della classe predetta.
     * @throws UnknownValueException se la risposta non è valida.
     */
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

    /**
     * Serializza l'albero su file.
     * @param nomeFile nome del file su cui salvare l'albero.
     * @throws FileNotFoundException se il file non viene trovato.
     * @throws IOException se si verifica un errore di I/O.
     */
    public void salva(String nomeFile) throws FileNotFoundException, IOException {
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(nomeFile));
        out.writeObject(this);
        out.close();
    }

    /**
     * Carica un albero da file.
     * @param nomeFile nome del file da cui caricare l'albero.
     * @return albero di regressione caricato.
     * @throws FileNotFoundException se il file non viene trovato.
     * @throws IOException se si verifica un errore di I/O.
     * @throws ClassNotFoundException se la classe non viene trovata.
     */
    public static RegressionTree carica(String nomeFile)
            throws FileNotFoundException, IOException, ClassNotFoundException {
        ObjectInputStream in = new ObjectInputStream(new FileInputStream(nomeFile));
        RegressionTree tree = (RegressionTree) in.readObject();
        in.close();
        return tree;
    }

    /**
     * Predice la classe per un nuovo esempio tramite socket.
     * @param in stream di input dal client.
     * @param out stream di output verso il client.
     * @return valore della classe predetta.
     * @throws IOException se si verifica un errore nella comunicazione.
     * @throws ClassNotFoundException se la classe dell'oggetto ricevuto non è trovata.
     */
    public Double predictClass(ObjectInputStream in, ObjectOutputStream out)
            throws IOException, ClassNotFoundException {
        if (root instanceof LeafNode) {
            return ((LeafNode) root).getPredictedClassValue();
        } else {
            SplitNode splitNode = (SplitNode) root;
            out.writeObject("QUERY");
            String query = "";
            for (int i = 0; i < splitNode.getNumberOfChildren(); i++) {
                query += i + ":" + splitNode.getAttribute().getName() +
                        splitNode.getSplitInfo(i).getComparator() +
                        splitNode.getSplitInfo(i).getSplitValue() + "\n";
            }
            out.writeObject(query);
            int risp = (Integer) in.readObject();
            if (risp < 0 || risp >= root.getNumberOfChildren()) {
                out.writeObject("ERR Risposta non valida! Scegli tra 0 e " + (root.getNumberOfChildren() - 1));
                return null;
            }
            return childTree[risp].predictClass(in, out);
        }
    }
}