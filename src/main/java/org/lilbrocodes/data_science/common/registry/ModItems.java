package org.lilbrocodes.data_science.common.registry;

import net.minecraft.item.Item;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredItemRegistry;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.item.RoughPrismarineCrystal;

public class ModItems {
    private static final DeferredItemRegistry ITEMS = new DeferredItemRegistry(DataScience.MOD_ID, ModItemGroups.DATA_SCIENCE_GROUP);

    public static final Item RAW_BASIC_CIRCUIT = ITEMS.registerSimple("raw_basic_circuit");
    public static final Item BASIC_CIRCUIT = ITEMS.registerSimple("basic_circuit");
    public static final Item ASSEMBLED_ADVANCED_CIRCUIT = ITEMS.registerSimple("assembled_advanced_circuit");
    public static final Item ADVANCED_CIRCUIT = ITEMS.registerSimple("advanced_circuit");

    public static final Item IRON_DUST = ITEMS.registerSimple("iron_dust");
    public static final Item COPPER_DUST = ITEMS.registerSimple("copper_dust");
    public static final Item RED_ALLOY_DUST = ITEMS.registerSimple("red_alloy_dust");
    public static final Item RED_ALLOY_INGOT = ITEMS.registerSimple("red_alloy_ingot");

    public static final RoughPrismarineCrystal ROUGH_PRISMARINE_CRYSTAL = ITEMS.register("rough_prismarine_crystal", new RoughPrismarineCrystal());
    public static final Item POLISHED_PRISMARINE_CRYSTAL = ITEMS.registerSimple("polished_prismarine_crystal");
    public static final Item SHATTERED_PRISMARINE_CRYSTAL = ITEMS.registerSimple("shattered_prismarine_crystal");
    public static final Item PRISMARINE_PEARL = ITEMS.registerSimple("prismarine_pearl");

    public static void initialize() {
        ITEMS.finalizeRegistration();
    }
}
