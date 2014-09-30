package assets.enums;

import play.i18n.Messages;

/**
 * Created by PENTAGON on 30.08.14.
 */
public enum HousePropertyKind {
    HOUSE("HOUSE"),
    DACHA("DACHA"),
    COTTAGE("COTTAGE"),
    TOWNHOUSE("TOWNHOUSE");

    private String value;

    private HousePropertyKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}