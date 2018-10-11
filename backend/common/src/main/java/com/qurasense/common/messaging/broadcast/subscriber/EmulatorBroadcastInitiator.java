package com.qurasense.common.messaging.broadcast.subscriber;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.pubsub.v1.SubscriptionAdminClient;
import com.google.cloud.pubsub.v1.SubscriptionAdminSettings;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PushConfig;
import com.google.pubsub.v1.Subscription;
import com.qurasense.common.messaging.MessagingConstants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class EmulatorBroadcastInitiator {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private BroadcastMessageReceiver messageReceiver;

    private String subscriptionName;
    private Subscriber subscriber;
    private ManagedChannel channel;

    public EmulatorBroadcastInitiator(String subscriptionName) {
        this.subscriptionName = subscriptionName;
    }

    @PostConstruct
    protected void start() throws Exception {
        // Your Google Cloud Platform project ID
        createTopicAndSubscriber();
    }

    private void createTopicAndSubscriber() {
        String hostport = System.getenv("PUBSUB_EMULATOR_HOST");
        logger.info("staring publisher on emulator at: {}", hostport);
        channel = ManagedChannelBuilder.forTarget(hostport).usePlaintext(true).build();
        try {
            TransportChannelProvider channelProvider = FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
            CredentialsProvider credentialsProvider = NoCredentialsProvider.create();

            TopicAdminClient topicClient = TopicAdminClient.create(
                        TopicAdminSettings.newBuilder()
                                .setTransportChannelProvider(channelProvider)
                                .setCredentialsProvider(credentialsProvider)
                                .build());

            String defaultProjectId = ServiceOptions.getDefaultProjectId();
            ProjectTopicName topicName = ProjectTopicName.of(defaultProjectId, MessagingConstants.BROADCAST_TOPIC_NAME);
            try {
                topicClient.createTopic(topicName);
                logger.info("topic created");
            } catch (Exception e) {
                logger.info("topic already created");
            }

            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(
                    SubscriptionAdminSettings.newBuilder()
                        .setTransportChannelProvider(channelProvider)
                        .setCredentialsProvider(credentialsProvider)
                        .build()
                    );

            ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(ServiceOptions.getDefaultProjectId(),
                    this.subscriptionName);

            try {
//                PushConfig pushConfig = PushConfig.newBuilder().build();
//                int ackDeadlineSeconds = 0;
                Subscription subscription = subscriptionAdminClient.createSubscription(subscriptionName, topicName,
                        PushConfig.getDefaultInstance(), 0);
                logger.info("subscription created");
            } catch (Exception e) {
                logger.info("subscription already created");
            }

            subscriber = Subscriber.newBuilder(subscriptionName, messageReceiver)
                    .setChannelProvider(channelProvider)
                    .setCredentialsProvider(credentialsProvider)
                    .build();
            subscriber.startAsync().awaitRunning();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("error creating emulator", e);
        }
    }

    @PreDestroy
    protected void close() throws Exception {
        logger.info("closing emulator pubsub");
        subscriber.stopAsync();
        channel.shutdown();
        logger.info("closed emulator pubsub");
    }

}
