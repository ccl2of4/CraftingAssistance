package me.ccl2of4.CraftingAssistance.JavaPluginCommandHandler;

import me.ccl2of4.CraftingAssistance.CraftingAssistanceLogic;
import me.ccl2of4.CraftingAssistance.StringAssembler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;

import java.util.Iterator;
import java.util.List;

/**
 * Created by connor on 3/9/15.
 */
class CAHowCommandHandler extends JavaPluginCommandHandler {

    @Override
    public void execute (CommandSender sender, Command cmd, String s, String[] args) {
        StringBuilder result = new StringBuilder ();
        Material material = null;
        String error = null;

        // check for an argument first
        if (args.length > 0) {
            String search = args[0];
            material = CraftingAssistanceLogic.getMaterialForString (search);
            if (material == null) {
                error = "No match for " + search + "\n";
                List<Material> candidateMaterials = CraftingAssistanceLogic.getCandidateMaterialsForString (search);
                if (candidateMaterials.size () > 0) {
                    error += "Did you mean: \n";
                    Iterator<Material> it = candidateMaterials.iterator ();
                    while (it.hasNext ()) {
                        Material candidateMaterial = it.next ();

                        error += StringAssembler.getStringForMaterial (candidateMaterial);
                        if (it.hasNext ())
                            error += ", ";
                    }
                    error += "?\n";
                }
            }
        }

        // otherwise check to see if the sender is a player and looking at a block
        else if (sender instanceof Player) {
            Player player = (Player) sender;
            Block block = CraftingAssistanceLogic.getBlock (player);

            if (block == null) {
                error = "No block is in sight\n";
            } else {
                material = block.getType ();
            }
        }

        // figure out how to craft the material
        result
                .append (ChatColor.GRAY)
                .append ("How to craft:\n")
                .append (ChatColor.RESET);
        if (material != null) {
            result
                    .append (ChatColor.YELLOW)
                    .append (StringAssembler.getStringForMaterial (material))
                    .append (ChatColor.RESET);

            List<Recipe> recipes = CraftingAssistanceLogic.getRecipes (material);
            if (recipes.size () > 0) {
                Iterator<Recipe> iterator = recipes.iterator ();
                while (iterator.hasNext ()) {
                    Recipe recipe = iterator.next ();
                    result
                            .append ("\n----------------------------------\n")
                            .append (StringAssembler.getStringForRecipe (recipe));
                    if (iterator.hasNext ())
                        result.append ("\n----------------------------------");
                    result.append ("\n");
                }
            } else {
                result
                        .append ("\n----------------------------------\n")
                        .append ("This material is not craftable");

            }
        }

        // report any error
        else if (error != null) {
            result
                    .append (ChatColor.RED)
                    .append (error)
                    .append (ChatColor.RESET);
        }

        sender.sendMessage (result.toString ());
    }
}
