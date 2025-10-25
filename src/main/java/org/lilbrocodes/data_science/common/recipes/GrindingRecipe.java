package org.lilbrocodes.data_science.common.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.*;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.world.World;
import org.lilbrocodes.data_science.common.registry.ModRecipes;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ClassCanBeRecord")
public class GrindingRecipe implements Recipe<SimpleInventory> {
    private final Identifier id;
    private final Ingredient input;
    private final List<ItemStack> outputs;
    private final float failChance;

    public GrindingRecipe(Identifier id, Ingredient input, List<ItemStack> outputs, float failChance) {
        this.id = id;
        this.input = input;
        this.outputs = outputs;
        this.failChance = failChance;
    }

    public Ingredient getInput() {
        return input;
    }

    public List<ItemStack> getOutputs() {
        return outputs;
    }

    public float getFailChance() {
        return failChance;
    }

    @Override
    public boolean matches(SimpleInventory inventory, World world) {
        return input.test(inventory.getStack(0));
    }

    @Override
    public ItemStack craft(SimpleInventory inventory,  DynamicRegistryManager registryManager) {
        return ItemStack.EMPTY; // We handle outputs manually (multiple outputs)
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return outputs.isEmpty() ? ItemStack.EMPTY : outputs.get(0);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.GRINDING_RECIPE_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.GRINDING_RECIPE_TYPE;
    }

    public static class Serializer implements RecipeSerializer<GrindingRecipe> {

        public static final Serializer INSTANCE = new Serializer();

        @Override
        public GrindingRecipe read(Identifier id, JsonObject json) {
            Ingredient input = Ingredient.fromJson(json.get("input"));

            List<ItemStack> outputs = new ArrayList<>();
            JsonArray outArray = json.getAsJsonArray("outputs");
            for (int i = 0; i < outArray.size(); i++) {
                outputs.add(ShapedRecipe.outputFromJson(outArray.get(i).getAsJsonObject()));
            }

            float failChance = JsonHelper.getFloat(json, "fail_chance", 0f);

            return new GrindingRecipe(id, input, outputs, failChance);
        }

        @Override
        public GrindingRecipe read(Identifier id, PacketByteBuf buf) {
            Ingredient input = Ingredient.fromPacket(buf);
            int size = buf.readVarInt();
            List<ItemStack> outputs = new ArrayList<>();
            for (int i = 0; i < size; i++) outputs.add(buf.readItemStack());
            float failChance = buf.readFloat();
            return new GrindingRecipe(id, input, outputs, failChance);
        }

        @Override
        public void write(PacketByteBuf buf, GrindingRecipe recipe) {
            recipe.getInput().write(buf);
            buf.writeVarInt(recipe.getOutputs().size());
            for (ItemStack stack : recipe.getOutputs()) buf.writeItemStack(stack);
            buf.writeFloat(recipe.getFailChance());
        }
    }
}
