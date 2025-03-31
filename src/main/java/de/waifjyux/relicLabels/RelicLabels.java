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

    private static RelicLabels plugin;

    @Override
    public void onEnable() {

        plugin = this;

        getCommand("reliclabels").setExecutor(new RelicLabelsCommand());

        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new RelicLabelEvents(), this);

        BaseConfig.loadConfig();
        RelicTier.loadConfig();
        RelicTemplate.loadConfig();

        if(BaseConfig.updateOnStartup) {
            Bukkit.getOnlinePlayers().forEach(p -> {
                p.getInventory().forEach(i -> {
                    RelicLabel.updateItemStack(i);
                });
            });
        }

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
        Bukkit.getLogger().info( "RelicLabels disabled!");
    }

    public static RelicLabels getPlugin() {
        return plugin;
    }

}
