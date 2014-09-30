package assets.enums;

/**
 * Created by PENTAGON on 29.07.14.
 */
public enum FloorType {
    NOT_FIRST("NOT_FIRST"),
    NOT_FIRST_NOT_LAST("NOT_FIRST_NOT_LAST"),
    NOT_LAST("NOT_LAST");

    private String value;

    private FloorType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
