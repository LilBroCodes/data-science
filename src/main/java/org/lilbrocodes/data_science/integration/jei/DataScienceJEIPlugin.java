package org.lilbrocodes.data_science.integration.jei;

import com.google.common.collect.ImmutableList;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.item.AbstractPolishableItem;
import org.lilbrocodes.data_science.common.recipes.GrindingRecipe;
import org.lilbrocodes.data_science.common.registry.ModRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lilbrocodes.data_science.integration.jei.GrindingCategory.GRINDING;

public class DataScienceJEIPlugin implements IModPlugin {
    @Override
    public @NotNull Identifier getPluginUid() {
        return DataScience.identify("data_science");
    }

    @Override
    public void registerRecipes(@NotNull IRecipeRegistration registration) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ClientWorld world = Objects.requireNonNull(mc.world);

        List<GrindingRecipe> realRecipes = world.getRecipeManager()
                .listAllOfType(ModRecipes.GRINDING_RECIPE_TYPE);

        List<GrindingRecipe> allRecipes = new ArrayList<>(realRecipes);

        for (Item item : Registries.ITEM) {
            if (item instanceof AbstractPolishableItem polishable) {
                allRecipes.add(new GrindingRecipe(
                        DataScience.identify("polish_" + Registries.ITEM.getId(polishable).getPath()),
                        Ingredient.ofStacks(polishable.getJEIInputs(item)),
                        polishable.getJEIOutputs(item),
                        polishable.getFailChance()
                ));
            }
        }

        registration.addRecipes(GRINDING, ImmutableList.copyOf(allRecipes));
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new GrindingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(Items.GRINDSTONE, GRINDING);
    }
}
