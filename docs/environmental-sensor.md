The sensor requires a nearby beacon (in a 10 block radius) for any method related to entities

| Method | Parameters | Return | Description |
|--------|------------|--------|-------------|
| `sensor.getTimeOfDay` | none | `long` | Returns the time of day in the world. |
| `sensor.getTime` | none | `long` | Returns total world time. |
| `sensor.getLunarTime` | none | `long` | Returns moon cycle time. |
| `sensor.isRaining` | none | `boolean` | Returns whether it is raining in the world. |
| `sensor.isRainingHere` | none | `boolean` | Returns whether it is raining at the sensor position. |
| `sensor.isThundering` | none | `boolean` | Returns whether thunder is occurring globally. |
| `sensor.isThunderingHere` | none | `boolean` | Returns whether thunder is occurring at the sensor position. |
| `sensor.getRainTime` | none | `int` | Returns remaining rain time in ticks. |
| `sensor.getThunderTime` | none | `int` | Returns remaining thunder time in ticks. |
| `sensor.willRainSoon` | none | `boolean` | Returns whether it will rain soon. |
| `sensor.willThunderSoon` | none | `boolean` | Returns whether it will thunder soon. |
| `sensor.getWorldBorder` | none | `table` | Returns world border info: `{ centerX, centerZ, size, warningTime, warningBlocks, damagePerBlock, safeZone }`. |
| `sensor.getWanderingTraderInfo` | none | `table` | Returns trader info: `{ spawnDelay, spawnChance, uuid }`. |
| `sensor.getDifficulty` | none | `string` | Returns world difficulty name. |
| `sensor.getNearbyBeaconLevel` | none | `int` | Returns the pyramid level of the nearest beacon. |
| `sensor.getEntityCount` | none | `int` | Returns the number of nearby entities within beacon radius. |
| `sensor.getEntityTypeCount` | none | `table` | Returns a map of entity type IDs to their counts nearby. |
| `sensor.getBiomeInfo` | none | `table` | Returns biome info at sensor: `{ id, temperature, precipitation }`. |
| `sensor.getNearbyPlayers` | none | `table` | Returns nearby players with `{ count, players: [{ name, uuid, distance }] }`. |