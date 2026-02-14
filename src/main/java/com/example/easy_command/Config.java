package com.example.easy_command;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.fml.loading.FMLPaths;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Config {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(UUID.class, new JsonSerializer<UUID>() {
                @Override
                public JsonElement serialize(UUID uuid, Type type, JsonSerializationContext jsonSerializationContext) {
                    return new JsonPrimitive(uuid.toString());
                }
            })
            .registerTypeAdapter(UUID.class, new JsonDeserializer<UUID>() {
                @Override
                public UUID deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                    UUID uuid;
                    try {
                        uuid = UUID.fromString(jsonElement.getAsString());
                    } catch (Exception e) {
                        uuid = UUID.randomUUID();
                        e.printStackTrace();
                    }
                    return uuid;
                }
            })
            .create();
    private static final Path CONFIG_PATH = FMLPaths.CONFIGDIR.get().resolve("easy_commond_custom_commands.json");

    private static List<CustomCommand> commands = new ArrayList<>();

    static {
        load();
    }

    public static void load() {
        try {
            if (Files.exists(CONFIG_PATH)) {
                String json = Files.readString(CONFIG_PATH);
                commands = GSON.fromJson(json, new TypeToken<List<CustomCommand>>(){}.getType());
                if (commands == null) commands = new ArrayList<>();

                for (CustomCommand cmd: commands) {
                    if (cmd.uuid == null) {
                        cmd.uuid = UUID.randomUUID();
                        save();
                    }
                }
            } else {
                commands = new ArrayList<>();
                save();
            }
        } catch (Exception e) {
            System.err.println("Failed to load custom commands config!");
            e.printStackTrace();
            commands = new ArrayList<>();
        }
    }

    public static void save() {
        try {
            String json = GSON.toJson(commands);
            Files.createDirectories(CONFIG_PATH.getParent());
            Files.writeString(CONFIG_PATH, json);
        } catch (IOException e) {
            System.err.println("Failed to save custom commands config!");
            e.printStackTrace();
        }
    }

    public static List<CustomCommand> getCommands() {
        return new ArrayList<>(commands);
    }

    public static void addCommand(CustomCommand cmd) {
        commands.removeIf(c -> c.uuid.equals(cmd.uuid));
        commands.add(cmd);
        save();
    }

    public static void removeCommand(UUID uuid) {
        commands.removeIf(c -> c.uuid.equals(uuid));
        save();
    }

    public static void setCommands(List<CustomCommand> newCommands) {
        commands = new ArrayList<>(newCommands);
        save();
    }
}
