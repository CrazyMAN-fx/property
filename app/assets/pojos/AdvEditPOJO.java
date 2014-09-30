package assets.pojos;

import assets.enums.*;

import java.util.List;

/**
 * Created by PENTAGON on 12.09.14.
 */
public class AdvEditPOJO {
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
    private String flatPropertyKind;
    private Integer roomNumber;
    private Integer floor;
    private Integer floorNumber;
    private String materialType;
    private Integer overallSq;
    private Integer livingSq;
    private String renovationType;
    private String securityType;
    private Boolean balcony;
    private Boolean internet;
    private Boolean ownTime;

    private Boolean extraMediaPackage;
    private Boolean firstInSearchPackage;

    public List<String> imageLinks;
    public List<String> imageDescriptions;
    public List<String> panoLinks;
    public List<String> panoDescriptions;
    public List<String> videoLinks;
    public List<String> videoDescriptions;

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

    public FlatPropertyKind getFlatPropertyKind() {
        return flatPropertyKind != null ? FlatPropertyKind.valueOf(flatPropertyKind) : null;
    }

    public void setFlatPropertyKind(FlatPropertyKind flatPropertyKind) {
        this.flatPropertyKind = flatPropertyKind != null ? flatPropertyKind.getValue() : null;
    }

    public MaterialType getMaterialType() {
        return materialType != null ? MaterialType.valueOf(materialType) : null;
    }

    public void setMaterialType(MaterialType materialType) {
        this.materialType = materialType != null ? materialType.getValue() : null;
    }

    public RenovationType getRenovationType() {
        return renovationType != null ? RenovationType.valueOf(renovationType) : null;
    }

    public void setRenovationType(RenovationType renovationType) {
        this.renovationType = renovationType != null ? renovationType.getValue() : null;
    }

    public SecurityType getSecurityType() {
        return securityType != null ? SecurityType.valueOf(securityType) : null;
    }

    public void setSecurityType(SecurityType securityType) {
        this.securityType = securityType != null ? securityType.getValue() : null;
    }

    public Integer getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(Integer roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public Integer getFloorNumber() {
        return floorNumber;
    }

    public void setFloorNumber(Integer floorNumber) {
        this.floorNumber = floorNumber;
    }

    public Integer getOverallSq() {
        return overallSq;
    }

    public void setOverallSq(Integer overallSq) {
        this.overallSq = overallSq;
    }

    public Integer getLivingSq() {
        return livingSq;
    }

    public void setLivingSq(Integer livingSq) {
        this.livingSq = livingSq;
    }

    public Boolean getBalcony() {
        return balcony;
    }

    public void setBalcony(Boolean balcony) {
        this.balcony = balcony;
    }

    public Boolean getInternet() {
        return internet;
    }

    public void setInternet(Boolean internet) {
        this.internet = internet;
    }

    public Boolean getOwnTime() {
        return ownTime;
    }

    public void setOwnTime(Boolean ownTime) {
        this.ownTime = ownTime;
    }

    public Boolean getExtraMediaPackage() {
        return extraMediaPackage;
    }

    public void setExtraMediaPackage(Boolean extraMediaPackage) {
        this.extraMediaPackage = extraMediaPackage;
    }

    public Boolean getFirstInSearchPackage() {
        return firstInSearchPackage;
    }

    public void setFirstInSearchPackage(Boolean firstInSearchPackage) {
        this.firstInSearchPackage = firstInSearchPackage;
    }
}
