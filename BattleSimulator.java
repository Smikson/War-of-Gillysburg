package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Attack.DmgType;

import java.io.*;

// Singleton class for running a battle -- Also contains helpful info for some class Abilities
public class BattleSimulator {
	// The instance of the simulator
	private static BattleSimulator instance;
	
	// Holds the info about combatants and rounds
	private int round;
	private LinkedList<Character> allyList;
	private LinkedList<Enemy> enemyList;
	private LinkedList<Character> combatantList;
	
	// Helpful for prompting controller for commands
	private Scanner prompter;
	
	// Constructor
	private BattleSimulator() {
		this.round = 1;
		this.allyList = new LinkedList<Character>();
		this.enemyList = new LinkedList<Enemy>();
		this.combatantList = new LinkedList<Character>();
		this.prompter = new Scanner(System.in);
	}
	
	// synchronized method to control simultaneous access
	synchronized public static BattleSimulator getInstance() {
		// Lazy instantiation
		if (instance == null) {
			instance = new BattleSimulator();
		}
		return instance;
	}
	
	// Get and Print methods:
	public int getNumCombatants() {
		return this.combatantList.size();
	}
	public int getRound() {
		return this.round;
	}
	public void printAllies() {
		for (Character ally : this.allyList) {
			System.out.println(ally.getName());
		}
	}
	public LinkedList<Character> getAllies() {
		LinkedList<Character> ret = new LinkedList<>();
		ret.addAll(this.allyList);
		return ret;
	}
	public void printEnemies() {
		for (Character enemy : this.enemyList) {
			System.out.println(enemy.getName());
		}
	}
	public LinkedList<Enemy> getEnemies() {
		LinkedList<Enemy> ret = new LinkedList<>();
		ret.addAll(this.enemyList);
		return ret;
	}
	public void printCombatants() {
		for (Character combatant : this.combatantList) {
			System.out.println(combatant.getName());
		}
	}
	public LinkedList<Character> getCombatants() {
		LinkedList<Character> ret = new LinkedList<>();
		ret.addAll(this.combatantList);
		return ret;
	}
	public Scanner getPrompter() {
		return this.prompter;
	}
	
	// Add methods
	// Adds an ally to the ally list and the combatants list
	public void addAlly(Character ally) {
		allyList.add(ally);
		combatantList.add(ally);
	}
	// Adds an enemy to the enemy list and the combatants list
	public void addEnemy(Enemy enemy) {
		enemyList.add(enemy);
		combatantList.add(enemy);
	}
	
	// Test methods to see if either "team" is dead
	public boolean allAlliesDead() {
		// If any ally is alive return false
		for (Character ally : this.getAllies()) {
			if (!ally.isDead()) {
				return false;
			}
		}
		// Otherwise return true
		return true;
	}
	public boolean allEnemiesDead() {
		// If any enemy is alive return false
		for (Enemy e : this.getEnemies()) {
			if (!e.isDead()) {
				return false;
			}
		}
		// Otherwise return true
		return true;
	}
	
	// Prompt methods
	// Returns true if yes, false if no
	public boolean askYorN() {
		// Prompt for Y or N:
		System.out.print("Y or N: ");
		
		// Get the response (uppercase it so only have to match one thing)
		String response = this.getPrompter().nextLine().toUpperCase();
		while (true) {
			// If Yes, return true
			if (response.equals("Y")) {
				return true;
			}
			// If No, return false
			else if (response.equals("N")) {
				return false;
			}
			// If anything else, ask again
			else {
				System.out.println("Please respond with Y or N.");
				response = this.getPrompter().nextLine().toUpperCase();
			}
		}
	}
	
