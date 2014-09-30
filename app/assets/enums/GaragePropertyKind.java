package assets.enums;

/**
 * Created by PENTAGON on 30.08.14.
 */
public enum GaragePropertyKind {
    GARAGE("GARAGE"),
    PLACE("PLACE");

    private String value;

    private GaragePropertyKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}