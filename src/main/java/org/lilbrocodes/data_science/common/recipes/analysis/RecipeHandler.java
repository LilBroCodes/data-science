package org.lilbrocodes.data_science.common.recipes.analysis;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;

import java.util.Collections;
import java.util.List;

public class RecipeHandler<T extends Recipe<?>> {

    public List<Ingredient> getInputs(T recipe) {
        return recipe.getIngredients();
    }

    public List<RecipeOutput> getOutputs(DynamicRegistryManager registryManager, T recipe) {
        return fullChance(registryManager, recipe);
    }

    public Ingredient getCatalysts(T recipe) {
        return Ingredient.EMPTY;
    }

    public int getCookTime(T recipe) {
        return -1;
    }

    public boolean needsFuel(T recipe) { return true; }

    public String getDisplayName(T recipe) {
        Identifier id = recipe.getId();
        return id != null ? id.toString() : "unknown";
    }

    public record RecipeOutput(ItemStack stack, float chance) {}

    protected List<RecipeOutput> fullChance(DynamicRegistryManager registryManager, T recipe) {
        return Collections.singletonList(new RecipeOutput(recipe.getOutput(registryManager), 1f));
    }
}
