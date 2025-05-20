package com.collicode.account.infrastructure.api;

import com.collicode.account.infrastructure.dto.request.AccountRequest;
import com.collicode.account.infrastructure.service.AccountQueryService;
import com.collicode.account.util.ResponseHandler;
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

import java.util.HashMap;
import java.util.Map;

import static com.collicode.account.infrastructure.api.AccountConstants.*;

@Service
public class AccountApiHandler {

    private final BusinessCommandRouterService businessCommandRouterService;
    private final AccountQueryService accountQueryService;

    public AccountApiHandler(BusinessCommandRouterService businessCommandRouterService,
                             AccountQueryService accountQueryService) {
        this.businessCommandRouterService = businessCommandRouterService;
        this.accountQueryService = accountQueryService;
    }

    public Mono<ServerResponse> createAccount(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        return serverRequest.bodyToMono(String.class)
                .flatMap(requestBody -> {
                    TypeToken<ApiRequest<AccountRequest>> typeToken = new TypeToken<>() {
                    };
                    ApiRequest<AccountRequest> request = JsonHelper.toObject(requestBody, typeToken.getType());

                    CommandWrapper<AccountRequest> commandWrapper = CommandWrapper.<AccountRequest>builder()
                            .entityName(ACCOUNT)
                            .actionName(CREATE)
                            .auditInfo(auditInfo)
                            .withOriginalApiRequest(request)
                            .build();

                    return businessCommandRouterService.processCommand(commandWrapper);
                })
                .transform(resultMono -> ResponseHandler.handleCommandResultMonoResponse(resultMono, auditInfo));
    }

    public Mono<ServerResponse> fetchAccountById(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);
        long accountId = Long.parseLong(serverRequest.pathVariable("accountId"));

        return ResponseHandler.handleMonoResponse(
                accountQueryService.fetchAccount(accountId),
                auditInfo
        );
    }

    public Mono<ServerResponse> fetchAllAccounts(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);
        Map<String, String> filters = new HashMap<>();

        serverRequest.queryParam("iban").ifPresent(value -> filters.put("iban", value));
        serverRequest.queryParam("bicSwift").ifPresent(value -> filters.put("bicSwift", value));
        serverRequest.queryParam("cardAlias").ifPresent(value -> filters.put("cardAlias", value));

        return ResponseHandler.handleFluxResponse(
                accountQueryService.fetchAllAccounts(filters),
                auditInfo
        );
    }

    public Mono<ServerResponse> deleteAccount(ServerRequest serverRequest) {
        AuditInfo auditInfo = AuditInfo.from(serverRequest);

        return serverRequest.bodyToMono(String.class)
                .flatMap(requestBody -> {
                    TypeToken<ApiRequest<AccountRequest>> typeToken = new TypeToken<>() {
                    };
                    ApiRequest<AccountRequest> request = JsonHelper.toObject(requestBody, typeToken.getType());

                    CommandWrapper<AccountRequest> commandWrapper = CommandWrapper.<AccountRequest>builder()
                            .entityName(ACCOUNT)
                            .actionName(DELETE)
                            .auditInfo(auditInfo)
                            .withOriginalApiRequest(request)
                            .build();

                    return businessCommandRouterService.processCommand(commandWrapper);
                })
                .transform(resultMono -> ResponseHandler.handleCommandResultMonoResponse(resultMono, auditInfo));
    }

}
