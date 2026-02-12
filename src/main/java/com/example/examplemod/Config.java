package com.example.examplemod;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.loading.FMLPaths;

import javax.json.JsonObject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
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
        commands.add(cmd);
        save();
    }

    public static void removeCommand(String name) {
        commands.removeIf(c -> c.name.equals(name));
        save();
    }

    public static void setCommands(List<CustomCommand> newCommands) {
        commands = new ArrayList<>(newCommands);
        save();
    }
}
