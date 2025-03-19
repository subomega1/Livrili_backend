package com.sfar.livrili.Validation;

import org.springframework.stereotype.Component;

@Component
public class PackValidation {

    public static Boolean isWeightPositive(float weight) {
        return weight > 0;
    }
}
