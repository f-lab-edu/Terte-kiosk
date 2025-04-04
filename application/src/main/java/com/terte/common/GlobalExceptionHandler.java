package com.terte.common;

import com.terte.common.exception.NotFoundException;
import com.terte.dto.common.ApiResDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.concurrent.CompletionException;

@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 비동기 코드는 CompletionException으로 감싸져 있음
     * CompletionException의 경우 getCause()로 원본 예외를 꺼내야 함
     * 따라서 cause가 있는 경우 cause를 처리하고, 없는 경우는 원본 예외를 처리
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResDTO<String>> handleGlobalException(Exception ex) {
        Throwable actualException = (ex instanceof CompletionException && ex.getCause() != null)
                ? ex.getCause()
                : ex;

        if (actualException instanceof NotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResDTO.error(actualException.getMessage()));
        } else if (actualException instanceof IllegalArgumentException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResDTO.error(actualException.getMessage()));
        } else if (actualException instanceof MethodArgumentNotValidException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResDTO.error("Validation error: " + actualException.getMessage()));
        } else if (actualException instanceof MethodArgumentTypeMismatchException) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResDTO.error("Invalid parameter: " + actualException.getMessage()));
        } else if (actualException instanceof RuntimeException) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResDTO.error("Unexpected error: " + actualException.getMessage()));
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResDTO.error("Unhandled exception: " + actualException.getMessage()));
    }


    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResDTO<String>> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResDTO.error("Unexpected error: " + ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResDTO<String>> handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResDTO.error("Invalid parameter: " + ex.getName()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResDTO<String>> handleValidationException(MethodArgumentNotValidException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResDTO.error("Validation error: " + ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResDTO<String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResDTO.error(ex.getMessage()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResDTO<String>> handleNotFoundException(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResDTO.error(ex.getMessage()));
    }
}
