package interfaces.tile;

import itemSystem.Item;

public interface Lootable {
	public Item[] obtainLoot();
	public void setLoot(Item[] loot);
	public void removeItem(Item item);
}
