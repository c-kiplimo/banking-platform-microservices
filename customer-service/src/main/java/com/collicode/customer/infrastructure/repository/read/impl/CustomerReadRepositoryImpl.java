package com.collicode.customer.infrastructure.repository.read.impl;

import com.collicode.customer.infrastructure.repository.model.read.CustomerReadModel;
import com.collicode.customer.infrastructure.repository.read.CustomerReadRepository;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.data.relational.core.query.Criteria;
import org.springframework.data.relational.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Repository
public class CustomerReadRepositoryImpl implements CustomerReadRepository {

    private final R2dbcEntityTemplate r2dbcEntityTemplate;

    public CustomerReadRepositoryImpl(R2dbcEntityTemplate r2dbcEntityTemplate) {
        this.r2dbcEntityTemplate = r2dbcEntityTemplate;
    }

    @Override
    public Mono<CustomerReadModel> fetchCustomerById(long customerId) {
        return r2dbcEntityTemplate
                .selectOne(
                        Query.query(Criteria.where("recordId").is(customerId)),
                        CustomerReadModel.class
                );
    }

    @Override
    public Flux<CustomerReadModel> fetchAllCustomers(String name, LocalDate startDate, LocalDate endDate) {
        Criteria criteria = Criteria.empty();

        // Full text search across firstName, lastName, otherName
        if (name != null && !name.isBlank()) {
            Criteria nameCriteria = Criteria.where("first_name").like("%" + name + "%")
                    .or("last_name").like("%" + name + "%")
                    .or("other_name").like("%" + name + "%");
            criteria = criteria.and(nameCriteria);
        }

        // Date range filtering on createdAt
        if (startDate != null && endDate != null) {
            criteria = criteria.and(
                    Criteria.where("created_at").between(
                            startDate.atStartOfDay(),
                            endDate.atTime(23, 59, 59)
                    )
            );

        } else if (startDate != null) {
            criteria = criteria.and(
                    Criteria.where("created_at")
                            .greaterThanOrEquals(startDate.atStartOfDay())
            );
        } else if (endDate != null) {
            criteria = criteria.and(
                    Criteria.where("created_at")
                            .lessThanOrEquals(endDate.atTime(23, 59, 59))
            );
        }

        Query query = Query.query(criteria);

        return r2dbcEntityTemplate.select(query, CustomerReadModel.class);
    }
}
