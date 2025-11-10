package org.lilbrocodes.data_science.common.registry;

import net.minecraft.block.entity.BlockEntityType;
import org.lilbrocodes.composer_reloaded.api.registry.lazy.DeferredBlockEntityRegistry;
import org.lilbrocodes.data_science.common.DataScience;
import org.lilbrocodes.data_science.common.blocks.entity.DataforgeBlockEntity;

public class ModBlockEntities {
    private static final DeferredBlockEntityRegistry REGISTRY = new DeferredBlockEntityRegistry(DataScience.MOD_ID);

    public static final BlockEntityType<DataforgeBlockEntity> DATAFORGE = REGISTRY.register(
            "dataforge", DataforgeBlockEntity::new, ModBlocks.DATAFORGE
    );

    public static void initialize() {

    }
}
