package com.qurasense.labApi.trial.commands;

public class CreateTrialCommand {
    public String id;
    public String name;
    public String creationUserId;

    public CreateTrialCommand(String id, String name, String creationUserId) {
        this.id = id;
        this.name = name;
        this.creationUserId = creationUserId;
    }
}
