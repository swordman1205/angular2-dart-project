package com.qurasense.communication.swu.provider;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.qurasense.communication.swu.Recipient;

public class NoReplySenderProviderAdapter extends NoReplySenderProvider {

    private final String templateId;
    private final Map<String,Object> emailData;
    private final Recipient recipient;
    private final List<Recipient> ccRecipients;

    public NoReplySenderProviderAdapter(String templateId, Map<String, Object> emailData,
            Recipient recipient) {
        this.templateId = templateId;
        this.emailData = emailData;
        this.recipient = recipient;
        this.ccRecipients = Collections.emptyList();
    }

    public NoReplySenderProviderAdapter(String templateId, Map<String, Object> emailData,
            Recipient recipient, List<Recipient> ccRecipients) {
        this.templateId = templateId;
        this.emailData = emailData;
        this.recipient = recipient;
        this.ccRecipients = ccRecipients;
    }

    @Override
    public String getTemplateId() {
        return templateId;
    }

    @Override
    public Map<String, Object> getEmailData() {
        return emailData;
    }

    @Override
    public Recipient getRecipient() {
        return recipient;
    }

    @Override
    public List<Recipient> getCcRecipients() {
        return ccRecipients;
    }
}
