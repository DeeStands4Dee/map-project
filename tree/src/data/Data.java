package data;

import java.io.*;
import java.util.*;
import exceptions.TrainingDataException;

public class Data {

    private Object data[][];
    private int numberOfExamples;
    private List<Attribute> explanatorySet = new LinkedList<Attribute>();
    private ContinuousAttribute classAttribute;

    public Data(String fileName) throws TrainingDataException {
        try {
            Scanner scanner = new Scanner(new File(fileName));

            String schemaLine = scanner.nextLine().trim();
            int numberOfAttributes = Integer.parseInt(schemaLine.split("\\s+")[1]);

            int index = 0;

            for (int i = 0; i < numberOfAttributes; i++) {
                String line = scanner.nextLine().trim();
                String[] parts = line.split("\\s+");
                String attrName = parts[1];
                Set<String> values = new TreeSet<>(Arrays.asList(parts[2].split(",")));
                explanatorySet.add(new DiscreteAttribute(attrName, index, values));
                index++;
            }

            String targetLine = scanner.nextLine().trim();
            String targetName = targetLine.split("\\s+")[1];
            classAttribute = new ContinuousAttribute(targetName, index);

            String dataLine = scanner.nextLine().trim();
            numberOfExamples = Integer.parseInt(dataLine.split("\\s+")[1]);

            if (numberOfExamples == 0) {
                throw new TrainingDataException("Training set vuoto!");
            }

            data = new Object[numberOfExamples][explanatorySet.size() + 1];
            for (int i = 0; i < numberOfExamples; i++) {
                String line = scanner.nextLine().trim();
                String[] values = line.split(",");
                for (int j = 0; j < explanatorySet.size(); j++) {
                    data[i][j] = values[j].trim();
                }
                data[i][explanatorySet.size()] = Double.parseDouble(values[explanatorySet.size()].trim());
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
        return explanatorySet.size();
    }

    public Double getClassValue(int exampleIndex) {
        return (Double) data[exampleIndex][classAttribute.getIndex()];
    }

    public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
        return data[exampleIndex][attributeIndex];
    }

    public Attribute getExplanatoryAttribute(int index) {
        return explanatorySet.get(index);
    }

    public ContinuousAttribute getClassAttribute() {
        return classAttribute;
    }

    public String toString() {
        String s = "";
        for (int i = 0; i < numberOfExamples; i++) {
            for (int j = 0; j < explanatorySet.size(); j++) {
                s += data[i][j] + " ";
            }
            s += data[i][classAttribute.getIndex()] + "\n";
        }
        return s;
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