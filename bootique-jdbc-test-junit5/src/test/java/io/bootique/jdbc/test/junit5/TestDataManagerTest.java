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

package io.bootique.jdbc.test.junit5;

import io.bootique.jdbc.test.DatabaseChannel;
import io.bootique.jdbc.test.Table;
import io.bootique.jdbc.test.matcher.TableMatcher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class TestDataManagerTest {

    private DatabaseChannel channel = mock(DatabaseChannel.class);

    @Test
    public void testGetTable() {
        Table t1 = Table.builder(channel, "t1").build();
        Table t2 = Table.builder(channel, "t2").build();

        TestDataManager dm = new TestDataManager(true, t1, t2);
        assertSame(t1, dm.getTable("t1"));
        assertSame(t2, dm.getTable("t2"));
    }

    @Test
    public void testGetTable_BadName() {
        Table t1 = Table.builder(channel, "t1").build();
        Table t2 = Table.builder(channel, "t2").build();

        TestDataManager dm = new TestDataManager(true, t1, t2);
        assertThrows(IllegalArgumentException.class, () -> dm.getTable("t3"));
    }

    @Test
    public void testEmptyConstructor() {
        TestDataManager dm = new TestDataManager(true);
        dm.beforeEach(mock(ExtensionContext.class));
    }

    @Test
    public void testMatcher() {
        Table t1 = Table.builder(channel, "t1").build();
        Table t2 = Table.builder(channel, "t2").build();

        TestDataManager dm = new TestDataManager(true, t1, t2);

        TableMatcher m1 = dm.matcher("t1");
        assertNotNull(m1);
        assertSame(t1, m1.getTable());

        TableMatcher m2 = dm.matcher("t2");
        assertNotNull(m2);
        assertSame(t2, m2.getTable());
    }

    @Test
    public void testMatcher_BadName() {
        Table t1 = Table.builder(channel, "t1").build();
        TestDataManager dm = new TestDataManager(true, t1);
        assertThrows(IllegalArgumentException.class, () -> dm.matcher("t3"));
    }
}
