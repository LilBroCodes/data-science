package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.StonecuttingRecipe;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

public class StonecuttingRecipeHandler extends RecipeHandler<StonecuttingRecipe> {
    @Override
    public Ingredient getCatalysts(StonecuttingRecipe recipe) {
        return Ingredient.ofItems(Items.STONECUTTER);
    }
}
