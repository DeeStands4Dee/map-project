import data.Data;
import tree.RegressionTree;
import exceptions.TrainingDataException;
import exceptions.UnknownValueException;
import utility.keyboard;

public class MainTest {

    public static void main(String[] args) {
        System.out.println("Training set:");
        String fileName = keyboard.readString();
        
        try {
            System.out.println("Starting data acquisition phase!");
            Data trainingSet = new Data(fileName);
            
            System.out.println("Starting learning phase!");
            RegressionTree tree = new RegressionTree(trainingSet);
            
            tree.printRules();
            System.out.println(tree.printTree());
            
            String answer = "y";
            while (answer.equals("y")) {
                System.out.println("Starting prediction phase!");
                try {
                    tree.predictClass();
                } catch (UnknownValueException e) {
                    System.out.println(e.getMessage());
                }
                System.out.println("Would you repeat? (y/n)");
                answer = keyboard.readString();
            }
            
        } catch (TrainingDataException e) {
            System.out.println(e.toString());
        }
    }
}