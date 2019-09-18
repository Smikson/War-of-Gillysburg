package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Dragon extends Character{
	// This first method helps set up the Dragon based on an original Kinitchu Order Dragon Fire Wizard.
	public Dragon(KinitchuOrderDragonFireWizard ori) {
		super(ori.getName() + "'s Dragon", ori.getLevel(), ori.getHealth() * 2, ori.getDamage() * 2, ori.getArmor() * 2, ori.getArmorPiercing() * 2, ori.getAccuracy() * 2, 0, ori.getDodge() * 2, ori.getCriticalChance() * 2, ori.getSpeed() * 2, ori.getAttackSpeed(), 1, ori.getThreat() * 2, ori.getTacticalThreat() * 2, ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getAttackType());
	}
	
	// Deals the Damage from a "cleave" basic attack.
	public String useCleave(List<Character> enemies) {
		String ret = "";
		
		for (Character enemy : enemies) {
			ret += this.attack(enemy, 1) + "/n"; // Attack, Targeted, 1x Damage
		}
		
		return ret;
	}
	
	// Deals the Damage from "Dragon Breath"
	public String useDragonBreath(List<Character> enemies) {
		String ret = "";
		
		for (Character enemy : enemies) {
			ret += this.attack(enemy, 1, false) + "\n"; // Attack, AOE, 1x Damage
		}
		
		return ret;
	}
}
