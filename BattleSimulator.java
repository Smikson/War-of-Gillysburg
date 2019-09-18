package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class BattleSimulator {
	// Holds the number of combatants in the current battle (default of 1 for testing)
	private static int numCombatants = 1;
	private static int round = 1;
	private static List<Character> allyList;
	private static List<Enemy> enemyList;
	private static List<Character> combatantList;
	
	
	// Methods to set up battles
	private static LinkedList<Character> setOrder(List<Character> combatants) {
		List<Character> temp = new LinkedList<>();
		for (int x = 0; x<combatants.size(); x++) {
			temp.add(combatants.get(x));
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
		return ret;
	}
	
	public static LinkedList<Character> initiate(List<Character> allies, List<Enemy> enemies) {
		// Stores all allies and enemies for the battle
		BattleSimulator.allyList = allies;
		BattleSimulator.enemyList = enemies;
		
		// Puts everyone in a Combatants List
		List<Character> combatants = new LinkedList<>();
		for (int x = 0; x<enemies.size(); x++) {
			combatants.add(enemies.get(x));
		}
		for (int x = 0; x<allies.size(); x++) {
			combatants.add(allies.get(x));
		}
		// Stores the number of combatants and the list (in battle, this will be done before all Conditions/Abilities use this value for TurnCounters)
		BattleSimulator.numCombatants = combatants.size();
		BattleSimulator.combatantList = combatants;
		
		
		// Sets Threat and TacticalThreat for each Enemy
		for (int x = 0; x<enemies.size(); x++) {
			enemies.get(x).setThreatOrder(allies);
		}
		
		return BattleSimulator.setOrder(combatants);
	}
	
	public static int numCombatants() {
		return BattleSimulator.numCombatants;
	}
	public static int round() {
		return BattleSimulator.round;
	}
}
