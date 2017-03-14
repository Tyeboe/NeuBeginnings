package battleSystem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

import application.GameEngine;
import character.Character;
import character.Enemy;
import character.Player;
import enums.Character.InventoryAction;
import interfaces.ability.Ability;
import interfaces.ability.GroupAbility;
import interfaces.item.Usable;
import interfaces.publisherSubscriber.Listener;
import interfaces.publisherSubscriber.Subscribable;
import itemSystem.Item;
import models.Coordinates;

public class Battle implements Subscribable<Battle>, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3115042468203904551L;
	protected boolean leveledUp = false;
	protected Player player;
	protected Enemy[] enemies;
	private Coordinates place = new Coordinates(0, 0);
	protected ArrayList<Listener<Battle>> subscribers = new ArrayList<Listener<Battle>>();
	protected Ability playerNextAbility = null;
	protected Usable playerNextItemUse = null;
	protected boolean isCompleted = false;
	protected String loggedAction = null;
	protected Character playerTarget = null;
	protected int creditsDropped;
	private ArrayList<Item> itemsDropped = new ArrayList<Item>();

	public Battle(Player player, Enemy... enemies) {
		if (player == null) {
			throw new IllegalArgumentException("The player in a battle cannot be null");
		}
		if (enemies == null) {
			throw new IllegalArgumentException("The enemies in a battle cannot be null");
		}
		if (enemies.length == 0) {
			throw new IllegalArgumentException("Battle must have at least 1 enemy.");
		}
		this.enemies = enemies;
		this.player = player;
	}

	protected Battle(Player player) {
		if (player == null) {
			throw new IllegalArgumentException("The player in a battle cannot be null");
		}
		this.player = player;
	}

	public void setPlayerNextItemUse(Usable playerNextItemUse) {
		this.playerNextItemUse = playerNextItemUse;
		this.playerNextAbility = null;
	}

	protected void playerTakesTurn() {
		GameEngine.playerBattleInput(this);

		// Player chose to use an ability
		if (playerNextAbility != null) {

			loggedAction = player.NAME + ": Used " + playerNextAbility;
			notifySubscribers();
			boolean successfulAbilityUse = false;
			if (playerNextAbility instanceof GroupAbility) {
				successfulAbilityUse = player.ability(playerNextAbility, enemies);
			} else {
				successfulAbilityUse = player.ability(playerNextAbility, playerTarget);
			}
			if(!successfulAbilityUse){
				loggedAction = "But it failed!";
				notifySubscribers();
			}

			// Player chose to use an item.
		} else if (playerNextItemUse != null) {
			loggedAction = player.NAME + ": Used " + (playerNextItemUse);
			playerNextItemUse.use(player);
			player.modifyInventory(InventoryAction.TAKE, (Item)playerNextItemUse);
			notifySubscribers();
			// If the player did not choose to use an ability or item but their
			// target was not null.
			// Attacks the target.
		} else if (playerTarget != null) {
			loggedAction = player.NAME + ": Attacked " + playerTarget.NAME;
			notifySubscribers();
			playerTarget.takeDmg(player.attack());

		}
	}

	public void start() {
		Character[] turnList = createTurnList();
		boolean battleOngoing = true;
		boolean allEnemiesDead = false;
		do {
			for (int i = 0; i < turnList.length; i++) {
				allEnemiesDead = true;
				//If the player should take their turn
				if (turnList[i] instanceof Player) {
					playerTakesTurn();
					
					//Else the enemy next in line will take his turn.
				} else {
					if (turnList[i].getHPProperty().get() > 0) {
						loggedAction = turnList[i].NAME + ": Attacked " + player.NAME;
						notifySubscribers();
						player.takeDmg(turnList[i].attack());

					}
				}
				// NOTE(andrew): check if the player is dead
				if (player.getHPProperty().get() <= 0) {
					battleOngoing = false;
				}
				// NOTE(andrew): checks that the enemies are dead
				for (int j = 0; j < enemies.length; j++) {
					if (enemies[j].getHPProperty().get() > 0) {
						allEnemiesDead = false;
					}
				}
				//If all enemies are dead, Do necessary cleanup before exiting.
				if (allEnemiesDead) {
					battleOngoing = false;
					for (int j = 0; j < enemies.length; j++) {
						Item[] loot = enemies[j].getInventoryContents();
						player.modifyInventory(InventoryAction.GIVE, loot);
						itemsDropped.addAll(Arrays.asList(loot));
						if (player.giveCredits(enemies[j].getCreditDrop())) {
							leveledUp = true;
						}
						creditsDropped += enemies[j].getCreditDrop();
					}

					break;
				}

			}
		} while (battleOngoing);
		//Clears any listener to the battle after it has ended
		subscribers.clear();
		isCompleted = true;
		GameEngine.displayEndBattle(this, leveledUp);
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public Enemy[] getEnemies() {
		return enemies;
	}

	protected void setEnemies(Enemy... enemies) {
		this.enemies = enemies;
	}

	public Coordinates getCoordinates() {
		return new Coordinates(place);
	}

	public void setPlayerNextAbility(Ability playerNextAbility) {
		this.playerNextAbility = playerNextAbility;
		this.playerNextItemUse = null;
	}

	public void setPlayerTarget(Character playerTarget) {
		this.playerTarget = playerTarget;
	}

	private Character[] createTurnList() {
		//Sorts the array based on each Characters wit stat.
		ArrayList<Character> initial = new ArrayList<>();
		initial.add(player);
		initial.addAll(Arrays.asList(enemies));
		Character[] turnList = initial.toArray(new Character[0]);
		Arrays.sort(turnList, Character::compareWit);

		return turnList;
	}

	@Override
	public void addSubscriber(Listener<Battle> sub) {
		if (sub != null) {
			sub.update(loggedAction);
			subscribers.add(sub);
		}
	}

	@Override
	public void removeSubscriber(Listener<Battle> sub) {
		if (sub != null) {
			subscribers.remove(sub);
		}
	}

	@Override
	public void notifySubscribers() {
		for (Listener<Battle> sub : subscribers) {
			sub.update(loggedAction);
		}
	}

	public Player getPlayer() {
		return player;
	}

	public synchronized String getLoggedAction() {
		return loggedAction;
	}

	public int getCreditsDropped() {
		return creditsDropped;
	}

	public Item[] getItemDrops() {
		return itemsDropped.toArray(new Item[0]);
	}

}
