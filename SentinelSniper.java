package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SentinelSniper extends Character {
	
	// These first two methods help set up the Sentinel Sniper subclass.
	public SentinelSniper(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, LinkedList<Type> types) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, types);
	}
	public SentinelSniper(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getBaseDmgType(), ori.getResistances(), ori.getVulnerabilities(), ori.getTypes());
	}
	
	// Deals the Damage from the "Keen Eyes" Passive Ability
	public void useKeenEyes(Character enemy) {
		//this.attack(enemy, .5); // Attack, Targeted, .5x Damage
	}
	
	// Returns a new Character with improved stats based on the "Sniper's Nest" Passive Ability for purposes of Calculation only.
	public SentinelSniper useSnipersNestEnhancement() {
		return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * 1.10))
				.ArmorPiercing((int) Math.round(this.getArmorPiercing() * 1.10))
				.Damage((int) Math.round(this.getDamage() * 1.25))
				.CriticalChance((int) Math.round(this.getCriticalChance() * 1.25))
				.buildSS();
	}
	
	// Deals with the healing portion of the "Keeping It Cool" Passive Ability
	public void useKeepingItCoolHealing() {
		int healing = (int) Math.round(.1 * (this.getHealth() - this.getCurrentHealth()));
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		this.restoreHealth(healing);
	}
	// Returns a new Character with improved stats based on the "Keeping It Cool" Passive Ability for purposes of Calculation only.
	public SentinelSniper useKeepingItCoolEnhancement() {
		return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * 1.15)).buildSS();
	}
	
	// Deals the Damage from the "Snipe" Ability (Ability 1)
	public void useSnipe(Character enemy) {
		//this.attack(enemy, 2); // Attack, Targeted, 2x Damage
	}
	
	public void useTrueAim(Character enemy) {
		//this.attackNoMiss(enemy, .75); // Attack, Targeted, Cannot Miss, .75x Damage
	}
	
	// Deals the Damage from the "Long Shot" Ability (Ability 3)
	public void useLongShot(Character enemy, boolean useAccuracyDeduction) {
		if (useAccuracyDeduction) {
			//new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * .75)).buildSS().attack(enemy, 1); // Attack, Targeted, 1x Damage
		}
		else {
			//this.attack(enemy, 1); // Attack, Targeted, 1x Damage
		}
	}
	
	// Deals the Damage from the "Crippling Shot" Ability (Ability 4)
	public void useCripplingShot(Character enemy) {
		//this.attack(enemy, .5); // Attack, Targeted, .5x Damage
	}
	
	// Deals the Damage from the "Headshot" ULTIMATE Ability
	public void useHeadshot(Character enemy) {
		//new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * 1.5)).buildSS().attack(enemy, 4); // Attack, Targeted, 4x Damage
	}
}
