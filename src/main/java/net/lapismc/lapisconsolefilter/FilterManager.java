package net.lapismc.lapisconsolefilter;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.List;

public class FilterManager {

    private final LapisConsoleFilter plugin;
    //Long is the UTC time that a message was logged, String is the IPAddress pulled from the log
    private static final HashMap<Long, String> ipAddresses = new HashMap<>();
    private List<String> filters;

    public FilterManager(LapisConsoleFilter plugin) {
        this.plugin = plugin;
        filters = plugin.getConfig().getStringList("Filters");
    }

    void processLogs() {
        //The number of times each IP Address is logged
        HashMap<String, Integer> numberOfLogs = new HashMap<>();
        for (String ipAddress : ipAddresses.values()) {
            if (numberOfLogs.containsKey(ipAddress)) {
                //Increment int
                numberOfLogs.put(ipAddress, numberOfLogs.get(ipAddress) + 1);
            } else {
                //Add value to map
                numberOfLogs.put(ipAddress, 1);
            }
        }
        Integer threshold = plugin.getConfig().getInt("Threshold");
        List<String> filters = plugin.getConfig().getStringList("Filters");
        boolean wasChanged = false;
        for (String ipAddress : numberOfLogs.keySet()) {
            if (numberOfLogs.get(ipAddress) >= threshold) {
                //Create a new filter with this IP
                filters.add("lost connection:," + ipAddress);
                plugin.getLogger().info("Added a filter for IP Address " + ipAddress + " because of failed connection spam");
                wasChanged = true;
                Bukkit.getScheduler().runTask(plugin, () -> ipAddresses.values().removeIf(s -> s.equals(ipAddress)));
            }
        }
        if (wasChanged) {
            plugin.getConfig().set("Filters", filters);
            this.filters = filters;
            plugin.saveConfig();
        }
        int minutesToStore = plugin.getConfig().getInt("MinutesToStore");
        //Remove old logs
        ipAddresses.keySet().removeIf(time -> time < System.currentTimeMillis() - ((long) minutesToStore * 60 * 1000));
    }

    public boolean shouldBlock(String msg) {
        boolean shouldBlock = false;
        for (String filter : filters) {
            if (checkSubStrings(msg, filter.split(","))) {
                shouldBlock = true;
            }
        }
        if (!shouldBlock && msg.contains("lost connection:")) {
            //This is a message of a player not really joining that might need to  be hidden if they are repeating
            //Example:
            //WiredNetworks (/176.65.148.220:27146) lost connection: Disconnected
            //Extract the IP address from the string
            String ipAddress = msg.replace(") lost connection: Disconnected", "")
                    .replace(" lost connection: Internal Exception: io.netty.handler.codec.DecoderException:" +
                            " Failed to decode packet 'serverbound/minecraft:hello'", "");
            ipAddress = ipAddress.substring(ipAddress.lastIndexOf("/") + 1, ipAddress.indexOf(":"));
            //Store the IP Address and the current time into a hashmap
            ipAddresses.put(System.currentTimeMillis(), ipAddress);
        }
        return shouldBlock;
    }

    private boolean checkSubStrings(String msg, String[] parts) {
        for (String part : parts) {
            if (!msg.contains(part))
                return false;
        }
        return true;
    }

}
