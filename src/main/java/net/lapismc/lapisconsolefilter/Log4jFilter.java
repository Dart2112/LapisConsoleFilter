package net.lapismc.lapisconsolefilter;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public class Log4jFilter {

    private final LapisConsoleFilter plugin;

    public Log4jFilter(LapisConsoleFilter plugin) {
        this.plugin = plugin;
        setLog4JFilter();
    }

    private void setLog4JFilter() {
        AbstractFilter abstractConsoleLogListener = new AbstractFilter() {

            private Result validateMessage(Message message) {
                if (message == null) {
                    return Result.NEUTRAL;
                }
                return validateMessage(message.getFormattedMessage());
            }

            private Result validateMessage(String message) {
                return shouldBlock(message) ? Result.DENY : Result.ACCEPT;
            }

            @Override
            public Result filter(LogEvent event) {
                Message candidate = null;
                if (event != null) {
                    candidate = event.getMessage();
                }
                return validateMessage(candidate);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
                return validateMessage(msg);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
                String candidate = null;
                if (msg != null) {
                    candidate = msg.toString();
                }
                return validateMessage(candidate);
            }

            @Override
            public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
                return validateMessage(msg);
            }
        };
        Logger logger = (Logger) LogManager.getRootLogger();
        logger.addFilter(abstractConsoleLogListener);
    }

    private boolean shouldBlock(String msg) {
        for (String filter : plugin.getConfig().getStringList("Filters")) {
            if (checkSubStrings(msg, filter.split(","))) {
                return true;
            }
        }
        return false;
    }

    private boolean checkSubStrings(String msg, String[] parts) {
        for (String part : parts) {
            if (!msg.contains(part))
                return false;
        }
        return true;
    }


}
