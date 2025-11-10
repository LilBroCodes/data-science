package org.lilbrocodes.data_science.common.dataforge;

import net.minecraft.util.Pair;
import org.lilbrocodes.data_science.common.blocks.entity.DataforgeBlockEntity.IterationState;

@FunctionalInterface
public interface OperationBinder {
    Pair<Dataset, IterationState> iterate(Object localData, Dataset dataset, Object... args);

    default DatasetOperation bind(Dataset set, Object... args) {
        return new DatasetOperation(set, args, this);
    }
}
