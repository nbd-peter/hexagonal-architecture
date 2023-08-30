package com.torder.service.common.jpa.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class RoutingDataSource extends AbstractRoutingDataSource {
  @Override
  protected Object determineCurrentLookupKey() { // (1)
    return (TransactionSynchronizationManager.isCurrentTransactionReadOnly())
        ? "read"
        : "write"; // (2)
  }
}
