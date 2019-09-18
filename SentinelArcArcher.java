package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SentinelArcArcher extends Character{
	// These first two methods help set up the Sentinel Arc Archer subclass.
	public SentinelArcArcher(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
	}
	public SentinelArcArcher(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getAttackType());
	}
	
	// Deals the Damage from the "Quick Shot" Passive Ability
	public String useQuickShot(Character enemy) {
		return this.attack(enemy, .75); // Attack, Targeted, .75x Damage
	}
	
	// Returns a new Character with improved stats based on the "Combat Roll" Passive Ability for purposes of Calculation only.
	public SentinelArcArcher useCombatRollEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.1)).buildSAA();
	}
	
	// Returns a new Character with improved stats based on the "Attack Speed" Passive Ability during the bonus turn for purposes of Calculation only.
	public SentinelArcArcher useAttackSpeedBonusTurnEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * .5)).buildSAA();
	}
	
	// Deals the Damage from the "Multi-Shot" Ability (Ability 1) to multiple enemies
	public String useMultiShot(List<Character> enemies) {
		String ret = "";
		
		for (Character enemy:enemies) {
			ret += this.attack(enemy, .5, false) + "\n"; // Attack, AOE, .5x Damage
		}
		
		return ret;
	}
	
	// Deals the Damage from the first arrow of the "Double Shredder" Ability (Ability 2)
	public String useDoubleShredder1(Character enemy) {
		String ret = "";
		
		// THINGS THAT CHANGE FOR ABILITY RIGHT HERE
		double scaler = .5;
		
		
		// The First Arrow
		
		boolean didHit = this.landAttack(enemy);
		
		// If the attack (Arrow 1) missed
		if (!didHit) {
			ret += this.getName() + " missed the first arrow shot at " + enemy.getName() + "!\n";
			
			// The second arrow strikes with 0% Critical Chance
			ret += new CharacterBuilder(this).CriticalChance(0).buildSAA().attack(enemy, 1); // Attack, Targeted, 1x Damage (0% crit)
			
			return ret;
		}
		// If the attack (Arrow 1) hits, now calculate Damage
		else {
			// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler, then multiplies it in
			double armorEffect;
			armorEffect = calcArmorEffect(enemy, true); // Armor Applies
			scaler*=armorEffect;
			
			// Find if attack critically hit (will affect both)
			boolean didCrit = landCrit(enemy);
			
			if (didCrit) {
				scaler *= 2;
				if (this.getCriticalChance()>100) {
					scaler += (this.getCriticalChance() - 100)/100; // This was changed to divide by 100
				}
			}
			
			// Calculates the final damage dealt over the deviation range
			int damageDealt = this.calcFinalDamage(enemy, this.getDamage(), scaler, didCrit, this.getAttackType());
			
			// Damages the enemy and determines whether enemy died
			ret += this.dealDamage(enemy, damageDealt, didCrit) + "\n";
			
			
			// The second arrow strikes with a 60% chance to ignore all armor.
			scaler = 1;
			
			Dice percent = new Dice(100);
			// Attack ignores all armor, crits if first crit.
			if (percent.roll() <= 60) {
				// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler, then multiplies it in
				armorEffect = calcArmorEffect(enemy, false); // Ignores Armor
				scaler*=armorEffect;
				
				if (didCrit) {
					scaler *= 2;
					if (this.getCriticalChance()>100) {
						scaler += (this.getCriticalChance() - 100)/100; // This was changed to divide by 100
					}
				}
				
				// Calculates the final damage dealt over the deviation range
				damageDealt = this.calcFinalDamage(enemy, this.getDamage(), scaler, didCrit, this.getAttackType());
				
				// Damages the enemy and determines whether enemy died
				ret += this.dealDamage(enemy, damageDealt, didCrit);
			}
			// Otherwise, same thing but attack does not ignore all armor
			else {
				// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler, then multiplies it in
				armorEffect = calcArmorEffect(enemy, true); // Armor Applies
				scaler*=armorEffect;
				
				if (didCrit) {
					scaler *= 2;
					if (this.getCriticalChance()>100) {
						scaler += (this.getCriticalChance() - 100)/100; // This was changed to divide by 100
					}
				}
				
				// Calculates the final damage dealt over the deviation range
				damageDealt = this.calcFinalDamage(enemy, this.getDamage(), scaler, didCrit, this.getAttackType());
				
				// Damages the enemy and determines whether enemy died
				ret += this.dealDamage(enemy, damageDealt, didCrit);
			}
			
			return ret;
		}
	
	}
	
	// Returns a new Character with improved stats based on the "Concentration" Ability (Ability 3) for purposes of Calculation only.
	public SentinelArcArcher useConcentrationEnhancement(boolean usingQuickShot) {
		if (usingQuickShot) {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * .5)).buildSAA();
		}
		else {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.25)).buildSAA();
		}
	}
	
	// Deals the Damage from the "Flip Trick Shot" Ability (Ability 4)
	public String useFlipTrickShot(Character enemy) {
		return this.attack(enemy, 1.2); // Attack, Targeted, 1.2x Damage
	}
	
	// Deals the Damage from the "Rain of Arrows" ULTIMATE Ability to multiple enemies
	public String useRainOfArrows(List<Character> enemies) {
		String ret = "";
		
		for (int x = 0; x < enemies.size() - 1; x++) {
			Character enemy = enemies.get(x);
			ret += this.attack(enemy, .5, false) + "\n"; // Attack, AOE, .5x Damage
		}
		ret += this.attack(enemies.get(enemies.size()-1), .75, false); // Attack, AOE, .75x Damage
		
		return ret;
	}
}
