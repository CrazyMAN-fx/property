package assets.pojos;

import assets.enums.UserType;

/**
 * Created by PENTAGON on 09.08.14.
 */
public class RegistrationPOJO {
    public UserType userType;
    public String privateName;
    public String companyName;
    public String contactPerson;
    public String email;
    public String phoneNumber;
    public String password;
    public String passwordCopy;
    public String recaptcha_challenge_field;
    public String recaptcha_response_field;

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
