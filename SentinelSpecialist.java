package WyattWitemeyer.WarOfGillysburg;
import java.util.*;


//The Sentinel Specialist itself:
public class SentinelSpecialist extends Character {
	// Enumerates the names of the abilities so Cooldown and use functions can be called
	public static enum AbilityNames {
		EmpoweredArrows, MasterworkArrows, Survivable, MultiPurposed, FlamingArrow, FrozenArrow, ExplodingArrow, PenetrationArrow, BlackArrow, RestorationArrow
	}
	
	// Passive Abilities
	private EmpoweredArrows EmpoweredArrows; // Unique Passive Ability (UPA)
	private MasterworkArrows MasterworkArrows;
	private Survivable Survivable;
	private MultiPurposed MultiPurposed;
	
	// Base Abilities
	private FlamingArrow FlamingArrow;
	private FrozenArrow FrozenArrow;
	private ExplodingArrow ExplodingArrow;
	private PenetrationArrow PenetrationArrow;
	private BlackArrow BlackArrow;
	private RestorationArrow RestorationArrow;
	
	// Maps all Abilities so all Cooldowns can be reduced at once
	private HashMap<SentinelSpecialist.AbilityNames, Ability> abilities;
	
	// A set containing the unique Abilities used so far, boolean to keep track of if we've altered the basic attack (useful for Multi-Purposed)
	private HashSet<String> uniqueAbilities;
	private boolean baIsAltered;
	
	// These first two methods help set up the Steel Legion Warrior subclass.
	public SentinelSpecialist(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, LinkedList<Type> types, int eaRank, int maRank, int sRank, int mpRank, int fireRank, int iceRank, int exRank, int pRank, int blackRank, int raRank) {
		// Calls the super constructor to create the Character
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, types);
		
		// Charges for base Abilities based on Multi_Purposed rank
		int charges = 1;
		if (mpRank >= 10) {
			charges = 2;
			if (mpRank >= 15) {
				charges = 3;
			}
		}
		
		// Initializes all Abilities according to their specifications.
		this.EmpoweredArrows = new EmpoweredArrows(this, eaRank);
		this.MasterworkArrows = new MasterworkArrows(this, maRank);
		this.Survivable = new Survivable(this, sRank);
		this.MultiPurposed = new MultiPurposed(this, mpRank);
		this.FlamingArrow = new FlamingArrow(this, fireRank, charges);
		this.FrozenArrow = new FrozenArrow(this, iceRank, charges);
		this.ExplodingArrow = new ExplodingArrow(this, exRank, charges);
		this.PenetrationArrow = new PenetrationArrow(this, pRank, charges);
		this.BlackArrow = new BlackArrow(this, blackRank);
		this.RestorationArrow = new RestorationArrow(this, raRank, charges);
		
		// Add Abilities to a list for Cooldown and usage purposes
		this.abilities = new HashMap<>();
		this.abilities.put(SentinelSpecialist.AbilityNames.EmpoweredArrows, this.EmpoweredArrows);
		this.abilities.put(SentinelSpecialist.AbilityNames.MasterworkArrows, this.MasterworkArrows);
		this.abilities.put(SentinelSpecialist.AbilityNames.Survivable, this.Survivable);
		this.abilities.put(SentinelSpecialist.AbilityNames.MultiPurposed, this.MultiPurposed);
		this.abilities.put(SentinelSpecialist.AbilityNames.FlamingArrow, this.FlamingArrow);
		this.abilities.put(SentinelSpecialist.AbilityNames.FrozenArrow, this.FrozenArrow);
		this.abilities.put(SentinelSpecialist.AbilityNames.ExplodingArrow, this.ExplodingArrow);
		this.abilities.put(SentinelSpecialist.AbilityNames.PenetrationArrow, this.PenetrationArrow);
		this.abilities.put(SentinelSpecialist.AbilityNames.BlackArrow, this.BlackArrow);
		this.abilities.put(SentinelSpecialist.AbilityNames.RestorationArrow, this.RestorationArrow);
		
		//DE Initialize abilities as "charged" if they have multiple charges (based on Multi-Purposed rank)
		
		// Initialize the Unique Abilities set, set baIsAltered to false
		this.uniqueAbilities = new HashSet<>();
		this.baIsAltered = false;
		
