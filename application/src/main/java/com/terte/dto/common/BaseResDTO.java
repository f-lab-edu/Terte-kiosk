package com.terte.dto.common;

import com.terte.common.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BaseResDTO {
    private ResponseStatus status;  // 성공 상태 ("success" or "error")
    private String message; // 응답 메시지
}
