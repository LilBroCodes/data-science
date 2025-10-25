package org.lilbrocodes.data_science.common.cc;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.GenericSource;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryMethods implements GenericSource {
    @Override
    public String id() {
        return "advanced_chest_data:inventory";
    }

    @LuaFunction(mainThread = true)
    public static int getSize(Inventory inventory) {
        return inventory.size();
    }

    @LuaFunction(mainThread = true)
    public static Object getStack(Inventory inventory, int slot) {
        if (slot <= 0 || slot > inventory.size()) return null;
        ItemStack stack = inventory.getStack(slot - 1);
        if (stack.isEmpty()) return null;

        NbtCompound tag = stack.writeNbt(new NbtCompound());
        return nbtToMap(tag);
    }

    private static Object nbtToMap(NbtElement nbt) {
        switch (nbt.getType()) {
            case NbtElement.COMPOUND_TYPE:
                Map<Object, Object> map = new HashMap<>();
                NbtCompound compound = (NbtCompound) nbt;
                for (String key : compound.getKeys()) {
                    var v = compound.get(key);
                    if (v != null) map.put(key, nbtToMap(v));
                }
                return map;
            case NbtElement.LIST_TYPE:
                Map<Object, Object> list = new HashMap<>();
                int i = 1;
                for (NbtElement e : (net.minecraft.nbt.NbtList) nbt) {
                    list.put(i++, nbtToMap(e));
                }
                return list;
            case NbtElement.STRING_TYPE:
                return nbt.asString();
            case NbtElement.INT_TYPE:
                return ((NbtInt) nbt).intValue();
            case NbtElement.FLOAT_TYPE:
                return ((NbtFloat) nbt).floatValue();
            case NbtElement.DOUBLE_TYPE:
                return ((NbtDouble) nbt).doubleValue();
            case NbtElement.BYTE_TYPE:
                return ((NbtByte) nbt).byteValue();
            case NbtElement.LONG_TYPE:
                return ((NbtLong) nbt).longValue();
            default:
                return nbt.toString();
        }
    }

    @LuaFunction(mainThread = true)
    public static double getFullness(Inventory inventory) {
        int size = inventory.size();
        int full = 0;
        for (int i = 0; i < size; i++) {
            if (!inventory.getStack(i).isEmpty()) full++;
        }
        return (double) full / size;
    }

    @LuaFunction(mainThread = true)
    public static Object getAllStacks(Inventory inventory) {
        Map<Integer, Object> result = new HashMap<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) {
                result.put(i + 1, stack.writeNbt(new NbtCompound()).toString());
            }
        }
        return result;
    }

    @LuaFunction(mainThread = true)
    public static int getUniqueItemCount(Inventory inventory) {
        Set<Item> items = new HashSet<>();
        for (int i = 0; i < inventory.size(); i++) {
            ItemStack stack = inventory.getStack(i);
            if (!stack.isEmpty()) items.add(stack.getItem());
        }
        return items.size();
    }

    @LuaFunction(mainThread = true)
    public static double getRoughEntropy(Inventory inventory) {
        return Math.log(fact(getUniqueItemCount(inventory)));
    }

    @LuaFunction(mainThread = true)
    public static double getAdvancedEntropy(Inventory inventory) {
        int size = inventory.size();
        if (size == 0) return 0;

        Map<String, Integer> counts = new HashMap<>();
        for (int i = 0; i < size; i++) {
            ItemStack stack = inventory.getStack(i);
            if (stack.isEmpty()) continue;
            String key = stack.getItem().toString() + ":" + stack.writeNbt(new NbtCompound());
            counts.put(key, counts.getOrDefault(key, 0) + stack.getCount());
        }

        double adjacencyFactor = 1.0;
        for (int i = 1; i < size; i++) {
            ItemStack prev = inventory.getStack(i - 1);
            ItemStack curr = inventory.getStack(i);
            if (!prev.isEmpty() && !curr.isEmpty() && prev.getItem() == curr.getItem()) {
                adjacencyFactor *= 0.9;
            }
        }

        double entropy = 0;
        int total = counts.values().stream().mapToInt(Integer::intValue).sum();
        for (int count : counts.values()) {
            double p = (double) count / total;
            entropy -= p * Math.log(p);
        }

        return entropy * adjacencyFactor;
    }

    private static double fact(double n) {
        if (n <= 1) return 1;
        return n * fact(n - 1);
    }
}
