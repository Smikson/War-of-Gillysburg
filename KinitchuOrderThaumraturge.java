package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class KinitchuOrderThaumraturge extends Character{
	// These first two methods help set up the Kinitchu Order Dragon Fire Wizard subclass.
	public KinitchuOrderThaumraturge(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls, Type type) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, type);
	}
	public KinitchuOrderThaumraturge(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getType());
	}
	
	// Deals the Damage from the "Ice Barrier" Passive Ability
	public void useIceBarrier(List<Character> enemies) {
		/*
		for (Character enemy : enemies) {
			
			// Calculates the total/average/normal Damage dealt by maximum Health, then casting it.
			int damageDealt = (int)Math.round(enemy.getHealth() * .1);
			if (damageDealt > this.getDamage() * 6) {
				damageDealt = this.getDamage() * 6;
			}
			
			// Damages the enemy and determines whether enemy died
			this.dealDamage(enemy, damageDealt, AttackType.TRUE); // Deals Damage
		}
		*/
	}
	
	// Returns a new Character with improved stats based on the "Cold Execution" Passive Ability for purposes of Calculation only.
	public KinitchuOrderThaumraturge useColdExecutionEnhancement(boolean isSelfCC) {
		if (isSelfCC) {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.2)).Accuracy((int) Math.round(this.getAccuracy() * 1.05)).buildKOT();
		}
		else {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.1)).Accuracy((int) Math.round(this.getAccuracy() * 1.05)).buildKOT();
		}
	}
	
	// Deals the Damage from the "Slick, Hard Ice Armor Spikes"
	public void useIceSpikes(Character enemy, boolean didDodge, boolean isIceArmored) {
		/*
		if (isIceArmored) {
			this.attackNoMiss(enemy, .1 * 3); // Attack, Targeted, Cannot Miss, .1*3x Damage
		}
		else if (didDodge) {
			this.attackNoMiss(enemy, .1 * 2); // Attack, Targeted, Cannot Miss, .1*2x Damage
		}
		else {
			this.attackNoMiss(enemy, .1); // Attack, Targeted, Cannot Miss, .1x Damage
		}
		*/
	}
	
	// Deals the Damage from the "Ice Shard" Ability (Ability 1)
	public void useIceShard(Character enemy) {
		//this.attack(enemy, 1.5); // Attack, Targeted, 1.5x Damage
	}
	
	// Deals the Damage from the empowered basic attack in the "Ice Armor" Ability (Ability 2)
	public void useIceArmorAttack(Character enemy) {
		//this.attackNoMiss(enemy, 1.1); // Attack, Targeted, Cannot Miss, 1.1x Damage
	}
	
	// Deals the Damage from the "Ice Cone" Ability (Ability 3) to multiple enemies
	public void useIceCone(List<Character> enemies) {
		/*
		for (Character enemy : enemies) {
			this.attackAOE(enemy, .75); // Attack, AOE, .75x Damage
		}
		*/
	}
	
	// Deals the Damage from the "Blizzard" Ability (Ability 4) to 1 enemy
	public void useBlizzardDamage(Character enemy) {
		//this.attackAOE(enemy, .1); // Attack, AOE, .1x Damage
	}
	// Deals with the Healing from the "Blizzard" Ability (Ability 4) to 1 ally
	public void useBlizzardHealing(Character ally, boolean isYou) {
		if (isYou) {
			// Calculates the amount of healing based on Damage
			int healing = (int) Math.round(.05 * 2 * this.getDamage());
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = this.restoreHealth(healing);
			System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
		}
		else {
			// Calculates the amount of healing based on Damage
			int healing = (int) Math.round(.05 * ally.getDamage());
			
			// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
			healing = ally.restoreHealth(healing);
			System.out.println(ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth());
		}
	}
	// Returns a new Character with improved stats based on the "Blizzard" Ability (Ability 4) for purposes of Calculation only.
	public KinitchuOrderThaumraturge useBlizzardEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.05)).buildKOT();
	}
	
	// Deals the Damage from the "Freeze Frame!" ULTIMATE Ability
	public void useFreezeFrame(List<Character> enemies) {
		/*
		for (Character enemy : enemies) {
			this.attackAOE(enemy, 2); // Attack, AOE, 2x Damage
		}
		*/
	}
}
