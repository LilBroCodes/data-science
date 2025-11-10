package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.Items;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

public class BlastingRecipeHandler extends RecipeHandler<BlastingRecipe> {
    @Override
    public Ingredient getCatalysts(BlastingRecipe recipe) {
        return Ingredient.ofItems(Items.BLAST_FURNACE);
    }

    @Override
    public int getCookTime(BlastingRecipe recipe) {
        return recipe.getCookTime();
    }
}
