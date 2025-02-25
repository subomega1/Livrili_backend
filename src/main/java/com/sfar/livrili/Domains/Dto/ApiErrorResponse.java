package com.sfar.livrili.Domains.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class ApiErrorResponse {
    private int status;
    private String message;
    private List<FieldsError> fields;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FieldsError {
        private String field;
        private String message;
    }

}
