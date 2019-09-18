package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SentinelSpecialist extends Character{
	
	// These first two methods help set up the Sentinel Specialist subclass.
	public SentinelSpecialist(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
	}
	public SentinelSpecialist(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getAttackType());
	}
	
	// Returns a new Character with improved stats based on the "Empowered Arrows" Passive Ability for purposes of Calculation only.
	public SentinelSpecialist useEmpoweredArrowsEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.25)).buildSSPL();
	}
	
	// Returns a new Character with improved stats based on the "Multi-Purposed" Passive Ability for purposes of Calculation only.
	public SentinelSpecialist useMultiPurposedEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.1)).buildSSPL();
	}
	
	// Deals the Damage from the "Flaming Arrow" Ability (Ability 1)
	public String useFlamingArrow(Character enemy) {
		return this.attack(enemy, 1); // Attack, Targeted, 1x Damage
	}
	// Deals the Damage from the burn effect of the "Flaming Arrow" Ability
	public String useFlamingArrowBurn(Character enemy) {
		return this.attack(enemy, .2, false); // Attack, AOE, .2x Damage
	}
	
	// Deals the Damage from the "Frozen Arrow" Ability (Ability 2)
	public String useFrozenArrow(Character enemy) {
		return this.attack(enemy, .8); // Attack, Targeted, .8x Damage
	}
	
	// Deals the Damage from the "Exploding Arrow" Ability (Ability 3) to multiple enemies with a primary target
	public String useExplodingArrow(Character enemy, List<Character> enemies) {
		String ret;
		ret = this.attack(enemy, .5) + "\n"; // Attack, Targeted, .5x Damage
		
		for (Character enemyHitByAOE:enemies) {
			ret += this.attack(enemyHitByAOE, .75, false) + "\n"; // Attack, AOE, .75x Damage
		}
		
		return ret;
	}
	
	// Deals the Damage from the "Penetration Arrow" Ability (Ability 4) to multiple enemies
	public String usePenetrationArrow(List<Character> enemies) {
		String ret = "";
		for (Character enemy:enemies) {
			ret += this.attack(enemy, .75) + "\n"; // Attack AOE, .75x Damage
		}
		
		return ret;
	}
	// Deals the Damage from the "Penetration Arrow" Ability when used with the "Empowered Arrows" Passive Ability
	public String usePenetrationArrowEmpowered(List<Character> enemies) {
		String ret = "";
		for (Character enemy:enemies) {
			ret += this.attack(enemy, .75, true, false, false) + "\n"; // Attack, Targeted, Cannot Miss, Ignores Armor, .75x Damage
		}
		return ret;
	}
	
	// Deals the Damage from the "Black Arrow" ULTIMATE Ability
	public String useBlackArrow(Character enemy) {
		return this.attack(enemy, 3, true, true, false); // Attack, Targeted, Ignores Armor, 3x Damage
	}
}
