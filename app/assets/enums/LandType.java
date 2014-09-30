package assets.enums;

/**
 * Created by PENTAGON on 30.08.14.
 */
public enum LandType {
    LIVING("LIVING"),
    AGRIC("AGRIC"),
    INDUST("INDUST");

    private String value;

    private LandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}