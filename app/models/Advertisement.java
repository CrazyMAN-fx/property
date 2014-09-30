package models;

import assets.enums.ActionType;
import assets.enums.CategoryType;
import assets.enums.RentKind;
import org.codehaus.jackson.annotate.JsonIgnore;
import play.db.ebean.Model;

import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

/**
 * Created by PENTAGON on 02.09.14.
 */
@javax.persistence.MappedSuperclass
public class Advertisement extends Model {
    @Id
    private Long id;
    private String actionType;
    private String categoryType;
    private String rentKind;
    private Integer rentPrice;
    private Integer deposit;
    private Integer price;
    private Boolean credit;
    private String address;
    private Double latitude;
    private Double longitude;

    private String ownerId;
    private String title;

    private List<Link> imageLinks;
    private List<Link> panoLinks;
    private List<Link> videoLinks;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ActionType getActionType() {
        return actionType != null ? ActionType.valueOf(actionType) : null;
    }

    public void setActionType(ActionType actionType) {
        this.actionType = actionType != null ? actionType.getValue() : null;
    }

    public CategoryType getCategoryType() {
        return categoryType != null ? CategoryType.valueOf(categoryType) : null;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType != null ? categoryType.getValue() : null;
    }

    public RentKind getRentKind() {
        return rentKind != null ? RentKind.valueOf(rentKind) : null;
    }

    public void setRentKind(RentKind rentKind) {
        this.rentKind = rentKind != null ? rentKind.getValue() : null;
    }

    public Integer getRentPrice() {
        return rentPrice;
    }

    public void setRentPrice(Integer rentPrice) {
        this.rentPrice = rentPrice;
    }

    public Integer getDeposit() {
        return deposit;
    }

    public void setDeposit(Integer deposit) {
        this.deposit = deposit;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Transient
    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    @Transient
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public Boolean getCredit() {
        return credit;
    }

    public void setCredit(Boolean credit) {
        this.credit = credit;
    }

    @Transient
    public List<Link> getImageLinks() {
        return imageLinks;
    }

    public void setImageLinks(List<Link> imageLinks) {
        this.imageLinks = imageLinks;
    }

    @Transient
    public List<Link> getPanoLinks() {
        return panoLinks;
    }

    public void setPanoLinks(List<Link> panoLinks) {
        this.panoLinks = panoLinks;
    }

    @Transient
    public List<Link> getVideoLinks() {
        return videoLinks;
    }

    public void setVideoLinks(List<Link> videoLinks) {
        this.videoLinks = videoLinks;
    }
}
