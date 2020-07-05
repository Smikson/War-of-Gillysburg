package WyattWitemeyer.WarOfGillysburg;

public class Ability {
	// Variables, only the turnCount can be accessed outside the Ability itself
	private String name;
	private Character owner;
	private int rank;
	protected int cooldown;
	private int turnsRemaining;
	protected double scaler;
	
	// Constructors
	public Ability(String name, Character owner, int rank) {
		this.name = name;
		this.owner = owner;
		this.rank = rank;
		this.cooldown = 0;
		this.turnsRemaining = 0;
		this.scaler = 1.0;
	}
	
	// Get methods
	public Character getOwner() {
		return this.owner;
	}
	public int rank() {
		return this.rank;
	}
	public int cooldown() {
		return this.cooldown;
	}
	public int turnsRemaining() {
		return this.turnsRemaining;
	}
	public double getScaler() {
		return this.scaler;
	}
	
	// Methods to deal with Cooldowns
	public void decrementTurnsRemaining() {
		this.setTurnsRemaining(this.turnsRemaining - 1);
	}
	public void setTurnsRemaining(int turns) {
		if (turns >= 0 && turns <= this.cooldown) {
			this.turnsRemaining = turns;
		}
	}
	public void setOnCooldown() {
		this.setTurnsRemaining(this.cooldown);
	}
	public void setOffCooldown() {
		this.setTurnsRemaining(0);
	}
	public boolean onCooldown() {
		return this.turnsRemaining > 0;
	}
	
	// Use functions to be overridden by each class
	// Add int parameter for version number.
	public void use() {
		this.setOnCooldown();
		System.out.println("Warning: The Ability, " + this.name + ", does not have a use() function defined!");
	}
	
	// Used to print the Ability's name for reference
	@Override
	public String toString() {
		return this.name;
	}
}


class UltimateAbility extends Ability {
	private int charges;
	
	// Constructors
	public UltimateAbility(String name, Character owner, int rank, int charges) {
		super(name, owner, rank);
		this.charges = charges;
	}
	public UltimateAbility(String name, Character owner, int rank) {
		this(name, owner, rank, 1);
	}
	
	// Slightly Different Cooldown Functions since it can only be used once by default
	public void useCharges(int numCharges) {
		this.charges -= numCharges;
	}
	
	@Override
	public void setOnCooldown() {
		this.useCharges(1);
	}
	
	@Override
	public boolean onCooldown() {
		return this.charges > 0;
	}
}
