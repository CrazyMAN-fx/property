package assets.pojos;

import assets.enums.UserType;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by PENTAGON on 09.08.14.
 */
public class RegistrationResponsePOJO {
    public String userType;
    public String privateName;
    public String companyName;
    public String contactPerson;
    public String email;
    public String phoneNumber;
    public String password;
    public String passwordCopy;
    public String recaptcha_response_field;
    public boolean hasProblems=false;

    public void validate(RegistrationPOJO registrationPOJO) {
        if (registrationPOJO.getUserType() == null) {
            userType = "e1";
            hasProblems=true;
            return;
        }
        if (registrationPOJO.getUserType() == UserType.PRIVATE) {
            if (StringUtils.isBlank(registrationPOJO.privateName)){
                privateName = "e1";
                hasProblems=true;
            }
        } else {
            if (StringUtils.isBlank(registrationPOJO.companyName)){
                companyName = "e1";
                hasProblems=true;
            }
            if (StringUtils.isBlank(registrationPOJO.contactPerson)){
                contactPerson = "e1";
                hasProblems=true;
            }
        }
        validateEmail(registrationPOJO);
        if (StringUtils.isBlank(registrationPOJO.phoneNumber)){
            phoneNumber = "e1";
            hasProblems=true;
        }
        if (StringUtils.isBlank(registrationPOJO.password) || registrationPOJO.password.length() < 5){
            password = "e1";
            hasProblems=true;
        }
        else if (!registrationPOJO.password.equals(registrationPOJO.passwordCopy)){
            hasProblems=true;
            passwordCopy = "e1";
        }
    }

    private void validateEmail(RegistrationPOJO registrationPOJO) {
        String emailValue = registrationPOJO.email;
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
