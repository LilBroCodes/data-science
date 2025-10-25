package org.lilbrocodes.data_science.common.cc;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Optional;

public class PeripheralFinder {
    public static <T extends IPeripheral> Optional<T> find(World world, BlockPos pos, Class<T> peripheralClass) {
        BlockApiLookup<IPeripheral, Direction> lookup = PeripheralLookup.get();

        for (Direction dir : Direction.values()) {
            BlockPos checkPos = pos.offset(dir);
            Optional<T> peripheral = isAt(world, checkPos, lookup, dir, peripheralClass);
            if (peripheral.isPresent()) return peripheral;
        }

        return Optional.empty();
    }

    public static <T extends IPeripheral> Optional<T> isAt(World world, BlockPos pos, BlockApiLookup<IPeripheral, Direction> lookup, Direction ctx, Class<T> peripheralClass) {
        BlockState state = world.getBlockState(pos);
        BlockApiLookup.BlockApiProvider<IPeripheral, Direction> provider = lookup.getProvider(state.getBlock());
        IPeripheral instance = null;

        if (provider != null) {
            instance = provider.find(world, pos, state, world.getBlockEntity(pos), ctx);
        }

        if (peripheralClass.isInstance(instance)) {
            return Optional.of(peripheralClass.cast(instance));
        }

        return Optional.empty();
    }
}
