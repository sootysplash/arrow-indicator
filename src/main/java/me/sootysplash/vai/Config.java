package me.sootysplash.vai;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Config {

    private static final Path file = FabricLoader.getInstance().getConfigDir().resolve("vanilla-arrow-indicator.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static Config instance;

    public boolean enabled = true;
    public boolean tippedArrowColors = true;
    public boolean countFireworks = true;
    public int ammoColor = Color.YELLOW.getRGB();
    public int maxAmmoStack = 99;
    public int onlyShowAmmoBelow = 16;

    public void save() {
        try {
            Files.writeString(file, GSON.toJson(this));
        } catch (IOException e) {
            Main.LOGGER.error(Main.LOGGER.getName() + " could not save the config.");
        }
    }

    public static Config getInstance() {
        if (instance == null) {
            try {
                instance = GSON.fromJson(Files.readString(file), Config.class);
            } catch (IOException exception) {
                Main.LOGGER.warn(Main.LOGGER.getName() + " couldn't load the config, using defaults.");
                instance = new Config();
            }
        }
        return instance;
    }
}
