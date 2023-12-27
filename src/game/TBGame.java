package game;

import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;
import abstractcharacter.AbstractCharacter;
import abstractcharacter.AbstractCharacterComparator;
import characters.*;
import opponents.*;
import weapons.Weapon;

public class TBGame {
	// Class that inits and runs the game
	
	private Queue<Turn> turnOrderQueue;
	private List<Opponent> opponentList;
	private List<Human<Weapon>> humanList;
	
	private Menu menu; // menu for asking inputs from user(s)
	private Initializer initializer; // initializer for creating lists and queue
	private Move move; // move for handling printings and move step.
	public TBGame() {
		menu = new Menu();
		initializer = new Initializer();
		turnOrderQueue = new LinkedList<>();
		opponentList = new ArrayList<>();
		humanList = new ArrayList<>();
		move = new Move();
	}
	
	public void initGame() {
		// method that inits the game.
		initializer.createOpponents();
		menu.printOpponentList();
		initializer.createHumans(menu.askCharacterNumber());
		initializer.createTurnOrders();
	}
	
	public void runGame() {
		// method that runs the game.
		menu.startBattle();
		
		boolean gameRunning = true;
		while(gameRunning) {
			Turn currentTurn = turnOrderQueue.peek();
			if(currentTurn.isOpponent()) {
				// Perform opponent actions.
				Opponent opponent = getOpponentById(currentTurn.getOwnerId());
				// if is skip than skip the turn
				if(opponent.isSkip()) {
					// set action, not to skip all turns.
					opponent.setAction();
					move.showSkipMove(opponent);
					turnOrderQueue.poll();
					turnOrderQueue.add(currentTurn);
					continue;
				}
				// If it is not special, than set the action.
				if(!opponent.isSpecial())
					opponent.setAction();
				// Case every action of the opponent.
				switch(opponent.getAction()) {
					case OPPONENT_ATTACK:{
						// attack to random human
						Human human = getRandomHuman();
						performOpponentAttack(opponent, human);
						turnOrderQueue.poll();
						turnOrderQueue.add(currentTurn);
						break;
					}
					case OPPONENT_GUARD:{
						// starts guarding
						opponent.guard();
						move.showGuardMove(opponent);
						turnOrderQueue.poll();
						turnOrderQueue.add(currentTurn);
						break;
					}
					case OPPONENT_SPECIAL:{
						// Case special abilities of the Opponent respect to its type.
						switch(opponent.getType()) {
							case GOBLIN:{
								// if special is used before
								if(((Goblin)opponent).getSpecialCounter()>0) {
									turnOrderQueue.poll();
									turnOrderQueue.add(currentTurn);
								}
								Human human = getRandomHuman();
								performOpponentSpecialAttack(opponent, currentTurn.getAttackModifier(), human);
								break;
							}
							case ORC:{
								turnOrderQueue.poll();
								turnOrderQueue.add(currentTurn);
								// if special has not been used before
								Human human = getRandomHuman();
								performOpponentSpecialAttack(opponent, currentTurn.getAttackModifier(), human);

								break;
							}
							case SLIME:{
								// attack normally and absorb the dealing damage.
								Human human = getRandomHuman();
								performOpponentSpecialAttack(opponent, currentTurn.getAttackModifier(), human);
								turnOrderQueue.poll();
								turnOrderQueue.add(currentTurn);
								break;
							}
							case WOLF:{
								turnOrderQueue.poll();
								turnOrderQueue.add(currentTurn);
								// Create a new wolf if it is possible.
								Wolf wolf = ((Wolf) opponent).callFriend(findMaxIdInOpponent()+1);
								if(wolf != null) {
									opponentList.add(wolf);
									addToHeadOfQueue(new Turn(wolf.getId(), 0));
									move.showSpecialPassiveMove(opponent);
									menu.showTurnOrder();
								}else // wolf could not be created
									move.showSpecialPassiveMoveFailed(opponent);
								break;
							}
						}
					}
				default:
					break;
				}
				
			}else {
				// Perform human actions.
				Human<Weapon> human = getHumanByName(currentTurn.getOwnerName());
				// if is skip.
				if(human.isSkip()) {
					human.setSkip(false);
					move.showSkipMove(human);
					turnOrderQueue.poll();
					turnOrderQueue.add(currentTurn);
					continue;
				}
				HumanAction action = null;
				try {
						action = menu.askAHumanAction(human);
						// if it is special and (not guard and run action)
						if(human.isSpecial() && (action != HumanAction.HUMAN_RUN)) {
							if(action == HumanAction.HUMAN_GUARD) {
								switch(human.getJob()) {
									case HUNTER:
										// human guarding
										human.guard();
										move.showHumanGuardMove(human);
										// after the move the special might be done.
										if(!human.isSpecial()) {
											turnOrderQueue.poll();
											turnOrderQueue.add(currentTurn);
										}
										menu.showTurnOrder();
										break;	
									default:
										// human guarding for other jobs.
										human.guard();
										move.showHumanGuardMove(human);
										turnOrderQueue.poll();
										turnOrderQueue.add(currentTurn);
										menu.showTurnOrder();
										break;	
								}
							}else {
								Opponent opponent = menu.selectAnOpponent();
								// handle specials respect to character job.
								switch(human.getJob()) {
									case HUNTER:{
										performHumanSpecialAttack(human, opponent, action, currentTurn.getAttackModifier());
										// dominates 2 turns back to back.
										if(((Hunter)human).getSpecialTurnCounter() > 2) {
											turnOrderQueue.poll();
											turnOrderQueue.add(currentTurn);
										}
										menu.showTurnOrder();
										break;
									}
									case KNIGHT:{
										// does special attack in its next turn.
										performHumanSpecialAttack(human, opponent, action, currentTurn.getAttackModifier());
										turnOrderQueue.poll();
										turnOrderQueue.add(currentTurn);
										menu.showTurnOrder();
										break;
									}
								default:
									break;
								}
							}
							
						}else if(action != null) { // if it is not special yet.
							// Case every action of the human.
							switch (action) {
								case HUMAN_PUNCH:{
									// Punch to selected opponent.
									Opponent opponent = menu.selectAnOpponent();
									performHumanPunch(human, opponent);
									turnOrderQueue.poll();
									turnOrderQueue.add(currentTurn);
									menu.showTurnOrder();
									break;
								}
								case HUMAN_ATTACK_WITH_WEAPON_1: 
									// Attack with first weapon skill to selected opponent.
								case HUMAN_ATTACK_WITH_WEAPON_2:{
									// Attack with second weapon skill to selected opponent.
									Opponent opponent = menu.selectAnOpponent();
									performHumanAttack(human, opponent, action);
									turnOrderQueue.poll();
									turnOrderQueue.add(currentTurn);
									menu.showTurnOrder();
									break;
								}
								case HUMAN_GUARD:{
									// human guarding
									human.guard();
									move.showHumanGuardMove(human);
									turnOrderQueue.poll();
									turnOrderQueue.add(currentTurn);
									menu.showTurnOrder();
									break;	
								}
								case HUMAN_RUN:{
									// human escapes from the matchup.
									human.run();
									break;
								}
								
								// if it is special action for the first time
								case HUMAN_SPECIAL_ACTION:{
									switch(human.getJob()) {
									
									case HUNTER:
									case SQUIRE:{
										// attacks in first special.
										HumanAction newAction = menu.askAHumanSpecialAction(human);
										Opponent opponent = menu.selectAnOpponent();
										performHumanSpecialAttack(human, opponent, newAction, currentTurn.getAttackModifier());
										turnOrderQueue.poll();
										turnOrderQueue.add(currentTurn);
										menu.showTurnOrder();
										break;
									}
									case KNIGHT:{
										// knight passes its first turn with using special.
										((Knight)human).activateSpecial(action);
										move.showHumanSpecialPassiveMove(human);
										turnOrderQueue.poll();
										turnOrderQueue.add(currentTurn);
										menu.showTurnOrder();
										break;
									}
									case VILLAGER:{
										// villager has no special action.
										// throws an exception.
										human.specialAction(action, currentTurn.getAttackModifier(), new Orc(0));
									
									}
								}
							}
						}
							
					}
				}catch(InsufficientStaminaException i) {
					System.out.println("There is no enough stamina for this action! Try again.");
					continue;
				}catch(SpecialAlreadyUsedException s) {
					System.out.println("This special has been already used! Try again.");
					continue;
				}catch (CharacterRunsException c) {
					System.out.println("Your character(s) started running away. The battle ends!");
					break;
				}catch (UnsupportedOperationException u) {
					System.out.println("You tried to use an unsupported action! Try again.");
					continue;
				}
				
			}
			// increment the move step.
			move.incrementMoveCounter();
			gameRunning = !isGameOver();
		}
		System.out.println("Thanks for playing");
		// close the menu.
		menu.closeMenu();
	}
	
