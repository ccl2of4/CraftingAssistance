package me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler;

import me.ccl2of4.CraftingAssistance.CraftingAssistanceLogic;
import me.ccl2of4.CraftingAssistance.StringAssembler;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by connor on 3/9/15.
 */
class CAWhatCommandHandler extends JavaPluginCommandHandler {

    @Override
    public void execute (CommandSender sender, Command cmd, String s, String[] args) {
        StringBuilder result = new StringBuilder ();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Block block = CraftingAssistanceLogic.getBlock (player);

            result
                    .append (ChatColor.GRAY)
                    .append ("You are looking at:\n")
                    .append (ChatColor.RESET);
            if (block != null) {
                result
                        .append (ChatColor.YELLOW)
                        .append (StringAssembler.getStringForMaterial (block.getType ()))
                        .append ("\n")
                        .append (ChatColor.RESET);
            } else {
                result
                        .append (ChatColor.RED)
                        .append ("No block is in sight.\n")
                        .append (ChatColor.RESET);
            }
        }

        else {
            result
                    .append (ChatColor.RED)
                    .append ("Only in-game players can issue the \"what\" command.\n")
                    .append (ChatColor.RESET);
        }

        sender.sendMessage (result.toString ());
    }

}
