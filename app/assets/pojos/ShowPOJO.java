package assets.pojos;

import models.Link;

import java.util.List;

/**
 * Created by PENTAGON on 08.09.14.
 */
public class ShowPOJO {
    public Long id;
    public String ownerId;
    public String title;
    public ShowPOJOField price;
    public ShowPOJOField address;
    public List<ShowPOJOField> commonFields;
    public List<ShowPOJOField> communicationFields;
    public List<ShowPOJOField> financialFields;
    public Double latitude;
    public Double longitude;
    public List<Link> imageLinks;
    public List<Link> panoLinks;
    public List<Link> videoLinks;
}
