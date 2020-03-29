package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;
public class Enemy extends Character{
	private LinkedList<Character> ThreatOrder;
	private boolean UseThreat;
	
	public Enemy(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, boolean useThrt, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls);
		this.UseThreat = useThrt;
		this.ThreatOrder = new LinkedList<>();
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
	
	public String getThreatOrder() {
		String ret = "The Threat Order for " + this.getName() + " is:\n";
		for (Character c : this.ThreatOrder) {
			ret += c.getName() + "\n";
		}
		return ret;
	}
}
