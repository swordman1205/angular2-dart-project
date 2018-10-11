package com.qurasense.communication.swu.provider;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;

public abstract class ResearchSenderProvider implements SwuDataProvider {

    @Value("${communicaltion.researchAddress}")
    private String researchAddress;

    @Override
    public Map<String, Object> getSenderData() {
        Map<String, Object> senderMap = new HashMap<String, Object>();
        senderMap.put("name", "Qurasense"); // optional
        senderMap.put("address", researchAddress);
        senderMap.put("reply_to", researchAddress); // optional
        return senderMap;
    }
}
