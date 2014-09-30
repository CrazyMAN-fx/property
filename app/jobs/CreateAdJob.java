package jobs;

import akka.actor.UntypedActor;
import com.fasterxml.jackson.databind.JsonNode;
import com.typesafe.plugin.RedisPlugin;
import controllers.AdController;
import dao.Dao;
import models.Flat;
import play.Logger;
import play.Play;
import play.libs.Json;
import redis.clients.jedis.Jedis;

/**
 * Created by PENTAGON on 26.08.14.
 */
public class CreateAdJob extends UntypedActor {
    @Override
    public void onReceive(Object message) throws Exception {
        RedisPlugin redisPlugin = Play.application().plugin(RedisPlugin.class);
        Jedis jedis = redisPlugin.jedisPool().getResource();
        try {
            int counter = 0;
            String adId;
            while ((adId = jedis.rpop(AdController.AD_TO_CREATE)) != null) {
                String adJson = jedis.get(AdController.AD + adId);
                JsonNode adNode = Json.parse(adJson);
                Flat flat = Json.fromJson(adNode, Flat.class);
                Dao.persist(flat);
                counter++;
            }
            if (counter > 0) Logger.info("Created " + counter + " new ads");
        } finally {
            redisPlugin.jedisPool().returnResource(jedis);
        }
    }
}
