package WyattWitemeyer.WarOfGillysburg;
import java.util.*;


// Passive Abilities:
// Unique Passive Ability: "Empowered Arrows"
class EmpoweredArrows extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private double multAbilityScaler;
	private int stackRequirement;
	private Condition abilityPreAttackBonus;
	
	// Constructor
	public EmpoweredArrows(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \"Empowered Arrows\"", source, rank);
		this.owner = source;
		
		// Set the scaler and the stack requirement
		this.setMultAbilityScaler();
		this.setStackRequirement();
		
		// Sets the Ability Pre-Attack Bonus
		this.setPreAttackBonus();
	}
	
	// Sets the scaler for using multiple empowered Abilities at one time (also sets the default scaler equal to this value)
	private void setMultAbilityScaler() {
		switch(this.rank()) {
			case 1:
			case 2:
			case 3:
				this.multAbilityScaler = 1.0;
				break;
			case 4:
				this.multAbilityScaler = 1.5;
				break;
			case 5:
				this.multAbilityScaler = 2.0;
			default:
				this.multAbilityScaler = 1.0;
		}
		this.scaler = this.multAbilityScaler;
	}
	
	// Sets the stack requirement based on level
	private void setStackRequirement() {
		// The stack requirement is always 4 until rank 5 when it is reduced to 3.
		this.stackRequirement = 4;
		if (this.rank() >= 5) {
			this.stackRequirement = 3;
		}
	}
	
	// Sets the Condition that enhances damage for the Empowered Abilities
	private void setPreAttackBonus() {
		// At rank 1, and by default, the damage bonus is 25%
		int amount = 25;
		switch (this.rank()) {
			case 1:
				amount = 25;
				break;
			case 2:
				amount = 30;
				break;
			case 3:
				amount = 35;
				break;
			case 4:
				amount = 40;
				break;
			case 5:
				amount = 50;
				break;
		}
		
		// Creates the StatusEffect for bonus Damage and Accuracy Conditions to be added to the respective Abilities attack(s)
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, amount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		// Create the Condition, duration of 0 since its only used for the one attack
		this.abilityPreAttackBonus = new Condition("Empowered Arrows: Pre Attack Damage Bonus", 0);
		this.abilityPreAttackBonus.setSource(this.owner);
		this.abilityPreAttackBonus.addStatusEffect(dmgBonus);
		this.abilityPreAttackBonus.addStatusEffect(accBonus);
	}
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getMultAbilityScaler() {
		return this.multAbilityScaler;
	}
	
	public int getStackRequirement() {
		return this.stackRequirement;
	}
	
	public Condition getAbilityPreAttackBonus() {
		return new Condition(this.abilityPreAttackBonus);
	}
	
	// Use function called when the action is chosen from the possible Commands (uses multiple empowered abilities)
	@Override
	public void use() {
		// If unavailable (no abilities currently empowered), state the issue and immediately return
		if (!this.owner.hasEmpoweredAbility()) {
			System.out.println("There are no empowered abilities, select a different action.");
			return;
		}
		
		// Get the available Empowered Abilities as options for multiple selection
		LinkedList<String> abilityChoices = new LinkedList<>();
		for (int abilityNum : this.owner.getEmpoweredAbilities()) {
			if (abilityNum == 1) {
				abilityChoices.add("Flaming Arrow");
			}
			else if (abilityNum == 2) {
				abilityChoices.add("Frozen Arrow");
			}
			else if (abilityNum == 3) {
				abilityChoices.add("Exploding Arrow");
			}
			else if (abilityNum == 4) {
				abilityChoices.add("Penetration Arrow");
			}
			else if (abilityNum == 5) {
				abilityChoices.add("Restoration Arrow");
			}
		}
		
		// Select the Empowered Abilities that will be used
		LinkedList<Integer> toUse = new LinkedList<>();
		toUse = BattleSimulator.getInstance().promptSelectMultiple("Select all available Empowered Abilities you wish to use this turn.", abilityChoices);
		if (toUse.isEmpty()) {
			return;
		}
		
		// Use each ability marked in toUse (holds index from owner's list of empowered abilities (toUse is 1-indexed)
		for (int index : toUse) {
			int ability = this.owner.getEmpoweredAbilities().get(index - 1);
			// Flaming Arrow
			if (ability == 1) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.FlamingArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Frozen Arrow
			else if (ability == 2) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.FrozenArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Exploding Arrow
			else if (ability == 3) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.ExplodingArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Penetration Arrow
			else if (ability == 4) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.PenetrationArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
			// Restoration Arrow
			else if (ability == 5) {
				this.owner.useAbilityEmpowered(SentinelSpecialist.AbilityNames.RestorationArrow, this.getMultAbilityScaler() / toUse.size(), false);
			}
		}
		
		// Lastly, using this Ability in this manner also uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Overrides the to-String to more correctly display the function of this Ability's use function
	public String toString() {
		return "Passive: \"Empowered Arrows\" (Use multiple EMPOWERED Abilities) - " + (this.owner.hasEmpoweredAbility()? "Available" : "Unavailable");
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 3? ("\n\tMultiple Ability Scaler: " + this.getMultAbilityScaler()) : "";
		ret += "\n\tStack Requirement: " + this.getStackRequirement();
		ret += "\n\t" + this.getAbilityPreAttackBonus();
		return ret;
	}
}

// Base Passive Ability: "Masterwork Arrows"
class MasterworkArrows extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private int bonusDamageStat;
	private int bonusArmorPiercingStat;
	private int bonusAccuracyStat;
	
	// Constructor
	public MasterworkArrows(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Masterwork Arrows\"", source, rank);
		this.owner = source;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	// An additional constructor to Calculate the bonus stats in order to create a Sentinel Specialist
	public MasterworkArrows(int rank) {
		super("No Owner: Masterwork Arrows", Character.EMPTY, rank);
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	
	// Calculates the flat stat bonuses for Damage, Armor Piercing, and Accuracy
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusDamageStat = 0;
		this.bonusArmorPiercingStat = 0;
		this.bonusAccuracyStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Ranks 1-2 grant +20 Damage and +3 Armor Piercing per rank
			if (walker <= 2) {
				this.bonusDamageStat += 20;
				this.bonusArmorPiercingStat += 3;
			}
			// Ranks 3-5 grant +35 Damage, +5 Armor Piercing, and +3 Accuracy per rank
			else if (walker <= 5) {
				this.bonusDamageStat += 35;
				this.bonusArmorPiercingStat += 5;
				this.bonusAccuracyStat += 3;
			}
			// Ranks 6-10 grant +55 Damage, +7 Armor Piercing, and +5 Accuracy per rank
			else if (walker <= 10) {
				this.bonusDamageStat += 55;
				this.bonusArmorPiercingStat += 7;
				this.bonusAccuracyStat += 5;
			}
			// Ranks 11-14 grant +75 Damage, +10 Armor Piercing, and +7 Accuracy per rank
			else if (walker <= 14) {
				this.bonusDamageStat += 75;
				this.bonusArmorPiercingStat += 10;
				this.bonusAccuracyStat += 7;
			}
			// Rank 15 grants +200 Damage, +15 Armor Piercing, and +10 Accuracy
			else if (walker == 15) {
				this.bonusDamageStat += 200;
				this.bonusArmorPiercingStat += 15;
				this.bonusAccuracyStat += 10;
			}
		}
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public int getDamageBonus() {
		return this.bonusDamageStat;
	}
	
	public int getArmorPiercingBonus() {
		return this.bonusArmorPiercingStat;
	}
	
	public int getAccuracyBonus() {
		return this.bonusAccuracyStat;
	}
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Damage Stat: " + this.getDamageBonus();
		ret += "\n\tBonus Armor Piercing Stat: " + this.getArmorPiercingBonus();
		ret += this.rank() >= 3? ("\n\tBonus Accuracy Stat: " + this.getAccuracyBonus()) : "";
		return ret;
	}
}

