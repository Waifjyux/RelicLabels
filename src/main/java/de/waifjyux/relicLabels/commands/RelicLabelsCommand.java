package de.waifjyux.relicLabels.commands;

import de.waifjyux.relicLabels.labels.RelicTier;
import de.waifjyux.relicLabels.util.ResourcePackCompiler;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.awt.*;
import java.util.List;

public class RelicLabelsCommand implements CommandExecutor, TabCompleter {

    private static String pre = "§8[§3RL§8]§7 ";

    @Override
    public boolean onCommand(CommandSender p, Command command, String s, String[] args) {

        if(!p.hasPermission("reliclabels.command")) {
            p.sendMessage(pre + "§cYou do not have permission to execute this command!");
            return false;
        }

        if(args.length >= 1) {

            if(args[0].equalsIgnoreCase("listtiers")) {
                p.sendMessage(pre + "§7Available Relic Tiers:");
                for(RelicTier tier : RelicTier.getTiers()) {
                    p.sendMessage(" §7- " + ChatColor.of(Color.decode(tier.getHexColor())) + tier.getName());
                }
            }

            else if(args[0].equalsIgnoreCase("addtier")) {
                if(args.length == 3) {
                    String name = args[1];
                    String hexColor = args[2];

                    if(RelicTier.getTier(name) != null) {
                        p.sendMessage(pre + "§cA tier with this name already exists!");
                        return false;
                    }

                    RelicTier.addTier(new RelicTier(name, hexColor));
                    RelicTier.saveConfig();
                    p.sendMessage(pre + "Successfully added tier!");
                } else {
                    p.sendMessage(pre + "Usage: /reliclabels addtier <name> <hexColor>");
                }
            }

            else if(args[0].equalsIgnoreCase("removetier")) {
                if(args.length == 2) {
                    String name = args[1];

                    RelicTier tier = RelicTier.getTier(name);
                    if(tier == null) {
                        p.sendMessage(pre + "§cA tier with this name does not exist!");
                        return false;
                    }

                    RelicTier.removeTier(tier);
                    RelicTier.saveConfig();
                    p.sendMessage(pre + "Successfully removed tier!");
                } else {
                    p.sendMessage(pre + "Usage: /reliclabels removetier <name>");
                }
            }

            else if(args[0].equalsIgnoreCase("compile")) {
                p.sendMessage(pre + "§7Compiling the resource pack...");
                long startTime = System.nanoTime();
                try {
                    ResourcePackCompiler.compile();
                } catch (Exception e) {
                    e.printStackTrace();
                    p.sendMessage(pre + "§cAn error occurred while compiling the resource pack!");
                    return false;
                }
                double duration = (System.nanoTime() - startTime) / 1_000_000_000.0;
                p.sendMessage(pre + String.format("§aSuccessfully compiled in %.3fs", duration));
            }

            else if(args[0].equalsIgnoreCase("tier")) {
                if(args.length == 4) {
                    String name = args[1];
                    String property = args[2];
                    String value = args[3];

                    RelicTier tier = RelicTier.getTier(name);
                    if(tier == null) {
                        p.sendMessage(pre + "§cA tier with this name does not exist!");
                        return false;
                    }

                    if(property.equalsIgnoreCase("setname")) {
                        tier.setName(value);
                        RelicTier.saveConfig();
                        p.sendMessage(pre + "Successfully set the name of the tier!");
                    }

                    if(property.equalsIgnoreCase("setcolor")) {
                        tier.setHexColor(value);
                        RelicTier.saveConfig();
                        p.sendMessage(pre + "Successfully set the color of the tier!");
                    }
                }

                else if(args.length == 3) {
                    String name = args[1];
                    String property = args[2];

                    if(!property.equalsIgnoreCase("info")) {
                        p.sendMessage(pre + "Usage: /reliclabels tier <name> [setname/setcolor] [value]");
                        p.sendMessage(pre + "Usage: /reliclabels tier <name> info");
                        return false;
                    }

                    RelicTier tier = RelicTier.getTier(name);
                    if(tier == null) {
                        p.sendMessage(pre + "§cA tier with this name does not exist!");
                        return false;
                    }

                    p.sendMessage(pre + "§7Tier Information: " + tier.getName());
                    p.sendMessage(" §7- " + ChatColor.of(tier.getPrimaryColor()) + "Color: " + tier.getHexColor());
                    p.sendMessage(" §7- " + ChatColor.of(tier.getSecondaryColor()) + "Secondary Color");
                    p.sendMessage(" §7- " + ChatColor.of(tier.getThertiaryColor()) + "Thertiary Color");
                } else {
                    p.sendMessage(pre + "Usage: /reliclabels tier <name> [setname/setcolor] [value]");
                    p.sendMessage(pre + "Usage: /reliclabels tier <name> info");
                }
            }

            else {
                p.sendMessage(pre + "§cUnknown subcommand!");
            }

        }else p.sendMessage(pre + "§cUsage: /reliclabels <subcommand>");

        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {

        if(args.length == 1) {
            return List.of("listtiers", "addtier", "removetier", "compile", "tier");
        }else if(args.length == 2) {
            if(args[0].equalsIgnoreCase("tier")) {
                return RelicTier.getTiers().stream().map(RelicTier::getName).toList();
            }else if(args[0].equalsIgnoreCase("removetier")) {
                return RelicTier.getTiers().stream().map(RelicTier::getName).toList();
            }
        }else if(args.length == 3) {
            if(args[0].equalsIgnoreCase("tier")) {
                return List.of("setname", "setcolor", "info");
            }
        }


        return List.of();
    }
}
