package me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by connor on 3/9/15.
 */
public abstract class JavaPluginCommandHandler {

    private static Map<String, JavaPluginCommandHandler> mappings = new HashMap<String, JavaPluginCommandHandler> ();

    static {
        mappings.put ("what", new CAWhatCommandHandler ());
        mappings.put ("how", new CAHowCommandHandler ());
        mappings.put ("craftingassistance", new CAAboutCommandHandler ());
    }

    public abstract void execute (CommandSender sender, Command cmd, String s, String[] args);

    public static JavaPluginCommandHandler getCommandHandler (String name) {
        JavaPluginCommandHandler handler = mappings.get (name.toLowerCase ());
        return handler;
    }

    protected JavaPluginCommandHandler () {}
}
