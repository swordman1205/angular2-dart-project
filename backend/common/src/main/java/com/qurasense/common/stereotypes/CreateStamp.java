package com.qurasense.common.stereotypes;

import java.util.Date;

public interface CreateStamp {
    Date getCreateTime();

    void setCreateTime(Date createTime);

    String getCreateUserId();

    void setCreateUserId(String createUserId);
}
