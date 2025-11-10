package org.lilbrocodes.data_science.common.blocks.entity;

import dan200.computercraft.api.lua.LuaException;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.lilbrocodes.data_science.common.dataforge.Dataset;
import org.lilbrocodes.data_science.common.dataforge.DatasetOperation;
import org.lilbrocodes.data_science.common.registry.ModBlockEntities;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

public class DataforgeBlockEntity extends BlockEntity {
    public static final String CSV_MODE = "csvMode";

    public boolean csvMode = false;
    private final List<Pair<DatasetOperation, IterationState>> queue = new ArrayList<>();
    private final Stack<Dataset> results = new Stack<>();

    public DataforgeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.DATAFORGE, pos, state);
    }

    public void push(DatasetOperation operation) {
        queue.add(new Pair<>(operation, null));
    }

    public Dataset pop() {
        if (results.empty()) return null;
        else return results.pop();
    }

    public static void tick(World world, BlockPos pos, BlockState state, DataforgeBlockEntity be) {
        Iterator<Pair<DatasetOperation, IterationState>> iterator = be.queue.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            Pair<DatasetOperation, IterationState> entry = iterator.next();

            DatasetOperation op = entry.getLeft();
            IterationState prevState = entry.getRight();

            Pair<Dataset, IterationState> result = op.iterate(prevState);

            Dataset dataset = result.getLeft();
            IterationState next = result.getRight();

            if (next.completed) {
                be.results.push(dataset);
                iterator.remove();
            } else {
                be.queue.set(i, new Pair<>(op, next));
            }

            i++;
        }
    }


    @Override
    protected void writeNbt(NbtCompound tag) {
        tag.putBoolean(CSV_MODE, csvMode);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        csvMode = tag.getBoolean(CSV_MODE);
    }

    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public static class IterationState {
        public Object data;
        public boolean completed;

        public IterationState(Object data, boolean completed) {
            this.data = data;
            this.completed = completed;
        }
    }
}
