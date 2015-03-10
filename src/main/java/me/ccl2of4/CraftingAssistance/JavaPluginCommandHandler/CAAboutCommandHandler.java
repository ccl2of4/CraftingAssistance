package me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * Created by connor on 3/9/15.
 */
public class CAAboutCommandHandler extends JavaPluginCommandHandler {
    public void execute (CommandSender sender, Command cmd, String s, String[] args) {
        sender.sendMessage ("Made by ccl2of4 - https://github.com/ccl2of4/CraftingAssistance");
    }
}
