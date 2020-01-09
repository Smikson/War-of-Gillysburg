package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

// Singleton class for running a battle -- Also contains helpful info for some class Abilities
public class BattleSimulator {
	// The instance of the simulator
	private static BattleSimulator instance;
	
	// Holds the info about combatants and rounds
	private int round;
	private LinkedList<Character> allyList;
	private LinkedList<Character> enemyList;
	private LinkedList<Character> combatantList;
	
	// Helpful for prompting controller for commands
	private Scanner prompter;
	
	// Constructor
	private BattleSimulator() {
		this.round = 1;
		this.allyList = new LinkedList<Character>();
		this.enemyList = new LinkedList<Character>();
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
	
	// Get methods:
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
	public LinkedList<Character> getEnemies() {
		LinkedList<Character> ret = new LinkedList<>();
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
	public void addAlly(Character ally) {
		allyList.add(ally);
		combatantList.add(ally);
	}
	
	public void addEnemy(Character enemy) {
		enemyList.add(enemy);
		combatantList.add(enemy);
	}
	
	// Prompt methods
	// Returns true if yes, false if no
	public boolean askYorN() {
		String responce = this.getPrompter().nextLine().toUpperCase();
		boolean flag = true, ret = false;
		while (flag) {
			if (responce.equals("Y")) {
				ret = true;
				flag = false;
			}
			else if (responce.equals("N")) {
				ret = false;
				flag = false;
			}
			else {
				System.out.println("Please respond with Y or N.");
				responce = this.getPrompter().nextLine().toUpperCase();
			}
		}
		// Return result
		return ret;
	}
	// Return a chosen Character in combatants (or EMPTY if chosen)
	public Character targetSingle() {
		boolean flag = true;
		int choice = 1;
		// Display options
		System.out.println("Select Single Target:");
		System.out.println("0. None (Go back)");
		for (int i = 0; i < this.getCombatants().size(); i++) {
			System.out.println("" + (i+1) + ". " + this.getCombatants().get(i).getName());
		}
		System.out.print("Choice? ");
		while (flag) {
			// Get result
			if (this.getPrompter().hasNextInt()) {
				choice = this.getPrompter().nextInt();
				this.getPrompter().nextLine();
				if (choice <= this.getCombatants().size()) {
					if (choice == 0) {
						return Character.EMPTY;
					}
					flag = false;
				}
				else {
					System.out.println("Invalid responce. Please enter a valid responce.");
					System.out.print("Choice? ");
				}
			}
			else {
				String responce = this.getPrompter().nextLine();
				System.out.println("\""+responce+"\" is not a valid responce. Please enter a valid responce.");
				System.out.print("Choice? ");
			}
		}
		return this.getCombatants().get(choice-1);
	}
	// Return a list of chosen Characters in combatants (or an empty list if none is requested)
	public LinkedList<Character> targetMultiple() {
		boolean flag = true;
		int choice = 1;
		LinkedList<Character> ret = new LinkedList<>();
		// Display options
		System.out.println("Select Targets:");
		System.out.println("0. Done (Goes back if none already chosen)");
		for (int i = 0; i < this.getCombatants().size(); i++) {
			System.out.println("" + (i+1) + ". " + this.getCombatants().get(i).getName());
		}
		System.out.print("Choice? ");
		while (flag) {
			// Get result
			if (this.getPrompter().hasNextInt()) {
				choice = this.getPrompter().nextInt();
				this.getPrompter().nextLine();
				if (choice <= this.getCombatants().size()) {
					if (choice == 0) {
						flag = false;
					}
					else {
						ret.add(this.getCombatants().get(choice-1));
						System.out.print("Current list: " + ret.get(0).getName());
						for (int i = 1; i < ret.size(); i++) {
							System.out.print(", " + ret.get(i).getName());
						}
						System.out.print("\nChoice? ");
					}
				}
				else if ((-choice) <= this.getCombatants().size()) {
					if (choice != 0) {
						if (ret.contains(this.getCombatants().get((-choice) - 1))) {
							ret.remove(this.getCombatants().get((-choice) - 1));
							System.out.print("Current list: " + ret.get(0).getName());
							for (int i = 1; i < ret.size(); i++) {
								System.out.print(", " + ret.get(i).getName());
							}
							System.out.print("\nChoice? ");
						}
					}
				}
				else {
					System.out.println("Invalid responce. Please enter a valid responce.");
					System.out.print("Choice? ");
				}
			}
			else {
				String responce = this.getPrompter().nextLine();
				System.out.println("\""+responce+"\" is not a valid responce. Please enter a valid responce.");
				System.out.print("Choice? ");
			}
		}
		return ret;
	}
	
	
	// Methods to set up battles
	private void setOrder() {
		List<Character> temp = new LinkedList<>();
		for (int x = 0; x<combatantList.size(); x++) {
			temp.add(combatantList.get(x));
		}
		LinkedList<Character> ret = new LinkedList<>();
		Dice d;
		int sum = 0;
		//Find's total Attack Speed
		for (int x = 0; x<temp.size(); x++) {
			sum += temp.get(x).getAttackSpeed();
		}
		
		while(sum>0) {
			d = new Dice(sum);
			int choice = d.roll();
			int position = 0;
			for (int x = 0; x<temp.size(); x++) {
				// If the next person was selected, it adds them to the return, and removes their Attack Speed from the total
				if (choice <= temp.get(x).getAttackSpeed() + position) {
					ret.add(temp.get(x));
					sum -= temp.get(x).getAttackSpeed();
					temp.remove(x);
					break;
				}
				// If the next person was not selected, the position takes their Attack Speed value to add above to select the next person
				position += temp.get(x).getAttackSpeed();
			}
		}
		this.combatantList = ret;
	}
	
	public void initiate() {
		/*
		// Sets Threat and TacticalThreat for each Enemy
		for (int x = 0; x<enemyList.size(); x++) {
			enemyList.get(x).setThreatOrder(allyList);
		}
		*/
		
		// Then sets the turn order
		setOrder();
		
		// Add stuff here for how turns work (first person begins turn, etc.)
		// Infintely loop, counting up in the number of rounds -- or use stop conditions below, have it only break out of the one loop?
		// For each Character in combatants, begin turn
		// If All Allies are dead, all enemies are dead, (or the flag variable has changed) break out of both loops
		while (this.round < 4) {
			System.out.println("Round: " + this.round);
			for (Character c : this.combatantList) {
				c.beginTurn();
			}
			this.round++;
		}
	}
	
}
