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
