package io.bootique.jdbc.test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * A helper class to run common DB operations during unit tests.
 *
 * @since 0.12
 */
public class DefaultDatabaseChannel implements DatabaseChannel {

    protected DataSource dataSource;

    public DefaultDatabaseChannel(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public <T> List<T> select(String sql, long maxRows, Function<ResultSet, T> rowReader) {
        try {
            return selectWithExceptions(sql, maxRows, rowReader);
        } catch (SQLException e) {
            throw new RuntimeException("Error running SQL: " + sql, e);
        }
    }

    protected <T> List<T> selectWithExceptions(String sql, long maxRows, Function<ResultSet, T> rowReader)
            throws SQLException {

        List<T> result = new ArrayList<>();

        Logger.log(sql);
        try (Connection c = getConnection();) {
            try (PreparedStatement st = c.prepareStatement(sql);) {
                try (ResultSet rs = st.executeQuery();) {

                    while (rs.next() && result.size() < maxRows) {
                        result.add(rowReader.apply(rs));
                    }
                }
            }
        }

        return result;
    }

    @Override
    public int update(String sql, List<Binding> bindings) {
        try {
            return updateWithExceptions(sql, bindings);
        } catch (SQLException e) {
            throw new RuntimeException("Error running SQL: " + sql, e);
        }
    }

    protected int updateWithExceptions(String sql, List<Binding> bindings) throws SQLException {
        Logger.log(sql);

        try (Connection c = getConnection();) {

            int count;
            try (PreparedStatement st = c.prepareStatement(sql);) {

                for (int i = 0; i < bindings.size(); i++) {
                    bindings.get(i).bind(st, i);
                }

                count = st.executeUpdate();
            }

            c.commit();
            return count;
        }
    }

    @Override
    public Connection getConnection() {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException("Error opening connection", e);
        }

        try {
            connection.setAutoCommit(false);
        } catch (SQLException e) {

            try {
                connection.close();
            } catch (SQLException ignored) {
            }
        }
        return connection;
    }
}