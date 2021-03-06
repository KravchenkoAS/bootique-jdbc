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
package io.bootique.jdbc.junit5.tester;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.bootique.annotation.BQConfig;
import io.bootique.di.Injector;
import io.bootique.di.Key;
import io.bootique.jdbc.managed.ManagedDataSourceFactory;
import io.bootique.jdbc.managed.ManagedDataSourceStarter;
import io.bootique.jdbc.junit5.DbTester;

/**
 * @since 2.0
 */
@BQConfig("Test DataSource configuration")
@JsonTypeName("bqjdbctest")
// must be able to deserialize over the existing configs, so instruct Jackson to be lenient
@JsonIgnoreProperties(ignoreUnknown = true)
public class TestDataSourceFactory implements ManagedDataSourceFactory {

    @Override
    public ManagedDataSourceStarter create(String dataSourceName, Injector injector) {

        DbTester tester = injector.getInstance(Key.get(DbTester.class, dataSourceName));

        return new ManagedDataSourceStarter(
                null, // we don't know the URL around here. Also passing null URL allows to bypass legacy DerbyListener
                () -> tester.getDataSource(), // return fixed DataSource managed by JdbcTester
                ds -> {
                }  // don't let Bootique to do shutdown... JdbcTester is responsible for it
        );
    }
}