// Base Passive Ability: "Survivable"
class Survivable extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private int bonusHealthStat;
	private int bonusArmorStat;
	private int bonusDodgeStat;
	
	private double blockChance;
	private double healingPercentage;
	private Condition nextAttackBonus;
	
	private boolean didAvoid;
	
	// Constructor
	public Survivable(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Survivable\"", source, rank);
		this.owner = source;
		this.didAvoid = false;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
		
		// Calculate the chance to block, healing amount, and Condition effectiveness
		this.setStatBlockChance();
		this.setHealingPercentage();
		this.setNextAttackBonus();
	}
	// An additional constructor to Calculate the bonus stats in order to create a Sentinel Specialist
	public Survivable(int rank) {
		super("No Owner: Survivable", Character.EMPTY, rank);
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
	}
	
	// Calculates the flat stat bonuses for Damage, Armor Piercing, and Accuracy
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusHealthStat = 0;
		this.bonusArmorStat = 0;
		this.bonusDodgeStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants +40 Health and +2 Armor
			if (walker == 1) {
				this.bonusHealthStat += 40;
				this.bonusArmorStat += 2;
			}
			// Ranks 2-4 grant +75 Health, +3 Armor, and +1 Dodge per rank
			else if (walker <= 4) {
				this.bonusHealthStat += 75;
				this.bonusArmorStat += 3;
				this.bonusDodgeStat += 1;
			}
			// Rank 5 grants +150 Health, +5 Armor, and +1 Dodge
			else if (walker == 5) {
				this.bonusHealthStat += 150;
				this.bonusArmorStat += 5;
				this.bonusDodgeStat += 1;
			}
			// Ranks 6-9 grant +325 Health, +7 Armor, and +2 Dodge per rank
			else if (walker <= 9) {
				this.bonusHealthStat += 325;
				this.bonusArmorStat += 7;
				this.bonusDodgeStat += 2;
			}
			// Rank 10 grants +375 Health, +8 Armor, and +3 Dodge
			else if (walker == 10) {
				this.bonusHealthStat += 375;
				this.bonusArmorStat += 8;
				this.bonusDodgeStat += 3;
			}
			// Ranks 11-14 grant +450 Health, +10 Armor, and +3 Dodge per rank
			else if (walker <= 14) {
				this.bonusHealthStat += 450;
				this.bonusArmorStat += 10;
				this.bonusDodgeStat += 3;
			}
			// Rank 15 grants +750 Health, +15 Armor, and +5 Dodge
			else if (walker == 15) {
				this.bonusHealthStat += 750;
				this.bonusArmorStat += 15;
				this.bonusDodgeStat += 5;
			}
		}
	}
	
	// Sets the chance to block if a Dodge attempt fails
	public void setStatBlockChance() {
		// Start at zero
		this.blockChance = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants 1% block chance
			if (walker == 1) {
				this.blockChance += .01;
			}
			// Ranks 2-4 grant +0.3% block chance per rank
			else if (walker <= 4) {
				this.blockChance += .003;
			}
			// Rank 5 grants +0.6% block chance
			else if (walker == 5) {
				this.blockChance += .006;
			}
			// Ranks 6-9 grant +0.8% block chance per rank
			else if (walker <= 9) {
				this.blockChance += .008;
			}
			// Rank 10 grants +1.3% block chance
			else if (walker == 10) {
				this.blockChance += .013;
			}
			// Ranks 11-14 grant +1.5% block chance per rank
			else if (walker <= 14) {
				this.blockChance += .015;
			}
			// Rank 15 grants +2% block chance
			else if (walker == 15) {
				this.blockChance += .02;
			}
		}
	}
	
	// Sets the healing percentage for healing based on missing Health
	public void setHealingPercentage() {
		// Start at zero
		this.healingPercentage = 0;
		
		for (int walker = 10; walker <= this.rank(); walker++) {
			// Rank 10 grants +10% missing health
			if (walker == 10) {
				this.healingPercentage += 10;
			}
			// Ranks 11-14 grant +3% missing health per rank
			else if (walker <= 14) {
				this.healingPercentage += 3;
			}
			// Rank 15 grants +2% block chance
			else if (walker == 15) {
				this.healingPercentage += 8;
			}
		}
	}
	
	// Sets the Condition for the bonus that occurrs for the next attack after successfully blocking
	public void setNextAttackBonus() {
		// The amount for each status effect (damage and accuracy bonus) is equal to the healing percentage based on missing Health at all ranks
		double amount = this.getHealingPercentage();
		
		// Create the StatusEffects
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, amount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		// Set the Condition to have the two StatusEffects, but decrement based on Charges instead of turns (permanent otherwise)
		this.nextAttackBonus = new Condition("Survivable: Next Attack Bonus", -1);
		this.nextAttackBonus.setSource(this.owner);
		this.nextAttackBonus.makeChargeBased(1);
		this.nextAttackBonus.addStatusEffect(dmgBonus);
		this.nextAttackBonus.addStatusEffect(accBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	// For Stat Bonuses
	public int getHealthBonus() {
		return this.bonusHealthStat;
	}
	
	public int getArmorBonus() {
		return this.bonusArmorStat;
	}
	
	public int getDodgeBonus() {
		return this.bonusDodgeStat;
	}
	// For other effects
	public double getBlockChance() {
		return this.blockChance;
	}
	
	public double getHealingPercentage() {
		return this.healingPercentage;
	}
	
	public Condition getNextAttackBonus() {
		return new Condition(this.nextAttackBonus);
	}
	
	// Applies the pre attack effects of the chance to block when Dodging fails
	@Override
	public void applyPreAttackEffects(Attack atk) {
		// Resets didAvoid if need be
		this.didAvoid = false;
		
		// Only acts if the owner is the defender
		if (atk.getDefender().equals(this.getOwner())) {
			// First find the results of if the incoming attack would normally land
			boolean willHit = atk.canHit();
			if (willHit && atk.canMiss()) {
				willHit = atk.landAttack();
			}
			
			// If the attack will hit, but can miss, instead add a chance to block
			if (willHit && atk.canMiss()) {
				// Get the chance to block the attack
				double blkChance = this.getBlockChance();
				
				// Possible bonuses based off Enemy attacker
				if (atk.getAttacker() instanceof Enemy) {
					Enemy attacker = (Enemy)atk.getAttacker();
					
					// At rank 5 the chance is doubled vs Normal and Advanced enemies
					if (this.rank() >= 5 && (attacker.getDifficulty().equals(Enemy.Difficulty.NORMAL) || attacker.getDifficulty().equals(Enemy.Difficulty.ADVANCED))) {
						blkChance *= 2;
					}
					
					// At rank 15 the chance is increased by 50% vs Elite enemies
					if (this.rank() >= 15 && attacker.getDifficulty().equals(Enemy.Difficulty.ELITE)) {
						blkChance *= 1.5;
					}
				}
				
				// Create a random probability
				Random rd = new Random();
				double prob = rd.nextDouble();
				
				// If the random probably is less than the chance to block, the attack will be blocked instead
				if (prob <= blkChance) {
					// Above rank 10, extra bonuses need to be applied in the PostAttackEffects
					if (this.rank() >= 10) {
						this.didAvoid = true;
					}
					
					// Change the attack so that is cannot hit, and state that the Armor protected the Sentinel Specialist
					atk.setCanHit(false);
					System.out.println(this.getOwner().getName() + "\'s Armor protected " + this.getOwner().getName() + " from " + atk.getAttacker().getName() + "\'s attack!");
				}
				// Otherwise, the attack must hit since the land attack has already been calculated, change the attack to represent this
				else {
					atk.makeCannotMiss();
				}
			}
			// If the attack will not hit, change the attack to reflect that, and at rank 15 the extra post-attack bonuses still occur
			else if (!willHit) {
				atk.setCanHit(false);
				if (this.rank() >= 15) {
					this.didAvoid = true;
				}
			}
		}
	}
	
	// Applies the post attack effects of the chance to block when Dodging fails
	@Override
	public void applyPostAttackEffects(AttackResult atkRes) {
		// Bonues are only applied when the owner is the defender and the local "didAvoid" is true (which as an additional check must happen after rank 10
		if (atkRes.getDefender().equals(this.getOwner()) && this.didAvoid && this.rank() >= 10) {
			// Heal the owner for the amount of missing health based on the scaler of this Ability
			this.getOwner().restoreHealth((int)Math.round(this.getOwner().getMissingHealth() * this.getHealingPercentage() / 100.0));
			
			// Add the next-attack buff to the owner
			this.getOwner().addCondition(this.getNextAttackBonus());
			
			// Reset didAvoid to false
			this.didAvoid = false;
		}
	}
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Health Stat: " + this.getHealthBonus();
		ret += "\n\tBonus Armor Stat: " + this.getArmorBonus();
		ret += this.rank() >= 2? ("\n\tBonus Dodge Stat: " + this.getDodgeBonus()) : "";
		ret += "\n\tBlock Chance: " + (this.getBlockChance() * 100.0) + "%";
		ret += "\n\tHealing Percentage (missing health): " + this.getHealingPercentage() + "%";
		ret += "\n\t" + this.getNextAttackBonus().toString();
		return ret;
	}
}

