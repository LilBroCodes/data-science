package org.lilbrocodes.data_science.data.data;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import org.lilbrocodes.data_science.common.registry.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class DataScienceBlockTagProvider extends FabricTagProvider.BlockTagProvider {
    public DataScienceBlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE)
                .add(ModBlocks.ENVIRONMENTAL_SENSOR.block)
                .add(ModBlocks.GRAPHER.block);
    }
}
