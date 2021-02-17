package WyattWitemeyer.WarOfGillysburg;

// Ability 2: "Frozen Arrow"
public class FrozenArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private int ccBaseDuration;
	private int numStacks;
	
	private Condition damageAlteration;
	private Condition empoweredDamageAlteration;
	
	// Constructor
	public FrozenArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 2: \"Frozen Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the scaler and Cooldown of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the base duration for Crowd Control Effects and the damage alteration conditions
		this.setBaseDuration();
		this.setDamageAlteration();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 5, reduced to 4 at rank 5
		this.cooldown = 5;
		if (this.rank() >= 5) {
			this.cooldown = 4;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the default scaler of the Ability
	private void setScaler() {
		// Set the scaler based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = .8;
				break;
			case 2:
				this.scaler = 1;
				break;
			case 3:
				this.scaler = 1;
				break;
			case 4:
				this.scaler = 1.2;
				break;
			case 5:
				this.scaler = 1.3;
				break;
			case 6:
				this.scaler = 1.5;
				break;
			case 7:
				this.scaler = 1.5;
				break;
			case 8:
				this.scaler = 1.7;
				break;
			case 9:
				this.scaler = 1.9;
				break;
			case 10:
				this.scaler = 2;
				break;
		}
	}
	
	// Sets the base duration for Crowd Control effects (varies slightly based on rank and what enemy is attacked)
	private void setBaseDuration() {
		// The base duration starts at 1, upgrading to 2 and rank 3 and to 3 at rank 10
		this.ccBaseDuration = 1;
		if (this.rank() >= 3) {
			this.ccBaseDuration = 2;
		}
		if (this.rank() >= 10) {
			this.ccBaseDuration = 3;
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
			return withEffect.hasType(Character.Type.FIRE);
		};
		Requirement applyReduction = (Character withEffect) -> {
			return withEffect.hasType(Character.Type.ICE) || withEffect.hasType(Character.Type.WATER) || withEffect.hasType(Character.Type.HAIRY);
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
		this.damageAlteration = new Condition("Frozen Arrow: Damage Alteration", 0);
		this.damageAlteration.setSource(this.owner);
		this.damageAlteration.addStatusEffect(dmgBonus);
		this.damageAlteration.addStatusEffect(dmgReduction);
		
		this.empoweredDamageAlteration = new Condition("EMPOWERED Frozen Arrow: Damage Alteration", 0);
		this.empoweredDamageAlteration.setSource(this.owner);
		this.empoweredDamageAlteration.addStatusEffect(EdmgBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public int getBaseDuration() {
		return this.ccBaseDuration;
	}
	
	public Condition getDamageAlteration() {
		return new Condition(this.damageAlteration);
	}
	public Condition getDamageAlterationEmpowered() {
		return new Condition(this.empoweredDamageAlteration);
	}
	public Slow getSlow(int duration, int amount) {
		Requirement actReq = (Character withEffect) -> {
			return !(withEffect.containsCondition("Frozen Arrow: Stun") || withEffect.containsCondition("Frozen Arrow: Snare"));
		};
		
		return new Slow("Frozen Arrow: Slow", duration, amount, actReq);
	}
	public Snare getSnare(int duration) {
		Requirement actReq = (Character withEffect) -> {
			return !(withEffect.containsCondition("Frozen Arrow: Stun"));
		};
		
		return new Snare("Frozen Arrow: Snare", duration, actReq);
	}
	public Stun getStun(int duration) {
		return new Stun("Frozen Arrow: Stun", duration);
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
	    AttackBuilder baseAtkBld = new AttackBuilder()
	    		.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler() + this.owner.scalerBonus())
				.type(Attack.DmgType.FLEX)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDamageAlteration());
	    
	    // Figure out the amount and duration of the different crowd control
	    int slowDur = 2;
	    int slowAmt = 2;
	    int snareDur = 0;
	    int stunDur = 0;
	    boolean guaranteeCrit = false;
	    boolean canCrit = true;
	    // Before rank 3 only the amount changes based on enemy type
	    if (this.rank() < 3) {
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		slowAmt--;
	    	}
	    }
	    // From 3 to 7 the slow amount goes to -3 for 2 turns and has the same alterations, but also apply to duration
	    else if (this.rank() < 7) {
	    	slowDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		slowDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		slowDur--;
	    		slowAmt--;
	    	}
	    }
	    // From 7 to 9 the amounts change to snare for 1 turn, slow by -3 for 1 turn after, with alterations
	    else if (this.rank() < 10) {
	    	snareDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		snareDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		snareDur--;
	    		slowDur++;
	    	}
	    }
	    // Otherwise at rank 10 stun is added with corresponding changes
	    else if (this.rank() == 10) {
	    	stunDur++;
	    	snareDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		stunDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		stunDur--;
	    		slowDur++;
	    	}
	    	// Check the attack's critical effect for another duration, but doing so counts as the chance to crit for the attack
	    	if (baseAtkBld.build().calcCrit()) {
				guaranteeCrit = true;
				stunDur++;
			}
			else {
				canCrit = false;
			}
	    }
	    
		// Add the crowd control if its duration is greater than 0
		if (stunDur > 0) {
			baseAtkBld.addSuccessCondition(this.getStun(stunDur));
		}
		if (snareDur > 0) {
			baseAtkBld.addSuccessCondition(this.getSnare(snareDur));
		}
		if (slowDur > 0) {
			baseAtkBld.addSuccessCondition(this.getSlow(slowDur, slowAmt));
		}
		
		// Build the attack with appropriate modifications
		baseAtkBld.canCrit(canCrit);
		if (guaranteeCrit) {
			baseAtkBld.guaranteedCrit();
		}
		Attack baseAtk = baseAtkBld.build();
		
		// Execute the attack
		AttackResult res = baseAtk.execute();
		
		// If the attack failed, end the function without messing with cooldowns or turn actions
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// Add basic "Frozen Arrow" to the set of unique abilities used
		this.owner.addToUniqueSet("Frozen Arrow");
		
		// If the attack landed, add a stack
		if (res.didHit()) {
			this.incrementStacks();
		}
		// Otherwise if Empowered Arrows is less than rank 2, remove a stack
		else if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
			this.decrementStacks();
		}
		
		// Put Frozen Arrow "on Cooldown"
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
	    AttackBuilder baseAtkBld = new AttackBuilder()
	    		.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler((this.getScaler() + this.owner.scalerBonus()) * scalerPortion)
				.type(Attack.DmgType.ICE)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDamageAlterationEmpowered())
				.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
	    
	    // Figure out the amount and duration of the different crowd control
	    int slowDur = 2;
	    int slowAmt = 2;
	    int snareDur = 0;
	    int stunDur = 0;
	    boolean guaranteeCrit = false;
	    boolean canCrit = true;
	    // Before rank 3 only the amount changes based on enemy type
	    if (this.rank() < 3) {
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		slowAmt--;
	    	}
	    }
	    // From 3 to 7 the slow amount goes to -3 for 2 turns and has the same alterations, but also apply to duration
	    else if (this.rank() < 7) {
	    	slowDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		slowDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		slowDur--;
	    		slowAmt--;
	    	}
	    }
	    // From 7 to 9 the amounts change to snare for 1 turn, slow by -3 for 1 turn after, with alterations
	    else if (this.rank() < 10) {
	    	snareDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		snareDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		snareDur--;
	    		slowDur++;
	    	}
	    }
	    // Otherwise at rank 10 stun is added with corresponding changes
	    else if (this.rank() == 10) {
	    	stunDur++;
	    	snareDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		stunDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		stunDur--;
	    		slowDur++;
	    	}
	    	// Check the attack's critical effect for another duration, but doing so counts as the chance to crit for the attack
	    	if (baseAtkBld.build().calcCrit()) {
				guaranteeCrit = true;
				stunDur++;
			}
			else {
				canCrit = false;
			}
	    }
	    // Set Empowered bonuses, At Empowered Arrows rank 4, the stun duration is increased by 1
	    int amount = 30;
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
	    	amount = 50;
			stunDur++;
		}
	    
	    // Create the status effects
	    StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -amount, StatusEffect.Type.OUTGOING);
	    accRed.makePercentage();
	    
	    StatusEffect dmgRed = new StatusEffect(Stat.Version.DAMAGE, -amount, StatusEffect.Type.OUTGOING);
	    dmgRed.makePercentage();
	    
	    // Create the attack slowing Condition
	    int atkSlowDur = stunDur + snareDur + slowDur;
	    Condition atkSlow = new Condition("Frozen Arrow: Attack Slow", atkSlowDur);
	    atkSlow.setSource(this.owner);
	    atkSlow.addStatusEffect(accRed);
	    atkSlow.addStatusEffect(dmgRed);
	    
		// Add the crowd control if its duration is greater than 0
		if (stunDur > 0) {
			baseAtkBld.addSuccessCondition(this.getStun(stunDur));
		}
		if (snareDur > 0) {
			baseAtkBld.addSuccessCondition(this.getSnare(snareDur));
		}
		if (slowDur > 0) {
			baseAtkBld.addSuccessCondition(this.getSlow(slowDur, slowAmt));
		}
		if (atkSlowDur > 0) {
			baseAtkBld.addSuccessCondition(atkSlow);
		}
		
		// Build the attack with appropriate modifications
		baseAtkBld.canCrit(canCrit);
		if (guaranteeCrit) {
			baseAtkBld.guaranteedCrit();
		}
		Attack baseAtk = baseAtkBld.build();
		
		// Execute the attack
		AttackResult res = baseAtk.execute();
		
		// If the attack failed, end the function without messing with cooldowns or turn actions
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// Add "Frozen Arrow" or "EMPOWERED Frozen Arrow" to the set of unique abilities used based on rank of Masterwork Arrows
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
	    	this.owner.addToUniqueSet("EMPOWERED Frozen Arrow");
	    }
	    else {
	    	this.owner.addToUniqueSet("Frozen Arrow");
	    }
	    
	    // If the attack landed, reset stacks
 		if (res.didHit()) {
 			// Reset the stacks
 			this.resetStacks();
 			
 			// Increment the stacks if Empowered Arrows is at least rank 4
 			if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
 				this.incrementStacks();
 			}
 		}
 		// Otherwise, only if we missed and Empowered Arrows is below rank 3, we still reset the stacks
 		else if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 3) {
 			// Reset the stacks
 			this.resetStacks();
 		}
 		
 		// Put Frozen Arrow "on Cooldown" if Empowered Arrows is less than rank 2
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
				.scaler(this.getScaler() + this.owner.scalerBonus())
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.owner.getRandomAbilityPreAttackBonus());
		
		if (isEmpoweredVersion) {
			baseAtkBld.type(Attack.DmgType.FIRE).addAttackerCondition(this.getDamageAlterationEmpowered()).addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
		}
		else {
			baseAtkBld.type(Attack.DmgType.FLEX).addAttackerCondition(this.getDamageAlteration());
		}
		
		// Figure out the amount and duration of the different crowd control
	    int slowDur = 2;
	    int slowAmt = 2;
	    int snareDur = 0;
	    int stunDur = 0;
	    boolean guaranteeCrit = false;
	    boolean canCrit = true;
	    // Before rank 3 only the amount changes based on enemy type
	    if (this.rank() < 3) {
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		slowAmt--;
	    	}
	    }
	    // From 3 to 7 the slow amount goes to -3 for 2 turns and has the same alterations, but also apply to duration
	    else if (this.rank() < 7) {
	    	slowDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		slowDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		slowDur--;
	    		slowAmt--;
	    	}
	    }
	    // From 7 to 9 the amounts change to snare for 1 turn, slow by -3 for 1 turn after, with alterations
	    else if (this.rank() < 10) {
	    	snareDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		snareDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		snareDur--;
	    		slowDur++;
	    	}
	    }
	    // Otherwise at rank 10 stun is added with corresponding changes
	    else if (this.rank() == 10) {
	    	stunDur++;
	    	snareDur++;
	    	slowAmt++;
	    	if (enemy.hasType(Character.Type.FIRE)) {
	    		stunDur++;
	    		slowAmt++;
	    	}
	    	if (enemy.hasType(Character.Type.ICE) || enemy.hasType(Character.Type.WATER) || enemy.hasType(Character.Type.HAIRY)) {
	    		stunDur--;
	    		slowDur++;
	    	}
	    	// Check the attack's critical effect for another duration, but doing so counts as the chance to crit for the attack
	    	if (baseAtkBld.build().calcCrit()) {
				guaranteeCrit = true;
				stunDur++;
			}
			else {
				canCrit = false;
			}
	    }
	    // If we are Empowered, set Empowered bonuses, At Empowered Arrows rank 4, the stun duration is increased by 1
	    if (isEmpoweredVersion) {
	    	int amount = 30;
		    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	amount = 50;
				stunDur++;
			}
		    
		    // Create the status effects
		    StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -amount, StatusEffect.Type.OUTGOING);
		    accRed.makePercentage();
		    
		    StatusEffect dmgRed = new StatusEffect(Stat.Version.DAMAGE, -amount, StatusEffect.Type.OUTGOING);
		    dmgRed.makePercentage();
		    
		    // Create the attack slowing Condition
		    int atkSlowDur = stunDur + snareDur + slowDur;
		    Condition atkSlow = new Condition("Frozen Arrow: Attack Slow", atkSlowDur);
		    atkSlow.setSource(this.owner);
		    atkSlow.addStatusEffect(accRed);
		    atkSlow.addStatusEffect(dmgRed);
		    
		    // Add the Condition
		    if (atkSlowDur > 0) {
				baseAtkBld.addSuccessCondition(atkSlow);
			}
	    }
	    
		// Add the crowd control if its duration is greater than 0
		if (stunDur > 0) {
			baseAtkBld.addSuccessCondition(this.getStun(stunDur));
		}
		if (snareDur > 0) {
			baseAtkBld.addSuccessCondition(this.getSnare(snareDur));
		}
		if (slowDur > 0) {
			baseAtkBld.addSuccessCondition(this.getSlow(slowDur, slowAmt));
		}
		
		
		// Build the attack with appropriate modifications
		baseAtkBld.canCrit(canCrit);
		if (guaranteeCrit) {
			baseAtkBld.guaranteedCrit();
		}
		Attack baseAtk = baseAtkBld.build();
		
		// Execute the attack
		AttackResult res = baseAtk.execute();
		
		// If the attack failed, end the function without messing with stacks
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// Clear the set of unique abilities
		this.owner.clearUniqueSet();
		
		// Add "Frozen Arrow" or "EMPOWERED Frozen Arrow" to the set of unique abilities used based on rank of Masterwork Arrows and of Multi-Purposed
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed) >= 5) {
	    	if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
		    	this.owner.addToUniqueSet("EMPOWERED Frozen Arrow");
		    }
		    else {
		    	this.owner.addToUniqueSet("Frozen Arrow");
		    }
	    }
	    
	    // If the attack landed, add a stack
 		if (res.didHit()) {
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
		ret += "\n\tBase CC Duration: " + this.getBaseDuration();
		return ret;
	}
}