package org.lilbrocodes.data_science.common.recipes.analysis;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.*;

public class RecipeHandlerCache {
    private static final Map<RecipeType<?>, Map<Item, List<Recipe<?>>>> cache = new HashMap<>();

    public static <T extends Recipe<?>> void buildCache(RecipeType<T> type, Collection<T> recipes, DynamicRegistryManager manager) {
        Map<Item, List<Recipe<?>>> outputMap = new HashMap<>();

        for (T recipe : recipes) {
            RecipeHandler<T> handler = RecipeHandlerRegistry.getHandler(type);

            List<RecipeHandler.RecipeOutput> outputs = handler.getOutputs(manager, recipe);
            for (RecipeHandler.RecipeOutput ro : outputs) {
                Item item = ro.stack().getItem();
                outputMap.computeIfAbsent(item, k -> new ArrayList<>()).add(recipe);
            }
        }

        cache.put(type, outputMap);
    }

    public static List<Recipe<?>> getRecipesProducing(ItemStack stack, RecipeType<?> type, DynamicRegistryManager manager) {
        Map<Item, List<Recipe<?>>> outputMap = cache.get(type);
        if (outputMap == null) return Collections.emptyList();

        return outputMap.getOrDefault(stack.getItem(), Collections.emptyList());
    }

    public static void clear() {
        cache.clear();
    }
}
