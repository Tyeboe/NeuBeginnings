package tiles;

import java.util.List;

import character.JerryPay;
import character.JoshKrebs;
import character.SecurityGuard;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import models.Coordinates;

public class TileManager {
	//NOTE(andrew): This number is the bound for how many tiles are drawn squared
		//This number must be > 0 and <= tiles.length
	private final static byte TILES_WIDE_TO_DRAW = 11;
	private final static byte TILES_HIGH_TO_DRAW = 11;
	private final static byte TILE_WIDTH = 32;
	public static Image tileSheet = new Image((new Object()).getClass().getResourceAsStream("/images/tileset.png"));
	private final static int TILESHEET_TILES_WIDE = (int)tileSheet.getWidth() / TILE_WIDTH;
	
	//Prevents the creation of instances of Tile Manager
	private TileManager(){
		
	}
	
	public static Tile createTile(int tileSheetNum){
		Tile tile = null;
		if(tileSheetNum >= 0 && tileSheetNum < TILESHEET_TILES_WIDE){
			tile = new GroundTile(tileSheetNum);
		}else if(tileSheetNum >= TILESHEET_TILES_WIDE && tileSheetNum < (TILESHEET_TILES_WIDE * 2)){
			tile = new Chest(tileSheetNum);
		}else if(tileSheetNum >= (TILESHEET_TILES_WIDE * 2) && tileSheetNum < (TILESHEET_TILES_WIDE * 3)){
			tile = new FloorMessage(tileSheetNum);
		}else if(tileSheetNum >= (TILESHEET_TILES_WIDE * 3) && tileSheetNum < (TILESHEET_TILES_WIDE * 4)){
			tile = new WallMessage(tileSheetNum);
		}else if(tileSheetNum >= (TILESHEET_TILES_WIDE * 4)&& tileSheetNum < (TILESHEET_TILES_WIDE * 5)){
			tile = new Wall(tileSheetNum);
		}else if(tileSheetNum >= (TILESHEET_TILES_WIDE * 5)&& tileSheetNum < (TILESHEET_TILES_WIDE * 6)){
			tile = new Wall(tileSheetNum);
		}else if(tileSheetNum == 72) {
			tile = new SecurityGuard();
		}else if(tileSheetNum == 73) {
			tile = new JerryPay();
		}else if(tileSheetNum == 74) {
			tile = new JoshKrebs();
		}
		//(dakota) i needed to add another column to accomodate more wall tiles without having to completely rewrite the floors
		return tile;
	}
	
	
	
	public static WritableImage getImageToDraw(List<List<Tile>> list, Coordinates playerPosition){
		//NOTE(andrew): The width and length of the image WILL change depending on the size
			//of the image that will be displayed at the end
		int startYTile = playerPosition.getY() - (TILES_HIGH_TO_DRAW/2);
		int startXTile = playerPosition.getX() - (TILES_WIDE_TO_DRAW/2);
		WritableImage wi = new WritableImage(TILES_WIDE_TO_DRAW * TILE_WIDTH, TILES_HIGH_TO_DRAW * TILE_WIDTH);
		
		for(int i = 0; i < TILES_HIGH_TO_DRAW; i++){
			if(i + startYTile >= 0 && i + startYTile < list.size()){
				buildRow(list.get(i + startYTile), wi, i, startXTile);
			}else{
				buildBlankRow(wi, i);
			}
		}
		
		return wi;
	}
	
	private static void buildRow(List<Tile> list, WritableImage wi, int row, int startXTile){
		//NOTE(andrew): grab the objects that allow reading from the tile sheet and writing to the writable image
		PixelReader pr = tileSheet.getPixelReader();
		PixelWriter pw = wi.getPixelWriter();
		//NOTE(andrew): x and y are the variables used to read the pixels from the tilesheet. They are initialized to 0
			// but they will most likely change before any pixels are read.
		int x = 0;
		int y = 0;
		//NOTE(andrew): tileCounter is used to keep track of the tile in the row that is currently being read from.
			//Remember this method on takes ONE ROW of the tiles array and builds ONE ROW of the image
			//This must be initialized to -1 in order for the counting and comparing to work correctly
		int tileCounter = -1;
		//NOTE(andrew): This variable keeps track of which tile is being read. initalized to 0, but will most likely change
			//before any pixels are read.
		int tileSheetNum = 0;
		//NOTE(andrew): Looping based on the individual pixels.
		for(int i = 0; i < TILE_WIDTH; i++){
			for(int j = 0; j < (TILES_WIDE_TO_DRAW * TILE_WIDTH); j++){
				//NOTE(andrew): This if should run each time the tile changes.
				if(j % TILE_WIDTH == 0){
					//NOTE(andrew): tick up the counter that keeps track of the index in the row we are at.
					tileCounter++;
					//NOTE(andrew): clear the read variables so they are ready to use.
						//These at first represent the index of the tile that is to be accessed
						//At the end of this statement, these variables are changed to represent the next pixel to be read
					x = 0;
					y = 0;
					//NOTE(andrew): if the tile that is trying to be accessed is outside of the array, we don't
						//need to read a tile, it will just be black.
					if(tileCounter + startXTile >= 0 && tileCounter + startXTile < list.size()){
						//NOTE(andrew): get the tile number that we are currently needing to access
						tileSheetNum = list.get(tileCounter + startXTile).getTileSheetNum();
						//NOTE(andrew): if the tile we are trying to access is not on the initial row, we use this loop to edit
							//y based on the tile num, this is used to find the ROW
						while(tileSheetNum > TILESHEET_TILES_WIDE - 1){
							//NOTE(andrew): find the row it belongs to
							y++;
							tileSheetNum -= TILESHEET_TILES_WIDE;
						}
						//NOTE(andrew): the leftover is the COLUMN it belongs to
						x = tileSheetNum;
						//NOTE(andrew): multiply by the width of a tile so these numbers now represent the top 
							//NOTE(andrew): left pixel in the tile we want to grab
						x *= TILE_WIDTH;//x = indexX * TILE_WIDTH;
						y *= TILE_WIDTH;//y = indexY * TILE_WIDTH;
					}
					
				}
				Color color = null;
				//NOTE(andrew): if the tile that is trying to be accessed is outside of the array, we don't
					//need to read a tile, it will just be black.
				if(tileCounter + startXTile >= 0 && tileCounter + startXTile < list.size()){
					//NOTE(andrew): read the current pixel from the tilesheet
					color = pr.getColor(x , y + i);
				}else{
					color = Color.BLACK;
				}
				//NOTE(andrew): write the current pixel to the WritableImage
				pw.setColor(j, i + (row * TILE_WIDTH), color);
				//NOTE(andrew): tick up the counters that represent the pixel we are currently at
				x++;
			}
			//NOTE(andrew): again, we need to make sure that this is set to -1 and not 0
			tileCounter = -1;
		}
	}
	
	private static void buildBlankRow(WritableImage wi, int row){
		PixelWriter pw = wi.getPixelWriter();
		for(int i = 0; i < TILE_WIDTH; i++){
			for(int j = 0; j < TILES_WIDE_TO_DRAW * TILE_WIDTH; j++){
				pw.setColor(j, i + (row * TILE_WIDTH), Color.BLACK);
			}
		}
	}
}
