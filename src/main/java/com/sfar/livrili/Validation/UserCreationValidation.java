package com.sfar.livrili.Validation;

import com.sfar.livrili.Domains.Entities.Gender;
import com.sfar.livrili.Domains.Entities.Role;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component

public class UserCreationValidation {




   public static String emailNotMatch = "Please enter a valid email address ";



    public static boolean isEmailValid(String email) {
        // Regular expression to match valid email formats
        String emailRegex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        // Compile the regex
        Pattern p = Pattern.compile(emailRegex);
        // Check if email matches the pattern
        return  p.matcher(email).matches();
    }
    public static boolean notEmpty(String arg) {
        return arg != null && !arg.isEmpty();
    }

    public static boolean passwordRespect(String password) {
         return password.length() >= 6;
    }

    public static boolean passwordMatch(String password, String confirmPassword) {
        return confirmPassword.equals(password);
    }
    public static boolean genderValid(Gender gender) {
        return gender != null && (gender.equals(Gender.MALE) || gender.equals(Gender.FEMALE));
    }
    public static boolean roleValidation(Role role) {
        return role != null && (role.equals(Role.CLIENT) || role.equals(Role.DELIVERY_PERSON));
    }
    public static boolean validatePhone(String phone) {
        return phone.length() == 8;
    }
    public static boolean validateNameFields(String name) {
        return  name.length() >= 2;
    }


}
