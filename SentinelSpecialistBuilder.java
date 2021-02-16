package WyattWitemeyer.WarOfGillysburg;

// Builds a Sentinel Specialist
public class SentinelSpecialistBuilder extends CharacterBuilder {
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
		
		// Calculate the bonus stats given by certain Abilities //DE Do this
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
