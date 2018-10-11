package com.qurasense.userApi.messaging;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.ApiException;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.StatusCode;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.pubsub.v1.ProjectTopicName;
import com.qurasense.common.messaging.AbstractMessagePublisher;
import com.qurasense.common.messaging.MessagingConstants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component("communicationMessagePublisher")
@Profile("emulator")
public class EmulatorCommunicationMessagePublisher extends AbstractMessagePublisher {

    private final Logger logger = LoggerFactory.getLogger(getClass());
    private ManagedChannel channel;
    private Publisher publisher;

    @PostConstruct
    protected void init() {
        //from https://cloud.google.com/pubsub/docs/emulator#pubsub-emulator-java
        String hostport = System.getenv("PUBSUB_EMULATOR_HOST");
        logger.info("staring publisher on emulator at: {}", hostport);
        channel = ManagedChannelBuilder.forTarget(hostport).usePlaintext(true).build();
        try {
            TransportChannelProvider channelProvider =
                    FixedTransportChannelProvider.create(GrpcTransportChannel.create(channel));
            CredentialsProvider credentialsProvider = NoCredentialsProvider.create();

            // Set the channel and credentials provider when creating a `TopicAdminClient`.
            // Similarly for SubscriptionAdminClient
            TopicAdminClient topicClient =
                    TopicAdminClient.create(
                            TopicAdminSettings.newBuilder()
                                    .setTransportChannelProvider(channelProvider)
                                    .setCredentialsProvider(credentialsProvider)
                                    .build());

            String defaultProjectId = ServiceOptions.getDefaultProjectId();
            ProjectTopicName topicName = ProjectTopicName.of(defaultProjectId, MessagingConstants.COMMUNICATION_TOPIC_NAME);
            try {
                topicClient.createTopic(topicName);
                logger.info("topic created");
            } catch (ApiException e) {
                if (e.getStatusCode().getCode() != StatusCode.Code.ALREADY_EXISTS) {
                    throw new IllegalStateException(e);
                }
            }

            // Set the channel and credentials provider when creating a `Publisher`.
            // Similarly for Subscriber
            publisher =
                    Publisher.newBuilder(topicName)
                            .setChannelProvider(channelProvider)
                            .setCredentialsProvider(credentialsProvider)
                            .build();
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("error start emulator publisher", e);
        }
    }

    @PreDestroy
    protected void clear() {
        channel.shutdown();
    }

    @Override
    protected Publisher getPublisher() {
        return publisher;
    }
}
