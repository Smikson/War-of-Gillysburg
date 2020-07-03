package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SteelLegionBarbarian extends Character {
	
	// These first two methods help set up the Steel Legion Barbarian subclass.
	public SteelLegionBarbarian(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls, Type type) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, type);
	}
	public SteelLegionBarbarian(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getType());
	}
	
	// Returns a new Character with improved stats based on the "Rage!" Passive Ability for purposes of Calculation only.
	public Character useRageEnhancement() {
		return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * .85))
				.Damage((int) Math.round(this.getDamage() * 1.5))
				.STDdown(85)
				.STDup(115)
				.buildSLB();
	}
	
	// Deals the Damage from the "Smash" Ability (Ability 1)
	public void useSmash(Character enemy) {
		//this.attack(enemy, 1.8); // Attack, Targeted, 1.8x Damage
	}
	
	// Deals the Damage from the "Sundering Strike" Ability (Ability 2)
	public void useSunderingStrike(Character enemy) {
		//this.attack(enemy, 1); // Attack, Targeted, 1x Damage
	}
	
	// Deals the Damage from the "Axe Throw" Ability (Ability 3) to multiple enemies
	public void useAxeThrow(Character enemy1, List<Character> enemies2) {
		/*
		this.attack(enemy1, 1.2); // Attack, Targeted, 1.2x Damage
		
		for (Character enemy : enemies2) {
			this.attackAOE(enemy, .75); // Attack, AOE, .75x Damage
		}
		*/
	}
	
	// Deals with the healing portion of the "War Cry" Ability (Ability 4)
	public void useWarCry(boolean isDoubled) {
		// Checks to see if specific conditions are met in which the healing should be doubled
		if (isDoubled) {
			// Calculates the healing based on Missing Health (includes the double by multiplying by 2)
			int healing = (int)Math.round(.15 * 2 *(this.getHealth() - this.getCurrentHealth()));
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
		}
		
		// Calculates the healing based on Missing Health
		int healing = (int)Math.round(.15 *(this.getHealth() - this.getCurrentHealth()));
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
	}
}
