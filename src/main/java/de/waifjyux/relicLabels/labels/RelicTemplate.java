package de.waifjyux.relicLabels.labels;

import de.waifjyux.relicLabels.RelicLabels;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RelicTemplate {

    private static ArrayList<RelicTemplate> templates = new ArrayList<>();

    private Material material;
    private String itemName;
    private int customModelData = -1;

    private String labelName;
    private String labelDescription;
    private int labelWidth;
    private RelicTier tier;

    private List<String> content;

    public RelicTemplate(Material material, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    public RelicTemplate(Material material, String itemName, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.itemName = itemName;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    public RelicTemplate(Material material, int customModelData, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.customModelData = customModelData;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    public RelicTemplate(Material material, String itemName, int customModelData, String labelName, String labelDescription, int labelWidth, RelicTier tier) {
        this.material = material;
        this.itemName = itemName;
        this.customModelData = customModelData;
        this.labelName = labelName;
        this.labelDescription = labelDescription;
        this.labelWidth = labelWidth;
        this.tier = tier;
    }

    public Material getMaterial() {
        return material;
    }

    public String getItemName() {
        return itemName;
    }

    public int getCustomModelData() {
        return customModelData;
    }

    public String getLabelName() {
        return labelName;
    }

    public String getLabelDescription() {
        return labelDescription;
    }

    public int getLabelWidth() {
        return labelWidth;
    }

    public RelicTier getTier() {
        return tier;
    }

    public List<String> getContent() {
        return content;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setCustomModelData(int customModelData) {
        this.customModelData = customModelData;
    }

    public void setLabelName(String labelName) {
        this.labelName = labelName;
    }

    public void setLabelDescription(String labelDescription) {
        this.labelDescription = labelDescription;
    }

    public void setLabelWidth(int labelWidth) {
        this.labelWidth = labelWidth;
    }

    public void setTier(RelicTier tier) {
        this.tier = tier;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }

    public RelicLabel generateLabel(ItemStack stack) {

        RelicLabel label = new RelicLabel(labelWidth, labelName, labelDescription, tier);

        List<String> enchantments = getEnchantmentsList(stack);

        for(String line : content) {
            if(line.equals("/s")) {
                label.addStrikethrough();
            }else if(line.equals("/es") && enchantments.size() > 0) {
                label.addStrikethrough();
            }else if(line.startsWith("/e") && enchantments.size() > 0) {
                String format = line.substring(2);
                for(String enchantment : enchantments) {
                    label.addAutoLineContent(format.replace("%ENCHANT_NAME%", enchantment));
                }
            }else if(!line.startsWith("/e")) label.addAutoLineContent(line);
        }

        return label;
    }

    private List<String> getEnchantmentsList(ItemStack item) {
        List<String> enchantmentsList = new ArrayList<>();
        if (item == null || !item.hasItemMeta() || !item.getItemMeta().hasEnchants()) {
            return enchantmentsList;
        }
        Map<Enchantment, Integer> enchantments = item.getItemMeta().getEnchants();
        for (Map.Entry<Enchantment, Integer> entry : enchantments.entrySet()) {
            String enchantmentName = entry.getKey().getKey().getKey();
            int level = entry.getValue();
            String formattedEnchantment = formatEnchantmentName(enchantmentName) + " " + level;
            enchantmentsList.add(formattedEnchantment);
        }
        return enchantmentsList;
    }

    private String formatEnchantmentName(String namespacedKey) {
        String[] parts = namespacedKey.split("_");
        StringBuilder formattedName = new StringBuilder();
        for (String part : parts) {
            formattedName.append(part.substring(0, 1).toUpperCase())
                    .append(part.substring(1).toLowerCase())
                    .append(" ");
        }
        return formattedName.toString().trim();
    }

    public static void addTemplate(RelicTemplate template) {
        templates.add(template);
    }

    public static void removeTemplate(RelicTemplate template) {
        templates.remove(template);
    }

    public static RelicTemplate getTemplate(ItemStack stack) {
        for(RelicTemplate template : templates) {
            if(template.material == stack.getType()) {
                if(template.customModelData == -1 || stack.getItemMeta().getCustomModelData() == template.customModelData) {
                    if(template.itemName == null || stack.getItemMeta().getDisplayName().equals(template.itemName)) {
                        return template;
                    }
                }
            }
        }
        return null;
    }

    public static void loadConfig() {

        File file = new File(RelicLabels.getPlugin().getDataFolder(), "templates.yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            }catch(IOException e) {
                throw new RuntimeException(e);
            }
            return;
        }

        YamlConfiguration cfg = YamlConfiguration.loadConfiguration(file);

        List<?> templates = cfg.getList("templates");

        int loaded = 0;

        try {
            if (templates != null) {
                for (Object obj : templates) {
                    if (obj instanceof Map) {
                        Map<?, ?> template = (Map<?, ?>) obj;

                        Object material = template.get("material");

                        String tier = (String) template.get("tier");
                        String labelName = (String) template.get("labelName");
                        String labelDescription = (String) template.get("labelDescription");
                        int size = (int) template.get("labelWidth");

                        RelicTier relicTier = RelicTier.getTier(tier);
                        if (relicTier == null) {
                            Bukkit.getLogger().severe("Could not find tier " + tier + " for template " + labelName);
                            continue;
                        }

                        Material mat = Material.matchMaterial((String) material);
                        if (mat == null) {
                            Bukkit.getLogger().severe("Could not find material " + material + " for template " + labelName);
                            continue;
                        }

                        RelicTemplate relicTemplate = new RelicTemplate(mat, labelName, labelDescription, size, relicTier);

                        List<String> content = (List<String>) template.get("content");
                        if(content != null) relicTemplate.setContent(content);

                        Object name = template.get("itemName");
                        if(name != null) relicTemplate.setItemName((String) name);

                        Object customModelData = template.get("customModelData");
                        if(customModelData != null) relicTemplate.setCustomModelData((int) customModelData);

                        RelicTemplate.addTemplate(relicTemplate);
                        loaded++;

                    }
                }
            }
        }catch(Exception e) {
            Bukkit.getLogger().warning("Error: " + e.getMessage());
            Bukkit.getLogger().severe("Could not load templates.yml. Check for syntax errors or refer to the plugin page!");
        }

        Bukkit.getLogger().info("Loaded " + loaded + " relic templates");

    }

}
