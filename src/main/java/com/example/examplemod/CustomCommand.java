package com.example.examplemod;

public class CustomCommand {
    public String name;
    public String command;
    public int row;
    public int column;

    public CustomCommand() {}

    public CustomCommand(String name, String command, int row, int column) {
        this.name = name;
        this.command = command;
        this.row = row;
        this.column = column;
    }
}
