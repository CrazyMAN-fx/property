package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 29.07.14.
 */
public enum CategoryType {
    APART("APART"),
    ROOM("ROOM"),
    HOUSE("HOUSE"),
    LAND("LAND"),
    GARAGE("GARAGE"),
    COMM("COMM");


    private String value;

    private CategoryType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String local() {
        return Messages.get(getClass().getSimpleName() +"."+value);
    }
}
