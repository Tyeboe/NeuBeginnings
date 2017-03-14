package battleSystem;

import java.util.Random;

import abilities.PlayEnchantment;
import application.GameEngine;
import character.Boss;
import character.Character;
import character.Player;
import enums.Character.InventoryAction;
import interfaces.ability.Ability;
import itemSystem.Item;

public class GuardBattle extends BossBattle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5672939660219233101L;

	public GuardBattle(Player player, Boss boss) {
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
						case 0:
						case 1:
							if (boss.getAbilities().size() != 0) {
								if (boss.getCurrentHealth() <= (boss.getMaxHealth() - 20)
										&& !((PlayEnchantment) boss.getAbilities().get(1)).getUsedBuff()) {
									Ability nextAbility = boss.getAbilities().get(1);
									loggedAction = turnList[i].NAME + ": Used " + nextAbility;
									notifySubscribers();
									boss.ability(nextAbility, player);
								} else {
									Ability nextAbility = boss.getAbilities().get(0);
									loggedAction = turnList[i].NAME + ": Used " + nextAbility;
									notifySubscribers();
									boss.ability(nextAbility, player);
								}
								break;
							}
						case 2:
						case 3:
						case 4:
							loggedAction = turnList[i].NAME + ": Attacked " + player.NAME;
							notifySubscribers();
							player.takeDmg(boss.attack());
							break;
						default:
							break;
						}
					}

				}
				// NOTE(andrew): check if the player is dead
				if (player.getHPProperty().get() <= 0) {
					battleOngoing = false;
				}
				// NOTE(dakota): only need to check if boss is dead
				if (boss.getHPProperty().get() > 0) {
					allEnemiesDead = false;
				}

				if (allEnemiesDead) {
					battleOngoing = false;
					Item[] loot = boss.getInventoryContents();
					player.modifyInventory(InventoryAction.GIVE, loot);
					player.giveCredits(boss.getCreditDrop());
					creditsDropped = boss.getCreditDrop();
				}

			}
		} while (battleOngoing);
		isCompleted = true;
		subscribers.clear();
		GameEngine.displayEndBattle(this, leveledUp);
	}
}
