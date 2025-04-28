package de.waifjyux.relicLabels.commands;

import de.waifjyux.relicLabels.labels.RelicTemplate;
import de.waifjyux.relicLabels.labels.RelicTier;
import de.waifjyux.relicLabels.util.ItemsAdderIntegration;
import de.waifjyux.relicLabels.util.ResourcePackCompiler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import net.md_5.bungee.api.chat.hover.content.Item;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IllegalFormatCodePointException;
import java.util.List;

public class RelicLabelsCommand implements CommandExecutor, TabCompleter {

    // Prefix for messages
    private static String pre = "§8[§3RL§8]§7 ";

    // Method to handle commands
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        // Check if the sender has permission to execute the command
        if (!sender.hasPermission("reliclabels.command")) {
            sender.sendMessage(pre + "§cYou do not have permission to execute this command!");
            return false;
        }

        // Ensure at least one argument is provided (the subcommand)
        if (args.length >= 1) {

            // Handle the "listtiers" subcommand: List available relic tiers
            if (args[0].equalsIgnoreCase("listtiers")) {
                sender.sendMessage(pre + "§7Available Relic Tiers:");
                for (RelicTier tier : RelicTier.getTiers()) {
                    sender.sendMessage(" §7- " + ChatColor.of(Color.decode(tier.getHexColor())) + tier.getName());
                }
            }
            // Handle the "addtier" subcommand: Add a new relic tier
            else if (args[0].equalsIgnoreCase("addtier")) {
                if (args.length == 3) {
                    String name = args[1];
                    String hexColor = args[2];

                    // Check if the tier already exists
                    if (RelicTier.getTier(name) != null) {
                        sender.sendMessage(pre + "§cA tier with this name already exists!");
                        return false;
                    }

                    // Add the new tier and save the configuration
                    RelicTier.addTier(new RelicTier(name, hexColor));
                    RelicTier.saveConfig();
                    sender.sendMessage(pre + "Successfully added tier!");
                } else {
                    sender.sendMessage(pre + "Usage: /reliclabels addtier <name> <hexColor>");
                }
            }
            // Handle the "removetier" subcommand: Remove an existing relic tier
            else if (args[0].equalsIgnoreCase("removetier")) {
                if (args.length == 2) {
                    String name = args[1];

                    // Get the tier and remove it if it exists
                    RelicTier tier = RelicTier.getTier(name);
                    if (tier == null) {
                        sender.sendMessage(pre + "§cA tier with this name does not exist!");
                        return false;
                    }

                    RelicTier.removeTier(tier);
                    RelicTier.saveConfig();
                    sender.sendMessage(pre + "Successfully removed tier!");
                } else {
                    sender.sendMessage(pre + "Usage: /reliclabels removetier <name>");
                }
            }
            // Handle the "compile" subcommand: Compile the resource pack
            else if (args[0].equalsIgnoreCase("compile")) {
                sender.sendMessage(pre + "§7Compiling the resource pack...");
                long startTime = System.nanoTime();
                try {
                    if(ItemsAdderIntegration.isItemsAdderEnabled()) {
                        ItemsAdderIntegration.compile();
                    }else ResourcePackCompiler.compile();
                } catch (Exception e) {
                    e.printStackTrace();
                    sender.sendMessage(pre + "§cAn error occurred while compiling the resource pack!");
                    return false;
                }
                double duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
                sender.sendMessage(pre + String.format("§aSuccessfully compiled in %.3fs", duration));
            }
            // Handle the "tier" subcommand: Modify or view tier information
            else if (args[0].equalsIgnoreCase("tier")) {
                // Modify tier properties (set name or set color)
                if (args.length == 4) {
                    String name = args[1];
                    String property = args[2];
                    String value = args[3];

                    // Get the tier to modify
                    RelicTier tier = RelicTier.getTier(name);
                    if (tier == null) {
                        sender.sendMessage(pre + "§cA tier with this name does not exist!");
                        return false;
                    }

                    // Change the tier's name
                    if (property.equalsIgnoreCase("setname")) {
                        tier.setName(value);
                        RelicTier.saveConfig();
                        sender.sendMessage(pre + "Successfully set the name of the tier!");
                    }
                    // Change the tier's color
                    if (property.equalsIgnoreCase("setcolor")) {
                        tier.setHexColor(value);
                        RelicTier.saveConfig();
                        sender.sendMessage(pre + "Successfully set the color of the tier!");
                    }
                }
                // Display tier information
                else if (args.length == 3) {
                    String name = args[1];
                    String property = args[2];

                    // If the property is not "info", show usage message
                    if (!property.equalsIgnoreCase("info")) {
                        sender.sendMessage(pre + "Usage: /reliclabels tier <name> [setname/setcolor] [value]");
                        sender.sendMessage(pre + "Usage: /reliclabels tier <name> info");
                        return false;
                    }

                    // Get and display the tier information
                    RelicTier tier = RelicTier.getTier(name);
                    if (tier == null) {
                        sender.sendMessage(pre + "§cA tier with this name does not exist!");
                        return false;
                    }
                    sender.sendMessage(pre + "§7Tier Information: " + tier.getName());
                    sender.sendMessage(" §7- " + ChatColor.of(tier.getPrimaryColor()) + "Color: " + tier.getHexColor());
                    sender.sendMessage(" §7- " + ChatColor.of(tier.getSecondaryColor()) + "Secondary Color");
                    sender.sendMessage(" §7- " + ChatColor.of(tier.getThertiaryColor()) + "Thertiary Color");
                } else {
                    sender.sendMessage(pre + "Usage: /reliclabels tier <name> [setname/setcolor] [value]");
                    sender.sendMessage(pre + "Usage: /reliclabels tier <name> info");
                }
            }
            // Handle the "listtemplates" subcommand: List available relic templates
            else if (args[0].equalsIgnoreCase("listtemplates")) {
                sender.sendMessage(pre + "§7Available Relic Templates:");
                int counter = 0;
                for (RelicTemplate template : RelicTemplate.getTemplates()) {
                    counter++;
                    String message = " §8[§3" + counter + "§8] §7" + template.getMaterial() + " §8- ";

                    // Build a clickable text component for the template
                    TextComponent component = new TextComponent(message);
                    TextComponent nameComponent = new TextComponent(template.getLabelName().equalsIgnoreCase("") ? "UNKNOWN" : template.getLabelName());
                    nameComponent.setColor(ChatColor.of(template.getTier().getPrimaryColor()));

                    component.addExtra(nameComponent);
                    component.setHoverEvent(new HoverEvent(
                            HoverEvent.Action.SHOW_TEXT,
                            new ComponentBuilder("§7Click to view more information").create()
                    ));
                    component.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                            net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND,
                            "/reliclabels template " + counter + " info"
                    ));

                    sender.spigot().sendMessage(component);
                }
                sender.sendMessage("");
            }
            // Handle the "addtemplate" subcommand: Add a new relic template
            else if (args[0].equalsIgnoreCase("addtemplate")) {
                if (args.length == 3) {
                    Material material = Material.matchMaterial(args[1]);
                    if (material == null) {
                        sender.sendMessage(pre + "§cInvalid material!");
                        return false;
                    }

                    RelicTier tier = RelicTier.getTier(args[2]);
                    if (tier == null) {
                        sender.sendMessage(pre + "§cInvalid tier!");
                        return false;
                    }

                    RelicTemplate.addTemplate(new RelicTemplate(material, "", "", 5, tier));
                    RelicTemplate.saveConfig();
                    sender.sendMessage(pre + "Successfully added template!");
                } else {
                    sender.sendMessage(pre + "Usage: /reliclabels addtemplate <material> <tier>");
                }
            }
            // Handle the "removetemplate" subcommand: Remove an existing relic template
            else if (args[0].equalsIgnoreCase("removetemplate")) {
                if (args.length == 2) {
                    int index = Integer.parseInt(args[1]);
                    RelicTemplate template = RelicTemplate.getTemplates().get(index + 1);

                    if (template == null) {
                        sender.sendMessage(pre + "§cInvalid index!");
                        return false;
                    }

                    RelicTemplate.removeTemplate(template);
                    RelicTemplate.saveConfig();
                    sender.sendMessage(pre + "Successfully removed template!");
                } else {
                    sender.sendMessage(pre + "Usage: /reliclabels removetemplate <index>");
                }
            }
            // Handle the "template" subcommand: Modify or view information about a template
            else if (args[0].equalsIgnoreCase("template")) {
                if (args.length <= 2) {
                    sender.sendMessage(pre + "§cUsage: /reliclabels template <index> <property> <value>");
                    sender.sendMessage(pre + "§cUsage: /reliclabels template <index> info");
                    return false;
                }

                // Display template information if "info" property is specified
                if (args[2].equalsIgnoreCase("info")) {
                    if (args.length == 3) {
                        int index;
                        try {
                            index = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(pre + "§cInvalid index!");
                            return false;
                        }

                        RelicTemplate template = RelicTemplate.getTemplates().get(index - 1);
                        if (template == null) {
                            sender.sendMessage(pre + "§cInvalid index!");
                            return false;
                        }

                        sender.sendMessage(pre + "§7Template Information: ");
                        printTemplateInformation(sender, template);
                    }
                }
                // Handle "getitem" property: Give the player the preview item from the template
                else if (args[2].equalsIgnoreCase("getitem")) {
                    if (args.length == 3) {
                        int index;
                        try {
                            index = Integer.parseInt(args[1]);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(pre + "§cInvalid index!");
                            return false;
                        }

                        RelicTemplate template = RelicTemplate.getTemplates().get(index - 1);
                        if (template == null) {
                            sender.sendMessage(pre + "§cInvalid index!");
                            return false;
                        }

                        if(!(sender instanceof Player)) {
                            sender.sendMessage(pre + "§cYou must be a player to receive the item!");
                            return false;
                        }

                        ItemStack item = template.getPreview();
                        Player player = (Player) sender;
                        player.getInventory().addItem(item);
                        sender.sendMessage(pre + "§aSuccessfully added the item to your inventory!");
                    }
                }
                // Modify template properties
                else if (args.length >= 4) {
                    int index = Integer.parseInt(args[1]);
                    String property = args[2];
                    String value = args[3];

                    // Validate the index
                    if (index > RelicTemplate.getTemplates().size() || index < 0) {
                        sender.sendMessage(pre + "§cInvalid index!");
                        return false;
                    }

                    RelicTemplate template = RelicTemplate.getTemplates().get(index - 1);
                    if (template == null) {
                        sender.sendMessage(pre + "§cInvalid index!");
                        return false;
                    }

                    // Set the material for the template
                    if (property.equalsIgnoreCase("setmaterial")) {
                        Material material = Material.matchMaterial(value);
                        if (material == null) {
                            sender.sendMessage(pre + "§cInvalid material!");
                            return false;
                        }
                        template.setMaterial(material);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the material of the template!");
                    }
                    // Set the tier for the template
                    else if (property.equalsIgnoreCase("settier")) {
                        RelicTier tier = RelicTier.getTier(value);
                        if (tier == null) {
                            sender.sendMessage(pre + "§cInvalid tier!");
                            return false;
                        }
                        template.setTier(tier);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the tier of the template!");
                    }
                    // Set the label width for the template
                    else if (property.equalsIgnoreCase("setWidth")) {
                        int width;
                        try {
                            width = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(pre + "§cInvalid width!");
                            return false;
                        }
                        template.setLabelWidth(width);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the width of the template!");
                    }
                    // Set the custom model data filter for the template
                    else if (property.equalsIgnoreCase("setcustommodeldata")) {
                        int modelData;
                        try {
                            modelData = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(pre + "§cInvalid modelData!");
                            return false;
                        }
                        if (modelData < -1) {
                            sender.sendMessage(pre + "§cModel data must be bigger than -1!");
                            return false;
                        }
                        template.setCustomModelData(modelData);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the custom model data filter of the template!");
                    }
                    // Set the item name filter for the template
                    else if (property.equalsIgnoreCase("setitemname")) {
                        value = "";
                        for (int i = 3; i < args.length; i++) {
                            value += args[i] + " ";
                        }
                        value = value.trim();
                        if (value.equalsIgnoreCase("%NULL%")) {
                            value = null;
                        }
                        template.setItemName(value);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the item name filter of the template!");
                    }
                    // Set the label name for the template
                    else if (property.equalsIgnoreCase("setlabelname")) {
                        value = "";
                        for (int i = 3; i < args.length; i++) {
                            value += args[i] + " ";
                        }
                        value = value.trim().replaceAll("&([0-9a-fA-F])", "§$1");
                        template.setLabelName(value);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the name of the template!");
                    }
                    // Set the label description for the template
                    else if (property.equalsIgnoreCase("setlabeldescription")) {
                        value = "";
                        for (int i = 3; i < args.length; i++) {
                            value += args[i] + " ";
                        }
                        value = value.trim().replaceAll("&([0-9a-fA-F])", "§$1");
                        template.setLabelDescription(value);
                        RelicTemplate.saveConfig();
                        sender.sendMessage(pre + "Successfully set the description of the template!");
                    }
                    // Set or remove a lore line for the template
                    else if (property.equalsIgnoreCase("setLore")) {
                        if (args.length >= 5) {
                            int line;
                            try {
                                line = Integer.parseInt(args[3]) - 1;
                            } catch (NumberFormatException e) {
                                sender.sendMessage(pre + "§cInvalid line!");
                                return false;
                            }
                            if (line < 0) {
                                sender.sendMessage(pre + "§cLine must be bigger than 0!");
                                return false;
                            }
                            // Ensure the line exists or create it
                            if (line >= template.getContent().size()) {
                                line = template.getContent().size();
                                template.getContent().add("");
                            }

                            String lore = "";
                            for (int i = 4; i < args.length; i++) {
                                lore += args[i] + " ";
                            }
                            lore = lore.trim().replaceAll("&([0-9a-fA-F])", "§$1");

                            if (lore.equals("%REMOVE%")) {
                                template.getContent().remove(line);
                            } else {
                                template.getContent().set(line, lore);
                            }
                            RelicTemplate.saveConfig();
                            sender.sendMessage(pre + "Successfully set the lore of the template!");
                        } else {
                            sender.sendMessage(pre + "§cUsage: /reliclabels template <index> setLore <line> <value>");
                        }
                    }
                    // Unknown property provided
                    else {
                        sender.sendMessage(pre + "§cUnknown property!");
                    }
                } else {
                    sender.sendMessage(pre + "§cUnknown property!");
                }
            }
            // Unknown subcommand handling
            else {
                sender.sendMessage(pre + "§cUnknown subcommand!");
            }
        } else {
            sender.sendMessage(pre + "§cUsage: /reliclabels <subcommand>");
        }

        return false;
    }


    // Method to handle tab completion for commands
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        // Provide tab completion for the first argument
        if(args.length == 1) {
            return filterCompletion(args[0], List.of("listtiers", "addtier", "removetier", "compile", "tier", "listtemplates", "addtemplate", "removetemplate", "template"));
        }
        // Provide tab completion for second argument if it's a tier-related command
        else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("tier")) {
                return filterCompletion(args[1], RelicTier.getTiers().stream().map(RelicTier::getName).toList());
            } else if(args[0].equalsIgnoreCase("removetier")) {
                return filterCompletion(args[1], RelicTier.getTiers().stream().map(RelicTier::getName).toList());
            } else if(args[0].equalsIgnoreCase("addtemplate")) {
                return filterCompletion(args[1], Arrays.stream(Material.values()).toList().stream().map(Enum::name).toList());
            }
        }
        // Provide tab completion for third argument if it's a tier modification command
        else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("tier")) {
                return filterCompletion(args[2], List.of("setname", "setcolor", "info"));
            }else if(args[0].equalsIgnoreCase("template")) {
                return filterCompletion(args[2], List.of("setmaterial", "settier", "setwidth", "setcustommodeldata", "setitemname", "setlabelname", "setlabeldescription", "info"));
            }else if(args[0].equalsIgnoreCase("addtemplate")) {
                return filterCompletion(args[2], RelicTier.getTiers().stream().map(RelicTier::getName).toList());
            }
        }

        else if(args.length == 4) {
            if(args[0].equalsIgnoreCase("template")) {
                if(args[2].equalsIgnoreCase("setmaterial")) {
                    return filterCompletion(args[3], Arrays.stream(Material.values()).toList().stream().map(Enum::name).toList());
                }else if(args[2].equalsIgnoreCase("settier")) {
                    return filterCompletion(args[3], RelicTier.getTiers().stream().map(RelicTier::getName).toList());
                }
            }
        }

        return List.of();
    }

    private List<String> filterCompletion(String arg, List<String> list) {
        List<String> filteredList = new ArrayList<>();
        for(String item : list) {
            if (item.toLowerCase().startsWith(arg.toLowerCase())) {
                filteredList.add(item);
            }
        }
        return filteredList;
    }

    private void printTemplateInformation(CommandSender sender, RelicTemplate template) {
        // Get the template index (1-based)
        int index = RelicTemplate.getTemplates().indexOf(template) + 1;

        // Print an empty line for spacing
        sender.sendMessage("");

        // Print the template ID
        sender.sendMessage("§7 - ID §8: §3" + index);

        // --- Material Information ---
        TextComponent material = new TextComponent("§7 - Material §8: §7" + template.getMaterial());
        material.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the material").create()
        ));
        material.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setmaterial "
        ));
        sender.spigot().sendMessage(material);

        // --- Item Name Filter ---
        TextComponent itemName = new TextComponent("§7 - Item Name §8: §7" +
                (template.getItemName() == null ? "NOT SET" : template.getItemName()));
        itemName.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the item name or keep \"NOT SET\" to ignore the filter").create()
        ));
        itemName.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setitemname "
        ));
        // Create a removal component if item name is set
        TextComponent itemNameRemove = new TextComponent("§c [✖]");
        itemNameRemove.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to remove the item name").create()
        ));
        itemNameRemove.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setitemname %NULL%"
        ));
        if (template.getItemName() != null) {
            itemName.addExtra(itemNameRemove);
        }
        sender.spigot().sendMessage(itemName);

        // --- Custom Model Data Filter ---
        TextComponent customModelData = new TextComponent("§7 - Custom Model Data §8: §7" +
                (template.getCustomModelData() == -1 ? "NOT SET" : template.getCustomModelData()));
        customModelData.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the custom model data or keep \"NOT SET\" to ignore the filter").create()
        ));
        customModelData.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setcustommodeldata "
        ));
        // Create a removal component if custom model data is set
        TextComponent customModelDataRemove = new TextComponent("§c [✖]");
        customModelDataRemove.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to remove the custom model data").create()
        ));
        customModelDataRemove.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setcustommodeldata -1"
        ));
        if (template.getCustomModelData() != -1) {
            customModelData.addExtra(customModelDataRemove);
        }
        sender.spigot().sendMessage(customModelData);

        // --- Tier Information ---
        TextComponent tier = new TextComponent("§7 - Tier §8: ");
        TextComponent tierName = new TextComponent(template.getTier().getName());
        tierName.setColor(ChatColor.of(template.getTier().getPrimaryColor()));
        tier.addExtra(tierName);
        tier.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the tier").create()
        ));
        tier.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " settier "
        ));
        sender.spigot().sendMessage(tier);

        // --- Label Width ---
        TextComponent width = new TextComponent("§7 - Width §8: §7" + template.getLabelWidth());
        width.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the width").create()
        ));
        width.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setwidth "
        ));
        sender.spigot().sendMessage(width);

        // --- Label Name ---
        TextComponent labelName = new TextComponent("§7 - Label Name §8:§r " + template.getLabelName());
        labelName.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the label name").create()
        ));
        labelName.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setlabelname " + template.getLabelName().replaceAll("§", "&")
        ));
        sender.spigot().sendMessage(labelName);

        // --- Label Description ---
        TextComponent labelDescription = new TextComponent("§7 - Label Description §8:§r " + template.getLabelDescription());
        labelDescription.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to change the label description").create()
        ));
        labelDescription.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setlabeldescription " + template.getLabelDescription().replaceAll("§", "&")
        ));
        sender.spigot().sendMessage(labelDescription);

        // --- Template Lore ---
        sender.sendMessage("");
        sender.sendMessage("§7 - Lore:");
        for (int i = 0; i < template.getContent().size(); i++) {
            // Create removal component for the lore line
            TextComponent remove = new TextComponent("§c [-]");
            remove.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§7Click to remove this line").create()
            ));
            remove.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    "/reliclabels template " + index + " setLore " + (i + 1) + " %REMOVE%"
            ));

            // Create lore line component
            TextComponent lore = new TextComponent("§7 - " + (i + 1) + "§8:§r " + template.getContent().get(i));
            lore.setHoverEvent(new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder("§7Click to change this line").create()
            ));
            lore.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    "/reliclabels template " + index + " setLore " + (i + 1) + " " +
                            template.getContent().get(i).replaceAll("§", "&")
            ));

            // Combine removal and lore line components
            remove.addExtra(lore);
            sender.spigot().sendMessage(remove);
        }

        // --- Option to Add a New Lore Line ---
        TextComponent add = new TextComponent("§a [+] Add new line");
        add.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to add a new line").create()
        ));
        add.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.SUGGEST_COMMAND,
                "/reliclabels template " + index + " setLore " + (template.getContent().size() + 1) + " "
        ));
        sender.spigot().sendMessage(add);

        sender.sendMessage("");

        // --- Preview of the Template ---
        String lorePreview = "";
        List<String> lore = template.generateLabel(new ItemStack(template.getMaterial())).generateLore();
        for (String line : lore) {
            lorePreview += line + "\n";
        }
        TextComponent preview = new TextComponent("§6[Preview / get Item]");
        preview.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder(lorePreview).create()
        ));
        preview.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/reliclabels template " + index + " getitem"
        ));
        sender.spigot().sendMessage(preview);

        // --- Reload Info Page ---
        TextComponent reload = new TextComponent("§a[Reload info page]");
        reload.setHoverEvent(new HoverEvent(
                HoverEvent.Action.SHOW_TEXT,
                new ComponentBuilder("§7Click to reload the template info").create()
        ));
        reload.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(
                ClickEvent.Action.RUN_COMMAND,
                "/reliclabels template " + index + " info"
        ));
        sender.spigot().sendMessage(reload);

        // Final spacing
        sender.sendMessage("");
    }


}
