package org.lilbrocodes.data_science.cca;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.entity.player.PlayerEntity;
import org.lilbrocodes.data_science.cca.basic.IntComponent;
import org.lilbrocodes.data_science.common.DataScience;

public class DataScienceCardinalComponents implements EntityComponentInitializer {
    public static ComponentKey<IntComponent> SHATTERED = ComponentRegistry.getOrCreate(DataScience.SHATTERED_PACKET, IntComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.beginRegistration(PlayerEntity.class, SHATTERED).respawnStrategy(RespawnCopyStrategy.ALWAYS_COPY).end(IntComponent::new);
    }
}
