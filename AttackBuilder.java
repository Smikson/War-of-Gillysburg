package WyattWitemeyer.WarOfGillysburg;

public class AttackBuilder {
	// Variables for each element of an attack
	private Character attacker;
	private Character defender;
	private AttackType type;
	private boolean didHit;
	private boolean didCrit;
	private double damageDealt;
	private boolean didKill;
	
	// Constructors
	public AttackBuilder(Attack atk) {
		this.attacker = atk.getAttacker();
		this.defender = atk.getDefender();
		this.type = atk.getType();
		this.didHit = atk.didHit();
		this.didCrit = atk.didCrit();
		this.damageDealt = atk.damageDealt();
		this.didKill = atk.didKill();
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
	
	public AttackBuilder type(AttackType atkType) {
		this.type = atkType;
		return this;
	}
	
	public AttackBuilder didHit(boolean didHit) {
		this.didHit = didHit;
		return this;
	}
	
	public AttackBuilder didCrit(boolean didCrit) {
		this.didCrit = didCrit;
		return this;
	}
	
	public AttackBuilder damageDealt(double damageDealt) {
		this.damageDealt = damageDealt;
		return this;
	}
	
	public AttackBuilder didKill(boolean didKill) {
		this.didKill = didKill;
		return this;
	}
	
	public Attack build() {
		return new Attack(this.attacker, this.defender, this.type, this.didHit, this.didCrit, this.damageDealt, this.didKill);
	}
}
