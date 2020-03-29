package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class SilentDeathPoisonSpecialist extends Character {
	// These first two methods help set up the Silent Death Poison Specialist subclass.
	public SilentDeathPoisonSpecialist(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls) {
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls);
	}
	public SilentDeathPoisonSpecialist(Character ori) {
		super(ori.getName(), ori.getLevel(), ori.getHealth(), ori.getDamage(), ori.getArmor(), ori.getArmorPiercing(), ori.getAccuracy(), ori.getDodge(), ori.getBlock(), ori.getCriticalChance(), ori.getSpeed(), ori.getAttackSpeed(), ori.getRange(), ori.getThreat(), ori.getTacticalThreat(), ori.getSTDdown(), ori.getSTDup(), ori.getResistances(), ori.getVulnerabilities());
	}
	
	// Returns a new Character with improved stats based on the "Law of Stealth" Passive Ability for purposes of Calculation only.
	public SilentDeathPoisonSpecialist useStealthEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.25)).buildSDPS();
	}
	
	// Returns a new Character with improved stats based on the "Advantage" Passive Ability for purposes of Calculation only.
	public SilentDeathPoisonSpecialist useAdvantageEnhancement() {
		return new CharacterBuilder(this).Accuracy((int) Math.round(this.getAccuracy() * 1.1)).CriticalChance(this.getCriticalChance() + 5).buildSDPS();
	}
	
	// Deals the Damage from the "Venomous Strike" Ability (Ability 1)
	public void useVenomousStrike(Character enemy) {
		this.attack(enemy, 1.5); // Attack, Targeted, 1.5x Damage
	}
	
	// Returns a new Character with improved stats based on the "Douse Blade" Ability (Ability 2) for Calculation only.
	public SilentDeathPoisonSpecialist useDouseBladeEnhancement() {
		return new CharacterBuilder(this).Damage((int) Math.round(this.getDamage() * 1.3)).buildSDPS();
	}
	
	// Deals the Damage from the "Poison Dart" Ability (Ability 2)
	public void usePoisonDart(Character enemy) {
		this.attack(enemy, 1.2); // Attack, Targeted, 1.2x Damage
	}
	
	// Deals the Damage from the "Poisonous Cloud" Ability (Ability 3)
	public void usePoisonousCloudHit(List<Character> enemies) {
		for (Character enemy : enemies) {
			
			// Calculates the total/average/normal Damage dealt by multiplying the Damage statistic by the scaler, then casting it.
			int damageDealt = (int)Math.round(enemy.getHealth() * .05);
			if (damageDealt > this.getDamage() * 3) {
				damageDealt = this.getDamage() * 3;
			}
			
			this.dealDamage(enemy, damageDealt, AttackType.NONE); // Deals Damage
		}
	}
	public void usePoisonousCloudStay(List<Character> enemies) {
		for (Character enemy : enemies) {
			
			// Calculates the total/average/normal Damage dealt by multiplying the Damage statistic by the scaler, then casting it.
			int damageDealt = (int)Math.round(enemy.getHealth() * .07);
			if (damageDealt > this.getDamage() * 3) {
				damageDealt = this.getDamage() * 3;
			}
			
			this.dealDamage(enemy, damageDealt, AttackType.NONE); // Deals Damage
		}
	}
	
	// Deals the Damage from the "Explosive Poison!" ULTIMATE Ability
	public void useExplosivePoison(List<Character> enemies, List<Integer> stacks, List<Integer> rounds) {
		for (int x = 0; x < enemies.size(); x++) {
			double scaler = stacks.get(x) * 1 + rounds.get(x) * .5;
			
			this.attackAOE(enemies.get(x), scaler); // Attack, AOE, Damage depends on stacks and rounds on enemies.
		}
	}
}
