package application;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import character.Player;
import floors.Floor;
import floors.Floor1;
import floors.Floor2;
import floors.Floor3;

public class Game implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2750081260774478492L;
	private Player player;
	private Floor[] floors;
	
	public Game(Player player) {
		this.player = player;
		this.player.setFloorNum(1);
		this.floors = new Floor[] {new Floor1(player), new Floor2(player), new Floor3(player)};
	}	
	
	public Player getPlayer(){
		return this.player;
	}
	
	public List<Floor> getFloors(){
		return Collections.unmodifiableList(Arrays.asList(floors));
	}
}
