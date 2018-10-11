package com.qurasense.communication.swu.provider;

import java.util.HashMap;
import java.util.Map;

public abstract class NoReplySenderProvider implements SwuDataProvider {

    @Override
    public Map<String, Object> getSenderData() {
        Map<String, Object> senderMap = new HashMap<String, Object>();
        senderMap.put("name", "Qurasense"); // optional
        senderMap.put("address", "no_reply@qurasense.com");
        senderMap.put("reply_to", "no_reply@qurasense.com"); // optional
        return senderMap;
    }
}
