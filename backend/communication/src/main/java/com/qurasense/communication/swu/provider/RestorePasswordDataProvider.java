package com.qurasense.communication.swu.provider;

import java.util.HashMap;
import java.util.Map;

import com.qurasense.common.messaging.messages.RestorePasswordMessage;
import com.qurasense.communication.swu.Recipient;

public class RestorePasswordDataProvider extends NoReplySenderProvider {

    private final Recipient recipient;
    private final Map<String, Object> dataMap;

    public RestorePasswordDataProvider(RestorePasswordMessage restorePasswordMessage) {
        this(restorePasswordMessage.getUrl(), restorePasswordMessage.getAddress());
    }

    public RestorePasswordDataProvider(String url, String address) {

        recipient = new Recipient(address);

        dataMap = new HashMap<>();
        dataMap.put("url", url);
    }

    @Override
    public String getTemplateId() {
        return "tem_XMkr796VxJ4Kwgv63vxHmBbX";
    }

    @Override
    public Map<String, Object> getEmailData() {
        return dataMap;
    }

    @Override
    public Recipient getRecipient() {
        return recipient;
    }
}
