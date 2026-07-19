package database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.Set;

/**
 * Classe che modella l'insieme di transazioni collezionate in una tabella del database.
 * Fornisce metodi per estrarre le transazioni e i valori distinti di una colonna.
 */
public class TableData {

    /** Oggetto per l'accesso al database. */
    private DbAccess db;

    /**
     * Costruttore che inizializza l'accesso al database.
     * @param db oggetto DbAccess per accedere al database.
     */
    public TableData(DbAccess db) {
        this.db = db;
    }

    /**
     * Restituisce la lista di transazioni memorizzate nella tabella.
     * @param table nome della tabella da cui estrarre le transazioni.
     * @return lista di oggetti Example rappresentanti le transazioni.
     * @throws SQLException se si verifica un errore nell'esecuzione della query.
     * @throws EmptySetException se la tabella non contiene tuple.
     */
    public List<Example> getTransazioni(String table) throws SQLException, EmptySetException {
        LinkedList<Example> transSet = new LinkedList<Example>();
        Statement statement;
        TableSchema tSchema = new TableSchema(db, table);

        String query = "select ";
        for (int i = 0; i < tSchema.getNumberOfAttributes(); i++) {
            Column c = tSchema.getColumn(i);
            if (i > 0) query += ",";
            query += c.getColumnName();
        }
        if (tSchema.getNumberOfAttributes() == 0)
            throw new SQLException();
        query += (" FROM " + table);

        statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        boolean empty = true;
        while (rs.next()) {
            empty = false;
            Example currentTuple = new Example();
            for (int i = 0; i < tSchema.getNumberOfAttributes(); i++)
                if (tSchema.getColumn(i).isNumber())
                    currentTuple.add(rs.getDouble(i + 1));
                else
                    currentTuple.add(rs.getString(i + 1));
            transSet.add(currentTuple);
        }
        rs.close();
        statement.close();
        if (empty) throw new EmptySetException("Tabella vuota!");
        return transSet;
    }

    /**
     * Restituisce l'insieme dei valori distinti ordinati di una colonna.
     * @param table nome della tabella.
     * @param column colonna di cui estrarre i valori distinti.
     * @return insieme ordinato dei valori distinti della colonna.
     * @throws SQLException se si verifica un errore nell'esecuzione della query.
     */
    public Set<Object> getDistinctColumnValues(String table, Column column) throws SQLException {
        TreeSet<Object> valuesSet = new TreeSet<Object>();
        Statement statement;
        String query = "SELECT DISTINCT " + column.getColumnName() +
                " FROM " + table +
                " ORDER BY " + column.getColumnName() + " ASC";
        statement = db.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(query);
        while (rs.next()) {
            if (column.isNumber())
                valuesSet.add(rs.getDouble(1));
            else
                valuesSet.add(rs.getString(1));
        }
        rs.close();
        statement.close();
        return valuesSet;
    }
}