	// Returns a chosen number of a list of Strings (nth string chosen -- NOT INDEX)
	public int promptSelect(String prompt, boolean noneOption, String noneText, LinkedList<String> choices) {
		int choice = 0;
		// Display options
		System.out.println(prompt);
		if (noneOption) {
			System.out.println("0. " + noneText);
		}
		for (int i = 0; i < choices.size(); i++) {
			System.out.println("" + (i+1) + ". " + choices.get(i));
		}
		
		// Prompt user for their choice from the options
		System.out.print("Choice? ");
		while (true) {
			// Get response, must be an integer
			if (this.getPrompter().hasNextInt()) {
				choice = this.getPrompter().nextInt();
				this.getPrompter().nextLine();
				
				// If their response is in the bounds, return the numbered choice
				if ((choice <= choices.size() && choice > 0) || (choice == 0 && noneOption)) {
					return choice;
				}
				// If their response is not in the bounds, notify and prompt again
				else {
					System.out.println("Invalid response. Please enter a valid response.");
					System.out.print("Choice? ");
				}
			}
			// If it is not an integer, notify and prompt again
			else {
				String response = this.getPrompter().nextLine();
				System.out.println("\""+response+"\" is not a valid response. Please enter a valid response.");
				System.out.print("Choice? ");
			}
		}
	}
	public int promptSelect(String prompt, boolean noneOption, LinkedList<String> choices) {
		return this.promptSelect(prompt, noneOption, "None (Cancel)", choices);
	}
	public int promptSelect(String prompt, LinkedList<String> choices) {
		// Default has a none Option if unspecified
		return this.promptSelect(prompt, true, choices);
	}
	public int promptSelect(LinkedList<String> choices) {
		// Default has "Choose one of these options:" as prompt
		return this.promptSelect("Choose one of these options:", choices);
	}
	
	// Return a chosen Character in combatants (or EMPTY if 0 chosen)
	public Character targetSingle() {
		// Create a list of strings for each combatant's name.
		LinkedList<String> nameList = new LinkedList<>();
		for (Character combatant : this.getCombatants()) {
			String deadInd = "";
			if (combatant.isDead()) {
				deadInd = " - Dead";
			}
			nameList.add(combatant.getName() + deadInd);
		}
		
		// Prompt the user for a choice from those names
		int choice = this.promptSelect(nameList);
		
		// Return the result
		if (choice == 0) {
			return Character.EMPTY;
		}
		else {
			return this.getCombatants().get(choice-1);
		}
	}
	public Enemy targetSingleEnemy() {
		// Create a list of strings for each enemy's name.
		LinkedList<String> nameList = new LinkedList<>();
		for (Enemy enemy : this.getEnemies()) {
			String deadInd = "";
			if (enemy.isDead()) {
				deadInd = " - Dead";
			}
			nameList.add(enemy.getName() + deadInd);
		}
		
		// Prompt the user for a choice from those names
		int choice = this.promptSelect(nameList);
		
		// Return the result
		if (choice == 0) {
			return Enemy.EMPTY;
		}
		else {
			return this.getEnemies().get(choice-1);
		}
	}
	
	// Return a list of chosen Characters in combatants (by convention, an empty list implies the user wants to "cancel" and select a different command)
	public LinkedList<Integer> promptSelectMultiple(String prompt, LinkedList<String> choices) {
		// Declare starting variables
		int choice = 0;
		LinkedList<Integer> ret = new LinkedList<>();
		
		// Display options
		System.out.println("[Use the negative of the chosen number to remove it from the list]");
		System.out.println(prompt);
		System.out.println("0. Done (Cancels if list is completely empty)");
		for (int i = 0; i < choices.size(); i++) {
			System.out.println("" + (i+1) + ". " + choices.get(i));
		}
		System.out.println();
		System.out.print("Choice? ");
		
		// Loop until 0 is entered
		while (true) {
			// Get input, make sure it is an integer
			if (this.getPrompter().hasNextInt()) {
				// Get their choice and move to the next line
				choice = this.getPrompter().nextInt();
				this.getPrompter().nextLine();
				
				// If they chose 0, return
				if (choice == 0) {
					return ret;
				}
				// If they chose a positive available option
				else if (choice <= choices.size() && choice > 0) {
					// Check if already in list
					if (ret.contains(choice)) {
						System.out.println("List already contains " + choice + ".");
					}
					// Add chosen number to the list
					else {
						// Add the selected number to the return list
						ret.add(choice);
						
						// Display the current list for checking
						System.out.print("Current list: " + ret.get(0));
						for (int i = 1; i < ret.size(); i++) {
							System.out.print(", " + ret.get(i));
						}
						System.out.println();
					}
					// Continue prompt
					System.out.print("Choice? ");
				}
				// If they chose a negative available option
				else if ((-choice) <= choices.size() && (-choice) > 0) {
					// If their choice is in the list
					if (ret.contains(-choice)) {
						// Remove the choice
						ret.remove(new Integer(-choice));
						
						// Display the remaining list
						System.out.print("Current list: ");
						if (!ret.isEmpty()) {
							System.out.print(ret.get(0));
							for (int i = 1; i < ret.size(); i++) {
								System.out.print(", " + ret.get(i));
							}
						}
						System.out.println();
					}
					// If their choice is not in the list, notify
					else {
						System.out.println("Choice already not in list.");
					}
					// Continue prompt
					System.out.print("Choice? ");
				}
				// If the user entered an invalid number, notify
				else {
					System.out.println("Invalid response. Please enter a valid response.");
					System.out.print("Choice? ");
				}
			}
			// If the user entered a non-integer, notify
			else {
				String response = this.getPrompter().nextLine();
				System.out.println("\""+response+"\" is not a valid response. Please enter a valid response.");
				System.out.print("Choice? ");
			}
		}
	}
	public LinkedList<Integer> promptSelectMultiple(LinkedList<String> choices) {
		return this.promptSelectMultiple("Choose from the following:", choices);
	}
	
