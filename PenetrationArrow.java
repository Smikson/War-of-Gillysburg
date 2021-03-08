package WyattWitemeyer.WarOfGillysburg;

import java.util.LinkedList;

// Ability 4: "Penetration Arrow"
public class PenetrationArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private int stdDownAmt;
	private int stdUpAmt;
	private Condition enemyArmorReduction;
	private int numStacks;
	
	// Constructor
	public PenetrationArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Penetration Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the Cooldown and the scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the STD bonuses and enemy Condition of the Ability
		this.setSTDamounts();
		this.setEnemyArmorCondition();
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
	
	// Sets the default scaler of the Ability
	private void setScaler() {
		// Set the scaler based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = .75;
				break;
			case 2:
				this.scaler = .8;
				break;
			case 3:
				this.scaler = .9;
				break;
			case 4:
				this.scaler = 1;
				break;
			case 5:
				this.scaler = 1;
				break;
			case 6:
				this.scaler = 1.2;
				break;
			case 7:
				this.scaler = 1.3;
				break;
			case 8:
				this.scaler = 1.4;
				break;
			case 9:
				this.scaler = 1.5;
				break;
			case 10:
				this.scaler = 1.5;
				break;
		}
	}
	
	// Sets the STD bonuses for the Ability
	private void setSTDamounts() {
		// For STD down, it is always an additional 5% until rank 10 when it drops to 0%
		this.stdDownAmt = 5;
		if (this.rank() == 10) {
			this.stdDownAmt = 0;
		}
		
		// For STD up, it starts at 5% then increases to 10% at rank 3, 15% at rank 7, and 20% at rank 10
		this.stdUpAmt = 5;
		if (this.rank() >= 3) {
			this.stdUpAmt = 10;
		}
		if (this.rank() >= 7) {
			this.stdUpAmt = 15;
		}
		if (this.rank() == 10) {
			this.stdUpAmt = 20;
		}
	}
	
	// Sets the enemy Armor Reduction Condition for the Ability
	private void setEnemyArmorCondition() {
		// Calculate the amount of armor ignored, default of 0
		int amount = 0;
		
		// Increases at the specified ranks in the for loop
		for (int walker = 3; walker <= this.rank(); walker++) {
			// Rank 3 starts the amount at 10%
			if (walker == 3) {
				amount += 10;
			}
			// Rank 4 increases the amount by 2%
			if (walker == 4) {
				amount += 2;
			}
			// Rank 6 increases the amount by 3%
			if (walker == 6) {
				amount += 3;
			}
			// Ranks 8, 9, and 10 increase the amount by 5%
			if (walker == 8 || walker == 9 || walker == 10) {
				amount += 5;
			}
		}
		
		// Create the Status Effect
		StatusEffect armorRed = new StatusEffect(Stat.Version.ARMOR, -amount, StatusEffect.Type.INCOMING);
		armorRed.makePercentage();
		
		// Create the Condition with a duration of 0 since it is only for the one attack
		this.enemyArmorReduction = new Condition("Penetration Arrow: Armor Reduction", 0);
		this.enemyArmorReduction.setSource(this.owner);
		this.enemyArmorReduction.addStatusEffect(armorRed);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public Condition getSTDeffects(boolean isEmpowered) {
		// When Empowered, stdDown is set to 0
		int empAmt = 100 - this.owner.getSTDdown();
		if (empAmt < 0) {
			empAmt = 0;
		}
		
		// Create the Status Effects 
		StatusEffect stdDownNormal = new StatusEffect(Stat.Version.STANDARD_DEVIATION_DOWN, -this.stdDownAmt, StatusEffect.Type.OUTGOING);
		stdDownNormal.makeFlat();
		
		StatusEffect stdDownEmpowered = new StatusEffect(Stat.Version.STANDARD_DEVIATION_DOWN, empAmt, StatusEffect.Type.OUTGOING);
		stdDownEmpowered.makeFlat();
		
		StatusEffect stdUp = new StatusEffect(Stat.Version.STANDARD_DEVIATION_UP, this.stdUpAmt, StatusEffect.Type.OUTGOING);
		stdUp.makeFlat();
		
		// Create the Condition (only include the downs when they are non-zero, up is always present)
		Condition stdEffects = new Condition("Piercing Arrow: STD Effects", 0);
		stdEffects.setSource(this.owner);
		stdEffects.addStatusEffect(stdUp);
		if (isEmpowered && empAmt != 0) {
			stdEffects.addStatusEffect(stdDownEmpowered);
		}
		if (!isEmpowered && this.stdDownAmt != 0) {
			stdEffects.addStatusEffect(stdDownNormal);
		}
		
		return stdEffects;
	}
	
	public Condition getEnemyArmorReduction() {
		return new Condition(this.enemyArmorReduction);
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
		
		// Select all enemies hit by the AOE, assume enemy must be hit
	    System.out.println("Choose enemies affected:");
    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
    	if (targets.isEmpty()) {
        	return;
        }
        if (targets.contains(Character.EMPTY)) {
        	targets.clear();
        }
        
        // Create a builder for the AOE attack
	    AttackBuilder atkBld = new AttackBuilder()
	    		.attacker(this.owner)
	    		.isAOE()
	    		.scaler(this.getScaler() + this.owner.scalerBonus())
	    		.type(Attack.DmgType.PIERCING)
	    		.range(Attack.RangeType.RANGED)
	    		.addAttackerCondition(this.getSTDeffects(false));
	    
	    // If above rank 3, apply the Armor reduction as well
	    if (this.rank() >= 3) {
	    	atkBld.addDefenderCondition(this.getEnemyArmorReduction());
	    }
	    
	    // Execute the attack against all foes
	    for (Character c : targets) {
	    	Attack atk = atkBld.defender(c).build();
	    	atk.execute();
	    }
	    
	    // Add basic "Penetration Arrow" to the set of unique abilities used
 		this.owner.addToUniqueSet("Penetration Arrow");
 		
 		// This Ability should never miss, add a stack
 		this.incrementStacks();
 		
 		// Put Penetration Arrow "on Cooldown"
		this.setOnCooldown();
		
		// If we are EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
		
		// At rank 10, if we hit at least 5 enemies, randomly Empower
		if (this.rank() == 10 && targets.size() >= 5) {
			this.owner.randomlyEmpower();
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use(2): Empowered version of Ability
	public void useEmpowered(double scalerPortion) {
		// Select all enemies hit by the AOE, assume enemy must be hit
	    System.out.println("Choose enemies affected:");
    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
    	if (targets.isEmpty()) {
        	return;
        }
        if (targets.contains(Character.EMPTY)) {
        	targets.clear();
        }
        
        // Create a builder for the AOE attack
	    AttackBuilder atkBld = new AttackBuilder()
	    		.attacker(this.owner)
	    		.isAOE()
	    		.scaler((this.getScaler() + this.owner.scalerBonus()) * scalerPortion)
	    		.type(Attack.DmgType.PIERCING)
	    		.range(Attack.RangeType.RANGED)
	    		.ignoresArmor()
	    		.addAttackerCondition(this.getSTDeffects(true))
	    		.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
	    
	    // If above rank 3, apply the Armor reduction as well
	    if (this.rank() >= 3) {
	    	atkBld.addDefenderCondition(this.getEnemyArmorReduction());
	    }
	    
	    // The Ability now has a chance to critically strike, if it does, change the attack to be a guarantee
	    if (atkBld.build().calcCrit()) {
	    	atkBld.guaranteedCrit();
	    }
	    
	    // Execute the attack against all foes
	    for (Character c : targets) {
	    	Attack atk = atkBld.defender(c).build();
	    	atk.execute();
	    }
	    
	    // Add "Penetration Arrow" or "EMPOWERED Penetration Arrow" to the set of unique abilities used based on rank of Masterwork Arrows
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
	    	this.owner.addToUniqueSet("EMPOWERED Penetration Arrow");
	    }
	    else {
	    	this.owner.addToUniqueSet("Penetration Arrow");
	    }
	    
	    // Reset the stacks, then increment the stacks if Empowered Arrows is at least rank 4
 		this.resetStacks();
 		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
 			this.incrementStacks();
 		}
 		
 		// Put Penetration Arrow "on Cooldown" if Empowered Arrows is less than rank 2
 		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
 			this.setOnCooldown();
 		}
 		
 		// If we are somehow EMPOWERED after adding stacks, refresh all Cooldowns for this ability
 		if (this.isEmpowered()) {
 			this.setOffCooldownAll();
 		}
 		
 		// At rank 10, if we hit at least 5 enemies, randomly Empower
		if (this.rank() == 10 && targets.size() >= 5) {
			this.owner.randomlyEmpower();
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
		
		// Select all enemies hit by the AOE, assume enemy must be hit
	    System.out.println("Choose enemies affected:");
    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
    	if (targets.isEmpty()) {
        	return;
        }
        if (targets.contains(Character.EMPTY)) {
        	targets.clear();
        }
        
        // Create a builder for the AOE attack
	    AttackBuilder atkBld = new AttackBuilder()
	    		.attacker(this.owner)
	    		.isAOE()
	    		.scaler(this.getScaler() + this.owner.scalerBonus())
	    		.type(Attack.DmgType.PIERCING)
	    		.range(Attack.RangeType.RANGED)
	    		.addAttackerCondition(this.getSTDeffects(isEmpoweredVersion))
	    		.addAttackerCondition(this.owner.getRandomAbilityPreAttackBonus());
	    
	    // If above rank 3, apply the Armor reduction as well
	    if (this.rank() >= 3) {
	    	atkBld.addDefenderCondition(this.getEnemyArmorReduction());
	    }
	    
	    // Alter the builder based on if this is the Empowered Version
	    if (isEmpoweredVersion) {
	    	atkBld.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
	    	if (atkBld.build().calcCrit()) {
		    	atkBld.guaranteedCrit();
		    }
	    }
	    
	    // Execute the attack against all foes
	    for (Character c : targets) {
	    	Attack atk = atkBld.defender(c).build();
	    	atk.execute();
	    }
	    
	    // Clear the set of unique abilities
	  	this.owner.clearUniqueSet();
	    
	    // Add "Penetration Arrow" or "EMPOWERED Penetration Arrow" to the set of unique abilities used based on rank of Masterwork Arrows and of Multi-Purposed
	  	if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed) >= 5) {
	  		if (isEmpoweredVersion && this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
		    	this.owner.addToUniqueSet("EMPOWERED Penetration Arrow");
		    }
		    else {
		    	this.owner.addToUniqueSet("Penetration Arrow");
		    }
	  	}
	  	
	  	// This Ability should never miss, add a stack
	 	this.incrementStacks();
	 	
	 	// If we are EMPOWERED after adding stacks, refresh all Cooldowns for this ability
  		if (this.isEmpowered()) {
  			this.setOffCooldownAll();
  		}
  		
  		// At rank 10, if we hit at least 5 enemies, randomly Empower
		if (this.rank() == 10 && targets.size() >= 5) {
			this.owner.randomlyEmpower();
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
		ret += this.rank() >= 3? ("\n\t" + this.getEnemyArmorReduction()) : "";
		return ret;
	}
}