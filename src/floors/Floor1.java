package floors;

import battleSystem.GuardBattle;
import character.Player;
import character.SecurityGuard;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.Item;
import itemSystem.MountainDew;
import itemSystem.Ramen;
import tiles.Tile;
import tiles.TileManager;

public class Floor1 extends Floor {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1760612558001013811L;

	public Floor1(Player player) {
		
		setBoss(new SecurityGuard());
		setBossBattle(new GuardBattle(player, boss));
		getBoss().getCoordinates().setCoordinates(9, 1);
		setPlayer(player);
		getPlayer().getCoordinates().setCoordinates(9, 12);

		genTiles();
		Note[] notesTemp = new Note[7];
		notesTemp[0] = new Note("Look at my backpack, my backpack is amazing...");
		notesTemp[1] = new Note("Man, this school is crazy hard! Five of my friends dropped out in the first week!"/*, new Coordinates(12,7)*/);
		notesTemp[2] = new Note("I need more coffee.....My project is due tonight and I need the energy!"/*, new Coordinates(8,8)*/);
		notesTemp[3] = new Note("I can't believe I spent 5 hours trying to fix my code when the problem was just a missing semi-colon."/*, new Coordinates(6,4)*/);
		notesTemp[4] = new Note("I heard you can't even get to class on the second floor without getting past the security guard..."/*, new Coordinates(10,2)*/);
		notesTemp[5] = new Note("Shopping list: More ramen(Really keeps me motivated to finish homework)"/*, new Coordinates(5,3)*/);
		notesTemp[6] = new Note("chill out my dude");
		
		Item[][] floorLoot = new Item[2][];
		floorLoot[0] = new Item[] {new Coffee(), new Ramen()};
		floorLoot[1] = new Item[] {new MountainDew(), new Doritos()};
		
		genNotes(notesTemp, floorLoot);
	}
	@Override
	protected void genTiles() {
		int[][] tileRefs = {
				{56,54,54,54,54,54,54,54,54, 4,57},
				{58,12, 5, 5, 5, 5, 5, 5, 5,72,59},
				{58, 5, 5, 5, 5, 5, 5, 5, 5, 5,59},
				{58, 5,52, 5,52, 5,52, 5,52, 5,59},
				{58, 5,55, 5,55, 5,55, 5,55, 5,59},
				{58, 5, 5,24, 5, 5, 5, 5, 5, 5,59},
				{58, 5,55, 5,37, 5,55, 5,55, 5,59},
				{58, 5, 5, 5, 5,12, 5, 5, 5, 5,59},
				{58, 5,55, 5,55, 5,55, 5,37, 5,59},
				{58, 5,52, 5,52, 5,52, 5,52, 5,59},
				{58, 5,24, 5, 5, 5, 5, 5, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5, 5, 5, 5,59},
				{58, 5, 5, 5, 5, 5, 5,24, 5, 5, 3},
				{58, 5, 5, 5, 5, 5, 5, 5, 5, 5,59},
				{48,53,53,53,53,53,53,53,53,53,49}
		};
		this.tiles = new Tile[tileRefs.length][tileRefs[0].length];
		
		for(int i = 0 ; i < tileRefs.length ; i++) {
			for(int j = 0 ; j < tileRefs[i].length ; j++) {
				this.tiles[i][j] = TileManager.createTile(tileRefs[i][j]);
			}
		}
	}
}
