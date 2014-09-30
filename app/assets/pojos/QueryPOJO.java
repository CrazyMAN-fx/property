package assets.pojos;

import assets.enums.*;
import com.sun.org.apache.xpath.internal.operations.Bool;
import models.Flat;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PENTAGON on 29.07.14.
 */
public class QueryPOJO {
    public ActionType actionType;
    public CategoryType categoryType;
    public FlatPropertyKind flatPropertyKind;
    public HousePropertyKind housePropertyKind;
    public GaragePropertyKind garagePropertyKind;
    public CommPropertyKind commPropertyKind;
    public CommClass commClass;
    public Integer roomNumber;
    public FloorType floorType;
    public MaterialType materialType;
    public Integer overallSqMin;
    public Integer overallSqMax;
    public Integer landSqMin;
    public Integer landSqMax;
    public Integer livingSqMin;
    public Integer livingSqMax;
    public RenovationType renovationType;
    public SecurityType securityType;
    public Integer distFromCityMax;
    public LandType landType;
    public Integer priceMin;
    public Integer priceMax;
    public RentKind rentKind;
    public Integer rentPriceMax;
    public Integer depositMax;
    public Boolean garageExs;
    public Boolean balcony;
    public Boolean gas;
    public Boolean electricity;
    public Boolean heating;
    public Boolean water;
    public Boolean sewerage;
    public Boolean internet;
    public Boolean ownTime;
    public Boolean credit;
    public Double cLatitude;
    public Double cLongitude;
    public Double radius;

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : this.getClass().getFields()) {
                Object value = field.get(this);
                if (value != null) map.put(field.getName(), value);
            }
        } catch (IllegalAccessException e) {
        }
        return map;
    }
}
