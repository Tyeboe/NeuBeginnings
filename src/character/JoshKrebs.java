package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import abilities.AssignFraculator;
import abilities.Lecture;
import abilities.PlayDivision;
import enums.Character.Stats;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class JoshKrebs extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1532878735222428440L;

	public JoshKrebs() {
		super("Josh Krebs", 3600, 74, new AssignFraculator(), new PlayDivision(), new Lecture());
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.MOTIVATION, 20);
		this.stats.put(Stats.WIT, 10);
		this.stats.put(Stats.ENDURANCE, 6);
		this.stats.put(Stats.STAMINA, 3);
		levelUp(14);
		updateDerivedStats();
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		battleImage = new Image(getClass().getResourceAsStream("/images/krebsicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/krebsdamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/krebsattack.png"));
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
		battleImage = new Image(getClass().getResourceAsStream("/images/krebsicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/krebsdamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/krebsattack.png"));
		
	}
}
