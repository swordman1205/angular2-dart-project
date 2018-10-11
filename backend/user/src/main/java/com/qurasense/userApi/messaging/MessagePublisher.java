package com.qurasense.userApi.messaging;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;

import com.google.api.core.ApiFuture;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectTopicName;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.MessagingConstants;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile({"cloud", "emulator"})
public class MessagePublisher extends AbstractMessagePublisher {

    private static final String PROJECT_ID = ServiceOptions.getDefaultProjectId();
    private Publisher publisher;

    @PostConstruct
    protected void create() {
        try {
            ProjectTopicName topicName = ProjectTopicName.of(PROJECT_ID, MessagingConstants.HEALTH_AUTH_TOPIC_NAME);
            try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
                topicAdminClient.createTopic(topicName);
            } catch (Exception e) {
                //could already exist
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
