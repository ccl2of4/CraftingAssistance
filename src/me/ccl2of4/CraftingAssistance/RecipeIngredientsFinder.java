package me.ccl2of4.CraftingAssistance;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by connor on 3/8/15.
 */
public final class RecipeIngredientsFinder {
    private static Map<Class, RecipeIngredientsFinderInner> mappings = new HashMap<Class, RecipeIngredientsFinderInner> ();

    private static abstract class RecipeIngredientsFinderInner {
        protected abstract String findIngredients (Recipe recipe);
    }

    static {

        mappings.put (ShapedRecipe.class, new RecipeIngredientsFinderInner () {
            protected String findIngredients (Recipe r) {
                ShapedRecipe recipe = (ShapedRecipe) r;
                StringBuilder builder = new StringBuilder ();
                builder
                        .append (ChatColor.GRAY)
                        .append ("Shaped:\n")
                        .append (ChatColor.RESET);

                Map<Character,ItemStack> ingredientMap = recipe.getIngredientMap ();
                for (String string : recipe.getShape ()) {
                    for (int i = 0; i < string.length (); ++i) {
                        char c = string.charAt (i);
                        ItemStack itemStack = ingredientMap.get (c);

                        builder
                                .append ("[")
                                .append (ChatColor.YELLOW);

                        if (itemStack != null) {
                            builder.append (getMaterialDescription (itemStack.getType ()));
                        } else {
                            builder.append ("-empty-");
                        }

                        builder
                                .append (ChatColor.RESET)
                                .append ("]");

                    }
                    builder.append ("\n");
                }

                return builder.toString ();
            }
        });

        mappings.put (ShapelessRecipe.class, new RecipeIngredientsFinderInner () {
            protected String findIngredients (Recipe r) {
                ShapelessRecipe recipe = (ShapelessRecipe) r;
                StringBuilder builder = new StringBuilder ();
                builder
                        .append (ChatColor.GRAY)
                        .append ("Shapeless:\n")
                        .append (ChatColor.RESET);

                for (ItemStack itemStack : recipe.getIngredientList ()) {
                    builder
                            .append ("[")
                            .append (ChatColor.YELLOW);

                    if (itemStack != null) {
                        builder.append (getMaterialDescription (itemStack.getType ()));
                    } else {
                        builder.append ("-empty-");
                    }

                    builder
                            .append (ChatColor.RESET)
                            .append ("]");
                }
                return builder.toString ();
            }
        });

        mappings.put (FurnaceRecipe.class, new RecipeIngredientsFinderInner () {
            protected String findIngredients (Recipe r) {
                FurnaceRecipe recipe = (FurnaceRecipe) r;
                StringBuilder builder = new StringBuilder ();
                builder
                        .append (ChatColor.GRAY)
                        .append ("Smelting:\n")
                        .append (ChatColor.RESET)

                        .append (ChatColor.YELLOW)
                        .append (getMaterialDescription (recipe.getInput ().getType ()))
                        .append (ChatColor.RESET);

                return builder.toString ();
            }
        });

    }

    public static String findIngredients (Recipe recipe) {
        Class c = recipe.getClass ();
        while (!mappings.containsKey (c)) {
            if (c == Object.class)
                return null;
            c = c.getSuperclass ();
        }
        return mappings.get (c).findIngredients (recipe);
    }

    public static String getMaterialDescription (Material material) {
        String result = material.toString ();
        result = result.toLowerCase ();
        return result;
    }

    private RecipeIngredientsFinder () {}
}
