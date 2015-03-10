package me.ccl2of4.CraftingAssistance;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by connor on 3/9/15.
 */
public class StringAssembler {
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
                            builder.append (getStringForMaterial (itemStack.getType ()));
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
                        builder.append (getStringForMaterial (itemStack.getType ()));
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
                        .append (getStringForMaterial (recipe.getInput ().getType ()))
                        .append (ChatColor.RESET);

                return builder.toString ();
            }
        });

    }

    public static String getStringForRecipe (Recipe recipe) {
        Class c = recipe.getClass ();
        while (!mappings.containsKey (c)) {
            if (c == Object.class)
                return null;
            c = c.getSuperclass ();
        }
        return mappings.get (c).findIngredients (recipe);
    }

    public static String getStringForMaterial (Material material) {
        String result = "null";
        if (material != null) {
            result = material.toString ();
            result = result.toLowerCase ();
        }
        return result;
    }

    private StringAssembler () {}
}
