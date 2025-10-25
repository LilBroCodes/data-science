package org.lilbrocodes.data_science.common.registry;


import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.recipes.GrindingRecipe;

public class ModRecipes {
    public static final RecipeType<GrindingRecipe> GRINDING_RECIPE_TYPE = Registry.register(
            Registries.RECIPE_TYPE,
            DataScience.identify("grinding"),
            new RecipeType<GrindingRecipe>() {
                @Override
                public String toString() {
                    return "data_science:grinding";
                }
            }
    );

    public static final RecipeSerializer<GrindingRecipe> GRINDING_RECIPE_SERIALIZER = Registry.register(
            Registries.RECIPE_SERIALIZER,
            DataScience.identify("grinding"),
            GrindingRecipe.Serializer.INSTANCE
    );

    public static void initialize() {

    }
}
