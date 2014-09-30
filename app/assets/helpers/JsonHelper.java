package assets.helpers;

import assets.enums.CategoryType;
import assets.enums.RentKind;
import assets.pojos.AdvPreview;
import assets.pojos.ShowPOJO;
import assets.pojos.ShowPOJOField;
import com.fasterxml.jackson.databind.JsonNode;
import models.Advertisement;
import models.Flat;
import models.Link;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import play.data.Form;
import play.i18n.Messages;
import play.libs.Json;
import play.mvc.Http;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by PENTAGON on 02.09.14.
 */
public class JsonHelper {
    public static List<Advertisement> fromJsonString(List<String> jsonStrings) {
        List<Advertisement> advertisements = new ArrayList<>(jsonStrings.size());
        for (String jsonString : jsonStrings) {
            JsonNode jsonNode = Json.parse(jsonString);
            CategoryType categoryType = CategoryType.valueOf(jsonNode.get("categoryType").textValue());
            switch (categoryType) {
                case APART:
                    advertisements.add(Json.fromJson(jsonNode, Flat.class));
                    break;
            }
        }
        return advertisements;
    }

    public static List<AdvPreview> previewFromJsonString(List<String> jsonStrings) {
        List<AdvPreview> previews = new ArrayList<>(jsonStrings.size());
        for (String jsonString : jsonStrings) {
            previews.add(Json.fromJson(Json.parse(jsonString), AdvPreview.class));
        }
        return previews;
    }

    public static AdvPreview toPreview(Advertisement advertisement) {
        AdvPreview preview = new AdvPreview();
        preview.id = advertisement.getId();
        preview.latitude = advertisement.getLatitude();
        preview.longitude = advertisement.getLongitude();
        preview.price = advertisement.getPrice();
        preview.rentPrice = advertisement.getRentPrice();
        preview.rentKind = advertisement.getRentKind();
        preview.title = advertisement.getTitle();
        if (advertisement.getImageLinks() != null && advertisement.getImageLinks().size() > 0)
            preview.titleLink = advertisement.getImageLinks().get(0);
        return preview;
    }

    public static Advertisement bindFromRequest() {
        Http.Request request = play.mvc.Controller.request();
        CategoryType categoryType = CategoryType.valueOf(request.getQueryString("categoryType"));
        Advertisement advertisement = null;
        switch (categoryType) {
            case APART:
                advertisement = Form.form(Flat.class).bindFromRequest().get();
                break;
        }
        Map<String, String[]> queryString = request.queryString();

        String[] imageLinkIds = queryString.get("imageLinkIds[]");
        String[] imageDescriptions = queryString.get("imageDescriptions[]");
        if (imageLinkIds != null) {
            List<Link> imageLinks = new ArrayList<>(imageLinkIds.length);
            for (int i = 0; i < imageLinkIds.length; i++)
                imageLinks.add(new Link(imageLinkIds[i], imageDescriptions[i]));
            advertisement.setImageLinks(imageLinks);
        }

        String[] panoLinkIds = queryString.get("panoLinkIds[]");
        String[] panoDescriptions = queryString.get("panoDescriptions[]");
        if (panoLinkIds != null) {
            List<Link> panoLinks = new ArrayList<>(panoLinkIds.length);
            for (int i = 0; i < panoLinkIds.length; i++)
                panoLinks.add(new Link(panoLinkIds[i], panoDescriptions[i]));
            advertisement.setPanoLinks(panoLinks);
        }

        String[] videoLinkIds = queryString.get("videoLinkIds[]");
        String[] videoDescriptions = queryString.get("videoDescriptions[]");
        if (videoLinkIds != null) {
            List<Link> videoLinks = new ArrayList<>(videoLinkIds.length);
            for (int i = 0; i < videoLinkIds.length; i++)
                videoLinks.add(new Link(videoLinkIds[i], videoDescriptions[i]));
            advertisement.setVideoLinks(videoLinks);
        }

        advertisement.setId(Long.valueOf(queryString.get("advId")[0]));
        return advertisement;
    }

    public static String generateTitle(Advertisement advertisement) {
        String title = null;
        if (advertisement instanceof Flat) {
            Flat flat = (Flat) advertisement;
            title = (flat.getRoomNumber() > 0 ?
                    flat.getRoomNumber() + "-к квартира, " :
                    "Студия, ") + flat.getOverallSq() + " м², " + flat.getFloor() + "/" + flat.getFloorNumber() + " эт.";
        }
        return title;
    }

