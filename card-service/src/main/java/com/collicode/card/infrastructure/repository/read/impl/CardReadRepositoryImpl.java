package com.collicode.card.infrastructure.repository.read.impl;

import com.collicode.card.infrastructure.repository.model.read.CardReadModel;
import com.collicode.card.infrastructure.repository.read.CardReadRepository;
import com.collicode.card.util.QueryUtil;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.Set;

@Repository
public class CardReadRepositoryImpl implements CardReadRepository {
    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public CardReadRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<CardReadModel> fetchCardById(long cardId) {
        return r2dbcEntityTemplate
                .selectOne(
                        Query.query(Criteria.where("recordId").is(cardId)),
                        CardReadModel.class
                );
    }

    @Override
    public Flux<CardReadModel> fetchCardsByAccountId(long accountId) {
        return r2dbcEntityTemplate
                .select(
                        Query.query(Criteria.where("accountId").is(accountId)),
                        CardReadModel.class
                );
    }

    @Override
    public Flux<CardReadModel> fetchAllCards(Map<String, String> filters) {
        Set<String> allowedFields = Set.of("alias", "cardType", "pan");


        Criteria criteria = QueryUtil.createCriteria(filters, allowedFields);

        PageRequest pageRequest = QueryUtil.pageRequest(filters);
        Query query = Query.query(criteria)
                .limit(pageRequest.getPageSize())
                .offset(pageRequest.getOffset());

        return r2dbcEntityTemplate.select(query, CardReadModel.class);
    }
}
