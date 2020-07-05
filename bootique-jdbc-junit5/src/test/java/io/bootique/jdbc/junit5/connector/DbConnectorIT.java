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

package io.bootique.jdbc.junit5.connector;

import io.bootique.jdbc.junit5.DbTester;
import io.bootique.jdbc.junit5.metadata.DbMetadata;
import io.bootique.junit5.BQTest;
import io.bootique.junit5.BQTestTool;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@BQTest
public class DbConnectorIT {

    @BQTestTool
    static final DbTester derbyTester = DbTester.derbyDb();

    @BQTestTool
    static final DbTester mysqlTester = DbTester.testcontainersDb("jdbc:tc:mysql:8.0.20:///db");

    @Test
    public void testDerbyQuotes() {
        DataSource dataSource = derbyTester.getDataSource();
        DbConnector connector = new DbConnector(dataSource, DbMetadata.create(dataSource));
        assertEquals("\"a\"", connector.identifierQuoter.quoted("a"));
    }

    @Test
    public void testMySQLQuotes() {
        DataSource dataSource = mysqlTester.getDataSource();
        DbConnector connector = new DbConnector(dataSource, DbMetadata.create(dataSource));
        assertEquals("`a`", connector.identifierQuoter.quoted("a"));
    }
}
