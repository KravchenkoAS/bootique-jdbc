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
package io.bootique.jdbc.test;

import io.bootique.BQRuntime;
import io.bootique.Bootique;
import io.bootique.test.junit5.BQApp;
import io.bootique.test.junit5.BQTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.sql.ResultSet;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BQTest
public class JdbcTester_ShouldDeleteBeforeEachTest_PostgresIT extends BaseJdbcTesterTest {

    @RegisterExtension
    static final JdbcTester jdbcTester = JdbcTester
            .useTestcontainers("jdbc:tc:postgresql:11:///mydb")
            .initDB("classpath:io/bootique/jdbc/test/JdbcTester_ShouldDeleteBeforeEachTest_PostgresIT.sql")
            .shouldDeleteBeforeEachTest("a", "b");

    @BQApp(skipRun = true)
    static final BQRuntime app = Bootique.app()
            .autoLoadModules()
            .module(jdbcTester.setOrReplaceDataSource("myDS"))
            .createRuntime();

    @Test
    @DisplayName("Check tables are clean, insert data for next test")
    public void test1() {
        checkNoData();
        insertTestData();
    }

    @Test
    @DisplayName("Check tables are clean, insert data for next test")
    public void test2() {
        checkNoData();
        insertTestData();
    }

    protected void checkNoData() {
        run(app, c -> {
            try (Statement s = c.createStatement()) {
                try (ResultSet rs = s.executeQuery("select count(1) from a")) {
                    rs.next();
                    assertEquals(0, rs.getInt(1));
                }

                try (ResultSet rs = s.executeQuery("select count(1) from b")) {
                    rs.next();
                    assertEquals(0, rs.getInt(1));
                }
            }
        });
    }

    protected void insertTestData() {
        run(app, c -> {
            try (Statement s = c.createStatement()) {
                s.executeUpdate("insert into a (id, name) values (10, 'myname')");
                s.executeUpdate("insert into b (id, name, a_id) values (11, 'myname', 10)");
            }
        });
    }
}
