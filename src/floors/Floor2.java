package floors;

import battleSystem.PayBattle;
import character.JerryPay;
import character.Player;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.Item;
import itemSystem.MountainDew;
import itemSystem.Ramen;
import models.Coordinates;
import tiles.Tile;
import tiles.TileManager;

public class Floor2 extends Floor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5349296670151094781L;
	
	public Floor2(Player player) {
		setBoss(new JerryPay());
		getBoss().getCoordinates().setCoordinates(1, 8);
		setBossBattle(new PayBattle(player, getBoss()));
		setPlayer(player);
		playerStart = new Coordinates(9,2);
		genTiles();
		Note[] notesTemp = new Note[8];
		notesTemp[0] = new Note("Where's my project folder????????"/*, new Coordinates(3,6)*/);
		notesTemp[1] = new Note("I hate this class. Why don't the people who sit next to me use deodorant?"/*, new Coordinates(5,3)*/);
		notesTemp[2] = new Note("Hey, why is that teacher looming so creepily at the end of the hallway? Everytime I head to the stairs he glares at me, so I can't leave."/*, new Coordinates(5,9)*/);
		notesTemp[3] = new Note("01100100 01100101 01110011 01110100 01110010 01101111 01111001 00100000 01100001 01101100 01101100 00100000 01101000 01110101 01101101 01100001 01101110 01110011"/*, new Coordinates(10,6)*/);
		notesTemp[4] = new Note("I'm scared to leave this room, that teacher is scary."/*, new Coordinates(12,1)*/);
		notesTemp[5] = new Note("I've pulled more all nighters in one quarter than I did throughout all of high school."/*, new Coordinates(14,3)*/);
		notesTemp[6] = new Note("hot choclety..................slime man");
		notesTemp[7] = new Note("Dear girl gamer, I see you are actually decent at games. You are allowed to date me if you send one nude every night and good morning snapchats every day.");
		
		Item[][] floorLoot = new Item[2][];
		floorLoot[0] = new Item[] {new Coffee(), new Ramen()};
		floorLoot[1] = new Item[] {new MountainDew(), new Doritos()};
		
		genNotes(notesTemp,floorLoot);
	}
	@Override
	protected void genTiles() {
		int[][] tileRefs = {
				{56,54,54,54,54,54,54,54,60, 4,57},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5,59},
				{58, 5,51,51,51,51,51, 5,59, 5,59},
				{58, 5,50,50,50,50,36, 5,59, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5,59},
				{58, 5, 5,24, 5, 5, 5, 5, 3,24,59},
				{58, 5, 5, 5,51,51,51, 5,59, 5,59},
				{63,53, 0,53,53,53,53,53,49, 5,59},
				{58,73, 5, 5, 5, 5, 5, 5, 5, 5,59},
				{61,54, 1,54,54,54,54,54,57, 5,59},
				{58, 5, 5,12,50,50,36, 5,59, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5, 3, 5,59},
				{58,24, 5, 5, 5, 5, 5, 5,59, 5,59},
				{58, 5,51,51,51,51,51, 5,59, 5,59},
				{58, 5,50,36,50,50,50, 5,59, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59,12,59},
				{48,53,53,53,53,53,53,53,62,53,49},
		};
		this.tiles = new Tile[tileRefs.length][tileRefs[0].length];
		
		for(int i = 0 ; i < tileRefs.length ; i++) {
			for(int j = 0 ; j < tileRefs[i].length ; j++) {
				this.tiles[i][j] = TileManager.createTile(tileRefs[i][j]);
			}
		}
	}
}
