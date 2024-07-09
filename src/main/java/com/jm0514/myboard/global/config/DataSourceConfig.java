package com.jm0514.myboard.global.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
public class DataSourceConfig {

    private static final String SOURCE_SERVER = "SOURCE";
    private static final String REPLICA_SERVER = "REPLICA";

    @Bean
    @Qualifier(SOURCE_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.write")
    public DataSource writeDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    @Qualifier(REPLICA_SERVER)
    @ConfigurationProperties(prefix = "spring.datasource.read")
    public DataSource readDataSource() {
        return DataSourceBuilder.create()
                .build();
    }

    @Bean
    public DataSource routeDataSource(
            @Qualifier(SOURCE_SERVER) DataSource sourceDataSource,
            @Qualifier(REPLICA_SERVER) DataSource replicaDataSource
    ) {
        DataSourceRouter dataSourceRouter = new DataSourceRouter();

        HashMap<Object, Object> dataSourceMap = new HashMap<>();
        dataSourceMap.put("write", sourceDataSource);
        dataSourceMap.put("read", replicaDataSource);

        dataSourceRouter.setTargetDataSources(dataSourceMap);
        dataSourceRouter.setDefaultTargetDataSource(sourceDataSource);
        return dataSourceRouter;
    }

    @Bean
    @Primary
    public DataSource dataSource() {
        DataSource determinedDataSource = routeDataSource(writeDataSource(), readDataSource());
        LazyConnectionDataSourceProxy lazyDateSource = new LazyConnectionDataSourceProxy(determinedDataSource);
        lazyDateSource.setDefaultAutoCommit(false);
        return lazyDateSource;
    }
}
