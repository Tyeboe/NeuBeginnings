package battleSystem;

import java.util.Random;

import abilities.AssignFraculator;
import abilities.PlayDivision;
import application.GameEngine;
import character.Boss;
import character.Character;
import character.JoshKrebs;
import character.Krebsinator;
import character.PickOnYou;
import character.Player;
import enums.Character.InventoryAction;
import interfaces.ability.Ability;
import interfaces.ability.AttackAbility;
import interfaces.ability.BuffAbility;
import itemSystem.Item;

public class KrebsBattle extends BossBattle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3108289910004524673L;

	public KrebsBattle(Player player, Boss boss) {
		super(player, boss, new Krebsinator());
	}

	@Override
	public void start() {
		Character[] turnList = createTurnList();

		boolean battleOngoing = true;
		boolean allEnemiesDead = false;
		do {
			for (int i = 0; i < turnList.length; i++) {
				// NOTE(andrew): initialize this to true, but it will be
				// validated before it is used
				allEnemiesDead = true;
				if (turnList[i] instanceof Player) {
					playerTakesTurn();
				} else if (turnList[i] instanceof JoshKrebs) {
					if (turnList[i].getHPProperty().get() > 0) {
						JoshKrebs boss = (JoshKrebs) turnList[i];
						Random r = new Random();
						switch (r.nextInt(5)) {
						case 0: case 1:
							if (boss.getAbilities().size() != 0) {
								Ability bossAbility = boss.getAbilities()
										.get(r.nextInt(boss.getAbilities().size()));
								
								if(bossAbility == boss.getAbilities().get(1) && !(((PlayDivision)boss.getAbilities().get(1)).getUsedBuff())) {
									bossAbility = boss.getAbilities().get(1);
								} else if(bossAbility == boss.getAbilities().get(0) && !(((AssignFraculator)boss.getAbilities().get(0)).getUsedBuff())) {
									bossAbility = boss.getAbilities().get(0);
								}
								
								if (bossAbility instanceof AttackAbility || bossAbility instanceof BuffAbility) {
									boss.ability(bossAbility, player);
									loggedAction = boss.NAME + " Used: " + bossAbility;
									break;
								} else if ((bossAbility) instanceof PickOnYou) {
									boss.ability(bossAbility, this.boss);
									loggedAction = boss.NAME + " Used: " + bossAbility + "\nMr. Krebs Wit went up!";
									break;
								}
							}
						case 2: case 3: case 4:
							player.takeDmg(boss.attack());
							loggedAction = boss.NAME + " attacked " + player.NAME;
							break;
						default:
							break;
						}
						notifySubscribers();
					}

				} else if(turnList[i] instanceof Krebsinator){
					Krebsinator boss = (Krebsinator) turnList[i];
					Random r = new Random();
					switch (r.nextInt(5)) {
					case 0: case 1:
						if (boss.getAbilities().size() != 0) {
							Ability bossAbility = boss.getAbilities()
									.get(r.nextInt(boss.getAbilities().size()));
							
							if(bossAbility == boss.getAbilities().get(0)) {
								bossAbility = boss.getAbilities().get(0);
							}
							
							if ((bossAbility) instanceof PickOnYou) {
								boss.ability(bossAbility, this.boss);
								loggedAction = boss.NAME + " Used: " + bossAbility + "\nMr. Krebs Wit went up!";
								break;
							}
						}
					case 2: case 3: case 4:
						player.takeDmg(boss.attack());
						loggedAction = boss.NAME + " attacked " + player.NAME;
						break;
					default:
						break;
					}
					notifySubscribers();
				}
					else {
					// NOTE(andrew): this branch runs if it's the enemies turn,
					// this should probably be changed, not totally sure,
					// because
					// this AI is linear, the enemy will always attack
					if (turnList[i].getHPProperty().get() > 0) {
						player.takeDmg(turnList[i].attack());
						// System.out.println("EnemyAttacked");
					}
				}
				// NOTE(andrew): check if the player is dead
				if (player.getHPProperty().get() <= 0) {
					battleOngoing = false;
				}
				// NOTE(andrew): this validates that the enemies are dead
				for (int j = 0; j < enemies.length; j++) {
					if (enemies[j].getHPProperty().get() > 0) {
						allEnemiesDead = false;
					}
				}

				if (allEnemiesDead) {
					battleOngoing = false;
					for (int j = 0; j < enemies.length; j++) {
						Item[] loot = enemies[j].getInventoryContents();
						player.modifyInventory(InventoryAction.GIVE, loot);
						player.giveCredits(enemies[j].getCreditDrop());
						creditsDropped += enemies[j].getCreditDrop();
					}

					break;
				}

			}
		} while (battleOngoing);
		// System.out.println(Thread.currentThread().getName());
		isCompleted = true;
		subscribers.clear();
		GameEngine.displayEndBattle(this, leveledUp);
	}

}