		// Add new commands for Abilities
		this.addCommand(new AbilityCommand(this.EmpoweredArrows));
		this.addCommand(new AbilityCommand(this.FlamingArrow));
		this.addCommand(new AbilityCommand(this.FrozenArrow));
		this.addCommand(new AbilityCommand(this.ExplodingArrow));
		this.addCommand(new AbilityCommand(this.PenetrationArrow));
		this.addCommand(new AbilityCommand(this.RestorationArrow));
		this.addCommand(new AbilityCommand(this.BlackArrow));
	}
	public SentinelSpecialist(Character copy, int eaRank, int maRank, int sRank, int mpRank, int fireRank, int iceRank, int exRank, int pRank, int blackRank, int raRank) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getBaseDmgType(), copy.getResistances(), copy.getVulnerabilities(), copy.getTypes(), eaRank, maRank, sRank, mpRank, fireRank, iceRank, exRank, pRank, blackRank, raRank);
	}
	public SentinelSpecialist(SentinelSpecialist copy) {
		this(copy, copy.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows), copy.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows), copy.getAbilityRank(SentinelSpecialist.AbilityNames.Survivable), copy.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed), copy.getAbilityRank(SentinelSpecialist.AbilityNames.FlamingArrow), copy.getAbilityRank(SentinelSpecialist.AbilityNames.FrozenArrow), copy.getAbilityRank(SentinelSpecialist.AbilityNames.ExplodingArrow), copy.getAbilityRank(SentinelSpecialist.AbilityNames.PenetrationArrow), copy.getAbilityRank(SentinelSpecialist.AbilityNames.BlackArrow), copy.getAbilityRank(SentinelSpecialist.AbilityNames.RestorationArrow));
	}
	
	
	// Functions for interaction between Abilities:
	// Functions to use an Ability
	public void useAbility(SentinelSpecialist.AbilityNames name, int version) {
		Ability chosen = this.abilities.get(name);
		chosen.use(version);
	}
	public void useAbility(SentinelSpecialist.AbilityNames name) {
		this.useAbility(name, 1);
	}
	
	// Function to set an ability's Cooldown
	public void setAbilityCD(SentinelSpecialist.AbilityNames name, int turnsRemaining) {
		Ability chosen = this.abilities.get(name);
		chosen.setTurnsRemaining(turnsRemaining);
	}
	
	// Function to get the rank of an Ability
	public int getAbilityRank(SentinelSpecialist.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.rank();
	}
	
	// Function to get the duration of an Ability
	public int getAbilityDuration(SentinelSpecialist.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.getDuration();
	}
	
	// Function to get whether or not an Ability is active
	public boolean isAbilityActive(SentinelSpecialist.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.isActive();
	}
	
	// Function to return the stack requirement for Empowered Abilities
	public int getEmpoweredStackRequirement() {
		return this.EmpoweredArrows.getStackRequirement();
	}
	
	// Function to return the Ability Pre-Attack Bonus of Empowered Abilities
	public Condition getEmpoweredPreAttackBonus() {
		return this.EmpoweredArrows.getAbilityPreAttackBonus();
	}
	
	// Function to return the Ability Pre-Attack Bonus when cast randomly from Multi-Purposed
	public Condition getRandomAbilityPreAttackBonus() {
		return this.MultiPurposed.getAbilityDamageBonus();
	}
	
	// Function to add to the set of unique abilities
	public void addToUniqueSet(String added) {
		this.uniqueAbilities.add(added);
	}
	
	// Function to clear the set of unique abilities (when it is used by Multi-Purposed)
	public void clearUniqueSet() {
		this.uniqueAbilities.clear();
	}
	
	// Function to calculate the number of turns left on Cooldown for all Abilities for Multi-Purposed
	public int getCooldownTurns() {
		// Sum the turns remaining of each Ability
		int total = 0;
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				if (a.onCooldown()) {
					total += a.turnsRemaining();
				}
				//DE Add extra from charges?
			}
		}
		
		// Return the result
		return total;
	}
	
	// Function to get the scaler bonus from Multi-Purposed
	public double scalerBonus() {
		return this.MultiPurposed.getScalerBonus(this.getCooldownTurns());
	}
	
	// Function to increment all stacks (used by ULTIMATE: Black Arrow)
	public void incrementAllStacks() {
		this.FlamingArrow.incrementStacks();
		this.FrozenArrow.incrementStacks();
		this.ExplodingArrow.incrementStacks();
		this.PenetrationArrow.incrementStacks();
		this.RestorationArrow.incrementStacks();
	}
	
	// Function to randomly Empower a basic Ability
	public void randomlyEmpower() {
		// Add all the Abilities to a list, represented by their basic number (Abilities 1-4 and the Hidden Ability as 5)
		LinkedList<Integer> choices = new LinkedList<>();
		if (this.FlamingArrow.rank() > 0 && !this.FlamingArrow.isEmpowered()) {
			choices.add(1);
		}
		if (this.FrozenArrow.rank() > 0 && !this.FrozenArrow.isEmpowered()) {
			choices.add(2);
		}
		if (this.ExplodingArrow.rank() > 0 && !this.ExplodingArrow.isEmpowered()) {
			choices.add(3);
		}
		if (this.PenetrationArrow.rank() > 0 && !this.PenetrationArrow.isEmpowered()) {
			choices.add(4);
		}
		if (this.RestorationArrow.rank() > 0 && !this.RestorationArrow.isEmpowered()) {
			choices.add(5);
		}
		
		// If we have no available Abilities, return, doing nothing
		if (choices.isEmpty()) {
			return;
		}
		
		// Otherwise, randomly pick an Ability and make it Empowered
		Dice d = new Dice(choices.size());
		int choice = choices.get(d.roll() - 1);
		if (choice == 1) {
			this.FlamingArrow.makeEmpowered();
		}
		if (choice == 2) {
			this.FrozenArrow.makeEmpowered();
		}
		if (choice == 3) {
			this.ExplodingArrow.makeEmpowered();
		}
		if (choice == 4) {
			this.PenetrationArrow.makeEmpowered();
		}
		if (choice == 5) {
			this.RestorationArrow.makeEmpowered();
		}
	}
	
	// Function that returns if the character currently has at least 1 Empowered Ability
	public boolean hasEmpoweredAbility() {
		return this.FlamingArrow.isEmpowered() || this.FrozenArrow.isEmpowered() || this.ExplodingArrow.isEmpowered() || this.PenetrationArrow.isEmpowered() || this.RestorationArrow.isEmpowered();
	}
	
	// Function that returns all the currently Empowered Abilities in a list, represented by their basic number (Abilities 1-4 and the Hidden Ability as 5)
	public LinkedList<Integer> getEmpoweredAbilities() {
		LinkedList<Integer> ret = new LinkedList<>();
		if (this.FlamingArrow.isEmpowered()) {
			ret.add(1);
		}
		if (this.FrozenArrow.isEmpowered()) {
			ret.add(2);
		}
		if (this.ExplodingArrow.isEmpowered()) {
			ret.add(3);
		}
		if (this.PenetrationArrow.isEmpowered()) {
			ret.add(4);
		}
		if (this.RestorationArrow.isEmpowered()) {
			ret.add(5);
		}
		return ret;
	}
	
	
	// Overrides the prompt to give class conditions
	@Override
	public void promptClassConditionGive(Character other) {
		/*
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
		*/
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
		
		// At rank 15, Masterwork Arrows has a 50% chance to randomly empowers an ability at the beginning of each turn
		if (this.MasterworkArrows.rank() >= 15) {
			Dice d = new Dice(2);
			if (d.roll() == 1) {
				this.randomlyEmpower();
			}
		}
		
		// At rank 10, Multi-Purposed has a permanent condition that refreshes at the beginning of each turn
		if (this.MultiPurposed.rank() >= 10) {
			this.addCondition(this.MultiPurposed.getPermanentCondition(this.getCooldownTurns()));
		}
		
		// At rank 1, Multi-Purposed replaces the basic attack when there is enough in the unique abilities set
		if (this.MultiPurposed.rank() > 0 && this.uniqueAbilities.size() >= this.MultiPurposed.getUniqueRequirement()) {
			this.alterBasicAttack(MultiPurposed.getRandomAbilityCommand());
			this.baIsAltered = true;
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
		
		// If we used a basic attack, add it to the set
		if (this.usedBasicAttack()) {
			this.uniqueAbilities.add("Basic Attack");
		}
		
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.endTurnEffects();
			}
		}
		
		// Restore the basic attack if it is altered from Multi-Purposed
		if (this.baIsAltered) {
			this.restoreBasicAttack();
			this.baIsAltered = false;
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
	
	// Overrides the getStatStrings to include the classification
	@Override
	public String getStatStrings() {
		return "Class: Sentinel Specialist\n" + super.getStatStrings();
	}
	
	// Does a full display of the Steel Legion Warrior including Abilities
	public String getDescription() {
		String ret = super.getDescription();
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				ret += "\n" + a.getDescription();
			}
		}
		return ret;
	}
}