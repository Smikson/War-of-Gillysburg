package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;

public class KinitchuOrderLuminescentWizard extends Character{
	// These first two methods help set up the Kinitchu Order Dragon Fire Wizard subclass.
	public KinitchuOrderLuminescentWizard(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, Type type) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, type);
	}
	public KinitchuOrderLuminescentWizard(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getBaseDmgType(), ori.getResistances(), ori.getVulnerabilities(), ori.getType());
	}
	
	
	// Deals with the healing from the "Light Speed Stabilization" Passive Ability (saves ally from death, heals to percentage of maximum Health).
	public void useLightSpeedStabilizationHealing(Character ally) {
		// Calculates the amount of healing (10% of maximum)
		int healing = (int) Math.round(.1 * ally.getHealth());
		
		// Sets ally's Health to 0 before restoring so their new Health total will be the healing amount
		ally.setCurrentHealth(0);
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		this.restoreHealth(healing);
	}
	
	// Deals the damage from the "Searing Light" (Ability 2)
	public void useSearingLight(Character enemy, boolean isBlinded) {
		// If they're blinded, attack with 50% extra damage
		if (isBlinded) {
			//this.attackNoMiss(enemy, 1.25 * 1.5); // Attack, Targeted, 1.25x Damage (increased by 50%), Cannot Miss
		}
		
		// Otherwise, just normal
		//this.attackNoMiss(enemy, 1.25); // Attack, Targeted, 1.25x Damage, Cannot Miss
	}
	// "Luminescent Amplification" AOE version of Ability
	public void useSearingLight(List<Character> enemies, List<Boolean> isBlinded) {
		for (int i = 0; i < enemies.size(); i++) {
			if (isBlinded.get(i)) {
				//this.attackAOE(enemies.get(i), .8 * 1.25 * 1.5); // Attack, AOE, 1.25x Damage (decreased to 80% effectiveness, increased by 50%)
			}
			else {
				//this.attackAOE(enemies.get(i), .8 * 1.25); // Attack, AOE, 1.25x Damage (decreased to 80% effectiveness)
			}
		}
	}
	
	// Deals with the healing for the "Luminescent Restoration" (Ability 3)
	public void useLuminescentRestoration(Character ally) {
		//boolean didCrit = landCrit(ally);
		
		// Calculates the amount of healing based on Damage
		int healing = (int) Math.round(1.3 * this.getDamage());
		/*
		// If a critical, double the healing
		if (didCrit) {
			healing *= 2;
		}
		*/
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		this.restoreHealth(healing);
	}
	// "Luminescent Amplification" AOE version of Ability
	public void useLuminescentRestoration(List<Character> allies) {
		/*
		for (Character ally : allies) {
			//boolean didCrit = landCrit(ally);
			
			// Calculates the amount of healing based on Damage (reduced to 80% effectiveness)
			int healing = (int) Math.round(.8 * 1.3 * this.getDamage());
			
			// If a critical, double the healing
			if (didCrit) {
				healing *= 2;
			}
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
		}
		*/
	}
	
	// Deals the Damage from "Blinding Light" (Ability 4)
	public void useBlindingLight(Character enemy) {
		//this.attack(enemy, .75); // Attack, Targeted, .75x Damage
	}
	// "Luminescent Amplification" AOE version of Ability
	public void useBlindingLight(List<Character> enemies) {
		/*
		for (Character enemy : enemies) {
			this.attackAOE(enemy, .8 * .75); // Attack, AOE, .75x Damage (reduced to 80% effectiveness)
		}
		*/
	}
}
