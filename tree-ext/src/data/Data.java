package data;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import database.DbAccess;
import database.DatabaseConnectionException;
import database.EmptySetException;
import database.Example;
import database.TableData;
import database.TableSchema;
import database.Column;
import exceptions.TrainingDataException;

public class Data {

    private List<Example> data = new ArrayList<Example>();
    private int numberOfExamples;
    private List<Attribute> explanatorySet = new LinkedList<Attribute>();
    private ContinuousAttribute classAttribute;

    public Data(String tableName) throws TrainingDataException {
        DbAccess db = new DbAccess();
        try {
            db.initConnection();

            TableSchema tSchema = new TableSchema(db, tableName);

            if (tSchema.getNumberOfAttributes() < 2)
                throw new TrainingDataException("La tabella ha meno di due colonne!");

            // Ultima colonna = attributo target (deve essere numerico)
            Column classColumn = tSchema.getColumn(tSchema.getNumberOfAttributes() - 1);
            if (!classColumn.isNumber())
                throw new TrainingDataException("L'ultima colonna non è numerica!");

            // Costruisce explanatorySet (tutte le colonne tranne l'ultima)
            for (int i = 0; i < tSchema.getNumberOfAttributes() - 1; i++) {
                Column col = tSchema.getColumn(i);
                if (col.isNumber()) {
                    explanatorySet.add(new ContinuousAttribute(col.getColumnName(), i));
                } else {
                    TableData td = new TableData(db);
                    Set<Object> distinctValues = td.getDistinctColumnValues(tableName, col);
                    Set<String> stringValues = new TreeSet<String>();
                    for (Object o : distinctValues)
                        stringValues.add(o.toString());
                    explanatorySet.add(new DiscreteAttribute(col.getColumnName(), i, stringValues));
                }
            }

            // Attributo classe
            classAttribute = new ContinuousAttribute(
                classColumn.getColumnName(),
                tSchema.getNumberOfAttributes() - 1
            );

            // Carica le transazioni
            TableData tableData = new TableData(db);
            List<Example> transactions = tableData.getTransazioni(tableName);
            numberOfExamples = transactions.size();
            data = transactions;

        } catch (DatabaseConnectionException e) {
            throw new TrainingDataException("Connessione al database fallita: " + e.getMessage());
        } catch (SQLException e) {
            throw new TrainingDataException("Errore SQL: " + e.getMessage());
        } catch (EmptySetException e) {
            throw new TrainingDataException("La tabella è vuota!");
        } finally {
            db.closeConnection();
        }
    }

    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    public int getNumberOfExplanatoryAttributes() {
        return explanatorySet.size();
    }

    public Double getClassValue(int exampleIndex) {
        return (Double) data.get(exampleIndex).get(classAttribute.getIndex());
    }

    public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
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
                s += data.get(i).get(j) + " ";
            }
            s += data.get(i).get(classAttribute.getIndex()) + "\n";
        }
        return s;
    }

    public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {
        quicksort(attribute, beginExampleIndex, endExampleIndex);
    }

    private void swap(int i, int j) {
        Example temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
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