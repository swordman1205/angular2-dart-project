package com.qurasense.communication.swu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.qurasense.communication.swu.provider.SwuDataProvider;
import com.sendwithus.SendWithUs;
import com.sendwithus.exception.SendWithUsException;
import com.sendwithus.model.SendReceipt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SwuSender {

    //test key
//    private static final String SENDWITHUS_API_KEY = "test_b7fa341489091cb13076aa96f3b0af51e2075456";

    //prod key
    private static final String SENDWITHUS_API_KEY = "live_bfe366af49458a797c1a1e61612337df18bce9c6";
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private SendWithUs sendWithUs = new SendWithUs(SENDWITHUS_API_KEY);

    public void send(SwuDataProvider dataProvider) {
        send(dataProvider, "unknown");
    }

    public void send(SwuDataProvider dataProvider, String type) {
        try {
            List<Map<String, Object>> cc = dataProvider.getCcRecipients().stream()
                .filter(r -> StringUtils.isNotBlank(r.getAddress()))
                .map(r -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", r.getName());
                    map.put("address", r.getAddress());
                    return map;
                }).collect(Collectors.toList());
            Map[] ccArray = cc.toArray(new Map[cc.size()]);

            Map<String, Object> recipientMap = new HashMap<>();
            recipientMap.put("name", dataProvider.getRecipient().getName());
            recipientMap.put("address", dataProvider.getRecipient().getAddress());

            if (StringUtils.isNotBlank(dataProvider.getRecipient().getAddress())) {
                SendReceipt sendReceipt = sendWithUs.send(
                        dataProvider.getTemplateId(),
                        recipientMap,
                        dataProvider.getSenderData(),
                        dataProvider.getEmailData(),
                        ccArray
                );
                logger.info("sended, recipient: {}, type: {}", sendReceipt, type);
            } else {
                logger.info("ignore send emails, no recipient address type: {}", type);
            }
        } catch (SendWithUsException e) {
            logger.error("error while send with swu", e);
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