// Base Passive Ability: "Multi-Purposed"
class MultiPurposed extends Ability {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional Variables
	private Condition abilityDamageBonus;
	private double scalerBonusPerCooldown;
	private double critAccBonusPerCooldown;
	private int uniqueRequirement;
	
	// Constructor
	public MultiPurposed(SentinelSpecialist source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Multi-Purposed\"", source, rank);
		this.owner = source;
		
		// Set the bonus effects of the Ability and the unique requirement
		this.setAbilityDamageBonus();
		this.setCooldownBonuses();
		this.setUniqueRequirement();
	}
	
	// Sets the damage bonus applied to the random Ability cast when enough unique abilities have been used
	private void setAbilityDamageBonus() {
		// Initialize the amount to 0
		int amount = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants 10% damage
			if (walker == 1) {
				amount += 10;
			}
			// Ranks 2-4 grant +1% damage per rank
			else if (walker <= 4) {
				amount += 1;
			}
			// Rank 5 grants +2% damage
			else if (walker == 5) {
				amount += 2;
			}
			// Ranks 6-10 grant +3% damage per rank
			else if (walker <= 10) {
				amount += 3;
			}
			// Ranks 11-15 grant +4% damage per rank
			else if (walker <= 15) {
				amount += 4;
			}
		}
		
		// Create the StatusEffect
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, amount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		
		// Create the Condition with the StatusEffect with a duration of 0 since it is 1-time use
		this.abilityDamageBonus = new Condition("Multi-Purposed: Random Ability Damage Bonus", 0);
		this.abilityDamageBonus.setSource(this.owner);
		this.abilityDamageBonus.addStatusEffect(dmgBonus);
	}
	
	// Sets the scaler and critical chance bonuses based on number of turns in Cooldown
	private void setCooldownBonuses() {
		// Intitalize both to be 0
		this.scalerBonusPerCooldown = 0;
		this.critAccBonusPerCooldown = 0;
		
		// Neither starts until rank 5
		for (int walker = 5; walker <= this.rank(); walker++) {
			// Rank 5 grants +.05x to scalers (no effect yet on crit)
			if (walker == 5) {
				this.scalerBonusPerCooldown += .05;
			}
			// Ranks 6-9 grant +.01x to scalers per rank
			else if (walker <= 9) {
				this.scalerBonusPerCooldown += .01;
			}
			// Rank 10 grants +.01x to scalers and +1% crit
			else if (walker == 10) {
				this.scalerBonusPerCooldown += .01;
				this.critAccBonusPerCooldown += 1;
			}
			// Ranks 11-14 grant +.02x to scalers and +.5% crit per rank
			else if (walker <= 14) {
				this.scalerBonusPerCooldown += .02;
				this.critAccBonusPerCooldown += .5;
			}
			// Rank 15 grants +.02x to scalers and +2% crit
			else if (walker == 15) {
				this.scalerBonusPerCooldown += .02;
				this.critAccBonusPerCooldown += 2;
			}
		}
	}
	
	// Sets the number of unique Abilities that need to be cast to get the random basic Ability bonus
	private void setUniqueRequirement() {
		// The unique requirement is always 4 until rank 15, then it is 3
		this.uniqueRequirement = 4;
		if (this.rank() == 15) {
			this.uniqueRequirement = 3;
		}
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public Condition getAbilityDamageBonus() {
		return new Condition(this.abilityDamageBonus);
	}
	
	public double getScalerBonus(int cdTurns) {
		return this.scalerBonusPerCooldown * cdTurns;
	}
	
	// Returns the critical chance and accuracy bonuses that are recalculated at the beginning of every turn
	public Condition getPermanentCondition(int cdTurns) {
		// Create the status effects for crit and accuracy bonuses
		double amount = this.critAccBonusPerCooldown*cdTurns;
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, amount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, amount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		// Create the permanent Condition
		Condition cdBonuses = new Condition("Multi-Purposed: Crit and Accuracy CD Bonuses", -1);
		cdBonuses.setSource(this.owner);
		if (amount != 0) {
			cdBonuses.addStatusEffect(critBonus);
			cdBonuses.addStatusEffect(accBonus);
		}
		
		// Return the result
		return cdBonuses;
	}
	
	public int getUniqueRequirement() {
		return this.uniqueRequirement;
	}
	
	// Function returning the custom command to cast a random ability from replaced basic attack
	public CustomCommand getRandomAbilityCommand() {
		// Create the Custom Command Execution needed for the random ability command
		CustomCmdExe useRandomAbility = () -> {
			// Get the 4 possible Abilities as long as their rank > 0
			LinkedList<SentinelSpecialist.AbilityNames> abilities = new LinkedList<>();
			if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.FlamingArrow) > 0) {
				abilities.add(SentinelSpecialist.AbilityNames.FlamingArrow);
			}
			if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.FrozenArrow) > 0) {
				abilities.add(SentinelSpecialist.AbilityNames.FrozenArrow);
			}
			if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.ExplodingArrow) > 0) {
				abilities.add(SentinelSpecialist.AbilityNames.ExplodingArrow);
			}
			if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.PenetrationArrow) > 0) {
				abilities.add(SentinelSpecialist.AbilityNames.PenetrationArrow);
			}
			
			// Get a random index
			Dice d = new Dice(abilities.size());
			int index = d.roll() - 1;
			
			// Tell which Ability was chosen then execute the use(3) version
			System.out.println("Arrow Selected: " + abilities.get(index).toString() + "!");
			this.owner.useAbility(abilities.get(index), 3);
		};
		
		String display = "RANDOM ABILITY (Multi-Purposed)";
		return new CustomCommand(this.owner, display, useRandomAbility);
	}
	
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tNumber of Required Unique Abilities: " + this.getUniqueRequirement();
		ret += "\n\t" + this.getAbilityDamageBonus();
		ret += this.rank() >= 10? ("\n\tBonus Crit Per Turn of Cooldown: " + this.critAccBonusPerCooldown) : "";
		ret += this.rank() >= 10? ("\n\tBonus Accuracy Per Turn of Cooldown: " + this.critAccBonusPerCooldown) : "";
		return ret;
	}
}


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
	
	// For the burn effect, activating executes the attack
	@Override
	public void executeDOT() {
		// If the host is dead, just do nothing
		if (this.burnAttack.getDefender().isDead()) {
			return;
		}
		
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
		return "On Fire: Deals ~" + dmgEstimate + " damage at the beginning of each turn.";
	}
}

