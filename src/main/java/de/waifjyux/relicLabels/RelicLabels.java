package de.waifjyux.relicLabels;

import de.waifjyux.relicLabels.commands.RelicLabelsCommand;
import de.waifjyux.relicLabels.events.RelicLabelEvents;
import de.waifjyux.relicLabels.labels.RelicLabel;
import de.waifjyux.relicLabels.labels.RelicTemplate;
import de.waifjyux.relicLabels.labels.RelicTier;
import de.waifjyux.relicLabels.util.BaseConfig;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class RelicLabels extends JavaPlugin {

    // The main plugin instance
    private static RelicLabels plugin;

    @Override
    public void onEnable() {

        // Assign the plugin instance to the static variable
        plugin = this;

        // Set up the command executor for "reliclabels"
        getCommand("reliclabels").setExecutor(new RelicLabelsCommand());

        // Register the event listeners for relic labels
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new RelicLabelEvents(), this);

        // Load configuration files for BaseConfig, RelicTier, and RelicTemplate
        BaseConfig.loadConfig();
        RelicTier.loadConfig();
        RelicTemplate.loadConfig();

        // Update all items in player inventories on startup, if the setting is enabled
        if(BaseConfig.updateOnStartup) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.getInventory().forEach(i -> {
                    RelicLabel.updateItemStack(i); // Update the item stack for relics
                });
            });
        }

        // Log the plugin logo to the console on enable
        Bukkit.getLogger().info("\n"+
                "  _____      _ _        _           _          _     \n" +
                " |  __ \\    | (_)      | |         | |        | |    \n" +
                " | |__) |___| |_  ___  | |     __ _| |__   ___| |___ \n" +
                " |  _  // _ \\ | |/ __| | |    / _` | '_ \\ / _ \\ / __|\n" +
                " | | \\ \\  __/ | | (__  | |___| (_| | |_) |  __/ \\__ \\\n" +
                " |_|  \\_\\___|_|_|\\___| |______\\__,_|_.__/ \\___|_|___/\n" );
    }

    @Override
    public void onDisable() {
        RelicTier.saveConfig();
        RelicTemplate.saveConfig();
        Bukkit.getLogger().info("RelicLabels disabled!");
    }

    // Getter for the plugin instance
    public static RelicLabels getPlugin() {
        return plugin;
    }
}
