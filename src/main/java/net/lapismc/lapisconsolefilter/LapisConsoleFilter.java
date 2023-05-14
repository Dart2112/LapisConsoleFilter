package net.lapismc.lapisconsolefilter;

import net.lapismc.lapisconsolefilter.filters.JavaLoggerFilter;
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
        new Log4JFilter(this).initialize();
        JavaLoggerFilter javaFilter = new JavaLoggerFilter(this);
        Bukkit.getScheduler().runTask(this, javaFilter::registerJavaFilter);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, manager::processLogs, 20 * 60, 20 * 60);
        super.onEnable();
    }

    @Override
    public void onDisable() {
        fileWatcher.stop();
        super.onDisable();
    }
}