// The class Flaming Arrow itself
class FlamingArrow extends ChargedAbility {
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
	public void useEmpowered(double scalerPortion, boolean usesTurnActions) {
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
		
		// This Ability uses the Character's turn actions when specified
		if (usesTurnActions) {
			this.owner.useTurnActions();
		}
	}
	public void useEmpowered() {
		this.useEmpowered(1.0, true);
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
		
		// This Ability uses the Character's turn actions 
		this.owner.useTurnActions();
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

// Ability 2: "Frozen Arrow"
class FrozenArrow extends ChargedAbility {
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
	public void useEmpowered(double scalerPortion, boolean usesTurnActions) {
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
		
		// This Ability uses the Character's turn actions when specified
		if (usesTurnActions) {
			this.owner.useTurnActions();
		}
	}
	public void useEmpowered() {
		this.useEmpowered(1.0, true);
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
		
		// This Ability uses the Character's turn actions 
		this.owner.useTurnActions();
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

// Ability 3: "Exploding Arrow"
class ExplodingArrow extends ChargedAbility {
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
 	
 	int numHit = primaryAOE.size() + aoe50.size();
		
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
		
		// At rank 10, if we hit at least 7 enemies, randomly Empower
		if (this.rank() == 10 && numHit >= 7) {
			this.owner.randomlyEmpower();
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use(2): Empowered version of Ability
	public void useEmpowered(double scalerPortion, boolean usesTurnActions) {
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
     
     int numHit = primaryAOE.size() + aoe50.size();
     
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
		
		// At rank 10, if we hit at least 7 enemies, randomly Empower
		if (this.rank() == 10 && numHit >= 7) {
			this.owner.randomlyEmpower();
		}
		
		// This Ability uses the Character's turn actions when specified
		if (usesTurnActions) {
			this.owner.useTurnActions();
		}
	}
	public void useEmpowered() {
		this.useEmpowered(1.0, true);
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
 	
 	int numHit = primaryAOE.size() + aoe50.size();
 	
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
		
		// At rank 10, if we hit at least 7 enemies, randomly Empower
		if (this.rank() == 10 && numHit >= 7) {
			this.owner.randomlyEmpower();
		}
		
		// This Ability uses the Character's turn actions 
		this.owner.useTurnActions();
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

// Ability 4: "Penetration Arrow"
class PenetrationArrow extends ChargedAbility {
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
	public void useEmpowered(double scalerPortion, boolean usesTurnActions) {
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
		
		// This Ability uses the Character's turn actions when specified
		if (usesTurnActions) {
			this.owner.useTurnActions();
		}
	}
	public void useEmpowered() {
		this.useEmpowered(1.0, true);
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
		
		// This Ability uses the Character's turn actions 
		this.owner.useTurnActions();
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

// ULTIMATE Ability: "Black Arrow"
class BlackArrow extends UltimateAbility {
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


// Hidden Ability: "Restoration Arrow"
// Makes use of a heal over time defined here (extends DOT, likely should just be named OT)
class SentinelHealOT extends DamageOverTime {
	// Variables needed to complete the restoration (slightly condensed)
	private SentinelSpecialist source;
	private Character affected;
	private int duration;
	private double scaler1, scaler2, scaler3, missHpBonus;
	private boolean isCrit, canRandEmp;
	private int curTurn;
	
	// Constructors
	public SentinelHealOT(SentinelSpecialist source, Character affected, int duration, double scaler1, double scaler2, double scaler3, double missHpBonus, boolean isCrit, boolean canRandEmp) {
		super(source, "Restoration Arrow: Heal Over Time Effect", duration);
		this.source = source;
		this.affected = affected;
		this.duration = duration;
		this.scaler1 = scaler1;
		this.scaler2 = scaler2;
		this.scaler3 = scaler3;
		this.missHpBonus = missHpBonus;
		this.isCrit = isCrit;
		this.canRandEmp = canRandEmp;
		this.curTurn = 0;
	}
	public SentinelHealOT(SentinelHealOT copy) {
		super(copy);
		this.source = copy.source;
		this.affected = copy.affected;
		this.duration = copy.duration;
		this.scaler1 = copy.scaler1;
		this.scaler2 = copy.scaler2;
		this.scaler3 = copy.scaler3;
		this.missHpBonus = copy.missHpBonus;
		this.isCrit = copy.isCrit;
		this.canRandEmp = copy.canRandEmp;
		this.curTurn = 0;
	}
	
	// For the heal over time effect, restore the amount based on the current turn
	@Override
	public void executeDOT() {
		// If the affected target is dead, do nothing
		if (this.affected.isDead()) {
			return;
		}
		
		// Increment to know if we are on turn 1, 2, or 3
		this.curTurn++;
		
		// If the current turn is greater than the duration, do nothing
		if (this.curTurn > this.duration) {
			return;
		}
		
		// Figure out which scaler to use based on the current turn
		double scaler = 0;
		switch(this.curTurn) {
			case 1:
				scaler = this.scaler1;
				break;
			case 2:
				scaler = this.scaler2;
				break;
			case 3:
				scaler = this.scaler3;
				break;
		}
		
		// Get the amount that the Character will be healed based on the found scaler
		double amount = this.source.getDamage() * scaler;
		if (this.isCrit) {
			amount *= 2;
		}
		int healAmt = (int)Math.round(amount);
		int missingHealth = this.affected.getMissingHealth();
		
		// Heal the Character for the specified amount
		this.affected.restoreHealth(healAmt);
		
		// Check if this initial heal did heal the Character over their maximum, and randomly empower if appropriate
		if (healAmt > missingHealth && this.canRandEmp) {
			this.source.randomlyEmpower();
			this.canRandEmp = false;
		}
		
		// If we're on the third heal and we did not fully restore the character (and the missHpBonus > 0), apply the missing health bonus heal
		if (this.curTurn == 3 && this.affected.getMissingHealth() != 0 && this.missHpBonus > 0) {
			this.affected.restoreHealth((int)Math.round(this.affected.getMissingHealth() * this.missHpBonus));
		}
	}
	
	// Returns the display line (without the tabs) of the Bleed DOT effect in the Condition
	@Override
	public String getDOTString() {
		// Do the same calculations as above to get a good estimate of the amount to be healed
		// Figure out which scaler to use based on the current turn
		double scaler = 0;
		switch(this.curTurn + 1) {
			case 1:
				scaler = this.scaler1;
				break;
			case 2:
				scaler = this.scaler2;
				break;
			case 3:
				scaler = this.scaler3;
				break;
		}
		
		// Get the amount that the Character will be healed based on the found scaler
		double amount = this.source.getDamage() * scaler;
		if (this.isCrit) {
			amount *= 2;
		}
		int estimate = (int)Math.round(amount);
		
		// Return the String with the estimate
		return "Healing Over Time: Next heals ~" + estimate + " healing at the beginning of the turn.";
	}
}


// The class Restoration Arrow itself
class RestorationArrow extends ChargedAbility {
	// Holds the owner of the Ability as a Sentinel Specialist
	private SentinelSpecialist owner;
	
	// Additional variables
	private double baseScaler;
	private double firstTurnScaler;
	private double secondTurnScaler;
	private double thirdTurnScaler;
	private double missingHealthScaler;
	private Condition selfCritBonus;
	private int numStacks;
	
	// Constructor
	public RestorationArrow(SentinelSpecialist source, int rank, int charges) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Flaming Arrow\"", source, rank, charges);
		this.owner = source;
		
		// Sets the Cooldown and scalers of the Ability
		this.setCooldown();
		this.setScalers();
		
		// Initialize additional variables
		this.numStacks = 0;
		
		// Sets the self crit bonus the Ability
		this.setCritBonus();
	}
	
	// Sets the Cooldown of the Ability
	private void setCooldown() {
		// The Cooldown is always 7 until rank 5 where it is reduced to 6
		this.cooldown = 7;
		if (this.rank() >= 5) {
			this.cooldown = 6;
		}
	}
	
	// Sets the base scaler, the missing Health scaler, and each heal over time scaler of the Ability
	private void setScalers() {
		// Initialize each scaler to 0
		this.baseScaler = 0;
		this.firstTurnScaler = 0;
		this.secondTurnScaler = 0;
		this.thirdTurnScaler = 0;
		this.missingHealthScaler = 0;
		
		// Set the scalers based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.baseScaler = .2;
				this.firstTurnScaler = .4;
				this.secondTurnScaler = .6;
				break;
			case 2:
				this.baseScaler = .25;
				this.firstTurnScaler = .45;
				this.secondTurnScaler = .65;
				break;
			case 3:
				this.baseScaler = .3;
				this.firstTurnScaler = .45;
				this.secondTurnScaler = .65;
				break;
			case 4:
				this.baseScaler = .35;
				this.firstTurnScaler = .5;
				this.secondTurnScaler = .7;
				break;
			case 5:
				this.baseScaler = .4;
				this.firstTurnScaler = .5;
				this.secondTurnScaler = .7;
				break;
			case 6:
				this.baseScaler = .45;
				this.firstTurnScaler = .55;
				this.secondTurnScaler = .75;
				break;
			case 7:
				this.baseScaler = .45;
				this.firstTurnScaler = .6;
				this.secondTurnScaler = .7;
				this.thirdTurnScaler = .8;
				break;
			case 8:
				this.baseScaler = .5;
				this.firstTurnScaler = .65;
				this.secondTurnScaler = .75;
				this.thirdTurnScaler = .85;
				break;
			case 9:
				this.baseScaler = .55;
				this.firstTurnScaler = .7;
				this.secondTurnScaler = .8;
				this.thirdTurnScaler = .9;
				break;
			case 10:
				this.baseScaler = .55;
				this.firstTurnScaler = .75;
				this.secondTurnScaler = .85;
				this.thirdTurnScaler = .95;
				this.missingHealthScaler = .25;
				break;
			case 11:
				this.baseScaler = .6;
				this.firstTurnScaler = .8;
				this.secondTurnScaler = .9;
				this.thirdTurnScaler = 1.0;
				this.missingHealthScaler = .25;
				break;
			case 12:
				this.baseScaler = .6;
				this.firstTurnScaler = .85;
				this.secondTurnScaler = .95;
				this.thirdTurnScaler = 1.05;
				this.missingHealthScaler = .35;
				break;
			case 13:
				this.baseScaler = .65;
				this.firstTurnScaler = .9;
				this.secondTurnScaler = 1.0;
				this.thirdTurnScaler = 1.1;
				this.missingHealthScaler = .35;
				break;
			case 14:
				this.baseScaler = .7;
				this.firstTurnScaler = .95;
				this.secondTurnScaler = 1.05;
				this.thirdTurnScaler = 1.1;
				this.missingHealthScaler = .35;
				break;
			case 15:
				this.baseScaler = .75;
				this.firstTurnScaler = 1.0;
				this.secondTurnScaler = 1.1;
				this.thirdTurnScaler = 1.25;
				this.missingHealthScaler = .5;
				break;
		}
		
		// Set the default scaler to be the base scaler
		this.scaler = this.baseScaler;
	}
	
	// Sets the self crit bonus pre attack Condition
	private void setCritBonus() {
		// Initialize the amount to 0
		int amount = 0;
		
		// Set the amount based on the rank of the Ability
		switch(this.rank()) {
			case 7:
				amount = 10;
				break;
			case 8:
				amount = 12;
				break;
			case 9:
				amount = 14;
				break;
			case 10:
				amount = 20;
				break;
			case 11:
				amount = 22;
				break;
			case 12:
				amount = 27;
				break;
			case 13:
				amount = 30;
				break;
			case 14:
				amount = 33;
				break;
			case 15:
				amount = 40;
				break;
		}
		
		// Creates the Status Effect for the Condition
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, amount);
		critBonus.makeFlat();
		
		// Creates the Condition with a duration of 0 since it is only for that heal usage
		this.selfCritBonus = new Condition("Restoration Arrow: Crit Bonus", 0);
		this.selfCritBonus.setSource(this.owner);
		this.selfCritBonus.addStatusEffect(critBonus);
	}
	
	
	// Get methods for additional effects
	@Override
	public SentinelSpecialist getOwner() {
		return this.owner;
	}
	
	public double getBaseScaler() {
		return this.baseScaler;
	}
	
	public double getFirstTurnScaler() {
		return this.firstTurnScaler;
	}
	
	public double getSecondTurnScaler() {
		return this.secondTurnScaler;
	}
	
	public double getThirdTurnScaler() {
		return this.thirdTurnScaler;
	}
	
	public double getMissingHealthScaler() {
		return this.missingHealthScaler;
	}
	
	public Condition getSelfCritBonus() {
		return new Condition(this.selfCritBonus);
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
		
		// Get the target of the heal
		Character affected = BattleSimulator.getInstance().targetSingle();
	    if (affected.equals(Character.EMPTY)) {
	    	return;
	    }
	    
	    // If the ability can crit (at least rank 3), see if it will crit
	    boolean isCrit = false;
	    if (this.rank() >= 3) {
	    	Attack fake = new AttackBuilder().attacker(this.owner).defender(affected).isTargeted().build();
	    	if (fake.calcCrit()) {
	    		isCrit = true;
	    	}
	    }
	    
	    // Get the base heal amount, doubled if we will crit (also notify if we crit, as restore does not notify)
	    double amount = this.getBaseScaler() * this.owner.getDamage();
	    if (isCrit) {
	    	amount *= 2;
	    	System.out.println("Restoration Arrow CRITICALLY Healed!");
	    }
	    int healAmt = (int)Math.round(amount);
	    int missingHealth = affected.getMissingHealth();
	    
	    // Heal the Character for the specified amount
		affected.restoreHealth(healAmt);
		
		// Check if this initial heal did heal the Character over their maximum, and randomly empower if appropriate
		boolean canRandEmp = this.rank() == 15;
		if (healAmt > missingHealth && canRandEmp) {
			this.owner.randomlyEmpower();
			canRandEmp = false;
		}
		
		// Create the heal over time effect and add it to the affected Character (can only crit if we've crit so far and above rank 5)
		isCrit = isCrit && this.rank() >= 5;
		int duration = this.rank() >= 7? 3 : 2;
		SentinelHealOT healOT = new SentinelHealOT(this.owner, affected, duration, this.getFirstTurnScaler(), this.getSecondTurnScaler(), this.getThirdTurnScaler(), this.getMissingHealthScaler(), isCrit, canRandEmp);
		affected.addCondition(healOT);
		
		// Add basic "Restoration Arrow" to the set of unique abilities used
		this.owner.addToUniqueSet("Restoration Arrow");
		
		// This Ability should never miss, add a stack
		this.incrementStacks();
		
		// Put Restoration Arrow "on Cooldown"
		this.setOnCooldown();
		
		// If we are EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Use(2): Empowered version of Ability
	public void useEmpowered(double scalerPortion, boolean usesTurnActions) {
		// Get the target of the heal
		Character affected = BattleSimulator.getInstance().targetSingle();
	    if (affected.equals(Character.EMPTY)) {
	    	return;
	    }
	    
	    // If the ability can crit (at least rank 3), see if it will crit
	    boolean isCrit = false;
	    if (this.rank() >= 3) {
	    	Attack fake = new AttackBuilder().attacker(this.owner).defender(affected).isTargeted().build();
	    	if (fake.calcCrit()) {
	    		isCrit = true;
	    	}
	    }
	    
	    // Apply the Empowered Pre Attack Bonus Condition before calculating heal amounts
	    this.owner.apply(this.owner.getEmpoweredPreAttackBonus());
	    
	    // Get the base heal amount, doubled if we will crit (also notify if we crit, as restore does not notify)
	    double amount = (this.getBaseScaler() + this.getFirstTurnScaler() + this.getSecondTurnScaler() + this.getThirdTurnScaler()) * this.owner.getDamage() * scalerPortion;
	    if (isCrit) {
	    	amount *= 2;
	    	System.out.println("Restoration Arrow CRITICALLY Healed!");
	    }
	    int healAmt = (int)Math.round(amount);
	    int missingHealth = affected.getMissingHealth();
	    
	    // Heal the Character for the specified amount
		affected.restoreHealth(healAmt);
	    
	    // Unapply the Empowered Pre Attack Bonus Condition
	    this.owner.unapply(this.owner.getEmpoweredPreAttackBonus());
	    
	    // Add "Restoration Arrow" or "EMPOWERED Restoration Arrow" to the set of unique abilities used based on rank of Masterwork Arrows
	    if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows) == 15) {
	    	this.owner.addToUniqueSet("EMPOWERED Restoration Arrow");
	    }
	    else {
	    	this.owner.addToUniqueSet("Restoration Arrow");
	    }
	    
	    // Reset the stacks, then increment the stacks if Empowered Arrows is at least rank 4
		this.resetStacks();
		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) >= 4) {
			this.incrementStacks();
		}
		
		// Put Restoration Arrow "on Cooldown" if Empowered Arrows is less than rank 2
		if (this.owner.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows) < 2) {
			this.setOnCooldown();
		}
		
		// If we are somehow EMPOWERED after adding stacks, refresh all Cooldowns for this ability
		if (this.isEmpowered()) {
			this.setOffCooldownAll();
		}
		
		// Check if this initial heal did heal the Character over their maximum, and randomly empower if appropriate
		if (healAmt > missingHealth && this.rank() == 15) {
			this.owner.randomlyEmpower();
		}
		
		// This Ability uses the Character's turn actions when specified
		if (usesTurnActions) {
			this.owner.useTurnActions();
		}
	}
	public void useEmpowered() {
		this.useEmpowered(1.0, true);
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
		ret += "\n\tFirst Turn Scaler: " + this.getFirstTurnScaler();
		ret += "\n\tSecond Turn Scaler: " + this.getSecondTurnScaler();
		ret += this.rank() >= 7? ("\n\tThird Turn Scaler: " + this.getThirdTurnScaler()) : "";
		ret += this.rank() >= 10? ("\n\tMissing Health Scaler: " + this.getMissingHealthScaler()) : "";
		ret += this.rank() >= 7? ("\n\t" + this.getSelfCritBonus()) : "";
		return ret;
	}
}


// The Sentinel Specialist itself:
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
	
	// These first two methods help set up the Sentinel Specialist subclass.
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
		
		// Initialize the Unique Abilities set, set baIsAltered to false
		this.uniqueAbilities = new HashSet<>();
		this.baIsAltered = false;
		
		// Add the passive command for Empowered Arrows only if it is Above rank 3
		if (this.EmpoweredArrows.rank() >= 3) {
			this.addCommand(new AbilityCommand(this.EmpoweredArrows));
		}
		
		// Add all other new commands for Abilities (the usual check for rank > 0 is sufficient)
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
	
	// Functions to use the EMPOWERED version of an Ability
	public void useAbilityEmpowered(SentinelSpecialist.AbilityNames name, double scalerPortion, boolean usesTurnActions) {
		// Check the 5 possible Abilities (cannot use ULTIMATE from this function), if the name was none that were specified, do nothing
		if (name.equals(SentinelSpecialist.AbilityNames.FlamingArrow)) {
			this.FlamingArrow.useEmpowered(scalerPortion, usesTurnActions);
		}
		else if (name.equals(SentinelSpecialist.AbilityNames.FrozenArrow)) {
			this.FrozenArrow.useEmpowered(scalerPortion, usesTurnActions);
		}
		else if (name.equals(SentinelSpecialist.AbilityNames.ExplodingArrow)) {
			this.ExplodingArrow.useEmpowered(scalerPortion, usesTurnActions);
		}
		else if (name.equals(SentinelSpecialist.AbilityNames.PenetrationArrow)) {
			this.PenetrationArrow.useEmpowered(scalerPortion, usesTurnActions);
		}
		else if (name.equals(SentinelSpecialist.AbilityNames.RestorationArrow)) {
			this.RestorationArrow.useEmpowered(scalerPortion, usesTurnActions);
		}
	}
	public void useAbilityEmpowered(SentinelSpecialist.AbilityNames name) {
		this.useAbilityEmpowered(name, 1.0, true);
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
		// Adds class Conditions to a list.
		LinkedList<Condition> classConditions = new LinkedList<>();
		classConditions.add(this.EmpoweredArrows.getAbilityPreAttackBonus());
		classConditions.add(this.MultiPurposed.getAbilityDamageBonus());
		classConditions.add(this.MultiPurposed.getPermanentCondition(3));
		classConditions.add(this.FlamingArrow.getDamageAlteration());
		classConditions.add(this.FlamingArrow.getDamageAlterationEmpowered());
		classConditions.add(this.FrozenArrow.getDamageAlteration());
		classConditions.add(this.FrozenArrow.getDamageAlterationEmpowered());
		classConditions.add(this.ExplodingArrow.getDamageAlteration());
		classConditions.add(this.ExplodingArrow.getDamageAlterationEmpowered());
		classConditions.add(this.PenetrationArrow.getEnemyArmorReduction());
		classConditions.add(this.PenetrationArrow.getSTDeffects(false));
		classConditions.add(this.BlackArrow.getDragonDamageBonus());
		classConditions.add(this.BlackArrow.getEmpoweredSelfPreAttackBonus());
		classConditions.add(this.BlackArrow.getSelfPreAttackBonus());
		//classConditions.add(this.RestorationArrow.getSelfCritBonus());
		
		
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
	
	
	// Overrides the begin and end turn functions of Character to include reducing the Cooldowns of Abilities and other things.
	// Print Turn override to include Multi-Purposed set (unique abilities)
	@Override
	protected void printTurnStats() {
		// First just call the original
		super.printTurnStats();
		
		// Then, if the set is non-empty, print it like a list of items
		if (!this.uniqueAbilities.isEmpty()) {
			System.out.println("Unique Abilities: " + this.uniqueAbilities.toString());
		}
	}
	
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
			this.alterBasicAttack(this.MultiPurposed.getRandomAbilityCommand());
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

// Builds a Sentinel Specialist
class SentinelSpecialistBuilder extends CharacterBuilder {
	// Creates all the Ability fields
	private int EmpoweredArrowsRank;
	private int MasterworkArrowsRank;
	private int SurvivableRank;
	private int MultiPurposedRank;
	private int FlamingArrowRank;
	private int FrozenArrowRank;
	private int ExplodingArrowRank;
	private int PenetrationArrowRank;
	private int BlackArrowRank;
	private int RestorationArrowRank;
	
	// Constructs a SentinelSpecialistBuilder (populating the CharacterBuilder variables) based on the constant stats from Character
	public SentinelSpecialistBuilder(Character base) {
		super(base);
		this.EmpoweredArrowsRank = 0;
		this.MasterworkArrowsRank = 0;
		this.SurvivableRank = 0;
		this.MultiPurposedRank = 0;
		this.FlamingArrowRank = 0;
		this.FrozenArrowRank = 0;
		this.ExplodingArrowRank = 0;
		this.PenetrationArrowRank = 0;
		this.BlackArrowRank = 0;
		this.RestorationArrowRank = 0;
	}
	public SentinelSpecialistBuilder(SentinelSpecialist base) {
		super(base);
		this.EmpoweredArrowsRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.EmpoweredArrows);
		this.MasterworkArrowsRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.MasterworkArrows);
		this.SurvivableRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.Survivable);
		this.MultiPurposedRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.MultiPurposed);
		this.FlamingArrowRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.FlamingArrow);
		this.FrozenArrowRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.FrozenArrow);
		this.ExplodingArrowRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.ExplodingArrow);
		this.PenetrationArrowRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.PenetrationArrow);
		this.BlackArrowRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.BlackArrow);
		this.RestorationArrowRank = base.getAbilityRank(SentinelSpecialist.AbilityNames.RestorationArrow);
	}
	public SentinelSpecialistBuilder() {
		this(Character.SENTINEL_SPECIALIST);
	}
	
	// Overrides the functions necessary from CharacterBuilder when constant stats are given.
	@Override
	public SentinelSpecialistBuilder Name(String name) {
		super.Name(name);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder Level(int level) {
		super.Level(level);
		return this;
	}
	
	@Override
	public SentinelSpecialistBuilder bonusHealth(int bonusHealth) {
		super.bonusHealth(bonusHealth);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusDamage(int bonusDamage) {
		super.bonusDamage(bonusDamage);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusArmor(int bonusArmor) {
		super.bonusArmor(bonusArmor);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		super.bonusArmorPiercing(bonusArmorPiercing);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusAccuracy(int bonusAccuracy) {
		super.bonusAccuracy(bonusAccuracy);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusBlock(int bonusBlock) {
		super.bonusBlock(bonusBlock);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusCriticalChance(int bonusCriticalChance) {
		super.bonusCriticalChance(bonusCriticalChance);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusSpeed(int bonusSpeed) {
		super.bonusSpeed(bonusSpeed);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		super.bonusAttackSpeed(bonusAttackSpeed);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusThreat(int bonusThreat) {
		super.bonusThreat(bonusThreat);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		super.bonusTacticalThreat(bonusTacticalThreat);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusSTDdown(int bonusSTDdown) {
		super.bonusSTDdown(bonusSTDdown);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder bonusSTDup(int bonusSTDup) {
		super.bonusSTDup(bonusSTDup);
		return this;
	}
	
	@Override
	public SentinelSpecialistBuilder baseDmgType(Attack.DmgType dmgType) {
		super.baseDmgType(dmgType);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder addResistance(Attack.DmgType resistance, double value) {
		super.addResistance(resistance, value);
		return this;
	}
	@Override
	public SentinelSpecialistBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
		super.addVulnerability(vulnerability, value);
		return this;
	}
	
	@Override
	public SentinelSpecialistBuilder addType(Character.Type type) {
		super.addType(type);
		return this;
	}
	
	
	// Sets the ranks of each Ability (then defines the base Cooldown and Scaler based on that)
	// Empowered Arrows (Passive Ability)
	public SentinelSpecialistBuilder setEmpoweredArrowsRank(int newRank) {
		this.EmpoweredArrowsRank = newRank;
		return this;
	}
	// Masterwork Arrows (Passive Ability):
	public SentinelSpecialistBuilder setMasterworkArrowsRank(int newRank) {
		this.MasterworkArrowsRank = newRank;
		return this;
	}
	// Survivable (Passive Ability):
	public SentinelSpecialistBuilder setSurvivableRank(int newRank) {
		this.SurvivableRank = newRank;
		return this;
	}
	// Multi-Purposed (Passive Ability):
	public SentinelSpecialistBuilder setMultiPurposedRank(int newRank) {
		this.MultiPurposedRank = newRank;
		return this;
	}
	
	// Flaming Arrow (Ability 1):
	public SentinelSpecialistBuilder setFlamingArrowRank(int newRank) {
		this.FlamingArrowRank = newRank;
		return this;
	}
	// Frozen Arrow (Ability 2):
	public SentinelSpecialistBuilder setFrozenArrowRank(int newRank) {
		this.FrozenArrowRank = newRank;
		return this;
	}
	// Exploding Arrow (Ability 3):
	public SentinelSpecialistBuilder setExplodingArrowRank(int newRank) {
		this.ExplodingArrowRank = newRank;
		return this;
	}
	// Penetration Arrow (Ability 4):
	public SentinelSpecialistBuilder setPenetrationArrowRank(int newRank) {
		this.PenetrationArrowRank = newRank;
		return this;
	}
	// Black Arrow (ULTIMATE):
	public SentinelSpecialistBuilder setBlackArrowRank(int newRank) {
		this.BlackArrowRank = newRank;
		return this;
	}
	// Restoration Arrow (Hidden Ability):
	public SentinelSpecialistBuilder setRestorationArrowRank(int newRank) {
		this.RestorationArrowRank = newRank;
		return this;
	}
	
	
	// Calculates the base stats based on level and stat-increasing passive abilities
	private void setBaseStats() {
		// Each stat is already set to its level 1 base value
		// Note: below only occurs if they specified a level, since the base level is 0.
		// "Level Up" each stat: (Multiply by the given multiplier for each level up to the current level)
		for (int counter = 2; counter <= this.Level; counter++) {
			// All these only increment at intervals of 5
			if (counter % 5 == 0) {
				this.Armor = (int)Math.round(this.Armor * 1.05);
				this.ArmorPiercing = (int)Math.round(this.ArmorPiercing * 1.05);
				this.Accuracy = (int)Math.round(this.Accuracy * 1.05);
				this.Dodge = (int)Math.round(this.Dodge * 1.05);
			}
			
			// Dynamically increasing stats
			// Health and Damage have various changes at intervals of 5 and 10
			if (counter % 10 == 0) {
				this.Health = (int)Math.round(this.Health * 1.05);
				this.Damage = (int)Math.round(this.Damage * 1.05);
			}
			else if (counter % 5 == 0) {
				this.Health = (int)Math.round(this.Health * 1.04);
				this.Damage = (int)Math.round(this.Damage * 1.04);
			}
			else {
				this.Health = (int)Math.round(this.Health * 1.03);
				this.Damage = (int)Math.round(this.Damage * 1.03);
			}
			
			// Attack Speed increases with various amounts at the given levels
			if (counter == 20) {
				this.AttackSpeed += 1;
			}
			if (counter == 40) {
				this.AttackSpeed += 1;
			}
			if (counter == 60) {
				this.AttackSpeed += 2;
			}
			if (counter == 80) {
				this.AttackSpeed += 3;
			}
			if (counter == 100) {
				this.AttackSpeed += 4;
			}
		}
		
		// Calculate the bonus stats given by certain Abilities
		if (this.MasterworkArrowsRank > 0) {
			MasterworkArrows ma = new MasterworkArrows(this.MasterworkArrowsRank);
			this.bDamage += ma.getDamageBonus();
			this.bArmorPiercing += ma.getArmorPiercingBonus();
			this.bAccuracy += ma.getAccuracyBonus();
		}
		if (this.SurvivableRank > 0) {
			Survivable sb = new Survivable(this.SurvivableRank);
			this.bHealth += sb.getHealthBonus();
			this.bArmor += sb.getArmorBonus();
			this.bDodge += sb.getDodgeBonus();
		}
	}
	
	// Finishes the build by returning a SentinelSpecialist Character
	public SentinelSpecialist build() {
		// Set the base stats for the level and ability ranks
		this.setBaseStats();
		
		// Return the Sentinel Specialist
		return new SentinelSpecialist(super.build(), this.EmpoweredArrowsRank, this.MasterworkArrowsRank, this.SurvivableRank, this.MultiPurposedRank, this.FlamingArrowRank, this.FrozenArrowRank, this.ExplodingArrowRank, this.PenetrationArrowRank, this.BlackArrowRank, this.RestorationArrowRank);
	}
}
