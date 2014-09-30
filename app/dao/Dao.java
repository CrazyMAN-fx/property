package dao;

import assets.enums.CategoryType;
import assets.enums.FloorType;
import assets.pojos.QueryPOJO;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.ExpressionList;
import com.avaje.ebean.Query;
import com.typesafe.plugin.RedisPlugin;
import controllers.AdController;
import models.Flat;
import play.Play;
import play.db.ebean.Model;
import play.libs.Json;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by PENTAGON on 30.07.14.
 */
public class Dao {

    public static void persist(Model model) {
        Ebean.save(model);
    }

    public static void addWhereParams(Query query, QueryPOJO queryPOJO) {
        ExpressionList where = query.where();
        if (queryPOJO.categoryType == CategoryType.APART) {
            if (queryPOJO.flatPropertyKind != null)
                where.eq("flatPropertyKind", queryPOJO.flatPropertyKind.getValue());
            if (queryPOJO.roomNumber != null)
                where.eq("roomNumber", queryPOJO.roomNumber);
            if (queryPOJO.floorType != null) {
                if (queryPOJO.floorType == FloorType.NOT_FIRST)
                    where.gt("floor", 1);
                else if (queryPOJO.floorType == FloorType.NOT_FIRST_NOT_LAST)
                    where.gt("floor", 1).raw("floor<floorNumber");
                else where.raw("floor<floorNumber");
            }
            if (queryPOJO.materialType != null)
                where.eq("materialType", queryPOJO.materialType.getValue());
            if (queryPOJO.overallSqMin != null)
                where.ge("overallSq", queryPOJO.overallSqMin);
            if (queryPOJO.overallSqMax != null)
                where.le("overallSq", queryPOJO.overallSqMax);
            if (queryPOJO.livingSqMin != null)
                where.ge("livingSq", queryPOJO.livingSqMin);
            if (queryPOJO.livingSqMax != null)
                where.le("livingSq", queryPOJO.livingSqMax);
            if (queryPOJO.renovationType != null)
                where.eq("renovationType", queryPOJO.renovationType.getValue());
            if (queryPOJO.securityType != null)
                where.eq("securityType", queryPOJO.securityType.getValue());
            if (queryPOJO.balcony != null)
                where.eq("balcony", true);
            if (queryPOJO.internet != null)
                where.eq("internet", true);
            if (queryPOJO.rentKind != null)
                where.eq("rentKind", queryPOJO.rentKind.getValue());
            if (queryPOJO.rentPriceMax != null)
                where.le("rentPrice", queryPOJO.rentPriceMax);
            if (queryPOJO.depositMax != null)
                where.le("deposit", queryPOJO.depositMax);
            if (queryPOJO.ownTime != null)
                where.eq("ownTime", true);
            if (queryPOJO.credit != null)
                where.eq("credit", true);
            if (queryPOJO.priceMin != null)
                where.ge("price", queryPOJO.priceMin);
            if (queryPOJO.priceMax != null)
                where.le("price", queryPOJO.priceMax);
        }
    }

    public static List<Object> getFlatIds(QueryPOJO queryPOJO) {
        String sqlWhere = "WHERE ST_DWithin(geogr, Geography(ST_MakePoint(?, ?)), ?) limit 10";
        Query flatQuery = Ebean.createQuery(Flat.class, sqlWhere);
        flatQuery.setParameter(1, queryPOJO.cLongitude).setParameter(2, queryPOJO.cLatitude).setParameter(3, queryPOJO.radius);
        addWhereParams(flatQuery, queryPOJO);
        return flatQuery.findIds();
    }

    private class View {
        private List<String> fields;

        private View(List<String> fields) {
            this.fields = Collections.unmodifiableList(fields);
        }

        public List<String> getFields() {
            return fields;
        }

        public String listFields() {
            StringBuilder builder = new StringBuilder(" ");
            for (int i = 0; i < fields.size() - 2; i++)
                builder.append(fields.get(i) + ", ");
            builder.append(fields.get(fields.size() - 1));
            builder.append(" ");
            return builder.toString();
        }
    }
}
