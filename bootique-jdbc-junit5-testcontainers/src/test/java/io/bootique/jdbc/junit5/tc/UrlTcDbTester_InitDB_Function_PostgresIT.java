/*
 * Licensed to ObjectStyle LLC under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ObjectStyle LLC licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package io.bootique.jdbc.junit5.tc;

import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestTool;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@BQTest
public class UrlTcDbTester_InitDB_Function_PostgresIT {

    @BQTestTool
    static final TcDbTester db = TcDbTester
            .db("jdbc:tc:postgresql:11:///")
            .initDB(UrlTcDbTester_InitDB_Function_PostgresIT::initDB);

    static void initDB(Connection c) throws SQLException {
        c.setAutoCommit(false);
        try (Statement s = c.createStatement()) {
            s.executeUpdate("create table b (id integer not null primary key, name text)");
        }
        c.commit();
    }

    @Test
    @DisplayName("DB was initialized with custom function")
    public void testInitDB() throws SQLException {

        try (Connection c = db.getConnection()) {

            // procedure must be there, and the second definition from the test must be in use
            try (Statement s = c.createStatement()) {
                s.executeUpdate("insert into b (id, name) values (77, 'x')");
            }

            try (Statement s = c.createStatement()) {
                try (ResultSet rs = s.executeQuery("select * from b")) {
                    assertTrue(rs.next());
                    assertEquals(77, rs.getInt("id"));
                    assertEquals("x", rs.getString("name"));
                }
            }
        }
    }
}
