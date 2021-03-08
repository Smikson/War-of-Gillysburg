package WyattWitemeyer.WarOfGillysburg;

import java.util.LinkedList;

// Ability 1: "Flaming Arrow"
// Makes use of Burn effect first defined here
class SentinelBurnDOT extends DamageOverTime {
	// Holds the Burn's attack
	private Attack burnAttack;
	
	// Additional variables that need to be held when the spreading
	private SentinelSpecialist source;
	private boolean isCrit;
	private Condition dmgAlteration;
	private boolean isEmpowered;
	
	// Constructors
	public SentinelBurnDOT(SentinelSpecialist source, Character affected, int duration, double scaler, boolean isCrit, Condition dmgAlteration, boolean isEmpowered) {
		super(source, "Flaming Arrow: Burn Effect", duration);
		this.source = source;
		this.isCrit = isCrit;
		this.dmgAlteration = dmgAlteration;
		AttackBuilder bld = new AttackBuilder()
				.attacker(source)
				.defender(affected)
				.scaler(scaler + source.scalerBonus())
				.type(Attack.DmgType.FIRE)
				.range(Attack.RangeType.OTHER)
				.isAOE()
				.addAttackerCondition(dmgAlteration);
		if (isCrit) {
			bld.guaranteedCrit();
		}
		if (isEmpowered) {
			bld.addAttackerCondition(source.getEmpoweredPreAttackBonus());
		}
		
		this.burnAttack = bld.build();
	}
	public SentinelBurnDOT(SentinelBurnDOT copy) {
		super(copy);
		this.burnAttack = new Attack(copy.burnAttack);
		this.source = copy.source;
	}
	
	// For the bleed effect, activating executes the attack
	@Override
	public void executeDOT() {
		AttackResult res = this.burnAttack.execute();
		
		// If the attack didn't fail, and the rank of Flaming Arrow is 10, prompt for spread
		if (!res.equals(AttackResult.EMPTY) && this.source.getAbilityRank(SentinelSpecialist.AbilityNames.FlamingArrow) == 10) {
			System.out.println("Did the fire spread to adjacent enemies?");
			if (BattleSimulator.getInstance().askYorN()) {
				// Select enemies to spread to
				System.out.println("Choose enemies fire spreads to:");
		    	LinkedList<Character> enemies = BattleSimulator.getInstance().targetMultiple();
		        if (enemies.isEmpty()) {
		        	return;
		        }
		        if (enemies.contains(Character.EMPTY)) {
		        	enemies.clear();
		        }
		        
		        // Add a new dot to each enemy affected
		        //NOTE: This uses constant values based off rank 10 of Flaming Arrow
		        for (Character enemy : enemies) {
		        	enemy.addCondition(new SentinelBurnDOT(this.source, enemy, 3, .5, this.isCrit, new Condition(this.dmgAlteration), this.isEmpowered));
		        }
			}
		}
	}
	
	// Returns the display line (without the tabs) of the Bleed DOT effect in the Condition
	@Override
	public String getDOTString() {
		int dmgEstimate = (int)Math.round(this.burnAttack.getScaler()*this.getSource().getDamage());
		if (this.burnAttack.guaranteedCrit()) {
			dmgEstimate *= 2;
		}
		return "Bleeding: Deals ~" + dmgEstimate + " damage at the beginning of each turn.";
	}
}

