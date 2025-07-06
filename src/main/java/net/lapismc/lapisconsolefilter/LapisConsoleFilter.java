package net.lapismc.lapisconsolefilter;

import net.lapismc.lapisconsolefilter.filters.Log4JFilter;
import net.lapismc.lapiscore.LapisCorePlugin;
import net.lapismc.lapiscore.utils.LapisCoreFileWatcher;
import org.bukkit.Bukkit;

public final class LapisConsoleFilter extends LapisCorePlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fileWatcher = new LapisCoreFileWatcher(this);
        FilterManager manager = new FilterManager(this);
        new Log4JFilter(manager).initialize();
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, manager::processLogs, 20 * 60, 20 * 60);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        fileWatcher.stop();
        super.onDisable();
    }
}
