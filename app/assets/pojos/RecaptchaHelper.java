package assets.pojos;

/**
 * Created by PENTAGON on 09.08.14.
 */
public class RecaptchaHelper {

    public static final String PUBLIC_KEY = "6Lda5PoSAAAAAPm9VY7p11mcOVPN0u76fh_8Dtc2";
    public static final String PRIVATE_KEY = "6Lda5PoSAAAAAINITVWkBtY0YkGnsPiSYX2zxSvl";

    public static final String PRIVATE_KEY_PARAM = "?privatekey=";
    public static final String REMOTE_IP_PARAM = "&remoteip=";
    public static final String CHALLENGE_PARAM = "&challenge=";
    public static final String RESPONSE_PARAM = "&response=";

    public static final String VERIFY_URL = "http://www.google.com/recaptcha/api/verify";

    public static final int VERIFY_TIMEOUT = 2000;

    public static String constructUrl(String remoteIP, String challenge, String response) {
        StringBuilder builder = new StringBuilder()
                .append(VERIFY_URL)
                .append(PRIVATE_KEY_PARAM).append(PRIVATE_KEY)
                .append(REMOTE_IP_PARAM).append(remoteIP)
                .append(CHALLENGE_PARAM).append(challenge)
                .append(RESPONSE_PARAM).append(response);
        return builder.toString();
    }
}
