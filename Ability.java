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
	public String getName() {
		return this.name;
	}
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
		if (turns < 0) {
			turns = 0;
		}
		if (turns > this.cooldown) {
			turns = this.cooldown;
		}
		this.turnsRemaining = turns;
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
	
	// Use functions to be overridden by each class (there can be multiple versions, int specifies version)
	public void use(int version) {
		// To also show the basic format of the use function, start by putting the Ability on Cooldown
		this.setOnCooldown();
		
		// Then, for each possible version of the Ability, specify based on the number give (if only 1 use function is present, the check must be the version 1 by default)
		if (version == 1) {
			this.use();
			return;
		}
		
		// Print a warning if this function is ever actually directly called
		System.out.println("Warning: The Ability, " + this.name + ", does not have a use(" + version + ") function defined, but it was called!");
	}
	public void use() {
		// Put the Ability on Cooldown (still to show format), then print a warning
		this.setOnCooldown();
		System.out.println("Warning: The Ability, " + this.name + ", does not have a use(1) function defined, but it was called!");
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