	private boolean isGameOver() {
		// see if opponents or humans are dead.
		boolean result = opponentList.size() == 0 || humanList.size() == 0;
		if(result) {
			if(opponentList.size() == 0)
				System.out.println("\nYou have won the game!");
			else if(humanList.size() == 0)
				System.out.println("\nYou have lost the game!");
		}
		return result;
	}
	
	private boolean isCharacterNameUnique(String name) {
		// method that checks whether given name is unique.
		for(Human<Weapon> human: humanList) {
			if(name.equals(human.getName()))
				return false;
		}
		return true;
	}
	
	private Opponent getOpponentById(int id) {
		// finds the opponent in the given id
		for(Opponent op: opponentList) {
			if(id == op.getId())
				return op;
		}
		return null;
	}
	
	private void performOpponentAttack(Opponent opponent, Human human) {
		// method that performs opponent attack.
		int dealedDamage = opponent.attack(human);
		move.showAttackMove(opponent, human, dealedDamage);
		removeHuman(human);
		
	}
	
	private void performOpponentSpecialAttack(Opponent opponent, double attackModifier, Human human) {
		// method that performs special opponent attack.
		int dealedDamage = opponent.specialAttack(attackModifier, human);
		move.showSpecialAttackMove(opponent, human, dealedDamage);
		removeHuman(human);
		
	}
	
