package WyattWitemeyer.WarOfGillysburg;
import java.util.*;



public class Enemy extends Character {
	// A list of the possible types of enemies
	public static enum Difficulty {
		NORMAL, ADVANCED, ELITE, BOSS;
	}
	
	private LinkedList<Character> ThreatOrder;
	private boolean UseThreat;
	private Enemy.Difficulty difficulty;
	
	public Enemy(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, boolean useThrt, Difficulty diff, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, Type type) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, type);
		this.ThreatOrder = new LinkedList<>();
		this.UseThreat = useThrt;
		this.difficulty = diff;
		
		
		// Adds new command for Displaying the Enemy's Threat Order
		this.addCommand(new DisplayThreatOrderCommand(this));
	}
	public Enemy(Enemy copy) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.usesThreat(), copy.getDifficulty(), copy.getResistances(), copy.getVulnerabilities(), copy.getType());
	}
	
	public boolean usesThreat() {
		return this.UseThreat;
	}
	public Difficulty getDifficulty() {
		return this.difficulty;
	}
	
	// Sets the threat order of the enemy.
	public void setThreatOrder(List<Character> allies) {
		List<Character> temp = new LinkedList<>();
		for (int x = 0; x<allies.size(); x++) {
			temp.add(allies.get(x));
		}
		LinkedList<Character> ret = new LinkedList<>();
		Dice d;
		int sum = 0;
		//Find's total Threat value based upon what this enemy uses for Threat (normal or Tactical)
		for (int x = 0; x<temp.size(); x++) {
			if (this.UseThreat) {
				sum += temp.get(x).getThreat();
			}
			else {
				sum += temp.get(x).getTacticalThreat();
			}
		}
		while(sum>0) {
			d = new Dice(sum);
			int choice = d.roll();
			int position = 0;
			for (int x = 0; x<temp.size(); x++) {
				if (this.UseThreat) {
					// If the next person was selected, it adds them to the return, and removes their Threat from the total
					if (choice <= temp.get(x).getThreat() + position) {
						ret.add(temp.get(x));
						sum -= temp.get(x).getThreat();
						temp.remove(x);
						break;
					}
					// If the next person was not selected, the position takes their Threat value to add above to select the next person
					position += temp.get(x).getThreat();
				}
				else {
					// If the next person was selected, it adds them to the return, and removes their TacticalThreat from the total
					if (choice <= temp.get(x).getTacticalThreat() + position) {
						ret.add(temp.get(x));
						sum -= temp.get(x).getTacticalThreat();
						temp.remove(x);
						break;
					}
					// If the next person was not selected, the position takes their TacticalThreat value to add above to select the next person
					position += temp.get(x).getTacticalThreat();
				}
			}
		}
		this.ThreatOrder = ret;
	}
	
	public LinkedList<Character> getThreatOrder() {
		LinkedList<Character> copy = new LinkedList<>();
		copy.addAll(this.ThreatOrder);
		return copy;
	}
	
	public void displayThreatOrder() {
		if (this.ThreatOrder.isEmpty()) {
			System.out.println("The Threat Order for " + this.getName() + " has not been set up. Something went wrong.");
			return;
		}
		
		System.out.println("The Threat Order for " + this.getName() + " is:");
		for (Character c : this.ThreatOrder) {
			System.out.println(c.getName());
		}
	}
}
