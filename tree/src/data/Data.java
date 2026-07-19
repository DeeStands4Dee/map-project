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

/**
 * Classe che modella il dataset di addestramento caricato dal database.
 * Gestisce gli attributi esplicativi e l'attributo target, e fornisce
 * metodi per l'accesso ai dati e l'ordinamento tramite quicksort.
 */
public class Data {

    /** Lista delle transazioni del dataset. */
    private List<Example> data = new ArrayList<Example>();
    /** Numero di esempi nel dataset. */
    private int numberOfExamples;
    /** Lista degli attributi esplicativi. */
    private List<Attribute> explanatorySet = new LinkedList<Attribute>();
    /** Attributo target (classe da predire). */
    private ContinuousAttribute classAttribute;

    /**
     * Costruttore che carica i dati dal database.
     * Legge lo schema della tabella, costruisce gli attributi e carica le transazioni.
     * @param tableName nome della tabella nel database.
     * @throws TrainingDataException se la connessione fallisce, la tabella non esiste,
     *         ha meno di due colonne, è vuota o l'ultima colonna non è numerica.
     */
    public Data(String tableName) throws TrainingDataException {
        DbAccess db = new DbAccess();
        try {
            db.initConnection();
            TableSchema tSchema = new TableSchema(db, tableName);
            if (tSchema.getNumberOfAttributes() < 2)
                throw new TrainingDataException("La tabella ha meno di due colonne!");
            Column classColumn = tSchema.getColumn(tSchema.getNumberOfAttributes() - 1);
            if (!classColumn.isNumber())
                throw new TrainingDataException("L'ultima colonna non è numerica!");
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
            classAttribute = new ContinuousAttribute(
                classColumn.getColumnName(),
                tSchema.getNumberOfAttributes() - 1
            );
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

    /**
     * Restituisce il numero di esempi nel dataset.
     * @return numero di esempi.
     */
    public int getNumberOfExamples() {
        return numberOfExamples;
    }

    /**
     * Restituisce il numero di attributi esplicativi.
     * @return numero di attributi esplicativi.
     */
    public int getNumberOfExplanatoryAttributes() {
        return explanatorySet.size();
    }

    /**
     * Restituisce il valore della classe per un esempio.
     * @param exampleIndex indice dell'esempio.
     * @return valore della classe come Double.
     */
    public Double getClassValue(int exampleIndex) {
        return (Double) data.get(exampleIndex).get(classAttribute.getIndex());
    }

    /**
     * Restituisce il valore di un attributo esplicativo per un esempio.
     * @param exampleIndex indice dell'esempio.
     * @param attributeIndex indice dell'attributo.
     * @return valore dell'attributo.
     */
    public Object getExplanatoryValue(int exampleIndex, int attributeIndex) {
        return data.get(exampleIndex).get(attributeIndex);
    }

    /**
     * Restituisce l'attributo esplicativo in posizione index.
     * @param index indice dell'attributo.
     * @return oggetto Attribute in posizione index.
     */
    public Attribute getExplanatoryAttribute(int index) {
        return explanatorySet.get(index);
    }

    /**
     * Restituisce l'attributo target del dataset.
     * @return attributo target come ContinuousAttribute.
     */
    public ContinuousAttribute getClassAttribute() {
        return classAttribute;
    }

    /**
     * Restituisce una rappresentazione testuale del dataset.
     * @return stringa con tutti gli esempi del dataset.
     */
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

    /**
     * Ordina il dataset rispetto a un attributo usando il quicksort.
     * @param attribute attributo rispetto al quale ordinare.
     * @param beginExampleIndex indice iniziale del sottoinsieme da ordinare.
     * @param endExampleIndex indice finale del sottoinsieme da ordinare.
     */
    public void sort(Attribute attribute, int beginExampleIndex, int endExampleIndex) {
        quicksort(attribute, beginExampleIndex, endExampleIndex);
    }

    /**
     * Scambia due esempi nel dataset.
     * @param i indice del primo esempio.
     * @param j indice del secondo esempio.
     */
    private void swap(int i, int j) {
        Example temp = data.get(i);
        data.set(i, data.get(j));
        data.set(j, temp);
    }

    /**
     * Partiziona il dataset rispetto a un attributo discreto.
     * @param attribute attributo discreto rispetto al quale partizionare.
     * @param inf indice inferiore del sottoinsieme.
     * @param sup indice superiore del sottoinsieme.
     * @return indice del punto di separazione.
     */
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

    /**
     * Partiziona il dataset rispetto a un attributo continuo.
     * @param attribute attributo continuo rispetto al quale partizionare.
     * @param inf indice inferiore del sottoinsieme.
     * @param sup indice superiore del sottoinsieme.
     * @return indice del punto di separazione.
     */
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

    /**
     * Algoritmo quicksort per l'ordinamento del dataset.
     * @param attribute attributo rispetto al quale ordinare.
     * @param inf indice inferiore del sottoinsieme.
     * @param sup indice superiore del sottoinsieme.
     */
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