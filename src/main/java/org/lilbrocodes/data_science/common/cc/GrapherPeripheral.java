package org.lilbrocodes.data_science.common.cc;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.util.Colour;
import dan200.computercraft.shared.peripheral.monitor.MonitorPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

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

    private MonitorPeripheral getMonitor() {
        if (serverWorld == null) return null;
        return PeripheralFinder.find(world, pos, MonitorPeripheral.class).orElse(null);
    }

    private Terminal getTerminal() {
        try {
            MonitorPeripheral monitor = getMonitor();
            if (monitor == null) return null;
            return monitor.getTerminal();
        } catch (LuaException ignored) {
            return null;
        }
    }

    @LuaFunction(mainThread = true)
    public final void clearMonitor(String text) {
        Terminal term = getTerminal();
        if (term == null) return;
        term.clear();
    }

    @LuaFunction(mainThread = true)
    public final void barGraph(Object dataset) {
        Terminal term = getTerminal();
        if (term == null) return;

        term.clear();
        term.setCursorPos(1, 1);

        if (!(dataset instanceof Map<?, ?> table)) {
            term.setTextColour(Colour.RED.getHex());
            term.write("Dataset must be a Lua table (key -> number).");
            return;
        }

        if (table.isEmpty()) {
            term.setTextColour(Colour.RED.getHex());
            term.write("Dataset is empty.");
            return;
        }

        for (Map.Entry<?, ?> entry : table.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();

            if (key == null) {
                term.setTextColour(Colour.RED.getHex());
                term.write("Dataset contains a null key.");
                return;
            }

            if (value == null) {
                term.setTextColour(Colour.RED.getHex());
                term.write("Dataset value for key '" + key + "' is null.");
                return;
            }

            if (!(value instanceof Number)) {
                term.setTextColour(Colour.RED.getHex());
                term.write("Dataset value for key '" + key + "' is not numeric: " + value);
                return;
            }
        }

        term.setTextColour(Colour.WHITE.getHex());
        int width = term.getWidth();
        int height = term.getHeight();

        int index = 0;
        int max = table.values().stream().mapToInt(v -> ((Number) v).intValue()).max().orElse(1);

        for (Map.Entry<?, ?> entry : table.entrySet()) {
            Object key = entry.getKey();
            int value = ((Number) entry.getValue()).intValue();

            int barHeight = (int) ((double) value / max * height);

            for (int y = height - 1; y >= height - barHeight; y--) {
                if (index < width) {
                    term.setCursorPos(index + 1, y + 1);
                    term.write("█");
                }
            }
            index++;
            if (index >= width) break;
        }
    }

}
