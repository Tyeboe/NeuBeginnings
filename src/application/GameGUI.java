package application;

/**
 * battle regen on floors / battle occur by chance after
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import battleSystem.Battle;
import battleSystem.KrebsBattle;
import character.Character;
import character.Enemy;
import character.Player;
import enums.Character.Direction;
import enums.Character.Genders;
import enums.Character.InventoryAction;
import enums.Character.Stats;
import enums.gui.GUILayouts;
import floors.Floor;
import interfaces.ability.Ability;
import interfaces.item.Usable;
import interfaces.tile.Lootable;
import itemSystem.Item;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.Coordinates;
import tiles.TileManager;

public class GameGUI extends Application {

	private Stage primaryStage;
	private GUILayouts currentLayout = GUILayouts.MAIN_MENU;
	private Game TESTINGGAME = GameEngine.getGame();
	private boolean isPlayersTurn = false;
	private Object lock = new Object();
	private Object takeDamageAnimationLock = new Object();
	private Object attackAnimationLock = new Object();
	private Parent p;
	private TextArea displayText = new TextArea("");
	private boolean isAnimating = false;
	{
		displayText.setId("displayText");
		displayText.setWrapText(true);
		displayText.setFocusTraversable(false);
		displayText.setEditable(false);
	}

	@FXML
	ImageView characterView;

	@FXML
	private Canvas playerBattleCanvas;

	@FXML
	private Button exitButton;
	@FXML
	private ImageView playerImageView;
	@FXML
	private Label playerName;
	@FXML
	private ProgressBar playerEnergyBar;
	@FXML
	private ProgressBar playerHealthBar;
	@FXML
	private Canvas canvas;
	@FXML
	private GridPane playerInventoryGrid;
	@FXML
	private ListView<Item> otherInventoryGrid;
	@FXML
	private Button lootManagerButton;
	@FXML
	private Button exitLootButton;
	@FXML
	private GridPane statGrid;

	public static void main(String[] args) {
		launch();
	}

	@Override
	public void init() {
		GameEngine.setView(this);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			this.primaryStage = primaryStage;
			displayMainMenu();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// MainMenu specific elements
	@FXML
	private Button newGameButton;
	@FXML
	private Button loadGameButton;

	public void displayMainMenu() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/MainMenuView.fxml"));

		loader.setController(this);

		try {

			p = loader.load();

			newGameButton.setOnAction(event -> {
				displayCharacterCreation();

			});

			loadGameButton.setOnAction(event -> {
				TESTINGGAME = GameEngine.loadGame();
				if (TESTINGGAME == null) {
					displayCharacterCreation();
				} else {
					displayGeneralView();
				}
			});

			exitButton.setOnAction(event -> {
				Platform.exit();
			});
			Scene scene = new Scene(p);
			scene.getStylesheets().add(

					getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (

		IOException e) {
			e.printStackTrace();
		}

	}

	// PauseMenu specific elements
	@FXML
	private Button mainMenuButton;
	@FXML
	private Button saveGameButton;
	@FXML
	private Button characterButton;

	public void displayPauseMenu() {
		this.currentLayout = GUILayouts.PAUSE;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/PauseView.fxml"));
		loader.setController(this);
		try {
			p = loader.load();
			p.setOnKeyPressed(event -> {
				KeyCode k = event.getCode();
				switch (k) {
				case ESCAPE:
					displayGeneralView();
					break;
				default:
					break;
				}

			});
			mainMenuButton.setOnMouseClicked(event -> {
				displayMainMenu();
			});

			saveGameButton.setOnMouseClicked(event -> {
				displayMessage("Saved to\n" + GameEngine.saveGame(TESTINGGAME));
			});

			characterButton.setOnMouseClicked(event -> {
				displayCharacterManager();
			});

			exitButton.setOnMouseClicked(event -> {
				displayGeneralView();
			});
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Battle Specific Elements
	@FXML
	private Pane rightBattleVBox;
	@FXML
	private ListView<String> leftActionList;

	private ListView<Ability> abilityList;

	@FXML
	private Button submitButton;
	@FXML
	private HBox enemies;
	@FXML
	private VBox middleBattleVBox;
	@FXML
	private Label battleTextLabel;
	@FXML
	private VBox battleLogVBox;
	@FXML
	private ScrollPane bLogScrollPane;
	private ListView<Usable> itemList;

	public void displayBattleView(Battle b) {

		this.currentLayout = GUILayouts.BATTLE;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/BattleView.fxml"));

		loader.setController(this);
		try {
			p = loader.load();
			TESTINGGAME.getPlayer().setBattleImageView(playerImageView);
			Image playerBattleImage = TESTINGGAME.getPlayer().getBattleImage();
			playerImageView.setImage(playerBattleImage);

			playerName.setText(TESTINGGAME.getPlayer().NAME + " Lvl. " + TESTINGGAME.getPlayer().getLevel());
			enemies.setAlignment(Pos.CENTER);
			b.addSubscriber(loggedAction -> {
				Platform.runLater(() -> {
					Text l = new Text(loggedAction);
					l.maxWidth(120);
					l.setWrappingWidth(130);
					battleLogVBox.getChildren().add(l);
					bLogScrollPane.vvalueProperty().bind(battleLogVBox.heightProperty());
				});
			});
			ArrayList<Label> enemyNames = new ArrayList<Label>();
			for (int i = 0; i < b.getEnemies().length; i++) {
				Enemy currentEnemy = b.getEnemies()[i];
				ImageView enemyImage = new ImageView();
				enemyImage.setImage(currentEnemy.getBattleImage());
				enemyImage.setFitHeight(100);
				enemyImage.setFitWidth(100);
				enemyImage.setPreserveRatio(true);
				Label enemyName = new Label(currentEnemy.NAME + " Lvl. " + currentEnemy.getLevel());
				enemyNames.add(enemyName);
				ProgressBar enemyHealth = new ProgressBar();
				enemyHealth.progressProperty()
						.bind(currentEnemy.getHPProperty().divide(currentEnemy.getMaxHPProperty().doubleValue()));
				Group child = new Group();
				enemies.getChildren().add(child);
				enemyHealth.progressProperty().addListener((observable, oldValue, newValue) -> {
					while (currentEnemy.isBattleAnimating()) {
						try {
							synchronized (takeDamageAnimationLock) {
								takeDamageAnimationLock.wait();
							}
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					Platform.runLater(() -> {
						if (newValue.doubleValue() <= 0) {
							enemies.getChildren().remove(child);
							Node n = null;
							try {
								n = enemies.getChildren().get(0);
							} catch (IndexOutOfBoundsException e) {
							}
							if (n != null) {
								n.getOnMouseClicked().handle(null);
							}
						}
					});
				});
				currentEnemy.setBattleImageView(enemyImage);

				VBox mainContainer = new VBox(currentEnemy.getBattleImageView(), enemyName, enemyHealth);
				child.getChildren().add(mainContainer);
				child.setOnMouseClicked(event -> {
					b.setPlayerTarget(currentEnemy);
					for (Label name : enemyNames) {
						name.styleProperty().setValue("");
					}
					enemyName.setStyle("-fx-background-color : lightblue;");
				});
				if (i == 0) {
					child.getOnMouseClicked().handle(null);
				}
			}
			submitButton.setDisable(true);
			leftActionList.setItems(FXCollections.observableArrayList("Attack", "Abilities", "Items"));
			leftActionList.setOnMouseClicked(event -> {
				switch (event.getButton()) {
				case PRIMARY:
					switch (leftActionList.getSelectionModel().getSelectedIndex()) {
					case 0:
						middleBattleVBox.getChildren().clear();
						rightBattleVBox.getChildren().clear();
						if (isPlayersTurn) {
							submitButton.setDisable(false);
							abilityList = null;
						}
						break;
					case 1:
						if (isPlayersTurn) {
							submitButton.setDisable(true);
						}
						middleBattleVBox.getChildren().clear();
						abilityList = new ListView<>(TESTINGGAME.getPlayer().getAbilities());
						middleBattleVBox.getChildren().add(abilityList);
						abilityList.getSelectionModel().selectedItemProperty()
								.addListener((observable, oldValue, newValue) -> {
									rightBattleVBox.getChildren().clear();
									if (abilityList.getSelectionModel().getSelectedItem() != null) {
										Label abilityDescription = new Label(
												abilityList.getSelectionModel().getSelectedItem().getDescription());
										abilityDescription.wrapTextProperty().set(true);
										rightBattleVBox.getChildren().add(abilityDescription);
										if (isPlayersTurn) {
											submitButton.setDisable(false);
										}
									}

								});
						abilityList.getSelectionModel().selectFirst();
						break;
					case 2:
						if (isPlayersTurn) {
							submitButton.setDisable(true);
							abilityList = null;
						}
						middleBattleVBox.getChildren().clear();
						rightBattleVBox.getChildren().clear();
						ArrayList<Usable> usable = new ArrayList<>();
						for (int i = 0; i < TESTINGGAME.getPlayer().getInventoryContents().length; i++) {
							if (TESTINGGAME.getPlayer().getInventoryContents()[i] instanceof Usable) {
								usable.add((Usable) TESTINGGAME.getPlayer().getInventoryContents()[i]);
							}
						}
						itemList = new ListView<Usable>(FXCollections.observableArrayList(usable));
						itemList.getSelectionModel().selectedItemProperty()
								.addListener((observable, oldValue, newValue) -> {
									if (isPlayersTurn) {
										submitButton.setDisable(false);
									}
									Label l = new Label(((Item) newValue).getDescription());
									l.wrapTextProperty().set(true);
									rightBattleVBox.getChildren().clear();
									rightBattleVBox.getChildren().add(l);
								});
						itemList.getSelectionModel().select(0);
						middleBattleVBox.getChildren().add(itemList);
						break;
					}
					break;
				default:
					break;
				}
			});

			submitButton.setOnAction(event -> {
				submitButton.setDisable(true);
				synchronized (lock) {
					isPlayersTurn = false;
					lock.notifyAll();
				}
			});
			playerEnergyBar.progressProperty().bind(TESTINGGAME.getPlayer().getEnergyProperty()
					.divide(TESTINGGAME.getPlayer().getMaxEnergyProperty().doubleValue()));
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);

			primaryStage.setScene(scene);
			primaryStage.show();
			GameEngine.startBattle(b);
		} catch (

		IOException e) {
			e.printStackTrace();
		}
	}

	public void waitForPlayerSelection(Battle battle) {
		try {
			isPlayersTurn = true;
			submitButton.setDisable(false);
			synchronized (lock) {
				lock.wait();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		switch (leftActionList.getSelectionModel().getSelectedIndex()) {
		case 0:
			// attack
			battle.setPlayerNextAbility(null);
			// NOTE(andrew): targets are set in the enemy onclick method
			break;

		case 1:
			// ability
			battle.setPlayerNextAbility(abilityList.getSelectionModel().getSelectedItem());
			break;

		case 2:
			// items
			battle.setPlayerNextItemUse(itemList.getSelectionModel().getSelectedItem());
			Platform.runLater(() -> {
				try {
					Thread.sleep(10L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				leftActionList.getOnMouseClicked().handle(new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY, 1, false, false, false, false, false, false, false, false, false, false, null));				
			});
			break;

		default:
			break;
		}
	}

	// GeneralView specific elements
	@FXML
	private Button menuButton;

	public void displayGeneralView() {
		this.currentLayout = GUILayouts.GENERAL;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/GeneralView.fxml"));

		loader.setController(this);
		try {
			p = loader.load();
			playerImageView.setImage(TESTINGGAME.getPlayer().getWorldIcon());
			drawToGeneralCanvas(TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0, 0);
			p.setOnKeyPressed(event -> {
				if (currentLayout == GUILayouts.GENERAL) {
					KeyCode keyEvent = event.getCode();

					switch (keyEvent) {
					case W:
						if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
							if (!isAnimating && GameEngine.checkMovement(Direction.UP)) {
								playMoveAnimation(Direction.UP);
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.UP);
							} else if (!isAnimating) {
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.UP);
								drawToGeneralCanvas(
										TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0, 0);
							}
						}
						break;
					case S:
						if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
							if (!isAnimating && GameEngine.checkMovement(Direction.DOWN)) {
								playMoveAnimation(Direction.DOWN);
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.DOWN);
							} else if (!isAnimating) {
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.DOWN);
								drawToGeneralCanvas(
										TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0, 0);

							}
						}
						break;
					case A:
						if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
							if (!isAnimating && GameEngine.checkMovement(Direction.LEFT)) {
								playMoveAnimation(Direction.LEFT);
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.LEFT);
							} else if (!isAnimating) {
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.LEFT);
								drawToGeneralCanvas(
										TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0, 0);
							}
						}
						break;
					case D:
						if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
							if (!isAnimating && GameEngine.checkMovement(Direction.RIGHT)) {
								playMoveAnimation(Direction.RIGHT);
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.RIGHT);
							} else if (!isAnimating) {
								TESTINGGAME.getPlayer().setDirectionFacing(Direction.RIGHT);
								drawToGeneralCanvas(
										TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0, 0);
							}
						}
						break;
					case E:
						if (((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
							((AnchorPane) primaryStage.getScene().getRoot()).getChildren().remove(displayText);
							GameEngine.checkLoot();
						} else {
							if (!GameEngine.checkForBoss()) {
								GameEngine.checkNote();
							}
						}

						break;
					case ESCAPE:
						displayPauseMenu();
						break;
					default:
						break;
					}
					// NOTE(andrew): added this if statement to ensure that
					// this code only runs when it needs to.
				}
			});
			// TODO
			for (int i = 0; i < Stats.values().length; i++) {
				Label stat = new Label(Stats.values()[i].toString().substring(0, 3));
				Label statNum = new Label(TESTINGGAME.getPlayer().getStat(Stats.values()[i]) + "");
				statGrid.addRow(i, stat, statNum);
			}
			statGrid.addRow(Stats.values().length, new Label("LVL"),
					new Label("" + TESTINGGAME.getPlayer().getLevel()));
			menuButton.setOnMouseClicked(event -> {
				displayPauseMenu();
			});
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			playerEnergyBar.progressProperty().bind(TESTINGGAME.getPlayer().getEnergyProperty()
					.divide(TESTINGGAME.getPlayer().getMaxEnergyProperty().doubleValue()));
			playerEnergyBar.setId("playerEnergyBar");
			playerName.setText(TESTINGGAME.getPlayer().NAME);
			Scene scene = new Scene(p);
			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void displayMessage(String message) {
		displayText.setText(message);

		Platform.runLater(() -> {
			if (!((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
				((AnchorPane) primaryStage.getScene().getRoot()).getChildren().add(displayText);
			}
			// NOTE(andrew): this must be an event filter that is passed in
			// the type of event and its function, rather than using the
			// setOnKeyPressed() method, because there are selections made
			// in the battle view, and those selections eat up the ESCAPE
			// event. Using this filter allows us to read the keycode before
			// it is eaten up.
			p.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
				if ((event.getCode().equals(KeyCode.ESCAPE) || event.getCode().equals(KeyCode.E))
						&& currentLayout.equals(GUILayouts.BATTLE)) {
					displayGeneralView();
				} else if (((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)
						&& (currentLayout.equals(GUILayouts.PAUSE) || currentLayout.equals(GUILayouts.LOOT_MANAGER))) {
					((AnchorPane) primaryStage.getScene().getRoot()).getChildren().remove(displayText);
				}
			});
		});
	}

	public void displayEndBattle(Battle b, boolean leveledUp) {
		Player player = b.getPlayer();
		if (player.getHPProperty().get() > 0) {
			// TODO(andrew): pop a text view displaying loot and exp/level gain
			// stats
			String itemsDropped = "Item Drops:\n";
			for (Item i : b.getItemDrops()) {
				itemsDropped += i.NAME + "\n";
			}
			String levelUp = "";
			String extraMessage = "";
			if (leveledUp) {
				levelUp += "\n";
				levelUp += "You leveled up! Here are your new stats!\n";
				Set<Stats> keys = b.getPlayer().getStats().keySet();
				Stats[] stats = keys.toArray(new Stats[0]);
				for (int i = 0; i < stats.length; i++) {
					levelUp += stats[i] + ": " + b.getPlayer().getStat(stats[i]) + "\n";
				}
				if (b.getPlayer().getLevel() == 5) {
					levelUp += "You learned a new ability!\n";
				}
			}
			if (b instanceof KrebsBattle) {
				extraMessage += "Congratulations! You have defeated Mr. Doctor Professor Kaiser Krebs! You are free to roam the third floor for as long as you want!";
			}
			displayMessage(
					extraMessage + "Credits Earned: " + b.getCreditsDropped() + "\n" + levelUp + "\n" + itemsDropped);
		} else {
			// TODO(andrew): pop a text view displaying "YOU SUCK" or something
			// along those lines.
			displayText.setText("Game Over\nPress escape to go back to the Main Menu.");
			Platform.runLater(() -> {
				((AnchorPane) primaryStage.getScene().getRoot()).getChildren().add(displayText);
				// NOTE(andrew): this must be an event filter that is passed
				// in the type of event and its function, rather than using
				// the
				// setOnKeyPressed() method, because there are selections
				// made in the battle view, and those selections eat up the
				// escape
				// event. Using this filter allows us to read the keycode
				// before it is eaten up.
				p.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
					if (event.getCode().equals(KeyCode.ESCAPE) && currentLayout.equals(GUILayouts.BATTLE)) {
						displayMainMenu();
					}
				});
			});
		}
	}

	private void drawToGeneralCanvas(Floor currentFloor, int offsetX, int offsetY) {
		TESTINGGAME.getFloors();
		GraphicsContext gc = canvas.getGraphicsContext2D();
		gc.clearRect(0, 0, gc.getCanvas().getWidth(), gc.getCanvas().getHeight());
		WritableImage image = TileManager.getImageToDraw(currentFloor.getTiles(),
				currentFloor.getPlayer().getCoordinates());
		int currentImageIndex = 0;
		int imageRow = 0;
		switch (currentFloor.getPlayer().getDirectionFacing()) {

		case UP:
			currentImageIndex = Math.abs((offsetY % 32) / 16);
			imageRow = 0;
			break;
		case DOWN:
			currentImageIndex = Math.abs((offsetY % 32) / 16);
			imageRow = 2;
			break;

		case LEFT:
			currentImageIndex = Math.abs((offsetX % 32) / 16);
			imageRow = 3;
			break;
		case RIGHT:
			currentImageIndex = Math.abs((offsetX % 32) / 16);
			imageRow = 1;
			break;
		}
		Image playerImg = currentFloor.getPlayer().getWorldImage();
		gc.drawImage(image, -64 + offsetX, -64 + offsetY, image.getWidth() * 2, image.getHeight() * 2);
		gc.drawImage(playerImg, currentImageIndex * 32, imageRow * 32, 32, 32, (canvas.getWidth() / 2) - 32,
				(canvas.getHeight() / 2) - 32, 64, 64);
	}

	public void displayCharacterManager() {
		currentLayout = GUILayouts.PLAYER_MENU;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/CharacterView.fxml"));
		loader.setController(this);

		try {
			p = loader.load();
			p.setOnKeyPressed(event -> {
				KeyCode k = event.getCode();
				switch (k) {
				case ESCAPE:
					displayPauseMenu();
					break;
				default:
					break;
				}
			});

			exitButton.setOnMouseClicked(event -> {
				displayPauseMenu();
			});
			characterView.setImage(TESTINGGAME.getPlayer().getWorldIcon());
			characterView.setFitWidth(150);
			playerName.setText(TESTINGGAME.getPlayer().NAME + " Lvl. " + TESTINGGAME.getPlayer().getLevel());
			playerHealthBar.progressProperty().bind(TESTINGGAME.getPlayer().getHPProperty()
					.divide(TESTINGGAME.getPlayer().getMaxHPProperty().doubleValue()));
			playerEnergyBar.progressProperty().bind(TESTINGGAME.getPlayer().getEnergyProperty()
					.divide(TESTINGGAME.getPlayer().getMaxEnergyProperty().doubleValue()));
			for (int i = 0; i < Stats.values().length; i++) {
				Label stat = new Label(Stats.values()[i].toString());
				Label statNum = new Label(TESTINGGAME.getPlayer().getStat(Stats.values()[i]) + "");
				statGrid.addRow(i, stat, statNum);
			}
			for (int i = 0; i < TESTINGGAME.getPlayer().getInventoryContents().length; i++) {
				Item theItem = TESTINGGAME.getPlayer().getInventoryContents()[i];
				Menu m = new Menu();
				Label l = new Label(theItem.toString() + ": " + theItem.getDescription());
				l.setPrefWidth(playerInventoryGrid.getPrefWidth());
				m.setGraphic(l);
				MenuBar item = new MenuBar(m);

				GridPane.setHalignment(item, HPos.CENTER);
				if (theItem instanceof Usable) {
					MenuItem use = new MenuItem("Use");
					MenuItem drop = new MenuItem("Drop");
					use.setOnAction(event -> {
						((Usable) theItem).use(TESTINGGAME.getPlayer());
						TESTINGGAME.getPlayer().modifyInventory(InventoryAction.TAKE, theItem);
						playerInventoryGrid.getChildren().remove(item);
					});
					drop.setOnAction(event -> {
						TESTINGGAME.getPlayer().modifyInventory(InventoryAction.TAKE, theItem);
						playerInventoryGrid.getChildren().remove(item);
					});
					m.getItems().add(use);
					m.getItems().add(drop);
				}
				playerInventoryGrid.addRow(i, item);
			}

			Scene scene = new Scene(p);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void displayLootManager(Lootable l) {
		currentLayout = GUILayouts.LOOT_MANAGER;

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/LootManagerView.fxml"));
		loader.setController(this);

		try {
			p = loader.load();

			for (int i = 0; i < l.obtainLoot().length; i++) {
				otherInventoryGrid.getItems().add(l.obtainLoot()[i]);
			}
			if (otherInventoryGrid.getSelectionModel().isEmpty()) {
				otherInventoryGrid.getSelectionModel().select(0);
			}
			lootManagerButton.setOnMouseClicked(event -> {
				Item item = otherInventoryGrid.getSelectionModel().getSelectedItem();
				boolean successful = false;
				if (item != null) {
					successful = TESTINGGAME.getPlayer().modifyInventory(InventoryAction.GIVE, item);
				}
				if (!successful && !((AnchorPane) p).getChildren().contains(displayText)) {
					displayMessage("You can't take the item!");
				}
				if (successful) {
					otherInventoryGrid.getItems().remove(item);
					l.removeItem(item);
					Label label = new Label(item.toString() + ": " + item.getDescription());
					label.setPrefWidth(playerInventoryGrid.getPrefWidth());
					label.setOnMouseClicked(labelEvent -> {
						otherInventoryGrid.getItems().add(item);
						if(otherInventoryGrid.getSelectionModel().isEmpty()){
							otherInventoryGrid.getSelectionModel().select(0);
						}
						playerInventoryGrid.getChildren().remove(label);
						TESTINGGAME.getPlayer().modifyInventory(InventoryAction.TAKE, item);
					});
					GridPane.setHalignment(label, HPos.CENTER);
					playerInventoryGrid.addColumn(0, label);
				}
			});
			exitLootButton.setOnMouseClicked(event -> {
				displayGeneralView();
			});
			for (int i = 0; i < TESTINGGAME.getPlayer().getInventoryContents().length; i++) {
				Item theItem = TESTINGGAME.getPlayer().getInventoryContents()[i];
				Label item = new Label(theItem.toString() + ": " + theItem.getDescription());
				item.setPrefWidth(playerInventoryGrid.getPrefWidth());
				item.setOnMouseClicked(event -> {
					otherInventoryGrid.getItems().add(theItem);
					if(otherInventoryGrid.getSelectionModel().isEmpty()){
						otherInventoryGrid.getSelectionModel().select(0);
					}
					playerInventoryGrid.getChildren().remove(item);
					TESTINGGAME.getPlayer().modifyInventory(InventoryAction.TAKE, theItem);
				});
				GridPane.setHalignment(item, HPos.CENTER);
				playerInventoryGrid.addColumn(0, item);
			}

			Scene scene = new Scene(p);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (

		IOException e) {
			e.printStackTrace();
		}

	}

	public void playMoveAnimation(Direction direction) {
		int speed = 4;
		if (!isAnimating) {
			switch (direction) {
			case UP:
				isAnimating = true;
				new AnimationTimer() {
					int y = 0;

					@Override
					public void handle(long currentNanoTime) {
						y += speed;
						int temp = y % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0,
								y);
						if (temp == 0) {
							isAnimating = false;
							y = 0;
							GameEngine.updatePlayerPosition(Direction.UP);
							chanceBattle();
							this.stop();
						}

					}
				}.start();

				break;
			case RIGHT:
				isAnimating = true;
				new AnimationTimer() {
					int x = 0;

					@Override
					public void handle(long currentNanoTime) {
						x -= speed;
						int temp = x % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), x,
								0);
						if (temp == 0) {

							isAnimating = false;
							x = 0;
							GameEngine.updatePlayerPosition(Direction.RIGHT);
							chanceBattle();
							this.stop();
						}

					}
				}.start();
				break;

			case DOWN:
				isAnimating = true;
				new AnimationTimer() {
					int y = 0;

					@Override
					public void handle(long currentNanoTime) {
						y -= speed;
						int temp = y % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0,
								y);
						if (temp == 0) {

							isAnimating = false;
							y = 0;
							GameEngine.updatePlayerPosition(Direction.DOWN);
							chanceBattle();
							this.stop();
						}

					}
				}.start();
				//

				break;

			case LEFT:
				isAnimating = true;
				new AnimationTimer() {
					int x = 0;

					@Override
					public void handle(long currentNanoTime) {
						x += speed;
						int temp = x % 64;
						drawToGeneralCanvas(TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), x,
								0);
						if (temp == 0) {

							isAnimating = false;
							x = 0;
							GameEngine.updatePlayerPosition(Direction.LEFT);
							chanceBattle();
							this.stop();
						}

					}
				}.start();

				break;

			default:
				break;
			}
		}
	}

	public void playAttackAnimation(Image animation, Character character) {

		PixelReader reader = animation.getPixelReader();
		ImageView view = character.getBattleImageView();
		Image temp = character.getBattleImage();
		int widthOfFrame = (int) temp.getWidth();
		int heightOfFrame = (int) temp.getHeight();
		int numOfFrames = (int) (animation.getWidth() / widthOfFrame);
		int framesPlayedPerAnimationFrame = 4;
		while (character.isBattleAnimating()) {
			try {
				synchronized (attackAnimationLock) {
					attackAnimationLock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		character.setIsBattleAnimating(true);
		Platform.runLater(() -> {

			new AnimationTimer() {
				int x = -1;

				@Override
				public void handle(long now) {
					x += 1;
					WritableImage currentFrame = new WritableImage(reader, ((x / numOfFrames) * widthOfFrame), 0,
							widthOfFrame, heightOfFrame);
					view.setImage(currentFrame);
					if (x > numOfFrames * framesPlayedPerAnimationFrame) {

						view.setImage(character.getBattleImage());
						synchronized (attackAnimationLock) {
							character.setIsBattleAnimating(false);
							attackAnimationLock.notifyAll();
						}
						this.stop();
					}
				}

			}.start();
		});
	}

	public void playTakeDamageAnimation(Image animation, Character character) {

		PixelReader reader = animation.getPixelReader();
		ImageView view = character.getBattleImageView();
		Image temp = character.getBattleImage();
		int widthOfFrame = (int) temp.getWidth();
		int heightOfFrame = (int) temp.getHeight();
		int numOfFrames = (int) (animation.getWidth() / widthOfFrame);
		int framesPlayedPerAnimationFrame = 4;
		while (character.isBattleAnimating()) {
			try {
				synchronized (takeDamageAnimationLock) {
					takeDamageAnimationLock.wait();

				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		character.setIsBattleAnimating(true);
		Platform.runLater(() -> {

			new AnimationTimer() {
				int x = -1;

				@Override
				public void handle(long now) {
					x += 1;
					WritableImage currentFrame = new WritableImage(reader, ((x / numOfFrames) * widthOfFrame), 0,
							widthOfFrame, heightOfFrame);
					view.setImage(currentFrame);
					if (x > numOfFrames * framesPlayedPerAnimationFrame) {

						view.setImage(character.getBattleImage());
						synchronized (takeDamageAnimationLock) {

							character.setIsBattleAnimating(false);
							takeDamageAnimationLock.notifyAll();
						}
						this.stop();
					}
				}

			}.start();

		});
	}

	private void chanceBattle() {
		Battle b = GameEngine.chanceABattle();

		Coordinates playerCoord = TESTINGGAME.getPlayer().getCoordinates();
		if (b != null) {
			displayBattleView(b);

		} else if (TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1).getTiles()
				.get(playerCoord.getY()).get(playerCoord.getX()).getTileSheetNum() == 4
				&& TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1).bossIsDefeated()) {
			// TESTINGGAME.getPlayer().setFloorNum(TESTINGGAME.getPlayer().getFloorNum()
			// + 1);
			TESTINGGAME.getPlayer().moveUpFloor();
			TESTINGGAME.getPlayer().getCoordinates().setCoordinates(
					TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1).getPlayerStart().getX(),
					TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1).getPlayerStart().getY());
			drawToGeneralCanvas(TESTINGGAME.getFloors().get(TESTINGGAME.getPlayer().getFloorNum() - 1), 0, 0);
		}
	}

	@FXML
	private RadioButton boyRadioButton;
	@FXML
	private RadioButton girlRadioButton;
	@FXML
	private TextField nameTextField;

	public void displayCharacterCreation() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxmls/CharacterCreationView.fxml"));

		loader.setController(this);

		try {
			p = loader.load();
			Scene scene = new Scene(p);

			ToggleGroup tg = new ToggleGroup();
			boyRadioButton.setUserData(Genders.BOY);
			tg.getToggles().add(boyRadioButton);
			girlRadioButton.setUserData(Genders.GIRL);
			tg.getToggles().add(girlRadioButton);
			boyRadioButton.setSelected(true);

			submitButton.setOnMouseClicked(event -> {
				if (nameTextField.getText().equals("")
						&& !((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
					displayMessage("Please enter a name.");
				} else if (!nameTextField.getText().equals("")) {
					if (((AnchorPane) primaryStage.getScene().getRoot()).getChildren().contains(displayText)) {
						((AnchorPane) primaryStage.getScene().getRoot()).getChildren().remove(displayText);
					}
					TESTINGGAME = new Game(
							new Player(nameTextField.getText(), (Genders) tg.getSelectedToggle().getUserData(), 0));
					GameEngine.setGame(TESTINGGAME);
					displayGeneralView();
					displayMessage("Use the 'E' key to interact with notes, backpacks, and bosses.\n"
							+ "Also use 'E' to close these dialog boxes when you see them on the screen.\n"
							+ "Use 'W' to move up	'S' to move down	'A' to move left	and 'D' to move right.\n"
							+ "Pressing escape will bring you to the pause menu.\n"
							+ "Walk around to get into battles, fight the enemies, and level up to defeat the bosses on each floor.");
				}
			});

			String css = getClass().getResource("application.css").toExternalForm();
			scene.getStylesheets().add(css);
			primaryStage.setScene(scene);
			primaryStage.show();

		} catch (IOException e) {

		}
	}
}
