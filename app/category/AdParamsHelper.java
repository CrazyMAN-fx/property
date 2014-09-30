package category;

import assets.enums.ActionType;
import assets.enums.CategoryType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;

/**
 * Created by PENTAGON on 23.08.14.
 */
public class AdParamsHelper {

    public static final String filterParams;

    static {
        ObjectNode root = Json.newObject();
        ArrayNode params = root.putArray("filterParams");

        ObjectNode flatSell = Json.newObject();
        params.add(flatSell);
        ArrayNode flatSellC = flatSell.putArray("conditions");
        flatSellC.add(createCondition("actionType", ActionType.SELL.getValue()));
        addAvailableFields(flatSell, new String[]{"actionType", "categoryType"});

        ObjectNode flatRent = Json.newObject();
        params.add(flatRent);
        ArrayNode flatRentC = flatRent.putArray("conditions");
        flatRentC.add(createCondition("actionType", ActionType.RENT.getValue()));
        addAvailableFields(flatRent, new String[]{"actionType", "categoryType"});

        ObjectNode apartSell = Json.newObject();
        params.add(apartSell);
        ArrayNode apartSellC = apartSell.putArray("conditions");
        apartSellC.add(createCondition("actionType", ActionType.SELL.getValue()));
        apartSellC.add(createCondition("categoryType", CategoryType.APART.getValue()));
        addAvailableFields(apartSell, new String[]{"actionType", "categoryType", "flatPropertyKind", "roomNumber", "floor", "floorNumber", "floorType",
                "materialType", "overallSq", "overallSqMin", "overallSqMax", "livingSq","livingSqMin", "livingSqMax", "renovationType", "securityType",
                "balcony", "address", "latitude", "longitude", "ownTime", "credit", "price", "priceMin", "priceMax"});

        ObjectNode apartRent = Json.newObject();
        params.add(apartRent);
        ArrayNode apartRentC = apartRent.putArray("conditions");
        apartRentC.add(createCondition("actionType", ActionType.RENT.getValue()));
        apartRentC.add(createCondition("categoryType", CategoryType.APART.getValue()));
        addAvailableFields(apartRent, new String[]{"actionType", "categoryType", "roomNumber", "floor", "floorNumber", "floorType",
                "materialType", "overallSq", "overallSqMin", "overallSqMax", "livingSq", "livingSqMin", "livingSqMax", "renovationType",
                "securityType", "balcony", "address", "latitude", "longitude", "internet", "rentKind", "rentPrice", "rentPriceMax",
                "deposit", "depositMax"});

        filterParams = Json.stringify(root);
    }

    private static ObjectNode createCondition(String fieldName, String fieldValue) {
        ObjectNode condition = Json.newObject();
        condition.put("name", fieldName);
        condition.put("value", fieldValue);
        return condition;
    }

    private static void addAvailableFields(ObjectNode node, String[] fieldNames) {
        ArrayNode arrayNode = node.putArray("availableFields");
        for (String s : fieldNames)
            arrayNode.add(s);
    }
}
