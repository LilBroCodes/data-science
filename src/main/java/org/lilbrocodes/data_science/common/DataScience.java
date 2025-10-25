package org.lilbrocodes.data_science.common;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Identifier;
import org.lilbrocodes.data_science.common.registry.*;

public class DataScience implements ModInitializer {
    public static String MOD_ID = "data_science";
    public static MinecraftServer SERVER;

    @Override
    public void onInitialize() {
        ModCCComponents.initialize();
        ModItemGroups.initialize();
        ModRecipes.initialize();
        ModBlocks.initialize();
        ModItems.initialize();

        ServerLifecycleEvents.SERVER_STARTED.register(DataScience::setServer);
    }

    public static Identifier identify(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void setServer(MinecraftServer server) {
        SERVER = server;
    }
}
