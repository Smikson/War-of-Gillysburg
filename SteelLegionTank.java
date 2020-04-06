package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

// Passive Abilities:
// Unique Passive Ability: "Hold It Right There!"
class HoldItRightThere extends Ability {
	// Additional Effects of Ability
	private Condition selfBlock;
	private Condition enemyHalt;
	
	// Constructor
	public HoldItRightThere(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Unique Passive Ability: \"Hold It Right There!\"";
		this.rank = rank;
		
		// Calculate the additional Staus Effects
		this.setSelfBlock();
		this.setEnemyHalt();
	}
	
	
	// Additional Status Effects for this Ability:
	
	// Increased Block when blocking for an ally
	private void setSelfBlock() {
		// Find block amount based on rank
		int amount = 0;
		for (int i = 1; i <= this.rank; i++) {
			// Ranks 2,5 grant +10% increased Block
			if (i == 2 || i == 5) {
				amount += 10;
			}
			// Ranks 3,4 grant +5% increased Block
			if (i == 3 || i == 4) {
				amount += 5;
			}
		}
		
		// Create the block Condition with the correct block Status Effect based on the numbers calculated
		this.selfBlock = new Condition("Hold It Right There!: Self Block Bonus", -1);
		this.selfBlock.makePermanent();
		this.selfBlock.setSource(this.owner);
		
		StatusEffect blockBonus = new StatusEffect(StatVersion.BLOCK, amount, StatusEffectType.INCOMING);
		DualRequirement req = (Character withEffect, Character other) -> {
			// Prompt controller if blocking for an ally
			System.out.println("Is " + this.owner.getName() + " blocking for an ally? Y or N");
			return BattleSimulator.getInstance().askYorN();
		};
		blockBonus.setDualRequirement(req);
		
		this.selfBlock.addStatusEffect(blockBonus);
		
		// Since it is permanent, this Condition will start attached to the Character if necessary (Rank 2 or above)
		if (this.rank >= 2) {
			this.owner.addCondition(this.selfBlock);
		}
	}
	
	// Provides the Halt condition with increased damage to halted enemies
	private void setEnemyHalt() {
		// Find damage bonus amount based on rank
		int amount = 0;
		if (this.rank == 4) {
			amount = 20;
		}
		if (this.rank == 5) {
			amount = 30;
		}
		
		// Create the Halt Damage Condition with the correct Status Effect(s) based on the numbers calculated
		this.enemyHalt = new Condition("Hold It Right There!: Halted - Enemy Damage Bonus", 2);
		this.enemyHalt.setSource(this.owner);
		
		StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.INCOMING);
		damageBonus.makeAffectOther();
		
		
		// At rank 5, bonus damage from the owner only is increased to an amount of 50
		if (this.rank == 5) {
			// Creates additional statusEffect for owner only
			StatusEffect ownerDamageBonus = new StatusEffect(StatVersion.DAMAGE, 50, StatusEffectType.INCOMING);
			ownerDamageBonus.makeAffectOther();
			DualRequirement ownerReq = (Character withEffect, Character other) -> {
				return other.equals(this.owner);
			};
			ownerDamageBonus.setDualRequirement(ownerReq);
			this.enemyHalt.addStatusEffect(ownerDamageBonus);
			
			// Creates a requirement for the original to exclude the owner (so both do not apply)
			DualRequirement req = (Character withEffect, Character other) -> {
				return !(other.equals(this.owner));
			};
			damageBonus.setDualRequirement(req);
		}
		
		this.enemyHalt.addStatusEffect(damageBonus);
	}
	
	// Get methods for additional effects and owner/source
	public Condition getSelfBlockCondition() {
		this.setSelfBlock();
		return this.selfBlock;
	}
	public Condition getEnemyHaltCondition() {
		this.setEnemyHalt();
		return this.enemyHalt;
	}
}

// Base Passive Ability 1: "Enchanted Armor"
class EnchantedArmor extends Ability {
	// Additional variables for the Ability
	private int bonusArmor;
	private int bonusBlock;
	
	// Constructors
	public EnchantedArmor(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Enchanted Armor\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the healing scaler and base stat amounts
		this.setScaler();
		this.setArmorBonus();
		this.setBlockBonus();
	}
	public EnchantedArmor(int rank) {
		this(Character.EMPTY, rank);
	}
	public EnchantedArmor() {
		super();
		this.bonusArmor = 0;
		this.bonusBlock = 0;
	}
	
	// Calculates the base scaler (used as a healing scaler) for this Ability
	private void setScaler() {
		// At rank 0, this scaler starts at .02
		this.scaler = .02;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +.5% to scaler
			if (walker <= 5) {
				this.scaler += .005;
			}
			// Ranks 6-10 grant +.7% to scaler
			else if (walker <= 10) {
				this.scaler += .007;
			}
			// Ranks 11-14 grant +1% to scaler
			else if (walker <= 14) {
				this.scaler += .01;
			}
			// Rank 15 grants +3% to scaler
			else if (walker <= 15) {
				this.scaler += .03;
			}
		}
	}
	
	
	// Calculates the values for the additional stats of this Ability
	private void setArmorBonus() {
		// Starting at 0:
		this.bonusArmor = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +8 bonus Armor
			if (walker <= 5) {
				this.bonusArmor += 8;
			}
			// Ranks 6-10 grant +10 bonus Armor
			else if (walker <= 10) {
				this.bonusArmor += 10;
			}
			// Ranks 11-14 grant +15 bonus Armor
			else if (walker <= 14) {
				this.bonusArmor += 15;
			}
			// Rank 15 grants +20 bonus Armor
			else if (walker <= 15) {
				this.bonusArmor += 20;
			}
		}
	}
	
	private void setBlockBonus() {
		// Starting at 0:
		this.bonusBlock = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-5 grant +8 bonus Block
			if (walker <= 5) {
				this.bonusBlock += 2;
			}
			// Ranks 6-10 grant +10 bonus Block
			else if (walker <= 10) {
				this.bonusBlock += 3;
			}
			// Ranks 11-14 grant +15 bonus Block
			else if (walker <= 14) {
				this.bonusBlock += 4;
			}
			// Rank 15 grants +20 bonus Block
			else if (walker <= 15) {
				this.bonusBlock += 7;
			}
		}
	}
	
	// Get methods for additional stats of this Ability and source
	public int getBonusArmor() {
		this.setArmorBonus();
		return this.bonusArmor;
	}
	public int getBonusBlock() {
		this.setBlockBonus();
		return this.bonusBlock;
	}
}

