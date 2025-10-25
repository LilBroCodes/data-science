package org.lilbrocodes.data_science.common.registry;

import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import org.lilbrocodes.data_science.common.cc.EnvironmentalSensorPeripheral;
import org.lilbrocodes.data_science.common.cc.GrapherPeripheral;
import org.lilbrocodes.data_science.common.cc.InventoryMethods;
import org.lilbrocodes.data_science.common.cc.MapHandlerAPI;

public class ModCCComponents {
    public static void initialize() {
        ComputerCraftAPI.registerGenericSource(new InventoryMethods());
        ComputerCraftAPI.registerAPIFactory(MapHandlerAPI::new);

        PeripheralLookup.get().registerForBlocks(EnvironmentalSensorPeripheral::new, ModBlocks.ENVIRONMENTAL_SENSOR.block);
        PeripheralLookup.get().registerForBlocks(GrapherPeripheral::new, ModBlocks.GRAPHER.block);
    }
}
