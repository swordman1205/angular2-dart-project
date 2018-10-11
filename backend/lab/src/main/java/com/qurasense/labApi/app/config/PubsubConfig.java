package com.qurasense.labApi.app.config;

import com.qurasense.common.messaging.broadcast.publisher.BroadcastMessagePublisher;
import com.qurasense.common.messaging.broadcast.publisher.EmulatorBroadcastMessagePublisher;
import com.qurasense.common.messaging.broadcast.subscriber.BroadcastInitiator;
import com.qurasense.common.messaging.broadcast.subscriber.EmulatorBroadcastInitiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class PubsubConfig {

    @Bean("broadcastMessagePublisher")
    @Profile({"cloud"})
    public BroadcastMessagePublisher broadcastMessagePublisher() {
        return new BroadcastMessagePublisher();
    }

    @Bean("broadcastMessagePublisher")
    @Profile({"emulator"})
    public EmulatorBroadcastMessagePublisher emulatorBroadcastMessagePublisher() {
        return new EmulatorBroadcastMessagePublisher();
    }

    @Bean
    @Profile("cloud")
    public BroadcastInitiator broadcastInitiator() {
        return new BroadcastInitiator("labBroadcastSubscribtion");
    }

    @Bean
    @Profile("emulator")
    public EmulatorBroadcastInitiator emulatorBroadcastInitiator() {
        return new EmulatorBroadcastInitiator("labBroadcastSubscribtion");
    }

}
