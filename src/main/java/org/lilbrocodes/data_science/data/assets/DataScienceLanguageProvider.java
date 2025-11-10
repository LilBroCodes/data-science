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
        builder.add(ModBlocks.ENVIRONMENTAL_SENSOR, "Environmental Sensor");
        builder.add(ModBlocks.GRAPHER, "Grapher");
        builder.add(ModBlocks.DATAFORGE, "Dataforge");

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

        builder.add("advancements.data_science.root.title", "Data Science");
        builder.add("advancements.data_science.crumble.title", "Diminishing Returns");
        builder.add("advancements.data_science.shatter.title", "Shattered");
        builder.add("advancements.data_science.grinding.title", "Grinding Away");
        builder.add("advancements.data_science.unlucky.title", "Unlucky!");
        builder.add("advancements.data_science.basic_circuit.title", "Compact Technology");
        builder.add("advancements.data_science.advanced_circuit.title", "Modern Technology");
        builder.add("advancements.data_science.environmental_sensor.title", "Beacon Not Included!");
        builder.add("advancements.data_science.grapher.title", "R-Rated");
        builder.add("advancements.data_science.red_alloy_craft.title", "Not Very Thrilling");
        builder.add("advancements.data_science.red_alloy_smelt.title", "Very Shocking");
        builder.add("advancements.data_science.polished_crystal.title", "Crystal Clear");

        builder.add("advancements.data_science.root.description", "Welcome to a wonderful new world of computing!");
        builder.add("advancements.data_science.crumble.description", "Have a grinding recipe fail.");
        builder.add("advancements.data_science.shatter.description", "Shatter an item while attempting to polish it.");
        builder.add("advancements.data_science.grinding.description", "Grind down an item to dust.");
        builder.add("advancements.data_science.unlucky.description", "Shatter five items in a row while attempting to polish them.");
        builder.add("advancements.data_science.basic_circuit.description", "Create your first basic circuit.");
        builder.add("advancements.data_science.advanced_circuit.description", "Create your first advanced circuit.");
        builder.add("advancements.data_science.environmental_sensor.description", "Automate looking up by creating an environmental sensor.");
        builder.add("advancements.data_science.grapher.description", "Craft a grapher to create graphic content from words.");
        builder.add("advancements.data_science.red_alloy_craft.description", "Create some red powder by mixing the three best conductors found around the world.");
        builder.add("advancements.data_science.red_alloy_smelt.description", "Create some super-conductive material by blasting down your red powder.");
        builder.add("advancements.data_science.polished_crystal.description", "Successfully polish a prismarine crystal.");
    }
}
