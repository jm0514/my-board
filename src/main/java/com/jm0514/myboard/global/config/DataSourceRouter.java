package com.jm0514.myboard.global.config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class DataSourceRouter extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        boolean readOnly = TransactionSynchronizationManager.isCurrentTransactionReadOnly();

        System.out.println("Transaction의 Read Only가 " + readOnly + " 입니다.");

        if (readOnly) {
            System.out.println("Replica 서버로 요청합니다.");
            return "read";
        }

        System.out.println("Source 서버로 요청합니다.");
        return "write";
    }
}
