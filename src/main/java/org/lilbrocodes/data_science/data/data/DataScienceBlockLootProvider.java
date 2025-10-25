package org.lilbrocodes.data_science.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import org.lilbrocodes.data_science.common.registry.ModBlocks;

public class DataScienceBlockLootProvider extends FabricBlockLootTableProvider {
    public DataScienceBlockLootProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        addDrop(ModBlocks.ENVIRONMENTAL_SENSOR.block);
        addDrop(ModBlocks.GRAPHER.block);
    }
}
