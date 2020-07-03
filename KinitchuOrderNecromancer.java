package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;

public class KinitchuOrderNecromancer extends Character{
	// These first two methods help set up the Kinitchu Order Dragon Fire Wizard subclass.
	public KinitchuOrderNecromancer(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls, Type type) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, type);
	}
	public KinitchuOrderNecromancer(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities(), ori.getType());
	}
	
	
	// Returns a new Character with improved stats based on the "Absorb Energy" Passive Ability for purposes of Calculation only.
	public KinitchuOrderNecromancer useAbsorbEnergyEnhancement(int numUndead) {
		// Base 10% increase
		double increase = .1/numUndead;
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * (1 + increase))).buildKON();
	}
	
	// Returns a new Character with improved stats based on the "Superior Strategy" Passive Ability for purposes of Calculation only.
	public KinitchuOrderNecromancer useSuperiorStrategyEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.1))
				.CriticalChance((int) Math.round(this.getCriticalChance() * 1.1))
				.Accuracy((int) Math.round(this.getAccuracy() * 1.1))
				.buildKON();
	}
}
