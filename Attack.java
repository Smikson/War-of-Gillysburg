package WyattWitemeyer.WarOfGillysburg;

enum AttackType {
	TRUE, SLASHING, SMASHING, PIERCING, MAGIC, FIRE, ICE, LIGHTNING, ARCANE, POISON, BLEED, EXPLOSIVE, LIGHT, NECROMANTIC;
}

public class Attack {
	// Constant for Empty Attacks to build up with
	public static final Attack EMPTY = new Attack();
	
	// Variables for each element of an attack
	private Character attacker;		// The Character attacking
	private Character defender;		// The Chatacter being attacked
	private AttackType type;		// The type of attack made
	private boolean usesScaler;		// Determines if the attack uses a scaler for damage (true) or a specified flat numeric amount (false)
	private double scaler;			// If a scaler is used (the usual), holds the Damage scaler of the attack
	private int flatDamage;			// If a scaler is not used, holds the specified flat numeric damage amount
	private boolean isTargeted;		// Determines if Targeted (true) or AOE (false)
	private boolean canMiss;		// Determines if the attack has the capability of being dodged or blocked
	private boolean canCrit;			// Determines if the attack has the capability of critically striking
	private boolean guaranteedCrit;	// Determines if the attack is a guaranteed critical strike
	private boolean ignoresArmor;	// Determines if the attack "ignores all armor"
	private boolean hasDeviation;	// Determines if the attack uses STDup and STDdown to deviate the attack
	
	// Constructors
	public Attack(Character attacker, Character defender, AttackType type, boolean usesScaler, double scaler, int flatDamage, boolean isTargeted, boolean canMiss, boolean canCrit, boolean guaranteedCrit, boolean ignoresArmor, boolean hasDeviation) {
		this.attacker = attacker;
		this.defender = defender;
		this.type = type;
		this.usesScaler = usesScaler;
		this.scaler = scaler;
		this.flatDamage = flatDamage;
		this.isTargeted = isTargeted;
		this.canMiss = canMiss;
		this.canCrit = canCrit;
		this.guaranteedCrit = guaranteedCrit;
		this.ignoresArmor = ignoresArmor;
		this.hasDeviation = hasDeviation;
	}
	public Attack() {
		this.attacker = Character.EMPTY;
		this.defender = Character.EMPTY;
		this.type = AttackType.TRUE;
		this.usesScaler = true;
		this.scaler = 1.0;
		this.flatDamage = 0;
		this.isTargeted = true;
		this.canMiss = true;
		this.canCrit = true;
		this.guaranteedCrit = false;
		this.ignoresArmor = false;
		this.hasDeviation = true;
	}
	
	// Get methods for each element
	public Character getAttacker() {
		return this.attacker;
	}
	public Character getDefender() {
		return this.defender;
	}
	public AttackType getAttackType() {
		return this.type;
	}
	public boolean usesScaler() {
		return this.usesScaler;
	}
	public double getScaler() {
		return this.scaler;
	}
	public int getFlatDamageAmount() {
		return this.flatDamage;
	}
	public boolean isTargeted() {
		return this.isTargeted;
	}
	public boolean canMiss() {
		return this.canMiss;
	}
	public boolean canCrit() {
		return this.canCrit;
	}
	public boolean guaranteedCrit() {
		return this.guaranteedCrit;
	}
	public boolean ignoresArmor() {
		return this.ignoresArmor;
	}
	public boolean hasDeviation() {
		return this.hasDeviation;
	}
}

// A helpful class used to build an Attack from the default static EMPTY Attack
class AttackBuilder {
	// Variables for each element of an attack
	private Character attacker;
	private Character defender;
	private AttackType type;
	private boolean usesScaler;
	private double scaler;
	private int flatDamage;
	private boolean isTargeted;
	private boolean canMiss;
	private boolean canCrit;
	private boolean guaranteedCrit;
	private boolean ignoresArmor;
	private boolean hasDeviation;
	
	// Constructors
	public AttackBuilder(Attack base) {
		this.attacker = base.getAttacker();
		this.defender = base.getDefender();
		this.type = base.getAttackType();
		this.usesScaler = base.usesScaler();
		this.scaler = base.getScaler();
		this.flatDamage = base.getFlatDamageAmount();
		this.isTargeted = base.isTargeted();
		this.canMiss = base.canMiss();
		this.canCrit = base.canCrit();
		this.guaranteedCrit = base.guaranteedCrit();
		this.ignoresArmor = base.ignoresArmor();
		this.hasDeviation = base.hasDeviation();
	}
	public AttackBuilder() {
		this(Attack.EMPTY);
	}
	
	// Methods for build process
	public AttackBuilder attacker(Character attacker) {
		this.attacker = attacker;
		return this;
	}
	
	public AttackBuilder defender(Character defender) {
		this.defender = defender;
		return this;
	}
	
	public AttackBuilder type(AttackType aType) {
		this.type = aType;
		return this;
	}
	
	public AttackBuilder usesScaler(boolean usesScaler) {
		this.usesScaler = usesScaler;
		return this;
	}
	public AttackBuilder usesScaler() {
		return this.usesScaler(true);
	}
	public AttackBuilder usesFlatDamage() {
		return this.usesScaler(false);
	}
	
	public AttackBuilder scaler(double scaler) {
		this.scaler = scaler;
		return this;
	}
	
	public AttackBuilder flatDamage(int flatDamage) {
		this.flatDamage = flatDamage;
		return this;
	}
	
	public AttackBuilder isTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
		
		// By default, Targeted attacks can miss and crit while AOE ones cannot
		this.canMiss = isTargeted;
		this.canCrit = isTargeted;
		
		return this;
	}
	public AttackBuilder isTargeted() {
		return this.isTargeted(true);
	}
	public AttackBuilder isAOE() {
		return this.isTargeted(false);
	}
	
	public AttackBuilder canMiss(boolean canMiss) {
		this.canMiss = canMiss;
		return this;
	}
	public AttackBuilder canMiss() {
		return this.canMiss(true);
	}
	public AttackBuilder cannotMiss() {
		return this.canMiss(false);
	}
	
	public AttackBuilder canCrit(boolean canCrit) {
		this.canCrit = canCrit;
		return this;
	}
	public AttackBuilder canCrit() {
		return this.canCrit(true);
	}
	public AttackBuilder cannotCrit() {
		return this.canCrit(false);
	}
	
	public AttackBuilder guaranteedCrit() {
		this.guaranteedCrit = true;
		return this;
	}
	
	public AttackBuilder ignoresArmor() {
		this.ignoresArmor = true;
		return this;
	}
	
	public AttackBuilder hasDeviation(boolean hasDeviation) {
		this.hasDeviation = hasDeviation;
		return this;
	}
	public AttackBuilder isStagnate() {
		this.hasDeviation = false;
		return this;
	}
	
	// Build the attack
	public Attack build() {
		return new Attack(this.attacker, this.defender, this.type, this.usesScaler, this.scaler, this.flatDamage, this.isTargeted, this.canMiss, this.canCrit, this.guaranteedCrit, this.ignoresArmor, this.hasDeviation);
	}
}
