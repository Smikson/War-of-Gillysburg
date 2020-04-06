package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SilentDeathShadow extends Character{
	// These first two methods help set up the Sentinel Sniper subclass.
	public SilentDeathShadow(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls);
	}
	public SilentDeathShadow(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities());
	}
	
	// Deals the Damage from the "Riposte" Passive Ability
	public void useRiposte(Character enemy) {
		this.attack(enemy, 1.5); // Attack, Targeted, 1.5x Damage
	}
	
	// Returns a new Character with improved stats based on the "Law of Stealth" and "A Shadow's Stealth" Passive Abilities for purposes of Calculation only.
	public SilentDeathShadow useStealthEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.5)).buildSDS();
	}
	
	// Returns a new Character with improved stats based on the "It Came From... Behind!" Passive Ability for purposes of Calculation only.
	public SilentDeathShadow useAttackBehindEnhancement() {
		return new CharacterBuilder(this).CriticalChance((int) Math.round(this.getCriticalChance() + 25)).buildSDS();
	}
	
	// Deals the Damage from the "Double Strike" Ability (Ability 2)
	public void useDoubleStrike(Character enemy, boolean usedFromStealth) {
		// THINGS THAT CHANGE FOR ABILITY RIGHT HERE
		double scaler = .75;
		double dodgeDeduction = .1;
		double armorReduction = .1;
		
		if (usedFromStealth) {
			// The First Attack
			
			int reducedToHit = (int)Math.round(enemy.getDodge() * (1 - dodgeDeduction) + enemy.getBlock() * (1 - dodgeDeduction));
			boolean didHit = this.landAttack(enemy, -reducedToHit);
			
			if (didHit) {
				this.attackNoMiss(enemy, scaler); // Attack, Targeted, Cannot Miss
			}
			else {
				this.attack(enemy, scaler); // Attack, Targeted
			}
			
			
			// The second attack
			
			// Second attack is a Targeted attack and can miss
			didHit = this.landAttack(enemy);
			
			// If the attack missed
			if (!didHit) {
				System.out.println(this.getName() + " missed " + enemy.getName() + "!");
			}
			// If the attack hits, now calculate Damage
			else {
				// Calculates the percentage in which the Armor/Armor Piercing affects the overall Damage scaler, then multiplies it in
				int reducedEnemyArmor = (int)Math.round(enemy.getArmor() * (1 - armorReduction));
				double armorEffect = calcArmorEffect(enemy, -reducedEnemyArmor, true);
				scaler*=armorEffect;
				
				// Calculates if the attack critically hits
				boolean didCrit = landCrit(enemy);
				
				if (didCrit) {
					scaler *= 2;
					if (this.getCriticalChance()>100) {
						scaler += (this.getCriticalChance() - 100)/100; // This was changed to divide by 100
					}
				}
				
				// Calculates the final damage dealt over the deviation range
				int damageDealt = this.calcFinalDamage(enemy, this.getDamage(), scaler, didCrit);
				
				// Damages the enemy and determines whether enemy died
				this.dealDamage(enemy, damageDealt, AttackType.TRUE, didCrit);
			}
		}
		else {
			this.attack(enemy, scaler); // Attack, Targeted
			this.attack(enemy, scaler); // Attack, Targeted
		}
	}
	
	// Deals the Damage from the "Backstab" Ability (Ability 3)
	public void useBackStab(Character enemy, boolean usedFromStealth) {
		if (usedFromStealth) {
			new CharacterBuilder(this).CriticalChance(this.getCriticalChance() + 50).buildSDS().attack(enemy, 1.2); // Attack, Targeted, 1.2x Damage
		}
		else {
			this.attack(enemy, 1.2); // Attack, Targeted, 1.2x Damage
		}
	}
	
	// Deals the Damage from the "Blink" Ability (Ability 4)
	public void useBlink(Character enemy) {
		this.attack(enemy, .75); // Attack, Targeted, .75x Damage
	}
	
	// Deals the Damage from the "Execute" ULTIMATE Ability
	public void useExecute(Character enemy, boolean usedFromStealth) {
		// Calculates the total Damage dealt by missing Health, then casting it.
		int damageDealt = (int)Math.round((enemy.getHealth() - enemy.getCurrentHealth()) * .4);
		if (damageDealt > this.getDamage() * 10) {
			damageDealt = this.getDamage() * 10;
		}
		
		if (usedFromStealth) {
			this.dealDamage(enemy, damageDealt, AttackType.TRUE); // Deals the damage to the enemy
		}
		
		else {
			// Calculates the chance for the attack to land
			boolean didHit = this.landAttack(enemy);
			
			// If the attack missed
			if (!didHit) {
				System.out.println(this.getName() + " missed " + enemy.getName() + "!");
			}
			// If the attack hits, it deals the damage
			else {
				this.dealDamage(enemy, damageDealt, AttackType.TRUE); // Deals the damage to the enemy
			}
		}
	}
}
