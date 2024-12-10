package com.terte.dto.common;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class ApiResDTO<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResDTO(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public ApiResDTO(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }


    public static <T> ApiResDTO<T> success(T data) {
        return new ApiResDTO<>(true, "요청이 성공했습니다.", data);
    }

    public static <T> ApiResDTO<T> success() {
        return new ApiResDTO<>(true, "요청이 성공했습니다.");
    }

    public static <T> ApiResDTO<T> error(String message) {
        return new ApiResDTO<>(false, message, null);
    }

}
