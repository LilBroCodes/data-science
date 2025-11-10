package org.lilbrocodes.data_science.common.dataforge;

import net.minecraft.util.Pair;
import org.lilbrocodes.data_science.common.blocks.entity.DataforgeBlockEntity.IterationState;

public record DatasetOperation(Dataset dataset, Object[] args, OperationBinder action) {
    public Pair<Dataset, IterationState> iterate(Object localData) {
        return action.iterate(localData, dataset, args);
    }
}
