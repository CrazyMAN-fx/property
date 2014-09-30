package assets.enums;

/**
 * Created by PENTAGON on 30.08.14.
 */
public enum CommClass {
    A("A"),
    B("B"),
    C("C"),
    D("D");

    private String value;

    private CommClass(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}