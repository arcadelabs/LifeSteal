/**
 *                  Copyright 2017 MrTenz
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *            http://www.apache.org/licenses/LICENSE-2.0
 **/

package in.arcadelabs.lifesteal.database.querybuilder;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a table
 *
 * @author Mrtenz
 * <a href="https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries">...</a>
 **/

public class UpdateQuery {

  private final String table;
  private final LinkedHashMap<String, String> values = new LinkedHashMap<>();
  private final List<String> wheres = new ArrayList<>();

  /**
   * Create an update query.
   *
   * @param table the table to be updated
   */
  public UpdateQuery(String table) {
    this.table = table;
  }

  /**
   * Set the value of a column.
   * <p>
   * MySQLAPI does not do any interpretation so you have to add apostrophes yourself.
   *
   * @param column the column to set the value for
   * @param value  the value to set the column to
   * @return the UpdateQuery object
   */
  public UpdateQuery set(String column, String value) {
    values.put(column, value);
    return this;
  }

  /**
   * Set the value of a column. Automatically sets the value to ? to be used with prepared statements.
   * <p>
   * MySQLAPIBukkit does not do any interpretation so you have to add apostrophes yourself.
   *
   * @param column the column to set the value for
   * @return the UpdateQuery object
   */
  public UpdateQuery set(String column) {
    set(column, "?");
    return this;
  }

  /**
   * Add a where clause.
   *
   * @param expression the expression
   * @return the UpdateQuery object
   */
  public UpdateQuery where(String expression) {
    wheres.add(expression);
    return this;
  }

  /**
   * Add a where clause.
   *
   * @param expression the expression
   * @return the UpdateQuery object
   */
  public UpdateQuery and(String expression) {
    where(expression);
    return this;
  }

  /**
   * Build the query as a String.
   *
   * @return the query as a String
   */
  public String build() {
    StringBuilder builder = new StringBuilder();
    builder.append("UPDATE ")
            .append(table)
            .append(" SET ");

    String seperator = "";
    for (Map.Entry<String, String> entry : values.entrySet()) {
      String column = entry.getKey();
      String value = entry.getValue();
      builder.append(seperator)
              .append(column)
              .append("=")
              .append(value);
      seperator = ",";
    }

    if (wheres.size() > 0) {
      builder.append(" WHERE ")
              .append(QueryUtils.separate(wheres, " AND "));
    }

    return builder.toString();
  }

}
