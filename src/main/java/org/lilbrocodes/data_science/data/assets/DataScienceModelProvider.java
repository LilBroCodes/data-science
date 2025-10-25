package org.lilbrocodes.data_science.data.assets;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.block.Block;
import net.minecraft.data.DataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;
import net.minecraft.data.client.*;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;
import org.lilbrocodes.data_science.common.registry.ModBlocks;
import org.lilbrocodes.data_science.common.registry.ModItems;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class DataScienceModelProvider implements DataProvider {
    private final DataOutput.PathResolver blockstatesPathResolver;
    private final DataOutput.PathResolver modelsPathResolver;

    public DataScienceModelProvider(FabricDataOutput output) {
        this.blockstatesPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "blockstates");
        this.modelsPathResolver = output.getResolver(DataOutput.OutputType.RESOURCE_PACK, "models");
    }

    public static void generateBlocks(BlockStateModelGenerator generator) {
        generator.registerSingleton(ModBlocks.ENVIRONMENTAL_SENSOR.block, TexturedModel.CUBE_COLUMN);
        generator.registerSingleton(ModBlocks.GRAPHER.block, TexturedModel.CUBE_COLUMN);
    }

    public static void generateItems(ItemModelGenerator generator) {
        generator.register(ModItems.RAW_BASIC_CIRCUIT, Models.GENERATED);
        generator.register(ModItems.ASSEMBLED_ADVANCED_CIRCUIT, Models.GENERATED);
        generator.register(ModItems.BASIC_CIRCUIT, Models.GENERATED);
        generator.register(ModItems.ADVANCED_CIRCUIT, Models.GENERATED);
        generator.register(ModItems.IRON_DUST, Models.GENERATED);
        generator.register(ModItems.COPPER_DUST, Models.GENERATED);
        generator.register(ModItems.RED_ALLOY_DUST, Models.GENERATED);
        generator.register(ModItems.RED_ALLOY_INGOT, Models.GENERATED);
        generator.register(ModItems.ROUGH_PRISMARINE_CRYSTAL, Models.GENERATED);
        generator.register(ModItems.POLISHED_PRISMARINE_CRYSTAL, Models.GENERATED);
        generator.register(ModItems.SHATTERED_PRISMARINE_CRYSTAL, Models.GENERATED);
        generator.register(ModItems.PRISMARINE_PEARL, Models.GENERATED);
    }

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Map<Block, BlockStateSupplier> blockStates = new HashMap<>();
        Consumer<BlockStateSupplier> blockConsumer = blockStateSupplier -> {
            Block block = blockStateSupplier.getBlock();
            if (blockStates.put(block, blockStateSupplier) != null) {
                throw new IllegalStateException("Duplicate blockstate definition for " + block);
            }
        };

        Map<Identifier, Supplier<com.google.gson.JsonElement>> models = new HashMap<>();
        BiConsumer<Identifier, Supplier<com.google.gson.JsonElement>> modelConsumer = (id, supplier) -> {
            if (models.put(id, supplier) != null) {
                throw new IllegalStateException("Duplicate model definition for " + id);
            }
        };

        Set<Item> itemModels = new HashSet<>();
        Consumer<Item> itemConsumer = itemModels::add;

        generateBlocks(new BlockStateModelGenerator(blockConsumer, modelConsumer, itemConsumer));
        generateItems(new ItemModelGenerator(modelConsumer));

        return CompletableFuture.allOf(
                writeJsons(writer, blockStates, b -> blockstatesPathResolver.resolveJson(b.getRegistryEntry().registryKey().getValue())),
                writeJsons(writer, models, modelsPathResolver::resolveJson)
        );
    }

    private <T> CompletableFuture<?> writeJsons(DataWriter writer, Map<T, ? extends Supplier<com.google.gson.JsonElement>> map, Function<T, Path> pathGetter) {
        return CompletableFuture.allOf(map.entrySet().stream()
                .map(entry -> DataProvider.writeToPath(writer, entry.getValue().get(), pathGetter.apply(entry.getKey())))
                .toArray(CompletableFuture[]::new));
    }

    @Override
    public String getName() {
        return "Reforestry Model Provider";
    }
}
