package org.lilbrocodes.data_science.common.recipes.analysis;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;

import java.util.*;

public class MaterialCollector {
    private final DynamicRegistryManager manager;
    private final Recipe<?> baseRecipe;

    public final List<ItemStack> developedIngredients = new ArrayList<>();
    public final List<RecipeHandler.RecipeOutput> totalResults = new ArrayList<>();
    public final List<Ingredient> allCatalysts = new ArrayList<>();
    public int totalCookTime = 0;

    public record CollectedMaterials(
            List<ItemStack> ingredients,
            List<RecipeHandler.RecipeOutput> totalResults,
            List<Ingredient> catalysts,
            int totalCookTime
    ) {}

    public MaterialCollector(DynamicRegistryManager manager, Recipe<?> baseRecipe) {
        this.manager = manager;
        this.baseRecipe = baseRecipe;
    }

    public CollectedMaterials develop(int maxDepth, Collection<Item> rawItems) {
        Set<Item> path = new HashSet<>();
        developRecipe(baseRecipe, 1, 0, maxDepth, path, rawItems);
        mergeCatalysts();
        mergeIngredients();
        return new CollectedMaterials(developedIngredients, totalResults, allCatalysts, totalCookTime);
    }

    public CollectedMaterials developNoCombines(int maxDepth) {
        return develop(maxDepth, List.of(
                Items.COAL,
                Items.COPPER_INGOT,
                Items.IRON_INGOT,
                Items.GOLD_INGOT,
                Items.LAPIS_LAZULI,
                Items.DIAMOND,
                Items.REDSTONE,
                Items.NETHERITE_SCRAP,
                Items.ANCIENT_DEBRIS,
                Items.SAND,
                Items.CLAY_BALL,
                Items.CACTUS,
                Items.STONE
        ));
    }

    private void mergeCatalysts() {
        List<Ingredient> unique = new ArrayList<>();
        outer:
        for (Ingredient ing : allCatalysts) {
            for (Ingredient existing : unique) {
                for (ItemStack stack : ing.getMatchingStacks()) {
                    for (ItemStack existingStack : existing.getMatchingStacks()) {
                        if (stack.getItem().equals(existingStack.getItem())) continue outer;
                    }
                }
            }
            unique.add(ing);
        }
        allCatalysts.clear();
        allCatalysts.addAll(unique);
    }

    private void mergeIngredients() {
        Map<String, ItemStack> map = new LinkedHashMap<>();
        for (ItemStack stack : developedIngredients) {
            String key = stack.getItem().toString();
            if (map.containsKey(key)) {
                map.get(key).increment(stack.getCount());
            } else {
                ItemStack copy = stack.copy();
                map.put(key, copy);
            }
        }
        developedIngredients.clear();
        developedIngredients.addAll(map.values());
    }

    @SuppressWarnings("unchecked")
    private <T extends Recipe<?>> void developRecipe(T recipe,
                                                     int multiplier,
                                                     int depth,
                                                     int maxDepth,
                                                     Set<Item> path,
                                                     Collection<Item> rawItems) {
        if (depth > maxDepth) return;

        RecipeHandler<Recipe<?>> handler = (RecipeHandler<Recipe<?>>) RecipeHandlerRegistry.getHandler(recipe.getType());

        // Track catalysts
        Ingredient catalyst = handler.getCatalysts(recipe);
        if (catalyst != null && !catalyst.isEmpty()) allCatalysts.add(catalyst);

        // Track cook time
        int cookTime = handler.getCookTime(recipe);
        if (cookTime > 0) totalCookTime += cookTime * multiplier;

        // Track results
        List<RecipeHandler.RecipeOutput> outputs = handler.getOutputs(manager, recipe);
        for (RecipeHandler.RecipeOutput out : outputs) {
            ItemStack copy = out.stack().copy();
            copy.setCount(copy.getCount() * multiplier);
            totalResults.add(new RecipeHandler.RecipeOutput(copy, out.chance()));
        }

        // Process inputs recursively
        List<Ingredient> inputs = handler.getInputs(recipe);
        if (inputs == null) return;

        for (Ingredient ing : inputs) {
            ItemStack[] matching = ing.getMatchingStacks();
            if (matching.length == 0) continue;

            ItemStack first = matching[0];
            int requiredAmount = first.getCount() * multiplier;

            // Prevent cycles
            if (path.contains(first.getItem()) || rawItems.contains(first.getItem())) {
                // Treat as raw ingredient
                addIngredient(first, requiredAmount);
                continue;
            }

            path.add(first.getItem());
            boolean crafted = false;

            for (Map.Entry<RecipeType<?>, RecipeHandler<?>> entry : RecipeHandlerRegistry.allHandlers().entrySet()) {
                List<Recipe<?>> subRecipes = RecipeHandlerCache.getRecipesProducing(first, entry.getKey(), manager);
                if (subRecipes.isEmpty()) continue;

                Recipe<?> subRecipe = subRecipes.get(0); // first recipe
                RecipeHandler<Recipe<?>> subHandler = (RecipeHandler<Recipe<?>>) RecipeHandlerRegistry.getHandler(subRecipe.getType());
                RecipeHandler.RecipeOutput output = subHandler.getOutputs(manager, subRecipe).get(0);

                int outputCount = output.stack().getCount();
                int runs = (int) Math.ceil((double) requiredAmount / outputCount);

                // Recurse into sub-recipe
                developRecipe(subRecipe, runs, depth + 1, maxDepth, path, rawItems);

                // Track leftovers
                int leftover = outputCount * runs - requiredAmount;
                if (leftover > 0) {
                    ItemStack leftoverStack = output.stack().copy();
                    leftoverStack.setCount(leftover);
                    totalResults.add(new RecipeHandler.RecipeOutput(leftoverStack, output.chance()));
                }

                crafted = true;
                break; // only first recipe
            }

            if (!crafted) {
                addIngredient(first, requiredAmount);
            }

            path.remove(first.getItem());
        }
    }

    private void addIngredient(ItemStack stack, int count) {
        Optional<ItemStack> existing = developedIngredients.stream()
                .filter(s -> s.getItem().equals(stack.getItem()))
                .findFirst();

        if (existing.isPresent()) {
            existing.get().increment(count);
        } else {
            ItemStack copy = stack.copy();
            copy.setCount(count);
            developedIngredients.add(copy);
        }
    }
}
