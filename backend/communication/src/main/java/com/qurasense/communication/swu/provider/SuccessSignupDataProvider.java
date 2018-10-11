package com.qurasense.communication.swu.provider;

import java.util.HashMap;
import java.util.Map;

import com.qurasense.common.messaging.messages.SuccessSignupMessage;
import com.qurasense.communication.swu.Recipient;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SuccessSignupDataProvider extends ResearchSenderProvider {

    private final Recipient recipient;
    private final String emailConfirmationUrl;

    public SuccessSignupDataProvider(SuccessSignupMessage cm) {
        recipient = new Recipient(cm.getFullName(), cm.getAddress());
        emailConfirmationUrl = cm.getEmailConfirmationUrl();
    }

    @Override
    public String getTemplateId() {
        return "tem_3M3HKyHX4hWSh6GqBgxSCVRF";
    }

    @Override
    public Map<String, Object> getEmailData() {
        Map<String, Object> emailData = new HashMap<>();
        emailData.put("emailConfirmUrl", emailConfirmationUrl);
        return emailData;
    }

    @Override
    public Recipient getRecipient() {
        return recipient;
    }
}
