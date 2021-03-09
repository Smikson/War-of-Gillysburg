package WyattWitemeyer.WarOfGillysburg;

import java.util.LinkedList;

// ULTIMATE Ability: "Black Arrow"
public class BlackArrow extends UltimateAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double scalerAOE;
	private Condition selfPreAttackBonus;
	private Condition selfPreAttackBonusEmpowered;
	private Condition dragonDamageBonus;
	private boolean isEmpowered;
	private boolean usedBase;
	private boolean usedEmpowered;
	
	// Constructor
	public BlackArrow(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("ULTIMATE Ability: \"Black Arrow\"", source, rank);
		this.owner = source;
		
		// Sets the scaler and the scaler deduction of the Ability
		this.setScalers();
		
		// Sets the conditions
		this.setSelfPreAttackBonuses();
		this.setDragonEffect();
		
		// The Ability is always not Empowered until made so, and unused until used
		this.isEmpowered = false;
		this.usedBase = false;
		this.usedEmpowered = false;
	}
	
	// Sets the damage scaler and the amount by which it decreases per enemy hit
	private void setScalers() {
		// At rank 1 (default) the Ability deals 3x damage with a 1x AOE scaler
		this.scaler = 3.0;
		this.scalerAOE = 1.0;
		// At rank 2, the Ability deals 3.5x damage with a 1.25x AOE scaler
		if (this.rank() == 2) {
			this.scaler = 3.5;
			this.scalerAOE = 1.25;
		}
		// At rank 3, the Ability deals 4x damage with a 1.5x AOE scaler
		if (this.rank() == 3) {
			this.scaler = 4.0;
			this.scalerAOE = 1.5;
		}
	}
	
	// Sets the Accuracy Pre-Attack Bonus for this Ability
	private void setSelfPreAttackBonuses() {
		// At rank 1 neither exists, have value 0
		int accAmount = 0;
		int critAmount = 0;
		
		// At rank 2 it is +30% Accuracy and +10% Critical Chance
		if (this.rank() == 2) {
			accAmount = 30;
			critAmount = 10;
		}
		// At rank 3 it is +50% Accuracy and +25% Critical Chance
		if (this.rank() == 3) {
			accAmount = 50;
			critAmount = 25;
		}
		
		// Creates the normal Status Effects
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, accAmount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		// Creates the Condition with a duration of 0 since it is used only for this attack
		this.selfPreAttackBonus = new Condition("Black Arrow: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accBonus);
		this.selfPreAttackBonus.addStatusEffect(critBonus);
		
		
		// The empowered version grants an additional +50% Accuracy and +25% Critical Chance making 100% and 50% respectively
		int accAmtE = 100;
		int critAmtE = 50;
		
		// Creates the Empowered Status Effects
		StatusEffect accBonusE = new StatusEffect(Stat.Version.ACCURACY, accAmtE, StatusEffect.Type.OUTGOING);
		accBonusE.makePercentage();
		
		StatusEffect critBonusE = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmtE, StatusEffect.Type.OUTGOING);
		critBonusE.makeFlat();
		
		// Creates the Condition with a duration of 0 since it is used only for this attack
		this.selfPreAttackBonusEmpowered = new Condition("EMPOWERED Black Arrow: Pre Attack Bonus", 0);
		this.selfPreAttackBonusEmpowered.setSource(this.owner);
		this.selfPreAttackBonusEmpowered.addStatusEffect(accBonusE);
		this.selfPreAttackBonusEmpowered.addStatusEffect(critBonusE);
	}
	
	// Sets the Dragon Bonus Damage Condition
	private void setDragonEffect() {
		// Create the requirement for the bonus damage status effect
		Requirement isDragon = (Character withEffect) -> {
			return withEffect.hasType(Character.Type.DRAGON);
		};
		
		// Creates the StatusEffects (double damage is an increase of 100%)
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, 100, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		dmgBonus.setBasicRequirement(isDragon);
		
		// Creates the Conditions with duration 0 since it is applied and unapplied in a single attack
		this.dragonDamageBonus = new Condition("Black Arrow: Dragon Damage Bonus", 0);
		this.dragonDamageBonus.setSource(this.owner);
		this.dragonDamageBonus.addStatusEffect(dmgBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getScalerAOE() {
		return this.scalerAOE;
	}
	
	public Condition getSelfPreAttackBonus() {
		return new Condition(this.selfPreAttackBonus);
	}
	public Condition getEmpoweredSelfPreAttackBonus() {
		return new Condition(this.selfPreAttackBonusEmpowered);
	}
	public Condition getDragonDamageBonus() {
		return new Condition(this.dragonDamageBonus);
	}
	public Stun getStun(int duration) {
		return new Stun("Black Arrow: Stun", duration);
	}
	
	// Functions for "Empowered" effects
	public boolean isEmpowered() {
		return this.isEmpowered;
	}
	public void makeEmpowered() {
		// Only make empowered if rank 3 and empowered is unused
		if (this.rank() == 3 && !this.usedEmpowered) {
			this.setOffCooldown();
			this.isEmpowered = true;
		}
	}
	
	
	// Function to specify which version of the Ability is to be used (1 for default version, 2 for Empowered version)
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
				.cannotMiss()
				.ignoresArmor()
				.type(Attack.DmgType.TRUE)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDragonDamageBonus());
		
		// Apply the pre-attack bonus for critical calculations if above rank 2, and add it to the attack (though this theoretically has no effect)
		if (this.rank() >= 2) {
			baseAtkBld.addAttackerCondition(this.getSelfPreAttackBonus());
			this.owner.apply(this.getSelfPreAttackBonus());
			
			// Calculate additional critical chance based on the character's chance to hit (1 - defender's (dodge+block) / attacker's accuracy)
			int bonus = (int)Math.round(1.0 - (enemy.getDodge() + enemy.getBlock()) / this.owner.getAccuracy());
			
			// Create the status effect increasing crit by the bonus amount
			StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, bonus, StatusEffect.Type.OUTGOING);
			critBonus.makeFlat();
			
			// Create the condition with duration 0 as it is only for the attack
			Condition extraCrit = new Condition("Black Arrow: Accuracy-Based Critical Chance Bonus", 0);
			extraCrit.setSource(this.owner);
			extraCrit.addStatusEffect(critBonus);
			
			// Add this condition to the attack
			baseAtkBld.addAttackerCondition(extraCrit);
			
			// Calculate if the attack would critically strike with the Condition added (based on bonus amount)
			boolean willCrit = baseAtkBld.build().calcCrit(bonus);
			
			// Find the stun duration and set alterations for if we did crit and the rank of this Ability
			int stunDuration = 1;
			boolean guaranteeCrit = false;
			boolean canCrit = true;
			if (this.rank() == 3) {
				stunDuration++;
			}
			if (willCrit) {
				stunDuration++;
				guaranteeCrit = true;
			}
			else {
				canCrit = false;
			}
			
			// Alter the attack based on above
			baseAtkBld.addSuccessCondition(this.getStun(stunDuration)).canCrit(canCrit);
			if (guaranteeCrit) {
				baseAtkBld.guaranteedCrit();
			}
			
			// Unapply the pre-attack bonus
			this.owner.unapply(this.getSelfPreAttackBonus());
		}
		// Otherwise, just add the stun effect with duration 1
		else {
			baseAtkBld.addSuccessCondition(this.getStun(1));
		}
		
		// Execute the attack
		AttackResult res = baseAtkBld.build().execute();
		
		// If the attack failed, end the function without messing with cooldowns or turn actions or AOE effects
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// If the enemy died from the attack, the AOE occurrs
		if (res.didKill()) {
			// Select all enemies hit by the AOE, assume enemy must be hit (does not return on empty list, assumes none hit)
		    System.out.println("Choose enemies affected:");
	    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
	        if (targets.contains(Character.EMPTY)) {
	        	targets.clear();
	        }
	        
	        // Create a builder for the AOE attack
		    AttackBuilder atkBld = new AttackBuilder()
		    		.attacker(this.owner)
		    		.isAOE()
		    		.scaler(this.getScalerAOE() + this.owner.scalerBonus())
		    		.type(Attack.DmgType.TRUE)
		    		.range(Attack.RangeType.OTHER)
		    		.addAttackerCondition(this.getDragonDamageBonus());
		    
		    // If res critically struck above rank 2, this critically strikes
		    if (this.rank() >= 2 && res.didCrit()) {
		    	atkBld.guaranteedCrit();
		    }
		    
		    // Execute the attack against all foes
		    for (Character c : targets) {
		    	Attack atk = atkBld.defender(c).build();
		    	atk.execute();
		    }
		}
		
		// Say that we used the basic version of this Ability, and it is on Cooldown
		this.usedBase = true;
		this.setOnCooldown();
		
		// Add "Black Arrow" to the set of unique abilities used
 		this.owner.addToUniqueSet("Black Arrow");
 		
 		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use(2): Empowered version of Ability
	public void useEmpowered() {
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
				.scaler(this.getScaler() + this.owner.scalerBonus() + 1.0)
				.cannotMiss()
				.ignoresArmor()
				.type(Attack.DmgType.TRUE)
				.range(Attack.RangeType.RANGED)
				.addAttackerCondition(this.getDragonDamageBonus())
				.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
		
		// Assuming rank 3 is achieved, apply the Empowered pre-attack bonus for critical calculations, and add it to the attack (though this theoretically has no effect)
		baseAtkBld.addAttackerCondition(this.getEmpoweredSelfPreAttackBonus());
		this.owner.apply(this.getEmpoweredSelfPreAttackBonus());
		
		// Calculate additional critical chance based on the character's chance to hit (1 - defender's (dodge+block) / attacker's accuracy)
		int bonus = (int)Math.round(1.0 - (enemy.getDodge() + enemy.getBlock()) / this.owner.getAccuracy());
		
		// Create the status effect increasing crit by the bonus amount
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, bonus, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		// Create the condition with duration 0 as it is only for the attack
		Condition extraCrit = new Condition("EMPOWERED Black Arrow: Accuracy-Based Critical Chance Bonus", 0);
		extraCrit.setSource(this.owner);
		extraCrit.addStatusEffect(critBonus);
		
		// Add this condition to the attack
		baseAtkBld.addAttackerCondition(extraCrit);
		
		// Calculate if the attack would critically strike with the Condition added (based on bonus amount)
		boolean willCrit = baseAtkBld.build().calcCrit(bonus);
		
		// Find the stun duration and set alterations for if we did crit and the rank of this Ability
		int stunDuration = 3;
		boolean guaranteeCrit = false;
		boolean canCrit = true;
		if (willCrit) {
			stunDuration++;
			guaranteeCrit = true;
		}
		else {
			canCrit = false;
		}
		
		// Alter the attack based on above
		baseAtkBld.addSuccessCondition(this.getStun(stunDuration)).canCrit(canCrit);
		if (guaranteeCrit) {
			baseAtkBld.guaranteedCrit();
		}
		
		// Unapply the Empowered pre-attack bonus
		this.owner.unapply(this.getEmpoweredSelfPreAttackBonus());
		
		// Execute the attack
		AttackResult res = baseAtkBld.build().execute();
		
		// If the attack failed, end the function without messing with cooldowns or turn actions or AOE effects
		if (res.equals(AttackResult.EMPTY)) {
			return;
		}
		
		// If the enemy died from the attack, the AOE occurrs
		if (res.didKill()) {
			// Select all enemies hit by the AOE, assume enemy must be hit (does not return on empty list, assumes none hit)
		    System.out.println("Choose enemies affected:");
	    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
	        if (targets.contains(Character.EMPTY)) {
	        	targets.clear();
	        }
	        
	        // Create a builder for the AOE attack
		    AttackBuilder atkBld = new AttackBuilder()
		    		.attacker(this.owner)
		    		.isAOE()
		    		.scaler(this.getScalerAOE() + this.owner.scalerBonus() + .5)
		    		.type(Attack.DmgType.TRUE)
		    		.range(Attack.RangeType.OTHER)
		    		.addAttackerCondition(this.getDragonDamageBonus())
		    		.addAttackerCondition(this.owner.getEmpoweredPreAttackBonus());
		    
		    // If res critically struck, this critically strikes
		    if (res.didCrit()) {
		    	atkBld.guaranteedCrit();
		    }
		    
		    // Execute the attack against all foes
		    for (Character c : targets) {
		    	Attack atk = atkBld.defender(c).build();
		    	atk.execute();
		    }
		}
		
		// Say that we used the Empowered version of this Ability, and set it on Cooldown if we also have already used the base version
		this.usedEmpowered = true;
		if (this.usedBase) {
			this.setOnCooldown();
		}
		
		// Add "EMPOWERED Black Arrow" to the set of unique abilities used (since level 100 is required, assume the proper level of Multi-Purposed is reached)
 		this.owner.addToUniqueSet("EMPOWERED Black Arrow");
 		
 		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	
	
	// Override the to-String to include if the Ability is Empowered
	@Override
	public String toString() {
		String ori = super.toString();
		if (this.isEmpowered()) {
			return ori + " --EMPOWERED";
		}
		return ori;
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tAOE Scaler: " + this.getScalerAOE();
		ret += this.rank() >= 2? ("\n\t" + this.getSelfPreAttackBonus()) : "";
		ret += this.rank() >= 3? ("\n\t" + this.getEmpoweredSelfPreAttackBonus()) : "";
		return ret;
	}
}