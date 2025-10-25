package org.lilbrocodes.data_science.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.CookingRecipeJsonBuilder;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.item.AbstractPolishableItem;
import org.lilbrocodes.data_science.common.recipes.json.GrindingRecipeJsonBuilder;
import org.lilbrocodes.data_science.common.registry.ModBlocks;
import org.lilbrocodes.data_science.common.registry.ModItems;

import java.util.function.Consumer;

public class DataScienceRecipeProvider extends FabricRecipeProvider {
    public DataScienceRecipeProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate(Consumer<RecipeJsonProvider> consumer) {
        // ================= CRAFTING =================

        // --------- Shaped ---------
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModBlocks.ENVIRONMENTAL_SENSOR.item)
                .pattern("NPN")
                .pattern("RCR")
                .pattern("IGI")
                .input('N', Items.NETHERITE_INGOT)
                .input('P', ModItems.PRISMARINE_PEARL)
                .input('R', ModItems.RED_ALLOY_INGOT)
                .input('I', Items.IRON_BLOCK)
                .input('G', Items.IRON_INGOT)
                .input('C', ModItems.ADVANCED_CIRCUIT)
                .criterion("has_circuit", conditionsFromItem(ModItems.ADVANCED_CIRCUIT))
                .criterion("has_pearl", conditionsFromItem(ModItems.PRISMARINE_PEARL))
                .criterion("has_alloy", conditionsFromItem(ModItems.RED_ALLOY_INGOT))
                .offerTo(consumer, DataScience.identify("environmental_sensor"));

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RAW_BASIC_CIRCUIT)
                .pattern("GRG")
                .pattern("C C")
                .pattern("GCG")
                .input('G', Items.GLASS_PANE)
                .input('R', ModItems.RED_ALLOY_INGOT)
                .input('C', Items.COPPER_INGOT)
                .criterion("has_red_alloy", conditionsFromItem(ModItems.RED_ALLOY_INGOT))
                .criterion("has_copper", conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(consumer, "basic_circuit");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.ASSEMBLED_ADVANCED_CIRCUIT)
                .pattern("PIP")
                .pattern("BRB")
                .pattern("PIP")
                .input('P', ModItems.POLISHED_PRISMARINE_CRYSTAL)
                .input('I', Items.GOLD_INGOT)
                .input('R', ModItems.RED_ALLOY_INGOT)
                .input('B', ModItems.BASIC_CIRCUIT)
                .criterion("has_red_alloy", conditionsFromItem(ModItems.RED_ALLOY_INGOT))
                .criterion("has_basic", conditionsFromItem(ModItems.BASIC_CIRCUIT))
                .criterion("has_crystal", conditionsFromItem(ModItems.POLISHED_PRISMARINE_CRYSTAL))
                .offerTo(consumer, "advanced_circuit");

        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.PRISMARINE_PEARL)
                .pattern(" S ")
                .pattern("SPS")
                .pattern(" S ")
                .input('S', ModItems.POLISHED_PRISMARINE_CRYSTAL)
                .input('P', Items.ENDER_PEARL)
                .criterion("has_pearl", conditionsFromItem(Items.ENDER_PEARL))
                .criterion("has_shard", conditionsFromItem(ModItems.POLISHED_PRISMARINE_CRYSTAL))
                .offerTo(consumer, "prismarine_pearl");

        // --------- Shapeless ---------
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RED_ALLOY_DUST, 3)
                .input(Items.REDSTONE)
                .input(ModItems.IRON_DUST)
                .input(ModItems.COPPER_DUST)
                .criterion("has_redstone", conditionsFromItem(Items.REDSTONE))
                .criterion("has_copper", conditionsFromItem(ModItems.COPPER_DUST))
                .criterion("has_iron", conditionsFromItem(ModItems.IRON_DUST))
                .offerTo(consumer, "red_alloy_dust");

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PRISMARINE_CRYSTALS)
                .input(ModItems.SHATTERED_PRISMARINE_CRYSTAL, 3)
                .criterion("has_crystal", conditionsFromItem(ModItems.SHATTERED_PRISMARINE_CRYSTAL))
                .offerTo(consumer, "combine_crystals");

        // ================= COOKING =================

        // --------- Smelting ---------
        CookingRecipeJsonBuilder.createSmelting(
                Ingredient.ofItems(ModItems.RAW_BASIC_CIRCUIT),
                RecipeCategory.MISC,
                ModItems.BASIC_CIRCUIT,
                0.1f,
                300
        ).criterion("has_raw_circuit", conditionsFromItem(ModItems.RAW_BASIC_CIRCUIT)).offerTo(consumer, "solder_smelting_basic");

        CookingRecipeJsonBuilder.createSmelting(
                Ingredient.ofItems(ModItems.ASSEMBLED_ADVANCED_CIRCUIT),
                RecipeCategory.MISC,
                ModItems.ADVANCED_CIRCUIT,
                0.1f,
                600
        ).criterion("has_raw_circuit", conditionsFromItem(ModItems.ASSEMBLED_ADVANCED_CIRCUIT)).offerTo(consumer, "solder_smelting_advanced");

        // --------- Blasting ---------
        CookingRecipeJsonBuilder.createBlasting(
                Ingredient.ofItems(ModItems.RED_ALLOY_DUST),
                RecipeCategory.MISC,
                ModItems.RED_ALLOY_INGOT,
                0.2f,
                300
        ).criterion("has_dust", conditionsFromItem(ModItems.RED_ALLOY_DUST)).offerTo(consumer, "red_alloy_smelting");

        // ================= GRINDING =================

        GrindingRecipeJsonBuilder.create(RecipeCategory.MISC, Items.IRON_INGOT)
                .failChance(0.05f)
                .addOutput(new ItemStack(ModItems.IRON_DUST, 3))
                .criterion("has_iron", conditionsFromItem(Items.IRON_INGOT))
                .offerTo(consumer, DataScience.identify("grind_iron"));

        GrindingRecipeJsonBuilder.create(RecipeCategory.MISC, Items.COPPER_INGOT)
                .failChance(0.05f)
                .addOutput(new ItemStack(ModItems.COPPER_DUST, 3))
                .criterion("has_copper", conditionsFromItem(Items.COPPER_INGOT))
                .offerTo(consumer, DataScience.identify("grind_copper"));

        GrindingRecipeJsonBuilder.create(RecipeCategory.MISC, Items.PRISMARINE_CRYSTALS)
                .failChance(0.05f)
                .addOutput(AbstractPolishableItem.create(ModItems.ROUGH_PRISMARINE_CRYSTAL, 1))
                .criterion("has_shard", conditionsFromItem(Items.PRISMARINE_CRYSTALS))
                .offerTo(consumer, DataScience.identify("polish_crystals"));

        GrindingRecipeJsonBuilder.create(RecipeCategory.MISC, ModItems.RED_ALLOY_INGOT)
                .failChance(0.05f)
                .addOutput(new ItemStack(ModItems.RED_ALLOY_DUST))
                .criterion("has_alloy", conditionsFromItem(ModItems.RED_ALLOY_INGOT))
                .offerTo(consumer, DataScience.identify("crush_alloy"));
    }
}
