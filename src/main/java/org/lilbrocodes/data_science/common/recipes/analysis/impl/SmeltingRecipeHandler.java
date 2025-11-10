package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmeltingRecipe;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

public class SmeltingRecipeHandler extends RecipeHandler<SmeltingRecipe> {
    @Override
    public Ingredient getCatalysts(SmeltingRecipe recipe) {
        return Ingredient.ofItems(Items.FURNACE);
    }

    @Override
    public int getCookTime(SmeltingRecipe recipe) {
        return recipe.getCookTime();
    }
}
