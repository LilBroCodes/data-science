package org.lilbrocodes.data_science.common.item;

import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.composer_reloaded.api.item.AbstractProgressItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class AbstractPolishableItem extends AbstractProgressItem {
    private final float failChance;

    public AbstractPolishableItem(Settings settings, int steps, float failChance) {
        super(settings, steps);
        this.failChance = failChance;
    }

    public abstract ItemStack getShatteredItem(ItemStack stack);

    public ItemStack tryPolish(ItemStack stack, World world, PlayerEntity player) {
        if (Math.random() <= getFailChance()) {
            return getShatteredItem(stack);
        }

        return tryIncrementProgress(stack, world, player, 1);
    }

    public static ItemStack create(Item type, int step) {
        ItemStack stack = new ItemStack(type);
        setStep(stack, step);
        return stack;
    }

    public Stream<ItemStack> getJEIInputs(Item type) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < getMaxSteps(); i++) stacks.add(create(type, i));
        return stacks.stream();
    }

    public List<ItemStack> getJEIOutputs(Item type) {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < getMaxSteps(); i++) stacks.add(create(type, i));
        stacks.add(getCompletedItem(stacks.get(stacks.size() - 1)));
        return stacks;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        int step = getStep(stack);
        int max = getMaxSteps();

        tooltip.add(Text.translatable("tooltip.data_science.polishing_progress").formatted(Formatting.GRAY)
                .append(": ")
                .append(Text.literal(step + " / " + max)));
    }


    public float getFailChance() {
        return failChance;
    }
}
