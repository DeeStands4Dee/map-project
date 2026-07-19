import java.io.IOException;

import data.Data;
import exceptions.TrainingDataException;
import exceptions.UnknownValueException;
import tree.RegressionTree;
import utility.keyboard;

public class MainTest {

    public static void main(String[] args) {

        int decision = 0;
        do {
            System.out.println("Learn Regression Tree from data [1]");
            System.out.println("Load Regression Tree from archive [2]");
            decision = keyboard.readInt();
        } while (!(decision == 1) && !(decision == 2));

        String tableName = "";
        System.out.println("Table/File name:");
        tableName = keyboard.readString();

        RegressionTree tree = null;
        if (decision == 1) {
            System.out.println("Starting data acquisition phase!");
            Data trainingSet = null;
            try {
                trainingSet = new Data(tableName);
            } catch (TrainingDataException e) {
                System.out.println(e);
                return;
            }

            System.out.println("Starting learning phase!");
            tree = new RegressionTree(trainingSet);
            try {
                tree.salva(tableName + ".dmp");
            } catch (IOException e) {
                System.out.println(e.toString());
            }
        } else {
            try {
                tree = RegressionTree.carica(tableName + ".dmp");
            } catch (ClassNotFoundException | IOException e) {
                System.out.print(e);
                return;
            }
        }
        tree.printRules();
        System.out.println(tree.printTree());

        char risp = 'y';
        do {
            System.out.println("Starting prediction phase!");
            try {
                System.out.println(tree.predictClass());
            } catch (UnknownValueException e) {
                System.out.println(e);
            }
            System.out.println("Would you repeat ? (y/n)");
            risp = keyboard.readChar();
        } while (Character.toUpperCase(risp) == 'Y');
    }
}