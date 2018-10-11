package com.qurasense.communication.swu.provider;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.qurasense.communication.swu.Recipient;

public interface SwuDataProvider {

    String getTemplateId();
    Map<String, Object> getSenderData();
    Map<String, Object> getEmailData();
    Recipient getRecipient();
    default List<Recipient> getCcRecipients() {
        return Collections.emptyList();
    };

}
