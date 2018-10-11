package com.qurasense.communication.rules;

import java.util.Objects;
import java.util.function.Consumer;

import com.qurasense.communication.model.Communication;
import org.springframework.stereotype.Component;

@Component("defaultProcessor")
public class DefaultProcessor implements TypeProcessor {

    private Consumer<Communication> onSend;
    private Runnable onIgnore;

    @Override
    public TypeProcessor create(String message) {
        return this;
    }

    @Override
    public TypeProcessor onSend(Consumer<Communication> onSend) {
        this.onSend = onSend;
        return this;
    }

    @Override
    public TypeProcessor onIgnore(Runnable onIgnore) {
        this.onIgnore = onIgnore;
        return this;
    }

    protected void tryCallOnSend(Communication communication) {
        if (onSend != null) {
            onSend.accept(communication);
        }
    }

    protected void tryCallOnIgnore() {
        if (Objects.nonNull(onIgnore)) {
            onIgnore.run();
        }
    }

    @Override
    public TypeProcessor process() {
        tryCallOnIgnore();
        return this;
    }

    @Override
    public String supportType() {
        return "default";
    }

}
