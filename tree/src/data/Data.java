package data;

import java.io.*;
import java.util.*;
import exceptions.TrainingDataException;

public class Data {

    private Object data[][];
    private int numberOfExamples;
    private Attribute explanatorySet[];
    private ContinuousAttribute classAttribute;

    public Data(String fileName) throws TrainingDataException {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            // Legge @schema -> numero attributi
            String schemaLine = scanner.nextLine().trim();
            int numberOfAttributes = Integer.parseInt(schemaLine.split("\\s+")[1]);

            // Legge le righe @desc e @target
            List<DiscreteAttribute> discreteList = new ArrayList<>();
            int index = 0;
            String targetName = null;

            for (int i = 0; i < numberOfAttributes; i++) {
                String line = scanner.nextLine().trim();
                // es: @desc motor A,B,C,D,E
                String[] parts = line.split("\\s+");
                String attrName = parts[1];
                String[] values = parts[2].split(",");
                discreteList.add(new DiscreteAttribute(attrName, index, values));
                index++;
            }

            // Legge @target
            String targetLine = scanner.nextLine().trim();
            targetName = targetLine.split("\\s+")[1];
            classAttribute = new ContinuousAttribute(targetName, index);

            // Costruisce explanatorySet
            explanatorySet = new Attribute[discreteList.size()];
            for (int i = 0; i < discreteList.size(); i++) {
                explanatorySet[i] = discreteList.get(i);
            }

            // Legge @data -> numero esempi
            String dataLine = scanner.nextLine().trim();
            numberOfExamples = Integer.parseInt(dataLine.split("\\s+")[1]);

            if (numberOfExamples == 0) {
                throw new TrainingDataException("Training set vuoto!");
            }

            // Legge i dati
            data = new Object[numberOfExamples][explanatorySet.length + 1];
            for (int i = 0; i < numberOfExamples; i++) {
                String line = scanner.nextLine().trim();
                String[] values = line.split(",");
                for (int j = 0; j < explanatorySet.length; j++) {
                    data[i][j] = values[j].trim();
                }
                data[i][explanatorySet.length] = Double.parseDouble(values[explanatorySet.length].trim());
            }

            scanner.close();

        } catch (FileNotFoundException e) {
            throw new TrainingDataException(e.toString());
        }
    }

    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfExplanatoryAttributes() {
        return explanatorySet.length;
    }

    public Double getClassValue(int exampleIndex) {
        return (Double) data[exampleIndex][classAttribute.getIndex()];
    }

    public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
        return data[exampleIndex][attributeIndex];
    }

    public Attribute getExplanatoryAttribute(int index) {
        return explanatorySet[index];
    }

    public ContinuousAttribute getClassAttribute() {
        return classAttribute;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < numberOfExamples; i++) {
            for (int j = 0; j < explanatorySet.length; j++) {
                s += data[i][j] + " ";
            }
            s += data[i][classAttribute.getIndex()] + "\n";
        }
        return s;
    }

    private void swap(int i, int j) {
        Object[] temp = (Object[]) data[i];
        data[i] = data[j];
        data[j] = temp;
    }

    public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {
        int attrIndex = attribute.getIndex();
        for (int i = beginExampleIndex + 1; i <= endExampleIndex; i++) {
            Object[] key = (Object[]) data[i];
            String keyVal = (String) key[attrIndex];
            int j = i - 1;
            while (j >= beginExampleIndex &&
                    ((String) data[j][attrIndex]).compareTo(keyVal) > 0) {
                data[j + 1] = data[j];
                j--;
            }
            data[j + 1] = key;
        }
    }
}