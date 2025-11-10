package org.lilbrocodes.data_science.common.registry;

import net.minecraft.recipe.RecipeType;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandlerRegistry;
import org.lilbrocodes.data_science.common.recipes.analysis.impl.*;

public class ModRecipeHandlers {
    public static void initialize() {
        RecipeHandlerRegistry.register(RecipeType.BLASTING, new BlastingRecipeHandler());
        RecipeHandlerRegistry.register(RecipeType.CAMPFIRE_COOKING, new CampfireCookingRecipeHandler());
        RecipeHandlerRegistry.register(RecipeType.CRAFTING, new CraftingRecipeHandler());
        RecipeHandlerRegistry.register(RecipeType.SMELTING, new SmeltingRecipeHandler());
        RecipeHandlerRegistry.register(RecipeType.SMITHING, new SmithingRecipeHandler());
        RecipeHandlerRegistry.register(RecipeType.SMOKING, new SmokingRecipeHandler());
        RecipeHandlerRegistry.register(RecipeType.STONECUTTING, new StonecuttingRecipeHandler());

        RecipeHandlerRegistry.register(ModRecipes.GRINDING_RECIPE_TYPE, new GrindingRecipeHandler());
    }
}
