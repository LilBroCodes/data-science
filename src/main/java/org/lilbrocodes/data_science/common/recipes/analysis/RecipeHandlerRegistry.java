package org.lilbrocodes.data_science.common.recipes.analysis;

import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.server.MinecraftServer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeHandlerRegistry {
    private static final Map<RecipeType<?>, RecipeHandler<?>> HANDLERS = new HashMap<>();

    public static <T extends Recipe<?>> void register(RecipeType<T> type, RecipeHandler<T> handler) {
        HANDLERS.put(type, handler);
    }

    public static void generateCache(MinecraftServer server) {
        for (RecipeType<?> type : HANDLERS.keySet()) {
            buildCacheForType(server, type);
        }
    }

    @SuppressWarnings("unchecked")
    private static <C extends Inventory, T extends Recipe<C>> void buildCacheForType(MinecraftServer server, RecipeType<?> type) {
        RecipeType<T> castType = (RecipeType<T>) type;
        List<T> recipes = server.getRecipeManager().listAllOfType(castType);
        RecipeHandlerCache.buildCache(castType, recipes, server.getRegistryManager());
    }

    @SuppressWarnings("unchecked")
    public static <T extends Recipe<?>> RecipeHandler<T> getHandler(RecipeType<T> type) {
        RecipeHandler<T> handler = (RecipeHandler<T>) HANDLERS.get(type);
        if (handler != null) return handler;
        else return new RecipeHandler<>();
    }

    public static Map<RecipeType<?>, RecipeHandler<?>> allHandlers() {
        return HANDLERS;
    }
}
