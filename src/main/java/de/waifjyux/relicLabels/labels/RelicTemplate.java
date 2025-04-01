package de.waifjyux.relicLabels.labels;

import de.waifjyux.relicLabels.RelicLabels;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class RelicTemplate {

    // List to store all templates
    private static ArrayList<RelicTemplate> templates = new ArrayList<>();

    private Material material;
    private String itemName;
    private int customModelData = -1;
    private String labelName;
    private String labelDescription;
    private int labelWidth;
    private RelicTier tier;
    private List<String> content = new ArrayList<>();

    // Constructor with parameters for material, label name, description, width, and tier
    public RelicTemplate(Material material, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    // Constructor with parameters for material, item name, label name, description, width, and tier
    public RelicTemplate(Material material, String itemName, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.itemName = itemName;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    // Constructor with parameters for material, custom model data, label name, description, width, and tier
    public RelicTemplate(Material material, int customModelData, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.customModelData = customModelData;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    // Constructor with parameters for material, item name, custom model data, label name, description, width, and tier
    public RelicTemplate(Material material, String itemName, int customModelData, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.itemName = itemName;
        this.customModelData = customModelData;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    // Getter for material
    public Material getMaterial() {
        return this.material;
    }

    // Getter for item name
    public String getItemName() {
        return this.itemName;
    }

    // Getter for custom model data
    public int getCustomModelData() {
        return this.customModelData;
    }

    // Getter for label name
    public String getLabelName() {
        return this.labelName;
    }

    // Getter for label description
    public String getLabelDescription() {
        return this.labelDescription;
    }

    // Getter for label width
    public int getLabelWidth() {
        return this.labelWidth;
    }

    // Getter for relic tier
    public RelicTier getTier() {
        return this.tier;
    }

    // Getter for content
    public List<String> getContent() {
        return this.content;
    }

    // Setter for material
    public void setMaterial(Material material) {
        this.material = material;
    }

    // Setter for item name
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    // Setter for custom model data
    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    // Setter for label name
    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    // Setter for label description
    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }

    // Setter for label width
    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    // Setter for relic tier
    public void setTier(RelicTier tier) {
        this.tier = tier;
    }

    // Setter for content
    public void setContent(List<String> content) {
        this.content = content;
    }

    // Method to generate a relic label for an item stack
    public RelicLabel generateLabel(ItemStack stack) {
        // Create a new relic label with given width, name, description, and tier
        RelicLabel label = new RelicLabel(this.labelWidth, this.labelName, this.labelDescription, this.tier);

        // Get enchantments list of the item stack
        List<String> enchantments = getEnchantmentsList(stack);

        // Process each line of the content
        for (String line : this.content) {
            // If line is "/s", add strikethrough
            if (line.equals("/s")) {
                label.addStrikethrough();
                continue;
            }

            // If line is "/es" and enchantments exist, add strikethrough
            if (line.equals("/es") && enchantments.size() > 0) {
                label.addStrikethrough();
                continue;
            }

            // If line starts with "/e" and enchantments exist, add the enchantment to the line
            if (line.startsWith("/e") && enchantments.size() > 0) {
                String format = line.substring(2);
                for (String enchantment : enchantments)
                    label.addAutoLineContent(format.replace("%ENCHANT_NAME%", enchantment));
                continue;
            }

            // If line does not start with "/e", add it as auto-line content
            if (!line.startsWith("/e"))
                label.addAutoLineContent(line);
        }

        return label;
    }

    // Method to get a list of enchantments for an item stack
    private List<String> getEnchantmentsList(ItemStack item) {
        List<String> enchantmentsList = new ArrayList<>();

        // If item is null or doesn't have enchantments, return empty list
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasEnchants())
            return enchantmentsList;

        // Get the map of enchantments for the item
        Map<Enchantment, Integer> enchantments = item.getItemMeta().getEnchants();

        // Loop through the enchantments and format the names
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            String enchantmentName = entry.getKey().getKey().getKey();
            int level = entry.getValue();
            String formattedEnchantment = formatEnchantmentName(enchantmentName) + " " + level;
            enchantmentsList.add(formattedEnchantment);
        }

        return enchantmentsList;
    }

    // Method to format enchantment names to a readable format
    private String formatEnchantmentName(String namespacedKey) {
        String[] parts = namespacedKey.split("_");
        StringBuilder formattedName = new StringBuilder();

        // Capitalize each part of the enchantment name
        for (String part : parts)
            formattedName.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase())
                    .append(" ");

        return formattedName.toString().trim();
    }

    // Method to get itemstack preview
    public ItemStack getPreview() {
        ItemStack stack = new ItemStack(this.material);
        ItemMeta meta = stack.getItemMeta();

        if(customModelData != -1) {
            meta.setCustomModelData(customModelData);
        }

        if(itemName != null) {
            meta.setDisplayName(itemName);
        }

        stack.setItemMeta(meta);

        generateLabel(stack).generate(stack);

        return stack;
    }


    // Method to add a template to the list of templates
    public static void addTemplate(RelicTemplate template) {
        templates.add(template);
    }

    // Method to remove a template from the list
    public static void removeTemplate(RelicTemplate template) {
        templates.remove(template);
    }

    // Method to get a list of all templates
    public static ArrayList<RelicTemplate> getTemplates() {
        return templates;
    }

    // Method to get a template for a given item stack
    public static RelicTemplate getTemplate(ItemStack stack) {
        for (RelicTemplate template : templates) {
            // Check if the template matches the stack's material and custom model data
            if (template.material == stack.getType() && (
                    template.customModelData == -1 || (stack.getItemMeta().hasCustomModelData() && stack.getItemMeta().getCustomModelData() == template.customModelData)) && (
                    template.itemName == null || stack.getItemMeta().getDisplayName().equals(template.itemName)))
                return template;
        }
        return null;
    }

    // Method to load templates from the configuration file
    public static void loadConfig() {
        File file = new File(RelicLabels.getPlugin().getDataFolder(), "templates.yml");

        // If file doesn't exist, create it
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);
        List<?> templates = cfg.getList("templates");
        int loaded = 0;

        try {
            // Loop through each template in the configuration file
            if (templates != null)
                for (Object obj : templates) {
                    if (obj instanceof Map) {
                        Map<?, ?> template = (Map<?, ?>)obj;
                        Object material = template.get("material");
                        String tier = (String)template.get("tier");
                        String labelName = (String)template.get("labelName");
                        String labelDescription = (String)template.get("labelDescription");
                        int size = ((Integer)template.get("labelWidth")).intValue();
                        RelicTier relicTier = RelicTier.getTier(tier);

                        // Check if the tier exists
                        if (relicTier == null) {
                            Bukkit.getLogger().severe("Could not find tier " + tier + " for template " + labelName);
                            continue;
                        }

                        // Get material and create a new RelicTemplate
                        Material mat = Material.matchMaterial((String)material);
                        if (mat == null) {
                            Bukkit.getLogger().severe("Could not find material " + String.valueOf(material) + " for template " + labelName);
                            continue;
                        }

                        RelicTemplate relicTemplate = new RelicTemplate(mat, labelName, labelDescription, size, relicTier);

                        // Set the content for the template
                        List<String> content = (List<String>)template.get("content");
                        if (content != null)
                            relicTemplate.setContent(content);

                        // Set additional properties for the template
                        Object name = template.get("itemName");
                        if (name != null)
                            relicTemplate.setItemName((String)name);

                        Object customModelData = template.get("customModelData");
                        if (customModelData != null)
                            relicTemplate.setCustomModelData(((Integer)customModelData).intValue());

                        addTemplate(relicTemplate);
                        loaded++;
                    }
                }
        } catch (Exception e) {
            Bukkit.getLogger().warning("Error: " + e.getMessage());
            Bukkit.getLogger().severe("Could not load templates.yml. Check for syntax errors or refer to the plugin page!");
        }

        Bukkit.getLogger().info("Loaded " + loaded + " relic templates");
    }

    // Saves the templates configuration to templates.yml
    public static void saveConfig() {
        // Get the configuration file in the plugin's data folder
        File file = new File(RelicLabels.getPlugin().getDataFolder(), "templates.yml");

        // Create a new YamlConfiguration object
        YamlConfiguration cfg = new YamlConfiguration();

        // Create a list to store each template as a map of values
        List<Object> templatesList = new ArrayList<>();

        // Loop through each template in the static templates list
        for (RelicTemplate template : templates) {
            // Create a map to hold the template values
            java.util.Map<String, Object> map = new java.util.HashMap<>();

            // Always include these keys
            map.put("material", template.getMaterial().toString());
            map.put("tier", template.getTier().getName());
            map.put("labelName", template.getLabelName());
            map.put("labelDescription", template.getLabelDescription());
            map.put("labelWidth", template.getLabelWidth());
            map.put("content", template.getContent());

            // Only include 'itemName' if it is not null
            if (template.getItemName() != null) {
                map.put("itemName", template.getItemName());
            }

            // Only include 'customModelData' if it is not the default value (-1)
            if (template.getCustomModelData() != -1) {
                map.put("customModelData", template.getCustomModelData());
            }

            // Add the map to the list of templates
            templatesList.add(map);
        }

        // Set the templates list in the configuration
        cfg.set("templates", templatesList);

        try {
            // Ensure the parent directory exists
            if (!file.getParentFile().exists())
                file.getParentFile().mkdirs();

            // Create the file if it doesn't exist
            if (!file.exists())
                file.createNewFile();

            // Save the configuration to the file
            cfg.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save templates.yml: " + e.getMessage());
        }
    }


}
