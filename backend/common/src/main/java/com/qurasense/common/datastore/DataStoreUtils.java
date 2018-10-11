package com.qurasense.common.datastore;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.BooleanValue;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.ListValue;
import com.google.cloud.datastore.LongValue;
import com.google.cloud.datastore.NullValue;
import com.google.cloud.datastore.StringValue;
import com.google.cloud.datastore.TimestampValue;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueType;

public class DataStoreUtils {

    private DataStoreUtils() {}

    public static Value getValue(String aString) {
        return Objects.nonNull(aString) ? StringValue.of(aString) : NullValue.of();
    }

    public static Value getValue(Timestamp timestamp) {
        return Objects.nonNull(timestamp) ? TimestampValue.of(timestamp) : NullValue.of();
    }

    public static Value getValue(LocalDate localDate) {
        if (localDate == null) {
            return NullValue.of();
        } else {
            Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
            return TimestampValue.of(Timestamp.of(date));
        }
    }

    public static Value getValue(Date date) {
        return Objects.nonNull(date) ? TimestampValue.of(Timestamp.of(date)) : NullValue.of();
    }

    public static Value getValue(Long aLong) {
        return Objects.nonNull(aLong) ? LongValue.of(aLong) : NullValue.of();
    }

    public static Value getValue(Boolean aBoolean) {
        return Objects.nonNull(aBoolean) ? BooleanValue.of(aBoolean) : NullValue.of();
    }

    public static <E extends Enum<E>> Value getValue(E enumValue) {
        return Objects.nonNull(enumValue) ? StringValue.of(enumValue.name()) : NullValue.of();
    }

    public static <E extends Enum<E>> ListValue getValue(List<E> values) {
        ListValue.Builder builder = ListValue.newBuilder();
        for (E value : values) {
            builder.addValue(getValue(value));
        }
        return builder.build();
    }

    public static Value getValue(LocalTime time) {
        return Objects.nonNull(time) ? StringValue.of(time.toString()) : NullValue.of();
    }

    public static Date transformDate(Timestamp timestamp) {
        return Objects.nonNull(timestamp) ? timestamp.toSqlTimestamp() : null;
    }

    public static LocalDate transformLocalDate(Timestamp timestamp) {
        return Objects.nonNull(timestamp) ? timestamp.toSqlTimestamp().toLocalDateTime().toLocalDate() : null;
    }

    public static LocalTime transformTime(String timeString) {
        return Objects.nonNull(timeString) ? LocalTime.parse(timeString) : null;
    }

    public static String getString(Entity aEntity, String propertyName) {
        return aEntity.contains(propertyName) ? aEntity.getString(propertyName) : null;
    }

    public static List<StringValue> getStringList(Entity aEntity, String propertyName) {
        return aEntity.contains(propertyName) ? aEntity.getList(propertyName) : new ArrayList<>();
    }

    public static LocalDate getLocalDate(Entity aEntity, String propertyName) {
        Timestamp propertyValue = aEntity.getTimestamp(propertyName);
        if (propertyValue != null) {
            return propertyValue.toSqlTimestamp().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        }
        return null;
    }

    public static Long getLong(Entity aEntity, String propertyName) {
        Value<?> aEntityValue = aEntity.getValue(propertyName);
        return aEntityValue.getType() == ValueType.NULL ? null : ((LongValue)aEntityValue).get();
    }

    public static <E extends Enum<E>> E getEnum(Class<E> enumClass, String value) {
        return Optional.ofNullable(value).map((v)->Enum.valueOf(enumClass, v)).orElse(null);
    }

}
