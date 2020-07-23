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
		this.VengeanceStrikeRank = base.getVengeanceStrikeRank();
		this.SwordplayProwessRank = base.getSwordplayProwessRank();
		this.WarriorsMightRank = base.getWarriorsMightRank();
		this.AgileFighterRank = base.getAgileFighterRank();
		this.SweepRank = base.getSweepRank();
		this.ChargeRank = base.getChargeRank();
		this.FlipStrikeRank = base.getFlipStrikeRank();
		this.IntimidatingShoutRank = base.getIntimidatingShoutRank();
		this.DeflectionRank = base.getDeflectionRank();
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
	
	
	// Finishes the build by returning a SteelLegionWarrior Character
	public SteelLegionWarrior build() {
		// Each stat is already set to its level 1 base value
		// Note: below only occurs if the specified a level, since the base level is 0.
		// "Level Up" each stat: (Multiply by the given multiplier for each level up to the current level)
		for (int counter = 2; counter <= this.Level; counter++) {
			// Statically increasing stats (increases by same amount each level)
			this.Armor = (int)Math.round(this.Armor * 1.05);
			this.ArmorPiercing = (int)Math.round(this.ArmorPiercing * 1.05);
			this.Accuracy = (int)Math.round(this.Accuracy * 1.05);
			this.Block = (int)Math.round(this.Block * 1.05);
			
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
		
		// Calculate the bonus stats given by certain Abilities [DO THIS]
		
		
		// Return the Steel Legion Warrior
		return new SteelLegionWarrior(this.name, 
				   					  this.Level,
				   					  this.Health + this.bHealth,
				   					  this.Damage + this.bDamage,
				   					  this.Armor + this.bArmor,
				   					  this.ArmorPiercing + this.bArmorPiercing,
				   					  this.Accuracy + this.bAccuracy,
				   					  this.Dodge + this.bDodge,
				   					  this.Block + this.bBlock,
				   					  this.CriticalChance + this.bCriticalChance,
				   					  this.Speed + this.bSpeed,
				   					  this.AttackSpeed + this.bAttackSpeed,
				   					  this.Range + this.bRange,
				   					  this.Threat + this.bThreat,
				   					  this.TacticalThreat + this.bTacticalThreat,
				   					  this.STDdown,
				   					  this.STDup,
				   					  this.resistances,
				   					  this.vulnerabilities,
				   					  this.Type,
				   					  this.VengeanceStrikeRank,
				   					  this.SwordplayProwessRank,
				   					  this.WarriorsMightRank,
				   					  this.AgileFighterRank,
				   					  this.SweepRank,
				   					  this.ChargeRank,
				   					  this.FlipStrikeRank,
				   					  this.IntimidatingShoutRank,
				   					  this.DeflectionRank);
	}
}