	private int findMaxIdInOpponent() {
		// method that finds max id in opponent list.
		int id = Integer.MIN_VALUE;
		for(Opponent op: opponentList) {
			if(op.getId() > id)
				id = op.getId();
		}
		return id;
	}
	
	private void performHumanPunch(Human<Weapon> human, Opponent opponent) throws InsufficientStaminaException {
		// method that punchs an opponent.
		int dealedDamage = human.punch(opponent);
		move.showHumanPunchMove(human, opponent, dealedDamage);
		removeOpponent(opponent);
	}
	
	private void performHumanAttack(Human<Weapon> human, Opponent opponent, HumanAction action) throws InsufficientStaminaException {
		// method that attacks with weapon to an opponent.
		int dealedDamage = human.attackWithWeapon(human.getWeapon(), action, opponent);
		move.showHumanAttackMove(human, opponent, dealedDamage);
		removeOpponent(opponent);
	}
	
	private void performHumanSpecialAttack(Human<Weapon> human, Opponent opponent, HumanAction action, double attackModifier) throws SpecialAlreadyUsedException, InsufficientStaminaException {
		// method that uses special attack on an opponent.
		int dealedDamage = human.specialAction(action, attackModifier, opponent);
		move.showHumanSpecialAttackMove(human, opponent, dealedDamage);
		removeOpponent(opponent);
	}

	private Human<Weapon> getRandomHuman() {
		// method that gets randomly a human from human list.
		int index = (int)Math.floor(Math.random()*humanList.size());
		return humanList.get(index);
	}
	
	private Human<Weapon> getHumanByName(String name) {
		// finds the human in the given name
		for(Human human: humanList) {
			if(name.equals(human.getName()))
				return human;
		}
		return null;
	}
	
	private void removeOpponent(Opponent opponent) {
		// removes opponent if it is not alive
		if(!opponent.isAlive()) {
			opponentList.remove(opponent);
			for(Turn turn: turnOrderQueue) {
				if(opponent.getId() == turn.getOwnerId()) {
					turnOrderQueue.remove(turn);
					break;
				}
			}
			System.out.println(opponent.toName()+" has been killed.");
		}
	}
	
