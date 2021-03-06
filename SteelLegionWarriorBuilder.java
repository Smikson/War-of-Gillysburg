package WyattWitemeyer.WarOfGillysburg;

public class SteelLegionWarriorBuilder extends CharacterBuilder {
	// Creates all the Ability fields
	private int VengeanceStrikeRank;
	private int SwordplayProwessRank;
	private int WarriorsMightRank;
	private int AgileFighterRank;
	private int SweepRank;
	private int ChargeRank;
	private int FlipStrikeRank;
	private int IntimidatingShoutRank;
	private int DeflectionRank;
	
	// Constructs a SteelLegionTankBuilder (populating the CharacterBuilder variables) based on the constant stats from Character
	public SteelLegionWarriorBuilder(Character base) {
		super(base);
		this.VengeanceStrikeRank = 0;
		this.SwordplayProwessRank = 0;
		this.WarriorsMightRank = 0;
		this.AgileFighterRank = 0;
		this.SweepRank = 0;
		this.ChargeRank = 0;
		this.FlipStrikeRank = 0;
		this.IntimidatingShoutRank = 0;
		this.DeflectionRank = 0;
	}
	public SteelLegionWarriorBuilder(SteelLegionWarrior base) {
		super(base);
		this.VengeanceStrikeRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.VengeanceStrike);
		this.SwordplayProwessRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.SwordplayProwess);
		this.WarriorsMightRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.WarriorsMight);
		this.AgileFighterRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.AgileFighter);
		this.SweepRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.Sweep);
		this.ChargeRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.Charge);
		this.FlipStrikeRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.FlipStrike);
		this.IntimidatingShoutRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.IntimidatingShout);
		this.DeflectionRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.Deflection);
	}
	public SteelLegionWarriorBuilder() {
		this(Character.STEEL_LEGION_WARRIOR);
	}
	
	// Overrides the functions necessary from CharacterBuilder when constant stats are given.
	@Override
	public SteelLegionWarriorBuilder Name(String name) {
		super.Name(name);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder Level(int level) {
		super.Level(level);
		return this;
	}
	
	@Override
	public SteelLegionWarriorBuilder bonusHealth(int bonusHealth) {
		super.bonusHealth(bonusHealth);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusDamage(int bonusDamage) {
		super.bonusDamage(bonusDamage);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusArmor(int bonusArmor) {
		super.bonusArmor(bonusArmor);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		super.bonusArmorPiercing(bonusArmorPiercing);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusAccuracy(int bonusAccuracy) {
		super.bonusAccuracy(bonusAccuracy);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusBlock(int bonusBlock) {
		super.bonusBlock(bonusBlock);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusCriticalChance(int bonusCriticalChance) {
		super.bonusCriticalChance(bonusCriticalChance);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusSpeed(int bonusSpeed) {
		super.bonusSpeed(bonusSpeed);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		super.bonusAttackSpeed(bonusAttackSpeed);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusThreat(int bonusThreat) {
		super.bonusThreat(bonusThreat);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		super.bonusTacticalThreat(bonusTacticalThreat);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusSTDdown(int bonusSTDdown) {
		super.bonusSTDdown(bonusSTDdown);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusSTDup(int bonusSTDup) {
		super.bonusSTDup(bonusSTDup);
		return this;
	}
	
	@Override
	public SteelLegionWarriorBuilder baseDmgType(Attack.DmgType dmgType) {
		super.baseDmgType(dmgType);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder addResistance(Attack.DmgType resistance, double value) {
		super.addResistance(resistance, value);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
		super.addVulnerability(vulnerability, value);
		return this;
	}
	
	@Override
	public SteelLegionWarriorBuilder Type(Character.Type type) {
		super.Type(type);
		return this;
	}
	
	
	// Sets the ranks of each Ability (then defines the base Cooldown and Scaler based on that)
	// Vengeance Strike (Passive Ability)
	public SteelLegionWarriorBuilder setVengeanceStrikeRank(int newRank) {
		this.VengeanceStrikeRank = newRank;
		return this;
	}
	// Swordplay Prowess (Passive Ability):
	public SteelLegionWarriorBuilder setSwordplayProwessRank(int newRank) {
		this.SwordplayProwessRank = newRank;
		return this;
	}
	// Warriors Might (Passive Ability):
	public SteelLegionWarriorBuilder setWarriorsMightRank(int newRank) {
		this.WarriorsMightRank = newRank;
		return this;
	}
	// Agile Fighter (Passive Ability):
	public SteelLegionWarriorBuilder setAgileFighterRank(int newRank) {
		this.AgileFighterRank = newRank;
		return this;
	}
	
	// Sweep (Ability 1):
	public SteelLegionWarriorBuilder setSweepRank(int newRank) {
		this.SweepRank = newRank;
		return this;
	}
	// Charge (Ability 2):
	public SteelLegionWarriorBuilder setChargeRank(int newRank) {
		this.ChargeRank = newRank;
		return this;
	}
	// Flip Strike (Ability 3):
	public SteelLegionWarriorBuilder setFlipStrikeRank(int newRank) {
		this.FlipStrikeRank = newRank;
		return this;
	}
	// Intimidating Shout (Ability 4):
	public SteelLegionWarriorBuilder setIntimidatingShoutRank(int newRank) {
		this.IntimidatingShoutRank = newRank;
		return this;
	}
	// Taunting Attack (ULTIMATE):
	public SteelLegionWarriorBuilder setDeflectionRank(int newRank) {
		this.DeflectionRank = newRank;
		return this;
	}
	
	
	// Calculates the base stats based on level and stat-increasing passive abilities
	private void setBaseStats() {
		// Each stat is already set to its level 1 base value
		// Note: below only occurs if the specified a level, since the base level is 0.
		// "Level Up" each stat: (Multiply by the given multiplier for each level up to the current level)
		for (int counter = 2; counter <= this.Level; counter++) {
			// All these only increment at intervals of 5
			if (counter % 5 == 0) {
				this.Armor = (int)Math.round(this.Armor * 1.05);
				this.ArmorPiercing = (int)Math.round(this.ArmorPiercing * 1.05);
				this.Accuracy = (int)Math.round(this.Accuracy * 1.05);
				this.Block = (int)Math.round(this.Block * 1.05);
			}
			
			// Dynamically increasing stats
			// Health and Damage have various changes at intervals of 5 and 10
			if (counter % 10 == 0) {
				this.Health = (int)Math.round(this.Health * 1.05);
				this.Damage = (int)Math.round(this.Damage * 1.05);
			}
			else if (counter % 5 == 0) {
				this.Health = (int)Math.round(this.Health * 1.05);
				this.Damage = (int)Math.round(this.Damage * 1.03);
			}
			else {
				this.Health = (int)Math.round(this.Health * 1.03);
				this.Damage = (int)Math.round(this.Damage * 1.03);
			}
			
			// Attack Speed increases with various amounts at the given levels
			if (counter == 20) {
				this.AttackSpeed += 2;
			}
			if (counter == 40) {
				this.AttackSpeed += 2;
			}
			if (counter == 60) {
				this.AttackSpeed += 3;
			}
			if (counter == 80) {
				this.AttackSpeed += 4;
			}
			if (counter == 100) {
				this.AttackSpeed += 5;
			}
			
			// Threat increases with various amounts at the given levels
			if (counter == 10) {
				this.Threat += 3;
			}
			if (counter == 30) {
				this.Threat += 4;
			}
			if (counter == 50) {
				this.Threat += 5;
			}
			if (counter == 70) {
				this.Threat += 6;
			}
			if (counter == 90) {
				this.Threat += 7;
			}
		}
		
		// Calculate the bonus stats given by certain Abilities
		if (this.SwordplayProwessRank > 0) {
			SwordplayProwess sp = new SwordplayProwess(this.SwordplayProwessRank);
			this.bDamage += sp.getDamageBonus();
			this.bArmorPiercing += sp.getArmorPiercingBonus();
		}
		if (this.WarriorsMightRank > 0) {
			WarriorsMight wm = new WarriorsMight(this.WarriorsMightRank);
			this.bHealth += wm.getHealthBonus();
			this.bArmor += wm.getArmorBonus();
			this.bThreat += wm.getThreatBonus();
			this.bDamage += wm.getDamageBonus();
		}
		if (this.AgileFighterRank >= 5) {
			AgileFighter af = new AgileFighter(this.AgileFighterRank);
			this.bSpeed += af.getSpeedBonus();
		}
	}
	
	// Finishes the build by returning a SteelLegionWarrior Character
	public SteelLegionWarrior build() {
		// Set the base stats for the level and ability ranks
		this.setBaseStats();
		
		// Return the Steel Legion Warrior
		return new SteelLegionWarrior(super.build(), this.VengeanceStrikeRank, this.SwordplayProwessRank, this.WarriorsMightRank, this.AgileFighterRank, this.SweepRank, this.ChargeRank, this.FlipStrikeRank, this.IntimidatingShoutRank, this.DeflectionRank);
	}
}
