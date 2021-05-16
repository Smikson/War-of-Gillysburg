package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character.Type;

// The Sentinel Arc Archer itself:
public class SentinelArcArcher extends Character {
	// Enumerates the names of the abilities so Cooldown and use functions can be called
	public static enum AbilityNames {
		QuickShot, Flawlessness, CombatRoll, AttackSpeed, MultiShot, DoubleShredder, Concentrate, FlipTrickShot, RainOfArrows
	}
	
	// Passive Abilities
	private QuickShot QuickShot; // Unique Passive Ability (UPA)
	private Flawlessness Flawlessness;
	private CombatRoll CombatRoll;
	private AttackSpeed AttackSpeed;
	
	// Base Abilities
	private MultiShot MultiShot;
	private DoubleShredder DoubleShredder;
	private Concentrate Concentrate;
	private FlipTrickShot FlipTrickShot;
	private RainOfArrows RainOfArrows;
	
	// Maps all Abilities so all Cooldowns can be reduced at once
	private HashMap<SentinelArcArcher.AbilityNames, Ability> abilities;
	
	
	// These first two methods help set up the Sentinel Arc Archer subclass.
	public SentinelArcArcher(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, LinkedList<Type> types, int qsRank, int fRank, int crRank, int asRank, int msRank, int dsRank, int cRank, int ftsRank, int roaRank) {
		// Calls the super constructor to create the Character
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, types);
		
		// Initializes all Abilities according to their specifications.
		this.QuickShot = new QuickShot(this, qsRank);
		this.Flawlessness = new Flawlessness(this, fRank);
		this.CombatRoll = new CombatRoll(this, crRank);
		this.AttackSpeed = new AttackSpeed(this, asRank);
		this.MultiShot = new MultiShot(this, msRank);
		this.DoubleShredder = new DoubleShredder(this, dsRank);
		this.Concentrate = new Concentrate(this, cRank);
		this.FlipTrickShot = new FlipTrickShot(this, ftsRank);
		this.RainOfArrows = new RainOfArrows(this, roaRank);
		
		// Add Abilities to a list for Cooldown and usage purposes
		this.abilities = new HashMap<>();
		this.abilities.put(SentinelArcArcher.AbilityNames.QuickShot, this.QuickShot);
		this.abilities.put(SentinelArcArcher.AbilityNames.Flawlessness, this.Flawlessness);
		this.abilities.put(SentinelArcArcher.AbilityNames.CombatRoll, this.CombatRoll);
		this.abilities.put(SentinelArcArcher.AbilityNames.AttackSpeed, this.AttackSpeed);
		this.abilities.put(SentinelArcArcher.AbilityNames.MultiShot, this.MultiShot);
		this.abilities.put(SentinelArcArcher.AbilityNames.DoubleShredder, this.DoubleShredder);
		this.abilities.put(SentinelArcArcher.AbilityNames.Concentrate, this.Concentrate);
		this.abilities.put(SentinelArcArcher.AbilityNames.FlipTrickShot, this.FlipTrickShot);
		this.abilities.put(SentinelArcArcher.AbilityNames.RainOfArrows, this.RainOfArrows);
		
		// Add all other new commands for Abilities (the usual check for rank > 0 is sufficient)
		this.addCommand(new AbilityCommand(this.MultiShot));
		this.addCommand(new AbilityCommand(this.DoubleShredder));
		this.addCommand(new AbilityCommand(this.Concentrate));
		this.addCommand(new AbilityCommand(this.FlipTrickShot));
		this.addCommand(new AbilityCommand(this.RainOfArrows));
	}
	public SentinelArcArcher(Character copy, int qsRank, int fRank, int crRank, int asRank, int msRank, int dsRank, int cRank, int ftsRank, int roaRank) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getBaseDmgType(), copy.getResistances(), copy.getVulnerabilities(), copy.getTypes(), qsRank, fRank, crRank, asRank, msRank, dsRank, cRank, ftsRank, roaRank);
	}
	public SentinelArcArcher(SentinelArcArcher copy) {
		this(copy, copy.getAbilityRank(SentinelArcArcher.AbilityNames.QuickShot), copy.getAbilityRank(SentinelArcArcher.AbilityNames.Flawlessness), copy.getAbilityRank(SentinelArcArcher.AbilityNames.CombatRoll), copy.getAbilityRank(SentinelArcArcher.AbilityNames.AttackSpeed), copy.getAbilityRank(SentinelArcArcher.AbilityNames.MultiShot), copy.getAbilityRank(SentinelArcArcher.AbilityNames.DoubleShredder), copy.getAbilityRank(SentinelArcArcher.AbilityNames.Concentrate), copy.getAbilityRank(SentinelArcArcher.AbilityNames.FlipTrickShot), copy.getAbilityRank(SentinelArcArcher.AbilityNames.RainOfArrows));
	}
	
	
	// Functions for interaction between Abilities:
	// Functions to use an Ability
	public void useAbility(SentinelArcArcher.AbilityNames name, int version) {
		Ability chosen = this.abilities.get(name);
		chosen.use(version);
	}
	public void useAbility(SentinelArcArcher.AbilityNames name) {
		this.useAbility(name, 1);
	}
	
	// Function to set an ability's Cooldown
	public void setAbilityCD(SentinelArcArcher.AbilityNames name, int turnsRemaining) {
		Ability chosen = this.abilities.get(name);
		chosen.setTurnsRemaining(turnsRemaining);
	}
	
	// Function to get the rank of an Ability
	public int getAbilityRank(SentinelArcArcher.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.rank();
	}
	
	// Function to get the duration of an Ability
	public int getAbilityDuration(SentinelArcArcher.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.getDuration();
	}
	
	// Function to get whether or not an Ability is active
	public boolean isAbilityActive(SentinelArcArcher.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.isActive();
	}
}
