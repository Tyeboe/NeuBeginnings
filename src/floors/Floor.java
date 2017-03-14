package floors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import battleSystem.BossBattle;
import character.Boss;
import character.Player;
import interfaces.draw.Paintable;
import interfaces.note.Noteable;
import interfaces.tile.Lootable;
import itemSystem.Item;
import javafx.scene.image.Image;
import models.Coordinates;
import tiles.Tile;

public abstract class Floor implements Paintable, Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4879286765234322293L;
	protected Tile[][] tiles;
	protected Boss boss;
	private BossBattle bossBattle;
	private Player player;
	protected Coordinates playerStart;
	private HashMap<Coordinates, Note> notes = new HashMap<>();
	
	public Coordinates getPlayerStart() {
		return playerStart;
	}
	
	public  List<List<Tile>> getTiles(){
		ArrayList<List<Tile>> t = new ArrayList<>();
		for(int i = 0; i < this.tiles.length; i++){
			t.add(Collections.unmodifiableList(Arrays.asList(this.tiles[i])));
		}
		return Collections.unmodifiableList(t);
	}
	
	public void changeTile(int y, int x, Tile tile){
		if(tile != null){
			tiles[y][x] = tile;
		}else{
			throw new IllegalArgumentException("You cannot set a tile on a floor to null");
		}
	}
	
	protected void genNotes(Note[] notesTemp, Item[][] floorLoot) {
		int noteCounter = 0;
		int lootCounter = 0;
		for(int i = 0; i < tiles.length; i++){
			for(int j = 0; j < tiles[i].length; j++){
				if(tiles[i][j] instanceof Noteable){
					((Noteable) tiles[i][j]).setNote(notesTemp[noteCounter]);
					noteCounter++;
				}
				if(tiles[i][j] instanceof Lootable){
					((Lootable)tiles[i][j]).setLoot(floorLoot[lootCounter]);
					lootCounter++;
				}
			}
		}
	}
	
	public Boss getBoss() {
		return boss;
	}
	
	protected void setBoss(Boss boss) {
		if (boss != null) {
			this.boss = boss;
		}
	}
	
	public Player getPlayer() {
		return player;
	}
	
	public void setPlayer(Player player) {
		if (player != null) {
			this.player = player;
			
		}
	}
	
	public BossBattle getBossBattle() {
		return bossBattle;
	}

	protected void setBossBattle(BossBattle bossBattle) {
		if (bossBattle != null) {
			this.bossBattle = bossBattle;			
		}
	}

	public HashMap<Coordinates, Note> getNotes() {
		return notes;
	}
	
	public boolean bossIsDefeated(){
		return boss.isDefeated();
	}
	
	protected abstract void genTiles();

	@Override
	public Image getWorldImage() {
		//TODO implement
		return null;
	}

	@Override
	public Image getBattleImage() {
		//TODO implement
		return null;
	}

	//TODO add a toString() override
	
}
