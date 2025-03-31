package de.waifjyux.relicLabels.labels;

import de.waifjyux.relicLabels.RelicLabels;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RelicTier {

    private String name;
    private String hexColor;

    // List of all RelicTiers
    private static ArrayList<RelicTier> tiers = new ArrayList<>();

    // Constructor to initialize the tier with name and hex color
    public RelicTier(String name, String hexColor) {
        this.name = name;
        this.hexColor = hexColor;
    }

    // Getters and Setters for name and hexColor
    public String getName() {
        return name;
    }

    public String getHexColor() {
        return hexColor;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setHexColor(String hexColor) {
        this.hexColor = hexColor;
    }

    // Returns the primary color based on hexColor
    public Color getPrimaryColor() {
        return Color.decode(hexColor);
    }

    // Returns a secondary color by darkening the primary color by 12%
    public Color getSecondaryColor() {
        return darkenColor(getPrimaryColor(), 0.12f);
    }

    // Returns a tertiary color by darkening the primary color by 24%
    public Color getThertiaryColor() {
        return darkenColor(getPrimaryColor(), 0.24f);
    }

    // Method to darken a color by a certain amount
    private Color darkenColor(Color color, float amount) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);

        // Darkens the color by decreasing the brightness value
        hsv[2] = Math.max(0, hsv[2] - amount);

        return Color.getHSBColor(hsv[0], hsv[1], hsv[2]);
    }

    // Returns the RelicTier based on the name
    public static RelicTier getTier(String name) {
        for (RelicTier tier : tiers) {
            if (tier.name.equals(name)) {
                return tier;
            }
        }
        return null;  // Returns null if tier not found
    }

    // Adds a new RelicTier to the list
    public static void addTier(RelicTier tier) {
        tiers.add(tier);
    }

    // Removes a RelicTier from the list
    public static void removeTier(RelicTier tier) {
        tiers.remove(tier);
    }

    // Returns the list of all RelicTiers
    public static ArrayList<RelicTier> getTiers() {
        return tiers;
    }

    // Saves the list of RelicTiers to the config file
    public static void saveConfig() {
        File file = new File(RelicLabels.getPlugin().getDataFolder(), "tiers.yml");

        FileConfiguration config = new YamlConfiguration();

        ArrayList<Object> tiers = new ArrayList<>();

        // Adds each tier to the configuration
        for (RelicTier tier : RelicTier.tiers) {
            tiers.add(new String[]{tier.name, tier.hexColor});
        }

        // Sets the tiers list in the config
        config.set("tiers", tiers);

        try {
            // Ensures the file and parent directories exist
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) file.createNewFile();
            config.save(file);  // Saves the config to file
        } catch (IOException e) {
            throw new RuntimeException(e);  // Throws an exception if there is an error saving
        }
    }

    // Loads the RelicTiers from the config file
    public static void loadConfig() {
        File file = new File(RelicLabels.getPlugin().getDataFolder(), "tiers.yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String out = "Loaded Relic Tiers: ";

        if (config.contains("tiers")) {
            // Loads each tier from the config file
            List<List<String>> tiersList = (List<List<String>>) config.getList("tiers");

            if (tiersList != null) {
                for (List<String> tierData : tiersList) {
                    if (tierData.size() == 2) {
                        String name = tierData.get(0);
                        String hexColor = tierData.get(1);

                        // Adds the tier to the list
                        RelicTier.addTier(new RelicTier(name, hexColor));
                        out += name + " ";  // Appends the name of the loaded tier
                    }
                }
            }
        }

        // Logs the loaded tiers
        RelicLabels.getPlugin().getLogger().info(out);
    }

    /*
     * Colors for Rarity:
     * Common 196, 196, 196
     * Uncommon 61, 148, 61
     * Rare 52, 147, 201
     * Epic: 186, 54, 171
     * Legendary: 235, 167, 9
     */
}
