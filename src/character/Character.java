package character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import enums.Character.InventoryAction;
import enums.Character.ModifiableFields;
import enums.Character.Stats;
import interfaces.publisherSubscriber.Listener;
import interfaces.publisherSubscriber.Subscribable;
import interfaces.tile.Collidable;
import itemSystem.Inventory;
import itemSystem.Item;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import models.Coordinates;
import tiles.Tile;

public abstract class Character extends Tile implements Subscribable<Character>, Serializable, Collidable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -888637062240963044L;
	public final String NAME;
	protected Inventory inv = new Inventory();
	protected int hitPoints = 100;
	protected transient IntegerProperty hpProperty = new SimpleIntegerProperty(hitPoints);
	protected transient IntegerProperty maxHPProperty = new SimpleIntegerProperty(hitPoints);
	protected int energy = 100;
	protected int level = 1;
	protected Coordinates coordinates = new Coordinates(0, 0);
	protected int floorNum;
	private int creditReq = 500;
	private int currentCredits = 0;
	protected ArrayList<Listener<Character>> subscribers = new ArrayList<>();
	protected transient Image worldImage;
	protected transient Image battleImage;
	protected transient Image takeDamageAnimation;
	protected transient Image attackAnimation;
	protected HashMap<Stats, Integer> stats = new HashMap<>();
	protected int maxHitPoints = 100;
	protected int maxEnergy = 100;
	protected transient IntegerProperty maxEnergyProperty = new SimpleIntegerProperty(energy);
	protected transient IntegerProperty energyProperty = new SimpleIntegerProperty(energy);
	protected transient ImageView battleImageView;
	protected boolean isBattleAnimating = false;

	public Character(String name, int tileSheetNum) {
		super(tileSheetNum);
		if (name == null) {
			throw new IllegalArgumentException("Name cannot be null.");
		} else if (name.isEmpty()) {
			throw new IllegalArgumentException("Name cannot be empty.");
		}
		this.coordinates = new Coordinates(0, 0);
		this.stats.put(Stats.MOTIVATION, 1);
		this.stats.put(Stats.INTELLIGIENCE, 1);
		this.stats.put(Stats.WIT, 1);
		this.stats.put(Stats.ENDURANCE, 1);
		this.stats.put(Stats.STAMINA, 1);
		updateDerivedStats();
		this.NAME = name;
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		energyProperty.set(energy);
		maxEnergyProperty.set(maxEnergy);

	}

	protected void levelUp(int level) {
		if (level > 0) {
			for (; level > 0; level--) {
				this.level += 1;
				int currentMot = stats.get(Stats.MOTIVATION);
				int currentInt = stats.get(Stats.INTELLIGIENCE);
				int currentWit = stats.get(Stats.WIT);
				int currentEnd = stats.get(Stats.ENDURANCE);
				int currentSta = stats.get(Stats.STAMINA);
				this.stats.put(Stats.MOTIVATION, currentMot + 2);
				this.stats.put(Stats.INTELLIGIENCE, currentInt + 2);
				this.stats.put(Stats.WIT, currentWit + 2);
				this.stats.put(Stats.ENDURANCE, currentEnd + 2);
				this.stats.put(Stats.STAMINA, currentSta + 2);
				updateDerivedStats();
			}
		}
	}

	protected void updateDerivedStats() {
		this.hitPoints = stats.get(Stats.MOTIVATION) * 10;
		this.maxHitPoints = hitPoints;
		this.energy = stats.get(Stats.STAMINA) * 10;
		this.maxEnergy = energy;
		energyProperty.set(energy);
		maxEnergyProperty.set(energy);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
	}

	public boolean giveCredits(int credits) {
		boolean leveledUp = false;
		currentCredits += credits;
		do {
			if (currentCredits >= creditReq) {
				leveledUp = true;
				currentCredits -= creditReq;
				levelUp(1);
				creditReq *= 1.3;
			} else {
				break;
			}
		} while (true);
		return leveledUp;
	}

	public int getLevel() {
		return level;
	}

	public IntegerProperty getHPProperty() {
		return hpProperty;
	}

	public IntegerProperty getMaxHPProperty() {
		return maxHPProperty;
	}

	public IntegerProperty getMaxEnergyProperty() {
		return maxEnergyProperty;
	}

	public IntegerProperty getEnergyProperty() {
		return energyProperty;
	}

	public int getNumOfCredits() {
		return currentCredits;
	}

	public int getCreditRequirement() {
		return creditReq;
	}

	public Item[] getInventoryContents() {
		return inv.getItems();
	}

	public boolean modifyInventory(InventoryAction ACTION, Item... items) {
		boolean successful = false;
		switch (ACTION) {
		case GIVE:
			successful = inv.addAllItems(items);
			break;
		case TAKE:
			successful = inv.removeAllItems(items);
			break;
		}
		return successful;
	}

	public void modifyField(ModifiableFields FIELD, int modification) {
		switch (FIELD) {
		case HITPOINTS:
			hitPoints += modification;
			hitPoints = hitPoints < 0 ? 0 : hitPoints;
			hitPoints = hitPoints > maxHitPoints ? maxHitPoints : hitPoints;
			hpProperty.set(hitPoints);
			break;
		case ENERGY:
			energy += modification;
			energy = energy < 0 ? 0 : energy;
			energy = energy > maxEnergy ? maxEnergy : energy;
			energyProperty.set(energy);
			break;
		}
	}

	public int getStat(Stats STAT) {
		return stats.get(STAT);
	}

	public HashMap<Stats, Integer> getStats() {
		return stats;
	}

	public Coordinates getCoordinates() {
		return coordinates;
	}

	@Override
	public void addSubscriber(Listener<Character> listener) {
		subscribers.add(listener);
	}

	@Override
	public void notifySubscribers() {
		for (Listener<Character> subscriber : subscribers) {
			subscriber.notify();
		}
	}

	@Override
	public void removeSubscriber(Listener<Character> sub) {
		subscribers.remove(sub);

	}

	public int getCurrentHealth() {
		return hitPoints;
	}

	public int getMaxEnergy() {
		return maxEnergy;
	}

	public int getMaxHealth() {
		return maxHitPoints;
	}

	public int getFloorNum() {
		return floorNum;
	}

	public ImageView getBattleImageView() {
		return battleImageView;
	}

	public void setBattleImageView(ImageView battleImageView) {
		this.battleImageView = battleImageView;
	}

	public Image getWorldImage() {
		return worldImage;
	}

	public Image getBattleImage() {
		return battleImage;
	}

	public Image getTakeDamageAnimation() {
		return takeDamageAnimation;
	}

	public boolean isBattleAnimating() {
		return isBattleAnimating;
	}

	public void setIsBattleAnimating(boolean isAnimating) {
		isBattleAnimating = isAnimating;
	}

	public int compareWit(Character chara) {
		int num = 0;
		if (this.getStat(Stats.WIT) > chara.getStat(Stats.WIT)) {
			num = 1;
		} else if (this.getStat(Stats.WIT) < chara.getStat(Stats.WIT)) {
			num = -1;
		}
		return num;
	}

	public abstract int takeDmg(int dmg);

	public abstract int attack();

	@Override
	public String toString() {
		return "Character [name=" + NAME + ", hitPoints=" + hitPoints + ", energy=" + energy + ", level=" + level
				+ ", floorNum=" + floorNum + "]";
	}

	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
	}

	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		hpProperty = new SimpleIntegerProperty(hitPoints);
		maxHPProperty = new SimpleIntegerProperty(hitPoints);
		energyProperty = new SimpleIntegerProperty(energy);
		maxEnergyProperty = new SimpleIntegerProperty(maxEnergy);

	}

	public boolean useEnergy(int i) {
		if (i <= energy) {
			energy -= i;
			energyProperty.set(energy);
			return true;
		}
		return false;
	}

	public int getEnergy() {
		return energy;
	}

	public void gainEnergy(int i) {
		energy += i;
		energy = energy > maxEnergy ? maxEnergy : energy;
		energyProperty.set(energy);

	}

}