	// Return a list of chosen Characters in combatants (by convention, an empty list implies the user wants to "cancel" and select a different command)
	public LinkedList<Character> targetMultiple() {
		// Declare starting variables
		int choice = 0;
		LinkedList<Character> ret = new LinkedList<>();
		
		// Create a list to hold each Character they can select (includes EMPTY for a "Null" selection)
		LinkedList<Character> options = new LinkedList<>();
		options.addAll(this.getCombatants());
		options.add(Character.EMPTY);
		
		// Display options
		System.out.println("[Use the negative of the chosen number to remove it from the list. Insert \"Null\" to select no targets]");
		System.out.println("Select Targets:");
		System.out.println("0. Done (Cancels if list is completely empty)");
		for (int i = 0; i < options.size(); i++) {
			// Print each Character option with an indicator if they are already dead
			String deadInd = "";
			if (!options.get(i).equals(Character.EMPTY) && options.get(i).isDead()) {
				deadInd = " - Dead";
			}
			System.out.println("" + (i+1) + ". " + options.get(i).getName() + deadInd);
		}
		System.out.println();
		System.out.print("Choice? ");
		
		// Loop until 0 is entered
		while (true) {
			// Get input, make sure it is an integer
			if (this.getPrompter().hasNextInt()) {
				// Get their choice and move to the next line
				choice = this.getPrompter().nextInt();
				this.getPrompter().nextLine();
				
				// If they chose 0, return
				if (choice == 0) {
					return ret;
				}
				// If they chose a positive available option
				else if (choice <= options.size() && choice > 0) {
					// Check if already in list
					if (ret.contains(options.get(choice-1))) {
						System.out.println("List already contains " + this.getCombatants().get(choice-1).getName());
					}
					// Add chosen Character to the list
					else {
						// If EMPTY is chosen, or if EMPTY is already present, first remove everything else from the list
						if (options.get(choice - 1).equals(Character.EMPTY) || ret.contains(Character.EMPTY)) {
							ret.clear();
						}
						
						// Add the Character to the return list
						ret.add(options.get(choice-1));
						
						// Display the current list for checking
						System.out.print("Current list: " + ret.get(0).getName());
						for (int i = 1; i < ret.size(); i++) {
							System.out.print(", " + ret.get(i).getName());
						}
						System.out.println();
					}
					// Continue prompt
					System.out.print("Choice? ");
				}
				// If they chose a negative available option
				else if ((-choice) <= options.size() && (-choice) > 0) {
					// If their choice is in the list
					if (ret.contains(options.get((-choice) - 1))) {
						// Remove the choice
						ret.remove(options.get((-choice) - 1));
						
						// Display the remaining list
						System.out.print("Current list: ");
						if (!ret.isEmpty()) {
							System.out.print(ret.get(0).getName());
							for (int i = 1; i < ret.size(); i++) {
								System.out.print(", " + ret.get(i).getName());
							}
						}
						System.out.println();
					}
					// If their choice is not in the list, notify
					else {
						System.out.println("Choice already not in list.");
					}
					// Continue prompt
					System.out.print("Choice? ");
				}
				// If the user entered an invalid number, notify
				else {
					System.out.println("Invalid response. Please enter a valid response.");
					System.out.print("Choice? ");
				}
			}
			// If the user entered a non-integer, notify
			else {
				String response = this.getPrompter().nextLine();
				System.out.println("\""+response+"\" is not a valid response. Please enter a valid response.");
				System.out.print("Choice? ");
			}
		}
	}
	
