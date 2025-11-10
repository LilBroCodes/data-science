package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.BlastingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmokingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

import java.util.List;

public class SmokingRecipeHandler extends RecipeHandler<SmokingRecipe> {
    @Override
    public Ingredient getCatalysts(SmokingRecipe recipe) {
        return Ingredient.ofItems(Items.SMOKER);
    }

    @Override
    public int getCookTime(SmokingRecipe recipe) {
        return recipe.getCookTime();
    }
}
