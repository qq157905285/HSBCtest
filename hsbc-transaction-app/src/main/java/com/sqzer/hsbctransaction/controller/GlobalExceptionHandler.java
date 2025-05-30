package com.sqzer.hsbctransaction.controller;

import com.sqzer.hsbctransaction.enums.HttpStatusCode;
import com.sqzer.hsbctransaction.exception.CustomRateLimitException;
import com.sqzer.hsbctransaction.exception.ResourceNotFoundException;
import com.sqzer.hsbctransaction.model.ApiResponse;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ApiResponse<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return ApiResponse.error(HttpStatusCode.BAD_REQUEST, "Validation Failed", errors);
    }

    @ExceptionHandler(RequestNotPermitted.class)
    public ApiResponse<String> handleRateLimitException(RequestNotPermitted ex) {
        return ApiResponse.error(HttpStatusCode.TOO_MANY_REQUESTS, "接口访问太频繁，请稍后再试");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ApiResponse<String> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        String message = String.format("参数 '%s' 的值 '%s' 类型错误，应为 %s",
                ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());
        return ApiResponse.error(HttpStatusCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ApiResponse<String> handleNotFound(ResourceNotFoundException ex) {
        return ApiResponse.error(HttpStatusCode.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ApiResponse<String> handleGenericException(Exception ex) {
        return ApiResponse.error(HttpStatusCode.INTERNAL_ERROR, ex.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ApiResponse<String> handleMissingParam(MissingServletRequestParameterException ex) {
        String message = String.format("缺少请求参数: %s（类型: %s）", ex.getParameterName(), ex.getParameterType());
        return ApiResponse.error(HttpStatusCode.BAD_REQUEST, message);
    }

    @ExceptionHandler(CustomRateLimitException.class)
    public ApiResponse<String> handleCustomRateLimitException(CustomRateLimitException ex) {
        System.out.println("CustomRateLimitException捕获到了：" + ex.getMessage());
        return ApiResponse.error(HttpStatusCode.TOO_MANY_REQUESTS, ex.getMessage());
    }

    @ExceptionHandler(Throwable.class)
    public ApiResponse<String> handleThrowable(Throwable ex) {
        ex.printStackTrace();
        return ApiResponse.error(HttpStatusCode.INTERNAL_ERROR, "全局异常: " + ex.getMessage());
    }
}