	// Other prompt methods to get numeric values
	// Returns an integer value as prompted
	public int promptInt(int minValue, int maxValue) {
		int choice;
		while (true) {
			if (this.getPrompter().hasNextInt()) {
				choice = this.getPrompter().nextInt();
				this.getPrompter().nextLine();
				if (choice < minValue) {
					System.out.print("Please enter an integer above " + minValue + ": ");
				}
				else if (choice > maxValue) {
					System.out.print("Please enter an integer below " + maxValue + ": ");
				}
				else {
					break;
				}
			}
			else {
				this.getPrompter().nextLine();
				System.out.print("Please enter an integer as a response: ");
			}
		}
		return choice;
	}
	public int promptInt() {
		return this.promptInt(0, Integer.MAX_VALUE);
	}
	
	// Returns a double value as prompted
	public double promptDouble(double minValue, double maxValue) {
		double choice;
		while (true) {
			if (this.getPrompter().hasNextDouble()) {
				choice = this.getPrompter().nextDouble();
				this.getPrompter().nextLine();
				if (choice < minValue) {
					System.out.print("Please enter a value above " + minValue + ": ");
				}
				else if (choice > maxValue) {
					System.out.print("Please enter a value below " + maxValue + ": ");
				}
				else {
					break;
				}
			}
			else {
				this.getPrompter().nextLine();
				System.out.print("Please enter a value as a response: ");
			}
		}
		return choice;
	}
	public double promptDouble() {
		return this.promptDouble(0, Double.MAX_VALUE);
	}
	
	
	// Methods to set up battles
	private void setOrder() {
		// Create a temporary list to alter the order of the combatants
		List<Character> temp = new LinkedList<>();
		for (int x = 0; x<this.combatantList.size(); x++) {
			temp.add(this.combatantList.get(x));
		}
		// Initialize other variables (the final ordered list, the random dice generator, and the sum of attack speeds)
		LinkedList<Character> ordered = new LinkedList<>();
		Dice d;
		int sum = 0;
		
		//Find's total Attack Speed
		for (int x = 0; x<temp.size(); x++) {
			sum += temp.get(x).getAttackSpeed();
		}
		
		// Loop until everything is subtracted from the sum
		while(sum>0) {
			// Create a random number between 1 and the current sum
			d = new Dice(sum);
			int choice = d.roll();
			
			// Select a character by comparing the random chosen number to each individual's attack speed
			int position = 0; // Moves the comparison past each individual as necessary
			for (int x = 0; x<temp.size(); x++) {
				// If the next person was selected, it adds them to the ordered list, removes their Attack Speed from the total, and removes them from the temp list
				if (choice <= temp.get(x).getAttackSpeed() + position) {
					ordered.add(temp.get(x));
					sum -= temp.get(x).getAttackSpeed();
					temp.remove(x);
					break;
				}
				// If the next person was not selected, the position takes their Attack Speed value to add above to select the next person
				position += temp.get(x).getAttackSpeed();
			}
		}
		// Set the combatantList as the new ordered list
		this.combatantList = ordered;
	}
	
