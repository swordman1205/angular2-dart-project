package com.qurasense.common.utest.cases;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qurasense.common.messaging.messages.CommunicationMessage;
import com.qurasense.common.messaging.messages.SuccessSignupMessage;
import org.junit.Assert;
import org.junit.Test;

public class CommunicationMessageInheritanceTest {

    @Test
    public void testGraphHierarchyJackson() throws IOException {
        ObjectMapper om  = new ObjectMapper();
        SuccessSignupMessage ssm = new SuccessSignupMessage("Dick Chane", "dick@chane.com", CommunicationMessage.ChannelType.EMAIL, "token");
        String string = om.writerWithDefaultPrettyPrinter().writeValueAsString(ssm);
        Object value = om.readerFor(CommunicationMessage.class).readValue(string);
        Assert.assertEquals(SuccessSignupMessage.class, value.getClass());
    }

}
