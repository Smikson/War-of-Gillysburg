package WyattWitemeyer.WarOfGillysburg;

public class AttackResult {
	// Constant for Empty Attacks
	public static final AttackResult EMPTY = new AttackResult();
	
	// Variables for each element of an attack
	private Character attacker;
	private Character defender;
	private Attack.DmgType type;
	private boolean didHit;
	private boolean didCrit;
	private double damageDealt;
	private boolean didKill;
	
	// Constructors
	public AttackResult(Character attacker, Character defender, Attack.DmgType atkType, boolean didHit, boolean didCrit, double damageDealt, boolean didKill) {
		this.attacker = attacker;
		this.defender = defender;
		this.type = atkType;
		this.didHit = didHit;
		this.didCrit = didCrit;
		this.damageDealt = damageDealt;
		this.didKill = didKill;
	}
	public AttackResult() {
		this.attacker = Character.EMPTY;
		this.defender = Character.EMPTY;
		this.type = Attack.DmgType.TRUE;
		this.didHit = false;
		this.didCrit = false;
		this.damageDealt = 0;
		this.didKill = false;
	}
	
	// Get methods of each element
	public Character getAttacker() {
		return this.attacker;
	}
	public Character getDefender() {
		return this.defender;
	}
	public Attack.DmgType getType() {
		return this.type;
	}
	public boolean didHit() {
		return this.didHit;
	}
	public boolean didCrit() {
		return this.didCrit;
	}
	public double damageDealt() {
		return this.damageDealt;
	}
	public boolean didKill() {
		return this.didKill;
	}
}

class AttackResultBuilder {
	// Variables for each element of an attack result
	private Character attacker;
	private Character defender;
	private Attack.DmgType type;
	private boolean didHit;
	private boolean didCrit;
	private double damageDealt;
	private boolean didKill;
	
	// Constructors
	public AttackResultBuilder(AttackResult atk) {
		this.attacker = atk.getAttacker();
		this.defender = atk.getDefender();
		this.type = atk.getType();
		this.didHit = atk.didHit();
		this.didCrit = atk.didCrit();
		this.damageDealt = atk.damageDealt();
		this.didKill = atk.didKill();
	}
	public AttackResultBuilder() {
		this(AttackResult.EMPTY);
	}
	
	
	// Methods for build process
	public AttackResultBuilder attacker(Character attacker) {
		this.attacker = attacker;
		return this;
	}
	
	public AttackResultBuilder defender(Character defender) {
		this.defender = defender;
		return this;
	}
	
	public AttackResultBuilder type(Attack.DmgType atkType) {
		this.type = atkType;
		return this;
	}
	
	public AttackResultBuilder didHit(boolean didHit) {
		this.didHit = didHit;
		return this;
	}
	
	public AttackResultBuilder didCrit(boolean didCrit) {
		this.didCrit = didCrit;
		return this;
	}
	
	public AttackResultBuilder damageDealt(double damageDealt) {
		this.damageDealt = damageDealt;
		return this;
	}
	
	public AttackResultBuilder didKill(boolean didKill) {
		this.didKill = didKill;
		return this;
	}
	
	public AttackResult build() {
		return new AttackResult(this.attacker, this.defender, this.type, this.didHit, this.didCrit, this.damageDealt, this.didKill);
	}
}
