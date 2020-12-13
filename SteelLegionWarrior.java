package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;

public class SteelLegionWarrior extends Character {
	// Enumerates the names of the abilities so Cooldown and use functions can be called
	public static enum AbilityNames {
		VengeanceStrike, SwordplayProwess, WarriorsMight, AgileFighter, Sweep, Charge, FlipStrike, IntimidatingShout, Deflection
	}
	
	// Passive Abilities
	private VengeanceStrike VengeanceStrike; // Unique Passive Ability (UPA)
	private SwordplayProwess SwordplayProwess;
	private WarriorsMight WarriorsMight;
	private AgileFighter AgileFighter;  // Add initialization?
	
	// Base Abilities
	private Sweep Sweep;
	private Charge Charge;
	private FlipStrike FlipStrike;
	private IntimidatingShout IntimidatingShout;
	private Deflection Deflection;
	
	// Maps all Abilities so all Cooldowns can be reduced at once
	private HashMap<SteelLegionWarrior.AbilityNames, Ability> abilities;
	
	// These first two methods help set up the Steel Legion Warrior subclass.
	public SteelLegionWarrior(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, Type type, int vsRank, int spRank, int wmRank, int afRank, int sweepRank, int chargeRank, int fsRank, int isRank, int deflectRank) {
		// Calls the super constructor to create the Character, then initializes all Abilities according to their specifications.
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, type);
		this.VengeanceStrike = new VengeanceStrike(this, vsRank);
		this.SwordplayProwess = new SwordplayProwess(this, spRank);
		this.WarriorsMight = new WarriorsMight(this, wmRank);
		this.AgileFighter = new AgileFighter(this, afRank);
		this.Sweep = new Sweep(this, sweepRank);
		this.Charge = new Charge(this, chargeRank);
		this.FlipStrike = new FlipStrike(this, fsRank);
		this.IntimidatingShout = new IntimidatingShout(this, isRank);
		this.Deflection = new Deflection(this, deflectRank);
		
		// Add Abilities to a list for Cooldown purposes
		this.abilities = new HashMap<>();
		this.abilities.put(SteelLegionWarrior.AbilityNames.VengeanceStrike, this.VengeanceStrike);
		this.abilities.put(SteelLegionWarrior.AbilityNames.SwordplayProwess, this.SwordplayProwess);
		this.abilities.put(SteelLegionWarrior.AbilityNames.WarriorsMight, this.WarriorsMight);
		this.abilities.put(SteelLegionWarrior.AbilityNames.AgileFighter, this.AgileFighter);
		this.abilities.put(SteelLegionWarrior.AbilityNames.Sweep, this.Sweep);
		this.abilities.put(SteelLegionWarrior.AbilityNames.Charge, this.Charge);
		this.abilities.put(SteelLegionWarrior.AbilityNames.FlipStrike, this.FlipStrike);
		this.abilities.put(SteelLegionWarrior.AbilityNames.IntimidatingShout, this.IntimidatingShout);
		this.abilities.put(SteelLegionWarrior.AbilityNames.Deflection, this.Deflection);
		
		// Add new commands for Abilities
		if (this.Sweep.rank() > 0) {
			this.addCommand(new AbilityCommand(this.Sweep));
		}
		if (this.Charge.rank() > 0) {
			this.addCommand(new AbilityCommand(this.Charge));
		}
		if (this.FlipStrike.rank() > 0) {
			this.addCommand(new AbilityCommand(this.FlipStrike));
		}
		if (this.IntimidatingShout.rank() > 0) {
			this.addCommand(new AbilityCommand(this.IntimidatingShout));
		}
		if (this.Deflection.rank() > 0) {
			this.addCommand(new AbilityCommand(this.Deflection));
		}
		
