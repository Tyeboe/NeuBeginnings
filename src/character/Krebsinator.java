package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import enums.Character.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class Krebsinator extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1846447040144482094L;

	public Krebsinator() {
		super("Krebsinator", 500, 76, new PickOnYou());
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.MOTIVATION, 20);
		this.stats.put(Stats.WIT, 7);
		this.stats.put(Stats.ENDURANCE, 3);
		this.stats.put(Stats.STAMINA, 3);
		levelUp(7);
		updateDerivedStats();
		battleImage = new Image(getClass().getResourceAsStream("/images/Krebsinator.png"));
	}
	
	private void readObject(ObjectInputStream ois){
		try {
			ois.defaultReadObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
		
		//TODO Replace image paths with correct images
		battleImage = new Image(getClass().getResourceAsStream("/images/Krebsinator.png"));
		
	}
}
