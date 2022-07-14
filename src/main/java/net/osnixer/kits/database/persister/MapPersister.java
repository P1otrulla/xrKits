package net.osnixer.kits.database.persister;

import com.google.common.collect.Maps;
import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.BaseDataType;
import com.j256.ormlite.support.DatabaseResults;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Map;

public class MapPersister extends BaseDataType {

    private static final MapPersister instance = new MapPersister();

    private MapPersister() {
        super(SqlType.LONG_STRING, new Class<?>[] { MapPersister.class });
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        if (javaObject == null) {
            return null;
        }

        Map<String, Instant> map = (Map<String, Instant>) javaObject;

        if (map.isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Instant> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue().toString());
            sb.append("@");
        }

        return sb.toString();
    }

    @Override
    public Object resultToSqlArg(FieldType fieldType, DatabaseResults results, int columnPos) throws SQLException {
        return results.getString(columnPos);
    }

    @Override
    public Object parseDefaultString(FieldType fieldType, String defaultStr) {
        return String.valueOf(defaultStr);
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        String s = (String) sqlArg;

        if (s == null) {
            return null;
        }

        Map<String, Instant> map = Maps.newHashMap();
        String[] split = s.split("@");

        for (String string : split) {
            String[] mapSplit = string.split("=");

            if (mapSplit.length < 1) {
                continue;
            }

            map.put(mapSplit[0], Instant.parse(mapSplit[1]));
        }

        return map;
    }

    public static MapPersister getSingleton() {
        return instance;
    }
}
