package assets.pojos;

/**
 * Created by PENTAGON on 02.09.14.
 */
public class FavouritePOJO {
    public FavouritePOJO() {
    }

    public FavouritePOJO(String advId, String comment, AdvPreview advPreview) {
        this.advId = advId;
        this.comment = comment;
        this.advPreview = advPreview;
    }

    public String advId;
    public String comment;
    public AdvPreview advPreview;
}
