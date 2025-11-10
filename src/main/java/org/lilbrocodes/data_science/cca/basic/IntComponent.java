package org.lilbrocodes.data_science.cca.basic;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;

public class IntComponent implements Component {
    private Integer value = 0;

    public IntComponent(PlayerEntity ignored) {

    }

    @Override
    public void readFromNbt(@NotNull NbtCompound tag) {
        value = tag.getInt("value");
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        tag.putInt("value", value);
    }

    public void setValue(int n) {
        this.value = n;
    }

    public int getValue() {
        return value;
    }
}
