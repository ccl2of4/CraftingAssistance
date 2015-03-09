package me.ccl2of4.CraftingAssistance;

/**
 * Created by Connor Lirot on 3/5/2015.
 */
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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

        // set up the listener
        logic = new CraftingAssistanceLogic ();
        logic.setLogger(logger);
    }

    // Command handler
    @Override
    public boolean onCommand (CommandSender sender, Command cmd, String s, String[] args) {

        String name = cmd.getName ();

        if ("CraftingAssistance".equalsIgnoreCase (name)) {
            sender.sendMessage ("Made by ccl2of4 - https://github.com/ccl2of4/CraftingAssistance");
            return true;
        }

        else {
            StringBuilder message = new StringBuilder ();
            message
                    .append ("=========")
                    .append (ChatColor.DARK_AQUA)
                    .append ("CraftingAssistance")
                    .append (ChatColor.RESET)
                    .append ("=========\n");

            if ("what".equalsIgnoreCase (name)) {
                message.append (logic.what (sender, cmd, s, args));
            } else if ("how".equalsIgnoreCase (name)) {
                message.append (logic.how (sender, cmd, s, args));
            }

            message.append ("==================================\n");
            sender.sendMessage (message.toString ());
        }

        return false;
    }
}