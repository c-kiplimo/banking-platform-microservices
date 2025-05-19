package com.collicode.account.infrastructure.repository.read.impl;

import com.collicode.account.infrastructure.repository.model.read.AccountReadModel;
import com.collicode.account.infrastructure.repository.read.AccountReadRepository;
import com.collicode.account.util.QueryUtil;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Repository
public class AccountReadRepositoryImpl implements AccountReadRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public AccountReadRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }


    @Override
    public Mono<AccountReadModel> fetchAccountById(long accountId) {
        return r2dbcEntityTemplate
                .selectOne(
                        Query.query(Criteria.where("recordId").is(accountId)),
                        AccountReadModel.class
                );
    }

    @Override
    public Flux<AccountReadModel> fetchAllAccounts(Map<String, String> filters) {
        Set<String> allowedFields = Set.of("iban", "bicSwift", "cardAlias");


        Criteria criteria = QueryUtil.createCriteria(filters, allowedFields);

        var pageRequest = QueryUtil.pageRequest(filters);

        return r2dbcEntityTemplate.select(
                Query.query(criteria)
                        .limit(pageRequest.getPageSize())
                        .offset(pageRequest.getOffset()),
                AccountReadModel.class
        );
    }

}
