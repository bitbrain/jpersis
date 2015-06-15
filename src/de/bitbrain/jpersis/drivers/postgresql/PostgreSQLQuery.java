/*
 * Copyright 2015 Miguel Gonzalez
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

package de.bitbrain.jpersis.drivers.postgresql;

import java.sql.Statement;

import de.bitbrain.jpersis.drivers.jdbc.JDBCQuery;
import de.bitbrain.jpersis.util.Naming;

public class PostgreSQLQuery extends JDBCQuery {

  public PostgreSQLQuery(Class<?> model, Naming naming, Statement statement) {
    super(model, naming, statement);
  }

  @Override
  protected Slang createSlang() {
    return new Slang() {

      @Override
      public String getAutoIncrement() {
        return "";
      }

      @Override
      public String getTypeRangeString() {
        return "";
      }

      @Override
      public String getPrimaryKey() {
        return "";
      }

    };
  }

  @Override
  protected String[] modifications(String table, String primaryKey, boolean autoIncrement) {
    String pk_name = table + "_" + primaryKey + "_pk";
    String sequence_name = table + "_" + primaryKey + "_seq";
    String pkQuery = "ALTER TABLE  " + table + "  ADD CONSTRAINT  " + pk_name + "  PRIMARY KEY(" + primaryKey + ")";
    if (autoIncrement) {
      return new String[] { pkQuery, "CREATE SEQUENCE  " + sequence_name,
          "ALTER TABLE  " + table + "  ALTER COLUMN " + primaryKey + " SET DEFAULT NEXTVAL('" + sequence_name + "')" };
    } else {
      return new String[] { pkQuery };
    }

  }
}
