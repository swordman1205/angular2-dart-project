package com.qurasense.common.messaging.messages;

public class SuccessSignupMessage extends CommunicationMessage {

    private String fullName;
    private String emailConfirmationUrl;

    public SuccessSignupMessage() {
        super();
    }

    public SuccessSignupMessage(String fullName, String address, ChannelType type, String emailConfirmationUrl) {
        super(address, type);
        this.fullName = fullName;
        this.emailConfirmationUrl = emailConfirmationUrl;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmailConfirmationUrl() {
        return emailConfirmationUrl;
    }

    public void setEmailConfirmationUrl(String emailConfirmationUrl) {
        this.emailConfirmationUrl = emailConfirmationUrl;
    }
}
