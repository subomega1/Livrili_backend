package com.sfar.livrili.Domains.Dto.ErrorDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public  class FieldsError {
    private String field;
    private String message;
}