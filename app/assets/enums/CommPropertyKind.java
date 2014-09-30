package assets.enums;

/**
 * Created by PENTAGON on 30.08.14.
 */
public enum CommPropertyKind {
    HOTEL("HOTEL"),
    OFFICE("OFFICE"),
    FREE("FREE"),
    INDUST("INDUST"),
    WAREHOUSE("WAREHOUSE"),
    COMM("COMM");

    private String value;

    private CommPropertyKind(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}