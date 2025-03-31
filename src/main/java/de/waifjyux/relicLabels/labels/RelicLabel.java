package de.waifjyux.relicLabels.labels;

import de.waifjyux.relicLabels.RelicLabels;
import de.waifjyux.relicLabels.util.BaseConfig;
import de.waifjyux.relicLabels.util.BitmapGlyphInfo;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.awt.*;
import java.util.ArrayList;

public class RelicLabel {

    private int size; // The size of the label
    private ArrayList<String> content = new ArrayList<>(); // List holding the content of the label
    private int index; // Index for the tier of the label

    // Constructor to create a RelicLabel with a specific size, name, lore, and tier
    public RelicLabel(int size, String nameLine, String loreLine, RelicTier tier) {
        this(size, nameLine, loreLine, RelicTier.getTiers().indexOf(tier));
    }

    // Overloaded constructor to create a RelicLabel with size, name, lore, and tier index
    public RelicLabel(int size, String nameLine, String loreLine, int index) {

        this.size = size;
        this.index = index;

        // Standard distance between characters and pixel size in chat
        String topLine = "§r" + white
                + (char) BaseConfig.baseUnicode // Resets the line with 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 4 + 12 * index) // Draws the top left corner
                + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel

        // String to reset the top line
        String reset = "";

        // Draws the top line to cover the default Minecraft item name
        for(int i = 0; i < size; i++) {
            topLine += ""
                    + (char) (BaseConfig.baseUnicode + 5 + 12 * index) // Draws the top line
                    + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel
            reset += (char) (BaseConfig.baseUnicode + 2) + ""; // Resets by the top bar width
        }

        reset += "" + (char) (BaseConfig.baseUnicode + 3); // Resets by 3 mc pixels

        // Draws the top right corner
        topLine += ""
                + (char) (BaseConfig.baseUnicode + 6 + 12 * index) // Draws the top right corner
                + (char) (BaseConfig.baseUnicode + 1) // Resets by 1 mc pixel
                + reset // Resets the top line to the start
                + (char) (BaseConfig.baseUnicode + 7 + 12 * index) // Draws the start of the plaque
                + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel

        // Draws the plaque in the top line
        for(int i = 0; i < size; i++) {
            topLine += ""
                    + (char) (BaseConfig.baseUnicode + 8 + 12 * index) // Draws the plaque
                    + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel
        }

        // Finalizes the top line
        topLine += ""
                + (char) (BaseConfig.baseUnicode + 9 + 12 * index) // Draws the end of the plaque
                + (char) (BaseConfig.baseUnicode + 1) // Resets by 1 mc pixel
                + reset // Resets the top line to the start
                + white + " §l" + nameLine; // Adds the name in bold

        content.add(topLine); // Adds the top line to the content

        content.add("§r" + white + loreLine); // Adds lore to the content
        content.add(""); // Empty line for spacing
    }

    // Method to add content to the label
    public RelicLabel addContent(String content) {

        String line = "§r" + white
                + (char) BaseConfig.baseUnicode // Resets line with 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 10 + 12 * index) // Draws the left border
                + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel

        String reset = "";

        // Creates the background line for the content
        for(int i = 0; i < size; i++) {
            line += ""
                    + (char) (BaseConfig.baseUnicode + 11 + 12 * index) // Draws the background of the line
                    + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel
            reset += (char) (BaseConfig.baseUnicode + 2) + ""; // Resets by the background width
        }

        // Finalizes the content line with borders and the content text
        line += ""
                + (char) (BaseConfig.baseUnicode + 10 + 12 * index) // Draws the right border
                + reset // Resets the line to the start
                + "§r" + white + content; // Adds the content to the line

        this.content.add(line); // Adds the content line to the label

        return this; // Returns the RelicLabel instance
    }

    // Method to add auto-wrapped content based on the given size
    public RelicLabel addAutoLineContent(String content) {

        String[] lines = null;

        // Choose the method based on the experimental setting
        if(BaseConfig.experimentalChatDistribution) {
            lines = BitmapGlyphInfo.getExperimentalChatDistribution(content, size * 32);
        } else {
            lines = BitmapGlyphInfo.getChatDistribution(content, size * 32);
        }

        // Add each line to the content
        for(String line : lines) {
            addContent(line);
        }

        return this; // Returns the RelicLabel instance
    }

    // Method to add a strikethrough line to the label
    public RelicLabel addStrikethrough() {

        String line = "§r" + white
                + (char) BaseConfig.baseUnicode // Resets line with 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 10 + 12 * index) // Draws the left border
                + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel

        // Creates the strikethrough background line
        for(int i = 0; i < size; i++) {
            line += ""
                    + (char) (BaseConfig.baseUnicode + 12 + 12 * index) // Draws the strikethrough line background
                    + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel
        }

        // Finalizes the strikethrough line with right border
        line += "" + (char) (BaseConfig.baseUnicode + 10 + 12 * index); // Draws the right border

        this.content.add(line); // Adds the strikethrough line to the label

        return this; // Returns the RelicLabel instance
    }

    // Private method to add the final bottom line to the label
    private void addFinalLine() {

        String finalLine = "§r" + white
                + (char) BaseConfig.baseUnicode // Resets line with 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 13 + 12 * index) // Draws the left border
                + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel

        // Creates the bottom line background
        for(int i = 0; i < size; i++) {
            finalLine += ""
                    + (char) (BaseConfig.baseUnicode + 14 + 12 * index) // Draws the bottom line
                    + (char) (BaseConfig.baseUnicode + 1); // Resets by 1 mc pixel
        }

        // Finalizes the bottom line with right border
        finalLine += "" + (char) (BaseConfig.baseUnicode + 15 + 12 * index); // Draws the right border

        content.add(finalLine); // Adds the final bottom line to the content
    }

    // Method to get the lore (content) of the RelicLabel
    public ArrayList<String> getLore() {
        return content;
    }

    // Method to generate and return the full lore of the label
    public ArrayList<String> generateLore() {
        addFinalLine(); // Adds the final bottom line
        return content; // Returns the generated content (lore)
    }

    // Method to generate the ItemStack with the lore and attributes
    public ItemStack generate(ItemStack stack) {

        ItemMeta meta = stack.getItemMeta(); // Get the item's metadata

        if(meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(stack.getType()); // Create new meta if it doesn't exist
        }

        addFinalLine(); // Add the final line to the lore

        // Set the item's lore and hide enchantments and attributes
        meta.setLore(content);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        stack.setItemMeta(meta); // Set the modified metadata to the item

        return stack; // Return the modified item
    }

    // White color constant for the label
    private static final String white = ChatColor.of(new Color(255,255,255)).toString();

    // Static method to update an item stack with the RelicLabel
    public static void updateItemStack(ItemStack stack) {

        if(stack == null) return;

        RelicTemplate template = RelicTemplate.getTemplate(stack); // Get the template for the item
        if(template == null) return;

        RelicLabel label = template.generateLabel(stack); // Generate the label from the template

        if(label == null) return;

        label.generate(stack); // Apply the label to the item

    }
}
