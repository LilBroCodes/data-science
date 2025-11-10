package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.StonecuttingRecipe;
import net.minecraft.registry.DynamicRegistryManager;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

import java.util.List;

public class SmithingRecipeHandler extends RecipeHandler<SmithingRecipe> {
    @Override
    public Ingredient getCatalysts(SmithingRecipe recipe) {
        return Ingredient.ofItems(Items.SMITHING_TABLE);
    }
}
