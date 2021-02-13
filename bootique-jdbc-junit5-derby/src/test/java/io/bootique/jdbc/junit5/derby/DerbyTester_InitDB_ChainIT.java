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
package io.bootique.jdbc.junit5.derby;

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.junit5.BQApp;
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
public class DerbyTester_InitDB_ChainIT extends BaseDerbyTesterTest {

    @BQTestTool
    static final DerbyTester db = DerbyTester
            .db()
            .initDB("classpath:io/bootique/jdbc/junit5/derby/derby_create_table_b.sql")
            .initDB(DerbyTester_InitDB_ChainIT::createTableA)
            .initDB("classpath:io/bootique/jdbc/junit5/derby/derby_insert_a_b.sql");

    static void createTableA(Connection c) throws SQLException {
        c.createStatement().execute("create table \"a\" (\"id\" integer not null, \"name\" varchar(20))");
    }

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique.app()
            .autoLoadModules()
            .module(db.moduleWithTestDataSource("myDS"))
            .createRuntime();

    @Test
    @DisplayName("DB was initialized")
    public void testInitDB() {
        run(app, c -> {
            try (Statement s = c.createStatement()) {
                try (ResultSet rs = s.executeQuery("select * from \"a\"")) {
                    assertTrue(rs.next());
                    assertEquals(5, rs.getInt("id"));
                    assertEquals("I am an A", rs.getString("name"));
                }

                try (ResultSet rs = s.executeQuery("select * from \"b\"")) {
                    assertTrue(rs.next());
                    assertEquals(10, rs.getInt("id"));
                    assertEquals("I am a B", rs.getString("name"));
                }
            }
        });
    }
}
