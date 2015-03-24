package me.ccl2of4.CraftingAssistance;

/**
 * Created by Connor Lirot on 3/5/2015.
 */
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler.JavaPluginCommandHandler;

import java.io.File;

public final class CraftingAssistancePlugin extends JavaPlugin
{
    private JavaPluginLogger logger;
    private CraftingAssistanceLogic logic;

    // Called when the server is being disabled.stop
    @Override
    public void onDisable ()
    {
        System.out.println(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " disabled!");
    }

    // Called when the server is being enabled.
    @Override
    public void onEnable ()
    {
         // Set up the logger
        logger = new JavaPluginLogger ();
        logger.setPlugin (this);
        logger.info(this.getDescription().getName() + " version " + this.getDescription().getVersion() + " enabled!");

        // copy the config file if necessary
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists ()) {
            logger.warning("config.yml not found in data folder. Creating default config.yml.");
            saveDefaultConfig();
        }
    }

    // Command handler
    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args) {

        String name = cmd.getName ();

        JavaPluginCommandHandler commandHandler = JavaPluginCommandHandler.getCommandHandler (cmd.getName ());
        if (commandHandler != null) {
            commandHandler.setPlugin (this);
            commandHandler.execute (sender, cmd, s, args);
            return true;
        }
        return false;
    }
}