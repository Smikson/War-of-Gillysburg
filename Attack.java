package WyattWitemeyer.WarOfGillysburg;

public class Attack {
	// Constant for Empty Attacks
	public static final Attack EMPTY = new Attack();
	
	// Variables for each element of an attack
	private Character attacker;
	private Character defender;
	private boolean didHit;
	private boolean didCrit;
	private double damageDealt;
	private boolean didKill;
	
	// Constructors
	public Attack(Character attacker, Character defender, boolean didHit, boolean didCrit, double damageDealt, boolean didKill) {
		this.attacker = attacker;
		this.defender = defender;
		this.didHit = didHit;
		this.didCrit = didCrit;
		this.damageDealt = damageDealt;
		this.didKill = didKill;
	}
	public Attack() {
		this.attacker = Character.EMPTY;
		this.defender = Character.EMPTY;
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
