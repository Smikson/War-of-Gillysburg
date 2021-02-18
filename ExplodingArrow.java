package WyattWitemeyer.WarOfGillysburg;

import java.util.LinkedList;

// Ability 3: "Exploding Arrow"
public class ExplodingArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double baseScaler;
	private double explosionScaler;
	private int numStacks;
	
	private Condition damageAlteration;
	private Condition empoweredDamageAlteration;
	
	// Constructor
	public ExplodingArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Exploding Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the scalers and the Cooldown of the Ability
		this.setCooldown();
		this.setScalers();
		
		// Initialize additional variables and the damage alteration conditions
		this.numStacks = 0;
		this.setDamageAlteration();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 3, reduced to 2 at rank 5
		this.cooldown = 3;
		if (this.rank() >= 5) {
			this.cooldown = 2;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the base and fire scalers of the Ability (and sets the default equal to the base)
	private void setScalers() {
		// Set a default value for the first rank
		this.baseScaler = .5;
		this.explosionScaler = .75;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = .5;
				this.explosionScaler = .75;
				break;
			case 2:
				this.baseScaler = .65;
				this.explosionScaler = .8;
				break;
			case 3:
				this.baseScaler = .65;
				this.explosionScaler = .8;
				break;
			case 4:
				this.baseScaler = .75;
				this.explosionScaler = 1;
				break;
			case 5:
				this.baseScaler = .75;
				this.explosionScaler = 1;
				break;
			case 6:
				this.baseScaler = .85;
				this.explosionScaler = 1.2;
				break;
			case 7:
				this.baseScaler = 1;
				this.explosionScaler = 1.2;
				break;
			case 8:
				this.baseScaler = 1.2;
				this.explosionScaler = 1.3;
				break;
			case 9:
				this.baseScaler = 1.5;
				this.explosionScaler = 1.5;
				break;
			case 10:
				this.baseScaler = 1.5;
				this.explosionScaler = 1.5;
				break;
		}
		
		// Set the default scaler to be the base scaler
		this.scaler = this.baseScaler;
	}
	
	// Sets the Conditions for damage alteration
	private void setDamageAlteration() {
		// Set the amount
		int bonusAmount = 25;
		int reduceAmount = 25;
		int EbonusAmount = 50;
		
		// Create the requirements for each status effect
		Requirement applyBonus = (Character withEffect) -> {
			return withEffect.hasType(Character.Type.ICE) || withEffect.hasType(Character.Type.WATER);
		};
		Requirement applyReduction = (Character withEffect) -> {
			return withEffect.hasType(Character.Type.FIRE);
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
		this.damageAlteration = new Condition("Exploding Arrow: Damage Alteration", 0);
		this.damageAlteration.setSource(this.owner);
		this.damageAlteration.addStatusEffect(dmgBonus);
		this.damageAlteration.addStatusEffect(dmgReduction);
		
		this.empoweredDamageAlteration = new Condition("EMPOWERED Exploding Arrow: Damage Alteration", 0);
		this.empoweredDamageAlteration.setSource(this.owner);
		this.empoweredDamageAlteration.addStatusEffect(EdmgBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public Condition getDamageAlteration() {
		return new Condition(this.damageAlteration);
	}
	public Condition getDamageAlterationEmpowered() {
		return new Condition(this.empoweredDamageAlteration);
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	
	public double getExplosionScaler() {
		return this.explosionScaler;
	}
	
	// Functions for 'Empowered" effects
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
	    
	    // Select all additional enemies hit by the AOE, assume enemy must be hit
	    System.out.println("Choose enemies affected by primary blast:");
    	LinkedList<Character> primaryAOE = BattleSimulator.getInstance().targetMultiple();
    	if (primaryAOE.isEmpty()) {
        	return;
        }
        if (primaryAOE.contains(Character.EMPTY)) {
        	primaryAOE.clear();
        }
    	if (!primaryAOE.contains(enemy)) {
    		primaryAOE.add(enemy);
    	}
    	
    	// At ranks 5, 6, and 10, different enemies take 50% of the AOE damage
    	LinkedList<Character> aoe50 = new LinkedList<>();
    	if (this.rank() == 5 || this.rank() == 6 || this.rank() == 10) {
    		System.out.println("Choose enemies affected by 50% blast:");
    		aoe50 = BattleSimulator.getInstance().targetMultiple();
        	if (aoe50.isEmpty()) {
            	return;
            }
            if (aoe50.contains(Character.EMPTY)) {
            	aoe50.clear();
            }
            // Remove any duplicates, assume supposed to be in primary
            for (Character c : primaryAOE) {
            	if (aoe50.contains(c)) {
            		aoe50.remove(c);
            	}
            }
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
	    
	    // Create a builder for the AOE attack
	    Attack aoeAtk = new AttackBuilder()
	    		.attacker(this.owner)
	    		.isAOE()
	    		.scaler(this.getExplosionScaler() + this.owner.scalerBonus())
	    		.type(Attack.DmgType.EXPLOSIVE)
	    		.range(Attack.RangeType.OTHER)
	    		.addAttackerCondition(this.getDamageAlteration())
	    		.build();
	    
	    // Execute the base attack followed by all AOE attacks
	    baseAtk.execute();
	    
	    // If we're at least rank 3, the primary target takes 50% extra explosion damage
	    if (this.rank() >= 3) {
	    	// Deal the extra damage and remove the primary target from the primaryAOE
	    	Attack pAOE = new AttackBuilder(aoeAtk).defender(enemy).scaler((this.getExplosionScaler() + this.owner.scalerBonus()) * 1.5).build();
	    	pAOE.execute();
	    	if (primaryAOE.contains(enemy)) {
	    		primaryAOE.remove(enemy);
	    	}
	    }
	    
	    // Primary AOE attacks
	    for (Character c : primaryAOE) {
	    	Attack pAOE = new AttackBuilder(aoeAtk).defender(c).build();
	    	pAOE.execute();
	    }
	    
	    // 50% AOE attacks
	    for (Character c : aoe50) {
	    	Attack sAOE = new AttackBuilder(aoeAtk).defender(c).scaler((this.getExplosionScaler() + this.owner.scalerBonus()) * .5).build();
	    	sAOE.execute();
	    }
	    
	    // Add basic "Exploding Arrow" to the set of unique abilities used
 		this.owner.addToUniqueSet("Exploding Arrow");
 		
 		// This Ability should never miss, add a stack
 		this.incrementStacks();
 		
 		// Put Exploding Arrow "on Cooldown"
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
	    
	    // Select all additional enemies hit by the AOE, assume enemy must be hit
	    System.out.println("Choose enemies affected by primary blast:");
    	LinkedList<Character> primaryAOE = BattleSimulator.getInstance().targetMultiple();
    	if (primaryAOE.isEmpty()) {
        	return;
        }
        if (primaryAOE.contains(Character.EMPTY)) {
        	primaryAOE.clear();
        }
    	if (!primaryAOE.contains(enemy)) {
    		primaryAOE.add(enemy);
    	}
    	
    	// Prompt for enemies taking the 50% of the AOE damage
    	LinkedList<Character> aoe50 = new LinkedList<>();
		System.out.println("Choose enemies affected by 50% blast:");
		aoe50 = BattleSimulator.getInstance().targetMultiple();
    	if (aoe50.isEmpty()) {
        	return;
        }
        if (aoe50.contains(Character.EMPTY)) {
        	aoe50.clear();
        }
        // Remove any duplicates, assume supposed to be in primary
        for (Character c : primaryAOE) {
        	if (aoe50.contains(c)) {
        		aoe50.remove(c);
        	}
        }
        
        // Create the base attack
	    Attack baseAtk = new AttackBuilder()
	    		.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler((this.getBaseScaler() + this.owner.scalerBonus()) * scalerPortion)
				.type(Attack.DmgType.EXPLOSIVE)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDamageAlterationEmpowered())
				.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus())
				.build();
	    
	    // Create a builder for the AOE attack
	    Attack aoeAtk = new AttackBuilder()
	    		.attacker(this.owner)
	    		.isAOE()
	    		.scaler(this.getExplosionScaler() + this.owner.scalerBonus())
	    		.type(Attack.DmgType.EXPLOSIVE)
	    		.range(Attack.RangeType.OTHER)
	    		.addAttackerCondition(this.getDamageAlterationEmpowered())
	    		.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus())
	    		.build();
	    
	    
	    
	    // Execute the base attack followed by all AOE attacks
	    baseAtk.execute();
	    
	    // If we're at least rank 3, the primary target takes 50% extra explosion damage
	    if (this.rank() >= 3) {
	    	// Declare an AttackBuilder to be identical to aoeAtk
	    	AttackBuilder pAOEbld = new AttackBuilder(aoeAtk);
	    	
	    	// Create the Accuracy reduction Condition to be applied if Empowered Arrows is above rank 4
		    StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -50, StatusEffect.Type.OUTGOING);
		    accRed.makePercentage();
		    Condition shockwave = new Condition("Exploding Arrow: Shockwave", 0);
		    shockwave.setSource(this.owner);
		    shockwave.addStatusEffect(accRed);
		    
		    // Add the condition if we meet the criteria
		    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	pAOEbld.addSuccessCondition(shockwave);
	        }
		    
	    	// Deal the extra damage and remove the primary target from the primaryAOE
		    Attack pAOE = pAOEbld.defender(enemy).scaler((this.getExplosionScaler() + this.owner.scalerBonus()) * 1.5).build();
		    pAOE.execute();
	    	if (primaryAOE.contains(enemy)) {
	    		primaryAOE.remove(enemy);
	    	}
	    }
	    
	    // Primary AOE attacks
	    for (Character c : primaryAOE) {
	    	// Declare an AttackBuilder to be identical to aoeAtk
	    	AttackBuilder pAOEbld = new AttackBuilder(aoeAtk);
	    	
	    	// Create the Accuracy reduction Condition to be applied if Empowered Arrows is above rank 4
		    StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -30, StatusEffect.Type.OUTGOING);
		    accRed.makePercentage();
		    Condition shockwave = new Condition("Exploding Arrow: Shockwave", 0);
		    shockwave.setSource(this.owner);
		    shockwave.addStatusEffect(accRed);
		    
		    // Add the condition if we meet the criteria
		    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	pAOEbld.addSuccessCondition(shockwave);
	        }
		    
	    	Attack pAOE = new AttackBuilder(aoeAtk).defender(c).build();
	    	pAOE.execute();
	    }
	    
	    // 50% AOE attacks -- become 75% when empowered >= Empowered Arrows rank 4
	    for (Character c : aoe50) {
	    	// Set the amount
	    	double amt = .5;
		    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	amt = .75;
	        }
		    
		    // Create and execute the attack
	    	Attack sAOE = new AttackBuilder(aoeAtk).defender(c).scaler((this.getExplosionScaler() + this.owner.scalerBonus()) * amt).build();
	    	sAOE.execute();
	    }
        
	    // Add "Exploding Arrow" or "EMPOWERED Exploding Arrow" to the set of unique abilities used based on rank of Masterwork Arrows
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
	    	this.owner.addToUniqueSet("EMPOWERED Exploding Arrow");
	    }
	    else {
	    	this.owner.addToUniqueSet("Exploding Arrow");
	    }
	    
	    // Reset the stacks, then increment the stacks if Empowered Arrows is at least rank 4
		this.resetStacks();
		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
			this.incrementStacks();
		}
		
		// Put Exploding Arrow "on Cooldown" if Empowered Arrows is less than rank 2
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
	    
	    // Select all additional enemies hit by the AOE, assume enemy must be hit
	    System.out.println("Choose enemies affected by primary blast:");
    	LinkedList<Character> primaryAOE = BattleSimulator.getInstance().targetMultiple();
    	if (primaryAOE.isEmpty()) {
        	return;
        }
        if (primaryAOE.contains(Character.EMPTY)) {
        	primaryAOE.clear();
        }
    	if (!primaryAOE.contains(enemy)) {
    		primaryAOE.add(enemy);
    	}
    	
    	// At ranks 5, 6, and 10 as well as when Empowered, different enemies take 50% of the AOE damage
    	LinkedList<Character> aoe50 = new LinkedList<>();
    	if (this.rank() == 5 || this.rank() == 6 || this.rank() == 10 || isEmpoweredVersion) {
    		System.out.println("Choose enemies affected by 50% blast:");
    		aoe50 = BattleSimulator.getInstance().targetMultiple();
        	if (aoe50.isEmpty()) {
            	return;
            }
            if (aoe50.contains(Character.EMPTY)) {
            	aoe50.clear();
            }
            // Remove any duplicates, assume supposed to be in primary
            for (Character c : primaryAOE) {
            	if (aoe50.contains(c)) {
            		aoe50.remove(c);
            	}
            }
    	}
    	
    	// Create the base attack
	    AttackBuilder baseAtkBld = new AttackBuilder()
	    		.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler((this.getBaseScaler() + this.owner.scalerBonus()))
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.owner.getRandomAbilityPreAttackBonus());
	    
	    // Create a builder for the AOE attack
	    AttackBuilder aoeAtkBld = new AttackBuilder()
	    		.attacker(this.owner)
	    		.isAOE()
	    		.scaler(this.getExplosionScaler() + this.owner.scalerBonus())
	    		.type(Attack.DmgType.EXPLOSIVE)
	    		.range(Attack.RangeType.OTHER)
	    		.addAttackerCondition(this.owner.getRandomAbilityPreAttackBonus());
	    
	    // Alter the builder based on if this is the Empowered Version
	    if (isEmpoweredVersion) {
			baseAtkBld.type(Attack.DmgType.EXPLOSIVE).addAttackerCondition(this.getDamageAlterationEmpowered()).addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
			aoeAtkBld.addAttackerCondition(this.getDamageAlterationEmpowered()).addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
		}
		else {
			baseAtkBld.type(Attack.DmgType.FLEX).addAttackerCondition(this.getDamageAlteration());
			aoeAtkBld.addAttackerCondition(this.getDamageAlteration());
		}
	    
	    // Build the attacks for further use
	    Attack baseAtk = baseAtkBld.build();
	    Attack aoeAtk = aoeAtkBld.build();
	    
	    // Execute the base attack followed by all AOE attacks
	    baseAtk.execute();
	    
	    // If we're at least rank 3, the primary target takes 50% extra explosion damage
	    if (this.rank() >= 3) {
	    	// Declare an AttackBuilder to be identical to aoeAtk
	    	AttackBuilder pAOEbld = new AttackBuilder(aoeAtk);
	    	
	    	// Create the Accuracy reduction Condition to be applied if Empowered Arrows is above rank 4
		    StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -50, StatusEffect.Type.OUTGOING);
		    accRed.makePercentage();
		    Condition shockwave = new Condition("Exploding Arrow: Shockwave", 0);
		    shockwave.setSource(this.owner);
		    shockwave.addStatusEffect(accRed);
		    
		    // Add the condition if we meet the criteria
		    if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	pAOEbld.addSuccessCondition(shockwave);
	        }
		    
	    	// Deal the extra damage and remove the primary target from the primaryAOE
		    Attack pAOE = pAOEbld.defender(enemy).scaler((this.getExplosionScaler() + this.owner.scalerBonus()) * 1.5).build();
		    pAOE.execute();
	    	if (primaryAOE.contains(enemy)) {
	    		primaryAOE.remove(enemy);
	    	}
	    }
	    
	    // Primary AOE attacks
	    for (Character c : primaryAOE) {
	    	// Declare an AttackBuilder to be identical to aoeAtk
	    	AttackBuilder pAOEbld = new AttackBuilder(aoeAtk);
	    	
	    	// Create the Accuracy reduction Condition to be applied if Empowered Arrows is above rank 4
		    StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -30, StatusEffect.Type.OUTGOING);
		    accRed.makePercentage();
		    Condition shockwave = new Condition("Exploding Arrow: Shockwave", 0);
		    shockwave.setSource(this.owner);
		    shockwave.addStatusEffect(accRed);
		    
		    // Add the condition if we meet the criteria
		    if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	pAOEbld.addSuccessCondition(shockwave);
	        }
		    
	    	Attack pAOE = new AttackBuilder(aoeAtk).defender(c).build();
	    	pAOE.execute();
	    }
	    
	    // 50% AOE attacks -- become 75% when empowered >= Empowered Arrows rank 4
	    for (Character c : aoe50) {
	    	// Set the amount
	    	double amt = .5;
		    if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
		    	amt = .75;
	        }
		    
		    // Create and execute the attack
	    	Attack sAOE = new AttackBuilder(aoeAtk).defender(c).scaler((this.getExplosionScaler() + this.owner.scalerBonus()) * amt).build();
	    	sAOE.execute();
	    }
	    
	    // Clear the set of unique abilities
 		this.owner.clearUniqueSet();
 		
 		// Add "Exploding Arrow" or "EMPOWERED Exploding Arrow" to the set of unique abilities used based on rank of Masterwork Arrows and of Multi-Purposed
 	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed) >= 5) {
 	    	if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
 		    	this.owner.addToUniqueSet("EMPOWERED Exploding Arrow");
 		    }
 		    else {
 		    	this.owner.addToUniqueSet("Exploding Arrow");
 		    }
 	    }
 	    
 	    // This Ability should never miss, add a stack
		this.incrementStacks();
  		
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
		ret += "\n\tExplosion Scaler: " + this.getExplosionScaler();
		return ret;
	}
}