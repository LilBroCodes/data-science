package org.lilbrocodes.data_science.common.registry;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockRegistry.BlockWithItem;
import org.lilbrocodes.data_science.common.DataScience;

public class ModBlocks {
    private static final DeferredBlockRegistry BLOCKS = new DeferredBlockRegistry(DataScience.MOD_ID, ModItemGroups.DATA_SCIENCE_GROUP);

    public static final BlockWithItem<Block> ENVIRONMENTAL_SENSOR = BLOCKS.register(
            "environmental_sensor",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK))
    );

    public static final BlockWithItem<Block> GRAPHER = BLOCKS.register(
            "grapher",
            new Block(FabricBlockSettings.copyOf(Blocks.IRON_BLOCK))
    );

    public static void initialize() {
        BLOCKS.finalizeRegistration();
    }
}
