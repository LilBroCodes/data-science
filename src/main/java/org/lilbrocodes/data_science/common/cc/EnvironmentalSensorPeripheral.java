package org.lilbrocodes.data_science.common.cc;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BeaconBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.level.LevelProperties;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

public class EnvironmentalSensorPeripheral implements IPeripheral {
    private final World world;
    private final ServerWorld serverWorld;
    private final BlockPos pos;

    public EnvironmentalSensorPeripheral(World world, BlockPos pos, BlockState ignored, BlockEntity ignored1, Direction ignored2) {
        this.world = world;
        this.pos = pos;
        this.serverWorld = world instanceof ServerWorld sw ? sw : null;
    }

    @Override
    public String getType() {
        return "environmental_sensor";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return other == this;
    }

    private int getWeatherInt(Function<LevelProperties, Integer> getter) {
        if (serverWorld == null) return -1;
        if (serverWorld.getLevelProperties() instanceof LevelProperties props) {
            return getter.apply(props);
        }
        return -1;
    }

    private boolean getWeatherBool(Predicate<LevelProperties> getter) {
        if (serverWorld == null) return false;
        if (serverWorld.getLevelProperties() instanceof LevelProperties props) {
            return getter.test(props);
        }
        return false;
    }

    private int getEntitySearchRadius() {
        int level = getNearbyBeaconLevel();
        return level == 0 ? -1 : level * 10;
    }

