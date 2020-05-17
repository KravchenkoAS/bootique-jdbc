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

import io.bootique.di.BQModule;
import io.bootique.jdbc.test.tester.HikariDerbyConfig;
import io.bootique.jdbc.test.tester.TestDataSourceConfig;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.io.File;

/**
 * A helper class that is declared in a unit test and manages database startup, schema and data initialization and
 * shutdown. Currently works only with Hikari DataSource provider.
 *
 * @since 2.0
 */
// TODO: change to a custom lifecycle
public class JdbcTester implements BeforeAllCallback, AfterAllCallback {

    private TestDataSourceConfig dataSourceConfig;

    public JdbcTester() {
        // TODO: Don't assume Maven, use deletable temp dir.
        this.dataSourceConfig = new HikariDerbyConfig(new File("target/derby"));
    }

    /**
     * Returns a Bootique module that can be used to configure a test DataSource in test Bootique runtime.
     *
     * @param dataSourceName the name of the DataSource
     * @return a new Bootique module with test DataSource configuration.
     */
    public BQModule setOrReplaceDataSource(String dataSourceName) {
        return binder -> dataSourceConfig.configure(binder, dataSourceName);
    }


    @Override
    public void beforeAll(ExtensionContext context) {

    }

    @Override
    public void afterAll(ExtensionContext context) {

    }
}