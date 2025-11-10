| Method | Parameters | Return | Description |
|--------|------------|--------|-------------|
| `maps.getMapData` | `dimension: string, mapId: int` | `table` | Returns a 128x128 table of map pixels. Each pixel is a table `{ colorId, brightness }`. |
| `maps.getMapColors` | `dimension: string, mapId: int` | `table` | Returns a 128x128 table of RGB colors (int). |
| `maps.getMapMeta` | `dimension: string, mapId: int` | `table` | Returns map metadata: `{ id, dimension, centerX, centerZ, scale, locked, unlimitedTracking, showIcons }`. |
| `maps.getMapIcons` | `dimension: string, mapId: int` | `table` | Returns non-player map icons as tables with `{ x, y, rotation, displayName, type }`. |
| `maps.getMapPlayers` | `dimension: string, mapId: int` | `table` | Returns player map icons as tables with `{ x, y, rotation, displayName }`. |
