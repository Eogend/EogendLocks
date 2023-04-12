package fr.lavapower.eogendlocks;

import fr.lavapower.eogendlocks.command.LockCommand;
import fr.lavapower.eogendlocks.listener.LockListener;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

public class EogendLocks extends JavaPlugin {
    private Logger lockLogger;

    @Override
    public void onEnable() {
        // Register command
        registerCommand("lock", new LockCommand(this));

        // Register listener
        getServer().getPluginManager().registerEvents(new LockListener(), this);

        // Init logger
        initLogger();
    }

    public Logger getLockLogger() {
        return lockLogger;
    }

    private void initLogger() {
        lockLogger = Logger.getLogger("LocksLogger");
        lockLogger.setLevel(Level.ALL);
        try {
            Files.createDirectories(Path.of(getDataFolder().getPath(), "logs"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
            FileHandler fileHandler = new FileHandler(getDataFolder().getPath() + "/logs/" + dateFormat.format(System.currentTimeMillis())+".log", true);
            fileHandler.setFormatter(new SimpleFormatter() {
                private static final String FORMAT = "[%1$tF %1$tT] %2$s %n";

                @Override
                public synchronized String format(LogRecord lr)
                {
                    return String.format(FORMAT, new Date(lr.getMillis()), lr.getMessage());
                }
            });
            lockLogger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private <T extends CommandExecutor & TabCompleter> void registerCommand(String name, T command)
    {
        PluginCommand pluginCommand = getCommand(name);
        pluginCommand.setExecutor(command);
        pluginCommand.setTabCompleter(command);
    }
}
