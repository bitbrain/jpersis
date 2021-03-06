/*
 * Copyright 2014 Miguel Gonzalez
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at 
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.bitbrain.jpersis.drivers.sqllite;

import java.sql.Statement;

import de.bitbrain.jpersis.drivers.jdbc.JDBCQuery;
import de.bitbrain.jpersis.drivers.jdbc.SQL;
import de.bitbrain.jpersis.util.Naming;

public class SQLiteQuery extends JDBCQuery {

  public SQLiteQuery(Class<?> model, Naming naming, Statement statement) {
    super(model, naming, statement);
  }

  @Override
  protected Slang createSlang() {
    return new Slang() {

      @Override
      public String getAutoIncrement() {
        return SQL.AUTOINCREMENT_SQLITE;
      }

      @Override
      public String getTypeRangeString() {
        return "";
      }

      @Override
      public String getPrimaryKey() {
        return SQL.PRIMARY_KEY;
      }

      @Override
      public boolean isAutoIncrementTyped() {
        return false;
      }

      @Override
      public String getReturningOptional(String key) {
        return "";
      }
    };
  }
}
