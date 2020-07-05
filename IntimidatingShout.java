package WyattWitemeyer.WarOfGillysburg;

public class IntimidatingShout extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private Condition tauntNormal;
	private Condition tauntNormalReducedValue;
	private Condition tauntAdvanced;
	private Condition tauntAdvancedReducedValue;
	private Condition tauntElite;
	private Condition tauntEliteReducedValue;
	private Condition tauntBoss;
	private Condition tauntBossReducedValue;
	private Condition selfDefenseBonus;
	private Condition selfDefenseBonusReducedValue;
	
	// These are used when "Deflection" is activated
	private Condition tauntNormalExtraDuration;
	private Condition tauntAdvancedExtraDuration;
	private Condition tauntEliteExtraDuration;
	private Condition tauntBossExtraDuration;
	private Condition selfDefenseBonusExtraDuration;
	
	// Constructor
	public IntimidatingShout(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Intimidating Shout\"", source, rank);
		this.owner = source;
		
		// Set the Cooldown of the Ability (this Ability has no scaler)
		this.setCooldown();
		
		// Calculate the additional Conditions of the ability
		this.setTauntConditions();
		this.setSelfDefenseBonuses();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 5, reduced to 4 at rank 5
		this.cooldown = 5;
		if (this.rank() >= 5) {
			this.cooldown = 4;
		}
		// The Ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Calculates the amount of damage reduction for Normal enemies (other enemies are multiples of this number)
	private double normalDmgReduction() {
		// Set a default value for the first rank
		double amount = 30;
		
		// Set the amount based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				amount = 30;
				break;
			case 2:
				amount = 33;
				break;
			case 3:
				amount = 36;
				break;
			case 4:
				amount = 39;
				break;
			case 5:
				amount = 45;
				break;
			case 6:
				amount = 50;
				break;
			case 7:
				amount = 50;
				break;
			case 8:
				amount = 60;
				break;
			case 9:
				amount = 75;
				break;
			case 10:
				amount = 100;
				break;
		}
		
		// Return the amount
		return amount;
	}
	
	// Calculate and create each Condition
	private void setTauntConditions() {
		// Set the amount for each version based on the rank
		double normalBaseAmount = this.normalDmgReduction();
		double normalReducedAmount = normalBaseAmount/2;
		
		double advancedBaseAmount = normalBaseAmount/2;
		double advancedReducedAmount = advancedBaseAmount/2;
		
		double eliteBaseAmount = normalBaseAmount/3;
		double eliteReducedAmount = eliteBaseAmount/2;
		
		// Boss amount is only set to non-zero if rank 7+
		double bossBaseAmount = 0;
		double bossReducedAmount = 0;
		if (this.rank() >= 7) {
			bossBaseAmount = normalBaseAmount/4;
			bossReducedAmount = bossBaseAmount/2;
		}
		
		
		// Default duration of 1, increased to 2 at rank 10.
		int duration = 1;
		if (this.rank() == 10) {
			duration = 2;
		}
		
		// Create the requirement that the reduced damage only occurs on the owner
		DualRequirement isOwner = (Character withEffect, Character other) -> {
			return other.equals(this.owner);
		};
		
		// Create the Status Effects for each version
		// Normal
		StatusEffect normalBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -normalBaseAmount, StatusEffect.Type.OUTGOING);
		normalBaseDmgReduction.makePercentage();
		normalBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect normalReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -normalReducedAmount, StatusEffect.Type.OUTGOING);
		normalReducedDmgReduction.makePercentage();
		normalReducedDmgReduction.setDualRequirement(isOwner);
		
		// Advanced
		StatusEffect advancedBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -advancedBaseAmount, StatusEffect.Type.OUTGOING);
		advancedBaseDmgReduction.makePercentage();
		advancedBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect advancedReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -advancedReducedAmount, StatusEffect.Type.OUTGOING);
		advancedReducedDmgReduction.makePercentage();
		advancedReducedDmgReduction.setDualRequirement(isOwner);
		
		// Elite
		StatusEffect eliteBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -eliteBaseAmount, StatusEffect.Type.OUTGOING);
		eliteBaseDmgReduction.makePercentage();
		eliteBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect eliteReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -eliteReducedAmount, StatusEffect.Type.OUTGOING);
		eliteReducedDmgReduction.makePercentage();
		eliteReducedDmgReduction.setDualRequirement(isOwner);
		
		// Boss
		StatusEffect bossBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -bossBaseAmount, StatusEffect.Type.OUTGOING);
		bossBaseDmgReduction.makePercentage();
		bossBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect bossReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -bossReducedAmount, StatusEffect.Type.OUTGOING);
		bossReducedDmgReduction.makePercentage();
		bossReducedDmgReduction.setDualRequirement(isOwner);
		
		// Create the base and extra duration Conditions for each version
		// Normal
		this.tauntNormal = new Condition("Intimidating Shout: TAUNT - Damage Reduced", duration);
		this.tauntNormal.setSource(this.owner);
		this.tauntNormal.makeSourceIncrementing();
		this.tauntNormal.makeEndOfTurn();
		this.tauntNormal.addStatusEffect(normalBaseDmgReduction);
		
		this.tauntNormalExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", duration + 1);
		this.tauntNormalExtraDuration.setSource(this.owner);
		this.tauntNormalExtraDuration.makeSourceIncrementing();
		this.tauntNormalExtraDuration.makeEndOfTurn();
		this.tauntNormalExtraDuration.addStatusEffect(normalBaseDmgReduction);
		
		// Advanced
		this.tauntAdvanced = new Condition("Intimidating Shout: TAUNT - Damage Reduced", duration);
		this.tauntAdvanced.setSource(this.owner);
		this.tauntAdvanced.makeSourceIncrementing();
		this.tauntAdvanced.makeEndOfTurn();
		this.tauntAdvanced.addStatusEffect(advancedBaseDmgReduction);
		
		this.tauntAdvancedExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", duration + 1);
		this.tauntAdvancedExtraDuration.setSource(this.owner);
		this.tauntAdvancedExtraDuration.makeSourceIncrementing();
		this.tauntAdvancedExtraDuration.makeEndOfTurn();
		this.tauntAdvancedExtraDuration.addStatusEffect(advancedBaseDmgReduction);
		
		// Elite
		this.tauntElite = new Condition("Intimidating Shout: TAUNT - Damage Reduced", duration);
		this.tauntElite.setSource(this.owner);
		this.tauntElite.makeSourceIncrementing();
		this.tauntElite.makeEndOfTurn();
		this.tauntElite.addStatusEffect(eliteBaseDmgReduction);
		
		this.tauntEliteExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", duration + 1);
		this.tauntEliteExtraDuration.setSource(this.owner);
		this.tauntEliteExtraDuration.makeSourceIncrementing();
		this.tauntEliteExtraDuration.makeEndOfTurn();
		this.tauntEliteExtraDuration.addStatusEffect(eliteBaseDmgReduction);
		
		// Boss
		this.tauntBoss = new Condition("Intimidating Shout: TAUNT - Damage Reduced", duration);
		this.tauntBoss.setSource(this.owner);
		this.tauntBoss.makeSourceIncrementing();
		this.tauntBoss.makeEndOfTurn();
		this.tauntBoss.addStatusEffect(bossBaseDmgReduction);
		
		this.tauntBossExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", duration + 1);
		this.tauntBossExtraDuration.setSource(this.owner);
		this.tauntBossExtraDuration.makeSourceIncrementing();
		this.tauntBossExtraDuration.makeEndOfTurn();
		this.tauntBossExtraDuration.addStatusEffect(bossBaseDmgReduction);
		
		// Create the requirement that the reduced Condition must meet to activate (the base Condition (any of them, including the deflection versions) cannot be present)
		Requirement actReq = (Character withEffect) -> {
			return !(withEffect.getAllConditions().contains(this.tauntNormal) || 
					 withEffect.getAllConditions().contains(this.tauntNormalExtraDuration) ||
					 withEffect.getAllConditions().contains(this.tauntAdvanced) ||
					 withEffect.getAllConditions().contains(this.tauntAdvancedExtraDuration) ||
					 withEffect.getAllConditions().contains(this.tauntElite) ||
					 withEffect.getAllConditions().contains(this.tauntEliteExtraDuration) ||
					 withEffect.getAllConditions().contains(this.tauntBoss) ||
					 withEffect.getAllConditions().contains(this.tauntBossExtraDuration));
		};
		
		// Create the reduced effect Condition that always has a duration of 1 for each enemy version
		// Normal
		this.tauntNormalReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntNormalReducedValue.setSource(this.owner);
		this.tauntNormalReducedValue.makeSourceIncrementing();
		this.tauntNormalReducedValue.makeEndOfTurn();
		this.tauntNormalReducedValue.addStatusEffect(normalReducedDmgReduction);
		
		// Advanced
		this.tauntAdvancedReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntAdvancedReducedValue.setSource(this.owner);
		this.tauntAdvancedReducedValue.makeSourceIncrementing();
		this.tauntAdvancedReducedValue.makeEndOfTurn();
		this.tauntAdvancedReducedValue.addStatusEffect(advancedReducedDmgReduction);
		
		// Elite
		this.tauntEliteReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntEliteReducedValue.setSource(this.owner);
		this.tauntEliteReducedValue.makeSourceIncrementing();
		this.tauntEliteReducedValue.makeEndOfTurn();
		this.tauntEliteReducedValue.addStatusEffect(eliteReducedDmgReduction);
		
		// Boss
		this.tauntBossReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntBossReducedValue.setSource(this.owner);
		this.tauntBossReducedValue.makeSourceIncrementing();
		this.tauntBossReducedValue.makeEndOfTurn();
		this.tauntBossReducedValue.addStatusEffect(bossReducedDmgReduction);
	}
	
	private void setSelfDefenseBonuses() {
		// Declare starting amounts (both are the same, so can use one variable) at rank 1
		double amount = 25;
		
		// Default duration of 1, increased to 2 at rank 10.
		int duration = 1;
		if (this.rank() == 10) {
			duration = 2;
		}
		
		// Calculate the amounts based on rank
		switch(this.rank()) {
			case 1:
				amount = 25;
				break;
			case 2:
				amount = 27;
				break;
			case 3:
				amount = 28;
				break;
			case 4:
				amount = 30;
				break;
			case 5:
				amount = 30;
				break;
			case 6:
				amount = 33;
				break;
			case 7:
				amount = 33;
				break;
			case 8:
				amount = 36;
				break;
			case 9:
				amount = 40;
				break;
			case 10:
				amount = 50;
				break;
		}
		
		// Create the Status Effects
		StatusEffect blkBonus = new StatusEffect(Stat.Version.BLOCK, amount, StatusEffect.Type.INCOMING);
		blkBonus.makePercentage();
		
		StatusEffect reducedBlkBonus = new StatusEffect(Stat.Version.BLOCK, amount/2, StatusEffect.Type.INCOMING);
		reducedBlkBonus.makePercentage();
		
		StatusEffect armorBonus = new StatusEffect(Stat.Version.ARMOR, amount, StatusEffect.Type.INCOMING);
		armorBonus.makePercentage();
		
		StatusEffect reducedArmorBonus = new StatusEffect(Stat.Version.ARMOR, amount/2, StatusEffect.Type.INCOMING);
		reducedArmorBonus.makePercentage();
		
		// Create the base Conditions (only difference is duration)
		this.selfDefenseBonus = new Condition("Intimidating Shout: Defense Bonus", duration);
		this.selfDefenseBonus.setSource(this.owner);
		this.selfDefenseBonus.makeEndOfTurn();
		this.selfDefenseBonus.addStatusEffect(blkBonus);
		this.selfDefenseBonus.addStatusEffect(armorBonus);
		
		this.selfDefenseBonusExtraDuration = new Condition("Intimidating Shout (Deflection): Defense Bonus", duration + 1);
		this.selfDefenseBonusExtraDuration.setSource(this.owner);
		this.selfDefenseBonusExtraDuration.makeEndOfTurn();
		this.selfDefenseBonusExtraDuration.addStatusEffect(blkBonus);
		this.selfDefenseBonusExtraDuration.addStatusEffect(armorBonus);
		
		// Create the activation requirement for the reduced Condition (neither base Condition can be present)
		Requirement actReq = (Character withEffect) -> {
			return !(withEffect.getAllConditions().contains(this.selfDefenseBonus) ||
					 withEffect.getAllConditions().contains(this.selfDefenseBonusExtraDuration));
		};
		
		// Create the reduced effect Condition that always has a duration of 1
		this.selfDefenseBonusReducedValue = new Condition("Intimidating Shout: Defense Bonus", 1, actReq);
		this.selfDefenseBonusReducedValue.setSource(this.owner);
		this.selfDefenseBonusReducedValue.makeEndOfTurn();
		this.selfDefenseBonusReducedValue.addStatusEffect(reducedBlkBonus);
		this.selfDefenseBonusReducedValue.addStatusEffect(reducedArmorBonus);
	}
	
	// Get methods for each Condition
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public Condition getTauntNormal() {
		return this.tauntNormal;
	}
	public Condition getTauntNormalReducedValue() {
		return this.tauntNormalReducedValue;
	}
	public Condition getTauntNormalDeflection() {
		return this.tauntNormalExtraDuration;
	}
	
	public Condition getTauntAdvanced() {
		return this.tauntAdvanced;
	}
	public Condition getTauntAdvancedReducedValue() {
		return this.tauntAdvancedReducedValue;
	}
	public Condition getTauntAdvancedDeflection() {
		return this.tauntAdvancedExtraDuration;
	}
	
	public Condition getTauntElite() {
		return this.tauntElite;
	}
	public Condition getTauntEliteReducedValue() {
		return this.tauntEliteReducedValue;
	}
	public Condition getTauntEliteDeflection() {
		return this.tauntEliteExtraDuration;
	}
	
	public Condition getTauntBoss() {
		return this.tauntBoss;
	}
	public Condition getTauntBossReducedValue() {
		return this.tauntBossReducedValue;
	}
	public Condition getTauntBossDeflection() {
		return this.tauntBossExtraDuration;
	}
	
	public Condition getSelfDefenseBonus() {
		return this.selfDefenseBonus;
	}
	public Condition getSelfDefenseBonusDeflection() {
		return this.selfDefenseBonusExtraDuration;
	}
}
