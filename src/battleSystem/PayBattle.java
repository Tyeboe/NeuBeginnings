package battleSystem;

import java.util.Random;

import abilities.AddValue;
import abilities.AssignATeamAssignment;
import abilities.ShowAPowerpoint;
import application.GameEngine;
import character.Boss;
import character.Character;
import character.Player;
import enums.Character.InventoryAction;
import interfaces.ability.Ability;
import itemSystem.Item;

public class PayBattle extends BossBattle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9056371708169981166L;

	public PayBattle(Player player, Boss boss) {
		super(player, boss);
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
				} else if (turnList[i] instanceof Boss) {
					if (turnList[i].getHPProperty().get() > 0) {
						Random r = new Random();
						switch (r.nextInt(5)) {
						case 0: case 1:
							if (boss.getAbilities().size() != 0) {
								Ability bossAbility = boss.getAbilities().get(r.nextInt(boss.getAbilities().size()));
								if(boss.getCurrentHealth() < boss.getMaxHealth()-50 && !((AddValue)boss.getAbilities().get(2)).getUsedBuff()) {
									bossAbility = boss.getAbilities().get(2);
								} else if(bossAbility == boss.getAbilities().get(1) && ((AssignATeamAssignment)(boss.getAbilities().get(1))).getTimesForUse() != 0){
									bossAbility = boss.getAbilities().get(1);
								} else if(bossAbility == boss.getAbilities().get(0) && ((ShowAPowerpoint)(boss.getAbilities().get(0))).getTimesForUse() != 0){
									bossAbility = boss.getAbilities().get(0);
								}
								boss.ability(bossAbility, player);
								loggedAction = boss.NAME + " Used: " + bossAbility.NAME;
								if(bossAbility instanceof AddValue){
									loggedAction += "\n" + boss.NAME + "'s stats and health went up.";
								}
								break;
							}
						case 2: case 3: case 4:
							player.takeDmg(boss.attack());
							loggedAction = boss.NAME + " Attacked " + player.NAME;
							break;
						default:
							break;
						}
						notifySubscribers();
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
