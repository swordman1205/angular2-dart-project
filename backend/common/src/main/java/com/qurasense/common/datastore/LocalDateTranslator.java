package com.qurasense.common.datastore;

import java.time.LocalDate;

import com.google.cloud.Timestamp;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.ValueType;
import com.googlecode.objectify.impl.Path;
import com.googlecode.objectify.impl.translate.CreateContext;
import com.googlecode.objectify.impl.translate.LoadContext;
import com.googlecode.objectify.impl.translate.SaveContext;
import com.googlecode.objectify.impl.translate.SkipException;
import com.googlecode.objectify.impl.translate.TypeKey;
import com.googlecode.objectify.impl.translate.ValueTranslator;
import com.googlecode.objectify.impl.translate.ValueTranslatorFactory;
import com.qurasense.common.datastore.DataStoreUtils;

public class LocalDateTranslator extends ValueTranslatorFactory<LocalDate, Timestamp> {
    public LocalDateTranslator(Class<LocalDate> pojoType) {
        super(pojoType);
    }

    @Override
    protected ValueTranslator<LocalDate, Timestamp> createValueTranslator(TypeKey<LocalDate> tk, CreateContext ctx, Path path) {
        final Class<?> clazz = tk.getTypeAsClass();

        return new ValueTranslator<LocalDate, Timestamp>(ValueType.TIMESTAMP) {
            @Override
            protected LocalDate loadValue(final Value<Timestamp> value, final LoadContext ctx, final Path path) throws SkipException {
                return value.get().toSqlTimestamp().toLocalDateTime().toLocalDate();
            }

            @Override
            protected Value<Timestamp> saveValue(final LocalDate value, final SaveContext ctx, final Path path) throws SkipException {
                return DataStoreUtils.getValue(value);
            }
        };
    }
}