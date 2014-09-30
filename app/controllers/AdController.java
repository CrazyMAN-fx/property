package controllers;

import assets.helpers.JsonHelper;
import assets.pojos.AdvPreview;
import assets.pojos.ShowPOJO;
import com.typesafe.plugin.RedisPlugin;
import models.Advertisement;
import play.Logger;
import play.Play;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import redis.clients.jedis.Jedis;
import views.html.show_property;

import java.util.*;

/**
 * Created by PENTAGON on 25.08.14.
 */
public class AdController extends Controller {
    //Seq for id generation
    private static final String AD_ID_S = "ad_id";
    //List of user active ad ids
    private static final String USER_AD_ACTIVE_IDS = "user_ad_active_id:";
    //List of user not active ad ids
    private static final String USER_AD_NOT_ACTIVE_IDS = "user_ad_not_active_id:";
    //List of user blocked ad ids
    private static final String USER_AD_BLOCKED_IDS = "user_ad_blocked_id:";
    //List of ad ids
    private static final String AD_IDS = "ad_ids";
    //Ad body
    public static final String AD = "ad:";
    //Ad preview
    public static final String AD_PREVIEW = "ad_preview:";
    //List of ad active images
    public static final String AD_IMAGE_ACTIVE = "ad_image_active:";
    //List of ad not active images
    private static final String AD_IMAGE_NOT_ACTIVE = "ad_image_not_active:";
    //List of ad active pano
    private static final String AD_PANO_ACTIVE = "ad_pano_active:";
    //List of ad not active pano
    private static final String AD_PANO_NOT_ACTIVE = "ad_pano_not_active:";
    //List of ad active video
    private static final String AD_VIDEO_ACTIVE = "ad_video_active:";
    //List of ad not active video
    private static final String AD_VIDEO_NOT_ACTIVE = "ad_video_not_active:";
    //List of id of ads to create
    public static final String AD_TO_CREATE = "ad_to_create";

    public static Result editAdv() {
        return ok("");
    }

    public static Result addNewAdv() {
        String userId = UserController.getUserId();
        if (userId == null) return unauthorized();

        Advertisement advertisement = JsonHelper.bindFromRequest();
        if (advertisement.getImageLinks()==null || advertisement.getImageLinks().size()==0)
            return badRequest();
        advertisement.setOwnerId(userId);
        advertisement.setTitle(JsonHelper.generateTitle(advertisement));
        AdvPreview preview = JsonHelper.toPreview(advertisement);

        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        try {
            if (advertisement.getImageLinks() != null) {
                String[] linkJsons = new String[advertisement.getImageLinks().size()];
                String[] linkIds = new String[advertisement.getImageLinks().size()];
                for (int i = 0; i < linkJsons.length; i++) {
                    linkJsons[i] = Json.stringify(Json.toJson(advertisement.getImageLinks().get(i)));
                    linkIds[i] = advertisement.getImageLinks().get(i).linkId;
                }
                jedis.sadd(AD_IMAGE_ACTIVE + advertisement.getId(), linkJsons);
                jedis.srem(FileController.TEMP_IMAGES, linkIds);
            }
            if (advertisement.getPanoLinks() != null) {
                String[] linkJsons = new String[advertisement.getPanoLinks().size()];
                String[] linkIds = new String[advertisement.getPanoLinks().size()];
                for (int i = 0; i < linkJsons.length; i++) {
                    linkJsons[i] = Json.stringify(Json.toJson(advertisement.getPanoLinks().get(i)));
                    linkIds[i] = advertisement.getPanoLinks().get(i).linkId;
                }
                jedis.sadd(AD_PANO_ACTIVE + advertisement.getId(), linkJsons);
                jedis.srem(FileController.TEMP_IMAGES, linkIds);
            }
            if (advertisement.getVideoLinks() != null) {
                String[] linkJsons = new String[advertisement.getVideoLinks().size()];
                for (int i = 0; i < linkJsons.length; i++) {
                    linkJsons[i] = Json.stringify(Json.toJson(advertisement.getVideoLinks().get(i)));
                }
                jedis.sadd(AD_VIDEO_ACTIVE + advertisement.getId(), linkJsons);
            }
            jedis.set(AD + advertisement.getId(), Json.stringify(Json.toJson(advertisement)));
            jedis.set(AD_PREVIEW + advertisement.getId(), Json.stringify(Json.toJson(preview)));
            jedis.sadd(AD_IDS, advertisement.getId().toString());
            jedis.sadd(USER_AD_ACTIVE_IDS, advertisement.getId().toString());
            jedis.lpush(AD_TO_CREATE, advertisement.getId().toString());
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return ok();
    }

    public static Result show() {
        Object advId = request().getQueryString("id");
        if (advId == null) return notFound();

        ShowPOJO showPOJO;
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        try {
            Advertisement advertisement = JsonHelper.fromJsonString(getAdJsonStringList(Collections.singletonList(advId))).get(0);
            showPOJO = JsonHelper.generateShowPOJO(advertisement);
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return ok(show_property.render(Json.stringify(Json.toJson(showPOJO))));
    }

    //Get list of stringified adv preview
    public static List<String> getAdPreviewJsonStringList(Collection<Object> ids) {
        String[] keys = new String[ids.size()];
        int counter = 0;
        for (Object id : ids) {
            keys[counter] = AD_PREVIEW + id;
            counter++;
        }
        List<String> adPreviewJsonStringList = new ArrayList<>();
        if (ids.size() > 0) {
            RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
            Jedis jedis = redisPlugin.jedisPool().getResource();
            try {
                adPreviewJsonStringList = jedis.mget(keys);
            } finally {
                redisPlugin.jedisPool().returnResource(jedis);
            }
        }

        Iterator iterator = adPreviewJsonStringList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null)
                iterator.remove();
        }
        return adPreviewJsonStringList;
    }

    //Get list of stringified adv
    public static List<String> getAdJsonStringList(Collection<Object> ids) {
        String[] keys = new String[ids.size()];
        int counter = 0;
        for (Object id : ids) {
            keys[counter] = AD + id;
            counter++;
        }
        List<String> adPreviewJsonStringList = new ArrayList<>();
        if (ids.size() > 0) {
            RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
            Jedis jedis = redisPlugin.jedisPool().getResource();
            try {
                adPreviewJsonStringList = jedis.mget(keys);
            } finally {
                redisPlugin.jedisPool().returnResource(jedis);
            }
        }

        Iterator iterator = adPreviewJsonStringList.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null)
                iterator.remove();
        }
        return adPreviewJsonStringList;
    }

    public static Long generateAdId() {
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        Long id;
        try {
            id = jedis.incr(AD_ID_S);
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return id;
    }
}
