package tiles;

import java.util.ArrayList;
import java.util.Arrays;

import character.Player;
import floors.Note;
import interfaces.note.Noteable;
import interfaces.tile.Collidable;
import interfaces.tile.Interactable;
import interfaces.tile.Lootable;
import itemSystem.Item;

public class Chest extends Tile implements Interactable, Collidable, Lootable, Noteable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6248188691328046026L;
	private Note note;
	//TODO(andrew):  need to initialize this bb
	private ArrayList<Item> loot = new ArrayList<>();
	
	public Chest(int tileSheetNum){
		super(tileSheetNum);
		//TODO(andrew): initialize items here
	}

	@Override
	public void interact(Player player) {
		//TODO(andrew):
		obtainLoot();
	}
	
	@Override
	public Item[] obtainLoot(){
		return loot.toArray(new Item[0]);
		//TODO(andrew): what exactly should this method do?
		
	}

	@Override
	public String getMessage() {
		return note.getMessage();
	}

	@Override
	public void setNote(Note note) {
		this.note = note;
	}

	@Override
	public void setLoot(Item[] loot) {
		this.loot = new ArrayList<Item>(Arrays.asList(loot));
	}

	@Override
	public void removeItem(Item item) {
		if(loot.contains(item)){
			loot.remove(item);
		}
	}
}
