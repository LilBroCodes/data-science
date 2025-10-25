package org.lilbrocodes.data_science.common.recipes.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.CriterionConditions;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.data_science.common.registry.ModRecipes;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class GrindingRecipeJsonBuilder {
    private final RecipeCategory category;
    private final Ingredient input;
    private final List<ItemStack> outputs = new ArrayList<>();
    private float failChance;
    private final Advancement.Builder advancement = Advancement.Builder.createUntelemetered();

    @Nullable
    private String group;

    public GrindingRecipeJsonBuilder(RecipeCategory category, Ingredient input) {
        this.category = category;
        this.input = input;
    }

    public static GrindingRecipeJsonBuilder create(RecipeCategory category, ItemConvertible... input) {
        return new GrindingRecipeJsonBuilder(category, Ingredient.ofItems(input));
    }

    public GrindingRecipeJsonBuilder addOutput(ItemStack stack) {
        this.outputs.add(stack);
        return this;
    }

    public GrindingRecipeJsonBuilder failChance(float chance) {
        this.failChance = chance;
        return this;
    }

    public GrindingRecipeJsonBuilder group(@Nullable String group) {
        this.group = group;
        return this;
    }

    public GrindingRecipeJsonBuilder criterion(String name, CriterionConditions criterion) {
        this.advancement.criterion(name, criterion);
        return this;
    }

    public void offerTo(Consumer<RecipeJsonProvider> exporter, Identifier id) {
        advancement.parent(new Identifier("recipes/root"))
                .criterion("has_the_recipe", RecipeUnlockedCriterion.create(id))
                .rewards(net.minecraft.advancement.AdvancementRewards.Builder.recipe(id));

        exporter.accept(new GrindingRecipeJsonProvider(
                id,
                this.group == null ? "" : this.group,
                this.input,
                this.outputs,
                this.failChance,
                this.advancement,
                id.withPrefixedPath("recipes/" + category.getName() + "/")
        ));
    }

    public static class GrindingRecipeJsonProvider implements RecipeJsonProvider {
        private final Identifier id;
        private final String group;
        private final Ingredient input;
        private final List<ItemStack> outputs;
        private final float failChance;
        private final Advancement.Builder advancement;
        private final Identifier advancementId;

        public GrindingRecipeJsonProvider(
                Identifier id,
                String group,
                Ingredient input,
                List<ItemStack> outputs,
                float failChance,
                Advancement.Builder advancement,
                Identifier advancementId
        ) {
            this.id = id;
            this.group = group;
            this.input = input;
            this.outputs = outputs;
            this.failChance = failChance;
            this.advancement = advancement;
            this.advancementId = advancementId;
        }

        @Override
        public void serialize(JsonObject json) {
            if (!group.isEmpty()) {
                json.addProperty("group", group);
            }

            json.add("input", input.toJson());

            JsonArray outputsArray = new JsonArray();
            for (ItemStack stack : outputs) {
                JsonObject obj = new JsonObject();
                obj.addProperty("item", Registries.ITEM.getId(stack.getItem()).toString());
                if (stack.getCount() > 1) {
                    obj.addProperty("count", stack.getCount());
                }
                if (stack.hasNbt()) {
                    obj.addProperty("nbt", stack.getNbt().toString());
                }
                outputsArray.add(obj);
            }
            json.add("outputs", outputsArray);

            json.addProperty("fail_chance", failChance);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return ModRecipes.GRINDING_RECIPE_SERIALIZER;
        }

        @Override
        public Identifier getRecipeId() {
            return id;
        }

        @Nullable
        @Override
        public JsonObject toAdvancementJson() {
            return advancement.toJson();
        }

        @Nullable
        @Override
        public Identifier getAdvancementId() {
            return advancementId;
        }
    }
}
