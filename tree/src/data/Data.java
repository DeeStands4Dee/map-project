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
                if (parts.length == 3) {
                    // attributo discreto — ha i valori elencati
                    Set<String> values = new TreeSet<>(Arrays.asList(parts[2].split(",")));
                    explanatorySet.add(new DiscreteAttribute(attrName, index, values));
                } else {
                    // attributo continuo — nessun valore elencato
                    explanatorySet.add(new ContinuousAttribute(attrName, index));
                }
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
                    if (explanatorySet.get(j) instanceof DiscreteAttribute) {
                        data[i][j] = values[j].trim();
                    } else {
                        data[i][j] = Double.parseDouble(values[j].trim());
                    }
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
        quicksort(attribute, beginExampleIndex, endExampleIndex);
    }

    private void swap(int i, int j) {
        Object temp;
        for (int k = 0; k < getNumberOfExplanatoryAttributes() + 1; k++) {
            temp = data[i][k];
            data[i][k] = data[j][k];
            data[j][k] = temp;
        }
    }

    private int partition(DiscreteAttribute attribute, int inf, int sup) {
        int i, j;
        i = inf;
        j = sup;
        int med = (inf + sup) / 2;
        String x = (String) getExplanatoryValue(med, attribute.getIndex());
        swap(inf, med);

        while (true) {
            while (i <= sup && ((String) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
                i++;
            }
            while (((String) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
                j--;
            }
            if (i < j) {
                swap(i, j);
            } else break;
        }
        swap(inf, j);
        return j;
    }

    private int partition(ContinuousAttribute attribute, int inf, int sup) {
        int i, j;
        i = inf;
        j = sup;
        int med = (inf + sup) / 2;
        Double x = (Double) getExplanatoryValue(med, attribute.getIndex());
        swap(inf, med);

        while (true) {
            while (i <= sup && ((Double) getExplanatoryValue(i, attribute.getIndex())).compareTo(x) <= 0) {
                i++;
            }
            while (((Double) getExplanatoryValue(j, attribute.getIndex())).compareTo(x) > 0) {
                j--;
            }
            if (i < j) {
                swap(i, j);
            } else break;
        }
        swap(inf, j);
        return j;
    }

    private void quicksort(Attribute attribute, int inf, int sup) {
        if (sup >= inf) {
            int pos;
            if (attribute instanceof DiscreteAttribute)
                pos = partition((DiscreteAttribute) attribute, inf, sup);
            else
                pos = partition((ContinuousAttribute) attribute, inf, sup);

            if ((pos - inf) < (sup - pos + 1)) {
                quicksort(attribute, inf, pos - 1);
                quicksort(attribute, pos + 1, sup);
            } else {
                quicksort(attribute, pos + 1, sup);
                quicksort(attribute, inf, pos - 1);
            }
        }
    }
}