    public static ShowPOJO generateShowPOJO(Advertisement advertisement) {
        ShowPOJO showPOJO = new ShowPOJO();
        showPOJO.id = advertisement.getId();
        showPOJO.ownerId = advertisement.getOwnerId();
        showPOJO.address = new ShowPOJOField(Messages.get("Address"), advertisement.getAddress());
        if (advertisement.getRentKind() != null) {
            if (advertisement.getRentKind() == RentKind.SHORT) {
                showPOJO.price = new ShowPOJOField(Messages.get("RentPrice"), advertisement.getRentPrice() + " " +
                        Messages.get("ForADay") + " " + Messages.get("Ruble"));
            } else {
                showPOJO.price = new ShowPOJOField(Messages.get("RentPrice"), advertisement.getRentPrice() + " " +
                        Messages.get("ForAMonth") + " " + Messages.get("Ruble"));
            }
        } else {
            showPOJO.price = new ShowPOJOField(Messages.get("Price"), advertisement.getPrice() + " " + Messages.get("Ruble"));
        }
        showPOJO.title = advertisement.getTitle();
        setCommonFields(showPOJO, advertisement);
        setCommunicationFields(showPOJO, advertisement);
        setFinancialFields(showPOJO, advertisement);

        showPOJO.latitude = advertisement.getLatitude();
        showPOJO.longitude = advertisement.getLongitude();
        showPOJO.imageLinks = advertisement.getImageLinks()!=null?advertisement.getImageLinks(): Collections.<Link>emptyList();
        showPOJO.panoLinks = advertisement.getPanoLinks()!=null?advertisement.getPanoLinks(): Collections.<Link>emptyList();
        showPOJO.videoLinks = advertisement.getVideoLinks()!=null?advertisement.getVideoLinks(): Collections.<Link>emptyList();

        return showPOJO;
    }

    private static void setCommonFields(ShowPOJO showPOJO, Advertisement advertisement) {
        showPOJO.commonFields = new ArrayList<>();
        if (advertisement instanceof Flat) {
            Flat flat = (Flat) advertisement;
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("FlatPropertyKind"), flat.getFlatPropertyKind().local()));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("RoomNumber"),
                    flat.getRoomNumber() != 0 ? flat.getRoomNumber() + StringUtils.EMPTY : Messages.get("Studio")));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("Floor"), flat.getFloor() + "/" + flat.getFloorNumber()));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("MaterialType"), flat.getMaterialType().local()));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("OverallSq"), flat.getOverallSq() + " " + Messages.get("meter")));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("LivingSq"), flat.getLivingSq() + " " + Messages.get("meter")));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("RenovationType"), flat.getRenovationType().local()));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("SecurityType"), flat.getSecurityType().local()));
            showPOJO.commonFields.add(new ShowPOJOField(Messages.get("Balcony"), "#" + BooleanUtils.isTrue(flat.getBalcony())));
        }
    }

    private static void setCommunicationFields(ShowPOJO showPOJO, Advertisement advertisement) {
        showPOJO.communicationFields = new ArrayList<>();
        if (advertisement instanceof Flat) {
            Flat flat = (Flat) advertisement;
            showPOJO.communicationFields.add(new ShowPOJOField(Messages.get("Internet"), "#" + BooleanUtils.isTrue(flat.getInternet())));
        }
    }

    private static void setFinancialFields(ShowPOJO showPOJO, Advertisement advertisement) {
        showPOJO.financialFields = new ArrayList<>();
        if (advertisement instanceof Flat) {
            Flat flat = (Flat) advertisement;
            showPOJO.financialFields.add(new ShowPOJOField(Messages.get("OwnTime"), "#" + BooleanUtils.isTrue(flat.getOwnTime())));
            showPOJO.financialFields.add(new ShowPOJOField(Messages.get("Credit"), "#" + BooleanUtils.isTrue(flat.getCredit())));
            if (flat.getRentKind() != null)
                showPOJO.financialFields.add(new ShowPOJOField(Messages.get("Deposit"), (flat.getDeposit() != null ? flat.getDeposit() : 0) + " " + Messages.get("Ruble")));
        }
    }
}
