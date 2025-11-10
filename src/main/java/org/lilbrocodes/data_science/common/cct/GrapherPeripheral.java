package org.lilbrocodes.data_science.common.cct;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.shared.computer.blocks.AbstractComputerBlockEntity;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.peripheral.monitor.MonitorPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public class GrapherPeripheral implements IPeripheral {
    private final World world;
    private final ServerWorld serverWorld;
    private final BlockPos pos;

    public GrapherPeripheral(World world, BlockPos pos, BlockState ignored, BlockEntity ignored1, Direction ignored2) {
        this.world = world;
        this.pos = pos;
        this.serverWorld = world instanceof ServerWorld sw ? sw : null;
    }

    @Override
    public String getType() {
        return "grapher";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other == this;
    }

    private AbstractComputerBlockEntity getComputer() {
        return PeripheralFinder.find(world, pos, AbstractComputerBlockEntity.class).orElse(null);
    }

    private MonitorPeripheral getMonitor() {
        AbstractComputerBlockEntity computer = getComputer();
        if (computer == null) return null;
        ServerComputer sc = computer.getServerComputer();
        if (sc == null) return null;
        for (ComputerSide side : ComputerSide.values()) {
            if (sc.getPeripheral(side) instanceof MonitorPeripheral monitor) return monitor;
        }
        return null;
    }

    private Terminal getTerminal() {
        MonitorPeripheral monitor = getMonitor();
        if (monitor == null) return null;
        try {
            return monitor.getTerminal();
        } catch (LuaException ignored) {
            return null;
        }
    }
}
