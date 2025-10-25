package org.lilbrocodes.data_science.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.data_science.common.recipes.GrindingRecipe;

import java.util.Objects;

public class GrindingCategory implements IRecipeCategory<GrindingRecipe> {
    public static RecipeType<GrindingRecipe> GRINDING;
    private final IGuiHelper guiHelper;
    private final IDrawable icon;
    private final IDrawableStatic arrow;

    public GrindingCategory(IGuiHelper guiHelper) {
        GRINDING = RecipeType.create(
                "data_science",
                "grinding",
                GrindingRecipe.class
        );

        this.guiHelper = guiHelper;
        icon = this.guiHelper.createDrawableItemStack(new ItemStack(Items.GRINDSTONE));
        Identifier furnaceGui = new Identifier("minecraft", "textures/gui/container/furnace.png");
        arrow = guiHelper.createDrawable(furnaceGui, 176, 14, 24, 17);
    }

    @Override
    public @NotNull RecipeType<GrindingRecipe> getRecipeType() {
        return GRINDING;
    }

    @Override
    public @NotNull Text getTitle() {
        return Text.translatable("jei.data_science.category.grinding");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public int getWidth() {
        return 150;
    }

    @Override
    public int getHeight() {
        return 50;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, GrindingRecipe recipe, @NotNull IFocusGroup focuses) {
        builder.addInputSlot(7, 17).addIngredients(recipe.getInput()).setSlotName("input");
        builder.addOutputSlot(120, 17).addItemStacks(recipe.getOutputs()).setSlotName("output");
    }

    @Override
    public void draw(@NotNull GrindingRecipe recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext ctx, double mouseX, double mouseY) {
        MatrixStack matrices = ctx.getMatrices();

        matrices.push();
        matrices.scale(1.5f, 1.5f, 1.5f);
        ctx.drawItemWithoutEntity(new ItemStack(Items.GRINDSTONE), (int) (40 / 1.5), (int) (16 / 1.5));
        matrices.pop();
        matrices.push();
        matrices.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(35));
        matrices.translate(Math.sin(System.currentTimeMillis() / 100d+ Objects.hash(recipe)) * 2 , 0, 100);

        ItemStack[] matchingStacks = recipe.getInput().getMatchingStacks();
        if (matchingStacks.length > 0) {
            int cycleTime = 20;
            long time = System.currentTimeMillis() / 50;
            int index = (int) ((time / cycleTime) % matchingStacks.length);

            ItemStack toRender = matchingStacks[index];

            int x = 18;
            int y = 35;
            ctx.drawItemWithoutEntity(toRender, x, y);
        }
        matrices.pop();

        matrices.push();
        matrices.translate(0, 0, 100);
        arrow.draw(ctx, 80, 17);
        ctx.drawCenteredTextWithShadow(MinecraftClient.getInstance().textRenderer, getSuccessRateText(recipe), 75, 40, 0xFFFFFFFF);
        matrices.pop();
    }

    private Text getSuccessRateText(GrindingRecipe recipe) {
        return Text.translatable("text.data_science.crumble_rate",  (int) (recipe.getFailChance() * 100));
    }
}
