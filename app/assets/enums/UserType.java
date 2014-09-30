package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 29.07.14.
 */
public enum UserType {
    PRIVATE("PRIVATE"),
    COMPANY("COMPANY");


    private String value;

    private UserType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String local() {
        return Messages.get(getClass().getSimpleName() +"."+value);
    }
}
