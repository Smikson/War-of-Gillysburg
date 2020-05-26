package WyattWitemeyer.WarOfGillysburg;

public class Ability {
	// Variables, only the turnCount can be accessed outside the Ability itself
	private String name;
	private Character owner;
	private int rank;
	protected int cooldown;
	public int turnCount;
	protected double scaler;
	
	// Constructors
	public Ability(String name, Character owner, int rank) {
		this.name = name;
		this.owner = owner;
		this.rank = rank;
		this.cooldown = 0;
		this.turnCount = 0;
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
	public int turnCount() {
		return this.turnCount;
	}
	public double getScaler() {
		return this.scaler;
	}
	
	// Methods to deal with Cooldowns
	public void incrementTurn() {
		this.turnCount++;
	}
	public void resetCounter() {
		this.turnCount = 0;
	}
	public boolean onCooldown() {
		return this.turnCount < this.cooldown;
	}
	
	// Used to print the Ability's name for reference
	@Override
	public String toString() {
		return this.name;
	}
}


class UltimateAbility extends Ability {
	private boolean onCdown;
	
	// Constructors
	public UltimateAbility(String name, Character owner, int rank) {
		super(name, owner, rank);
		this.onCdown = false;
	}
	
	// Slightly Different Cooldown Functions since it can only be used once by default
	@Override
	public void resetCounter() {
		this.onCdown = true;
	}
	
	@Override
	public boolean onCooldown() {
		return this.onCdown;
	}
}
