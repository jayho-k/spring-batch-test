package com.example.Partition_Test.ChunkTest.config.tenant;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TenantConnectProvider implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

    private static final String DEFAULT_SCHEMA = "spring_batch";
    private final DataSource dataSource;

    @Override
    public Connection getAnyConnection() throws SQLException {
        log.info("getAnyConnection");
        return getConnection(DEFAULT_SCHEMA);
    }

    @Override
    public void releaseAnyConnection(Connection connection) throws SQLException {
        log.info("connection close {}",connection);
        connection.close();
    }

    @Override
    public Connection getConnection(Object schema) throws SQLException {

        Connection connection = dataSource.getConnection();
        connection.setCatalog(schema.toString());
        log.info("getCatalog : {}" , connection.getCatalog());

        return connection;
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        log.info("releaseConnection : {}, tenantIdentifier {} : " , connection.getCatalog(), tenantIdentifier);
        connection.setCatalog(connection.getCatalog());
        connection.close();
    }

    @Override
    public boolean supportsAggressiveRelease() {
        return false;
    }

    @Override
    public @UnknownKeyFor @NonNull @Initialized boolean isUnwrappableAs(@UnknownKeyFor @NonNull @Initialized Class<@UnknownKeyFor @NonNull @Initialized ?> unwrapType) {
        return false;
    }

    @Override
    public <T> T unwrap(@UnknownKeyFor @NonNull @Initialized Class<T> unwrapType) {
        return null;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
    }
}
