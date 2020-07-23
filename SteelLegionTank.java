package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

// Passive Abilities:
// Unique Passive Ability: "Hold It Right There!"
class HoldItRightThere extends Ability {
	// Holds the owner of the Ability as a Steel Legion Tank
	private final SteelLegionTank owner;
	
	// Additional Effects of Ability
	private Condition selfBlock;
	private Condition enemyHalt;
	
	// Constructor
	public HoldItRightThere(SteelLegionTank source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \"Hold It Right There!\"", source, rank);
		this.owner = source;
		
		// Calculate the additional Staus Effects
		this.setSelfBlock();
		this.setEnemyHalt();
	}
	
	
	// Additional Status Effects for this Ability:
	
	// Increased Block when blocking for an ally
	private void setSelfBlock() {
		// Find block amount based on rank
		int amount = 0;
		for (int i = 1; i <= this.rank(); i++) {
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
		
		StatusEffect blockBonus = new StatusEffect(Stat.Version.BLOCK, amount, StatusEffect.Type.INCOMING);
		DualRequirement req = (Character withEffect, Character other) -> {
			// Prompt controller if blocking for an ally
			System.out.println("Is " + this.owner.getName() + " blocking for an ally?");
			return BattleSimulator.getInstance().askYorN();
		};
		blockBonus.setDualRequirement(req);
		
		this.selfBlock.addStatusEffect(blockBonus);
		
		// Since it is permanent, this Condition will start attached to the Character if necessary (Rank 2 or above)
		if (this.rank() >= 2) {
			this.owner.addCondition(this.selfBlock);
		}
	}
	
	// Provides the Halt condition with increased damage to halted enemies
	private void setEnemyHalt() {
		// Find damage bonus amount based on rank
		int amount = 0;
		if (this.rank() == 4) {
			amount = 20;
		}
		if (this.rank() == 5) {
			amount = 30;
		}
		
		// Create the Halt Damage Condition with the correct Status Effect(s) based on the numbers calculated
		this.enemyHalt = new Condition("Hold It Right There!: Halted - Enemy Damage Bonus", 2);
		this.enemyHalt.setSource(this.owner);
		
		StatusEffect damageBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.INCOMING);
		damageBonus.makeAffectOther();
		
		
		// At rank 5, bonus damage from the owner only is increased to an amount of 50
		if (this.rank() == 5) {
			// Creates additional statusEffect for owner only
			StatusEffect ownerDamageBonus = new StatusEffect(Stat.Version.DAMAGE, 50, StatusEffect.Type.INCOMING);
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
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
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
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional variables for the Ability
	private int bonusArmor;
	private int bonusBlock;
	
	// Constructors
	public EnchantedArmor(SteelLegionTank source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Enchanted Armor\"", source, rank);
		this.owner = source;
		
		// Set the healing scaler and base stat amounts
		this.setScaler();
		this.setArmorBonus();
		this.setBlockBonus();
	}
	// Makes a constructer that is only used for calculating the bonus Armor and Block from this Ability
	public EnchantedArmor(int rank) {
		super("No owner: Enchanted Armor", Character.EMPTY, rank);
		
		this.setArmorBonus();
		this.setBlockBonus();
	}
	
	// Calculates the base scaler (used as a healing scaler) for this Ability
	private void setScaler() {
		// At rank 0, this scaler starts at .02
		this.scaler = .02;
		for (int walker = 1; walker <= this.rank(); walker++) {
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
		for (int walker = 1; walker <= this.rank(); walker++) {
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
		for (int walker = 1; walker <= this.rank(); walker++) {
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
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
	public int getBonusArmor() {
		this.setArmorBonus();
		return this.bonusArmor;
	}
	public int getBonusBlock() {
		this.setBlockBonus();
		return this.bonusBlock;
	}
	
	// Creates a use function to be used at the end of the owner's turn. Heals the owner for the specified amount
	@Override
	public void use() {
		// Calculates the amount of healing based on Maximum Health
		int healing = (int) Math.round(this.getScaler() * this.owner.getHealth());
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.owner.restoreHealth(healing);
		System.out.println(this.owner.getName() + " healed for " + healing + " Health for a new total of " + this.owner.getCurrentHealth());
	}
}

// Base Passive Ability 2: "Shield Skills"
class ShieldSkills extends Ability {
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional variables for ability
	private double scalerBlind3;
	private double scalerBlind5;
	
	public ShieldSkills(SteelLegionTank source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Shield Skills\"", source, rank);
		this.owner = source;
		
		// At rank 1, the healing scaler for blinding 3 enemies starts at .02, and there is no extra benefit for blinding 5
		this.scalerBlind3 = .02;
		this.scalerBlind5 = 0;
		
		// Adjust for current rank
		for (int walker = 2; walker <= this.rank(); walker++) {
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
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
	public double getScalerBlind3() {
		return this.scalerBlind3;
	}
	public double getScalerBlind5() {
		return this.scalerBlind5;
	}
	
	// Create a use function that is called when Shield Reflection hits multiple enemies. The version number is how many enemies are blinded
	@Override
	public void use(int numBlinded) {
		// First, non-healing-wise, if 7 enemies are hit at rank 15, the Cooldown of Shield Bash is refreshed.
		if (this.rank() >= 15 && numBlinded >= 7) {
			this.owner.setAbilityCD(SteelLegionTank.AbilityNames.ShieldBash, 0);
		}
		
		// Calculates the amount of healing based on missing Health and the number of enemies blinded
		int healing = 0;
		if ((this.rank() < 11 && numBlinded >= 3) || (this.rank() >= 11 && numBlinded >= 3 && numBlinded < 5)) {
			healing = (int) Math.round(this.getScalerBlind3() * (this.owner.getHealth() - this.owner.getCurrentHealth()));
		}
		else if (this.rank() >= 11 && numBlinded >= 5){
			healing = (int) Math.round((this.getScalerBlind3() + this.getScalerBlind5()) * (this.owner.getHealth() - this.owner.getCurrentHealth()));
		}
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.owner.restoreHealth(healing);
		if (healing == 0) {
			return;
		}
		System.out.println(this.owner.getName() + " healed for " + healing + " Health for a new total of " + this.owner.getCurrentHealth());
	}
}

// Base Passive Ability 3: "Professional Laughter"
class ProfessionalLaughter extends Ability {
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional variables for the Ability
	private int bonusThreat;
	private int bonusTacticalThreat;
	private int bonusHealth;
	
	public ProfessionalLaughter(SteelLegionTank source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \\\"Professional Laughter\\\"\"", source, rank);
		this.owner = source;
		
		// Set the base stat amounts
		this.setThreatBonus();
		this.setTacticalThreatBonus();
		this.setHealthBonus();
	}
	// An additional constuctor just for use when calculating the bonus stats needed to make a Steel Legion Tank
	public ProfessionalLaughter(int rank) {
		super("No Owner: Professional Laughter", Character.EMPTY, rank);
		
		// Set stats so they can be looked at.
		this.setThreatBonus();
		this.setTacticalThreatBonus();
		this.setHealthBonus();
	}
	
	// Calculates the values for the additional stats of this Ability
	private void setThreatBonus() {
		// Starting at 0
		this.bonusThreat = 0;
		for (int walker = 1; walker <= this.rank(); walker++) {
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
		for (int walker = 11; walker <= this.rank() ; walker++) {
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
		for (int walker = 1; walker <= this.rank(); walker++) {
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
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
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
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional Conditions for the Ability
	private Stun stun;
	private Condition enemyAccReduction;
	private Condition selfPreAttackBonus;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Shield Bash misses, if a previous attack has been blocked, and if this Ability critically hit (for bonus effects)
	public int numMisses;
	public boolean didCrit;
	
	
	public ShieldBash(SteelLegionTank source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super("Shield Bash (Ability 1)", source, rank);
		this.owner = source;
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		this.didCrit = false;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Sets the additional effects of the Ability
		this.setStun();
		this.setEnemyAccReduction();
		this.setPreAttackBonus();
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		this.setOffCooldown();  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// Checks based on this Ability's rank
		// At rank 1, this scaler starts at .7
		this.scaler = .7;
		for (int walker = 2; walker <= this.rank(); walker++) {
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
			if (walker <= 10 && this.owner.didBlock) {
				this.scaler += .2;
			}
			// Rank 15 grants +.5x damage to scaler if an attack was blocked
			else if (walker == 15 && this.owner.didBlock) {
				this.scaler += .5;
			}
		}
	}
	
	// Calculates the values for the additional Effects for this Ability
	private void setStun() {
		int stunDuration = 1;
		
		// Checks based on this Ability's rank
		// Rank 10 increases the duration to 2, then is increased by an additional turn if the Ability didCrit
		if (this.rank() >= 10) {
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
		if (this.rank() >= 5) {
			int amount = 0;
			for (int walker = 1; walker <= this.rank(); walker++) {
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
			if (this.rank() >= 10 && this.didCrit) {
				amount *= 2;
			}
			
			// Sets the Calculated Status Effect and adds it to the stun
			StatusEffect damageBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.INCOMING);
			damageBonus.makeAffectOther();
			this.stun.addStatusEffect(damageBonus);
		}
	}
	
	private void setEnemyAccReduction() {
		int amount = 0;
		// Checks based on this Ability's rank
		for (int walker = 1; walker <= this.rank(); walker++) {
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
			if (walker <= 15 && this.owner.didBlock) {
				amount += 2;
			}
			// Rank 15 grants an additional 5% accuracy deduction on top of that (total of a bonus 7% from rank 15) if an attack was blocked
			if (walker == 15 && this.owner.didBlock) {
				amount += 5;
			}
		}
		
		// the value "amount" doubles when you critically strike at rank 10
		if (this.rank() >= 10 && this.didCrit) {
			amount *= 2;
		}
		
		// Creates the requirement of the Condition to become Active (occurs after stun)
		Requirement actReq = (Character withEffect) -> {
			return !withEffect.getAllConditions().contains(this.stun);
		};
		
		// Finds the duration: 2 when >= 8
		int duration = this.rank() < 8? 1 : 2;
		
		// Create the Enemy Accuracy Reduction Condition with the correct Status Effect based on the numbers calculated
		this.enemyAccReduction = new Condition("Shield Bash: Accuracy Reduction", duration, actReq);
		this.enemyAccReduction.setSource(this.owner);
		// Sets the Calculated Status Effect
		StatusEffect accReduction = new StatusEffect(Stat.Version.ACCURACY, -amount, StatusEffect.Type.OUTGOING);
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
		StatusEffect accuracyBonus = new StatusEffect(Stat.Version.ACCURACY, accAmount*this.numMisses, StatusEffect.Type.OUTGOING);
		
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
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		
		// Creates the pre-attack condition with accuracy and crit bonuses as needed
		this.selfPreAttackBonus = new Condition("Shield Bash: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
		this.selfPreAttackBonus.addStatusEffect(critBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
	
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
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Before anything, put Shield Bash "on Cooldown"
		this.setOnCooldown();
		
		// Target a chosen enemy
		Character enemy = BattleSimulator.getInstance().targetSingle();
        if (enemy.equals(Character.EMPTY)) {
        	return;
        }
		
		// Apply bonus pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.getSelfPreAttackBonus();
		this.owner.apply(preCondition);
		
		// Make the attack
		Attack bashAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SMASHING)
				.build();
		bashAtk.execute();
		
		// Unapply the bonus pre-conditions
		this.owner.unapply(preCondition);
		
		// Change the didCrit of "Shield Bash" to match if the Ability critically struck (this may affect the effects below when received)
		this.didCrit = this.owner.previousAttack().didCrit();
		
		// If the attack hit, apply all the conditions (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.owner.previousAttack().didHit()) {
			// Add all conditions to enemy hit
			Stun stunEffect = this.getStunEffect();
			enemy.addCondition(stunEffect);
			Condition accuracyReduction = this.getEnemyAccuracyReduction();
			enemy.addCondition(accuracyReduction);
			
			// Change numMisses back to 0
			this.numMisses = 0;
		}
		// If the attack missed, check to see if its Cooldown is reduced by "Shield Skills" and increment numMisses
		else {
			// Each point in "Shield Skills" causes a reduction of 1 up to a maximum of the Cooldown itself
			int cdr = this.owner.getAbilityRank(SteelLegionTank.AbilityNames.ShieldSkills);
			if (cdr > this.cooldown()) {
				cdr = this.cooldown();
			}
			this.setTurnsRemaining(this.cooldown() - cdr);
			
			// Increment numMisses
			this.numMisses++;
		}
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") and didCrit to false (using this ability consumes the buff if present)
		this.owner.didBlock = false;
		this.didCrit = false;
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
}

// Ability 2: "Shield Reflection"
class ShieldReflection extends Ability {
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional Condition for the Ability
	private Blind blind;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of if a previous attack has been blocked
	
	public ShieldReflection(SteelLegionTank source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super("Shield Reflection (Ability 2)", source, rank);
		this.owner = source;
		this.ssRank = ShieldSkillsRank;
		
		// Sets the Cooldown and Scaler of the Ability
		this.setCooldown();
		this.setScaler();
		
		// Sets additional effects of the Ability
		this.setBlind();
	}
	
	// Calculates the basic values for this Ability
	private void setCooldown() {
		this.cooldown = 5;
		if (this.rank() >= 5) {
			this.cooldown = 4;
		}
		this.setOffCooldown();  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at .1
		this.scaler = .1;
		for (int walker = 2; walker <= this.rank(); walker++) {
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
			if (walker <= 10 && this.owner.didBlock) {
				this.scaler += .2;
			}
			// Rank 15 grants +.5x damage to scaler if an attack was blocked
			else if (walker == 15 && this.owner.didBlock) {
				this.scaler += .5;
			}
		}
	}
	
	// Calculates the value for the additional blind effect for this Ability
	public void setBlind() {
		// Calculates blind duration
		int blindDuration = 1;
		if (this.rank() >= 3) {
			blindDuration = 2;
		}
		
		// Sets the blind effect
		this.blind = new Blind("Shield Reflection: Blind", blindDuration);
		this.blind.setSource(this.owner);
	}
	
	// Get methods for additional effects as necessary
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
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
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Select enemies to attack and blind
		System.out.println("Choose enemies hit by attack:");
    	LinkedList<Character> enemies = BattleSimulator.getInstance().targetMultiple();
        if (enemies.isEmpty()) {
        	return;
        }
        if (enemies.contains(Character.EMPTY)) {
        	enemies.clear();
        }
        System.out.println("Choose enemies blinded:");
        LinkedList<Character> blinded = BattleSimulator.getInstance().targetMultiple();
        if (blinded.isEmpty()) {
        	return;
        }
        if (blinded.contains(Character.EMPTY)) {
        	blinded.clear();
        }
		
		// Before anything, put Shield Reflection "on Cooldown"
		this.setOnCooldown();
		
		// Make the attack against all enemies affected
		for (Character enemy : enemies) {
			Attack reflectAtk = new AttackBuilder()
					.attacker(this.owner)
					.defender(enemy)
					.isAOE()
					.scaler(this.getScaler())
					.type(Attack.DmgType.LIGHT)
					.build();
			reflectAtk.execute();
		}
		
		// Blind all enemies affected
		for (Character enemy : blinded) {
			enemy.addCondition(this.getBlindEffect());
		}
		
		// Add the possible healing string based on bonus effects from the "Shield Skills" Ability (will be empty String if nothing happens)
		this.owner.useAbility(SteelLegionTank.AbilityNames.ShieldSkills, blinded.size());
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") to false (using this ability consumes the buff if present)
		this.owner.didBlock = false;
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
}

// Ability 3: "Taunting Attack"
class TauntingAttack extends Ability {
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional Conditions for the Ability
	private Condition selfPreAttackBonus;
	private Condition taunt;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Taunting Attack misses (for bonus effects)
	public int numMisses;
	
	public TauntingAttack(SteelLegionTank source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super("Taunting Attack (Ability 3)", source, rank);
		this.owner = source;
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		
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
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		this.setOffCooldown();  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at 1.0
		this.scaler = 1.0;
		for (int walker = 2; walker <= this.rank(); walker++) {
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
		StatusEffect accuracyBonus = new StatusEffect(Stat.Version.ACCURACY, amount*this.numMisses, StatusEffect.Type.OUTGOING);
		
		// Creates the pre-attack condition with accuracy bonus as needed
		this.selfPreAttackBonus = new Condition("Taunting Attack: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
	}
	
	private void setTauntEffect() {
		int duration = 1;
		if (this.rank() >= 10) {
			duration = 2;
		}
		this.taunt = new Condition("Taunting Attack: Taunted", duration);
	}
	
	// Get methods for additional effects of this Ability
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
	
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
		if (this.rank() >= 3) {
			basicChance = 25;
			eliteChance = 10;
		}
		// This then extends at various ranks
		for (int walker = 4; walker <= this.rank(); walker++) {
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
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Select the target enemy
		Character enemy = BattleSimulator.getInstance().targetSingle();
        if (enemy.equals(Character.EMPTY)) {
        	return;
        }
		
		// Before anything, put Tauning Attack "on Cooldown"
		this.setOnCooldown();
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.getPreAttackBonus();
		this.owner.apply(preCondition);
		
		// Make the attack
		Attack tauntAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SLASHING)
				.build();
		tauntAtk.execute();
		
		// Unapply the bonus accuracy pre-condition
		this.owner.unapply(preCondition);
		
		// If the attack hit, apply the taunt condition (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.owner.previousAttack().didHit()) {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.getTauntEffectHit();
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				System.out.println(enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!");
			}
			
			// Change numMisses back to 0
			this.numMisses = 0;
		}
		// If the attack missed, still apply the taunt condition if it is effective (will be 0 if not) and increment numMisses
		else {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.getTauntEffectMiss(enemy);
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				System.out.println(enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!");
			}
			
			// Increment numMisses if necessary (shield skills rank 15)
			if (this.owner.getAbilityRank(SteelLegionTank.AbilityNames.ShieldSkills) >= 15) {
				this.numMisses++;
			}
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
}

// Ability 4: "Leader Strike"
class LeaderStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional variables for the Ability
	private Condition selfPreAttackBonus;
	private Condition allyDamageBonus;
	private double healingScaler;
	
	// Holds the rank of the Shield Skills Ability (since it affects some of the Status Effects)
	private int ssRank;
	
	// Keeps track of the number of previous Leader Strike misses (for bonus effects)
	public int numMisses;
	
	public LeaderStrike(SteelLegionTank source, int rank, int ShieldSkillsRank) {
		// Initialize all Ability variables to defaults
		super("Leader Strike (Ability 4)", source, rank);
		this.owner = source;
		this.ssRank = ShieldSkillsRank;
		this.numMisses = 0;
		
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
		if (this.rank() >= 7) {
			this.cooldown = 4;
		}
		this.setOffCooldown();  // The Ability always starts off Cooldown
	}
	
	private void setScaler() {
		// At rank 1, this scaler starts at .7
		this.scaler = .7;
		for (int walker = 2; walker <= this.rank(); walker++) {
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
		StatusEffect accuracyBonus = new StatusEffect(Stat.Version.ACCURACY, amount*this.numMisses, StatusEffect.Type.OUTGOING);
		
		// Creates the pre-attack condition with accuracy bonus as needed
		this.selfPreAttackBonus = new Condition("Taunting Attack: Pre Attack Bonus", 0);
		this.selfPreAttackBonus.setSource(this.owner);
		this.selfPreAttackBonus.addStatusEffect(accuracyBonus);
	}
	
	private void setAllyDamageBonus() {
		// At rank 1, this damage bonus starts at 20% and lasts 1 turn
		int amount = 20;
		int duration = 2;
		for (int walker = 2; walker <= this.rank(); walker++) {
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
		StatusEffect damageBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		
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
		for (int walker = 2; walker <= this.rank(); walker++) {
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
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
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
		if (this.rank() >= 5) {
			chance += 10;
		}
		for (int walker = 6; walker < this.rank(); walker++) {
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
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Select the enemy to use the ability on
		Character enemy = BattleSimulator.getInstance().targetSingle();
        if (enemy.equals(Character.EMPTY)) {
        	return;
        }
		
		// Before anything, put Leader Strike "on Cooldown"
		this.setOnCooldown();
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		Condition preCondition = this.getPreAttackBonus();
		this.owner.apply(preCondition);
		
		// Make the attack
		Attack leadAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SLASHING)
				.build();
		leadAtk.execute();
		
		// Unapply the bonus accuracy pre-condition
		this.owner.unapply(preCondition);
		
		// If the attack hit revert numMisses to 0, if it missed, increment numMisses
		if (this.owner.previousAttack().didHit()) {
			this.numMisses = 0;
		}
		else {
			// Increment numMisses if necessary (shield skills rank 15)
			if (this.owner.getAbilityRank(SteelLegionTank.AbilityNames.ShieldSkills) >= 15) {
				this.numMisses++;
			}
		}
		
		
		// Past rank 3, this Character is included for the buffs in "allies", either way, create a copy of the list so the original is unchanged (extra safety net)
		LinkedList<Character> alliesCopy = new LinkedList<>();
		for (Character ally : BattleSimulator.getInstance().getAllies()) {
			alliesCopy.add(ally);
		}
		if (!alliesCopy.contains(this.owner) && this.rank() >= 3) {  // Adds this if not present and should be
			alliesCopy.add(this.owner);
		}
		if (alliesCopy.contains(this.owner) && this.rank() < 3) {  // Removes this if present and should not be
			alliesCopy.remove(this.owner);
		}
		
		// Calculates and adds the amount of Healing received for each ally affected by the Ability (in the list) and apply the damage boost
		for (Character ally : alliesCopy) {
			// Calculates the healing amount for the ally in the list
			int healing = (int)Math.round(ally.getHealth() * this.getHealingScaler());
			
			healing = ally.restoreHealth(healing);
			System.out.println(ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth());
			
			// Applies the damage boost to each ally affected
			ally.addCondition(this.getAllyDamageBonus());
			
			// Checks to see if each ally's attack will stun the next target, and adds it to the return if so.
			if (this.willStun()) {
				System.out.println(ally.getName() + " will also stun the next target hit");
			}
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
}

// ULTIMATE Ability: "Hahaha! You Can't Kill Me!"
class HaHaHaYouCantKillMe extends UltimateAbility {
	// Holds the owner of the Ability as a Steel Legion Tank
	private SteelLegionTank owner;
	
	// Additional variables for the Ability
	private Condition selfArmorBonus;
	private Condition enemyTauntEffect;
	private Condition allyDamageBonus;
	
	
	// NOTE: This Ability does not yet implement the Character Requirement for decreased Damage.
	public HaHaHaYouCantKillMe(SteelLegionTank source, int rank) {
		// Initialize all Ability variables to defaults
		super("HaHaHa You Can't Kill Me! (ULTIMATE Ability)", source, rank);
		this.owner = source;
		
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
		if (this.rank() == 2) {
			amount = 30;
		}
		// Increased to 50% at rank 3 for 3 turns
		else if (this.rank() >= 3) {
			amount = 50;
			duration = 3;
		}
		StatusEffect armorBonus = new StatusEffect(Stat.Version.ARMOR, amount, StatusEffect.Type.INCOMING);
		
		// Creates the condition with armor bonus as needed
		this.selfArmorBonus = new Condition("HaHaHa You Can't Kill Me!: Self Armor Bonus", duration);
		this.selfArmorBonus.setSource(this.owner);
		this.selfArmorBonus.addStatusEffect(armorBonus);
	}
	// Sets the Taunt Condition (with the reduced damage status effect as necessary)
	public void setEnemyTauntEffect(Character enemy) {
		int duration = 2;
		if (this.rank() >= 3) {
			duration = 3;
		}
		this.enemyTauntEffect = new Condition("HaHaHa You Can't Kill Me!: Taunt Effect", duration);
		this.enemyTauntEffect.setSource(this.owner);
		
		// Adds reduced damage at rank 2 based on enemy affected
		if (this.rank() >= 2) {
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
			StatusEffect damageReduction = new StatusEffect(Stat.Version.DAMAGE, -amount, StatusEffect.Type.OUTGOING);
			
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
		if (this.rank() >= 3) {
			amount = 50;
			duration = 1;
		}
		StatusEffect damageBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		
		// Creates the condition with the damage bonus as needed
		this.allyDamageBonus = new Condition("HaHaHa You Can't Kill Me: Damage Bonus", duration);
		this.allyDamageBonus.setSource(this.owner);
		this.allyDamageBonus.addStatusEffect(damageBonus);
	}
	
	// Get methods for the additional variables of this Ability
	@Override
	public SteelLegionTank getOwner() {
		return this.owner;
	}
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
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Before anything, put HahahaYouCantKillMe "on Cooldown"
		this.setOnCooldown();
		
		// Heal to full Health
		int healing = this.owner.getHealth() - this.owner.getCurrentHealth();
		healing = this.owner.restoreHealth(healing);
		System.out.println(this.owner.getName() + " healed for " + healing + " Health for a new total of " + this.owner.getCurrentHealth());
		
		// Apply Additional Conditions
		this.owner.addCondition(this.getSelfArmorBonus());
		for (Character enemy : BattleSimulator.getInstance().getEnemies()) {
			enemy.addCondition(this.getEnemyTauntEffect(enemy));
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use function called when the Steel Legion Tank dies
	public void useDeath() {
		// Create a list to hold all allies
		LinkedList<Character> allies = BattleSimulator.getInstance().getAllies();
		
		// All this only happens at rank 3
		if (this.rank() >= 3) {
			// Restore each ally for 25% of their max Health and give each ally the damage buff from the Ability
			for (Character ally : allies) {
				if (!ally.equals(this.owner)) {
					// Calculates the healing amount for the ally in the list
					int healing = (int)Math.round(ally.getHealth() * .25);
					
					healing = ally.restoreHealth(healing);
					System.out.println(ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth());
					
					// Gives each ally the Damage buff and Invincibility for 1 turn
					ally.addCondition(this.getAllyDamageBonus());
					ally.addCondition(new Invincible("HaHaHa You Can't Kill Me: Invincibility", 1));
				}
			}
		}
	}
	
	// Use function with specified version
	@Override
	public void use(int version) {
		// If 2, specify the Death Version
		if (version == 2) {
			this.useDeath();
			return;
		}
		// Otherwise use the base version where 1 is use() and anything else is a warning
		super.use(version);
	}
}



// The Steel Legion Tank itself:
public class SteelLegionTank extends Character {
	// Enumerates the names of the abilities so Cooldown and use functions can be called
	public static enum AbilityNames {
		HoldItRightThere, EnchantedArmor, ShieldSkills, ProfessionalLaughter, ShieldBash, ShieldReflection, TauntingAttack, LeaderStrike, HaHaHaYouCantKillMe
	}
	
	// Passive Abilities
	private HoldItRightThere HoldItRightThere; // Unique Passive Ability (UPA)
	private EnchantedArmor EnchantedArmor;
	private ShieldSkills ShieldSkills;
	private ProfessionalLaughter ProfessionalLaughter;
	
	// Base Abilities
	private ShieldBash ShieldBash;
	private ShieldReflection ShieldReflection;
	private TauntingAttack TauntingAttack;
	private LeaderStrike LeaderStrike;
	private HaHaHaYouCantKillMe HaHaHaYouCantKillMe;
	
	// Keeps track of the buff for if a previous attack was blocked
	public boolean didBlock;
	
	// A List of all Abilities so all Cooldowns can be reduced at once
	private HashMap<SteelLegionTank.AbilityNames, Ability> abilities;
	
	// These first two methods help set up the Steel Legion Tank subclass.
	public SteelLegionTank(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, Type type, int upaRank, int eArmorRank, int sSkillsRank, int profLaughRank, int sBashRank, int sReflectRank, int tAttackRank, int lStrikeRank, int haRank) {
		// Calls the super constructor to create the Character, then initializes all Abilities according to their specifications.
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, type);
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
		this.abilities = new HashMap<>();
		this.abilities.put(SteelLegionTank.AbilityNames.HoldItRightThere, this.HoldItRightThere);
		this.abilities.put(SteelLegionTank.AbilityNames.EnchantedArmor, this.EnchantedArmor);
		this.abilities.put(SteelLegionTank.AbilityNames.ShieldSkills, this.ShieldSkills);
		this.abilities.put(SteelLegionTank.AbilityNames.ProfessionalLaughter, this.ProfessionalLaughter);
		this.abilities.put(SteelLegionTank.AbilityNames.ShieldBash, this.ShieldBash);
		this.abilities.put(SteelLegionTank.AbilityNames.ShieldReflection, this.ShieldReflection);
		this.abilities.put(SteelLegionTank.AbilityNames.TauntingAttack, this.TauntingAttack);
		this.abilities.put(SteelLegionTank.AbilityNames.LeaderStrike, this.LeaderStrike);
		this.abilities.put(SteelLegionTank.AbilityNames.HaHaHaYouCantKillMe, this.HaHaHaYouCantKillMe);
		
		// Add new commands for Abilities
		this.addCommand(new AbilityCommand(this.ShieldBash));
		this.addCommand(new AbilityCommand(this.ShieldReflection));
		this.addCommand(new AbilityCommand(this.TauntingAttack));
		this.addCommand(new AbilityCommand(this.LeaderStrike));
		this.addCommand(new AbilityCommand(this.HaHaHaYouCantKillMe));
		
		// Set didBlock to false to start
		this.didBlock = false;
	}
	public SteelLegionTank(Character copy, int upaRank, int eArmorRank, int sSkillsRank, int profLaughRank, int sBashRank, int sReflectRank, int tAttackRank, int lStrikeRank, int haRank) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getBaseDmgType(), copy.getResistances(), copy.getVulnerabilities(), copy.getType(), upaRank, eArmorRank, sSkillsRank, profLaughRank, sBashRank, sReflectRank, tAttackRank, lStrikeRank, haRank);
	}
	public SteelLegionTank(SteelLegionTank copy) {
		this(copy, copy.getHoldItRightThereRank(), copy.getEnchantedArmorRank(), copy.getShieldSkillsRank(), copy.getProfessionalLaughterRank(), copy.getShieldBashRank(), copy.getShieldReflectionRank(), copy.getTauntingAttackRank(), copy.getTauntingAttackRank(), copy.getHaHaHaYouCantKillMeRank());
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
	
	// Functions to use an Ability or affect its Cooldown
	public void useAbility(SteelLegionTank.AbilityNames name, int version) {
		Ability chosen = this.abilities.get(name);
		chosen.use(version);
	}
	public void useAbility(SteelLegionTank.AbilityNames name) {
		this.useAbility(name, 1);
	}
	
	// Function to set an ability's Cooldown
	public void setAbilityCD(SteelLegionTank.AbilityNames name, int turnsRemaining) {
		Ability chosen = this.abilities.get(name);
		chosen.setTurnsRemaining(turnsRemaining);
	}
	
	// Function to get the rank of an Ability
	public int getAbilityRank(SteelLegionTank.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.rank();
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
	protected void beginTurnSetup() {
		// Do the usual setup
		super.beginTurnSetup();
		
		// Reduces the Cooldown of all Abilities that need it.
		for (Ability a : abilities.values()) {
			if (a.onCooldown()) {
				a.decrementTurnsRemaining();
			}
		}
	}
	
	@Override
	protected void printTurnStats() {
		// Prints all the usual stats
		super.printTurnStats();
		
		// Then, checks to see if it is the beginning of the round (Rank 15 of Professional Laughter gives Taunt)
		if (BattleSimulator.getInstance().getRound() == 1 && this.ProfessionalLaughter.rank() >= 15) {
			System.out.println("You have \"Taunt\" for 2 rounds.\n");
		}
	}
	
	// End of Turn Override
	@Override
	public void endTurnSetup() {
		// Normal Setup
		super.endTurnSetup();
		
		// If not dead, use Enchanted Armor Healing (end of turn effect)
		if (!this.isDead()) {
			this.EnchantedArmor.use();
		}
	}
	
	// Overrides "applyPostAttackEffects" in order to also store the fact that an attack was blocked in "Shield Bash" and "Shield Reflection"
	@Override
	protected void applyPostAttackEffects(AttackResult atkRes) {
		super.applyPostAttackEffects(atkRes);
		this.didBlock = !atkRes.didHit();
		if (this.isDead() && this.HaHaHaYouCantKillMe.rank() >= 3) {
			this.useAbility(SteelLegionTank.AbilityNames.HaHaHaYouCantKillMe, 2);
		}
	}
}

// Builds a Steel Legion Warrior
class SteelLegionTankBuilder extends CharacterBuilder {
	// Creates all the Ability fields
	private int HoldItRightThereRank;
	private int EnchantedArmorRank;
	private int ShieldSkillsRank;
	private int ProfessionalLaughterRank;
	private int ShieldBashRank;
	private int ShieldReflectionRank;
	private int TauntingAttackRank;
	private int LeaderStrikeRank;
	private int HaHaHaYouCantKillMeRank;
	
	// Constructs a SteelLegionTankBuilder (populating the CharacterBuilder variables) based on the constant stats from Character
	public SteelLegionTankBuilder(Character base) {
		super(base);
		this.HoldItRightThereRank = 0;
		this.EnchantedArmorRank = 0;
		this.ShieldSkillsRank = 0;
		this.ProfessionalLaughterRank = 0;
		this.ShieldBashRank = 0;
		this.ShieldReflectionRank = 0;
		this.TauntingAttackRank = 0;
		this.LeaderStrikeRank = 0;
		this.HaHaHaYouCantKillMeRank = 0;
	}
	public SteelLegionTankBuilder(SteelLegionTank base) {
		super(base);
		this.HoldItRightThereRank = base.getHoldItRightThereRank();
		this.EnchantedArmorRank = base.getEnchantedArmorRank();
		this.ShieldSkillsRank = base.getShieldSkillsRank();
		this.ProfessionalLaughterRank = base.getProfessionalLaughterRank();
		this.ShieldBashRank = base.getShieldBashRank();
		this.ShieldReflectionRank = base.getShieldReflectionRank();
		this.TauntingAttackRank = base.getTauntingAttackRank();
		this.LeaderStrikeRank = base.getLeaderStrikeRank();
		this.HaHaHaYouCantKillMeRank = base.getHaHaHaYouCantKillMeRank();
	}
	public SteelLegionTankBuilder() {
		this(Character.STEEL_LEGION_TANK);
	}
	
	
	// Overrides the functions necessary from CharacterBuilder when constant stats are given.
	@Override
	public SteelLegionTankBuilder Name(String name) {
		super.Name(name);
		return this;
	}
	@Override
	public SteelLegionTankBuilder Level(int level) {
		super.Level(level);
		return this;
	}
	
	@Override
	public SteelLegionTankBuilder bonusHealth(int bonusHealth) {
		super.bonusHealth(bonusHealth);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusDamage(int bonusDamage) {
		super.bonusDamage(bonusDamage);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusArmor(int bonusArmor) {
		super.bonusArmor(bonusArmor);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		super.bonusArmorPiercing(bonusArmorPiercing);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusAccuracy(int bonusAccuracy) {
		super.bonusAccuracy(bonusAccuracy);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusBlock(int bonusBlock) {
		super.bonusBlock(bonusBlock);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusCriticalChance(int bonusCriticalChance) {
		super.bonusCriticalChance(bonusCriticalChance);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusSpeed(int bonusSpeed) {
		super.bonusSpeed(bonusSpeed);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		super.bonusAttackSpeed(bonusAttackSpeed);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusThreat(int bonusThreat) {
		super.bonusThreat(bonusThreat);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		super.bonusTacticalThreat(bonusTacticalThreat);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusSTDdown(int bonusSTDdown) {
		super.bonusSTDdown(bonusSTDdown);
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusSTDup(int bonusSTDup) {
		super.bonusSTDup(bonusSTDup);
		return this;
	}
	
	@Override
	public SteelLegionTankBuilder baseDmgType(Attack.DmgType dmgType) {
		super.baseDmgType(dmgType);
		return this;
	}
	@Override
	public SteelLegionTankBuilder addResistance(Attack.DmgType resistance, double value) {
		super.addResistance(resistance, value);
		return this;
	}
	@Override
	public SteelLegionTankBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
		super.addVulnerability(vulnerability, value);
		return this;
	}
	
	@Override
	public SteelLegionTankBuilder Type(Character.Type type) {
		super.Type(type);
		return this;
	}
	
	
	// Sets the ranks of each Ability (then defines the base Cooldown and Scaler based on that)
	
	// Hold It Right There (Passive Ability)
	public SteelLegionTankBuilder setHoldItRightThereRank(int newRank) {
		this.HoldItRightThereRank = newRank;
		return this;
	}
	// Enchanted Armor (Passive Ability):
	public SteelLegionTankBuilder setEnchantedArmorRank(int newRank) {
		this.EnchantedArmorRank = newRank;
		return this;
	}
	// Shield Skills (Passive Ability):
	public SteelLegionTankBuilder setShieldSkillsRank(int newRank) {
		this.ShieldSkillsRank = newRank;
		return this;
	}
	// Professional Laughter (Passive Ability):
	public SteelLegionTankBuilder setProfessionalLaughterRank(int newRank) {
		this.ProfessionalLaughterRank = newRank;
		return this;
	}
	
	// Shield Bash (Ability 1):
	public SteelLegionTankBuilder setShieldBashRank(int newRank) {
		this.ShieldBashRank = newRank;
		return this;
	}
	// Shield Reflection (Ability 2):
	public SteelLegionTankBuilder setShieldReflectionRank(int newRank) {
		this.ShieldReflectionRank = newRank;
		return this;
	}
	// Taunting Attack (Ability 3):
	public SteelLegionTankBuilder setTauntingAttackRank(int newRank) {
		this.TauntingAttackRank = newRank;
		return this;
	}
	// Leader Strike (Ability 4):
	public SteelLegionTankBuilder setLeaderStrikeRank(int newRank) {
		this.LeaderStrikeRank = newRank;
		return this;
	}
	// Taunting Attack (ULTIMATE):
	public SteelLegionTankBuilder setHaHaHaYouCantKillMeRank(int newRank) {
		this.HaHaHaYouCantKillMeRank = newRank;
		return this;
	}
	
	private void setBaseStats() {
		// Each stat is already set to its level 1 base value
		// Note: below only occurs if they specified a level, since the base level is 0.
		// "Level Up" each stat: (Multiply by the given multiplier for each level up to the current level)
		for (int counter = 2; counter <= this.Level; counter++) {
			// Statically increasing stats (increases by same amount each level)
			this.Damage = (int)Math.round(this.Damage * 1.03);
			this.Armor = (int)Math.round(this.Armor * 1.05);
			this.ArmorPiercing = (int)Math.round(this.ArmorPiercing * 1.05);
			this.Accuracy = (int)Math.round(this.Accuracy * 1.05);
			this.Block = (int)Math.round(this.Block * 1.05);
			
			// Dynamically increasing stats
			// Health changes at intervals of 5 and 10
			if (counter % 10 == 0) {
				this.Health = (int)Math.round(this.Health * 1.07);
			}
			else if (counter % 5 == 0) {
				this.Health = (int)Math.round(this.Health * 1.05);
			}
			else {
				this.Health = (int)Math.round(this.Health * 1.03);
			}
			
			// Attack Speed increases every 20 levels by 2
			if (counter % 20 == 0) {
				this.AttackSpeed += 2;
			}
			
			// Threat increases with various amounts at the given levels
			if (counter == 10) {
				this.Threat += 5;
			}
			if (counter == 30) {
				this.Threat += 6;
			}
			if (counter == 50) {
				this.Threat += 7;
			}
			if (counter == 70) {
				this.Threat += 8;
			}
			if (counter == 90) {
				this.Threat += 10;
			}
		}
		
		// Calculate the bonus stats given by certain Abilities
		if (this.EnchantedArmorRank > 0) {
			EnchantedArmor ea = new EnchantedArmor(this.EnchantedArmorRank);
			this.bArmor += ea.getBonusArmor();
			this.bBlock += ea.getBonusBlock();
		}
		if (this.ProfessionalLaughterRank > 0) {
			ProfessionalLaughter pf = new ProfessionalLaughter(this.ProfessionalLaughterRank);
			this.bThreat += pf.getBonusThreat();
			this.bTacticalThreat += pf.getBonusTacticalThreat();
			this.bHealth += pf.getBonusHealth();
		}
	}
	
	// Finishes the build by returning a SteelLegionTank Character
	public SteelLegionTank build() {
		// Sets the base stats based on level and abilities
		this.setBaseStats();
		
		// Return the Steel Legion Tank
		return new SteelLegionTank(super.build(), this.HoldItRightThereRank, this.EnchantedArmorRank, this.ShieldSkillsRank, this.ProfessionalLaughterRank, this.ShieldBashRank, this.ShieldReflectionRank, this.TauntingAttackRank, this.LeaderStrikeRank, this.HaHaHaYouCantKillMeRank);
	}
}
