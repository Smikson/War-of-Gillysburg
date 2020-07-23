package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public class CharacterBuilder {
	// Holds the base stats for each stat (along with constants such as "Name" and "UseThreat"
	protected String name;
	protected int Level;
	
	protected int Health;
	protected int Damage;
	protected int Armor;
	protected int ArmorPiercing;
	protected int Accuracy;
	protected int Dodge;
	protected int Block;
	protected int CriticalChance;
	protected int Speed;
	protected int AttackSpeed;
	protected int Range;
	protected int Threat;
	protected int TacticalThreat;
	protected int STDdown;
	protected int STDup;
	
	// Holds the bonus stats for each stat
	protected int bHealth;
	protected int bDamage;
	protected int bArmor;
	protected int bArmorPiercing;
	protected int bAccuracy;
	protected int bDodge;
	protected int bBlock;
	protected int bCriticalChance;
	protected int bSpeed;
	protected int bAttackSpeed;
	protected int bRange;
	protected int bThreat;
	protected int bTacticalThreat;
	protected int bSTDdown;
	protected int bSTDup;
	
	protected Attack.DmgType baseDmgType;
	protected HashMap<Attack.DmgType,Double> resistances;
	protected HashMap<Attack.DmgType,Double> vulnerabilities;
	
	protected Character.Type Type;
	
	public CharacterBuilder(Character c) {
		// Matches everything (as total) with the current Character, setting all bonuses to 0.
		this.name = c.getName();
		this.Level = c.getLevel();
		this.Health = c.getHealth();
		this.Damage = c.getDamage();
		this.Armor = c.getArmor();
		this.ArmorPiercing = c.getArmorPiercing();
		this.Accuracy = c.getAccuracy();
		this.Dodge = c.getDodge();
		this.Block = c.getBlock();
		this.CriticalChance = c.getCriticalChance();
		this.Speed = c.getSpeed();
		this.AttackSpeed = c.getAttackSpeed();
		this.Range = c.getRange();
		this.Threat = c.getThreat();
		this.TacticalThreat = c.getTacticalThreat();
		this.STDdown = c.getSTDdown();
		this.STDup = c.getSTDup();
		
		this.baseDmgType = c.getBaseDmgType();
		this.resistances = c.getResistances();
		this.vulnerabilities = c.getVulnerabilities();
		
		this.Type = c.getType();
		
		this.bHealth = 0;
		this.bDamage = 0;
		this.bArmor = 0;
		this.bArmorPiercing = 0;
		this.bAccuracy = 0;
		this.bDodge = 0;
		this.bBlock = 0;
		this.bCriticalChance = 0;
		this.bSpeed = 0;
		this.bAttackSpeed = 0;
		this.bRange = 0;
		this.bThreat = 0;
		this.bTacticalThreat = 0;
		this.bSTDdown = 0;
		this.bSTDup = 0;
	}
	
	public CharacterBuilder() {
		// Makes everything 0.
		this(Character.EMPTY);
	}
	
	public CharacterBuilder Name(String name) {
		this.name = name;
		return this;
	}
	
	public CharacterBuilder Level(int level) {
		this.Level = level;
		return this;
	}
	
	public CharacterBuilder Health(int health) {
		this.Health = health;
		return this;
	}
	public CharacterBuilder bonusHealth(int bonusHealth) {
		this.bHealth = bonusHealth;
		return this;
	}
	
	public CharacterBuilder Damage(int damage) {
		this.Damage = damage;
		return this;
	}
	public CharacterBuilder bonusDamage(int bonusDamage) {
		this.bDamage = bonusDamage;
		return this;
	}
	
	public CharacterBuilder Armor(int armor) {
		this.Armor = armor;
		return this;
	}
	public CharacterBuilder bonusArmor(int bonusArmor) {
		this.bArmor = bonusArmor;
		return this;
	}
	
	public CharacterBuilder ArmorPiercing(int armorPiercing) {
		this.ArmorPiercing = armorPiercing;
		return this;
	}
	public CharacterBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		this.bArmorPiercing = bonusArmorPiercing;
		return this;
	}
	
	public CharacterBuilder Accuracy(int accuracy) {
		this.Accuracy = accuracy;
		return this;
	}
	public CharacterBuilder bonusAccuracy(int bonusAccuracy) {
		this.bAccuracy = bonusAccuracy;
		return this;
	}
	
	public CharacterBuilder Dodge(int dodge) {
		this.Dodge = dodge;
		return this;
	}
	public CharacterBuilder bonusDodge(int bonusDodge) {
		this.bDodge = bonusDodge;
		return this;
	}
	
	public CharacterBuilder Block(int block) {
		this.Block = block;
		return this;
	}
	public CharacterBuilder bonusBlock(int bonusBlock) {
		this.bBlock = bonusBlock;
		return this;
	}
	
	public CharacterBuilder CriticalChance(int criticalChance) {
		this.CriticalChance = criticalChance;
		return this;
	}
	public CharacterBuilder bonusCriticalChance(int bonusCriticalChance) {
		this.bCriticalChance = bonusCriticalChance;
		return this;
	}
	
	public CharacterBuilder Speed(int speed) {
		this.Speed = speed;
		return this;
	}
	public CharacterBuilder bonusSpeed(int bonusSpeed) {
		this.bSpeed = bonusSpeed;
		return this;
	}
	
	public CharacterBuilder AttackSpeed(int attackSpeed) {
		this.AttackSpeed = attackSpeed;
		return this;
	}
	public CharacterBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		this.bAttackSpeed = bonusAttackSpeed;
		return this;
	}
	
	public CharacterBuilder Range(int range) {
		this.Range = range;
		return this;
	}
	public CharacterBuilder bonusRange(int bonusRange) {
		this.bRange = bonusRange;
		return this;
	}
	
	public CharacterBuilder Threat(int threat) {
		this.Threat = threat;
		return this;
	}
	public CharacterBuilder bonusThreat(int bonusThreat) {
		this.bThreat = bonusThreat;
		return this;
	}
	
	public CharacterBuilder TacticalThreat(int tacticalThreat) {
		this.TacticalThreat = tacticalThreat;
		return this;
	}
	public CharacterBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		this.bTacticalThreat = bonusTacticalThreat;
		return this;
	}
	
	public CharacterBuilder STDdown(int stdDown) {
		this.STDdown = stdDown;
		return this;
	}
	public CharacterBuilder bonusSTDdown(int bonusSTDdown) {
		this.bSTDdown = bonusSTDdown;
		return this;
	}
	
	public CharacterBuilder STDup(int stdUp) {
		this.STDup = stdUp;
		return this;
	}
	public CharacterBuilder bonusSTDup(int bonusSTDup) {
		this.bSTDup = bonusSTDup;
		return this;
	}
	
	public CharacterBuilder baseDmgType(Attack.DmgType dmgType) {
		this.baseDmgType = dmgType;
		return this;
	}
	public CharacterBuilder addResistance(Attack.DmgType resistance, double value) {
		if (resistance.equals(Attack.DmgType.TRUE)) {
			return this;
		}
		
		this.resistances.put(resistance, value);
		if (resistance.equals(Attack.DmgType.MAGIC)) {
			this.addResistance(Attack.DmgType.ARCANE, value);
			this.addResistance(Attack.DmgType.FIRE, value);
			this.addResistance(Attack.DmgType.ICE, value);
			this.addResistance(Attack.DmgType.LIGHT, value);
			this.addResistance(Attack.DmgType.LIGHTNING, value);
			this.addResistance(Attack.DmgType.NECROMANTIC, value);
		}
		
		return this;
	}
	public CharacterBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
		if (vulnerability.equals(Attack.DmgType.TRUE)) {
			return this;
		}
		
		this.vulnerabilities.put(vulnerability, value);
		if (vulnerability.equals(Attack.DmgType.MAGIC)) {
			this.addVulnerability(Attack.DmgType.ARCANE, value);
			this.addVulnerability(Attack.DmgType.FIRE, value);
			this.addVulnerability(Attack.DmgType.ICE, value);
			this.addVulnerability(Attack.DmgType.LIGHT, value);
			this.addVulnerability(Attack.DmgType.LIGHTNING, value);
			this.addVulnerability(Attack.DmgType.NECROMANTIC, value);
		}
		
		return this;
	}
	
	public CharacterBuilder Type(Character.Type type) {
		this.Type = type;
		return this;
	}
	
	
	// Calculates the Base Health of a class based on level
	protected int calcBaseHealth(Character pc, int level) {
		// Start increasing Base Health at level 2 and stop at the current level
		int baseHealth = this.Health;
		int counter = 2;
		
		// Calculates Health for:
		// Steel Legion Tank
		if (pc.equals(Character.STEEL_LEGION_TANK)) {
			while (counter <= level) {
				if (counter % 10 == 0) {
					baseHealth = (int)Math.round(baseHealth * 1.07);
				}
				else if (counter % 5 == 0) {
					baseHealth = (int)Math.round(baseHealth * 1.05);
				}
				else {
					baseHealth = (int)Math.round(baseHealth * 1.03);
				}
				counter++;
			}
		}
		
		// Steel Legion Warrior, Silent Death Hunter, Kinitchu Order Thaumraturge
		if (pc.equals(Character.STEEL_LEGION_WARRIOR) || pc.equals(Character.SILENT_DEATH_HUNTER) || pc.equals(Character.KINITCHU_ORDER_THAUMRATURGE)) {
			while (counter <= level) {
				if (counter % 5 == 0) {
					baseHealth = (int)Math.round(baseHealth * 1.05);
				}
				else {
					baseHealth = (int)Math.round(baseHealth * 1.03);
				}
				counter++;
			}
		}
		
		// Steel Legion Barbarian, Kinitchu Order Luminescent Wizard
		if (pc.equals(Character.STEEL_LEGION_BARBARIAN) || pc.equals(Character.KINITCHU_ORDER_LUMINESCENT_WIZARD)) {
			while (counter <= level) {
				if (counter % 10 == 0) {
					baseHealth = (int)Math.round(baseHealth * 1.05);
				}
				else {
					baseHealth = (int)Math.round(baseHealth * 1.03);
				}
				counter++;
			}
		}
		
		// Sentinel Specialist, Silent Death Poison Specialist
		if (pc.equals(Character.SENTINEL_SPECIALIST) || pc.equals(Character.SILENT_DEATH_POISON_SPECIALIST)) {
			while (counter <= level) {
				if (counter % 10 == 0) {
					baseHealth = (int)Math.round(baseHealth * 1.05);
				}
				else if (counter % 5 == 0) {
					baseHealth = (int)Math.round(baseHealth * 1.04);
				}
				else {
					baseHealth = (int)Math.round(baseHealth * 1.03);
				}
				counter++;
			}
		}
		
		// Silent Death Shadow, Sentinel Sniper, Sentinel Arc Archer, Kinitchu Order Dragon Fire Wizard, Kinitchu Order Arcana, Kinitchu Order Necromancer
		if (pc.equals(Character.SILENT_DEATH_SHADOW) || pc.equals(Character.SENTINEL_SNIPER) || pc.equals(Character.SENTINEL_ARC_ARCHER) 
				|| pc.equals(Character.KINITCHU_ORDER_DRAGON_FIRE_WIZARD) || pc.equals(Character.KINITCHU_ORDER_ARCANA) || pc.equals(Character.KINITCHU_ORDER_NECROMANCER)) {
			
			while (counter <= level) {
				baseHealth = (int)Math.round(baseHealth * 1.03);
				counter++;
			}
		}
		
		return baseHealth;
	}
	
	// Calculates the Base Health of a class based on level
	protected int calcBaseDamage(Character pc, int level) {
		// Start increasing Base Health at level 2 and stop at the current level
		int baseDamage = this.Damage;
		int counter = 2;
		
		// Calculates Damage for:
		// Steel Legion Tank
		if (pc.equals(Character.STEEL_LEGION_TANK)) {
			while (counter <= level) {
				baseDamage = (int)Math.round(baseDamage * 1.03);
				counter++;
			}
		}
		
		// Steel Legion Warrior, Silent Death Hunter, Kinitchu Order Thaumraturge
		if (pc.equals(Character.STEEL_LEGION_WARRIOR) || pc.equals(Character.SILENT_DEATH_HUNTER) || pc.equals(Character.KINITCHU_ORDER_THAUMRATURGE)) {
			while (counter <= level) {
				if (counter % 10 == 0) {
					baseDamage = (int)Math.round(baseDamage * 1.05);
				}
				else {
					baseDamage = (int)Math.round(baseDamage * 1.03);
				}
				counter++;
			}
		}
		
		// Steel Legion Barbarian, Kinitchu Order Luminescent Wizard
		if (pc.equals(Character.STEEL_LEGION_BARBARIAN) || pc.equals(Character.KINITCHU_ORDER_LUMINESCENT_WIZARD)) {
			while (counter <= level) {
				if (counter % 5 == 0) {
					baseDamage = (int)Math.round(baseDamage * 1.05);
				}
				else {
					baseDamage = (int)Math.round(baseDamage * 1.03);
				}
				counter++;
			}
		}
		
		// Sentinel Specialist, Silent Death Poison Specialist
		if (pc.equals(Character.SENTINEL_SPECIALIST) || pc.equals(Character.SILENT_DEATH_POISON_SPECIALIST)) {
			while (counter <= level) {
				if (counter % 10 == 0) {
					baseDamage = (int)Math.round(baseDamage * 1.05);
				}
				else if (counter % 5 == 0) {
					baseDamage = (int)Math.round(baseDamage * 1.04);
				}
				else {
					baseDamage = (int)Math.round(baseDamage * 1.03);
				}
				counter++;
			}
		}
		
		// Silent Death Shadow, Sentinel Sniper, Sentinel Arc Archer, Kinitchu Order Dragon Fire Wizard, Kinitchu Order Arcana, Kinitchu Order Necromancer
		if (pc.equals(Character.SILENT_DEATH_SHADOW) || pc.equals(Character.SENTINEL_SNIPER) || pc.equals(Character.SENTINEL_ARC_ARCHER) 
				|| pc.equals(Character.KINITCHU_ORDER_DRAGON_FIRE_WIZARD) || pc.equals(Character.KINITCHU_ORDER_ARCANA) || pc.equals(Character.KINITCHU_ORDER_NECROMANCER)) {
			
			while (counter <= level) {
				if (counter % 10 == 0) {
					baseDamage = (int)Math.round(baseDamage * 1.07);
				}
				else if (counter % 5 == 0) {
					baseDamage = (int)Math.round(baseDamage * 1.05);
				}
				else {
					baseDamage = (int)Math.round(baseDamage * 1.03);
				}
				counter++;
			}
		}
		
		return baseDamage;
	}
	
	
	public Character build() {
		return new Character(this.name, 
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
							 this.baseDmgType,
							 this.resistances,
							 this.vulnerabilities,
							 this.Type);
	}
	
	public SteelLegionBarbarian buildSLB() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.STEEL_LEGION_BARBARIAN, this.Level);
			baseDamage = this.calcBaseDamage(Character.STEEL_LEGION_BARBARIAN, this.Level);
		}
		
		return new SteelLegionBarbarian(this.name, 
									    this.Level,
									    baseHealth + this.bHealth,
									    baseDamage + this.bDamage,
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
									    this.baseDmgType,
									    this.resistances,
									    this.vulnerabilities,
									    this.Type);
	}
	
	public SentinelSniper buildSS() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.SENTINEL_SNIPER, this.Level);
			baseDamage = this.calcBaseDamage(Character.SENTINEL_SNIPER, this.Level);
		}
		
		return new SentinelSniper(this.name, 
								  this.Level,
								  baseHealth + this.bHealth,
								  baseDamage + this.bDamage,
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
								  this.baseDmgType,
								  this.resistances,
								  this.vulnerabilities,
								  this.Type);
	}
	
	public SentinelSpecialist buildSSPL() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.SENTINEL_SPECIALIST, this.Level);
			baseDamage = this.calcBaseDamage(Character.SENTINEL_SPECIALIST, this.Level);
		}
		
		return new SentinelSpecialist(this.name, 
									  this.Level,
									  baseHealth + this.bHealth,
									  baseDamage + this.bDamage,
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
									  this.baseDmgType,
									  this.resistances,
									  this.vulnerabilities,
									  this.Type);
	}
	
	public SentinelArcArcher buildSAA() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.SENTINEL_ARC_ARCHER, this.Level);
			baseDamage = this.calcBaseDamage(Character.SENTINEL_ARC_ARCHER, this.Level);
		}
		
		return new SentinelArcArcher(this.name, 
									 this.Level,
									 baseHealth + this.bHealth,
									 baseDamage + this.bDamage,
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
									 this.baseDmgType,
									 this.resistances,
									 this.vulnerabilities,
									 this.Type);
	}
	
	public SilentDeathShadow buildSDS() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.SILENT_DEATH_SHADOW, this.Level);
			baseDamage = this.calcBaseDamage(Character.SILENT_DEATH_SHADOW, this.Level);
		}
		
		return new SilentDeathShadow(this.name, 
									 this.Level,
									 baseHealth + this.bHealth,
									 baseDamage + this.bDamage,
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
									 this.baseDmgType,
									 this.resistances,
									 this.vulnerabilities,
									 this.Type);
	}
	
	public SilentDeathPoisonSpecialist buildSDPS(){
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.SILENT_DEATH_POISON_SPECIALIST, this.Level);
			baseDamage = this.calcBaseDamage(Character.SILENT_DEATH_POISON_SPECIALIST, this.Level);
		}
		
		return new SilentDeathPoisonSpecialist(this.name, 
											   this.Level,
											   baseHealth + this.bHealth,
											   baseDamage + this.bDamage,
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
											   this.baseDmgType,
											   this.resistances,
											   this.vulnerabilities,
											   this.Type);
	}
	
	public SilentDeathHunter buildSDH() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.SILENT_DEATH_HUNTER, this.Level);
			baseDamage = this.calcBaseDamage(Character.SILENT_DEATH_HUNTER, this.Level);
		}
		
		return new SilentDeathHunter(this.name, 
									 this.Level,
									 baseHealth + this.bHealth,
									 baseDamage + this.bDamage,
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
									 this.baseDmgType,
									 this.resistances,
									 this.vulnerabilities,
									 this.Type);
	}
	
	public KinitchuOrderDragonFireWizard buildKODFW() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.KINITCHU_ORDER_DRAGON_FIRE_WIZARD, this.Level);
			baseDamage = this.calcBaseDamage(Character.KINITCHU_ORDER_DRAGON_FIRE_WIZARD, this.Level);
		}
		
		return new KinitchuOrderDragonFireWizard(this.name, 
												 this.Level,
												 baseHealth + this.bHealth,
												 baseDamage + this.bDamage,
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
												 this.baseDmgType,
												 this.resistances,
												 this.vulnerabilities,
												 this.Type);
	}
	
	public KinitchuOrderThaumraturge buildKOT() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.KINITCHU_ORDER_THAUMRATURGE, this.Level);
			baseDamage = this.calcBaseDamage(Character.KINITCHU_ORDER_THAUMRATURGE, this.Level);
		}
		
		return new KinitchuOrderThaumraturge(this.name, 
											 this.Level,
											 baseHealth + this.bHealth,
											 baseDamage + this.bDamage,
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
											 this.baseDmgType,
											 this.resistances,
											 this.vulnerabilities,
											 this.Type);
	}
	
	public KinitchuOrderArcana buildKOA() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.KINITCHU_ORDER_ARCANA, this.Level);
			baseDamage = this.calcBaseDamage(Character.KINITCHU_ORDER_ARCANA, this.Level);
		}
		
		return new KinitchuOrderArcana(this.name, 
									   this.Level,
									   baseHealth + this.bHealth,
									   baseDamage + this.bDamage,
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
									   this.baseDmgType,
									   this.resistances,
									   this.vulnerabilities,
									   this.Type);
	}
	
	public KinitchuOrderLuminescentWizard buildKOL() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.KINITCHU_ORDER_LUMINESCENT_WIZARD, this.Level);
			baseDamage = this.calcBaseDamage(Character.KINITCHU_ORDER_LUMINESCENT_WIZARD, this.Level);
		}
		
		return new KinitchuOrderLuminescentWizard(this.name, 
												  this.Level,
												  baseHealth + this.bHealth,
												  baseDamage + this.bDamage,
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
												  this.baseDmgType,
												  this.resistances,
												  this.vulnerabilities,
												  this.Type);
	}
	
	public KinitchuOrderNecromancer buildKON() {
		int baseHealth = this.Health;
		int baseDamage = this.Damage;
		
		// If they specified a level, calculate the base Health and Damage for that level and overwrite any previous base Health.
		if (this.Level > 0) {
			baseHealth = this.calcBaseHealth(Character.KINITCHU_ORDER_NECROMANCER, this.Level);
			baseDamage = this.calcBaseDamage(Character.KINITCHU_ORDER_NECROMANCER, this.Level);
		}
		
		return new KinitchuOrderNecromancer(this.name, 
										    this.Level,
										    baseHealth + this.bHealth,
										    baseDamage + this.bDamage,
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
										    this.baseDmgType,
										    this.resistances,
										    this.vulnerabilities,
										    this.Type);
	}
}
