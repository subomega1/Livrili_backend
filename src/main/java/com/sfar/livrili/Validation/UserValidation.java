package com.sfar.livrili.Validation;

import java.util.regex.Pattern;

public class UserValidation {

   public static String emailNotMatch = "Please enter a valid email address ";

    public static boolean isEmailValid(String email) {
        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);
        // Check if email matches the pattern
        return email != null && p.matcher(email).matches();
    }


}
