package controllers;

import assets.helpers.JsonHelper;
import assets.pojos.AdvPreview;
import assets.pojos.QueryPOJO;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dao.Dao;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by PENTAGON on 25.08.14.
 */
public class SearchController extends Controller {
    //List of active saved search body
    private static final String SEARCH_SAVED_ACTIVE = "search_saved_active";
    //List of not active saved search body
    private static final String SEARCH_SAVED_NOT_ACTIVE = "search_saved_not_active";
    //List of user active saved search ids
    private static final String USER_SEARCH_SAVED_ACTIVE_IDS = "user_search_saved_active_ids:";
    //List of user not active saved search ids
    private static final String USER_SEARCH_SAVED_NOT_ACTIVE_IDS = "user_search_saved_not_active_ids:";

    public static Result searchResult() {
        QueryPOJO queryPOJO = Form.form(QueryPOJO.class).bindFromRequest().get();
        List<Object> ids = Dao.getFlatIds(queryPOJO);
        List<AdvPreview> previews = JsonHelper.previewFromJsonString(AdController.getAdPreviewJsonStringList(ids));
        ObjectNode root = Json.newObject();
        ArrayNode data = root.putArray("data");
        for (AdvPreview advPreview : previews) {
            data.add(Json.toJson(advPreview));
        }
        return ok(root);
    }
}