	private void removeHuman(Human human) {
		// removes human if it is not alive
		if(!human.isAlive()) {
			humanList.remove(human);
			for(Turn turn: turnOrderQueue) {
				if(human.getName().equals(turn.getOwnerName())) {
					turnOrderQueue.remove(turn);
					break;
				}
			}
			System.out.println(human.toName()+" has been killed.");
		}
	}
	
	private void addToHeadOfQueue(Turn turn) {
		// method that adds turn to the head of the queue.
		Queue<Turn> tempQueue = new LinkedList<>();
		tempQueue.add(turn);
		tempQueue.addAll(turnOrderQueue);
		turnOrderQueue = tempQueue;
	}
	
	private class Menu {
		// inner menu that implements scanner issues of TBGame.
		private Scanner keyboard;
		private String humanActions[] = new String[] {
				"[1] Punch",
				"[2] Attack with weapon",
				"[3] Guard",
				"[4] Special Action",
				"[5] Run",
		};
		
		private String humanAttackActions[] = new String[] {
				"[1] Punch",
				"[2] Attack with weapon"	
		};
		public Menu() {
			// init keyboard
			keyboard = new Scanner(System.in);
			System.out.println("Welcome to TBGame!\n");
		}
		
		private void showOptions(String[] array) {
			// shows the given options
			for(String str: array)
				System.out.println(str);
			System.out.print("Select an option: ");
		}
		
		private void showWeaponOptions(String[] array) {
			// shows the given weapon's options.
			System.out.print("Please select an weapon attack type (");
			for(int i = 0; i < array.length; i++)
				System.out.print("["+(i+1)+"] "+array[i]);
			System.out.print("): ");
		}
		public void startBattle() {
			System.out.println("\nThe battle starts!\n");
			showTurnOrder();
		}
		
		public void showTurnOrder() {
			// method that shows every turn in the queue.
			Turn[] turnOrders = turnOrderQueue.toArray(new Turn[1]);
			System.out.print("\n***Turn Order: ");
			for(int i = 0; i < turnOrders.length; i++) {
				if(i < turnOrders.length-1)
					System.out.print(turnOrders[i]+", ");
				else
					System.out.print(turnOrders[i]+"***");
			}
			System.out.println();
		}
		
		public void closeMenu() {
			keyboard.close();
		}
		
		public HumanAction askAHumanSpecialAction(Human<Weapon> human) {
			// Method that asks what action would be used with the special action.
			boolean isLoop = true;
			while(isLoop) {
				try {
					showOptions(humanAttackActions);
					int actionNumber = keyboard.nextInt();
					System.out.println();
					// see selection is valid.
					while(!isSelectionValid(actionNumber, 1, humanAttackActions.length)) {
						System.out.println("Selection must be between 1 and "+humanAttackActions.length+".");
						showOptions(humanActions);
						actionNumber = keyboard.nextInt();
						System.out.println();
					}
					// if it is a weapon action.
					if(actionNumber == 2) {
						// show weapon names.
						String[] weaponOptions = human.getWeapon().getAttackNames();
						showWeaponOptions(weaponOptions);
						int weaponNumber = keyboard.nextInt();
						System.out.println();
						while(!isSelectionValid(weaponNumber, 1, weaponOptions.length)) {
							System.out.println("Selection must be between 1 and "+weaponOptions.length);
							showWeaponOptions(weaponOptions);
							weaponNumber = keyboard.nextInt();
							System.out.println();
						}
						// Return weapon attack type.
						if(weaponNumber == 1)
							return HumanAction.HUMAN_ATTACK_WITH_WEAPON_1;
						else
							return HumanAction.HUMAN_ATTACK_WITH_WEAPON_2;
					}
					// Return other actions.
					else if(actionNumber == 1)
						return HumanAction.HUMAN_PUNCH;
					isLoop = false;
					
				} catch(InputMismatchException e) {
					// if any selection is not an integer.
					System.out.println("Input is not an integer value! Try again.");
					keyboard.nextLine();
				}
			}
			return null;
		}
		
