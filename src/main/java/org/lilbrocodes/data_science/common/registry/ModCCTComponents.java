package org.lilbrocodes.data_science.common.registry;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import org.lilbrocodes.data_science.common.cct.DataforgePeripheral;
import org.lilbrocodes.data_science.common.cct.EnvironmentalSensorPeripheral;
import org.lilbrocodes.data_science.common.cct.InventoryMethods;
import org.lilbrocodes.data_science.common.cct.MapHandlerAPI;

public class ModCCTComponents {
    public static void initialize() {
        ComputerCraftAPI.registerGenericSource(new InventoryMethods());
        ComputerCraftAPI.registerAPIFactory(MapHandlerAPI::new);

        PeripheralLookup.get().registerForBlocks(EnvironmentalSensorPeripheral::new, ModBlocks.ENVIRONMENTAL_SENSOR);
//        PeripheralLookup.get().registerForBlocks(GrapherPeripheral::new, ModBlocks.GRAPHER);
        PeripheralLookup.get().registerForBlockEntities(DataforgePeripheral::new, ModBlockEntities.DATAFORGE);
    }
}
