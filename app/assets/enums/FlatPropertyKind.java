package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 25.08.14.
 */
public enum FlatPropertyKind {
    NEW("NEW"),
    NOT_NEW("NOT_NEW");

    private String value;

    private FlatPropertyKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String local() {
        return Messages.get(getClass().getSimpleName() + "." + value);
    }
}
