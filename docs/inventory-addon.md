| Method | Parameters | Return | Description |
|--------|------------|--------|-------------|
| `inventory.getSize` | `inventory: Inventory` | `int` | Returns the number of slots in the inventory. |
| `inventory.getStack` | `inventory: Inventory, slot: int` | `table or nil` | Returns a map representing the item in the given slot (1-indexed), or `nil` if empty/invalid. |
| `inventory.getFullness` | `inventory: Inventory` | `double` | Returns the fraction of filled slots (0.0–1.0). |
| `inventory.getAllStacks` | `inventory: Inventory` | `table` | Returns all non-empty stacks as NBT string keyed by slot index (1-indexed). |
| `inventory.getUniqueItemCount` | `inventory: Inventory` | `int` | Returns the number of unique item types in the inventory. |
| `inventory.getRoughEntropy` | `inventory: Inventory` | `double` | Returns a rough entropy measure: `log(uniqueItemCount)`. |
| `inventory.getAdvancedEntropy` | `inventory: Inventory` | `double` | Returns a refined entropy considering item counts and adjacency. |