		public HumanAction askAHumanAction(Human<Weapon> human) {
			// Method that asks what action would be made by the human.
			boolean isLoop = true;
			move.showMoveOrder(human);
			while(isLoop) {
				try {
					showOptions(humanActions);
					int actionNumber = keyboard.nextInt();
					System.out.println();
					// see selection is valid.
					while(!isSelectionValid(actionNumber, 1, 5)) {
						System.out.println("Selection must be between 1 and 5.");
						showOptions(humanActions);
						actionNumber = keyboard.nextInt();
						System.out.println();
					}
					// if it is a weapon action.
					if(actionNumber == 2) {
						// show weapon names.
						String[] weaponOptions = human.getWeapon().getAttackNames();
						showWeaponOptions(weaponOptions);
						int weaponNumber = keyboard.nextInt();
						System.out.println();
						while(!isSelectionValid(weaponNumber, 1, weaponOptions.length)) {
							System.out.println("Selection must be between 1 and "+weaponOptions.length);
							showWeaponOptions(weaponOptions);
							weaponNumber = keyboard.nextInt();
							System.out.println();
						}
						// Return weapon attack type.
						if(weaponNumber == 1)
							return HumanAction.HUMAN_ATTACK_WITH_WEAPON_1;
						else
							return HumanAction.HUMAN_ATTACK_WITH_WEAPON_2;
					}
					// Return other actions.
					else if(actionNumber == 1)
						return HumanAction.HUMAN_PUNCH;
					else if(actionNumber == 3)
						return HumanAction.HUMAN_GUARD;
					else if(actionNumber == 4)
						return HumanAction.HUMAN_SPECIAL_ACTION;
					else if(actionNumber == 5)
						return HumanAction.HUMAN_RUN;
					isLoop = false;
					
				} catch(InputMismatchException e) {
					// if any selection is not an integer.
					System.out.println("Input is not an integer value! Try again.");
					keyboard.nextLine();
				}
			}
			return null;
		}
		
		public void printOpponentList() {
			// method that prints opponent list.
			System.out.println("These opponents appeared in front of you:");
			for(Opponent op: opponentList)
				System.out.println(op.toFullCharacterInfo());
			System.out.println();
			
		}
		
		public String askCharacterName(int number) throws NotAUniqueNameException {
			// method that asks user for an unique character name.
			String holder = "";
			// show number in an appropriate way.
			if(number == 1)
				holder = number+"st";
			else if(number == 2)
				holder = number+"nd";
			else
				holder = number+"rd";
			// ask for name
			System.out.print("Enter name for your "+holder+" character: ");
			String name = keyboard.next();
			keyboard.nextLine();
			// if it is not unique throw an exception.
			if(!isCharacterNameUnique(name))
				throw new NotAUniqueNameException(name);
			System.out.println("The stats of your "+holder+" character: ");
			return name;
		}
		
		public int askCharacterNumber() {
			// method that asks user how many characters should be created
			boolean isLoop = false;
			int number = 0;
			System.out.print("Please enter the number of characters to create: ");
			
			while(!isLoop) { // while character number is not entered appropriately.
				try {
					number= keyboard.nextInt();
					if(number > 3 ) {
						System.out.println("Character number set to 3. There can be three characters at most");
						number = 3;
					}
					else if(number < 1) {
						System.out.println("Character number set to 1. There must be one character at least.");
						number = 1;
					}
					isLoop = true;
				}catch(InputMismatchException e) {
					System.out.println("This is not an integer value!");
					keyboard.nextLine();
					System.out.print("Please reenter the number of characters to create: ");
				}
			}
			return number;
		}
		
		public Opponent selectAnOpponent() {
			// method that selects an opponent with user input.
			boolean isLoop = true;
			Opponent op = null;
			while(isLoop) {
				try {
					System.out.print("Please enter an opponent id: ");
					int id = keyboard.nextInt();
					op = getOpponentById(id);
					if(op == null)
						System.out.println("Opponent with id "+id+" is not exist.");
					else
						isLoop = false;
				}catch(InputMismatchException e) {
					System.out.println("Input is not an integer value! Try again.");
				}
			}
			return op;
			
		}
		
