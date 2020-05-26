package WyattWitemeyer.WarOfGillysburg;

public class AttackResultBuilder {
	// Variables for each element of an attack
	private Character attacker;
	private Character defender;
	private AttackType type;
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
	
	public AttackResultBuilder type(AttackType atkType) {
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
