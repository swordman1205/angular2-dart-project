package com.qurasense.common.repository;

import com.qurasense.common.stereotypes.CreateStamp;
import com.qurasense.common.stereotypes.History;
import com.qurasense.common.stereotypes.Identifiable;
import com.qurasense.common.stereotypes.Nameable;
import org.apache.commons.lang3.Validate;

public final class EntityValidator {

    private EntityValidator() {
    }

    public static void validateOnEntitySave(Object entity) {
        if (entity instanceof Identifiable) {
            Validate.notNull(((Identifiable)entity).getId(), "Can`t save entity with null ID");
        }
        if (entity instanceof Nameable) {
            Validate.notNull(((Nameable) entity).getName(), "Can`t save nameable with null name");
        }
        if (entity instanceof CreateStamp) {
            Validate.notNull(((CreateStamp) entity).getCreateTime(), "Can`t save without create time");
            Validate.notNull(((CreateStamp) entity).getCreateUserId(), "Can`t save without create user id");
        }
        if(entity instanceof History) {
            History historyEntity = (History) entity;
            if(!historyEntity.isCurrent()) {
                Validate.notNull(historyEntity.getCancelTime(), "Can`t save cancelled entity without cancel time");
                Validate.notNull(historyEntity.getCancelUserId(), "Can`t save cancelled entity without cancel user id");
            }
        }
    }
}
