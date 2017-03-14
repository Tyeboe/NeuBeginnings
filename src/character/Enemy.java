package character;

import java.util.Random;

import application.GameEngine;
import enums.Character.Stats;
import itemSystem.Coffee;
import itemSystem.Doritos;
import itemSystem.Inventory;
import itemSystem.MountainDew;
import itemSystem.Ramen;

public class Enemy extends Character {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4495778022214568163L;
	protected int creditDrop;

	public Enemy(String name, int creditDrop, int tileSheetNum) {
		super(name, tileSheetNum);
		this.creditDrop = creditDrop;
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
		inv = genLoot();
	}

	public int getCreditDrop() {
		return creditDrop;
	}

	private Inventory genLoot() {
		Inventory tempInv = new Inventory();
		Random xD = new Random();

		int howMuchLoot = xD.nextInt(3);
		int item = xD.nextInt(4);

		for (int i = 0; i < howMuchLoot; i++) {
			switch (item) {
			case 0:
				tempInv.addItem(new Coffee());
				break;
			case 1:
				tempInv.addItem(new Doritos());
				break;
			case 2:
				tempInv.addItem(new MountainDew());
				break;
			case 3:
				tempInv.addItem(new Ramen());
				break;
			}
		}

		return tempInv;
	}

	@Override
	public int takeDmg(int dmg) {
		GameEngine.playTakeDamageAnimation(takeDamageAnimation, this);
		if (dmg - getStat(Stats.ENDURANCE) <= 0) {
			hitPoints -= 1;
		} else {
			hitPoints -= (dmg - getStat(Stats.ENDURANCE));
		}
		hitPoints = hitPoints < 0 ? 0 : hitPoints;
		hpProperty.set(hitPoints);

		return dmg;
	}

	@Override
	public int attack() {
		// GameEngine.playAttackAnimation(attackAnimation, this);
		int damage = 0;
		damage = getStat(Stats.INTELLIGIENCE);
		return damage;
	}
	
	@Override
	protected void updateDerivedStats() {
		this.hitPoints = stats.get(Stats.MOTIVATION);
		this.maxHitPoints = hitPoints;
		this.energy = stats.get(Stats.STAMINA);
		this.maxEnergy = energy;
		energyProperty.set(energy);
		maxEnergyProperty.set(energy);
		hpProperty.set(hitPoints);
		maxHPProperty.set(hitPoints);
	}

}
