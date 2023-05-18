package net.lapismc.lapisconsolefilter.filters;

import net.lapismc.lapisconsolefilter.FilterManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.util.logging.Filter;

public class JavaLoggerFilter {

    private final FilterManager manager;

    public JavaLoggerFilter(FilterManager manager) {
        this.manager = manager;
        registerJavaFilter();
    }

    public void registerJavaFilter() {
        Filter filter = record -> {
            String msg = record.getMessage();
            return manager.shouldBlock(msg);
        };
        Bukkit.getLogger().setFilter(filter);
        for (Plugin pl : Bukkit.getPluginManager().getPlugins()) {
            pl.getLogger().setFilter(filter);
        }
    }

}
