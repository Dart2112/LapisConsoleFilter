package net.lapismc.lapisconsolefilter.filters;

import net.lapismc.lapisconsolefilter.FilterManager;
import net.lapismc.lapisconsolefilter.LapisConsoleFilter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Filter;

public class JavaLoggerFilter extends FilterManager {

    public JavaLoggerFilter(LapisConsoleFilter plugin) {
        super(plugin);
        registerJavaFilter();
    }

    public void registerJavaFilter() {
        Filter filter = record -> {
            String msg = record.getMessage();
            return shouldBlock(msg);
        };
        Bukkit.getLogger().setFilter(filter);
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            pl.getLogger().setFilter(filter);
        }
    }

}