    private Pair<List<Entity>, Boolean> getNearbyEntities() {
        if (serverWorld == null) return new Pair<>(new ArrayList<>(), false);

        int radius = getEntitySearchRadius();
        if (radius <= 0) return new Pair<>(new ArrayList<>(), false);

        int minY = serverWorld.getBottomY();
        int maxY = serverWorld.getTopY() - 1;

        Box box = new Box(
                pos.getX() - radius, minY, pos.getZ() - radius,
                pos.getX() + radius, maxY, pos.getZ() + radius
        );

        List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, box, e -> true);
        return new Pair<>(entities, true);
    }

    private int calculateBeaconLevel(BlockPos p) {
        int i = 0;
        for(int j = 1; j <= 4; i = j++) {
            int k = p.getY() - j;
            if (k < world.getBottomY()) {
                break;
            }
            boolean bl = true;
            for(int l = p.getX() - j; l <= p.getX() + j && bl; ++l) {
                for(int m = p.getZ() - j; m <= p.getZ() + j; ++m) {
                    if (!world.getBlockState(new BlockPos(l, k, m)).isIn(BlockTags.BEACON_BASE_BLOCKS)) {
                        bl = false;
                        break;
                    }
                }
            }
            if (!bl) {
                break;
            }
        }
        return i;
    }

    @LuaFunction
    public final long getTimeOfDay() {
        return world.getTimeOfDay();
    }

    @LuaFunction
    public final long getTime() {
        return world.getTime();
    }

    @LuaFunction
    public final long getLunarTime() {
        return world.getLunarTime();
    }

    @LuaFunction
    public final boolean isRaining() {
        return world.isRaining();
    }

    @LuaFunction
    public final boolean isRainingHere() {
        return world.hasRain(pos.add(0, 1, 0));
    }

    @LuaFunction
    public final boolean isThundering() {
        return world.isThundering();
    }

    @LuaFunction
    public final boolean isThunderingHere() {
        return isRainingHere() && world.isThundering();
    }

    @LuaFunction
    public final int getRainTime() {
        return getWeatherInt(LevelProperties::getRainTime);
    }

    @LuaFunction
    public final int getThunderTime() {
        return getWeatherInt(LevelProperties::getThunderTime);
    }

    @LuaFunction
    public final boolean willRainSoon() {
        return getWeatherBool(props -> !props.isRaining() && props.getClearWeatherTime() < 12000);
    }

    @LuaFunction
    public final boolean willThunderSoon() {
        return getWeatherBool(props -> willRainSoon() && !props.isThundering() && props.getThunderTime() < 12000);
    }

    @LuaFunction
    public final Object getWorldBorder() {
        if (!(serverWorld.getLevelProperties() instanceof LevelProperties props)) return new HashMap<>();
        Map<String, Object> table = new HashMap<>();
        WorldBorder.Properties border = props.getWorldBorder();
        table.put("centerX", border.getCenterX());
        table.put("centerZ", border.getCenterZ());
        table.put("size", border.getSize());
        table.put("warningTime", border.getWarningTime());
        table.put("warningBlocks", border.getWarningBlocks());
        table.put("damagePerBlock", border.getDamagePerBlock());
        table.put("safeZone", border.getSafeZone());
        return table;
    }

    @LuaFunction
    public final Object getWanderingTraderInfo() {
        if (!(serverWorld.getLevelProperties() instanceof LevelProperties props)) return new HashMap<>();
        Map<String, Object> table = new HashMap<>();
        table.put("spawnDelay", props.getWanderingTraderSpawnDelay());
        table.put("spawnChance", props.getWanderingTraderSpawnChance());
        table.put("uuid", props.getWanderingTraderId() != null ? props.getWanderingTraderId().toString() : "");
        return table;
    }

    @LuaFunction
    public final String getDifficulty() {
        if (serverWorld != null) {
            return serverWorld.getLevelProperties().getDifficulty().getName();
        }
        return "unknown";
    }

    @LuaFunction
    public final int getNearbyBeaconLevel() {
        if (serverWorld == null) return 0;

        int maxLevel = -1;
        int radius = 10;

        for (int dx = -radius; dx <= radius; dx++) {
            for (int dy = -radius; dy <= radius; dy++) {
                for (int dz = -radius; dz <= radius; dz++) {
                    BlockPos checkPos = this.pos.add(dx, dy, dz);
                    BlockEntity be = world.getBlockEntity(checkPos);
                    if (be instanceof BeaconBlockEntity beacon) {
                        int level = beacon.level;
                        if (level > maxLevel) maxLevel = level;
                    } else if (world.getBlockState(checkPos).getBlock().equals(Blocks.BEACON)) {
                        int level = calculateBeaconLevel(checkPos);
                        if (level > maxLevel) maxLevel = level;
                    };
                }
            }
        }

        return maxLevel;
    }

    @LuaFunction
    public final int getEntityCount() {
        Pair<List<Entity>, Boolean> entities = getNearbyEntities();
        return entities.getRight() ? entities.getLeft().size() : -1;
    }

    @LuaFunction
    public final Object getEntityTypeCount() {
        Pair<List<Entity>, Boolean> res = getNearbyEntities();
        if (!res.getRight()) return new HashMap<>();
        List<Entity> entities = res.getLeft();
        Map<String, Integer> counts = new HashMap<>();
        for (Entity entity : entities) {
            String type = Registries.ENTITY_TYPE.getId(entity.getType()).toString();
            if (counts.containsKey(type)) {
                counts.put(type, counts.get(type) + 1);
            } else {
                counts.put(type, 1);
            }
        }
        return counts;
    }

    @LuaFunction
    public final Object getBiomeInfo() {
        if (world == null) return new HashMap<>();

        Map<String, Object> table = new HashMap<>();
        RegistryEntry<Biome> biome = world.getBiome(pos);

        table.put("id", biome.getKey().orElseThrow().getValue().toString());
        table.put("temperature", biome.value().getTemperature());
        table.put("precipitation", biome.value().getPrecipitation(pos.add(0, 1, 0)).name().toLowerCase());

        return table;
    }

    @LuaFunction
    public final Object getNearbyPlayers() {
        if (!(world instanceof ServerWorld sw)) return new HashMap<>();

        int radius = getEntitySearchRadius();
        if (radius <= 0) radius = 10; // fallback if no beacon

        int minY = sw.getBottomY();
        int maxY = sw.getTopY() - 1;

        Box box = new Box(
                pos.getX() - radius, minY, pos.getZ() - radius,
                pos.getX() + radius, maxY, pos.getZ() + radius
        );

        List<PlayerEntity> players = sw.getEntitiesByClass(PlayerEntity.class, box, p -> true);
        Map<String, Object> table = new HashMap<>();

        table.put("count", players.size());

        List<Map<String, Object>> playerList = new ArrayList<>();
        for (PlayerEntity player : players) {
            Map<String, Object> pInfo = new HashMap<>();
            pInfo.put("name", player.getName().getString());
            pInfo.put("uuid", player.getUuidAsString());
            double dx = player.getX() - pos.getX();
            double dy = player.getY() - pos.getY();
            double dz = player.getZ() - pos.getZ();
            pInfo.put("distance", Math.sqrt(dx*dx + dy*dy + dz*dz));
            playerList.add(pInfo);
        }

        table.put("players", playerList);
        return table;
    }
}
