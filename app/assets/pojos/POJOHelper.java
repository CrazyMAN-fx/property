package assets.pojos;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by PENTAGON on 30.07.14.
 */
public class POJOHelper {
    final static ObjectMapper mapper = new ObjectMapper();
    public static Map<String, Object> toMap(Object o) {
        Map<String, Object> map = new HashMap<>();
        try {
            for (Field field : o.getClass().getFields()) {
                Object value = field.get(o);
                if (value != null) map.put(field.getName(), value.toString());
            }
        } catch (IllegalAccessException e) {
        }
        return map;
    }

    public static Map<String, String> toStringMap(Object o) {
        Map<String, String> map = new HashMap<>();
        for (Map.Entry<String, Object> entry : toMap(o).entrySet())
            map.put(entry.getKey(), entry.getValue().toString());
        return map;
    }

    public static Object fromMap(Class cl, Map<String, String> map) {
        return mapper.convertValue(map, cl);
    }
}
