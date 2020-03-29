package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SteelLegionWarrior extends Character {
	
	// These first two methods help set up the Steel Legion Warrior subclass.
	public SteelLegionWarrior(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls);
	}
	public SteelLegionWarrior(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities());
	}
	
	// Deals the Damage from the "Vengeance Strike" Passive Ability
	public void useVengeanceStrike(Character enemy) {
		this.attack(enemy, 1.2); // Attack, Targeted, 1.2x Damage
	}
	// Used for when "Deflection" is active
	public void useDeflectionVengeanceStrike(Character enemy) {
		this.attackDeflection(enemy, 1.2); // Deflection Attack, Targeted, 1.2x Damage
	}
	
	// Returns a new Character with improved stats based on the "Swordplay Prowess" Passive Ability for purposes of Calculation only.
	public SteelLegionWarrior useSwordplayProwessEnhancement() {
		return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * 1.11))
				.Damage((int) Math.round(this.getDamage() * 1.11))
				.CriticalChance((int) Math.round(this.getCriticalChance() + 11))
				.buildSLW();
	}
	
	// Returns a new Character with improved stats based on the "Agile Fighter" Passive Ability for purposes of Calculation only.
	public SteelLegionWarrior useAgileFighterEnhancement() {
		return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * 1.11)).buildSLW();
	}
	
	// Deals with the healing portion of the "Agile Fighter" Passive Ability
	public void useAgileFighterHealing(int numSpaces, boolean didUseAbility) {
		// Calculates the healing done by Maximum Health.
		int healing = (int) Math.round(.01 * numSpaces * this.getHealth());
		if (!didUseAbility) {
			// Halves the amount if an Ability was not used.
			healing /= 2;
		}
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
	}
	
	// Deals the Damage from the "Sweep" Ability (Ability 1) to multiple enemies
	public void useSweep(List<Character> enemies) {
		for (Character enemy : enemies) {
			this.attackAOE(enemy, .8); // Attack, AOE, .8x Damage
		}
	}
	// Used for when "Deflection" is active
	public void useDeflectionSweep(List<Character> enemies) {
		for (Character enemy : enemies) {
			this.attackDeflection(enemy, .8, false); // Deflection Attack, AOE, .8x Damage
		}
	}
	
	// Deals the Damage from the "CHARGE!" Ability (Ability 2) to multiple enemies with a primary target
	public void useCharge(List<Character> enemies, Character primary) {
		for (Character enemy : enemies) {
			this.attackAOE(enemy, .5); // Attack, AOE, .5x Damage
		}
		
		// Attacks Primary with bonus effect.
		new SteelLegionWarrior(new CharacterBuilder(this).CriticalChance(this.getCriticalChance()+10).buildSLW()) // Bonus effect
				.attack(primary); // Attack, Targeted, 1x Damage
	}
	// Deals the Damage from the "CHARGE!" Ability (Ability 2) to multiple enemies without a primary target
	public void useCharge(List<Character> enemies) {
		for (Character enemy : enemies) {
			this.attackAOE(enemy, .5); // Attack, AOE, .5x Damage
		}
	}
	// First Version With Primary: Used for when "Deflection" is active
	public void useDeflectionCharge(List<Character> enemies, Character primary) {
		for (Character enemy : enemies) {
			this.attackDeflection(enemy, .5, false); // Deflection Attack, AOE, .5x Damage
		}
		
		// Attacks Primary with bonus effect.
		new SteelLegionWarrior(new CharacterBuilder(this).CriticalChance(this.getCriticalChance()+10).buildSLW()) // Bonus effect
				.attackDeflection(primary); // Deflection Attack, Targeted, 1x Damage
	}
	// Second Version Without Primary: Used for when "Deflection" is active
	public void useDeflectionCharge(List<Character> enemies) {
		for (Character enemy : enemies) {
			this.attackDeflection(enemy, .5, false); // Deflection Attack, AOE, .5x Damage
		}
	}
	
	// Deals the Damage from the "Flip Strike" Ability (Ability 3)
	public void useFlipStrike(Character enemy) {
		this.attack(enemy, 1.5); // Attack, Targeted, 1.5x Damage
	}
	// Used for when "Deflection" is active
	public void useDeflectionFlipStrike(Character enemy) {
		this.attackDeflection(enemy, 1.5); // Deflection Attack, Targeted, 1.5x Damage
	}
	
	
	// Creates a copy of "attack" Character function specifically to be used specifically when "Deflection" is activated
	public void attackDeflection(Character enemy, double scaler, boolean isTargeted, boolean canMiss) {
		// Attack always hits unless it is a Targeted attack and can miss (some targeted attacks cannot miss)
		boolean didHit = true;
		
		if (isTargeted && canMiss) {
			// Finds if the attack landed
			didHit = this.landAttack(enemy);
		}
		
		// If the attack missed
		if (!didHit) {
			System.out.println(this.getName() + " missed " + enemy.getName() + "!");
		}
		// If the attack hits, now calculate Damage
		else {
			// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler, then multiplies it in (using that Armor does not apply)
			double armorEffect;
			armorEffect = calcArmorEffect(enemy, false);
			scaler*=armorEffect;
			
			// Only Targeted attacks can critically hit
			boolean didCrit = false;
			if (isTargeted) {
				// Calculates whether the attack was a Critical Strike
				didCrit = landCrit(enemy);
			}
			
			if (didCrit) {
				scaler *= 2;
				if (this.getCriticalChance()>100) {
					scaler += (this.getCriticalChance() - 100)/100; // This was changed to divide by 100
				}
			}
			
			// Calculates the final damage dealt over the deviation range for the normal portion of the attack
			int normalDamage = this.calcFinalDamage(enemy, this.getDamage(), scaler, didCrit);
			
			// Calculates the bonus electrical damage dealt over the deviation range for the electrical portion of the attack
			int bonusDamage = this.calcFinalDamage(enemy, this.getDamage(), .2 * scaler, didCrit);
			int armorDamage = enemy.getArmor();
			if (armorDamage > this.getDamage() * .3) {
				armorDamage = (int)Math.round(this.getDamage() * .3);
			}
			
			// Calculates the final total Damage with each portion applied
			int damageDealt = normalDamage + bonusDamage + armorDamage;
			
			// Damages the enemy and determines whether enemy died
			this.dealDamage(enemy, damageDealt, AttackType.NONE, didCrit);
		}
	}
	public void attackDeflection(Character enemy, double scaler) {
		this.attackDeflection(enemy, scaler, true, true);
	}
	public void attackDeflection(Character enemy) {
		this.attackDeflection(enemy, 1);
	}
	public void attackDeflection(Character enemy, double scaler, boolean isTargeted) {
		this.attackDeflection(enemy, scaler, isTargeted, true);
	}
	public void attackDeflection(Character enemy, boolean isTargeted) {
		this.attackDeflection(enemy, 1, isTargeted);
	}
	
	public void attackedDeflection(Character enemy) {
		// Calculates the total/average/normal Damage dealt.
		int totalDamage = this.calcFinalDamage(enemy, this.getDamage(), .25, false);
		// Adds the bonus Damage from Armor due to Deflection mechanic
		int damageAdded = this.getArmor();
		if (damageAdded > this.getDamage() * .3) {
			damageAdded = (int) Math.round(this.getDamage() * .3);
		}
		totalDamage += damageAdded;
		
		// Damages the enemy and determines whether enemy died
		this.dealDamage(enemy, totalDamage, AttackType.NONE);
	}
}