// Base Passive Ability 2: "Shield Skills"
class ShieldSkills extends Ability {
	// Additional variables for ability
	private double scalerBlind3;
	private double scalerBlind5;
	
	public ShieldSkills(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Shield Skills\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// At rank 1, the healing scaler for blinding 3 enemies starts at .02, and there is no extra benefit for blinding 5
		this.scalerBlind3 = .02;
		this.scalerBlind5 = 0;
		
		// Adjust for current rank
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2-5 grant +2% to scaler for blinding 3
			if (walker <= 5) {
				this.scalerBlind3 += .02;
			}
			// Ranks 11-15 grant +3% to a second scaler for each rank
			else if (walker >= 11 && walker <= 15) {
				this.scalerBlind5 += .03;
			}
		}
		// Use Scaler for blinding 3 people for default
		this.scaler = this.scalerBlind3;
	}
	
	// Get methods for additional scalers and source
	public double getScalerBlind3() {
		return this.scalerBlind3;
	}
	public double getScalerBlind5() {
		return this.scalerBlind5;
	}
}

// Base Passive Ability 3: "Professional Laughter"
class ProfessionalLaughter extends Ability {
	// Additional variables for the Ability
	private int bonusThreat;
	private int bonusTacticalThreat;
	private int bonusHealth;
	
	public ProfessionalLaughter(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Base Passive Ability: \"Professional Laughter\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the healing scaler and base stat amounts
		this.setThreatBonus();
		this.setTacticalThreatBonus();
		this.setHealthBonus();
	}
	public ProfessionalLaughter(int rank) {
		this(Character.EMPTY, rank);
	}
	
	// Calculates the values for the additional stats of this Ability
	private void setThreatBonus() {
		// Starting at 0
		this.bonusThreat = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-2 grant +5 bonus Threat
			if (walker <= 2) {
				this.bonusThreat += 5;
			}
			// Ranks 3-5 grant +7 bonus Threat
			else if (walker <= 5) {
				this.bonusThreat += 7;
			}
			// Ranks 6-10 grant +8 bonus Threat
			else if (walker <= 10) {
				this.bonusThreat += 8;
			}
			// Ranks 11-14 grant +10 bonus Threat
			else if (walker <= 14) {
				this.bonusThreat += 10;
			}
			// Rank 15 grants +15 bonus Threat
			else if (walker <= 15) {
				this.bonusThreat += 15;
			}
		}
	}
	
	private void setTacticalThreatBonus() {
		// Starting at 0
		this.bonusTacticalThreat = 0;
		
		// First rank that grants bonus Tactical Threat is Rank 11
		for (int walker = 11; walker <= this.rank; walker++) {
			// Ranks 11-14 grant +3 bonus Tactical Threat
			if (walker <= 14) {
				this.bonusTacticalThreat += 3;
			}
			// Rank 15 grants +5 bonus Tactical Threat
			else if (walker <= 15) {
				this.bonusTacticalThreat += 5;
			}
		}
	}
	
	private void setHealthBonus() {
		// Starting at 0
		this.bonusHealth = 0;
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 1-2 grant +100 bonus Health
			if (walker <= 2) {
				this.bonusHealth += 100;
			}
			// Ranks 3-5 grant +225 bonus Health
			else if (walker <= 5) {
				this.bonusHealth += 225;
			}
			// Ranks 6-10 grant +350 bonus Health
			else if (walker <= 10) {
				this.bonusHealth += 350;
			}
			// Ranks 11-14 grant +625 bonus Health
			else if (walker <= 14) {
				this.bonusHealth += 625;
			}
			// Rank 15 grants +1725 bonus Health
			else if (walker <= 15) {
				this.bonusHealth += 1725;
			}
		}
	}
	
	// Get methods for additional stats of this Ability
	public int getBonusThreat() {
		this.setThreatBonus();
		return this.bonusThreat;
	}
	public int getBonusTacticalThreat() {
		this.setTacticalThreatBonus();
		return this.bonusTacticalThreat;
	}
	public int getBonusHealth() {
		this.setHealthBonus();
		return this.bonusHealth;
	}
}


