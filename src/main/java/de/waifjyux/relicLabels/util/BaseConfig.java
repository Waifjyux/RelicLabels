package de.waifjyux.relicLabels.util;

import de.waifjyux.relicLabels.RelicLabels;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BaseConfig {

    public static int baseUnicode = 0xF000;
    public static boolean updateOnEnchant = true;
    public static boolean updateOnPickup = true;
    public static boolean updateOnJoin = true;
    public static boolean updateOnStartup = true;
    public static boolean experimentalChatDistribution = true;

    private static File configFile;
    private static YamlConfiguration config;

    public static void loadConfig() {

        copyBaseImages();

        configFile = new File(RelicLabels.getPlugin().getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            createDefaultConfig();
        }

        config = YamlConfiguration.loadConfiguration(configFile);

        try {
            baseUnicode = config.getInt("baseUnicode", baseUnicode);
            updateOnEnchant = config.getBoolean("updateOnEnchant", updateOnEnchant);
            updateOnPickup = config.getBoolean("updateOnPickup", updateOnPickup);
            updateOnJoin = config.getBoolean("updateOnJoin", updateOnJoin);
            updateOnStartup = config.getBoolean("updateOnStartup", updateOnStartup);
            experimentalChatDistribution = config.getBoolean("experimentalChatDistribution", experimentalChatDistribution);
        }catch(Exception e) {
            RelicLabels.getPlugin().getLogger().severe("Could not load config.yml");
        }
    }

    private static void createDefaultConfig() {
        configFile.getParentFile().mkdirs();

        try (InputStream inputStream = RelicLabels.getPlugin().getResource("config.yml")) {
            if (inputStream == null) {
                RelicLabels.getPlugin().getLogger().severe("Default config.yml not found in resources! Contact the developer.");
                return;
            }

            Files.copy(inputStream, configFile.toPath());
        } catch (IOException e) {
            RelicLabels.getPlugin().getLogger().severe("Could not copy default config.yml");
        }
    }

    private static void copyBaseImages() {

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

    private static void copyImage(String name, String path) {

        InputStream inputStream = BaseConfig.class.getResourceAsStream("/img/" + name + ".png");

        if(inputStream == null) {
            RelicLabels.getPlugin().getLogger().severe("Could not find image " + name + ".png in resources!");
            return;
        }

        File file = new File(RelicLabels.getPlugin().getDataFolder(), path + name + ".png");

        if(file.exists()) return;
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();

        try {
            Files.copy(inputStream, file.toPath());
        } catch (IOException e) {
            RelicLabels.getPlugin().getLogger().severe("Could not copy image " + name + ".png");
        }

    }

}
