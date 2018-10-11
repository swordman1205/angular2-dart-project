package com.qurasense.userApi.messaging;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectTopicName;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.MessagingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("communicationMessagePublisher")
@Profile("cloud")
public class CloudCommunicationMessagePublisher extends AbstractMessagePublisher {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Publisher publisher;

    @PostConstruct
    protected void create() {
        try {
            ProjectTopicName topicName = ProjectTopicName.of(ServiceOptions.getDefaultProjectId(), MessagingConstants.COMMUNICATION_TOPIC_NAME);
            try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
                topicAdminClient.createTopic(topicName);
            } catch (ApiException e) {
                if (e.getStatusCode().getCode() != StatusCode.Code.ALREADY_EXISTS) {
                    throw new IllegalStateException(e);
                }
            }
            List<ApiFuture<String>> apiFutures = new ArrayList<>();
            publisher = Publisher.newBuilder(topicName).build();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    protected Publisher getPublisher() {
        return publisher;
    }
}
