package character;

import java.io.IOException;
import java.io.ObjectInputStream;

import abilities.MinionAssault;
import abilities.PlayEnchantment;
import enums.Character.Stats;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.MountainDew;
import itemSystem.Planeswalker;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;

public class SecurityGuard extends Boss {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8572599965260907613L;

	public SecurityGuard() {
		super("Security Guard", 1200, 72, new MinionAssault(), new PlayEnchantment());
		this.inv.clearInventory();
		this.inv.addItem(new Planeswalker());
		this.inv.addAllItems(new MountainDew(), new Doritos(), new Coffee());
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.MOTIVATION, 20);
		this.stats.put(Stats.WIT, 7);
		this.stats.put(Stats.ENDURANCE, 4);
		this.stats.put(Stats.STAMINA, 3);
		levelUp(4);
		updateDerivedStats();
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		battleImage = new Image(getClass().getResourceAsStream("/images/guardicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/guarddamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/guardattack.png"));
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
		battleImage = new Image(getClass().getResourceAsStream("/images/guardicon.png"));
		takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/guarddamage.png"));
		attackAnimation = new Image(getClass().getResourceAsStream("/images/guardattack.png"));
	}
	

}
