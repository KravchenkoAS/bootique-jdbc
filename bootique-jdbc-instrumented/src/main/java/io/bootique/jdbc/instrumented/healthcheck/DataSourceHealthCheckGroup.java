package io.bootique.jdbc.instrumented.healthcheck;

import io.bootique.jdbc.DataSourceFactory;
import io.bootique.metrics.health.HealthCheck;
import io.bootique.metrics.health.HealthCheckGroup;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates individual healthchecks for a {@link io.bootique.jdbc.DataSourceFactory} DataSources. It is important to
 * report DataSource health individually.
 *
 * @since 0.12
 */
public class DataSourceHealthCheckGroup implements HealthCheckGroup {

    private DataSourceFactory dataSourceFactory;

    public DataSourceHealthCheckGroup(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

    @Override
    public Map<String, HealthCheck> getHealthChecks() {

        Map<String, HealthCheck> checks = new HashMap<>();

        // TODO: namespacing module healthchecks..
        dataSourceFactory.allNames().forEach(n ->
                checks.put(healthCheckName(n), new DataSourceHealthCheck(dataSourceFactory, n)));

        return checks;
    }

    // generate stable qualified name for the DataSource healthcheck
    private String healthCheckName(String dataSourceName) {
        return "bq.jdbc.canConnect." + dataSourceName;
    }
}
