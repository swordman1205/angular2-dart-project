package com.qurasense.communication.rules;

import java.util.function.Consumer;

import com.qurasense.communication.model.Communication;

public interface TypeProcessor {

    TypeProcessor create(String message);

    TypeProcessor onSend(Consumer<Communication> onSend);

    TypeProcessor onIgnore(Runnable onIgnore);

    TypeProcessor process();

    String supportType();
}
