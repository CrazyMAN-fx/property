package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 29.07.14.
 */
public enum RentKind {
    LONG("SELL"),
    SHORT("RENT");


    private String value;

    private RentKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String local() {
        return Messages.get(getClass().getSimpleName() +"."+value);
    }
}
