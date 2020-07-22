package WyattWitemeyer.WarOfGillysburg;

public class EnemyBuilder extends CharacterBuilder {
	// Additional fields for enemies
	protected boolean UseThreat;
	protected Enemy.Difficulty difficulty;
	
	// Constructs a EnemyBuilder (populating the CharacterBuilder variables) based on the constant stats from Character
	public EnemyBuilder(Character base) {
		super(base);
		this.UseThreat = false;
		this.difficulty = Enemy.Difficulty.NORMAL;
	}
	public EnemyBuilder(Enemy base) {
		super(base);
		this.UseThreat = base.usesThreat();
		this.difficulty = base.getDifficulty();
	}
	public EnemyBuilder() {
		this(Character.EMPTY);
	}
	
	// Overrides the functions necessary from CharacterBuilder when constant stats are given.
	@Override
	public EnemyBuilder Name(String name) {
		this.name = name;
		return this;
	}
	@Override
	public EnemyBuilder Level(int level) {
		this.Level = level;
		return this;
	}
	@Override
	public EnemyBuilder Health(int health) {
		this.Health = health;
		return this;
	}
	@Override
	public EnemyBuilder Damage(int damage) {
		this.Damage = damage;
		return this;
	}
	@Override
	public EnemyBuilder Armor(int armor) {
		this.Armor = armor;
		return this;
	}
	@Override
	public EnemyBuilder ArmorPiercing(int armorPiercing) {
		this.ArmorPiercing = armorPiercing;
		return this;
	}
	@Override
	public EnemyBuilder Accuracy(int accuracy) {
		this.Accuracy = accuracy;
		return this;
	}
	@Override
	public EnemyBuilder Dodge(int dodge) {
		this.Dodge = dodge;
		return this;
	}
	@Override
	public EnemyBuilder Block(int block) {
		this.Block = block;
		return this;
	}
	@Override
	public EnemyBuilder CriticalChance(int criticalChance) {
		this.CriticalChance = criticalChance;
		return this;
	}
	@Override
	public EnemyBuilder Speed(int speed) {
		this.Speed = speed;
		return this;
	}
	@Override
	public EnemyBuilder AttackSpeed(int attackSpeed) {
		this.AttackSpeed = attackSpeed;
		return this;
	}
	@Override
	public EnemyBuilder Range(int range) {
		this.Range = range;
		return this;
	}
	@Override
	public EnemyBuilder Threat(int threat) {
		this.Threat = threat;
		return this;
	}
	@Override
	public EnemyBuilder TacticalThreat(int tacticalThreat) {
		this.TacticalThreat = tacticalThreat;
		return this;
	}
	@Override
	public EnemyBuilder STDdown(int stdDown) {
		this.STDdown = stdDown;
		return this;
	}
	@Override
	public EnemyBuilder STDup(int stdUp) {
		this.STDup = stdUp;
		return this;
	}
	@Override
	public EnemyBuilder addResistance(Attack.DmgType resistance, double value) {
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
	@Override
	public EnemyBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
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
	@Override
	public EnemyBuilder Type(Character.Type type) {
		this.Type = type;
		return this;
	}
	
	
	// Enemy specific fields
	public EnemyBuilder UseThreat(boolean useThrt) {
		this.UseThreat = useThrt;
		return this;
	}
	public EnemyBuilder Difficulty(Enemy.Difficulty diff) {
		this.difficulty = diff;
		return this;
	}
	
	// Finishes building the enemy
	public Enemy build() {
		return new Enemy(this.name, 
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
						 this.UseThreat,
						 this.difficulty,
						 this.resistances,
						 this.vulnerabilities,
						 this.Type);
	}
}
