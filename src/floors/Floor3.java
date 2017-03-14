package floors;

import battleSystem.KrebsBattle;
import character.JoshKrebs;
import character.Player;
import itemSystem.Doritos;
import itemSystem.Item;
import itemSystem.MountainDew;
import itemSystem.Ramen;
import models.Coordinates;
import tiles.Tile;
import tiles.TileManager;

public class Floor3 extends Floor {

	/**
	 * 
	 */
	private static final long serialVersionUID = 167455105787912757L;
	
	public Floor3(Player player) {
	
		setBoss(new JoshKrebs());
		setBossBattle(new KrebsBattle(player, boss));
		getBoss().getCoordinates().setCoordinates(1, 17);
		setPlayer(player);
		playerStart = new Coordinates(9,2);

		genTiles();
		Note[] notesTemp = new Note[7];
		notesTemp[0] = new Note("My presentation and League tournament are at the same time! What do I do??.....But my League rating is on the line...");
		notesTemp[1] = new Note("It's like it KNOWS. How does it know??? Does it read every students mind and say \"Yup this person has no idea\" and pick them?"/*, new Coordinates(12,7)*/);
		notesTemp[2] = new Note("This is the only thing I have to eat for the next five days...I hope no one takes it while I pass this assignment off."/*, new Coordinates(8,8)*/);
		notesTemp[3] = new Note("I can only explain a concept so many times, for the love of god just go to the tutors who are paid to help you understand a concept that I teach."/*, new Coordinates(6,4)*/);
		notesTemp[4] = new Note("I don't understand! Why is my grade so low? I do an hour of homework every week! What more do they want?!?!"/*, new Coordinates(10,2)*/);
		notesTemp[5] = new Note("Everytime he assigns a lab he gets this.....sadistic look....and smile...:("/*, new Coordinates(5,3)*/);
		notesTemp[6] = new Note("I'm scared to even walk into class! I feel like the krebsinator is a straight up guillotine!");
		
		Item[][] floorLoot = new Item[2][];
		floorLoot[0] = new Item[] {new Doritos(), new MountainDew(), new MountainDew()};
		floorLoot[1] = new Item[] {new Ramen(), new Ramen()};
		
		genNotes(notesTemp, floorLoot);
	}
	@Override
	protected void genTiles() {
		int[][] tileRefs = {
				{56,54,54,54,54,54,54,54,60,54,54,57},
				{58,12,50,50,50,50,50, 5,59, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5, 5, 3},
				{58, 5, 5,51,51,51,51, 5, 3, 5, 5,59},
				{58,55, 5,50,50,50,50, 5,59, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5,24,59},
				{48,53,53,53,53,53,53,53,66, 5, 5,59},
				{58, 5,50,50,50,50,50,12,59, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5, 5,59},
				{58, 5, 5,51,51,51,51, 5, 3, 5, 5,59},
				{58,37, 5,50,36,50,50, 5,59, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5, 5,59},
				{48,54,54,54,54,54,54,54,65, 5, 5,59},
				{58, 5,50,50,50,36,50, 5,59, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5,59, 5, 5,59},
				{58, 5, 5,51,51,51,51, 5, 3, 5, 5,59},
				{58,55, 5,50,50,50,50, 5,59, 5, 5,59},
				{58,74, 5, 5, 5, 5, 5, 5,59,24, 5,59},
				{48,53,53,53,53,53,53,53,62,53,53,49}
		};
		this.tiles = new Tile[tileRefs.length][tileRefs[0].length];
		
		for(int i = 0 ; i < tileRefs.length ; i++) {
			for(int j = 0 ; j < tileRefs[i].length ; j++) {
				this.tiles[i][j] = TileManager.createTile(tileRefs[i][j]);
			}
		}
	}

}
