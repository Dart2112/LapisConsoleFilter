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
        //runTestingMessages();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        fileWatcher.stop();
        super.onDisable();
    }

    private void runTestingMessages() {
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            sendDelayedMessage("WiredNetworks (/176.65.148.220:63148) lost connection: Disconnected", 2);
            sendDelayedMessage("/176.65.148.221:56066 lost connection: Internal Exception: io.netty.handler.codec." +
                    "DecoderException: Failed to decode packet 'serverbound/minecraft:hello'", 4);
            sendDelayedMessage("dart2112 lost connection: Disconnected", 6);
        }, 1, 20 * 5);
    }

    /**
     * Used to send messages at different times for testing
     *
     * @param msg   The message to send
     * @param delay How many ticks to delay it
     */
    private void sendDelayedMessage(String msg, long delay) {
        Bukkit.getScheduler().runTaskLater(this, () -> getLogger().info(msg), delay);
    }
}
