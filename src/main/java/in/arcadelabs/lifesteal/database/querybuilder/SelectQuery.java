package in.arcadelabs.lifesteal.database.querybuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a table
 * @author Mrtenz
 * <a href="https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries">...</a>
 **/

public class SelectQuery {

    private final String table;
    private final List<String> columns = new ArrayList<>();
    private final List<String> wheres = new ArrayList<>();
    private String orderBy;
    private boolean orderByAscending = false;
    private int limitOffset = 0;
    private int limitRowCount = 0;

    /**
     * Create a select query.
     *
     * @param table the table to be selected from
     */
    public SelectQuery(String table) {
        this.table = table;
    }

    /**
     * Add a column to be selected.
     *
     * @param column the column
     * @return the SelectQuery object
     */
    public SelectQuery column(String column) {
        columns.add(column);
        return this;
    }

    /**
     * Add a where clause.
     *
     * @param expression the expression
     * @return the SelectQuery object
     */
    public SelectQuery where(String expression) {
        wheres.add(expression);
        return this;
    }

    /**
     * Add a where clause.
     *
     * @param expression the expression
     * @return the SelectQuery object
     */
    public SelectQuery and(String expression) {
        where(expression);
        return this;
    }

    /**
     * Add a order by clause.
     *
     * @param column    the column to be ordered by
     * @param ascending whether it should be ordered ascending or descending
     * @return the SelectQuery object
     */
    public SelectQuery orderBy(String column, boolean ascending) {
        this.orderBy = column;
        this.orderByAscending = ascending;
        return this;
    }

    /**
     * Add a limit clause.
     *
     * @param offset   the offset, starting from 0.
     * @param rowCount the number of rows
     * @return the SelectQuery object
     */
    public SelectQuery limit(int offset, int rowCount) {
        this.limitOffset = offset;
        this.limitRowCount = rowCount;
        return this;
    }

    /**
     * Add a limit clause, with a offset of 0.
     *
     * @param rowCount the number of rows
     * @return the SelectQuery object
     */
    public SelectQuery limit(int rowCount) {
        this.limitOffset = 0;
        this.limitRowCount = rowCount;
        return this;
    }

    /**
     * Build the query as a String.
     *
     * @return the query as a String
     */
    public String build() {
        StringBuilder builder = new StringBuilder();
        builder.append("SELECT ")
                .append(QueryUtils.separate(columns, ","))
                .append(" FROM ")
                .append(table);

        if (wheres.size() > 0) {
            builder.append(" WHERE ")
                    .append(QueryUtils.separate(wheres, " AND "));
        }

        if (orderBy != null) {
            builder.append(" ORDER BY ")
                    .append(orderBy)
                    .append(orderByAscending ? " ASC" : " DESC");
        }

        if (limitRowCount > 0) {
            builder.append(" LIMIT ")
                    .append(limitOffset)
                    .append(",").append(limitRowCount);
        }

        return builder.toString();
    }

}
