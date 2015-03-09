package me.ccl2of4.CraftingAssistance;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.*;
import org.bukkit.util.BlockIterator;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Connor on 3/6/15.
 */


public final class CraftingAssistanceLogic {

    private JavaPluginLogger logger;

    public JavaPluginLogger getLogger () { return logger; }
    public void setLogger (JavaPluginLogger logger) { this.logger = logger; }

    public String what (CommandSender sender, Command cmd, String s, String[] args) {
        StringBuilder result = new StringBuilder ();

        if (sender instanceof Player) {
            Player player = (Player) sender;
            Block block = getBlock (player);

            result
                    .append (ChatColor.GRAY)
                    .append ("You are looking at:\n")
                    .append (ChatColor.RESET);
            if (block != null) {
                result
                        .append (ChatColor.YELLOW)
                        .append (RecipeIngredientsFinder.getMaterialDescription (block.getType ()))
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

        return result.toString ();
    }

    public String how (CommandSender sender, Command cmd, String s, String[] args) {
        StringBuilder result = new StringBuilder ();
        Material material = null;
        String error = null;

        // check for an argument first
        if (args.length > 0) {
            String search = args[0];
            material = materialForString (search);
            if (material == null) {
                error = "No match for " + search + "\n";
                List<Material> candidateMaterials = candidateMaterialsForString (search);
                if (candidateMaterials.size () > 0) {
                    error += "Did you mean: \n";
                    Iterator<Material> it = candidateMaterials.iterator ();
                    while (it.hasNext ()) {
                        Material candidateMaterial = it.next ();
                        error += candidateMaterial.toString().toLowerCase ();
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
            Block block = getBlock (player);

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
                    .append (RecipeIngredientsFinder.getMaterialDescription (material))
                    .append (ChatColor.RESET);

            List<String> recipes = getRecipes (material);
            Iterator<String> iterator = recipes.iterator ();
            while (iterator.hasNext ()) {
                String recipe = iterator.next ();
                result
                        .append ("\n----------------------------------\n")
                        .append (recipe);
                if (iterator.hasNext ())
                    result.append ("\n----------------------------------");
                result.append ("\n");
            }
        }

        // report any error
        else if (error != null) {
            result
                    .append (ChatColor.RED)
                    .append (error)
                    .append (ChatColor.RESET);
        }

        return result.toString ();
    }

    private Block getBlock (Player player) {
        BlockIterator it = new BlockIterator (player, 10);

        Block block = null;
        while (it.hasNext ()) {
            block = it.next ();
            if (block.getType () != Material.AIR)
                break;
            block = null;
        }
        return block;
    }

    private List<String> getRecipes (Material material) {
        List<String> result = new LinkedList<String> ();
        List<Recipe> recipes = Bukkit.getRecipesFor (new ItemStack (material));

        if (recipes.size () > 0) {
            for (Recipe recipe : recipes) {
                result.add (RecipeIngredientsFinder.findIngredients (recipe));
            }
        } else {
            result.add (ChatColor.RED + "This material is not craftable.\n" + ChatColor.RESET);
        }

        return result;
    }

    private static Material materialForString (String string) {
        Material[] enums = Material.class.getEnumConstants ();
        for (Material material : enums) {
            if (material.toString().equalsIgnoreCase (string))
                return material;
        }
        return null;
    }

    private static List<Material> candidateMaterialsForString (String string) {
        Material[] enums = Material.class.getEnumConstants ();
        List<Material> result = new LinkedList<Material> ();
        for (Material material : enums) {
            if (material.toString().toLowerCase ().contains (string.toLowerCase ()))
                result.add (material);
        }
        return result;
    }
}

