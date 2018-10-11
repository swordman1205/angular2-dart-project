package com.qurasense.userApi.samples;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.google.api.core.ApiFuture;
import com.google.api.gax.core.CredentialsProvider;
import com.google.api.gax.core.NoCredentialsProvider;
import com.google.cloud.ServiceOptions;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.ProjectTopicName;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.PullRequest;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class PubSubSample {

    private static final BlockingQueue<PubsubMessage> messages = new LinkedBlockingDeque<>();

    static class MessageReceiverExample implements MessageReceiver {

        @Override
        public void receiveMessage(PubsubMessage message, AckReplyConsumer consumer) {
            messages.offer(message);
            consumer.ack();
        }
    }

    public static void main(String[] args) throws Exception {
        ManagedChannel channel = ManagedChannelBuilder.forTarget("localhost:8381").usePlaintext(true).build();
        try {
//            ChannelProvider channelProvider = FixedChannelProvider.create(channel);
            CredentialsProvider credentialsProvider = new NoCredentialsProvider();

            // Similarly for SubscriptionAdminSettings
//            TopicAdminClient topicClient = TopicAdminClient.create(
//                    TopicAdminSettings
//                            .defaultBuilder()
//                            .setTransportProvider(
//                                    GrpcTransportProvider.newBuilder()
//                                            .setChannelProvider(channelProvider)
//                                            .build())
//                            .setCredentialsProvider(credentialsProvider)
//                            .build());

            // Similarly for Subscriber
            String defaultProjectId = ServiceOptions.getDefaultProjectId();
            ProjectTopicName topicName = ProjectTopicName.of(defaultProjectId, "topic_id");
            try {
//                topicClient.createTopic(topicName);
            } catch (Exception e) {

            }

//            SubscriptionAdminSettings subscriptionAdminSettings = SubscriptionAdminSettings.defaultBuilder()
//                    .setTransportProvider(
//                            GrpcTransportProvider.newBuilder()
//                                    .setChannelProvider(channelProvider)
//                                    .build())
//                    .setCredentialsProvider(credentialsProvider)
//                    .build();
//            SubscriptionAdminClient subscriptionAdminClient = SubscriptionAdminClient.create(subscriptionAdminSettings);

            ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(defaultProjectId, "sub-1");
            try {
//                subscriptionAdminClient.createSubscription(subscriptionName, topicName, PushConfig.getDefaultInstance(), 10);
            } catch (Exception e) {
                //
            }

            // Retry settings control how the publisher handles retryable failures
//            RetrySettings retrySettings = RetrySettings.newBuilder()
//                    .setInitialRetryDelay(Duration.ofMillis(100))
//                    .setRetryDelayMultiplier(2.0)
//                    .setMaxRetryDelay(Duration.ofSeconds(5))
//                    .setTotalTimeout(Duration.ofSeconds(10))
//                    .setInitialRpcTimeout(Duration.ofSeconds(1))
//                    .setMaxRpcTimeout(Duration.ofSeconds(1))
//                    .build();

//            Publisher publisher = Publisher
//                    .defaultBuilder(topicName)
//                    .setChannelProvider(channelProvider)
//                    .setCredentialsProvider(credentialsProvider)
//                    .build();
//            GrpcSubscriberStub subscriber = GrpcSubscriberStub.create(subscriptionAdminSettings);

//            publishMessage(publisher, "Test");
            PullRequest pullRequest =
                    PullRequest.newBuilder()
                            .setMaxMessages(1)
                            .setReturnImmediately(false) // return immediately if messages are not available
                            .setSubscription(subscriptionName.toString())
                            .build();
            // use pullCallable().futureCall to asynchronously perform this operation
//            PullResponse pullResponse = subscriber.pullCallable().call(pullRequest);
//            List<String> ackIds = new ArrayList<>();
//            for (ReceivedMessage message : pullResponse.getReceivedMessagesList()) {
//                System.out.println(message.getMessage().getData().toStringUtf8());
//                ackIds.add(message.getAckId());
//            }
//            // acknowledge received messages
//            AcknowledgeRequest acknowledgeRequest =
//                    AcknowledgeRequest.newBuilder()
//                            .setSubscription(subscriptionName.toString())
//                            .addAllAckIds(ackIds)
//                            .build();
//            // use acknowledgeCallable().futureCall to asynchronously perform this operation
//            subscriber.acknowledgeCallable().call(acknowledgeRequest);
        } finally {
            channel.shutdown();
        }
    }

    //schedule a message to be published, messages are automatically batched
    private static ApiFuture<String> publishMessage(Publisher publisher, String message)
            throws Exception {
        // convert message to bytes
        ByteString data = ByteString.copyFromUtf8(message);
        PubsubMessage pubsubMessage = PubsubMessage.newBuilder()
                .setData(data)
                .build();
        return publisher.publish(pubsubMessage);
    }

}
