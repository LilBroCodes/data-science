package org.lilbrocodes.data_science.common.registry;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredItemGroupRegistry;
import org.lilbrocodes.data_science.common.DataScience;

public class ModItemGroups {
    private static final DeferredItemGroupRegistry GROUPS = new DeferredItemGroupRegistry(DataScience.MOD_ID);

    public static final RegistryKey<ItemGroup> DATA_SCIENCE_GROUP = GROUPS.registerItemGroup(
            "data_science",
            () -> new ItemStack(ModItems.ADVANCED_CIRCUIT)
    );

    public static void initialize() {

    }
}
