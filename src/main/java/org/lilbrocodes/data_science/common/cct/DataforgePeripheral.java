package org.lilbrocodes.data_science.common.cct;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;
import org.lilbrocodes.data_science.common.blocks.entity.DataforgeBlockEntity;
import org.lilbrocodes.data_science.common.blocks.entity.DataforgeBlockEntity.IterationState;
import org.lilbrocodes.data_science.common.dataforge.Dataset;
import org.lilbrocodes.data_science.common.dataforge.DatasetLoader;
import org.lilbrocodes.data_science.common.dataforge.OperationBinder;
import org.lilbrocodes.data_science.common.util.ArgumentValidator;

import java.util.List;

public class DataforgePeripheral implements IPeripheral {
    private final DataforgeBlockEntity be;

    public DataforgePeripheral(BlockEntity be, Direction direction) {
        if (be instanceof DataforgeBlockEntity entity) {
            this.be = entity;
        } else {
            throw new RuntimeException("Dataforge peripheral is missing a block entity!");
        }
    }

    @Override
    public String getType() {
        return "dataforge";
    }

    private Dataset parseDataset(String dataset) throws LuaException {
        if (be.csvMode) {
            return DatasetLoader.fromCsv(dataset);
        } else {
            return DatasetLoader.fromJsonArray(dataset);
        }
    }

    private String serializeDataset(Dataset dataset) {
        if (be.csvMode) {
            return DatasetLoader.toCsv(dataset);
        } else {
            return DatasetLoader.toJsonArray(dataset);
        }
    }

    @LuaFunction
    public final void head(String dataString, int n) throws LuaException {
        Dataset dataset = parseDataset(dataString);
        be.push(Operations.HEAD.bind(dataset, n));
    }

    @LuaFunction
    public final void tail(String dataString, int n) throws LuaException {
        Dataset dataset = parseDataset(dataString);
        be.push(Operations.TAIL.bind(dataset, n));
    }

    @LuaFunction
    public final List<Integer> getShape(String dataString) throws LuaException {
        Dataset dataset = parseDataset(dataString);
        return dataset.luaShape();
    }

    @LuaFunction
    public final List<String> getColumns(String dataString) throws LuaException {
        Dataset dataset = parseDataset(dataString);
        return dataset.getColumns();
    }

    @LuaFunction
    public final void select(String dataString, List<String> columns) {

    }

    @LuaFunction
    public final String pop() {
        Dataset set = be.pop();
        return set != null ? serializeDataset(set) : null;
    }

    @LuaFunction
    public final void jsonMode() {
        be.csvMode = false;
    }

    @LuaFunction
    public final void csvMode() {
        be.csvMode = true;
    }

    @LuaFunction
    public final boolean isJsonMode() {
        return !be.csvMode;
    }

    @LuaFunction
    public final boolean isCsvMode() {
        return be.csvMode;
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this.equals((Object) other);
    }

    public static final class Operations extends ArgumentValidator {
        public static final OperationBinder HEAD = (data, dataset, args) -> {
            List<Object> params = validateArgs(args, Integer.class);
            Integer n = (Integer) params.get(0);

            return new Pair<>(dataset.head(n), new IterationState(data, true));
        };

        public static final OperationBinder TAIL = (data, dataset, args) -> {
            List<Object> params = validateArgs(args, Integer.class);
            Integer n = (Integer) params.get(0);

            return new Pair<>(dataset.tail(n), new IterationState(data, true));
        };
    }
}
