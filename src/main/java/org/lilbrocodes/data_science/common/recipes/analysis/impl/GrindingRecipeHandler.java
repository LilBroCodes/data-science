package org.lilbrocodes.data_science.common.recipes.analysis.impl;

import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.DynamicRegistryManager;
import org.lilbrocodes.data_science.common.recipes.GrindingRecipe;
import org.lilbrocodes.data_science.common.recipes.analysis.RecipeHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GrindingRecipeHandler extends RecipeHandler<GrindingRecipe> {
    @Override
    public List<Ingredient> getInputs(GrindingRecipe recipe) {
        return Collections.singletonList(recipe.getInput());
    }

    @Override
    public List<RecipeOutput> getOutputs(DynamicRegistryManager registryManager, GrindingRecipe recipe) {
        List<RecipeOutput> outputs = new ArrayList<>();
        recipe.getOutputs().forEach(stack -> outputs.add(new RecipeOutput(stack, 1 - recipe.getFailChance())));
        return outputs;
    }

    @Override
    public Ingredient getCatalysts(GrindingRecipe recipe) {
        return Ingredient.ofItems(Items.GRINDSTONE);
    }
}
