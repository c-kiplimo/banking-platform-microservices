package com.collicode.customer.infrastructure.api;

import com.collicode.customer.infrastructure.dto.request.CustomerRequest;
import com.collicode.customer.infrastructure.service.CustomerQueryService;
import com.collicode.customer.util.ResponseHandler;
import com.collicode.shared.domain.api.ApiRequest;
import com.collicode.shared.domain.api.AuditInfo;
import com.collicode.shared.domain.api.BusinessCommandRouterService;
import com.collicode.shared.domain.api.CommandWrapper;
import com.collicode.shared.util.JsonHelper;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Optional;

import static com.collicode.customer.infrastructure.api.CustomerConstants.*;

@Service
public class CustomerApiHandler {
    private final BusinessCommandRouterService businessCommandRouterService;
    private final CustomerQueryService customerQueryService;

    public CustomerApiHandler(BusinessCommandRouterService businessCommandRouterService, CustomerQueryService customerQueryService) {
        this.businessCommandRouterService = businessCommandRouterService;
        this.customerQueryService = customerQueryService;
    }

    public Mono<ServerResponse> createCustomer(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        return serverRequest.bodyToMono(String.class)
                .flatMap(requestBody -> {
                    TypeToken<ApiRequest<CustomerRequest>> typeToken = new TypeToken<>() {
                    };
                    ApiRequest<CustomerRequest> request = JsonHelper.toObject(requestBody, typeToken.getType());

                    CommandWrapper<CustomerRequest> commandWrapper = CommandWrapper.<CustomerRequest>builder()
                            .entityName(CUSTOMER)
                            .actionName(CREATE)
                            .auditInfo(auditInfo)
                            .withOriginalApiRequest(request)
                            .build();

                    return businessCommandRouterService.processCommand(commandWrapper);
                })
                .transform(resultMono -> ResponseHandler.handleCommandResultMonoResponse(resultMono, auditInfo));
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        return serverRequest.bodyToMono(String.class)
                .flatMap(requestBody -> {
                    TypeToken<ApiRequest<CustomerRequest>> typeToken = new TypeToken<>() {
                    };
                    ApiRequest<CustomerRequest> request = JsonHelper.toObject(requestBody, typeToken.getType());

                    CommandWrapper<CustomerRequest> commandWrapper = CommandWrapper.<CustomerRequest>builder()
                            .entityName(CUSTOMER)
                            .actionName(UPDATE)
                            .auditInfo(auditInfo)
                            .withOriginalApiRequest(request)
                            .build();

                    return businessCommandRouterService.processCommand(commandWrapper);
                })
                .transform(resultMono -> ResponseHandler.handleCommandResultMonoResponse(resultMono, auditInfo));
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        return serverRequest.bodyToMono(String.class)
                .flatMap(requestBody -> {
                    TypeToken<ApiRequest<CustomerRequest>> typeToken = new TypeToken<>() {
                    };
                    ApiRequest<CustomerRequest> request = JsonHelper.toObject(requestBody, typeToken.getType());

                    CommandWrapper<CustomerRequest> commandWrapper = CommandWrapper.<CustomerRequest>builder()
                            .entityName(CUSTOMER)
                            .actionName(DELETE)
                            .auditInfo(auditInfo)
                            .withOriginalApiRequest(request)
                            .build();

                    return businessCommandRouterService.processCommand(commandWrapper);
                })
                .transform(resultMono -> ResponseHandler.handleCommandResultMonoResponse(resultMono, auditInfo));
    }

    public Mono<ServerResponse> fetchCustomerById(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);
        long customerId = Long.parseLong(serverRequest.pathVariable("customerId"));

        return ResponseHandler.handleMonoResponse(
                customerQueryService.fetchCustomer(customerId),
                auditInfo
        );
    }

    public Mono<ServerResponse> fetchAllCustomers(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        Optional<String> nameOpt = serverRequest.queryParam("name");
        Optional<String> startDateStr = serverRequest.queryParam("startDate");
        Optional<String> endDateStr = serverRequest.queryParam("endDate");

        LocalDate startDate = startDateStr.map(LocalDate::parse).orElse(null);
        LocalDate endDate = endDateStr.map(LocalDate::parse).orElse(null);

        return ResponseHandler.handleFluxResponse(
                customerQueryService.fetchAllCustomers(nameOpt.orElse(null), startDate, endDate),
                auditInfo
        );
    }
}
