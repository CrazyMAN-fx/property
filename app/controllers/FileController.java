package controllers;

import com.amazonaws.services.s3.model.S3Object;
import com.typesafe.plugin.RedisPlugin;
import file_storage.ImageUploadResponse;
import file_storage.S3Plugin;
import org.apache.commons.lang3.StringUtils;
import play.Play;
import play.libs.F;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by PENTAGON on 19.08.14.
 */
public class FileController extends Controller {
    //Seq for id generation
    private static final String LINK_ID_S = "link_id";
    //
    public static final String TEMP_IMAGES = "temp_images";

    public static F.Promise<Result> uploadImage() {
        if (!UserController.isLogin())
            return F.Promise.promise(
                    new F.Function0<Result>() {
                        public Result apply() {
                            return unauthorized();
                        }
                    });

        F.Promise<Result> promise = F.Promise.promise(
                new F.Function0<Result>() {
                    public Result apply() {
                        List<Http.MultipartFormData.FilePart> fileParts = request().body().asMultipartFormData().getFiles();
                        String id = generateLinkId();
                        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
                        Jedis jedis = redisPlugin.jedisPool().getResource();
                        try {
                            jedis.randomKey();
                            jedis.sadd(TEMP_IMAGES, id);
                        } finally {
                            redisPlugin.jedisPool().returnResource(jedis);
                        }

                        S3Plugin.amazonS3.putObject(S3Plugin.imagesBucket, id + "thumbnail", fileParts.get(0).getFile());
                        S3Plugin.amazonS3.putObject(S3Plugin.imagesBucket, id, fileParts.get(1).getFile());
                        ImageUploadResponse uploadResponse = new ImageUploadResponse();
                        ImageUploadResponse.ImageFile imageFile = new ImageUploadResponse.ImageFile();
                        imageFile.name = fileParts.get(1).getFilename();
                        imageFile.id = id;
                        imageFile.thumbnailUrl = "/get_image/temp/?id=" + id + "thumbnail";
                        imageFile.deleteUrl = "/delete_image/temp/?id=" + id;
                        uploadResponse.files.add(imageFile);
                        return ok(Json.toJson(uploadResponse));
                    }
                }
        );

        return promise;
    }

    public static F.Promise<Result> deleteTempImage() {
        if (!UserController.isLogin())
            return F.Promise.promise(
                    new F.Function0<Result>() {
                        public Result apply() {
                            return unauthorized();
                        }
                    });

        F.Promise<Result> promise = F.Promise.promise(
                new F.Function0<Result>() {
                    public Result apply() {
                        String id = request().getQueryString("id");
                        S3Plugin.amazonS3.deleteObject(S3Plugin.imagesBucket, id + "thumbnail");
                        S3Plugin.amazonS3.deleteObject(S3Plugin.imagesBucket, id);
                        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
                        Jedis jedis = redisPlugin.jedisPool().getResource();
                        try {
                            jedis.srem(TEMP_IMAGES, id);
                        } finally {
                            redisPlugin.jedisPool().returnResource(jedis);
                        }
                        return ok("");
                    }
                });
        return promise;
    }

    public static F.Promise<Result> getTempImage() {
        if (!UserController.isLogin())
            return F.Promise.promise(
                    new F.Function0<Result>() {
                        public Result apply() {
                            return unauthorized();
                        }
                    });

        F.Promise<Result> promise = F.Promise.promise(
                new F.Function0<Result>() {
                    public Result apply() {
                        String id = request().getQueryString("id");
                        S3Object s3Object = S3Plugin.amazonS3.getObject(S3Plugin.imagesBucket, id);
                        return ok(s3Object.getObjectContent());
                    }
                });
        return promise;
    }

    public static F.Promise<Result> getImage() {
        F.Promise<Result> promise = F.Promise.promise(
                new F.Function0<Result>() {
                    public Result apply() {
                        String id = request().getQueryString("id");
                        S3Object s3Object = S3Plugin.amazonS3.getObject(S3Plugin.imagesBucket, id);
                        return ok(s3Object.getObjectContent());
                    }
                });
        return promise;
    }

    public static String generateLinkId() {
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        String id;
        try {
            id = StringUtils.leftPad(jedis.incr(LINK_ID_S).toString(), 16, "0");
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
        return id;
    }
}
