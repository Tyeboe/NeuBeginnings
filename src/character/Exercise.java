package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import enums.Character.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import models.Coordinates;

public class Exercise extends Enemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 804702981939770628L;
	
	public Exercise(int level) {
		super("Exercise", 100 * level, 0);
		coordinates = new Coordinates(5,5);
		this.stats.put(Stats.MOTIVATION, 9);
		this.stats.put(Stats.INTELLIGIENCE, 9);
		this.stats.put(Stats.WIT, 9);
		this.stats.put(Stats.ENDURANCE, 9);
		this.stats.put(Stats.STAMINA, 10);
		this.hitPoints = stats.get(Stats.MOTIVATION);
		this.energy = stats.get(Stats.STAMINA);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		levelUp(level);
		battleImage = new Image(getClass().getResourceAsStream("/images/exercise.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/exercisetakedamage.png"));
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();		
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
		battleImage = new Image(getClass().getResourceAsStream("/images/exercise.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/exercisetakedamage.png"));
	}
}
