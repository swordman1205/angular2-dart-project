package com.qurasense.userApi.config;

import com.qurasense.common.messaging.broadcast.subscriber.BroadcastInitiator;
import com.qurasense.common.messaging.broadcast.subscriber.EmulatorBroadcastInitiator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class PubsubConfig {

    @Bean
    @Profile("cloud")
    public BroadcastInitiator broadcastInitiator() {
        return new BroadcastInitiator("accountBroadcastSubscribtion");
    }

    @Bean
    @Profile("emulator")
    public EmulatorBroadcastInitiator emulatorBroadcastInitiator() {
        return new EmulatorBroadcastInitiator("accountBroadcastSubscribtion");
    }

}
