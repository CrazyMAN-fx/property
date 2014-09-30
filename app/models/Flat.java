package models;

import assets.enums.FlatPropertyKind;
import assets.enums.MaterialType;
import assets.enums.RenovationType;
import assets.enums.SecurityType;

import javax.persistence.Entity;

/**
 * Created by PENTAGON on 30.07.14.
 */
@Entity(name = "Flat")
public class Flat extends Advertisement {
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
}