//The class Flaming Arrow itself
public class FlamingArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private double baseScaler;
	private double fireScaler;
	private int fireDuration;
	private int numStacks;
	
	private Condition damageAlteration;
	private Condition empoweredDamageAlteration;
	
	// Constructor
	public FlamingArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Flaming Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the Cooldown and scalers of the Ability
		this.setCooldown();
		this.setScalers();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the burn duration of the Ability and the damage alteration conditions
		this.setFireDuration();
		this.setDamageAlteration();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 3, reduced to 2 at rank 7
		this.cooldown = 3;
		if (this.rank() >= 7) {
			this.cooldown = 2;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the base and fire scalers of the Ability (and sets the default equal to the base)
	private void setScalers() {
		// Set a default value for the first rank
		this.baseScaler = 1.0;
		this.fireScaler = .2;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = 1.0;
				this.fireScaler = .2;
				break;
			case 2:
				this.baseScaler = 1.2;
				this.fireScaler = .2;
				break;
			case 3:
				this.baseScaler = 1.3;
				this.fireScaler = .25;
				break;
			case 4:
				this.baseScaler = 1.5;
				this.fireScaler = .25;
				break;
			case 5:
				this.baseScaler = 1.7;
				this.fireScaler = .25;
				break;
			case 6:
				this.baseScaler = 1.8;
				this.fireScaler = .3;
				break;
			case 7:
				this.baseScaler = 2.0;
				this.fireScaler = .3;
				break;
			case 8:
				this.baseScaler = 2.1;
				this.fireScaler = .35;
				break;
			case 9:
				this.baseScaler = 2.2;
				this.fireScaler = .4;
				break;
			case 10:
				this.baseScaler = 2.5;
				this.fireScaler = .5;
				break;
		}
		
		// Set the default scaler to be the base scaler
		this.scaler = this.baseScaler;
	}
	
	// Sets the duration of the burn/fire effect
	private void setFireDuration() {
		// The fire duration is 2 until rank 5 where it is increased to 3
		this.fireDuration = 2;
		if (this.rank() >= 5) {
			this.fireDuration = 3;
		}
	}
	
	// Sets the Conditions for damage alteration
	private void setDamageAlteration() {
		// Set the amount
		int bonusAmount = 25;
		int reduceAmount = 25;
		int EbonusAmount = 50;
		
		// Create the requirements for each status effect
		Requirement applyBonus = (Character withEffect) -> {
			return withEffect.hasType(Character.Type.HAIRY);
		};
		Requirement applyReduction = (Character withEffect) -> {
			return withEffect.hasType(Character.Type.ICE) || withEffect.hasType(Character.Type.WATER) || withEffect.hasType(Character.Type.FIRE);
		};
		
		// Creates the StatusEffects
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, bonusAmount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		dmgBonus.setBasicRequirement(applyBonus);
		
		StatusEffect EdmgBonus = new StatusEffect(Stat.Version.DAMAGE, EbonusAmount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		dmgBonus.setBasicRequirement(applyBonus);
		
		StatusEffect dmgReduction = new StatusEffect(Stat.Version.DAMAGE, -reduceAmount, StatusEffect.Type.OUTGOING);
		dmgReduction.makePercentage();
		dmgReduction.setBasicRequirement(applyReduction);
		
		// Creates the Conditions with duration 0 since it is applied and unapplied in a single attack
		this.damageAlteration = new Condition("Flaming Arrow: Damage Alteration", 0);
		this.damageAlteration.setSource(this.owner);
		this.damageAlteration.addStatusEffect(dmgBonus);
		this.damageAlteration.addStatusEffect(dmgReduction);
		
		this.empoweredDamageAlteration = new Condition("EMPOWERED Flaming Arrow: Damage Alteration", 0);
		this.empoweredDamageAlteration.setSource(this.owner);
		this.empoweredDamageAlteration.addStatusEffect(EdmgBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	
	public double getFireScaler() {
		return this.fireScaler;
	}
	
	public int getFireDuration(Character enemy) {
		int duration = this.fireDuration;
		if (enemy.hasType(Character.Type.HAIRY)) {
			duration++;
		}
		if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.FIRE)) {
			duration--;
		}
		return duration;
	}
	public int getFireDuration() {
		return this.fireDuration;
	}
	
	public Condition getDamageAlteration() {
		return new Condition(this.damageAlteration);
	}
	public Condition getDamageAlterationEmpowered() {
		return new Condition(this.empoweredDamageAlteration);
	}
	public Blind getBlind(int duration) {
		return new Blind("Flaming Arrow: Blind", duration);
	}
	public Stun getStun() {
		return new Stun("Flaming Arrow: Stun", 1);
	}
	
	// Functions for "Empowered" effects
	public int getNumStacks() {
		return this.numStacks;
	}
	public void incrementStacks() {
		this.numStacks++;
	}
	public void decrementStacks() {
		this.numStacks--;
		if (this.numStacks < 0) {
			this.numStacks = 0;
		}
	}
	public void resetStacks() {
		for (int x = 0; x < this.owner.getEmpoweredStackRequirement(); x++) {
			this.decrementStacks();
		}
	}
	public boolean isEmpowered() {
		return this.numStacks >= this.owner.getEmpoweredStackRequirement();
	}
	public void makeEmpowered() {
		this.setOffCooldownAll();
		this.numStacks = this.owner.getEmpoweredStackRequirement();
	}
	
	
	// Function to specify which version of the Ability is to be used (1 for default version, 2 for Empowered version, 3 for cast randomly (Multi-Purposed) version)
	@Override
	public void use(int version) {
		// Default version, what is used as specified from the Command of this Ability
		if (version == 1) {
			this.use();
			return;
		}
		
		// Empowered version, rarely directly specified, is usually called from default version when this Ability isEmpowered().
		if (version == 2) {
			this.useEmpowered();
			return;
		}
		
		// Cast randomly (Multi-Purposed) version, cast from the new basic attack from getting enough unique abilities cast
		if (version == 3) {
			this.useRandom();
			return;
		}
		
		// Print a warning if this function is ever actually directly called with a wrong version number
		System.out.println("Warning: The Ability, " + this.getName() + ", does not have a use(" + version + ") function defined, but it was called!");
	}
	
	// Use(1): Default version of Ability
	@Override
	public void use() {
		// First, if we are Empowered, cast the Empowered version of this Ability instead
		if (this.isEmpowered()) {
			this.useEmpowered();
			return;
		}
		
		// Select the target enemy
 		Enemy enemy = BattleSimulator.getInstance().targetSingleEnemy();
	    if (enemy.equals(Enemy.EMPTY)) {
	    	return;
	    }
		
		// Create the base attack
		Attack baseAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getBaseScaler() + this.owner.scalerBonus())
				.type(Attack.DmgType.FLEX)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDamageAlteration())
				.build();
		
		// Add the blind affect based on rank and if the attack would critically hit
		int blindDuration = 0;
		boolean guaranteeCrit = false;
		boolean canCrit = true;
		boolean willStun = false;
		if (this.rank() >= 7) {
			if (this.rank() == 10) {
				blindDuration++;
				if (enemy.hasType(Character.Type.HAIRY) && (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL) || enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED))) {
					willStun = true;
				}
			}
			// Check the attack's critical effect for another duration, but doing so counts as the chance to crit for the attack
			if (baseAtk.calcCrit()) {
				guaranteeCrit = true;
				blindDuration++;
			}
			else {
				canCrit = false;
			}
		}
		
		// If our blind duration is greater than 0, rebuild the attack with the appropriate modifications
		if (blindDuration > 0) {
			AttackBuilder bld = new AttackBuilder(baseAtk)
					.canCrit(canCrit)
					.addSuccessCondition(this.getBlind(blindDuration));
			if (guaranteeCrit) {
				bld.guaranteedCrit();
			}
			if (willStun) {
				bld.addSuccessCondition(this.getStun());
			}
			baseAtk = bld.build();
		}
		
		// Execute the attack
		AttackResult res = baseAtk.execute();
		
		// If the attack failed, end the function without messing with cooldowns or turn actions or adding burn effects
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// Add basic "Flaming Arrow" to the set of unique abilities used
		this.owner.addToUniqueSet("Flaming Arrow");
		
		// Create the damage over time effect to add to the affected enemy if the attack lands
		SentinelBurnDOT burn = new SentinelBurnDOT(this.owner, enemy, this.getFireDuration(enemy), this.getFireScaler(), res.didCrit(), this.getDamageAlteration(), false);
		
		// If the attack landed, add the burn effect to the affected enemy and add a stack
		if (res.didHit()) {
			enemy.addCondition(burn);
			this.incrementStacks();
		}
		// Otherwise if Empowered Arrows is less than rank 2, remove a stack
		else if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
			this.decrementStacks();
		}
		
		// Put Flaming Arrow "on Cooldown"
		this.setOnCooldown();
		
		// If we are EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use(2): Empowered version of Ability
	public void useEmpowered(double scalerPortion) {
		// Select the target enemy
 		Enemy enemy = BattleSimulator.getInstance().targetSingleEnemy();
	    if (enemy.equals(Enemy.EMPTY)) {
	    	return;
	    }
		
		// Create the base attack
		Attack baseAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler((this.getBaseScaler() + this.owner.scalerBonus()) * scalerPortion)
				.type(Attack.DmgType.FIRE)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDamageAlterationEmpowered())
				.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus())
				.build();
		
		// Add the blind affect based on rank (of this and Empowered Arrows) and if the attack would critically hit
		int blindDuration = 0;
		boolean guaranteeCrit = false;
		boolean canCrit = true;
		boolean willStun = false;
		if (this.rank() >= 7) {
			if (this.rank() == 10) {
				blindDuration++;
				if (enemy.hasType(Character.Type.HAIRY) && (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL) || enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED))) {
					willStun = true;
				}
			}
			// Check the attack's critical effect for another duration, but doing so counts as the chance to crit for the attack
			if (baseAtk.calcCrit()) {
				guaranteeCrit = true;
				blindDuration++;
			}
			else {
				canCrit = false;
			}
		}
		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
			blindDuration++;
		}
		
		// If our blind duration is greater than 0, rebuild the attack with the appropriate modifications
		if (blindDuration > 0) {
			AttackBuilder bld = new AttackBuilder(baseAtk)
					.canCrit(canCrit)
					.addSuccessCondition(this.getBlind(blindDuration));
			if (guaranteeCrit) {
				bld.guaranteedCrit();
			}
			if (willStun) {
				bld.addSuccessCondition(this.getStun());
			}
			baseAtk = bld.build();
		}
		
		// Execute the attack
		AttackResult res = baseAtk.execute();
		
		// If the attack failed, end the function without messing with turn actions, stacks, or adding burn effects
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// Add "Flaming Arrow" or "EMPOWERED Flaming Arrow" to the set of unique abilities used based on rank of Masterwork Arrows
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
	    	this.owner.addToUniqueSet("EMPOWERED Flaming Arrow");
	    }
	    else {
	    	this.owner.addToUniqueSet("Flaming Arrow");
	    }
		
		// Create the damage over time effect to add to the affected enemy if the attack lands
		int bonusDuration = (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4)? 2 : 1;
		SentinelBurnDOT burn = new SentinelBurnDOT(this.owner, enemy, this.getFireDuration(enemy) + bonusDuration, this.getFireScaler(), res.didCrit(), this.getDamageAlteration(), true);
		
		// If the attack landed, add the burn effect to the affected enemy
		if (res.didHit()) {
			enemy.addCondition(burn);
			
			// Reset the stacks
			this.resetStacks();
			
			// Increment the stacks if Empowered Arrows is at least rank 4
			if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
				this.incrementStacks();
			}
		}
		// Otherwise, if we missed and Empowered Arrows is below rank 3, we still reset the stacks
		else if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 3) {
			// Reset the stacks
			this.resetStacks();
		}
		
		// Put Flaming Arrow "on Cooldown" if Empowered Arrows is less than rank 2
		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
			this.setOnCooldown();
		}
		
		// If we are somehow EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	public void useEmpowered() {
		this.useEmpowered(1.0);
	}
	
	// Use(3): Cast randomly (Multi-Purposed) version of Ability
	public void useRandom() {
		// First determine if we're doing the normal or Empowered version based on the rank of Multi-Purposed
		boolean isEmpoweredVersion = this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed) == 15;
		
		// Select the target enemy
 		Enemy enemy = BattleSimulator.getInstance().targetSingleEnemy();
	    if (enemy.equals(Enemy.EMPTY)) {
	    	return;
	    }
		
		// Create the base attack
		AttackBuilder baseAtkBld = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getBaseScaler() + this.owner.scalerBonus())
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.owner.getRandomAbilityPreAttackBonus());
		
		if (isEmpoweredVersion) {
			baseAtkBld.type(Attack.DmgType.FIRE).addAttackerCondition(this.getDamageAlterationEmpowered()).addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
		}
		else {
			baseAtkBld.type(Attack.DmgType.FLEX).addAttackerCondition(this.getDamageAlteration());
		}
		Attack baseAtk = baseAtkBld.build();
		
		// Add the blind affect based on rank (of this and Empowered Arrows) and if the attack would critically hit
		int blindDuration = 0;
		boolean guaranteeCrit = false;
		boolean canCrit = true;
		boolean willStun = false;
		if (this.rank() >= 7) {
			if (this.rank() == 10) {
				blindDuration++;
				if (enemy.hasType(Character.Type.HAIRY) && (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL) || enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED))) {
					willStun = true;
				}
			}
			// Check the attack's critical effect for another duration, but doing so counts as the chance to crit for the attack
			if (baseAtk.calcCrit()) {
				guaranteeCrit = true;
				blindDuration++;
			}
			else {
				canCrit = false;
			}
		}
		if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
			blindDuration++;
		}
		
		// If our blind duration is greater than 0, rebuild the attack with the appropriate modifications
		if (blindDuration > 0) {
			AttackBuilder bld = new AttackBuilder(baseAtk)
					.canCrit(canCrit)
					.addSuccessCondition(this.getBlind(blindDuration));
			if (guaranteeCrit) {
				bld.guaranteedCrit();
			}
			if (willStun) {
				bld.addSuccessCondition(this.getStun());
			}
			baseAtk = bld.build();
		}
		
		// Execute the attack
		AttackResult res = baseAtk.execute();
		
		// If the attack failed, end the function without messing with cooldowns or turn actions or adding burn effects
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// Clear the set of unique abilities
		this.owner.clearUniqueSet();
		
		// Add "Flaming Arrow" or "EMPOWERED Flaming Arrow" to the set of unique abilities used based on rank of Masterwork Arrows and of Multi-Purposed
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed) >= 5) {
	    	if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
		    	this.owner.addToUniqueSet("EMPOWERED Flaming Arrow");
		    }
		    else {
		    	this.owner.addToUniqueSet("Flaming Arrow");
		    }
	    }
		
		// Create the damage over time effect to add to the affected enemy if the attack lands
	    int bonusDuration = 0;
	    if (isEmpoweredVersion) {
	    	bonusDuration = (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4)? 2 : 1;
	    }
		SentinelBurnDOT burn = new SentinelBurnDOT(this.owner, enemy, this.getFireDuration(enemy) + bonusDuration, this.getFireScaler(), res.didCrit(), this.getDamageAlteration(), isEmpoweredVersion);
		
		// If the attack landed, add the burn effect to the affected enemy and add a stack
		if (res.didHit()) {
			enemy.addCondition(burn);
			this.incrementStacks();
		}
		// Otherwise if Empowered Arrows is less than rank 2, remove a stack
		else if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
			this.decrementStacks();
		}
		
		// If we are EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
	}
	
	
	// Override the to-String to include a description of the number of stacks and if the Ability is Empowered
	@Override
	public String toString() {
		String ori = super.toString();
		if (this.isEmpowered()) {
			return ori + " --EMPOWERED";
		}
		return ori + "  (" + this.getNumStacks() + " stack(s))";
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBase Scaler: " + this.getBaseScaler();
		ret += "\n\tFire Scaler: " + this.getFireScaler();
		ret += "\n\tFire Duration: " + this.getFireDuration();
		return ret;
	}
}