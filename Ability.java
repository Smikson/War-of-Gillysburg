package WyattWitemeyer.WarOfGillysburg;

public class Ability {
	// Variables, only the turnCount can be accessed outside the Ability itself
	protected String name;
	protected Character owner;
	protected int rank;
	protected int cooldown;
	public int turnCount;
	protected double scaler;
	
	// Constructors
	public Ability() {
		this.name = "Blank Ability";
		this.owner = Character.EMPTY;
		this.rank = 0;
		this.cooldown = 0;
		this.turnCount = 0;
		this.scaler = 1.0;
	}
	public Ability(int rank) {
		this();
		this.rank = rank;
	}
	public Ability(int rank, int cooldown, double scaler) {
		this();
		this.rank = rank;
		this.cooldown = cooldown;
		
		this.scaler = scaler;
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
	public UltimateAbility() {
		super();
		this.onCdown = false;
	}
	public UltimateAbility(int rank) {
		super(rank);
		this.onCdown = false;
	}
	public UltimateAbility(int rank, double scaler) {
		super(rank, 0, scaler);
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
