package org.lilbrocodes.data_science.common.registry;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredItemRegistry;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.item.RoughPrismarineCrystal;

public class ModItems {
    private static final DeferredItemRegistry ITEMS = new DeferredItemRegistry(DataScience.MOD_ID, ModItemGroups.DATA_SCIENCE_GROUP);

    // Blocks
    public static final BlockItem GRAPHER = ITEMS.register(ModBlocks.GRAPHER, "grapher");
    public static final BlockItem ENVIRONMENTAL_SENSOR = ITEMS.register(ModBlocks.ENVIRONMENTAL_SENSOR, "environmental_sensor");
    public static final BlockItem DATAFORGE = ITEMS.register(ModBlocks.DATAFORGE, "dataforge");

    // Items
    public static final Item RAW_BASIC_CIRCUIT = ITEMS.register("raw_basic_circuit");
    public static final Item BASIC_CIRCUIT = ITEMS.register("basic_circuit");
    public static final Item ASSEMBLED_ADVANCED_CIRCUIT = ITEMS.register("assembled_advanced_circuit");
    public static final Item ADVANCED_CIRCUIT = ITEMS.register("advanced_circuit");

    public static final Item IRON_DUST = ITEMS.register("iron_dust");
    public static final Item COPPER_DUST = ITEMS.register("copper_dust");
    public static final Item RED_ALLOY_DUST = ITEMS.register("red_alloy_dust");
    public static final Item RED_ALLOY_INGOT = ITEMS.register("red_alloy_ingot");

    public static final Item ROUGH_PRISMARINE_CRYSTAL = ITEMS.register("rough_prismarine_crystal", new RoughPrismarineCrystal());
    public static final Item POLISHED_PRISMARINE_CRYSTAL = ITEMS.register("polished_prismarine_crystal");
    public static final Item SHATTERED_PRISMARINE_CRYSTAL = ITEMS.register("shattered_prismarine_crystal");
    public static final Item PRISMARINE_PEARL = ITEMS.register("prismarine_pearl");

    public static void initialize() {
        ITEMS.finalizeRegistration();
    }
}
