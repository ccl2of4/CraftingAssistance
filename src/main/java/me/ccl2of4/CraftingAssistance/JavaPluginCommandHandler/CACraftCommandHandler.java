package me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler;

import me.ccl2of4.CraftingAssistance.CraftingAssistanceLogic;
import me.ccl2of4.CraftingAssistance.StringAssembler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Connor on 3/15/15.
 */

public class CACraftCommandHandler extends JavaPluginCommandHandler {

    private static final String CraftingTableMaxDistanceKey = "crafting_table_max_distance";

    @Override
    public void execute (CommandSender sender, Command cmd, String s, String[] args) {
        StringBuilder result = new StringBuilder ();
        Material material = null;
        String error = null;
        int num = 0;

        // command can only be executed by player
        if (! (sender instanceof Player) ) {
            return;
        }

        Player player = (Player)sender;

        // player must be near a crafting table
        int maxDistance = getCraftingTableMaxDistance ();
        int distance = CraftingAssistanceLogic.getDistanceFromCraftingTable (player, maxDistance);
        if (distance == -1) {
            sender.sendMessage (ChatColor.RED + "You must be near a crafting table to use /craft." + ChatColor.RESET);
            return;
        }

        if (args.length == 0) {
            sender.sendMessage (ChatColor.RED + usage () + ChatColor.RESET);
            return;
        }

        // what material are we crafting?
        else {
            String search = args[0];
            material = CraftingAssistanceLogic.getMaterialForString(search);
            if (material == null) {
                error = "No match for " + search + "\n";
                List<Material> candidateMaterials = CraftingAssistanceLogic.getCandidateMaterialsForString(search);
                if (candidateMaterials.size() > 0) {
                    error += "Did you mean: \n";
                    Iterator<Material> it = candidateMaterials.iterator();
                    while (it.hasNext()) {
                        Material candidateMaterial = it.next();

                        error += StringAssembler.getStringForMaterial(candidateMaterial);
                        if (it.hasNext())
                            error += ", ";
                    }
                    error += "?\n";
                }
            }
        }


        if (error == null) {

            // how many are we crafting ?
            if (args.length > 1) {
                try {
                    num = Integer.parseInt (args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage (ChatColor.RED + usage () + ChatColor.RESET);
                    return;
                }
            } else {
                num = 1;
            }

            // finally do the crafting
            if (material != null) {
                int numCrafted = CraftingAssistanceLogic.craftMaterials (material, num, player.getInventory ());
                result  .append ("Crafted ")
                        .append (Integer.toString (numCrafted))
                        .append (" ")
                        .append (StringAssembler.getStringForMaterial (material));
            }
        }

        // show error
        else {
            if (error != null) {
                sender.sendMessage (ChatColor.RED + error + ChatColor.RESET);
                return;
            }
        }

        sender.sendMessage (result.toString ());
    }

    private int getCraftingTableMaxDistance () {
        JavaPlugin plugin = getPlugin ();
        if (plugin == null) {
            throw new IllegalStateException ("CACraftCommandHandler cannot handle commands without having a plugin assigned to it.");
        }

        FileConfiguration config = plugin.getConfig ();
        int value = config.getInt (CraftingTableMaxDistanceKey);

        return value;
    }

    private static String usage () {
        return "USAGE: /craft <material> [num]";
    }
}
