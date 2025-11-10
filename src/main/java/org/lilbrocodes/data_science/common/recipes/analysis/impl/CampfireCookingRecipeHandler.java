package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.Items;
import net.minecraft.recipe.CampfireCookingRecipe;
import net.minecraft.recipe.Ingredient;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

public class CampfireCookingRecipeHandler extends RecipeHandler<CampfireCookingRecipe> {
    @Override
    public Ingredient getCatalysts(CampfireCookingRecipe recipe) {
        return Ingredient.ofItems(Items.CAMPFIRE);
    }

    @Override
    public int getCookTime(CampfireCookingRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    public boolean needsFuel(CampfireCookingRecipe recipe) {
        return false;
    }
}
