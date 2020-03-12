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
	public void useFlamingArrow(Character enemy) {
		this.attack(enemy, 1); // Attack, Targeted, 1x Damage
	}
	// Deals the Damage from the burn effect of the "Flaming Arrow" Ability
	public void useFlamingArrowBurn(Character enemy) {
		this.attack(enemy, .2, false); // Attack, AOE, .2x Damage
	}
	
	// Deals the Damage from the "Frozen Arrow" Ability (Ability 2)
	public void useFrozenArrow(Character enemy) {
		this.attack(enemy, .8); // Attack, Targeted, .8x Damage
	}
	
	// Deals the Damage from the "Exploding Arrow" Ability (Ability 3) to multiple enemies with a primary target
	public void useExplodingArrow(Character enemy, List<Character> enemies) {
		this.attack(enemy, .5); // Attack, Targeted, .5x Damage
		
		for (Character enemyHitByAOE:enemies) {
			this.attack(enemyHitByAOE, .75, false); // Attack, AOE, .75x Damage
		}
	}
	
	// Deals the Damage from the "Penetration Arrow" Ability (Ability 4) to multiple enemies
	public void usePenetrationArrow(List<Character> enemies) {
		for (Character enemy:enemies) {
			this.attack(enemy, .75); // Attack AOE, .75x Damage
		}
	}
	// Deals the Damage from the "Penetration Arrow" Ability when used with the "Empowered Arrows" Passive Ability
	public void usePenetrationArrowEmpowered(List<Character> enemies) {
		for (Character enemy:enemies) {
			this.attack(enemy, .75, true, false, false); // Attack, Targeted, Cannot Miss, Ignores Armor, .75x Damage
		}
	}
	
	// Deals the Damage from the "Black Arrow" ULTIMATE Ability
	public void useBlackArrow(Character enemy) {
		this.attack(enemy, 3, true, true, false); // Attack, Targeted, Ignores Armor, 3x Damage
	}
}
