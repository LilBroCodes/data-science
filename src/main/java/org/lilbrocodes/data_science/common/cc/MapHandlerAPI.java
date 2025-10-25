package org.lilbrocodes.data_science.common.cc;

import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.block.MapColor;
import net.minecraft.item.map.MapIcon;
import net.minecraft.item.map.MapState;
import net.minecraft.registry.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import org.lilbrocodes.data_science.common.DataScience;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class MapHandlerAPI implements ILuaAPI {
    public MapHandlerAPI(IComputerSystem ignored) { }

    @Override
    public String[] getNames() {
        return new String[]{"maps"};
    }

    public static Object getPrivateField(MapState state, String name) {
        try {
            Field field = MapState.class.getDeclaredField(name);
            field.setAccessible(true);
            return field.get(state);
        } catch (Exception e) {
            return null;
        }
    }

    public static ServerWorld getWorldById(MinecraftServer server, String dimensionId) {
        Identifier id = new Identifier(dimensionId);
        RegistryKey<World> dimKey = RegistryKey.of(RegistryKeys.WORLD, id);
        return server.getWorld(dimKey);
    }

    public static Map<Object, Object> putIcons(MapState state, Map<Object, Object> icons, Function<MapIcon, Object> put) {
        int i = icons.size() - 1;
        for (MapIcon icon : state.getIcons()) {
            var data = put.apply(icon);
            if (data != null) icons.put(i++, data);
        }
        return icons;
    }

    public static MapState getMapState(String dimension, int mapId) {
        if (DataScience.SERVER == null) return null;
        ServerWorld world = getWorldById(DataScience.SERVER, dimension);
        if (world == null) return null;

        return world.getMapState("map_" + mapId);
    }

    public static boolean isPlayerIcon(MapIcon icon) {
        return switch (icon.getType()) {
            case PLAYER, PLAYER_OFF_MAP, PLAYER_OFF_LIMITS -> true;
            default -> false;
        };
    }

    public static Object getFilteredIcons(String dimension, int mapId, Predicate<MapIcon> filter, Function<MapIcon, Map<String, Object>> mapper) {
        MapState state = getMapState(dimension, mapId);
        if (state == null) return null;

        Map<Object, Object> iconsTable = new HashMap<>();
        int index = 1;

        for (MapIcon icon : state.getIcons()) {
            if (filter.test(icon)) {
                iconsTable.put(index++, mapper.apply(icon));
            }
        }

        return iconsTable;
    }

    public static Map<String, Object> makeIconData(MapIcon icon, boolean includeType) {
        Map<String, Object> data = new HashMap<>();
        if (includeType) data.put("type", icon.getType().name());
        data.put("x", icon.getX());
        data.put("y", icon.getZ());
        data.put("rotation", icon.getRotation());
        data.put("displayName", icon.getText() != null ? icon.getText().getString() : null);
        return data;
    }

    public static Map<String, Object> makeIconData(MapIcon icon) {
        return makeIconData(icon, false);
    }

    @LuaFunction(mainThread = true)
    public final Object getMapData(String dimension, int mapId) {
        MapState state = getMapState(dimension, mapId);
        if (state == null) return null;

        byte[] colors = state.colors;
        Map<Object, Object> table = new HashMap<>();

        for (int y = 0; y < 128; y++) {
            Map<Object, Object> row = new HashMap<>();
            for (int x = 0; x < 128; x++) {
                int index = x + y * 128;
                int colorByte = colors[index] & 0xFF;

                int colorId = colorByte / 4;
                int brightness = colorByte & 3;

                Map<String, Integer> pixelData = new HashMap<>();
                pixelData.put("colorId", colorId);
                pixelData.put("brightness", brightness);

                row.put(x + 1, pixelData);
            }
            table.put(128 - y, row); // vertical flip
        }

        return table;
    }

    @LuaFunction(mainThread = true)
    public final Object getMapColors(String dimension, int mapId) {
        Object mapDataObj = getMapData(dimension, mapId);
        if (!(mapDataObj instanceof Map<?, ?> mapData)) return null;

        Map<Object, Object> rgbTable = new HashMap<>();

        for (Map.Entry<?, ?> rowEntry : mapData.entrySet()) {
            Object yKey = rowEntry.getKey();
            Object rowObj = rowEntry.getValue();
            if (!(rowObj instanceof Map<?, ?> row)) continue;

            Map<Object, Object> rgbRow = new HashMap<>();
            for (Map.Entry<?, ?> colEntry : row.entrySet()) {
                Object xKey = colEntry.getKey();
                Object pixelObj = colEntry.getValue();
                if (!(pixelObj instanceof Map<?, ?> pixel)) continue;

                Object colorIdObj = pixel.get("colorId");
                Object brightnessObj = pixel.get("brightness");
                if (!(colorIdObj instanceof Number colorId) || !(brightnessObj instanceof Number brightness)) continue;

                int colorIndex = colorId.intValue();
                int brightnessLevel = brightness.intValue();

                if (colorIndex >= 61 || colorIndex < 0) {
                    rgbRow.put(xKey, 0);
                    continue;
                }

                int baseColor = MapColor.get(colorIndex).color;
                int r = (baseColor >> 16) & 0xFF;
                int g = (baseColor >> 8) & 0xFF;
                int b = baseColor & 0xFF;

                float brightnessMul;
                switch (brightnessLevel) {
                    case 0 -> brightnessMul = 0.71f;
                    case 1 -> brightnessMul = 0.86f;
                    case 3 -> brightnessMul = 0.53f;
                    default -> brightnessMul = 1.0f;
                }

                int finalR = Math.min(255, Math.round(r * brightnessMul));
                int finalG = Math.min(255, Math.round(g * brightnessMul));
                int finalB = Math.min(255, Math.round(b * brightnessMul));

                rgbRow.put(xKey, (finalR << 16) | (finalG << 8) | finalB);
            }
            rgbTable.put(yKey, rgbRow);
        }

        return rgbTable;
    }

    @LuaFunction(mainThread = true)
    public final Object getMapMeta(String dimension, int mapId) {
        MapState state = getMapState(dimension, mapId);
        if (state == null) return null;

        Map<String, Object> meta = new HashMap<>();
        meta.put("id", mapId);
        meta.put("dimension", state.dimension.getValue().toString());
        meta.put("centerX", state.centerX);
        meta.put("centerZ", state.centerZ);
        meta.put("scale", state.scale);
        meta.put("locked", state.locked);
        meta.put("unlimitedTracking", getPrivateField(state, "unlimitedTracking"));
        meta.put("showIcons", getPrivateField(state, "showIcons"));
        return meta;
    }

    @LuaFunction(mainThread = true)
    public final Object getMapIcons(String dimension, int mapId) {
        return getFilteredIcons(dimension, mapId,
                icon -> !isPlayerIcon(icon),
                MapHandlerAPI::makeIconData
        );
    }

    @LuaFunction(mainThread = true)
    public final Object getMapPlayers(String dimension, int mapId) {
        return getFilteredIcons(dimension, mapId,
                MapHandlerAPI::isPlayerIcon,
                icon -> makeIconData(icon, false)
        );
    }
}
