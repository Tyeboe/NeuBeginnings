package character;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import abilities.AnExcuse;
import abilities.ExpertTimeManagement;
import abilities.Procrastinate;
import abilities.PullAnAllNighter;
import application.GameEngine;
import enums.Character.Direction;
import enums.Character.Genders;
import enums.Character.Stats;
import interfaces.ability.Ability;
import interfaces.ability.AttackAbility;
import interfaces.ability.BuffAbility;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.MountainDew;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

public class Player extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3349758486478712145L;

	private final ArrayList<Ability> ABILITIES = new ArrayList<>();
	private Direction directionFacing = Direction.DOWN;
	private final Genders sex;
	private transient Image worldIcon;
	
	public Player(String name, Genders gender, int tileSheetNum) {
		super(name, tileSheetNum);
		ABILITIES.addAll(Arrays.asList((new Procrastinate()), (new ExpertTimeManagement()), (new AnExcuse())));
		this.stats.put(Stats.INTELLIGIENCE, 15);
		this.stats.put(Stats.MOTIVATION, 20);
		this.stats.put(Stats.WIT, 7);
		this.stats.put(Stats.ENDURANCE, 3);
		this.stats.put(Stats.STAMINA, 3);
		updateDerivedStats();
		inv.addAllItems(new Coffee(), new Doritos(), new MountainDew());
		switch (gender) {
		case BOY:
			sex = Genders.BOY;
			worldImage = new Image(getClass().getResourceAsStream("/images/MaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/malebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/maleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/maletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/maleattack.png"));
			break;
		case GIRL:
			sex = Genders.GIRL;
			worldImage = new Image(getClass().getResourceAsStream("/images/FemaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/femalebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/femaleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/femaletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/femaleattack.png"));
			break;
		default:
			sex = Genders.BOY;
			break;

		}
	}
	@Override
	public int takeDmg(int dmg) {
		int damage = dmg - getStat(Stats.ENDURANCE);
		if(damage <= 0){
			damage = 1;
		}
		hitPoints -= damage;
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hitPoints = maxHitPoints < hitPoints ? maxHitPoints : hitPoints;
		hpProperty.set(hitPoints);
		GameEngine.playTakeDamageAnimation(takeDamageAnimation, this);
		return dmg;
	}

	@Override
	public int attack() {
		GameEngine.playAttackAnimation(attackAnimation, this);
		return getStat(Stats.INTELLIGIENCE);
	}

	public boolean ability(Ability ability, Character... enemies) {
		if (ability instanceof AttackAbility) {
			return ability.use(this, enemies);
		} else if (ability instanceof BuffAbility) {
			return ability.use(this, this);
		}
		return false;

	}

	public ObservableList<Ability> getAbilities() {
		ObservableList<Ability> abilities = FXCollections.observableArrayList();
		abilities.addAll(this.ABILITIES);

		return abilities;
	}

	public void setFloorNum(int i) {
		this.floorNum = i;
	}

	public Direction getDirectionFacing() {
		return directionFacing;
	}

	public void setDirectionFacing(Direction directionFacing) {
		this.directionFacing = directionFacing;
	}

	public Image getWorldImage() {
		return worldImage;
	}
	
	public Image getWorldIcon() {
		return worldIcon;
	}
	
	@Override
	protected void levelUp(int level) {
		if (level > 0) {
			this.level += level;
			Random r = new Random(83948);
			this.stats.put(Stats.MOTIVATION, getStat(Stats.MOTIVATION) + r.nextInt(3) + 1);
			this.stats.put(Stats.INTELLIGIENCE, getStat(Stats.INTELLIGIENCE) + r.nextInt(3) + 1);
			this.stats.put(Stats.WIT, getStat(Stats.WIT) + r.nextInt(3)+1);
			this.stats.put(Stats.ENDURANCE, getStat(Stats.ENDURANCE) +r.nextInt(3)+ 1);
			this.stats.put(Stats.STAMINA, getStat(Stats.STAMINA) + r.nextInt(3)+1);
			updateDerivedStats();
		}
		if(this.level == 5){
			ABILITIES.add(new PullAnAllNighter());
		}
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
		switch (sex) {
		case BOY:
			worldImage = new Image(getClass().getResourceAsStream("/images/MaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/malebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/maleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/maletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/maleattack.png"));
			break;
		case GIRL:
			worldImage = new Image(getClass().getResourceAsStream("/images/FemaleWalk.png"));
			battleImage = new Image(getClass().getResourceAsStream("/images/femalebattleicon.png"));
			worldIcon = new Image(getClass().getResourceAsStream("/images/femaleicon.png"));
			takeDamageAnimation = new Image(getClass().getResourceAsStream("/images/femaletakedamage.png"));
			attackAnimation = new Image(getClass().getResourceAsStream("/images/femaleattack.png"));
			break;
		default:
			break;
		}
	}
	public void moveUpFloor() {
		setFloorNum(getFloorNum() + 1);
	}

}
