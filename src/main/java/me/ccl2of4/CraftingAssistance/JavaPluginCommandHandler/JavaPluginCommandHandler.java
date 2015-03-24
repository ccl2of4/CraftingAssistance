package me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by connor on 3/9/15.
 */
public abstract class JavaPluginCommandHandler {

    private static Map<String, JavaPluginCommandHandler> mappings = new HashMap<String, JavaPluginCommandHandler> ();
    private JavaPlugin plugin;

    static {
        mappings.put ("what", new CAWhatCommandHandler ());
        mappings.put ("how", new CAHowCommandHandler ());
        mappings.put ("craft", new CACraftCommandHandler ());
        mappings.put ("craftingassistance", new CAAboutCommandHandler ());
    }

    public abstract void execute (CommandSender sender, Command cmd, String s, String[] args);

    public static JavaPluginCommandHandler getCommandHandler (String name) {
        JavaPluginCommandHandler handler = mappings.get (name.toLowerCase ());
        return handler;
    }

    public void setPlugin (JavaPlugin plugin) { this.plugin = plugin; }
    public JavaPlugin getPlugin () { return plugin; }
}
