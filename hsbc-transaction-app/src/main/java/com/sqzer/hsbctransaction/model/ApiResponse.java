package com.sqzer.hsbctransaction.model;

import com.sqzer.hsbctransaction.enums.HttpStatusCode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public static <T> ApiResponse<T> success(T data) {
        return of(HttpStatusCode.OK.getCode(), HttpStatusCode.OK.getDefaultMessage(), data);
    }

    public static ApiResponse<Void> success() {
        return of(HttpStatusCode.OK.getCode(), HttpStatusCode.OK.getDefaultMessage(), null);
    }

    public static ApiResponse<Void> error(HttpStatusCode status) {
        return of(status.getCode(), status.getDefaultMessage(), null);
    }

    public static <T> ApiResponse<T> error(HttpStatusCode status, T data) {
        return of(status.getCode(), status.getDefaultMessage(), data);
    }

    public static <T> ApiResponse<T> error(HttpStatusCode status, String customMessage, T data) {
        return of(status.getCode(), customMessage, data);
    }

    public boolean isSuccess() {
        return this.code == HttpStatusCode.OK.getCode();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public static <T> ApiResponse<T> of(int code, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setCode(code);
        response.setMessage(message);
        response.setData(data);
        return response;
    }
}
