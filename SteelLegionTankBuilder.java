package WyattWitemeyer.WarOfGillysburg;

public class SteelLegionTankBuilder extends CharacterBuilder {
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
		this.name = name;
		return this;
	}
	@Override
	public SteelLegionTankBuilder Level(int level) {
		this.Level = level;
		return this;
	}
	
	@Override
	public SteelLegionTankBuilder bonusHealth(int bonusHealth) {
		this.bHealth = bonusHealth;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusDamage(int bonusDamage) {
		this.bDamage = bonusDamage;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusArmor(int bonusArmor) {
		this.bArmor = bonusArmor;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		this.bArmorPiercing = bonusArmorPiercing;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusAccuracy(int bonusAccuracy) {
		this.bAccuracy = bonusAccuracy;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusBlock(int bonusBlock) {
		this.bBlock = bonusBlock;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusCriticalChance(int bonusCriticalChance) {
		this.bCriticalChance = bonusCriticalChance;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusSpeed(int bonusSpeed) {
		this.bSpeed = bonusSpeed;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		this.bAttackSpeed = bonusAttackSpeed;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusThreat(int bonusThreat) {
		this.bThreat = bonusThreat;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		this.bTacticalThreat = bonusTacticalThreat;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusSTDdown(int bonusSTDdown) {
		this.bSTDdown = bonusSTDdown;
		return this;
	}
	@Override
	public SteelLegionTankBuilder bonusSTDup(int bonusSTDup) {
		this.bSTDup = bonusSTDup;
		return this;
	}
	
	@Override
	public SteelLegionTankBuilder addResistance(AttackType resistance, double value) {
		if (resistance.equals(AttackType.TRUE)) {
			return this;
		}
		
		this.resistances.put(resistance, value);
		if (resistance.equals(AttackType.MAGIC)) {
			this.addResistance(AttackType.ARCANE, value);
			this.addResistance(AttackType.FIRE, value);
			this.addResistance(AttackType.ICE, value);
			this.addResistance(AttackType.LIGHT, value);
			this.addResistance(AttackType.LIGHTNING, value);
			this.addResistance(AttackType.NECROMANTIC, value);
		}
		
		return this;
	}
	@Override
	public SteelLegionTankBuilder addVulnerability(AttackType vulnerability, double value) {
		if (vulnerability.equals(AttackType.TRUE)) {
			return this;
		}
		
		this.vulnerabilities.put(vulnerability, value);
		if (vulnerability.equals(AttackType.MAGIC)) {
			this.addVulnerability(AttackType.ARCANE, value);
			this.addVulnerability(AttackType.FIRE, value);
			this.addVulnerability(AttackType.ICE, value);
			this.addVulnerability(AttackType.LIGHT, value);
			this.addVulnerability(AttackType.LIGHTNING, value);
			this.addVulnerability(AttackType.NECROMANTIC, value);
		}
		
		return this;
	}
	
	@Override
	public SteelLegionTankBuilder Type(CharacterType type) {
		this.Type = type;
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
	
	
	// Finishes the build by returning a SteelLegionTank Character
	public SteelLegionTank build() {
		// Each stat is already set to its level 1 base value
		// Note: below only occurs if the specified a level, since the base level is 0.
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
		
		// Return the Steel Legion Tank
		return new SteelLegionTank(this.name, 
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
								   this.HoldItRightThereRank,
								   this.EnchantedArmorRank,
								   this.ShieldSkillsRank,
								   this.ProfessionalLaughterRank,
								   this.ShieldBashRank,
								   this.ShieldReflectionRank,
								   this.TauntingAttackRank,
								   this.LeaderStrikeRank,
								   this.HaHaHaYouCantKillMeRank);
	}
}
