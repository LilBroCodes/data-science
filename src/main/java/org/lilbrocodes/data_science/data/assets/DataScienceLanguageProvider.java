package org.lilbrocodes.data_science.data.assets;


import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import org.lilbrocodes.data_science.common.registry.ModBlocks;
import org.lilbrocodes.data_science.common.registry.ModItemGroups;
import org.lilbrocodes.data_science.common.registry.ModItems;

public class DataScienceLanguageProvider extends FabricLanguageProvider {
    public DataScienceLanguageProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        builder.add(ModBlocks.ENVIRONMENTAL_SENSOR.block, "Environmental Sensor");
        builder.add(ModBlocks.GRAPHER.block, "Grapher");

        builder.add(ModItems.RAW_BASIC_CIRCUIT, "Raw Basic Circuit");
        builder.add(ModItems.ASSEMBLED_ADVANCED_CIRCUIT, "Raw Advanced Circuit");
        builder.add(ModItems.BASIC_CIRCUIT, "Basic Circuit");
        builder.add(ModItems.ADVANCED_CIRCUIT, "Advanced Circuit");
        builder.add(ModItems.IRON_DUST, "Iron Dust");
        builder.add(ModItems.COPPER_DUST, "Copper Dust");
        builder.add(ModItems.RED_ALLOY_DUST, "Red Alloy Dust");
        builder.add(ModItems.RED_ALLOY_INGOT, "Red Alloy Ingot");
        builder.add(ModItems.ROUGH_PRISMARINE_CRYSTAL, "Roughly Polished Prismarine Crystal");
        builder.add(ModItems.POLISHED_PRISMARINE_CRYSTAL, "Polished Prismarine Crystal");
        builder.add(ModItems.SHATTERED_PRISMARINE_CRYSTAL, "Shattered Prismarine Crystal");
        builder.add(ModItems.PRISMARINE_PEARL, "Prismarine Pearl");

        builder.add(ModItemGroups.DATA_SCIENCE_GROUP, "Data Science");

        builder.add("jei.data_science.category.grinding", "Item Grinding");
        builder.add("text.data_science.crumble_rate", "%s%% chance of crumbling");
        builder.add("tooltip.data_science.polishing_progress", "Polishing Progress");
    }
}
