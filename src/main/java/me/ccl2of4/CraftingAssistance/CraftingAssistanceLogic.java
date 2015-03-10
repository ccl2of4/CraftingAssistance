package me.ccl2of4.CraftingAssistance;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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

    public static Block getBlock (Player player) {
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

    public static List<Recipe> getRecipes (Material material) {
        List<Recipe> result = new LinkedList<Recipe> ();
        List<Recipe> recipes = Bukkit.getRecipesFor (new ItemStack (material));

        if (recipes.size () > 0) {
            for (Recipe recipe : recipes) {
                result.add (recipe);
            }
        }

        return result;
    }

    public static Material getMaterialForString (String string) {
        Material[] enums = Material.class.getEnumConstants ();
        for (Material material : enums) {
            if (material.toString().equalsIgnoreCase (string))
                return material;
        }
        return null;
    }

    public static List<Material> getCandidateMaterialsForString (String string) {
        Material[] enums = Material.class.getEnumConstants ();
        List<Material> result = new LinkedList<Material> ();
        for (Material material : enums) {
            if (material.toString().toLowerCase ().contains (string.toLowerCase ()))
                result.add (material);
        }
        return result;
    }
}

