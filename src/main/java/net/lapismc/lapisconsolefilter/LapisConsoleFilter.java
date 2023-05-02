package net.lapismc.lapisconsolefilter;

import net.lapismc.lapiscore.LapisCorePlugin;
import net.lapismc.lapiscore.utils.LapisCoreFileWatcher;

public final class LapisConsoleFilter extends LapisCorePlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        fileWatcher = new LapisCoreFileWatcher(this);
        super.onEnable();
        new Log4jFilter(this);
    }

    @Override
    public void onDisable() {
        fileWatcher.stop();
        super.onDisable();
    }
}
