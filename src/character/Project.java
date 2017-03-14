package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import enums.Character.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import models.Coordinates;

public class Project extends Enemy{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7442909421705805038L;
	
	public Project(int level) {
		super("Project", 225 * level, 2);
		coordinates = new Coordinates(5,5);
		this.stats.put(Stats.MOTIVATION, 12);
		this.stats.put(Stats.INTELLIGIENCE, 11);
		this.stats.put(Stats.WIT, 10);
		this.stats.put(Stats.ENDURANCE, 9);
		this.stats.put(Stats.STAMINA, 10);
		this.hitPoints = stats.get(Stats.MOTIVATION);
		this.energy = stats.get(Stats.STAMINA);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		levelUp(level);
		battleImage = new Image(getClass().getResourceAsStream("/images/project.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/projecttakedamage.png"));
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
		in.defaultReadObject();		
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);
		battleImage = new Image(getClass().getResourceAsStream("/images/project.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/projecttakedamage.png"));
	}
}
