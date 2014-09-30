package controllers;

import assets.helpers.JsonHelper;
import assets.pojos.*;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.plugin.RedisPlugin;
import models.Advertisement;
import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import play.Logger;
import play.Play;
import play.data.Form;
import play.libs.F;
import play.libs.Json;
import play.libs.ws.WS;
import play.libs.ws.WSRequestHolder;
import play.libs.ws.WSResponse;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import redis.clients.jedis.Jedis;

import java.util.*;

/**
 * Created by PENTAGON on 19.08.14.
 */
public class UserController extends Controller {
    //Seq for id generation
    private static final String USER_ID_S = "user_id";
    //User body
    private static final String USER_TABLE_PREFIX = "user:";
    //List of user login to user id
    private static final String USER_LOGIN_TO_ID_TABLE = "login_to_id";
    //List of auth keys (persistence session)
    private static final String USER_AUTH_TABLE = "auths";
    //List of user ids
    private static final String USER_LIST_TABLE = "users";
    //List of user favourite ads body
    private static final String AD_FAVOURITE_LIST = "ad_fav:";

    public static F.Promise<Result> register() {
        final RegistrationPOJO registrationPOJO = Form.form(RegistrationPOJO.class).bindFromRequest().get();
        String url = RecaptchaHelper.constructUrl(request().remoteAddress(), registrationPOJO.recaptcha_challenge_field,
                StringUtils.deleteWhitespace(registrationPOJO.recaptcha_response_field));
        WSRequestHolder holder = WS.url(url).setTimeout(RecaptchaHelper.VERIFY_TIMEOUT);
        F.Promise<Result> responseResult = holder.post(StringUtils.EMPTY).map(
                new F.Function<WSResponse, Result>() {
                    public Result apply(WSResponse response) {
                        RegistrationResponsePOJO problemsPOJO = new RegistrationResponsePOJO();
                        String[] responseParams = response.getBody().split("\n");
                        boolean captchaStatus = Boolean.TRUE.toString().equals(responseParams[0]);
                        if (!captchaStatus) {
                            problemsPOJO.recaptcha_response_field = "e2";
                        }
                        problemsPOJO.validate(registrationPOJO);

                        if (!problemsPOJO.hasProblems) {
                            String id = generateUserId();
                            RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
                            Jedis jedis = redisPlugin.jedisPool().getResource();
                            try {
                                registrationPOJO.password = BCrypt.hashpw(registrationPOJO.password, BCrypt.gensalt());
                                jedis.hmset(USER_TABLE_PREFIX + id, POJOHelper.toStringMap(registrationPOJO));
                                jedis.hmset(USER_LOGIN_TO_ID_TABLE, Collections.singletonMap(registrationPOJO.email, id));
                                jedis.sadd(USER_LIST_TABLE, id);

                                String randomString = UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY);
                                String unencryptedToken = registrationPOJO.email + "_" + randomString;
                                String encryptedToken = BCrypt.hashpw(unencryptedToken, BCrypt.gensalt());
                                jedis.hmset(USER_AUTH_TABLE, Collections.singletonMap(encryptedToken, id));
                                response().setCookie("auth", encryptedToken);
                                response().setCookie("userId", id);
                            } finally {
                                redisPlugin.jedisPool().returnResource(jedis);
                            }
                        }
                        return ok(Json.toJson(problemsPOJO));
                    }
                }
        );
        return responseResult;
    }

    public static Result login() {
        LoginPOJO loginPOJO = Form.form(LoginPOJO.class).bindFromRequest().get();
        LoginResponsePOJO loginResponsePOJO = new LoginResponsePOJO();
        loginResponsePOJO.validate(loginPOJO);
        if (!loginResponsePOJO.hasProblems) {
            RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
            Jedis jedis = redisPlugin.jedisPool().getResource();
            try {
                String id = jedis.hmget(USER_LOGIN_TO_ID_TABLE, loginPOJO.email).get(0);
                if (id != null) {
                    String passwordHash = jedis.hmget(USER_TABLE_PREFIX + id, "password").get(0);
                    if (BCrypt.checkpw(loginPOJO.password, passwordHash)) {
                        String randomString = UUID.randomUUID().toString().replaceAll("-", StringUtils.EMPTY);
                        String unencryptedToken = loginPOJO.email + "_" + randomString;
                        String encryptedToken = BCrypt.hashpw(unencryptedToken, BCrypt.gensalt());
                        jedis.hmset(USER_AUTH_TABLE, Collections.singletonMap(encryptedToken, id));
                        response().setCookie("auth", encryptedToken);
                        response().setCookie("userId", id);
                    } else loginResponsePOJO.loginStatus = false;
                } else loginResponsePOJO.loginStatus = false;
            } finally {
                redisPlugin.jedisPool().returnResource(jedis);
            }
        }
        return ok(Json.toJson(loginResponsePOJO));
    }

    public static String getUserId() {
        Http.Cookie cookie = request().cookie("auth");
        String id = null;
        if (cookie != null && cookie.value() != null) {
            String encryptedToken = cookie.value();
            RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
            Jedis jedis = redisPlugin.jedisPool().getResource();
            try {
                id = jedis.hmget(USER_AUTH_TABLE, encryptedToken).get(0);
            } finally {
                redisPlugin.jedisPool().returnResource(jedis);
            }
        }
        return id;
    }

    public static Result checkInFav() {
        String advId = request().getQueryString("id");
        String userId = getUserId();
        if (userId == null) return unauthorized();
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        Boolean checkResult = false;
        try {
            checkResult = jedis.hexists(AD_FAVOURITE_LIST + userId, advId);
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return ok(checkResult.toString());
    }

    public static Result listInFav() {
        String userId = getUserId();
        if (userId == null) return unauthorized();
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        ObjectNode favListRoot = Json.newObject();
        ArrayNode favList = favListRoot.putArray("favList");
        try {
            Map<String, String> userFavMap = jedis.hgetAll(AD_FAVOURITE_LIST + userId);
            List<AdvPreview> previews = JsonHelper.previewFromJsonString(AdController.getAdPreviewJsonStringList(new ArrayList<Object>(userFavMap.keySet())));
            for (AdvPreview advPreview : previews) {
                favList.add(Json.toJson(new FavouritePOJO(advPreview.id.toString(), userFavMap.get(advPreview.id.toString()), advPreview)));
            }
            if (previews.size()!=userFavMap.size())
                Logger.info("User "+userId+" has deleted adv in fav");
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return ok(favListRoot);
    }

    public static Result addInFav() {
        String advId = request().getQueryString("id");
        String advComment = request().getQueryString("comment");
        String userId = getUserId();
        if (userId == null) return unauthorized();
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        try {
            jedis.hset(AD_FAVOURITE_LIST + userId, advId, advComment);
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return ok();
    }

    public static Result removeInFav() {
        String advId = request().getQueryString("id");
        String userId = getUserId();
        if (userId == null) return unauthorized();
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        try {
            jedis.hdel(AD_FAVOURITE_LIST + userId, advId);
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return ok();
    }

    public static boolean isLogin() {
        return getUserId() != null;
    }

    public static Result logout() {
        Http.Cookie cookie = request().cookie("auth");
        if (cookie != null && cookie.value() != null) {
            String encryptedToken = cookie.value();
            RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
            Jedis jedis = redisPlugin.jedisPool().getResource();
            try {
                jedis.hdel(USER_AUTH_TABLE, encryptedToken);
                response().discardCookie("auth");
                response().discardCookie("userId");
            } finally {
                redisPlugin.jedisPool().returnResource(jedis);
            }
        }
        return ok("");
    }

    public static String generateUserId() {
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        String id;
        try {
            id = StringUtils.leftPad(jedis.incr(USER_ID_S).toString(), 16, "0");
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return id;
    }
}
