package com.qurasense.communication.pubsub;

import java.io.IOException;
import javax.annotation.PostConstruct;

import com.google.pubsub.v1.PubsubMessage;
import com.googlecode.objectify.ObjectifyService;
import com.qurasense.common.messaging.broadcast.subscriber.BroadcastMessageReceiver;
import com.qurasense.communication.model.Communication;
import com.qurasense.communication.rules.TypeProcessor;
import com.qurasense.communication.rules.TypeProcessorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.googlecode.objectify.ObjectifyService.ofy;

@Component
public class CommunicationBroadcastMessageReceiver extends BroadcastMessageReceiver {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private TypeProcessorFactory typeProcessorFactory;

    @PostConstruct
    protected void init() {
        ObjectifyService.init();
        //TODO: if it registered this way - on message i receive not registered error. Figure out why.
//        ObjectifyService.register(Communication.class);
    }

    @Override
    protected void process(String jsonString, String type, PubsubMessage message) throws IOException {
        ObjectifyService.register(Communication.class);
        ObjectifyService.run(() -> {
            TypeProcessor processor = typeProcessorFactory.getProcessor(type);
            return processor
                    .create(jsonString)
                    .onSend(communication -> ofy().save().entity(communication))
                    .onIgnore(() -> logger.info("message {} was ignored by typeProcessor {}", message.getMessageId(), processor.getClass()))
                    .process();
        });
    }

}
