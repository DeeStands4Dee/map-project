package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Classe che modella lo schema di una tabella nel database relazionale.
 * Contiene la lista delle colonne con il loro nome e tipo.
 */
public class TableSchema implements Iterable<Column> {

    /** Lista delle colonne che compongono lo schema della tabella. */
    private List<Column> tableSchema = new ArrayList<Column>();

    /**
     * Costruttore che ricava lo schema della tabella dal database.
     * @param db oggetto DbAccess per accedere al database.
     * @param tableName nome della tabella di cui ricavare lo schema.
     * @throws SQLException se si verifica un errore nell'accesso al database.
     */
    public TableSchema(DbAccess db, String tableName) throws SQLException {
        HashMap<String, String> mapSQL_JAVATypes = new HashMap<String, String>();

        mapSQL_JAVATypes.put("CHAR", "string");
        mapSQL_JAVATypes.put("VARCHAR", "string");
        mapSQL_JAVATypes.put("LONGVARCHAR", "string");
        mapSQL_JAVATypes.put("BIT", "string");
        mapSQL_JAVATypes.put("SHORT", "number");
        mapSQL_JAVATypes.put("INT", "number");
        mapSQL_JAVATypes.put("LONG", "number");
        mapSQL_JAVATypes.put("FLOAT", "number");
        mapSQL_JAVATypes.put("DOUBLE", "number");

        Connection con = db.getConnection();
        DatabaseMetaData meta = con.getMetaData();
        ResultSet res = meta.getColumns(null, null, tableName, null);

        while (res.next()) {
            if (mapSQL_JAVATypes.containsKey(res.getString("TYPE_NAME")))
                tableSchema.add(new Column(
                        res.getString("COLUMN_NAME"),
                        mapSQL_JAVATypes.get(res.getString("TYPE_NAME"))));
        }
        res.close();
    }

    /**
     * Restituisce il numero di attributi (colonne) della tabella.
     * @return numero di colonne.
     */
    public int getNumberOfAttributes() {
        return tableSchema.size();
    }

    /**
     * Restituisce la colonna in posizione index.
     * @param index indice della colonna.
     * @return oggetto Column in posizione index.
     */
    public Column getColumn(int index) {
        return tableSchema.get(index);
    }

    /**
     * Restituisce un iteratore sulle colonne dello schema.
     * @return iteratore sugli oggetti Column.
     */
    @Override
    public Iterator<Column> iterator() {
        return tableSchema.iterator();
    }
}