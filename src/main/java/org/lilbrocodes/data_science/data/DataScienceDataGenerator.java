package org.lilbrocodes.data_science.data;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import org.lilbrocodes.data_science.data.assets.DataScienceLanguageProvider;
import org.lilbrocodes.data_science.data.assets.DataScienceModelProvider;
import org.lilbrocodes.data_science.data.data.DataScienceBlockLootProvider;
import org.lilbrocodes.data_science.data.data.DataScienceBlockTagProvider;
import org.lilbrocodes.data_science.data.data.DataScienceRecipeProvider;

public class DataScienceDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator generator) {
        FabricDataGenerator.Pack pack = generator.createPack();

        pack.addProvider(DataScienceModelProvider::new);
        pack.addProvider(DataScienceLanguageProvider::new);

        pack.addProvider(DataScienceBlockLootProvider::new);
        pack.addProvider(DataScienceRecipeProvider::new);
        pack.addProvider(DataScienceBlockTagProvider::new);
    }
}