	// Prompts adjustments to the turn order
	private void promptAdjustTurnOrder() {
		// Prompt for any adjustments
		System.out.println("\nAdjust turn order?");
		if (this.askYorN()) {
			while (true) {
				// Pick a Character to adjust
				System.out.println("Pick a Character to adjust.");
				Character movingCharacter = this.targetSingle();
				// If canceled, return and be done
				if (movingCharacter.equals(Character.EMPTY)) {
					return;
				}
				
				// Pick a place to put them
				System.out.println("Select a number location in the current order (listed above) to move " + movingCharacter.getName() + " to.");
				System.out.println("Choices: 1 - " + this.combatantList.size());
				while (true) {
					// When they enter an integer
					if (this.getPrompter().hasNextInt()) {
						// Get the integer
						int location = this.getPrompter().nextInt();
						this.getPrompter().nextLine();
						// If it's in the bounds
						if (location >= 1 && location <= this.combatantList.size()) {
							// Change the location
							this.combatantList.remove(this.combatantList.indexOf(movingCharacter));
							this.combatantList.add(location - 1, movingCharacter);
							
							// Break out of loop
							break;
						}
						// If it's not in the bounds, notify and ask again
						else {
							System.out.println("Invalid response. Please enter a valid response.");
							System.out.println("Choices: 1 - " + this.combatantList.size());
						}
					}
					// If they did not enter an integer, notify and ask again
					else {
						String response = this.getPrompter().nextLine();
						System.out.println("\""+response+"\" is not a valid response. Please enter a valid response.");
						System.out.println("Choices: 1 - " + this.combatantList.size());
					}
				}
				
				// Display new turn order
				System.out.println("New Turn Order of Combat:");
				for (int x = 0; x < this.combatantList.size(); x++) {
					System.out.println("" + (x+1) + ". " + this.combatantList.get(x).getName());
				}
				
				// Prompt for further adjustment.
				System.out.println("\nAdjust turn order?");
				// If done (they responded no), return to be done
				if (!this.askYorN()) {
					return;
				}
			}
		}
	}
	
	// Writes information about the Enemies in the battle to a text file to be viewed by me (controlling the enemies)
	private void writeEnemyInfoFile() throws IOException {
		// Create a Buffered Writer to write to the file: "EnemyInfo.txt"
		BufferedWriter writer = new BufferedWriter(new FileWriter("EnemyInfo.txt"));
		int i = 0;
		// Print in maximum groups of 3
		while (i + 2 < this.enemyList.size()) {
			// Write name, speed, and range of each enemy with appropriate spacing (40 characters wide per enemy)
			writer.write(String.format("%-40s%-40s%-40s", this.enemyList.get(i).getName(), this.enemyList.get(i+1).getName(), this.enemyList.get(i+2).getName()));
			writer.newLine();
			writer.write(String.format("Speed: %-33dSpeed: %-33dSpeed: %-33d", this.enemyList.get(i).getSpeed(), this.enemyList.get(i+1).getSpeed(), this.enemyList.get(i+2).getSpeed()));
			writer.newLine();
			writer.write(String.format("Range: %-33dRange: %-33dRange: %-33d", this.enemyList.get(i).getRange(), this.enemyList.get(i+1).getRange(), this.enemyList.get(i+2).getRange()));
			writer.newLine();
			
			// Write threat order of each enemy
			writer.write(String.format("%-40s%-40s%-40s", "Threat Order:", "Threat Order:", "Threat Order:"));
			writer.newLine();
			for (int x = 0; x < this.allyList.size(); x++) {
				// Each enemy's Threat order should have the same length, allyList. In case of errors, the try-catch will keep going and notify.
				try {
					// Write each ally from each enemy's getThreatList() numbered by x + 1 with appropriate spacing (40 characters)
					writer.write(String.format("%2d.%-37s%2d.%-37s%2d.%-37s", x+1, this.enemyList.get(i).getThreatOrder().get(x).getName(), 
																			  x+1, this.enemyList.get(i+1).getThreatOrder().get(x).getName(),
																			  x+1, this.enemyList.get(i+2).getThreatOrder().get(x).getName()));
					writer.newLine();
				}
				catch (IndexOutOfBoundsException ex) {
					System.out.println("Note: An index error occurred when writing Enemy Threat Lists. Not all were the length of the ally list.");
				}
			}
			// Increment counter by 3
			i += 3;
		}
		// Print the rest (1 or 2) in the same way
		if (i < this.enemyList.size()) {
			// Get an extra line so it isn't all compact
			writer.newLine();
			
			// Set a boolean value to true if there is a second enemy
			boolean isAnother = i + 1 < this.enemyList.size();
			
			// Write the first (and second if present) enemy's name with appropriate spacing (40 characters wide)
			writer.write(String.format("%-40s", this.enemyList.get(i).getName()));
			if (isAnother) {
				writer.write(String.format("%-40s", this.enemyList.get(i+1).getName()));
			}
			writer.newLine();
			
			// Do the same for speed
			writer.write(String.format("Speed: %-33d", this.enemyList.get(i).getSpeed()));
			if (isAnother) {
				writer.write(String.format("Speed: %-33d", this.enemyList.get(i+1).getSpeed()));
			}
			writer.newLine();
			
			// And the same for range
			writer.write(String.format("Range: %-33d", this.enemyList.get(i).getRange()));
			if (isAnother) {
				writer.write(String.format("Range: %-33d", this.enemyList.get(i).getRange()));
			}
			writer.newLine();
			
			// Then write the threat order for the enemy (and the second if present)
			writer.write(String.format("%-40s", "Threat Order:"));
			if (isAnother) {
				writer.write(String.format("%-40s", "Threat Order:"));
			}
			writer.newLine();
			for (int x = 0; x < this.allyList.size(); x++) {
				// The enemy's Threat order should have the same length as the others, allyList. In case of errors, the try-catch will keep going and notify.
				try {
					// Write each ally from each enemy's getThreatList() numbered by x + 1 with appropriate spacing (40 characters)
					writer.write(String.format("%2d.%-37s", x+1, this.enemyList.get(i).getThreatOrder().get(x).getName()));
					if (isAnother) {
						writer.write(String.format("%2d.%-37s", x+1, this.enemyList.get(i).getThreatOrder().get(x).getName()));
					}
					writer.newLine();
				}
				catch (IndexOutOfBoundsException ex) {
					System.out.println("Note: An index error occurred when writing Enemy Threat Lists. Not all were the length of the ally list.");
				}
			}
		}
		// Close the writer since we're all done
		writer.close();
	}
	
