package WyattWitemeyer.WarOfGillysburg;

// Builds a Sentinel Arc Archer
public class SentinelArcArcherBuilder extends CharacterBuilder {
	// Creates all the Ability fields
	private int QuickShotRank;
	private int FlawlessnessRank;
	private int CombatRollRank;
	private int AttackSpeedRank;
	private int MultiShotRank;
	private int DoubleShredderRank;
	private int ConcentrateRank;
	private int FlipTrickShotRank;
	private int RainOfArrowsRank;
	
	// Constructs a SentinelArcArcherBuilder (populating the CharacterBuilder variables) based on the constant stats from Character
	public SentinelArcArcherBuilder(Character base) {
		super(base);
		this.QuickShotRank = 0;
		this.FlawlessnessRank = 0;
		this.CombatRollRank = 0;
		this.AttackSpeedRank = 0;
		this.MultiShotRank = 0;
		this.DoubleShredderRank = 0;
		this.ConcentrateRank = 0;
		this.FlipTrickShotRank = 0;
		this.RainOfArrowsRank = 0;
	}
	public SentinelArcArcherBuilder(SentinelArcArcher base) {
		super(base);
		this.QuickShotRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.QuickShot);
		this.FlawlessnessRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.Flawlessness);
		this.CombatRollRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.CombatRoll);
		this.AttackSpeedRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.AttackSpeed);
		this.MultiShotRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.MultiShot);
		this.DoubleShredderRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.DoubleShredder);
		this.ConcentrateRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.Concentrate);
		this.FlipTrickShotRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.FlipTrickShot);
		this.RainOfArrowsRank = base.getAbilityRank(SentinelArcArcher.AbilityNames.RainOfArrows);
	}
	public SentinelArcArcherBuilder() {
		this(Character.SENTINEL_ARC_ARCHER);
	}
	
	// Overrides the functions necessary from CharacterBuilder when constant stats are given.
	@Override
	public SentinelArcArcherBuilder Name(String name) {
		super.Name(name);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder Level(int level) {
		super.Level(level);
		return this;
	}
	
	@Override
	public SentinelArcArcherBuilder bonusHealth(int bonusHealth) {
		super.bonusHealth(bonusHealth);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusDamage(int bonusDamage) {
		super.bonusDamage(bonusDamage);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusArmor(int bonusArmor) {
		super.bonusArmor(bonusArmor);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		super.bonusArmorPiercing(bonusArmorPiercing);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusAccuracy(int bonusAccuracy) {
		super.bonusAccuracy(bonusAccuracy);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusBlock(int bonusBlock) {
		super.bonusBlock(bonusBlock);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusCriticalChance(int bonusCriticalChance) {
		super.bonusCriticalChance(bonusCriticalChance);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusSpeed(int bonusSpeed) {
		super.bonusSpeed(bonusSpeed);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		super.bonusAttackSpeed(bonusAttackSpeed);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusThreat(int bonusThreat) {
		super.bonusThreat(bonusThreat);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		super.bonusTacticalThreat(bonusTacticalThreat);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusSTDdown(int bonusSTDdown) {
		super.bonusSTDdown(bonusSTDdown);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder bonusSTDup(int bonusSTDup) {
		super.bonusSTDup(bonusSTDup);
		return this;
	}
	
	@Override
	public SentinelArcArcherBuilder baseDmgType(Attack.DmgType dmgType) {
		super.baseDmgType(dmgType);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder addResistance(Attack.DmgType resistance, double value) {
		super.addResistance(resistance, value);
		return this;
	}
	@Override
	public SentinelArcArcherBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
		super.addVulnerability(vulnerability, value);
		return this;
	}
	
	@Override
	public SentinelArcArcherBuilder addType(Character.Type type) {
		super.addType(type);
		return this;
	}
	
	
	// Sets the ranks of each Ability (then defines the base Cooldown and Scaler based on that)
	// Quick Shot (Passive Ability)
	public SentinelArcArcherBuilder setQuickShotRank(int newRank) {
		this.QuickShotRank = newRank;
		return this;
	}
	// Flawlessness (Passive Ability):
	public SentinelArcArcherBuilder setFlawlessnessRank(int newRank) {
		this.FlawlessnessRank = newRank;
		return this;
	}
	// Combat Roll (Passive Ability):
	public SentinelArcArcherBuilder setCombatRollRank(int newRank) {
		this.CombatRollRank = newRank;
		return this;
	}
	// Attack Speed (Passive Ability):
	public SentinelArcArcherBuilder setAttackSpeedRank(int newRank) {
		this.AttackSpeedRank = newRank;
		return this;
	}
	
	// Multi-shot (Ability 1):
	public SentinelArcArcherBuilder setMultiShotRank(int newRank) {
		this.MultiShotRank = newRank;
		return this;
	}
	// Double Shredder (Ability 2):
	public SentinelArcArcherBuilder setDoubleShredderRank(int newRank) {
		this.DoubleShredderRank = newRank;
		return this;
	}
	// Concentrate (Ability 3):
	public SentinelArcArcherBuilder setConcentrateRank(int newRank) {
		this.ConcentrateRank = newRank;
		return this;
	}
	// Flip Trick Shot (Ability 4):
	public SentinelArcArcherBuilder setFlipTrickShotRank(int newRank) {
		this.FlipTrickShotRank = newRank;
		return this;
	}
	// Rain of Arrows (ULTIMATE):
	public SentinelArcArcherBuilder setRainOfArrowsRank(int newRank) {
		this.RainOfArrowsRank = newRank;
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
				this.AttackSpeed = (int)Math.round(this.AttackSpeed * 1.05);
			}
			
			// Dynamically increasing stats
			// Health is a constant +3% in each case and Damage goes up from 3% to 5% to 7% in the more rare cases
			if (counter % 10 == 0) {
				this.Health = (int)Math.round(this.Health * 1.03);
				this.Damage = (int)Math.round(this.Damage * 1.07);
			}
			else if (counter % 5 == 0) {
				this.Health = (int)Math.round(this.Health * 1.03);
				this.Damage = (int)Math.round(this.Damage * 1.05);
			}
			else {
				this.Health = (int)Math.round(this.Health * 1.03);
				this.Damage = (int)Math.round(this.Damage * 1.03);
			}
		}
		
		// Calculate the bonus stats given by certain Abilities //DE Do this
		/*
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
		}*/
	}
	
	// Finishes the build by returning a SentinelArcArcher Character
	public SentinelArcArcher build() {
		// Set the base stats for the level and ability ranks
		this.setBaseStats();
		
		// Return the Sentinel Arc Archer
		return new SentinelArcArcher(super.build(), this.QuickShotRank, this.FlawlessnessRank, this.CombatRollRank, this.AttackSpeedRank, this.MultiShotRank, this.DoubleShredderRank, this.ConcentrateRank, this.FlipTrickShotRank, this.RainOfArrowsRank);
	}
}
