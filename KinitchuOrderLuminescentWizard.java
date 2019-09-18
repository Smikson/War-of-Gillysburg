package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;

public class KinitchuOrderLuminescentWizard extends Character{
	// These first two methods help set up the Kinitchu Order Dragon Fire Wizard subclass.
	public KinitchuOrderLuminescentWizard(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
	}
	public KinitchuOrderLuminescentWizard(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getAttackType());
	}
	
	
	// Deals with the healing from the "Light Speed Stabilization" Passive Ability (saves ally from death, heals to percentage of maximum Health).
	public String useLightSpeedStabilizationHealing(Character ally) {
		// Calculates the amount of healing (10% of maximum)
		int healing = (int) Math.round(.1 * ally.getHealth());
		
		// Sets ally's Health to 0 before restoring so their new Health total will be the healing amount
		ally.setCurrentHealth(0);
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	
	// Deals the damage from the "Searing Light" (Ability 2)
	public String useSearingLight(Character enemy, boolean isBlinded) {
		// If they're blinded, attack with 50% extra damage
		if (isBlinded) {
			return this.attack(enemy, 1.25 * 1.5, true, false); // Attack, Targeted, 1.25x Damage (increased by 50%), Cannot Miss
		}
		
		// Otherwise, just normal
		return this.attack(enemy, 1.25, true, false); // Attack, Targeted, 1.25x Damage, Cannot Miss
	}
	// "Luminescent Amplification" AOE version of Ability
	public String useSearingLight(List<Character> enemies, List<Boolean> isBlinded) {
		String ret = "";
		for (int i = 0; i < enemies.size(); i++) {
			if (isBlinded.get(i)) {
				ret += this.attack(enemies.get(i), .8 * 1.25 * 1.5, false) + "\n"; // Attack, AOE, 1.25x Damage (decreased to 80% effectiveness, increased by 50%)
			}
			else {
				ret += this.attack(enemies.get(i), .8 * 1.25, false) + "\n"; // Attack, AOE, 1.25x Damage (decreased to 80% effectiveness)
			}
		}
		
		return ret;
	}
	
	// Deals with the healing for the "Luminescent Restoration" (Ability 3)
	public String useLuminescentRestoration(Character ally) {
		boolean didCrit = landCrit(ally);
		
		// Calculates the amount of healing based on Damage
		int healing = (int) Math.round(1.3 * this.getDamage());
		
		// If a critical, double the healing
		if (didCrit) {
			healing *= 2;
		}
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	// "Luminescent Amplification" AOE version of Ability
	public String useLuminescentRestoration(List<Character> allies) {
		String ret = "";
		
		for (Character ally : allies) {
			boolean didCrit = landCrit(ally);
			
			// Calculates the amount of healing based on Damage (reduced to 80% effectiveness)
			int healing = (int) Math.round(.8 * 1.3 * this.getDamage());
			
			// If a critical, double the healing
			if (didCrit) {
				healing *= 2;
			}
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			ret += this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth() + "\n";
		}
		
		return ret;
	}
	
	// Deals the Damage from "Blinding Light" (Ability 4)
	public String useBlindingLight(Character enemy) {
		return this.attack(enemy, .75); // Attack, Targeted, .75x Damage
	}
	// "Luminescent Amplification" AOE version of Ability
	public String useBlindingLight(List<Character> enemies) {
		String ret = "";
		for (Character enemy : enemies) {
			ret += this.attack(enemy, .8 * .75, false) + "\n"; // Attack, AOE, .75x Damage (reduced to 80% effectiveness)
		}
		
		return ret;
	}
}
