package io.bootique.jdbc.instrumented.tomcat;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import io.bootique.jdbc.DataSourceFactory;
import io.bootique.jdbc.JdbcModule;
import io.bootique.jdbc.instrumented.tomcat.healthcheck.DataSourceHealthCheckGroup;
import io.bootique.metrics.health.HealthCheckModule;

public class TomcatInstrumentedJdbcModule implements Module {

    @Override
    public void configure(Binder binder) {
        JdbcModule.extend(binder).addDataSourceListener(TomcatMetricsInitializer.class);
        HealthCheckModule.extend(binder).addHealthCheckGroup(DataSourceHealthCheckGroup.class);
    }

    @Singleton
    @Provides
    TomcatMetricsInitializer provideMetricsListener(MetricRegistry metricRegistry) {
        return new TomcatMetricsInitializer(metricRegistry);
    }

    @Singleton
    @Provides
    DataSourceHealthCheckGroup provideDataSourceHealthCheckGroup(DataSourceFactory dataSourceFactory) {
        return new DataSourceHealthCheckGroup(dataSourceFactory);
    }
}