		private boolean isSelectionValid(int selection, int min, int max) {
			return !(selection< min || selection > max);
		}
		
	}
	
	private class Initializer {
		// helper class that inits humans, opponents and queue.
		private void createTurnOrders() {
			// method that creates turn order and adds to queue.
			List<AbstractCharacter> allCharacters = new ArrayList<>();
			allCharacters.addAll(opponentList);
			allCharacters.addAll(humanList);
			allCharacters.sort(new AbstractCharacterComparator<AbstractCharacter>());
			for(AbstractCharacter ac: allCharacters) {
				// if it is a human
				if(ac instanceof Human) {
					Human human = (Human)ac;
					Turn turn = null;
					switch(human.getJob()) {
						// both has same attack modifier.
						case SQUIRE:
						case HUNTER:{
							turn = new Turn(human.getName(), 0.5);
							break;
						}
						case KNIGHT: {
							turn = new Turn(human.getName(), 3);
							break;
						}
						case VILLAGER:{
							turn = new Turn(human.getName(), 1);
							break;
						}
					}
					if(turn != null)
						turnOrderQueue.add(turn);
				}
				else if(ac instanceof Opponent) {
					// if it is an opponent.
					Opponent opponent = (Opponent)ac;
					Turn turn = null;
					switch(opponent.getType()) {
						// create a turn respect to their attack modifier.
						case GOBLIN:
							turn = new Turn(opponent.getId(), 0.7);
							break;
						case SLIME:
							turn = new Turn(opponent.getId(), 1);
							break;
						case ORC:
							turn = new Turn(opponent.getId(), 2);
							break;
						case WOLF: // wolf has 0 attack modifier, its special is not doing any damage.
							turn = new Turn(opponent.getId(), 0);
							break;
					}
					if(turn != null)
						turnOrderQueue.add(turn);
				}
				
			}
		}
		
		private void createOpponents() {
			// method that creates opponents and adds to the list.
			OpponentType[] opponents = OpponentType.values();
			int opponentNumber = (int)Math.floor(Math.random()*4)+1;
			for(int i = 0; i < opponentNumber; i++) {
				int index = (int)Math.floor(Math.random()*opponents.length);
				Opponent opponent = createAnOpponent(opponents[index], (i+1));
				if(opponent != null) 
					opponentList.add(opponent);
			}
		}
		
		private void createHumans(int numberOfHumans){
			// method that creates humans and adds to the list.
			CharacterJob[] characters = CharacterJob.values();
			for(int i = 0; i < numberOfHumans; i++) {
				boolean isLoop = false;
				String name = "";
				while(!isLoop) {
					// asks character name input.
					try {
						name = menu.askCharacterName(i+1);
						isLoop = true;
					}catch(NotAUniqueNameException e) {
						System.out.println(e.getMessage());
					}
				
				}
				// choose a random character job.
				int index = (int)Math.floor(Math.random()*characters.length);
				// create a human
				Human<Weapon> human = createAHuman(characters[index], name);
				if(human != null) {
					humanList.add(human);
					System.out.println(human.toFullCharacterInfo());
				}
				
			}
			
		}
		
		private Opponent createAnOpponent(OpponentType opponentType, int id) {
			// method that creates an opponent with given values.
			Opponent opp;
			switch(opponentType) {
				case WOLF:
					opp = new Wolf(id);
					break;
				case SLIME:
					opp = new Slime(id);
					break;
				case ORC:
					opp = new Orc(id);
					break;
				case GOBLIN:
					opp = new Goblin(id);
					break;
				default:
					opp = null;
			}
			
			return opp;
		}
		
		private Human<Weapon> createAHuman(CharacterJob job, String name) {
			// method that creates a human with given values.
			Human<Weapon> human;
			switch(job) {
				case KNIGHT:
					human = new Knight<Weapon>(name);
					break;
				case SQUIRE:
					human = new Squire<Weapon>(name);
					break;
				case VILLAGER:
					human = new Villager<Weapon>(name);
					break;
				case HUNTER:
					human = new Hunter<Weapon>(name);
					break;
				default:
					human = null;
			}
			return human;
		}
	}
}