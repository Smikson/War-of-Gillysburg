package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class KinitchuOrderDragonFireWizard extends Character{
	// These first two methods help set up the Kinitchu Order Dragon Fire Wizard subclass.
	public KinitchuOrderDragonFireWizard(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls);
	}
	public KinitchuOrderDragonFireWizard(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities());
	}
	
	// Returns a new Character with improved stats based on the "Dragon's Rage" Passive Ability for purposes of Calculation only.
	public KinitchuOrderDragonFireWizard useDragonsRageEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.5)).buildKODFW();
	}
	
	// Deals the Damage from the "Scorched Earth" Fire-Terrain Passive Ability
	public void useScorchedEarth(List<Character> enemies) {
		for (Character enemy : enemies) {
			this.attackAOE(enemy, .1); // Attack, AOE, .1x Damage
		}
	}
	// Returns a new Character with improved stats based on the "Scorched Earth" Passive Ability for purposes of Calculation only.
	public KinitchuOrderDragonFireWizard useScorchedEarthEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.05)).buildKODFW();
	}
	
	// Deals the Damage from the "Fireball" Ability (Ability 1)
	public void useFireball(Character enemy) {
		this.attack(enemy, 1.5); // Attack, Targeted, 1.5x Damage
	}
	
	// Deals the Damage from the "Ring of Fire" Ability (Ability 2) to multiple enemies
	public void useRingOfFire(List<Character> innerEnemies, List<Character> outerEnemies) {
		for (Character enemy : innerEnemies) {
			this.attackAOE(enemy, 1); // Attack, AOE, 1x Damage
		}
		for (Character enemy : outerEnemies) {
			this.attackAOE(enemy, .5); // Attack, AOE, .5x Damage
		}
	}
	
	// Deals the Basic Attack Damage from the "Summon: Phoenix" Ability (Ability 3)
	public void usePhoenixBasicAttack(Character enemy) {
		this.attack(enemy, 1); // Attack, Targeted, 1x Damage
	}
	
	// Details for the ULTIMATE, "Summon: Dragon", are in the separate class: Dragon
}
