package com.collicode.account.util;


import com.collicode.shared.domain.api.AuditInfo;
import com.collicode.shared.domain.api.CommandResult;
import com.collicode.shared.exception.BusinessEntityException;
import com.collicode.shared.exception.BusinessException;
import com.collicode.shared.exception.SystemException;
import com.collicode.shared.response.ErrorMessage;
import com.collicode.shared.util.APIResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ResponseHandler {

    private static final Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    public static <R> Mono<ServerResponse> handleMonoResponse(CommandResult<R> response, AuditInfo auditInfo) {
        try {
            return ServerResponse.status(HttpStatus.OK)
                    .bodyValue(APIResponseUtil.successApiResponse(auditInfo, response));
        } catch (Throwable error) {
            return handleError(error, auditInfo);
        }
    }

    public static <R> Mono<ServerResponse> handleCommandResultMonoResponse(Mono<CommandResult<R>> response, AuditInfo auditInfo) {
        return response.flatMap(result -> {
                    var apiResponse = APIResponseUtil.successApiResponse(auditInfo, result.getResult());
                    return ServerResponse.status(HttpStatus.OK).bodyValue(apiResponse);
                })
                .onErrorResume(error -> handleError(error, auditInfo))
                .switchIfEmpty(ServerResponse.ok().bodyValue(APIResponseUtil.failedApiResponse(
                        auditInfo,
                        ErrorMessage.of(ResponseCode.NOTFOUND.getCode(), ResponseCode.NOTFOUND.getMessage())
                )));
    }

    public static Mono<ServerResponse> handleMonoResponse(Mono<?> response, AuditInfo auditInfo) {
        return response.flatMap(result -> {
                    var apiResponse = APIResponseUtil.successApiResponse(auditInfo, result);
                    logger.info("{} processed request successfully responding back", apiResponse.getMeta().getResponseId());
                    return ServerResponse.status(HttpStatus.OK).bodyValue(apiResponse);
                })
                .onErrorResume(error -> handleError(error, auditInfo))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(APIResponseUtil.failedApiResponse(
                        auditInfo,
                        ErrorMessage.of(ResponseCode.NOTFOUND.getCode(), ResponseCode.NOTFOUND.getMessage())
                )));
    }

    public static <R> Mono<ServerResponse> handleFluxResponse(Flux<R> response, AuditInfo auditInfo) {
        return response.collectList()
                .flatMap(resultList -> {
                    var apiResponse = APIResponseUtil.successApiListResponse(auditInfo, resultList);
                    return ServerResponse.status(HttpStatus.OK).bodyValue(apiResponse);
                })
                .onErrorResume(error -> handleError(error, auditInfo))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).bodyValue(APIResponseUtil.failedApiResponse(
                        auditInfo,
                        ErrorMessage.of(ResponseCode.NOTFOUND.getCode(), ResponseCode.NOTFOUND.getMessage())
                )));
    }

    private static Mono<ServerResponse> handleError(Throwable error, AuditInfo auditInfo) {
        error.printStackTrace();

        if (error instanceof BusinessException e) {
            var apiResponse = APIResponseUtil.failedApiResponse(auditInfo, ErrorMessage.of(e.getCode(), e.getMessage()));
            logger.error("{} request failed with error {}", auditInfo.getTransactionId(), e.getMessage());
            return ServerResponse.badRequest().bodyValue(apiResponse);
        }

        if (error instanceof BusinessEntityException e) {
            var apiResponse = APIResponseUtil.failedApiResponse(auditInfo, ErrorMessage.of(e.getCode(), e.getMessage()));
            logger.error("{} request failed with error {}", auditInfo.getTransactionId(), e.getMessage());
            return ServerResponse.badRequest().bodyValue(apiResponse);
        }

        if (error instanceof SystemException e) {
            var apiResponse = APIResponseUtil.failedApiResponse(auditInfo, ErrorMessage.of(e.getCode(), e.getMessage()));
            logger.error("{} request failed with error {}", auditInfo.getTransactionId(), e.getMessage());
            return ServerResponse.badRequest().bodyValue(apiResponse);
        }

        var apiResponse = APIResponseUtil.failedApiResponse(
                auditInfo,
                ErrorMessage.of(ResponseCode.INTERNALSERVERERROR.getCode(), ResponseCode.INTERNALSERVERERROR.getMessage())
        );
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR).bodyValue(apiResponse);
    }
}

