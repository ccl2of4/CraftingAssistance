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

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by Connor on 3/6/15.
 */


public final class CraftingAssistanceLogic {

    private JavaPluginLogger logger;

    public JavaPluginLogger getLogger () { return logger; }
    public void setLogger (JavaPluginLogger logger) { this.logger = logger; }

    public static int craftMaterials (Material material, int num, Inventory inventory) {
        assert (material != null);
        assert (num >= 0);
        assert (inventory != null);

        int result = 0;
        List<Recipe> recipes = getRecipes (material);

        if (recipes.size () > 0) {
            while (result < num) {
                for (Recipe recipe : recipes) {
                    ItemStack output = recipe.getResult ();
                    assert (output.getType () == material);

                    if (craftRecipe (recipe, inventory)) {
                        result += output.getAmount ();
                        break;
                    } else {
                        return result;
                    }

                }
            }
        }

        return result;
    }

    public static boolean craftRecipe (Recipe recipe, Inventory inventory) {

        ItemStack output = recipe.getResult ();
        List<ItemStack> ingredients = getIngredients (recipe);
        Map<Material, Integer> requiredMaterials = new HashMap<Material, Integer> ();

        // only support shaped and shapeless recipes
        if (ingredients == null) {
            return false;
        }

        // collect all ItemStacks together
        for (ItemStack itemStack : ingredients) {
            Material material = itemStack.getType ();
            Integer currentRequiredAmount = requiredMaterials.get (material);
            int requiredAmount = itemStack.getAmount ();
            requiredMaterials.put (material,
                    (currentRequiredAmount == null ? 0 : currentRequiredAmount) + requiredAmount);
        }

        // check to see if inventory can craft the recipe
        for (Material material : requiredMaterials.keySet ()) {
            int requiredAmount = requiredMaterials.get (material);
            if (!inventory.contains (material, requiredAmount)) return false;
        }

        // remove appropriate materials from inventory
        for (Material material : requiredMaterials.keySet ()) {
            int requiredAmount = requiredMaterials.get (material);

            Map<Integer, ? extends ItemStack> inventoryItemStacks = inventory.all (material);

            for (Integer index : inventoryItemStacks.keySet ()) {
                ItemStack inventoryItemStack = inventoryItemStacks.get (index);
                int invAmount = inventoryItemStack.getAmount ();
                int removed = Math.min (invAmount, requiredAmount);

                if (removed == invAmount) {
                    inventory.clear (index);
                } else {
                    inventoryItemStack.setAmount (invAmount - removed);
                    inventory.setItem (index, inventoryItemStack);
                }

                requiredAmount -= removed;
                if (requiredAmount == 0) break;
            }

            // we've checked if we can craft this so if the bukkit api works as expected,
            // this assertion should never fail
            assert (requiredAmount != 0);
        }

        // finally add the ItemStack
        inventory.addItem (output);

        return true;
    }

    public static List<ItemStack> getIngredients (Recipe recipe) {
        if (recipe instanceof ShapedRecipe) {
            ShapedRecipe shapedRecipe = (ShapedRecipe)recipe;
            List<ItemStack> result = new LinkedList<ItemStack>();
            Map<Character, ItemStack> ingredientMap = shapedRecipe.getIngredientMap();
            for (String string : shapedRecipe.getShape()) {
                for (int i = 0; i < string.length(); ++i) {
                    char c = string.charAt(i);
                    ItemStack itemStack = ingredientMap.get(c);
                    result.add(itemStack);
                }
            }
            return result;
        }

        else if (recipe instanceof ShapelessRecipe) {
            ShapelessRecipe shapelessRecipe = (ShapelessRecipe)recipe;
            return shapelessRecipe.getIngredientList();
        }

        else {
            return null;
        }
    }

    public static List<ItemStack> getIngredients (ShapelessRecipe recipe) {
        return recipe.getIngredientList();
    }

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

