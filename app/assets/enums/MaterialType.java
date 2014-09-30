package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 29.07.14.
 */
public enum MaterialType {
    MONO("MONO"),
    PANEL("PANEL"),
    BLOCK("BLOCK"),
    BRICK("BRICK");

    private String value;

    private MaterialType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String local() {
        return Messages.get(getClass().getSimpleName() + "." + value);
    }
}
