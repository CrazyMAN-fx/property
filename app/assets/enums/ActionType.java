package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 29.07.14. 123
 */
public enum ActionType {
    SELL("SELL"),
    RENT("RENT"),
    DDD("DDD");


    private String value;

    private ActionType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String local() {
        return Messages.get(getClass().getSimpleName() +"."+value);
    }
}