	public void initiate() throws IOException {
		// Display that combat has begun
		System.out.println("War of Gillysburg Combat initiated.");
		
		// First, set the turn order
		this.setOrder();
		
		// Display the turn order
		System.out.println("Turn order for combat:");
		for (int x = 0; x < this.combatantList.size(); x++) {
			System.out.println("" + (x+1) + ". " + this.combatantList.get(x).getName());
		}
		
		// Prompt for any adjustments
		this.promptAdjustTurnOrder();
		
		// Set the Threat and TacticalThreat for each Enemy
		for (int x = 0; x<this.enemyList.size(); x++) {
			// Set the Threat Order:
			this.enemyList.get(x).setThreatOrder(this.allyList);
		}
		
		// Write the now set Enemy information to the EnemyInfo.txt file for reference when playing.
		this.writeEnemyInfoFile();
		
		// Begin loop, counting up in the number of rounds
		boolean bothAlive = true;
		while (bothAlive) {
			// Output the round number in a big way
			System.out.println();
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("Round: " + this.round);
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println();
			// For each Character in combatants, begin turn
			for (Character c : this.combatantList) {
				c.beginTurn();
				// If All Allies are dead, all enemies are dead, break out of both loops
				if (this.allAlliesDead() || this.allEnemiesDead()) {
					bothAlive = false;
					break;
				}
			}
			this.round++;
		}
		// Output result of battle.
		if (this.allAlliesDead() && !this.allEnemiesDead()) {
			System.out.println("\nDEFEAT!");
		}
		else if (this.allEnemiesDead() && !this.allAlliesDead()) {
			System.out.println("\nVICTORY!!!");
		}
		else {
			System.out.println("A... DRAW?!?");
		}
	}
	
