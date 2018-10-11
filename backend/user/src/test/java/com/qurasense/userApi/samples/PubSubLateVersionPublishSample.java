package com.qurasense.userApi.samples;

import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.api.gax.grpc.GrpcTransportChannel;
import com.google.api.gax.rpc.FixedTransportChannelProvider;
import com.google.api.gax.rpc.TransportChannelProvider;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.cloud.pubsub.v1.TopicAdminClient;
import com.google.cloud.pubsub.v1.TopicAdminSettings;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.qurasense.common.messaging.MessagingConstants;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PubSubLateVersionPublishSample {

    private final static Logger logger = LoggerFactory.getLogger(PubSubLateVersionPublishSample.class);
    private static ManagedChannel channel;
    private static Publisher publisher;

    public static void main(String[] args) {
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
            logger.info("project id: {}", defaultProjectId);
            ProjectTopicName topicName = ProjectTopicName.of(defaultProjectId, MessagingConstants.COMMUNICATION_TOPIC_NAME);
            try {
                topicClient.createTopic(topicName);
                logger.info("topic created");
            } catch (Exception e) {
                logger.info("topic already created");
            }

            // Set the channel and credentials provider when creating a `Publisher`.
            // Similarly for Subscriber
            publisher =
                    Publisher.newBuilder(topicName)
                            .setChannelProvider(channelProvider)
                            .setCredentialsProvider(credentialsProvider)
                            .build();
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                    .setData(ByteString.copyFromUtf8("sample message")).build();
            String messageId = publisher.publish(pubsubMessage).get();
            logger.info("sended message id {}", messageId);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException("error start emulator publisher", e);
        } finally {
            channel.shutdown();
        }
    }

}
