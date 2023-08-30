package com.torder.service.common.jpa.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile({"prod", "stage"})
@EnableJpaAuditing
public class DataSourceConfiguration {

  public static final String WRITE_DATASOURCE = "writeDataSource";
  public static final String READ_DATASOURCE = "readDataSource";

  @Bean(WRITE_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.write.datasource")
  public DataSource masterDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean(READ_DATASOURCE)
  @ConfigurationProperties(prefix = "spring.read.datasource")
  public DataSource slaveDataSource() {
    return DataSourceBuilder.create().build();
  }

  @Bean
  @Primary
  @DependsOn({WRITE_DATASOURCE, READ_DATASOURCE})
  public DataSource routingDataSource(
      @Qualifier(WRITE_DATASOURCE) DataSource writeDataSource,
      @Qualifier(READ_DATASOURCE) DataSource readDataSource) {

    RoutingDataSource routingDataSource = new RoutingDataSource();

    Map<Object, Object> datasourceMap =
        new HashMap<>() {
          {
            put("write", writeDataSource);
            put("read", readDataSource);
          }
        };

    routingDataSource.setTargetDataSources(datasourceMap);
    routingDataSource.setDefaultTargetDataSource(writeDataSource);

    return routingDataSource;
  }

  @Bean
  @DependsOn("routingDataSource")
  public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource) {
    return new LazyConnectionDataSourceProxy(routingDataSource);
  }
}
