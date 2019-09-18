package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;

public class KinitchuOrderArcana extends Character{
	// These first two methods help set up the Kinitchu Order Arcana subclass.
	public KinitchuOrderArcana(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
	}
	public KinitchuOrderArcana(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getAttackType());
	}
	
	// Find the enemy that is vulnerable and the Ability they are vulnerable to from the "Arcane Knowledge" Passive Ability
	public String useArcaneKnowledgeFind(List<Character> enemies) {
		// Determines if any character was found.
		Dice percent = new Dice(100);
		if (percent.roll() > 50) {
			return "You found no enemies vulnerable. Bummer.";
		}
		else {
			List<Character> temp = new LinkedList<>();
			for (int x = 0; x<enemies.size(); x++) {
				temp.add(enemies.get(x));
			}
			
			String ret = "";
			Dice d;
			int sum = 0;
			//Find's total Level (Number based on difficulty)
			for (int x = 0; x<temp.size(); x++) {
				sum += temp.get(x).getLevel();
			}
			
			d = new Dice(sum);
			int choice = d.roll();
			int position = 0;
			for (int x = 0; x<temp.size(); x++) {
				// If the next person was selected, it adds them to the return, and removes their Level from the total
				if (choice <= temp.get(x).getLevel() + position) {
					ret += temp.get(x).getName() + "\n";
					sum -= temp.get(x).getLevel();
					temp.remove(x);
					break;
				}
				// If the next person was not selected, the position takes their Level value to add above to select the next person
				position += temp.get(x).getLevel();
			}
			return ret;
		}
	}
	
	// Returns a new Character with improved stats based on the "Arcane Knowledge" Passive Ability for purposes of Calculation only.
	public KinitchuOrderArcana useArcaneKnowledgeEnhancement(boolean didUseAbility) {
		if (didUseAbility ) {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.1)).CriticalChance((int) Math.round(this.getCriticalChance() * 1.1)).Accuracy((int) Math.round(this.getAccuracy() * 1.1)).ArmorPiercing((int) Math.round(this.getArmorPiercing() * 1.1)).buildKOA();
		}
		else {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.05)).CriticalChance((int) Math.round(this.getCriticalChance() * 1.05)).Accuracy((int) Math.round(this.getAccuracy() * 1.05)).ArmorPiercing((int) Math.round(this.getArmorPiercing() * 1.05)).buildKOA();
		}
	}
	
	// Deals with the Healing from the "Raw, Magical Power" Passive Ability when an enemy is defeated
	public String useRawMagicalPowerDefeatHealing(Character enemyDefeated) {
		// If Enemy is Normal or Advanced
		if (enemyDefeated.getLevel() == 5) {
			// Calculates the amount of healing based on Maximum Health
			int healing = (int) Math.round(.05 * this.getHealth());
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
		}
		// If Enemy is Elite
		else if (enemyDefeated.getLevel() == 3) {
			// Calculates the amount of healing based on Maximum Health
			int healing = (int) Math.round(.1 * this.getHealth());
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
		}
		// Enemy is Boss
		else {
			// Calculates the amount of healing based on Maximum Health
			int healing = (int) Math.round(.2 * this.getHealth());
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
		}
	}
	// Returns a new character with improved stats based on the "Raw, Magical Power" Passive Ability for purposes of calculation only.
	public KinitchuOrderArcana useRawMagicalPowerDefeatEnhancement(Character enemyDefeated) {
		// If Enemy is Normal or Advanced
		if (enemyDefeated.getLevel() == 5) {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.1)).buildKOA();
		}
		// If Enemy is Elite
		else if (enemyDefeated.getLevel() == 3) {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.15)).buildKOA();
		}
		// Enemy is Boss
		else {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.25)).buildKOA();
		}
	}
	// Deals with the Healing from the "Raw, Magical Power" Passive Ability when the amount of Damage is equal to at least double the Damage stat.
	public String useRawMagicalPowerDouble() {
		// Calculates the amount of healing based on Maximum Health
		int healing = (int) Math.round(.05 * this.getHealth());
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	// Deals the Damage from the "Raw, Magical Power" Passive Ability upon death. Big explosion.
	public String useRawMagicalPowerExplosion(List<Character> enemies) {
		String ret = "";
		for (Character enemy : enemies) {
			ret += this.attack(enemy, 5, false) + "\n"; // Attack, AOE, 5x Damage
		}
		
		return ret;
	}
	
	// Deals with the chance to succeed in the extra turn attempt in the "Quick Caster" Passive Ability
	public String useQuickCasterTurn() {
		Dice percent = new Dice(100);
		if (percent.roll() <= 5) {
			return "Success! Take another turn!";
		}
		else {
			return "Failure! Do not take another turn! Bummer!";
		}
	}
	// Returns a new character with improved stats based on the "Quick Caster" Passive Ability for purposes of calculation only if the Dodge bonus succeeded.
	public KinitchuOrderArcana useQuickCasterDodgeEnhancement() {
		return new CharacterBuilder(this).Dodge((int) Math.round(this.getDodge() * 1.75)).buildKOA();
	}
	
	// Deals the Damage from the "Magic Missiles" Ability (Ability 1).
	public String useMagicMissiles(Character enemy) {
		// Two Attacks, Targeted, Cannot Miss, .75x Damage
		return this.attack(enemy, .75, true, false) + "\n" + this.attack(enemy, .75, true, false);
	}
	
	// Deals the Damage from the "Arcane Repulse" Ability (Ability 2).
	public String useArcaneRepulse(List<Character> innerEnemies, List<Character> outerEnemies) {
		String ret = "";
		for (Character enemy : innerEnemies) {
			ret += this.attack(enemy, 1, false) + "\n"; // Attack, AOE, 1x Damage
		}
		for (Character enemy : outerEnemies) {
			ret += this.attack(enemy, .5, false) + "\n"; // Attack, AOE, .5x Damage
		}
		
		return ret;
	}
	
	// Deals the Damage from the "Teleport" Ability (Ability 3) bonus attack.
	public String useTeleportBonusAttack(Character enemy) {
		return this.attack(enemy, .5); // Attack, Targeted, .5x Damage
	}
	
	// Deals the Damage from the "Arcane Beam" Ability (Ability 4) Targeted Option
	public String useArcaneBeam(List<Character> enemiesHit, Character primaryEnemy) {
		String ret = "";
		
		for (Character enemy : enemiesHit) {
			ret += this.attack(enemy, .25, false) + "\n"; // Attack, AOE, .25x Damage
		}
		
		ret += this.attack(primaryEnemy, 1.5); // Attack, Targeted, 1.5x Damage
		return ret;
	}
	
	// Returns a new character with improved stats based on the "Supercharged" ULTIMATE Ability for purposes of calculation only.
	public KinitchuOrderArcana useSuperchargedEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.5)).buildKOA();
	}
}
