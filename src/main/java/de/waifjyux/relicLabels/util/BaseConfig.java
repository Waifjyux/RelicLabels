package de.waifjyux.relicLabels.util;

import de.waifjyux.relicLabels.RelicLabels;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BaseConfig {

    // Unicode base value for images
    public static int baseUnicode = 0xF000;

    // Flags to determine when to update relic data
    public static boolean updateOnEnchant = true;
    public static boolean updateOnPickup = true;
    public static boolean updateOnJoin = true;
    public static boolean updateOnStartup = true;
    public static boolean experimentalChatDistribution = true;

    // File and configuration object
    private static File configFile;
    private static YamlConfiguration config;

    // Loads the configuration settings from the config file
    public static void loadConfig() {

        // Ensure that base images are copied before loading the config
        copyBaseImages();

        // Set the configuration file path
        configFile = new File(RelicLabels.getPlugin().getDataFolder(), "config.yml");

        // If the config file doesn't exist, create a default one
        if (!configFile.exists()) {
            createDefaultConfig();
        }

        // Load the configuration file
        config = YamlConfiguration.loadConfiguration(configFile);

        try {
            // Load the configuration values from the file or use defaults if not set
            baseUnicode = config.getInt("baseUnicode", baseUnicode);
            updateOnEnchant = config.getBoolean("updateOnEnchant", updateOnEnchant);
            updateOnPickup = config.getBoolean("updateOnPickup", updateOnPickup);
            updateOnJoin = config.getBoolean("updateOnJoin", updateOnJoin);
            updateOnStartup = config.getBoolean("updateOnStartup", updateOnStartup);
            experimentalChatDistribution = config.getBoolean("experimentalChatDistribution", experimentalChatDistribution);
        } catch (Exception e) {
            // Log error if configuration loading fails
            RelicLabels.getPlugin().getLogger().severe("Could not load config.yml");
        }
    }

    // Creates a default config file if none exists
    private static void createDefaultConfig() {
        // Create necessary directories for the config file
        configFile.getParentFile().mkdirs();

        try (InputStream inputStream = RelicLabels.getPlugin().getResource("config.yml")) {
            // Check if the default config file exists in resources
            if (inputStream == null) {
                RelicLabels.getPlugin().getLogger().severe("Default config.yml not found in resources! Contact the developer.");
                return;
            }

            // Copy the default config file from resources to the plugin data folder
            Files.copy(inputStream, configFile.toPath());
        } catch (IOException e) {
            // Log error if file copying fails
            RelicLabels.getPlugin().getLogger().severe("Could not copy default config.yml");
        }
    }

    // Copies all required base images to the plugin's data folder
    private static void copyBaseImages() {
        // Copy a predefined list of image files to the data folder
        copyImage("reset", "resources/");
        copyImage("bottombar", "resources/");
        copyImage("bottomleftcorner", "resources/");
        copyImage("bottomrightcorner", "resources/");
        copyImage("line", "resources/");
        copyImage("lineborder", "resources/");
        copyImage("linestrike", "resources/");
        copyImage("titleplaquestart", "resources/");
        copyImage("titleplaqueend", "resources/");
        copyImage("titleplaque", "resources/");
        copyImage("topleftcorner", "resources/");
        copyImage("toprightcorner", "resources/");
        copyImage("topbar", "resources/");
    }

    // Copies a single image file from the resources to the plugin data folder
    private static void copyImage(String name, String path) {
        // Load the image input stream from resources
        InputStream inputStream = BaseConfig.class.getResourceAsStream("/img/" + name + ".png");

        // If the image doesn't exist, log an error
        if (inputStream == null) {
            RelicLabels.getPlugin().getLogger().severe("Could not find image " + name + ".png in resources!");
            return;
        }

        // Define the target file path for the image
        File file = new File(RelicLabels.getPlugin().getDataFolder(), path + name + ".png");

        // If the file already exists, no need to copy it
        if (file.exists()) return;
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs(); // Create directories if they don't exist

        try {
            // Copy the image from resources to the plugin data folder
            Files.copy(inputStream, file.toPath());
        } catch (IOException e) {
            // Log error if the image copy fails
            RelicLabels.getPlugin().getLogger().severe("Could not copy image " + name + ".png");
        }
    }

}
