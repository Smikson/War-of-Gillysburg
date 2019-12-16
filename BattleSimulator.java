package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

// Singleton class for running a battle -- Also contains helpful info for some class Abilities
public class BattleSimulator {
	// The instance of the simulator
	private static BattleSimulator instance;
	
	// Holds the info about combatants and rounds
	private int round;
	private LinkedList<Character> allyList;
	private LinkedList<Enemy> enemyList;
	private LinkedList<Character> combatantList;
	
	// Constructor
	private BattleSimulator() {
		this.round = 1;
		this.allyList = new LinkedList<Character>();
		this.enemyList = new LinkedList<Enemy>();
		this.combatantList = new LinkedList<Character>();
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
	public void printEnemies() {
		for (Character enemy : this.enemyList) {
			System.out.println(enemy.getName());
		}
	}
	public void printCombatants() {
		for (Character combatant : this.combatantList) {
			System.out.println(combatant.getName());
		}
	}
	
	// Add methods
	public void addAlly(Character ally) {
		allyList.add(ally);
		combatantList.add(ally);
	}
	
	public void addEnemy(Enemy enemy) {
		enemyList.add(enemy);
		combatantList.add(enemy);
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
		combatantList = ret;
	}
	
	public void initiate() {
		// Sets Threat and TacticalThreat for each Enemy
		for (int x = 0; x<enemyList.size(); x++) {
			enemyList.get(x).setThreatOrder(allyList);
		}
		
		// Then sets the turn order
		setOrder();
		
		// Add stuff here for how turns work (first person begins turn, etc.)
	}
	
}
