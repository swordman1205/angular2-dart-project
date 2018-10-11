package com.qurasense.common.messaging.broadcast.publisher;

import javax.annotation.PostConstruct;

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

public class BroadcastMessagePublisher extends AbstractMessagePublisher {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private Publisher publisher;

    @PostConstruct
    protected void create() {
        try {
            ProjectTopicName topicName = ProjectTopicName.of(ServiceOptions.getDefaultProjectId(), MessagingConstants.BROADCAST_TOPIC_NAME);
            try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
                topicAdminClient.createTopic(topicName);
            } catch (ApiException e) {
                if (e.getStatusCode().getCode() != StatusCode.Code.ALREADY_EXISTS) {
                    throw new IllegalStateException(e);
                }
            }
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
