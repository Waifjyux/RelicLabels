package de.waifjyux.relicLabels.util;

import de.waifjyux.relicLabels.RelicLabels;
import de.waifjyux.relicLabels.labels.RelicLabel;
import de.waifjyux.relicLabels.labels.RelicTier;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ItemsAdderIntegration {

    // Private constructor to prevent instantiation
    private ItemsAdderIntegration() {}

    public static void compile() throws IOException {
        if(!isItemsAdderEnabled()) return;
        createImages();
        createItemsAdderConfig();
    }

    public static boolean isItemsAdderEnabled() {
        // Check if the ItemsAdder plugin is enabled
        return Bukkit.getPluginManager().isPluginEnabled("ItemsAdder");
    }

    // Creates the image files for each relic tier
    private static void createImages() throws IOException {

        String path = Bukkit.getPluginManager().getPlugin("ItemsAdder").getDataFolder() + "/contents/reliclabels/textures";

        // Delete the existing folder if it exists
        File folder = new File(path);
        deleteFolder(folder);


        // Loop through each relic tier and create images
        for (RelicTier tier : RelicTier.getTiers()) {
            createImage(tier, path, "titleplaque");
            createImage(tier, path, "topleftcorner");
            createImage(tier, path, "topbar");
            createImage(tier, path, "toprightcorner");
            createImage(tier, path, "titleplaquestart");
            createImage(tier, path, "titleplaqueend");
            createImage(tier, path, "lineborder");
            createImage(tier, path, "line");
            createImage(tier, path, "linestrike");
            createImage(tier, path, "bottomleftcorner");
            createImage(tier, path, "bottombar");
            createImage(tier, path, "bottomrightcorner");
        }

        // Copy additional necessary files to the resource pack
        Files.copy(ResourcePackCompiler.class.getClassLoader().getResourceAsStream("img/reset.png"),
                new File(path + "/reset.png").toPath());
    }

    // Creates a single image file for a relic tier with a given name
    private static void createImage(RelicTier tier, String path, String name) throws IOException {
        File file = new File(RelicLabels.getPlugin().getDataFolder() + "/resources", name + ".png");
        BufferedImage img = ImageIO.read(file);

        // Convert image to ARGB format
        BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = convertedImg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        img = convertedImg;

        // Define colors to be replaced
        Color replacePrimary = new Color(255, 255, 255);
        Color replaceSecondary = new Color(181, 181, 181);
        Color replaceTertiary = new Color(104, 104, 104);

        // Get the colors for the relic tier
        Color primary = tier.getPrimaryColor();
        Color secondary = tier.getSecondaryColor();
        Color tertiary = tier.getThertiaryColor();

        // Loop through all pixels and replace the defined colors
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int rgb = img.getRGB(x, y);
                if (rgb == replacePrimary.getRGB()) {
                    img.setRGB(x, y, primary.getRGB());
                } else if (rgb == replaceSecondary.getRGB()) {
                    img.setRGB(x, y, secondary.getRGB());
                } else if (rgb == replaceTertiary.getRGB()) {
                    img.setRGB(x, y, tertiary.getRGB());
                }
            }
        }

        // Define the output file path
        File out = new File(path, tier.getName() + "/" + name + ".png");

        // Ensure directories and the file exist
        if (!out.getParentFile().exists()) out.getParentFile().mkdirs();
        if (!out.exists()) out.createNewFile();

        // Write the modified image to the output file
        ImageIO.write(img, "png", out);
    }

    /*
    providers.add(createUnicode(tier.getName() + "/topleftcorner.png", 23, 23, baseUnicode + 4 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/topbar.png", 23, 23, baseUnicode + 5 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/toprightcorner.png", 23, 23, baseUnicode + 6 + itter * 12));

            providers.add(createUnicode(tier.getName() + "/titleplaquestart.png", 14, 35, baseUnicode + 7 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/titleplaque.png", 14, 35, baseUnicode + 8 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/titleplaqueend.png", 14, 35, baseUnicode + 9 + itter * 12));

            providers.add(createUnicode(tier.getName() + "/lineborder.png", 9, 10, baseUnicode + 10 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/line.png", 9, 10, baseUnicode + 11 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/linestrike.png", 9, 10, baseUnicode + 12 + itter * 12));

            providers.add(createUnicode(tier.getName() + "/bottomleftcorner.png", 9, 10, baseUnicode + 13 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/bottombar.png", 9, 10, baseUnicode + 14 + itter * 12));
            providers.add(createUnicode(tier.getName() + "/bottomrightcorner.png", 9, 10, baseUnicode + 15 + itter * 12));
     */

    private static void createItemsAdderConfig() throws IOException {
        YamlConfiguration config = new YamlConfiguration();
        config.set("info.namespace", "reliclabels");

        addUnicode(config, 0, "reset.png", (char) BaseConfig.baseUnicode, -6, -1000);
        addUnicode(config, 1, "reset.png", (char) (BaseConfig.baseUnicode + 1), -3, -1000);
        addUnicode(config, 2, "reset.png", (char) (BaseConfig.baseUnicode + 2), -34, -1000);
        addUnicode(config, 3, "reset.png", (char) (BaseConfig.baseUnicode + 3), -9, -1000);

        int itter = 0;
        for(RelicTier tier : RelicTier.getTiers()) {

            addUnicode(config, 4 + itter * 12, tier.getName() + "/topleftcorner.png", (char) (BaseConfig.baseUnicode + 4 + itter * 12), 23, 23);
            addUnicode(config, 5 + itter * 12, tier.getName() + "/topbar.png", (char) (BaseConfig.baseUnicode + 5 + itter * 12), 23, 23);
            addUnicode(config, 6 + itter * 12, tier.getName() + "/toprightcorner.png", (char) (BaseConfig.baseUnicode + 6 + itter * 12), 23, 23);

            addUnicode(config, 7 + itter * 12, tier.getName() + "/titleplaquestart.png", (char) (BaseConfig.baseUnicode + 7 + itter * 12), 35, 14);
            addUnicode(config, 8 + itter * 12, tier.getName() + "/titleplaque.png", (char) (BaseConfig.baseUnicode + 8 + itter * 12), 35, 14);
            addUnicode(config, 9 + itter * 12, tier.getName() + "/titleplaqueend.png", (char) (BaseConfig.baseUnicode + 9 + itter * 12), 35, 14);

            addUnicode(config, 10 + itter * 12, tier.getName() + "/lineborder.png", (char) (BaseConfig.baseUnicode + 10 + itter * 12), 10, 9);
            addUnicode(config, 11 + itter * 12, tier.getName() + "/line.png", (char) (BaseConfig.baseUnicode + 11 + itter * 12), 10, 9);
            addUnicode(config, 12 + itter * 12, tier.getName() + "/linestrike.png", (char) (BaseConfig.baseUnicode + 12 + itter * 12), 10, 9);

            addUnicode(config, 13 + itter * 12, tier.getName() + "/bottomleftcorner.png", (char) (BaseConfig.baseUnicode + 13 + itter * 12), 10, 9);
            addUnicode(config, 14 + itter * 12, tier.getName() + "/bottombar.png", (char) (BaseConfig.baseUnicode + 14 + itter * 12), 10, 9);
            addUnicode(config, 15 + itter * 12, tier.getName() + "/bottomrightcorner.png", (char) (BaseConfig.baseUnicode + 15 + itter * 12), 10, 9);

            itter++;
        }

        File file = new File(Bukkit.getPluginManager().getPlugin("ItemsAdder").getDataFolder() + "/contents/reliclabels", "my_config.yml");
        if(!file.getParentFile().exists()) file.getParentFile().mkdirs();
        config.save(file);

    }

    private static void addUnicode(YamlConfiguration config, int index, String file, char unicode, int height, int ascent) {
        String path = "font_images.reliclabels_" + index + ".";
        config.set(path + "path", file);
        config.set(path + "symbol", unicode);
        config.set(path + "scale_ratio", height);
        config.set(path + "y_position", ascent);
    }

    private static void deleteFolder(File folder) {
        if (folder.exists()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteFolder(file);
                    } else {
                        file.delete();
                    }
                }
            }
        }
        folder.delete();
    }

}
