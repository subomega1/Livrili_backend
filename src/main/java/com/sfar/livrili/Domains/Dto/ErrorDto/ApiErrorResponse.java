package com.sfar.livrili.Domains.Dto.ErrorDto;

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



}
