package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

public class CraftingRecipeHandler extends RecipeHandler<CraftingRecipe> {
    @Override
    public Ingredient getCatalysts(CraftingRecipe recipe) {
        if (recipe.fits(2, 2)) return Ingredient.ofItems(Items.CRAFTING_TABLE, Items.AIR);
        return Ingredient.ofItems(Items.CRAFTING_TABLE);
    }
}
