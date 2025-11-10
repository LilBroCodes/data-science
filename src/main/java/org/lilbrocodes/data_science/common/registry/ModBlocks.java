package org.lilbrocodes.data_science.common.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.blocks.DataforgeBlock;

public class ModBlocks {
    private static final DeferredBlockRegistry BLOCKS = new DeferredBlockRegistry(DataScience.MOD_ID);
    private static final AbstractBlock.Settings MACHINE_SETTINGS = FabricBlockSettings.copyOf(Blocks.IRON_BLOCK);

    public static final Block ENVIRONMENTAL_SENSOR = BLOCKS.register(
            "environmental_sensor",
            new Block(MACHINE_SETTINGS)
    );

    public static final Block GRAPHER = BLOCKS.register(
            "grapher",
            new Block(MACHINE_SETTINGS)
    );

    public static final Block DATAFORGE = BLOCKS.register(
            "dataforge",
            new DataforgeBlock(MACHINE_SETTINGS)
    );

    public static void initialize() {

    }
}
