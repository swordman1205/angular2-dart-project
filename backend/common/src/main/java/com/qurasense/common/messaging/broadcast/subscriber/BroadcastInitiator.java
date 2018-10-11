package com.qurasense.common.messaging.broadcast.subscriber;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.StatusCode;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.qurasense.common.messaging.MessagingConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BroadcastInitiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BroadcastMessageReceiver messageReceiver;

    private String subscriptionName;
    private Subscriber subscriber;

    public BroadcastInitiator(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    @PostConstruct
    protected void start() throws Exception {
        // Your Google Cloud Platform project ID
        createTopic();
        createSubscriber();
    }

    private void createSubscriber() throws Exception {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(ServiceOptions.getDefaultProjectId(),
                this.subscriptionName);
        // create a subscriber bound to the asynchronous message receiver
        try (SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create()) {
            ProjectTopicName topicName = ProjectTopicName.of(ServiceOptions.getDefaultProjectId(),
                    MessagingConstants.BROADCAST_TOPIC_NAME);
            Subscription subscription = subscriptionAdminClient.createSubscription(
                            subscriptionName, topicName, PushConfig.getDefaultInstance(), 0);
        } catch (ApiException e) {
            if (e.getStatusCode().getCode() != StatusCode.Code.ALREADY_EXISTS) {
                throw new IllegalStateException(e);
            }
        }
        subscriber = Subscriber.newBuilder(subscriptionName, messageReceiver).build();
        subscriber.startAsync().awaitRunning();
        logger.info("subscriber start receiving messages");
    }

    @PreDestroy
    protected void close() throws Exception {
        logger.info("subscriber stop receiving messages");
        subscriber.stopAsync();
    }

    private void createTopic() throws Exception {
        ProjectTopicName topic = ProjectTopicName.of(ServiceOptions.getDefaultProjectId(), MessagingConstants.BROADCAST_TOPIC_NAME);
        try (TopicAdminClient topicAdminClient = TopicAdminClient.create()) {
            topicAdminClient.createTopic(topic);
            logger.info("Topic {}:{} created", topic.getProject(), topic.getTopic());
        } catch (ApiException e) {
            if (e.getStatusCode().getCode() == StatusCode.Code.ALREADY_EXISTS) {
                logger.info("Topic {}:{} already exist", topic.getProject(), topic.getTopic());
            } else {
                logger.error("Error create topic: {}", e.getStatusCode().getCode());
            }
            logger.info("isRetryable: {}", e.isRetryable());
        }
    }

}
