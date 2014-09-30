package assets.pojos;

/**
 * Created by PENTAGON on 09.08.14.
 */
public class LoginPOJO {
    public String email;
    public String password;
    public Boolean tempLogin;

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
