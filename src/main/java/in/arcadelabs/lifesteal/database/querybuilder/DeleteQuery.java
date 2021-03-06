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
import java.util.List;

/**
 * Creates a table
 *
 * @author Mrtenz
 * <a href="https://github.com/Mrtenz/MySQLAPI/tree/master/src/main/java/me/mrten/mysqlapi/queries">...</a>
 **/

public class DeleteQuery {

  private final String table;
  private final List<String> wheres = new ArrayList<>();

  /**
   * Create a delete query.
   *
   * @param table - the table to be deleted from
   */
  public DeleteQuery(String table) {
    this.table = table;
  }

  /**
   * Add a where clause.
   *
   * @param expression the expression
   * @return the DeleteQuery object
   */
  public DeleteQuery where(String expression) {
    wheres.add(expression);
    return this;
  }

  /**
   * Add a where clause.
   *
   * @param expression the expression
   * @return the DeleteQuery object
   */
  public DeleteQuery and(String expression) {
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
    builder.append("DELETE FROM ")
            .append(table);

    if (wheres.size() > 0) {
      builder.append(" WHERE ")
              .append(QueryUtils.separate(wheres, " AND "));
    }

    return builder.toString();
  }

}