	public void playDemo(Character player) throws IOException {
		// Add an ally and two enemies
		Character ally = new CharacterBuilder()
				.Name("Ally")
				.Level(player.getLevel())
				.Health((int)Math.round(350 * Math.pow(1.03, player.getLevel())))
				.Damage((int)Math.round(80 * Math.pow(1.03, player.getLevel())))
				.Armor((int)Math.round(100 * Math.pow(1.05, player.getLevel() / 5)))
				.ArmorPiercing((int)Math.round(100 * Math.pow(1.05, player.getLevel() / 5)))
				.Accuracy((int)Math.round(110 * Math.pow(1.05, player.getLevel() / 5)))
				.Dodge((int)Math.round(25 * Math.pow(1.05, player.getLevel() / 5)))
				.CriticalChance(10)
				.Speed(6)
				.AttackSpeed(5)
				.Threat(10)
				.TacticalThreat(10)
				.build();
		
		Enemy normEnemy = new EnemyBuilder()
				.Name("Enemy")
				.Level(player.getLevel())
				.Health((int)Math.round(350 * Math.pow(1.03, player.getLevel())))
				.Damage((int)Math.round(80 * Math.pow(1.03, player.getLevel())))
				.Armor((int)Math.round(100 * Math.pow(1.05, player.getLevel() / 5)))
				.ArmorPiercing((int)Math.round(100 * Math.pow(1.05, player.getLevel() / 5)))
				.Accuracy((int)Math.round(110 * Math.pow(1.05, player.getLevel() / 5)))
				.Dodge((int)Math.round(25 * Math.pow(1.05, player.getLevel() / 5)))
				.CriticalChance(10)
				.Speed(6)
				.AttackSpeed(5)
				.UseThreat(true)
				.build();
		
		Enemy lightVulEnemy = new EnemyBuilder(normEnemy)
				.Name("Tactical Light Vulnerable Enemy")
				.UseThreat(false)
				.addVulnerability(DmgType.LIGHT, 30)
				.build();
		
		// Add the ally and enemies to the combat
		this.addAlly(ally);
		this.addEnemy(normEnemy);
		this.addEnemy(lightVulEnemy);
		
		// Display that combat has begun
		System.out.println();
		System.out.println("When combat is first initiated, the program first determines the turn order of the combatants via bias random based on the Attack Speed stat (as seen below).");
		System.out.println("Here, as is done elsewhere, the program makes sure the outcome is desired by the Game Master, prompting for any changes before continuing.");
		System.out.println();
		
		// First, set the turn order
		this.setOrder();
		
		// Display the turn order
		System.out.println("Turn order for combat:");
		for (int x = 0; x < this.combatantList.size(); x++) {
			System.out.println("" + (x+1) + ". " + this.combatantList.get(x).getName());
		}
		
		// Prompt for any adjustments
		this.promptAdjustTurnOrder();
		
		// Continue descriptions, talking now about Threat and Tactical Threat
		System.out.println();
		System.out.println("Once turn order has been established, the program then decides for the enemies which of the allies they find to be most threating.");
		System.out.println("This also occurs via bias random on the Threat stat for basic enemies and on the Tactical Threat stat for more advanced enemies.");
		System.out.println("This happens in the background and is written in a file that can be used for the Game Master's reference.");
		System.out.println("Additionally, on an enemy\'s turn, the Game Master and view the listing created by selecting the \"Display Threat Order\" command.");
		
		// Set the Threat and TacticalThreat for each Enemy
		for (int x = 0; x<this.enemyList.size(); x++) {
			// Set the Threat Order:
			this.enemyList.get(x).setThreatOrder(this.allyList);
		}
		
		// Write the now set Enemy information to the EnemyInfo.txt file for reference when playing.
		this.writeEnemyInfoFile();
		
		// Describe 1 final time before initiating combat
		System.out.println();
		System.out.println("With all the prep work done, combat is started allowing each combatant to be able to take turns making actions until one side is defeated.");
		System.out.println("The program is designed to keep track of as much as possible without having to execute multiple programs or keep track of a ton of things on paper.");
		System.out.println("Even though the program keeps track of things such as limiting conditions and cooldowns, it will always allow the Game Master to make a decision (that could be based on the mission being played) that overrides the norm.");
		System.out.println("Feel free to try out the systems in place! Press enter to start the combat.");
		this.getPrompter().nextLine();
		
		// Begin loop, counting up in the number of rounds
		boolean bothAlive = true;
		while (bothAlive) {
			// Output the round number in a big way
			System.out.println();
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("Round: " + this.round);
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println("-----------------------------------------------------------------------------");
			System.out.println();
			// For each Character in combatants, begin turn
			for (Character c : this.combatantList) {
				c.beginTurn();
				// If All Allies are dead, all enemies are dead, break out of both loops
				if (this.allAlliesDead() || this.allEnemiesDead()) {
					bothAlive = false;
					break;
				}
			}
			this.round++;
		}
		// Output result of battle.
		if (this.allAlliesDead() && !this.allEnemiesDead()) {
			System.out.println("\nDEFEAT!");
		}
		else if (this.allEnemiesDead() && !this.allAlliesDead()) {
			System.out.println("\nVICTORY!!!");
		}
		else {
			System.out.println("A... DRAW?!?");
		}
	}
}
