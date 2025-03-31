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

    private int size;
    private ArrayList<String> content = new ArrayList<>();
    private int index;

    public RelicLabel(int size, String nameLine, String loreLine, RelicTier tier) {
        this(size, nameLine, loreLine, RelicTier.getTiers().indexOf(tier));
    }

    public RelicLabel(int size, String nameLine, String loreLine, int index) {

        this.size = size;
        this.index = index;

        //3 pixels is the standard distance between two characters and the size of a single pixel in chat = 1 mc pixel
        String topLine = "§r" + white
                + (char) BaseConfig.baseUnicode//resets line 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 4 + 12 * index)//draws the top left corner
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
        ;

        //resets the topLine to the start
        String reset = "";

        //draws the top line that covers the default minecraft item name
        for(int i = 0; i < size; i++) {
            topLine += ""
                    + (char) (BaseConfig.baseUnicode + 5 + 12 * index)//draws the top line
                    + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
            ;
            reset += (char) (BaseConfig.baseUnicode + 2) + "";//resets by the topbar width
        }

        reset += "" + (char) (BaseConfig.baseUnicode + 3);//resets by 3 mc pixels

        //draws the top right corner
        topLine += ""
                + (char) (BaseConfig.baseUnicode + 6 + 12 * index)//draws the top right corner
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
                + reset//resets the top line to the start
                + (char) (BaseConfig.baseUnicode + 7 + 12 * index)//draws the start of the plaque
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
        ;

        for(int i = 0; i < size; i++) {
            topLine += ""
                    + (char) (BaseConfig.baseUnicode + 8 + 12 * index)//draws the plaque
                    + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
            ;
        }

        topLine += ""
                + (char) (BaseConfig.baseUnicode + 9 + 12 * index)//draws the end of the plaque
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
                + reset//resets the top line to the start
                + white + " §l" + nameLine
        ;

        content.add(topLine);

        content.add("§r" + white + loreLine);
        content.add("");

    }

    public RelicLabel addContent(String content) {

        String line = "§r" + white
                + (char) BaseConfig.baseUnicode//resets line 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 10 + 12 * index)//draws the left border
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
        ;

        String reset = "";

        for(int i = 0; i < size; i++) {
            line += ""
                    + (char) (BaseConfig.baseUnicode + 11 + 12 * index)//draws the line background
                    + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
            ;
            reset += (char) (BaseConfig.baseUnicode + 2) + "";//resets by the background width
        }

        line += ""
                + (char) (BaseConfig.baseUnicode + 10 + 12 * index)//draws the right border
                + reset//resets the line to the start
                + "§r" + white + content;


        this.content.add(line);

        return this;
    }

    public RelicLabel addAutoLineContent(String content) {

        String[] lines = null;
        if(BaseConfig.experimentalChatDistribution) {
            lines = BitmapGlyphInfo.getExperimentalChatDistribution(content, size);
        }else lines = BitmapGlyphInfo.getChatDistribution(content, size);

        for(String line : lines) {
            addContent(line);
        }

        return this;
    }

    public RelicLabel addStrikethrough() {

        String line = "§r" + white
                + (char) BaseConfig.baseUnicode//resets line 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 10 + 12 * index)//draws the left border
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
                ;

        for(int i = 0; i < size; i++) {
            line += ""
                    + (char) (BaseConfig.baseUnicode + 12 + 12 * index)//draws the strikethrough line background
                    + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
            ;
        }

        line += "" + (char) (BaseConfig.baseUnicode + 10 + 12 * index);//draws the right border

        this.content.add(line);

        return this;
    }

    private void addFinalLine() {

        String finalLine = "§r" + white
                + (char) BaseConfig.baseUnicode//resets line 2 mc pixels
                + (char) (BaseConfig.baseUnicode + 13 + 12 * index)//draws the left border
                + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
        ;

        for(int i = 0; i < size; i++) {
            finalLine += ""
                    + (char) (BaseConfig.baseUnicode + 14 + 12 * index)//draws the bottom line
                    + (char) (BaseConfig.baseUnicode + 1)//resets by 1 mc pixels
            ;
        }

        finalLine += "" + (char) (BaseConfig.baseUnicode + 15 + 12 * index);//draws the right border

        content.add(finalLine);
    }

    public ArrayList<String> getLore() {
        return content;
    }

    public ArrayList<String> generateLore() {
        addFinalLine();
        return content;
    }

    public ItemStack generate(ItemStack stack) {

        ItemMeta meta = stack.getItemMeta();

        if(meta == null) {
            meta = Bukkit.getItemFactory().getItemMeta(stack.getType());
        }

        addFinalLine();

        meta.setLore(content);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        stack.setItemMeta(meta);

        return stack;
    }

    private static final String white = ChatColor.of(new Color(255,255,255)).toString();

    public static void updateItemStack(ItemStack stack) {

        if(stack == null) return;

        RelicTemplate template = RelicTemplate.getTemplate(stack);
        if(template == null) return;
        RelicLabel label = template.generateLabel(stack);

        if(label == null) return;

        label.generate(stack);

    }


}