		// Finish creating the Deflection Ability (needed Intimidating Shout to be created)
		this.Deflection.setDuration();
	}
	public SteelLegionWarrior(Character copy, int vsRank, int spRank, int wmRank, int afRank, int sweepRank, int chargeRank, int fsRank, int isRank, int deflectRank) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getBaseDmgType(), copy.getResistances(), copy.getVulnerabilities(), copy.getType(), vsRank, spRank, wmRank, afRank, sweepRank, chargeRank, fsRank, isRank, deflectRank);
	}
	public SteelLegionWarrior(SteelLegionWarrior copy) {
		this(copy, copy.getAbilityRank(SteelLegionWarrior.AbilityNames.VengeanceStrike), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.SwordplayProwess), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.WarriorsMight), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.AgileFighter), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.Sweep), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.Charge), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.FlipStrike), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.IntimidatingShout), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.Deflection));
	}
	
	
	// Functions for interaction between Abilities:
	// Functions to use an Ability
	public void useAbility(SteelLegionWarrior.AbilityNames name, int version) {
		Ability chosen = this.abilities.get(name);
		chosen.use(version);
	}
	public void useAbility(SteelLegionWarrior.AbilityNames name) {
		this.useAbility(name, 1);
	}
	
	// Function to set an ability's Cooldown
	public void setAbilityCD(SteelLegionWarrior.AbilityNames name, int turnsRemaining) {
		Ability chosen = this.abilities.get(name);
		chosen.setTurnsRemaining(turnsRemaining);
	}
	
	// Function to get the rank of an Ability
	public int getAbilityRank(SteelLegionWarrior.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.rank();
	}
	
	// Function to get the duration of an Ability
	public int getAbilityDuration(SteelLegionWarrior.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.getDuration();
	}
	
	// Function to get whether or not an Ability is active
	public boolean isAbilityActive(SteelLegionWarrior.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.isActive();
	}
	
	// Other useful transitions to Ability calls
	// Returns a new attack that is the same as the original with the bonuses from the Deflection Ability
	public Attack getDeflectionVersion(Attack original) {
		return this.Deflection.getDeflectionVersion(original);
	}
	
	public void useVengeanceStrike(Character enemy) {
		if (enemy instanceof Enemy) {
			this.VengeanceStrike.execute((Enemy)enemy);
			return;
		}
		System.out.println("Warning: Vengeance Strike attempted to be called on a non-enemy which should not be possible.");
	}
	
	public void useVengeanceFlipStrike(Attack oriVenStr) {
		this.FlipStrike.useVengeanceStrikeVersion(oriVenStr);
	}
	
	
	// Overrides the prompt to give class conditions
	@Override
	public void promptClassConditionGive(Character other) {
		// Adds class Conditions to a list.
		LinkedList<Condition> classConditions = new LinkedList<>();
		classConditions.add(this.VengeanceStrike.getEnemyDamageReduction());
		classConditions.add(this.SwordplayProwess.getEmpoweredCondition());
		classConditions.add(this.WarriorsMight.getStun());
		classConditions.add(this.AgileFighter.getAbilityBlockBonus());
		classConditions.add(this.AgileFighter.getAbilityPreAttackBonus());
		classConditions.add(this.AgileFighter.getBaseBlockBonus());
		classConditions.add(this.AgileFighter.getBasePreAttackBonus());
		classConditions.add(this.Sweep.getSlow());
		classConditions.add(this.Charge.getTargetedPreAttackBonus());
		classConditions.add(this.IntimidatingShout.getSelfDefenseBonus());
		classConditions.add(this.IntimidatingShout.getTauntNormal());
		classConditions.add(this.IntimidatingShout.getTauntAdvanced());
		classConditions.add(this.IntimidatingShout.getTauntElite());
		classConditions.add(this.IntimidatingShout.getTauntBoss());
		classConditions.add(this.Deflection.getVsArmoredCondition());
		classConditions.add(this.Deflection.getStun());
		
		
		// Make a parallel String list for printing
		LinkedList<String> conditionStringList = new LinkedList<>();
		for (Condition c : classConditions) {
			conditionStringList.add(c.toString());
		}
		
		// Add chosen condition to the Character
		int choice = BattleSimulator.getInstance().promptSelect(conditionStringList);
		if (choice == 0) {
			return;
		}
		other.addCondition(classConditions.get(choice-1));
		
		conditionStringList.clear();
	}
	
	
	// Overrides the begin and end turn function of Character to include reducing the Cooldowns of Abilities.
	// Start of Turn override
	@Override
	protected void beginTurnSetup() {
		// Do the usual setup
		super.beginTurnSetup();
		
		// Reduces the Cooldown and/or duration of all Abilities that need it.
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.decrementTurnsRemaining();
			}
		}
	}
	
	// End of Turn Override
	@Override
	public void endTurnSetup() {
		// Normal Setup
		super.endTurnSetup();
		
		// If we're dead, we're done
		if (this.isDead()) {
			return;
		}
		
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.endTurnEffects();
			}
		}
	}
	
	// Overrides pre and post attack effects to apply Ability effects
	protected void applyPreAttackEffects(Attack atk) {
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.applyPreAttackEffects(atk);
			}
		}
	}
	
	protected void applyPostAttackEffects(AttackResult atkRes) {
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.applyPostAttackEffects(atkRes);
			}
		}
	}
}
