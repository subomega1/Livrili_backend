package com.sfar.livrili.Domains.Dto.ErrorDto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class IllegalArgs extends IllegalArgumentException {

     private List<FieldsError> errors = new ArrayList<>();
    public IllegalArgs(String message , List<FieldsError> errors) {
        super(message);
        this.errors = errors;
    }

}
