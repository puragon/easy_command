package com.example.easy_command;

import java.util.UUID;

public class CustomCommand {
    public UUID uuid;
    public String name;
    public String command;
    public int row;
    public int column;

    public CustomCommand() {}

    public CustomCommand(UUID uuid, String name, String command, int row, int column) {
        this.uuid = uuid;
        this.name = name;
        this.command = command;
        this.row = row;
        this.column = column;
    }
}
