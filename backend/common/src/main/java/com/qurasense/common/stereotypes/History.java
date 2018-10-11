package com.qurasense.common.stereotypes;

import java.util.Date;

public interface History extends CreateStamp {
    default boolean isCurrent() {
        return getCancelTime() == null && getCancelUserId() == null;
    }

    Date getCancelTime();

    void setCancelTime(Date cancelTime);

    String getCancelUserId();

    void setCancelUserId(String userId);
}
