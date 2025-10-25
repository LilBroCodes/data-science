package org.lilbrocodes.data_science.common.item;

import net.minecraft.item.ItemStack;
import org.lilbrocodes.data_science.common.registry.ModItems;

public class RoughPrismarineCrystal extends AbstractPolishableItem {
    public RoughPrismarineCrystal() {
        super(new Settings().maxCount(1), 5, 0.15f);
    }

    @Override
    protected ItemStack getCompletedItem(ItemStack itemStack) {
        return new ItemStack(ModItems.POLISHED_PRISMARINE_CRYSTAL);
    }

    @Override
    public ItemStack getShatteredItem(ItemStack stack) {
        return new ItemStack(ModItems.SHATTERED_PRISMARINE_CRYSTAL);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return 0x55FCD2;
    }
}
