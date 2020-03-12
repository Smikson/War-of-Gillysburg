package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SilentDeathHunter extends Character{
	// These first two methods help set up the Silent Death Hunter subclass.
	public SilentDeathHunter(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
	}
	public SilentDeathHunter(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getAttackType());
	}
	
	// Deals the Damage from the "Keen Hearing" Passive Ability
	public void useKeenHearing(Character enemy, int numOutside) {
		double accuracyReduction = 0;
		if (numOutside > 3 ) {
			accuracyReduction = .20;
		}
		else {
			accuracyReduction = .05 * numOutside;
		}
		
		new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * (1 - accuracyReduction))).buildSDH().attack(enemy, 1); // Attack, Targeted, 1x Damage
	}
	
	// Returns a new Character with improved stats based on the "Law of Stealth" and "Camouflage" Passive Abilities for purposes of Calculation only.
	public SilentDeathHunter useStealthEnhancement(boolean usingCamouflage) {
		if (usingCamouflage) {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.3)).CriticalChance(this.getCriticalChance() + 10).buildSDH();
		}
		else {
			return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.25)).buildSDH();
		}
	}
	
	// Returns a new Character with improved stats based on the "Big Game Hunter" Passive Ability for purposes of Calculation only.
	public SilentDeathHunter useBigGameEnchancement(boolean isDoubled) {
		if (isDoubled) {
			return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() *1.2)).Damage((int) Math.round(this.getDamage() *1.2)).buildSDH();
		}
		else {
			return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() *1.1)).Damage((int) Math.round(this.getDamage() *1.1)).buildSDH();
		}
	}
	
	// Deals the Damage from the "Snare Trap" Ability (Ability 1)
	public void useSnareTrap(Character enemy, boolean usedAsUlt) {
		if (usedAsUlt) {
			this.attack(enemy, 2.5, true, false); // Attack, Targeted, Cannot miss, 2.5x Damage
		}
		else {
			this.attack(enemy, 2, true, false); // Attack, Targeted, Cannot Miss, 2x Damage
		}
	}
	
	// Deals the Damage from the "Blade Trap" Ability (Ability 2)
	public void useBladeTrap(List<Character> enemies) {
		for (Character enemy : enemies) {
			this.attack(enemy, 1.2, false); // Attack, AOE, 1.2x Damage
		}
	}
	
	// Deals the Damage from the "Floor Trap" Ability (Ability 3)
	public void useFloorTrap(List<Character> enemies) {
		for (Character enemy : enemies) {
			
			// Calculates the total/average/normal Damage dealt by maximum Health, then casting it.
			int damageDealt = (int)Math.round(enemy.getHealth() * .1);
			if (damageDealt > this.getDamage() * 5) {
				damageDealt = this.getDamage() * 5;
			}
			
			this.dealDamage(enemy, damageDealt); // Deals Damage
		}
	}

	// Deals the Damage from the "Iron Gauntlets" Ability (Ability 4)
	public void useIronGauntlets(Character enemy) {
		this.attack(enemy, 1.5); // Attack, Targeted, 1.5x Damage
	}
}
