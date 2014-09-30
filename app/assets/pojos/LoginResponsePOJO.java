package assets.pojos;

import assets.enums.UserType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by PENTAGON on 09.08.14.
 */
public class LoginResponsePOJO {
    public String email;
    public String password;
    public boolean loginStatus=true;
    public boolean hasProblems=false;

    public void validate(LoginPOJO loginPOJO) {
        validateEmail(loginPOJO);
        if (StringUtils.isBlank(loginPOJO.password) || loginPOJO.password.length() < 5){
            password = "e1";
            hasProblems=true;
        }
    }

    private void validateEmail(LoginPOJO loginPOJO) {
        String emailValue = loginPOJO.email;
        if (StringUtils.isBlank(emailValue)){
            email = "e1";
            hasProblems=true;
            return;
        }
        int atpos = emailValue.indexOf("@");
        int dotpos = emailValue.lastIndexOf(".");
        if (atpos < 1 || dotpos < atpos + 2 || dotpos + 2 >= emailValue.length()) {
            email = "e2";
            hasProblems=true;
        }
    }
}