// Basic Abilities:
// Ability 1: "Shield Bash"
class ShieldBash extends Ability {
	// Additional Conditions for the Ability
	private Stun stun;
	private Condition enemyAccReduction;
	private Condition selfPreAttackBonus;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Shield Bash misses, if a previous attack has been blocked, and if this Ability critically hit (for bonus effects)
	public int numMisses;
	public boolean didBlock;
	public boolean didCrit;
	
	
	public ShieldBash(Character source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 1: \"Shield Bash\"";
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		this.didBlock = false;
		this.didCrit = false;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Sets the additional effects of the Ability
		this.setStun();
		this.setEnemyAccReduction();
		this.setPreAttackBonus();
	}
	public ShieldBash(int rank) {
		this(Character.EMPTY, rank, 0);
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 4;
		if (this.rank >= 5) {
			this.cooldown = 3;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// Checks based on this Ability's rank
		// At rank 1, this scaler starts at .7
		this.scaler = .7;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2,7 grants +.1 to scaler
			if (walker == 2 || walker == 7) {
				this.scaler += .1;
			}
			// Ranks 3,4,6,8,9 grant +.2 to scaler
			else if (walker == 3 || walker == 4 || walker == 6 || walker == 8 || walker == 9) {
				this.scaler += .2;
			}
			// Ranks 5,10 grant +.3 to scaler
			else if (walker == 5 || walker == 10) {
				this.scaler += .3;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		for (int walker = 6; walker < this.ssRank; walker++) {
			// Ranks 6-10 grant +.2x damage to scaler if an attack was blocked
			if (walker <= 10 && this.didBlock) {
				this.scaler += .2;
			}
			// Rank 15 grants +.5x damage to scaler if an attack was blocked
			else if (walker == 15 && this.didBlock) {
				this.scaler += .5;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setStun() {
		int stunDuration = 1;
		
		// Checks based on this Ability's rank
		// Rank 10 increases the duration to 2, then is increased by an additional turn if the Ability didCrit
		if (this.rank >= 10) {
			stunDuration = 2;
			if (this.didCrit) {
				stunDuration++;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		// Rank 15 grants an additional stun turn
		if (this.ssRank >= 15) {
			stunDuration++;
		}
		
		// Sets the calculated Stun
		this.stun = new Stun("Shield Bash: Stun", stunDuration);
		this.stun.setSource(this.owner);
		
		// Adds Damage Bonus While Stunned after rank 5
		if (this.rank >= 5) {
			int amount = 0;
			for (int walker = 1; walker <= this.rank; walker++) {
				// Rank 5 grants +20% damage to the stunned target
				if (walker == 5) {
					amount += 20;
				}
				// Ranks 6,10 grants +5% damage to the stunned target
				if (walker == 6 || walker == 10) {
					amount += 5;
				}
			}
			// the value "amount" doubles when you critically strike at rank 10
			if (this.rank >= 10 && this.didCrit) {
				amount *= 2;
			}
			
			// Sets the Calculated Status Effect and adds it to the stun
			StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.INCOMING);
			damageBonus.makeAffectOther();
			this.stun.addStatusEffect(damageBonus);
		}
	}
	
	private void setEnemyAccReduction() {
		int amount = 0;
		// Checks based on this Ability's rank
		for (int walker = 1; walker <= this.rank; walker++) {
			// Ranks 3,7 grant +10% Accuracy Reduction
			if (walker == 3 || walker == 7) {
				amount += 10;
			}
			// Ranks 4,6 grant +5% Accuracy Reduction
			if (walker == 4 || walker == 6) {
				amount += 5;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		for (int walker = 11; walker <= this.ssRank; walker++) {
			// Ranks 11-15 grant +2% extra accuracy deduction if an attack was blocked
			if (walker <= 15 && this.didBlock) {
				amount += 2;
			}
			// Rank 15 grants an additional 5% accuracy deduction on top of that (total of a bonus 7% from rank 15) if an attack was blocked
			if (walker == 15 && this.didBlock) {
				amount += 5;
			}
		}
		
		// the value "amount" doubles when you critically strike at rank 10
		if (this.rank >= 10 && this.didCrit) {
			amount *= 2;
		}
		
		// Creates the requirement of the Condition to become Active (occurs after stun)
		Requirement actReq = (Character withEffect) -> {
			return !withEffect.getAllConditions().contains(this.stun);
		};
		
		// Finds the duration: 2 when >= 8
		int duration = this.rank < 8? 1 : 2;
		
		// Create the Enemy Accuracy Reduction Condition with the correct Status Effect based on the numbers calculated
		this.enemyAccReduction = new Condition("Shield Bash: Accuracy Reduction", duration, actReq);
		this.enemyAccReduction.setSource(this.owner);
		// Sets the Calculated Status Effect
		StatusEffect accReduction = new StatusEffect(StatVersion.ACCURACY, -amount, StatusEffectType.OUTGOING);
		this.enemyAccReduction.addStatusEffect(accReduction);
	}
	
	private void setPreAttackBonus() {
		// Accuracy Bonus:
		// Bonuses for "Shield Skills" rank
		int accAmount = 0;
		
		// Base of 10%, increases for each rank
		if (this.ssRank >= 1) {
			accAmount += 10;
		}
		for (int walker = 1; walker <= this.ssRank; walker++) {
			// Ranks 1-5 grant +1% Accuracy (starting at 10%)
			if (walker <= 5) {
				accAmount += 1;
			}
			// Ranks 6-10 grant +2% Accuracy
			else if (walker <= 10) {
				accAmount += 2;
			}
			// Ranks 11-15 grant +3% Accuracy
			else if (walker <= 15) {
				accAmount += 3;
			}
		}
		// Creates the Calculated Status Effect based on the number of misses
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, accAmount*this.numMisses, StatusEffectType.OUTGOING);
		
		// Crit Bonus:
		// Bonuses for "Shield Skills" rank
		int critAmount = 0;
		for (int walker = 6; walker <= this.ssRank; walker++) {
			// Ranks 6-10 grant +2% Critical Chance
			if (walker <= 10) {
				critAmount += 2;
			}
			// Ranks 11-15 grant +3% Critical Chance
			else if (walker <= 15) {
				critAmount += 3;
			}
		}
		// Creates the calculated Status Effect
		StatusEffect critBonus = new StatusEffect(StatVersion.CRITICAL_CHANCE, critAmount, StatusEffectType.OUTGOING);
		
		// Creates the pre-attack condition with accuracy and crit bonuses as needed
		this.selfPreAttackBonus = new Condition("Shield Bash: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
		this.selfPreAttackBonus.addStatusEffect(critBonus);
	}
	
	
	// Get methods for additional effects
	public Stun getStunEffect() {
		// Returns the stun, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setStun();
		return this.stun;
	}
	
	public Condition getEnemyAccuracyReduction() {
		// Returns the condition, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setEnemyAccReduction();
		return this.enemyAccReduction;
	}
	
	public Condition getSelfPreAttackBonus() {
		// Returns the condition, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setPreAttackBonus();
		return this.selfPreAttackBonus;
	}
	
	@Override
	public double getScaler() {
		// Returns the scaler, but sets it again first in case things changed because of "didCrit" or "didBlock" or "numMisses"
		this.setScaler();
		return this.scaler;
	}
}

// Ability 2: "Shield Reflection"
class ShieldReflection extends Ability {
	// Additional Condition for the Ability
	private Blind blind;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of if a previous attack has been blocked
	public boolean didBlock;
	
	public ShieldReflection(Character source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 2: \"Shield Reflection\"";
		this.ssRank = ShieldSkillsRank;
		this.didBlock = false;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Sets additional effects of the Ability
		this.setBlind();
	}
	public ShieldReflection(int rank) {
		this(Character.EMPTY, rank, 0);
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 5;
		if (this.rank >= 5) {
			this.cooldown = 4;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at .1
		this.scaler = .1;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2-6 but not 3 grant +.05 to scaler
			if (walker <= 6 && walker != 3) {
				this.scaler += .05;
			}
			// Ranks 8 and 10 grant +.1 to scaler
			else if (walker == 8 || walker == 10) {
				this.scaler += .1;
			}
		}
		
		// Bonuses for "Shield Skills" rank
		for (int walker = 6; walker < this.ssRank; walker++) {
			// Ranks 6-10 grant +.2x damage to scaler if an attack was blocked
			if (walker <= 10 && this.didBlock) {
				this.scaler += .2;
			}
			// Rank 15 grants +.5x damage to scaler if an attack was blocked
			else if (walker == 15 && this.didBlock) {
				this.scaler += .5;
			}
		}
	}
	
	// Calculates the value for the additional blind effect for this Ability
	public void setBlind() {
		// Calculates blind duration
		int blindDuration = 1;
		if (this.rank >= 3) {
			blindDuration = 2;
		}
		
		// Sets the blind effect
		this.blind = new Blind("Shield Reflection: Blind", blindDuration);
		this.blind.setSource(this.owner);
	}
	
	// Get methods for additional effects as necessary
	public Blind getBlindEffect() {
		// For good habit and in case of future changes, should still set it again before returning it
		this.setBlind();
		return this.blind;
	}
	@Override
	public double getScaler() {
		// Returns the scaler, but sets it again first in case things changed because of "didBlock" or "numMisses"
		this.setScaler();
		return this.scaler;
	}
}

// Ability 3: "Taunting Attack"
class TauntingAttack extends Ability {
	// Additional Conditions for the Ability
	private Condition selfPreAttackBonus;
	private Condition taunt;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Taunting Attack misses (for bonus effects)
	public int numMisses;
	
	public TauntingAttack(Character source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 3: \"Taunting Attack\"";
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Set the additional effects of the Ability
		this.setPreAttackBonus();
		this.setTauntEffect();
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 4;
		if (this.rank >= 5) {
			this.cooldown = 3;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at 1.0
		this.scaler = 1.0;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 3,4,5,10 grants +.1 to scaler
			if (walker == 3 || walker == 4 || walker == 5 || walker == 10) {
				this.scaler += .1;
			}
			// Ranks 2,6,8,9 grant +.2 to scaler
			else if (walker == 2 || walker == 6 || walker == 8 || walker == 9) {
				this.scaler += .2;
			}
			// Rank 7 grant +.3 to scaler
			else if (walker == 7) {
				this.scaler += .3;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setPreAttackBonus() {
		int amount = 0;
		if (this.ssRank >= 15) {
			amount = 40;
		}
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, amount*this.numMisses, StatusEffectType.OUTGOING);
		
		// Creates the pre-attack condition with accuracy bonus as needed
		this.selfPreAttackBonus = new Condition("Taunting Attack: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
	}
	
	private void setTauntEffect() {
		int duration = 1;
		if (this.rank >= 10) {
			duration = 2;
		}
		this.taunt = new Condition("Taunting Attack: Taunted", duration);
	}
	
	// Get methods for additional effects of this Ability
	public Condition getTauntEffectHit() {
		this.setTauntEffect();
		return this.taunt;
	}
	
	public Condition getTauntEffectMiss(Character enemy) {
		// If the Ability misses, there is still a chance (based on rank) that a Taunt effect occurs (returns a Condition with duration = 0 if it does not occur)
		int basicChance = 0;  // For Normal and Advanced Enemies (Level = 1,2)
		int eliteChance = 0;  // For Elite Enemies (Level = 3)
		int bossChance = 0;   // For Boss Enemies (Level = 4)
		
		// At rank 3, there is a chance to still Taunt basic (Normal/Advanced) and Elite enemies
		if (this.rank >= 3) {
			basicChance = 25;
			eliteChance = 10;
		}
		// This then extends at various ranks
		for (int walker = 4; walker <= this.rank; walker++) {
			// First, basic chance increases:
			// Ranks 4-9 except 5 grant +5% chance for basic enemies
			if (walker <= 9 && walker != 5) {
				basicChance += 5;
			}
			// Rank 10 grants +25% chance for basic enemies
			else if (walker == 10) {
				basicChance += 25;
			}
			
			// Second, elite and boss chance increases (only rank 10 differs):
			// Ranks 4-7 except 5 grant +5% chance for elite and boss enemies
			if (walker <= 7 && walker !=5) {
				eliteChance += 5;
				bossChance += 5;
			}
			// Rank 10 grants +25% chance for elite enemies and +10% for boss enemies
			else if(walker == 10) {
				eliteChance += 25;
				bossChance += 10;
			}
		}
		
		// Creates a percent die to determine if the taunt effect happens (will have duration of 1 rather than 0)
		Dice percent = new Dice(100);
		int result = percent.roll();
		int tauntDuration = 0;
		// If basic enemy (Level = 1 or 2)
		if (enemy.getLevel() == 1 || enemy.getLevel() == 2) {
			if (result <= basicChance) {
				tauntDuration = 1;
			}
		}
		// If Elite enemy (Level = 3)
		else if (enemy.getLevel() == 3) {
			if (result <= eliteChance) {
				tauntDuration = 1;
			}
		}
		// If Boss enemy (Level = 4)
		else if (enemy.getLevel() == 4) {
			if (result <= bossChance) {
				tauntDuration = 1;
			}
		}
		
		// Return the result
		return new Condition(this.taunt.getName(), tauntDuration);
	}
	
	public Condition getPreAttackBonus() {
		// Returns the condition, but sets it again first in case things changed because of "numMisses"
		this.setPreAttackBonus();
		return this.selfPreAttackBonus;
	}
}

// Ability 4: "Leader Strike"
class LeaderStrike extends Ability {
	// Additional variables for the Ability
	private Condition selfPreAttackBonus;
	private Condition allyDamageBonus;
	private double healingScaler;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Leader Strike misses (for bonus effects)
	public int numMisses;
	
	public LeaderStrike(Character source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 4: \"Leader Strike\"";
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Set the additional effects of the Ability
		this.setPreAttackBonus();
		this.setAllyDamageBonus();
		this.setHealingScaler();
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 5;
		if (this.rank >= 7) {
			this.cooldown = 4;
		}
		this.turnCount = this.cooldown;  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at .7
		this.scaler = .7;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Rank 2 grants +.05 to scaler
			if (walker == 2) {
				this.scaler += .05;
			}
			// Ranks 4,8 grant +.1 to scaler
			else if (walker == 4 || walker == 8) {
				this.scaler += .1;
			}
			// Rank 6,9 grant +.15 to scaler
			else if (walker == 6 || walker == 9) {
				this.scaler += .15;
			}
			// Rank 10 grants +.25 to scaler
			else if (walker == 10) {
				this.scaler += .25;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setPreAttackBonus() {
		int amount = 0;
		if (this.ssRank >= 15) {
			amount = 40;
		}
		StatusEffect accuracyBonus = new StatusEffect(StatVersion.ACCURACY, amount*this.numMisses, StatusEffectType.OUTGOING);
		
		// Creates the pre-attack condition with accuracy bonus as needed
		this.selfPreAttackBonus = new Condition("Taunting Attack: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
	}
	
	private void setAllyDamageBonus() {
		// At rank 1, this damage bonus starts at 20% and lasts 1 turn
		int amount = 20;
		int duration = 2;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2,8 grants +2% increased damage
			if (walker == 2 || walker == 8) {
				amount += 2;
			}
			// Ranks 4,6,9 grant +3% increased damage
			else if (walker == 4 || walker == 6 || walker == 9) {
				amount += 3;
			}
			// Rank 10 grants +7% increased damage
			else if (walker == 10) {
				this.scaler += 7;
			}
			
			// Rank 3 increases the duration to 3
			if (walker == 3) {
				duration++;
			}
		}
		StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.OUTGOING);
		
		// Creates the damage bonus condition with the damage bonus status effect
		this.allyDamageBonus = new Condition("Leader Strike: Damage Bonus", duration);
		this.allyDamageBonus.setSource(this.owner);
		this.allyDamageBonus.addStatusEffect(damageBonus);
		this.allyDamageBonus.makeSourceIncrementing();
		this.allyDamageBonus.makeEndOfTurn();
	}
	
	private void setHealingScaler() {
		// At rank 1, this healing scaler starts at .05
		this.healingScaler = .05;
		for (int walker = 2; walker <= this.rank; walker++) {
			// Ranks 2,8 grants +1% to scaler
			if (walker == 2 || walker == 8) {
				this.healingScaler += .01;
			}
			// Ranks 4,9 grant +1.5% to scaler
			else if (walker == 4 || walker == 9) {
				this.healingScaler += .015;
			}
			// Ranks 6,10 grant +2.5% to scaler
			else if (walker == 6 || walker == 10) {
				this.healingScaler += .025;
			}
		}
	}
	
	// Get methods for additional variables for this Ability
	public Condition getPreAttackBonus() {
		// Returns the condition, but sets it again first in case things changed because of "numMisses"
		this.setPreAttackBonus();
		return this.selfPreAttackBonus;
	}
	public Condition getAllyDamageBonus() {
		this.setAllyDamageBonus();
		return this.allyDamageBonus;
	}
	public double getHealingScaler() {
		this.setHealingScaler();
		return this.healingScaler;
	}
	
	// At certain ranks, the next attack of each ally has a chance to stun, this returns true if it does
	public boolean willStun() {
		// 0% chance until rank 5, which gives a 10% chance
		int chance = 0;
		if (this.rank >= 5) {
			chance += 10;
		}
		for (int walker = 6; walker < this.rank; walker++) {
			// Rank 6 increases stun chance by 3
			if (walker == 6) {
				chance += 3;
			}
			// Rank 7 increases stun chance by 2
			else if (walker == 7) {
				chance += 2;
			}
			// Ranks 8,9 increase stun chance by 5
			else if (walker == 8 || walker == 9) {
				chance += 5;
			}
			// Rank 10 increases stun chance by 15
			else if (walker == 10) {
				chance += 15;
			}
		}
		
		// Returns true if the chance succeeded, false otherwise.
		Dice percent = new Dice(100);
		if (percent.roll() <= chance) {
			return true;
		}
		return false;
	}
}

// ULTIMATE Ability: "Hahaha! You Can't Kill Me!"
class HaHaHaYouCantKillMe extends UltimateAbility {
	// Additional variables for the Ability
	private Condition selfArmorBonus;
	private Condition enemyTauntEffect;
	private Condition allyDamageBonus;
	
	
	// NOTE: This Ability does not yet implement the Character Requirement for decreased Damage.
	public HaHaHaYouCantKillMe(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 5 (ULTIMATE): \"HaHaHa You Can't Kill Me!\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the additional effects of the Ability
		this.setSelfArmorBonus();
		this.setEnemyTauntEffect();
		this.setAllyDamageBonus();
	}
	
	// Calculates the values for the additional Effects for this Ability
	public void setSelfArmorBonus() {
		// At rank 1, this increases Armor by 25% for 2 turns
		int amount = 25;
		int duration = 2;
		// Increased to 30% at rank 2
		if (this.rank == 2) {
			amount = 30;
		}
		// Increased to 50% at rank 3 for 3 turns
		else if (this.rank >= 3) {
			amount = 50;
			duration = 3;
		}
		StatusEffect armorBonus = new StatusEffect(StatVersion.ARMOR, amount, StatusEffectType.INCOMING);
		
		// Creates the condition with armor bonus as needed
		this.selfArmorBonus = new Condition("HaHaHa You Can't Kill Me!: Self Armor Bonus", duration);
		this.selfArmorBonus.setSource(this.owner);
		this.selfArmorBonus.addStatusEffect(armorBonus);
	}
	// Sets the Taunt Condition (with the reduced damage status effect as necessary)
	public void setEnemyTauntEffect(Character enemy) {
		int duration = 2;
		if (this.rank >= 3) {
			duration = 3;
		}
		this.enemyTauntEffect = new Condition("HaHaHa You Can't Kill Me!: Taunt Effect", duration);
		this.enemyTauntEffect.setSource(this.owner);
		
		// Adds reduced damage at rank 2 based on enemy affected
		if (this.rank >= 2) {
			// Default of 50% reduction for Normal and Advanced enemies (Level = 1, 2)
			int amount = 50;
			// 25% reduction for Elite enemies (Level = 3)
			if (enemy.getLevel() == 3) {
				amount = 25;
			}
			// 0% reduction for Boss enemies
			else if (enemy.getLevel() == 4) {
				amount = 0;
			}
			StatusEffect damageReduction = new StatusEffect(StatVersion.DAMAGE, -amount, StatusEffectType.OUTGOING);
			
			this.enemyTauntEffect.addStatusEffect(damageReduction);
		}
	}
	public void setEnemyTauntEffect() {
		this.setEnemyTauntEffect(Character.EMPTY);
	}
	
	public void setAllyDamageBonus() {
		// Only occurs at rank 3
		int amount = 0;
		int duration = 0;
		if (this.rank >= 3) {
			amount = 50;
			duration = 1;
		}
		StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.OUTGOING);
		
		// Creates the condition with the damage bonus as needed
		this.allyDamageBonus = new Condition("HaHaHa You Can't Kill Me: Damage Bonus", duration);
		this.allyDamageBonus.setSource(this.owner);
		this.allyDamageBonus.addStatusEffect(damageBonus);
	}
	
	// Get methods for the additional variables of this Ability
	public Condition getSelfArmorBonus() {
		this.setSelfArmorBonus();
		return this.selfArmorBonus;
	}
	public Condition getEnemyTauntEffect(Character enemy) {
		this.setEnemyTauntEffect(enemy);
		return this.enemyTauntEffect;
	}
	public Condition getAllyDamageBonus() {
		this.setAllyDamageBonus();
		return this.allyDamageBonus;
	}
}



// The Steel Legion Tank itself:
public class SteelLegionTank extends Character {
	// Passive Abilities
	private HoldItRightThere HoldItRightThere; // Unique Passive Ability (UPA)
	private EnchantedArmor EnchantedArmor;
	private ShieldSkills ShieldSkills;
	private ProfessionalLaughter ProfessionalLaughter = new ProfessionalLaughter(5);
	
	// Base Abilities
	private ShieldBash ShieldBash;
	private ShieldReflection ShieldReflection;
	private TauntingAttack TauntingAttack;
	private LeaderStrike LeaderStrike;
	private HaHaHaYouCantKillMe HaHaHaYouCantKillMe;
	
	// A List of all Abilities so all Cooldowns can be reduced at once
	private LinkedList<Ability> abilities;
	
	// These first two methods help set up the Steel Legion Tank subclass.
	public SteelLegionTank(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashMap<AttackType,Double> resis, HashMap<AttackType,Double> vuls, int upaRank, int eArmorRank, int sSkillsRank, int profLaughRank, int sBashRank, int sReflectRank, int tAttackRank, int lStrikeRank, int haRank) {
		// Calls the super constructor to create the Character, then initializes all Abilities according to their specifications.
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls);
		this.HoldItRightThere = new HoldItRightThere(this, upaRank);
		this.EnchantedArmor = new EnchantedArmor(this, eArmorRank);
		this.ShieldSkills = new ShieldSkills(this, sSkillsRank);
		this.ProfessionalLaughter = new ProfessionalLaughter(this, profLaughRank);
		this.ShieldBash = new ShieldBash(this, sBashRank, sSkillsRank);
		this.ShieldReflection = new ShieldReflection(this, sReflectRank, sSkillsRank);
		this.TauntingAttack = new TauntingAttack(this, tAttackRank, sSkillsRank);
		this.LeaderStrike = new LeaderStrike(this, lStrikeRank, sSkillsRank);
		this.HaHaHaYouCantKillMe = new HaHaHaYouCantKillMe(this, haRank);
		
		// Add Abilities to a list for Cooldown purposes
		this.abilities = new LinkedList<>();
		this.abilities.add(this.HoldItRightThere);
		this.abilities.add(this.EnchantedArmor);
		this.abilities.add(this.ShieldSkills);
		this.abilities.add(this.ProfessionalLaughter);
		this.abilities.add(this.ShieldBash);
		this.abilities.add(this.ShieldReflection);
		this.abilities.add(this.TauntingAttack);
		this.abilities.add(this.LeaderStrike);
		this.abilities.add(this.HaHaHaYouCantKillMe);
		
		// Add new commands for Abilities
		this.addCommand(1, "Shield Bash");
		this.addCommand(2, "Shield Reflection");
		this.addCommand(3, "Taunting Attack");
		this.addCommand(4, "Leader Strike");
		this.addCommand(5, "HaHaHaYouCantKillMe");
	}
	public SteelLegionTank(SteelLegionTank copy) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getResistances(), copy.getVulnerabilities(), copy.getHoldItRightThereRank(), copy.getEnchantedArmorRank(), copy.getShieldSkillsRank(), copy.getProfessionalLaughterRank(), copy.getShieldBashRank(), copy.getShieldReflectionRank(), copy.getTauntingAttackRank(), copy.getTauntingAttackRank(), copy.getHaHaHaYouCantKillMeRank());
	}
	
	// Get methods for ranks for Abilities (sometimes assists in Character creation or testing)
	public int getHoldItRightThereRank() {
		return this.HoldItRightThere.rank();
	}
	public int getEnchantedArmorRank() {
		return this.EnchantedArmor.rank();
	}
	public int getShieldSkillsRank() {
		return this.ShieldSkills.rank();
	}
	public int getProfessionalLaughterRank() {
		return this.ProfessionalLaughter.rank();
	}
	public int getShieldBashRank() {
		return this.ShieldBash.rank();
	}
	public int getShieldReflectionRank() {
		return this.ShieldReflection.rank();
	}
	public int getTauntingAttackRank() {
		return this.TauntingAttack.rank();
	}
	public int getLeaderStrikeRank() {
		return this.LeaderStrike.rank();
	}
	public int getHaHaHaYouCantKillMeRank() {
		return this.HaHaHaYouCantKillMe.rank();
	}
	
	// Overrides the prompt to give class conditions
	@Override
	public void promptClassConditionGive(Character other) {
		// Adds class Conditions to a list.
		LinkedList<Condition> classConditions = new LinkedList<>();
		classConditions.add(this.HoldItRightThere.getEnemyHaltCondition());
		classConditions.add(this.HoldItRightThere.getSelfBlockCondition());
		classConditions.add(this.ShieldBash.getEnemyAccuracyReduction());
		classConditions.add(this.ShieldBash.getStunEffect());
		classConditions.add(this.ShieldReflection.getBlindEffect());
		classConditions.add(this.TauntingAttack.getTauntEffectHit());
		classConditions.add(this.TauntingAttack.getTauntEffectMiss(other));
		classConditions.add(this.LeaderStrike.getAllyDamageBonus());
		classConditions.add(this.HaHaHaYouCantKillMe.getAllyDamageBonus());
		classConditions.add(this.HaHaHaYouCantKillMe.getEnemyTauntEffect(other));
		classConditions.add(this.HaHaHaYouCantKillMe.getSelfArmorBonus());
		
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
	public void beginTurn() {
		// Base Setup
		this.beginTurnSetup();
		
		// State if Character is dead
		if (this.getCurrentHealth() < 0) {
			System.out.println(this.getName() + " is dead. Have turn anyway? Y or N");
			if (!BattleSimulator.getInstance().askYorN()) {
				this.endTurn();
				return;
			}
		}
		
		// Setup for Class
		// Checks to see if it is the beginning of the round (Rank 15 of Professional Laughter gives Taunt)
		if (BattleSimulator.getInstance().getRound() == 1 && this.ProfessionalLaughter.rank() >= 15) {
			System.out.println("You have \"Taunt\" for 2 rounds.\n");
		}
		
		// Reduces the Cooldown of all Abilities that need it.
		for (Ability a : abilities) {
			if (a.onCooldown()) {
				a.incrementTurn();
			}
		}
		
		// Do action based on command given
		boolean flag = true;
		while (flag) {
			// Display available actions
			this.beginTurnDisplay();
			
			System.out.print("Choice? ");
			String responce = BattleSimulator.getInstance().getPrompter().nextLine();
			Character target;
			switch(responce)
	        {
	            case "1": // Basic Attack
	                target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                this.attack(target, AttackType.SLASHING);
	                flag = false;
	                break;
	            case "2": // Shield Bash
	            	target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                this.useShieldBash(target);
	                flag = false;
	                break;
	            case "3": // Shield Reflection
	            	System.out.println("Choose enemies hit by attack:");
	            	LinkedList<Character> attackTargets = BattleSimulator.getInstance().targetMultiple();
	                if (attackTargets.isEmpty()) {
	                	break;
	                }
	                System.out.println("Choose enemies blinded (0 no longer takes back, will remain empty list):");
	                LinkedList<Character> blindedTargets = BattleSimulator.getInstance().targetMultiple();
	                this.useShieldReflection(attackTargets, blindedTargets);
	                flag = false;
	                break;
	            case "4": // Taunting Attack
	            	target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                this.useTauntingAttack(target);
	                flag = false;
	                break;
	            case "5": // Leader Strike
	            	target = BattleSimulator.getInstance().targetSingle();
	                if (target.equals(Character.EMPTY)) {
	                	break;
	                }
	                this.useLeaderStrike(target);
	                flag = false;
	                break;
	            case "6": // HaHaHaYouCantKillMe
	            	this.useHahahaYouCantKillMe();
	                flag = false;
	                break;
	            case "7": // Alter Character
	            	Character chosen = BattleSimulator.getInstance().targetSingle();
	            	if (chosen.equals(Character.EMPTY)) {
	            		break;
	            	}
	            	chosen.promptAlterCharacter();
	            	break;
	            case "8": // End Turn
	                flag = false;
	                break;
	            default:
	                System.out.println("Please enter a number that corresponds to one of your choices.\n");
	        }
		}
		
		this.endTurn();
	}
	// End of Turn Override
	@Override
	public void endTurn() {
		// Setup
		this.endTurnSetup();
		
		// Use Enchanted Armor Healing (end of turn effect)
		this.useEnchantedArmorHealing();
		
		// State facts
		System.out.println(this.getName() + "'s turn is over.");
		
		// Return
		System.out.println("Enter something the press enter to continue.");
		BattleSimulator.getInstance().getPrompter().nextLine();
	}
	
	// Overrides "avoidAttack" in order to also store the fact that an attack was blocked in "Shield Bash" and "Shield Reflection"
	@Override
	protected void avoidAttack(Attack atk) {
		super.avoidAttack(atk);
		this.ShieldBash.didBlock = true;
		this.ShieldReflection.didBlock = true;
	}
	// Overrides "receivedAttack" for death effect 
	@Override
	protected void receivedAttack(Attack atk) {
		super.receivedAttack(atk);
		if (this.isDead() && this.HaHaHaYouCantKillMe.rank() >= 3) {
			this.useDeathHaHaHaYouCantKillMe(BattleSimulator.getInstance().getAllies());
		}
	}
	
	// Methods to use "Hold It Right There" Unique Passive
	public void addHoldItRightThereBlockBonus() {
		// Do not add Condition if already present
		for (Condition c : this.getAllConditions()) {
			if (c.getName() == this.HoldItRightThere.getSelfBlockCondition().getName()) {
				return;
			}
		}
		this.addCondition(this.HoldItRightThere.getSelfBlockCondition());
	}
	public void useHoldItRightThereHaltCondition(Character enemy) {
		// If condition is present, remove it first
		for (Condition c : enemy.getAllConditions()) {
			if (c.getName() == this.HoldItRightThere.getEnemyHaltCondition().getName()) {
				enemy.removeCondition(c);
			}
		}
		enemy.addCondition(this.HoldItRightThere.getEnemyHaltCondition());
	}
	
	// Deals with the healing from the "Enchanted Armor" Passive
	public void useEnchantedArmorHealing() {
		// Calculates the amount of healing based on Maximum Health
		int healing = (int) Math.round(this.EnchantedArmor.getScaler() * this.getHealth());
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
	}
	
	// Deals with the healing (and rank 15 Cooldown) portion of the "Shield Skills" Passive from hitting multiple enemies with "Shield Reflection"
	private void useShieldSkillsHealing(int numBlinded) {
		// First, non-healing-wise, if 7 enemies are hit at rank 15, the Cooldown of Shield Bash is refreshed.
		if (this.ShieldSkills.rank() >= 15 && numBlinded >= 7) {
			this.ShieldBash.turnCount = ShieldBash.cooldown();
		}
		
		// Calculates the amount of healing based on missing Health and the number of enemies blinded
		int healing = 0;
		if ((this.ShieldSkills.rank() < 11 && numBlinded >= 3) || (this.ShieldSkills.rank() >= 11 && numBlinded >= 3 && numBlinded < 5)) {
			healing = (int) Math.round(this.ShieldSkills.getScalerBlind3() * (this.getHealth() - this.getCurrentHealth()));
		}
		else if (this.ShieldSkills.rank() >= 11 && numBlinded >= 5){
			healing = (int) Math.round((this.ShieldSkills.getScalerBlind3() + this.ShieldSkills.getScalerBlind5()) * (this.getHealth() - this.getCurrentHealth()));
		}
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		if (healing == 0) {
			return;
		}
		System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
	}
	
	// Deals the Damage from the "Shield Bash" Ability (Ability 1)
	public void useShieldBash(Character enemy) {
		// Before anything, put Shield Bash "on Cooldown"
		this.ShieldBash.resetCounter();
		
		// Apply bonus pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.ShieldBash.getSelfPreAttackBonus();
		this.apply(preCondition);
		
		// Make the attack
		this.attack(enemy, this.ShieldBash.getScaler(), AttackType.SMASHING);
		
		// Unapply the bonus pre-conditions
		this.unapply(preCondition);
		
		// Change the didCrit of "Shield Bash" to match if the Ability critically struck (this may affect the effects below when received)
		this.ShieldBash.didCrit = this.previousAttack().didCrit();
		
		// If the attack hit, apply all the conditions (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.previousAttack().didHit()) {
			// Add all conditions to enemy hit
			Stun stunEffect = this.ShieldBash.getStunEffect();
			enemy.addCondition(stunEffect);
			Condition accuracyReduction = this.ShieldBash.getEnemyAccuracyReduction();
			enemy.addCondition(accuracyReduction);
			
			// Change numMisses back to 0
			this.ShieldBash.numMisses = 0;
		}
		// If the attack missed, check to see if its Cooldown is reduced by "Shield Skills" and increment numMisses
		else {
			// Each point in "Shield Skills" causes a reduction of 1 up to a maximum of the Cooldown itself
			int cdr = this.ShieldSkills.rank();
			if (cdr > this.ShieldBash.cooldown()) {
				cdr = this.ShieldBash.cooldown();
			}
			this.ShieldBash.turnCount = cdr;
			
			// Increment numMisses
			this.ShieldBash.numMisses++;
		}
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") and didCrit to false (using this ability consumes the buff if present)
		this.ShieldBash.didBlock = false;
		this.ShieldReflection.didBlock = false;
		this.ShieldBash.didCrit = false;
	}
	
	// Deals the Damage from the "Shield Reflection" Ability (Ability 2) to multiple enemies
	public void useShieldReflection(LinkedList<Character> enemies, LinkedList<Character> blinded) {
		// Before anything, put Shield Reflection "on Cooldown"
		this.ShieldReflection.resetCounter();
		
		// Make the attack against all enemies affected
		for (Character enemy : enemies) {
			this.attackAOE(enemy, this.ShieldReflection.getScaler(), AttackType.LIGHT); // AOE attack
		}
		
		// Blind all enemies affected
		for (Character enemy : blinded) {
			enemy.addCondition(this.ShieldReflection.getBlindEffect());
		}
		
		// Add the possible healing string based on bonus effects from the "Shield Skills" Ability (will be empty String if nothing happens)
		this.useShieldSkillsHealing(blinded.size());
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") to false (using this ability consumes the buff if present)
		this.ShieldBash.didBlock = false;
		this.ShieldReflection.didBlock = false;
	}
	public void useShieldReflection(LinkedList<Character> enemies) {
		this.useShieldReflection(enemies, enemies);
	}
	
	// Deals the Damage from the "Taunting Attack" Ability (Ability 3)
	public void useTauntingAttack(Character enemy) {
		// Before anything, put Tauning Attack "on Cooldown"
		this.TauntingAttack.resetCounter();
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.TauntingAttack.getPreAttackBonus();
		this.apply(preCondition);
		
		// Make the attack
		this.attack(enemy, this.TauntingAttack.getScaler(), AttackType.SLASHING);
		
		// Unapply the bonus accuracy pre-condition
		this.unapply(preCondition);
		
		// If the attack hit, apply the taunt condition (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.previousAttack().didHit()) {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.TauntingAttack.getTauntEffectHit();
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				System.out.println(enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!");
			}
			
			// Change numMisses back to 0
			this.TauntingAttack.numMisses = 0;
		}
		// If the attack missed, still apply the taunt condition if it is effective (will be 0 if not) and increment numMisses
		else {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.TauntingAttack.getTauntEffectMiss(enemy);
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				System.out.println(enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!");
			}
			
			// Increment numMisses if necessary (shield skills rank 15)
			if (this.ShieldSkills.rank() >= 15) {
				this.TauntingAttack.numMisses++;
			}
		}
	}
	
	// Deals the Damage from the "Leader Strike" Ability (Ability 4) and Calculates the amount healed for allies.
	public void useLeaderStrike(Character enemy) {
		// Before anything, put Tauning Attack "on Cooldown"
		this.LeaderStrike.resetCounter();
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.LeaderStrike.getPreAttackBonus();
		this.apply(preCondition);
		
		// Make the attack
		this.attack(enemy, this.LeaderStrike.getScaler(), AttackType.SLASHING);
		
		// Unapply the bonus accuracy pre-condition
		this.unapply(preCondition);
		
		// If the attack hit revert numMisses to 0, if it missed, increment numMisses
		if (this.previousAttack().didHit()) {
			this.LeaderStrike.numMisses = 0;
		}
		else {
			// Increment numMisses if necessary (shield skills rank 15)
			if (this.ShieldSkills.rank() >= 15) {
				this.TauntingAttack.numMisses++;
			}
		}
		
		
		// Past rank 3, this Character is included for the buffs in "allies", either way, create a copy of the list so the original is unchanged (extra safety net)
		LinkedList<Character> alliesCopy = new LinkedList<>();
		for (Character ally : BattleSimulator.getInstance().getAllies()) {
			alliesCopy.add(ally);
		}
		if (!alliesCopy.contains(this) && this.LeaderStrike.rank() >= 3) {  // Adds this if not present and should be
			alliesCopy.add(this);
		}
		if (alliesCopy.contains(this) && this.LeaderStrike.rank() < 3) {  // Removes this if present and should not be
			alliesCopy.remove(this);
		}
		
		// Calculates and adds the amount of Healing received for each ally affected by the Ability (in the list) and apply the damage boost
		for (Character ally : alliesCopy) {
			// Calculates the healing amount for the ally in the list
			int healing = (int)Math.round(ally.getHealth() * this.LeaderStrike.getHealingScaler());
			
			healing = ally.restoreHealth(healing);
			System.out.println(ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth());
			
			// Applies the damage boost to each ally affected
			ally.addCondition(this.LeaderStrike.getAllyDamageBonus());
			
			// Checks to see if each ally's attack will stun the next target, and adds it to the return if so.
			if (this.LeaderStrike.willStun()) {
				System.out.println(ally.getName() + " will also stun the next target hit");
			}
		}
	}
	
	
	// Restores the Character to full health and grants bonuses from the ULTIMATE Ability "HaHaHa You Can't Kill Me!"
	public void useHahahaYouCantKillMe() {
		// Before anything, put HahahaYouCantKillMe "on Cooldown"
		this.HaHaHaYouCantKillMe.resetCounter();
		
		// Heal to full Health
		int healing = this.getHealth() - this.getCurrentHealth();
		healing = this.restoreHealth(healing);
		System.out.println(this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth());
		
		// Apply Additional Conditions
		this.addCondition(this.HaHaHaYouCantKillMe.getSelfArmorBonus());
		for (Character enemy : BattleSimulator.getInstance().getEnemies()) {
			enemy.addCondition(this.HaHaHaYouCantKillMe.getEnemyTauntEffect(enemy));
		}
	}
	
	// Gives all allies the appropriate buffs if you die while "HaHaHa You Can't Kill Me!" is active
	public void useDeathHaHaHaYouCantKillMe(List<Character> allies) {
		// All this only happens at rank 3
		if (this.HaHaHaYouCantKillMe.rank() >= 3) {
			// Restore each ally for 25% of their max Health and give each ally the damage buff from the Ability
			for (Character ally : allies) {
				if (!ally.equals(this)) {
					// Calculates the healing amount for the ally in the list
					int healing = (int)Math.round(ally.getHealth() * .25);
					
					healing = ally.restoreHealth(healing);
					System.out.println(ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth());
					
					// Gives each ally the Damage buff and Invincibility for 1 turn
					ally.addCondition(this.HaHaHaYouCantKillMe.getAllyDamageBonus());
					ally.addCondition(new Invincible("HaHaHa You Can't Kill Me: Invincibility", 1));
				}
			}
		}
	}
}
