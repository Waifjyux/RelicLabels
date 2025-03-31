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

    private static ArrayList<RelicTier> tiers = new ArrayList<>();

    public RelicTier(String name, String hexColor) {
        this.name = name;
        this.hexColor = hexColor;
    }

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

    public Color getPrimaryColor() {
        return Color.decode(hexColor);
    }

    public Color getSecondaryColor() {
        return darkenColor(getPrimaryColor(), 0.12f);
    }

    public Color getThertiaryColor() {
        return darkenColor(getPrimaryColor(), 0.24f);
    }

    private Color darkenColor(Color color, float amount) {
        float[] hsv = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsv);

        hsv[2] = Math.max(0, hsv[2] - amount);

        return Color.getHSBColor(hsv[0], hsv[1], hsv[2]);
    }

    public static RelicTier getTier(String name) {
        for(RelicTier tier : tiers) {
            if(tier.name.equals(name)) {
                return tier;
            }
        }
        return null;
    }

    public static void addTier(RelicTier tier) {
        tiers.add(tier);
    }

    public static void removeTier(RelicTier tier) {
        tiers.remove(tier);
    }

    public static ArrayList<RelicTier> getTiers() {
        return tiers;
    }

    public static void saveConfig() {

        File file = new File(RelicLabels.getPlugin().getDataFolder(), "tiers.yml");

        FileConfiguration config = new YamlConfiguration();

        ArrayList<Object> tiers = new ArrayList<>();
        for(RelicTier tier : RelicTier.tiers) {
            tiers.add(new String[]{tier.name, tier.hexColor});
        }

        config.set("tiers", tiers);

        try {
            if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if(!file.exists()) file.createNewFile();
            config.save(file);
        }catch(IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void loadConfig() {
        File file = new File(RelicLabels.getPlugin().getDataFolder(), "tiers.yml");

        FileConfiguration config = YamlConfiguration.loadConfiguration(file);

        String out = "Loaded Relic Tiers: ";

        if (config.contains("tiers")) {

            List<List<String>> tiersList = (List<List<String>>) config.getList("tiers");

            if (tiersList != null) {
                for (List<String> tierData : tiersList) {
                    if (tierData.size() == 2) {
                        String name = tierData.get(0);
                        String hexColor = tierData.get(1);

                        RelicTier.addTier(new RelicTier(name, hexColor));
                        out += name + " ";
                    }
                }
            }
        }

        RelicLabels.getPlugin().getLogger().info(out);

    }

    /*
    Colors for Rarity:
    Common 196, 196, 196
    Uncommon 61, 148, 61
    Rare 52, 147, 201
    Epic: 186, 54, 171
    Legendary: 235, 167, 9
     */

}
