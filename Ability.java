package WyattWitemeyer.WarOfGillysburg;

import java.util.*;


public class Ability {
	// Variables, the cooldown, scaler, and duration can be set when making the Ability
	private String name;
	private Character owner;
	private int rank;
	protected int cooldown;
	private int turnsRemaining;
	protected double scaler;
	private int duration;
	private int activeTurnsRemaining;
	private boolean activeFinalTurn;
	private boolean inFinalTurn;
	
	// Constructors
	public Ability(String name, Character owner, int rank) {
		this.name = name;
		this.owner = owner;
		this.rank = rank;
		this.cooldown = 0;
		this.turnsRemaining = 0;
		this.scaler = 1.0;
		this.duration = 0;
		this.activeTurnsRemaining = 0;
		this.activeFinalTurn = true;
		this.inFinalTurn = false;
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
	public int activeTurnsRemaining() {
		return this.activeTurnsRemaining;
	}
	public double getScaler() {
		return this.scaler;
	}
	public int getDuration() {
		return this.duration;
	}
	
	// Functions to deal with Cooldowns
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
		// The ability is on Cooldown if there are turns remaining and the Cooldown exists
		return this.turnsRemaining > 0 && this.cooldown > 0;
	}
	
	// Functions to deal with activatable Abilities with durations.
	protected void makeActiveAbility(int duration, boolean actFinalTurn) {
		this.duration = duration;
		this.activeFinalTurn = actFinalTurn;
	}
	protected void makeActiveAbility(int duration) {
		this.makeActiveAbility(duration, false);
	}
	public void activate(int extraTurns) {
		// If somehow, the Ability activates while active, it first deactivates, then reactivates
		if (this.isActive()) {
			this.deactivate();
		}
		this.activeTurnsRemaining = this.duration + extraTurns;
		
		// If the ability has a duration of 0 (a default ability), place it in the final turn -- will deactivate at end of turn
		if (this.duration == 0) {
			this.inFinalTurn = true;
		}
	}
	public void activate() {
		this.activate(0);
	}
	public void deactivate() {
		this.activeTurnsRemaining = 0;
		this.inFinalTurn = false;
	}
	public void setActiveTurnsRemaining(int turns) {
		if (turns < 0) {
			turns = 0;
		}
		boolean prevZero = this.activeTurnsRemaining == 0;
		this.activeTurnsRemaining = turns;
		if (this.activeTurnsRemaining == 0 && !prevZero) {
			this.inFinalTurn = true;
			if (!this.activeFinalTurn) {
				this.deactivate();
			}
		}
	}
	public boolean isActive() {
		return this.activeTurnsRemaining > 0 || (this.activeFinalTurn && this.inFinalTurn);
	}
	
	// Abilities can generally have effects before and after attacks, but by default do nothing
	public void applyPreAttackEffects(Attack atk) {}
	public void applyPostAttackEffects(AttackResult atkRes) {}
	
	// Functions that should be called at the beginning, end, and during turns.
	// Beginning turns: Decrements the turns remaining for Cooldowns and Durations
	public void decrementTurnsRemaining() {
		if (this.onCooldown()) {
			this.setTurnsRemaining(this.turnsRemaining - 1);
		}
		if (this.isActive()) {
			this.setActiveTurnsRemaining(this.activeTurnsRemaining - 1);
		}
	}
	
	// Ending turns: Allows for Abilities to have additional effects at the end of a Character's turn. By default, the ending of an active ability during the final turn
	public void endTurnEffects() {
		if (this.inFinalTurn && this.activeFinalTurn) {
			this.deactivate();
		}
		this.inFinalTurn = false;
	}
	
	// During turns: Use functions to be overridden by each class (there can be multiple versions, int specifies version)
	public void use(int version) {
		
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
		String activeInd = "";
		if (this.isActive()) {
			activeInd = "\n\t- ACTIVE: " + (this.activeTurnsRemaining != 0 ? this.activeTurnsRemaining + " Turn(s) Remaining!" : "Final Turn!");
		}
		String cdInd = "";
		if (this.onCooldown()) {
			cdInd = "\n\t- CD: " + this.turnsRemaining + " Turn(s) Remaining!";
		}
		return this.name + activeInd + cdInd;
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = "";
		ret += this.getName() + " - Rank " + this.rank();
		ret += this.cooldown() > 0? ("\n\tCooldown: " + this.cooldown()) : "";
		ret += this.getDuration() > 0? ("\n\tDuration: " + this.getDuration()) : "";
		ret += "\n\tScaler: " + this.getScaler();
		return ret;
	}
}

// ChargedAbility acts as a normal Ability but has multiple "charges" (and thus multiple turncounters, etc)
class ChargedAbility extends Ability {
	// Variables for multiple charges
	private int charges;
	private LinkedList<Integer> chargeTurnsRemaining;
	
	// Constructors
	public ChargedAbility(String name, Character owner, int rank, int charges) {
		// Create a normal Ability
		super(name, owner, rank);
		
		// Initialize the additional variables based on the number of specified charges
		this.charges = charges;
		this.chargeTurnsRemaining = new LinkedList<>();
		for (int i = 0; i < this.charges; i++) {
			this.chargeTurnsRemaining.add(0);
		}
	}
	
	// Get method for class variables
	public int getCharges() {
		return this.charges;
	}
	public int chargeTurnsRemaining(int index) {
		if (index < 0 || index >= this.getCharges()) {
			System.out.println("Warning: Tried to get charge turns remaining for " + this.getName() + " out of bounds");
			return -1;
		}
		return this.chargeTurnsRemaining.get(index);
	}
	
	// Override the get method for turns remaining to return ALL turns remaining (for all charges)
	@Override
	public int turnsRemaining() {
		int sum = 0;
		for (int tr : this.chargeTurnsRemaining) {
			sum += tr;
		}
		return sum;
	}
	
	// Functions to deal with Cooldowns (including overrides from Ability)
	public void setTurnsRemaining(int index, int turns) {
		if (index < 0 || index >= this.getCharges()) {
			System.out.println("Warning: Tried to set charge turns remaining for " + this.getName() + " out of bounds");
			return;
		}
		this.chargeTurnsRemaining.set(index, turns);
	}
	// Let the default be whatever charge is stored in the first position (index 0)
	@Override
	public void setTurnsRemaining(int turns) {
		this.setTurnsRemaining(0, turns);
	}
	
	// Sets a specific charge on Cooldown
	public void setOnCooldown(int index) {
		this.setTurnsRemaining(index, this.cooldown());
	}
	// Set on Cooldown will by default use 1 charge of the Ability: the charge with smallest turns remaining (first smallest if tied)
	@Override
	public void setOnCooldown() {
		// Finds the smallest turns remaining (first smallest if tied)
		int smallestRem = this.cooldown();
		int index = 0;
		for (int i = 0; i < this.getCharges(); i++) {
			if (this.chargeTurnsRemaining(i) < smallestRem) {
				smallestRem = this.chargeTurnsRemaining(i);
				index = i;
			}
		}
		
		// Places that charge "on Cooldown"
		this.setOnCooldown(index);
	}
	// Sets all charges on Cooldown
	public void setOnCooldownAll() {
		for (int i = 0; i < this.getCharges(); i++) {
			this.setOnCooldown(i);
		}
	}
	
	// Sets a specific charge on Cooldown
	public void setOffCooldown(int index) {
		this.setTurnsRemaining(index, 0);
	}
	// Set off Cooldown will by default use 1 charge of the Ability: the charge with largest turns remaining (first largest if tied)
	@Override
	public void setOffCooldown() {
		// Finds the largest turns remaining (first largest if tied)
		int largestRem = 0;
		int index = 0;
		for (int i = 0; i < this.getCharges(); i++) {
			if (this.chargeTurnsRemaining(i) > largestRem) {
				largestRem = this.chargeTurnsRemaining(i);
				index = i;
			}
		}
		
		// Places that charge "off Cooldown"
		this.setOffCooldown(index);
	}
	// Sets all charges off Cooldown
	public void setOffCooldownAll() {
		for (int i = 0; i < this.getCharges(); i++) {
			this.setOffCooldown(i);
		}
	}
	
	// Checks to see if a certain charge is "on Cooldown"
	public boolean onCooldown(int index) {
		// The charge is on Cooldown if there are turns remaining and the Cooldown exists
		return this.chargeTurnsRemaining(index) > 0 && this.cooldown() > 0;
	}
	
	// By default, the Ability is "on Cooldown" only if all its charges are "on Cooldown"
	@Override
	public boolean onCooldown() {
		// Return false if any charge is not on Cooldown
		for (int i = 0; i < this.getCharges(); i++) {
			if (!this.onCooldown(i)) {
				return false;
			}
		}
		
		// Return true otherwise
		return true;
	}
	
	
}



class UltimateAbility extends Ability {
	private String name;
	private int chargesRemaining;
	private int maxCharges;
	
	// Constructors
	public UltimateAbility(String name, Character owner, int rank, int charges) {
		super(name, owner, rank);
		this.name = name;
		this.maxCharges = charges;
		this.chargesRemaining = charges;
	}
	public UltimateAbility(String name, Character owner, int rank) {
		this(name, owner, rank, 1);
	}
	
	// Slightly Different Cooldown Functions since it can only be used once by default
	public void useCharges(int numCharges) {
		this.chargesRemaining -= numCharges;
		if (this.chargesRemaining < 0) {
			this.chargesRemaining = 0;
		}
	}
	
	@Override
	public void setOnCooldown() {
		this.useCharges(1);
	}
	
	@Override
	public void setOffCooldown() {
		this.chargesRemaining = this.maxCharges;
	}
	
	@Override
	public boolean onCooldown() {
		return this.chargesRemaining <= 0;
	}
	
	// Overrides toString for display purposes during combat
	@Override
	public String toString() {
		String activeInd = "";
		if (this.isActive()) {
			activeInd = "\n\t- ACTIVE: " + (this.activeTurnsRemaining() != 0 ? this.activeTurnsRemaining() + " Turn(s) Remaining!" : "Final Turn!");
		}
		String cdInd = "";
		if (this.onCooldown()) {
			cdInd = "\n\t- CD: " + this.chargesRemaining + " Charge(s) Remaining!";
		}
		return this.name + activeInd + cdInd;
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = "";
		ret += this.getName() + " - Rank " + this.rank();
		ret += this.maxCharges > 0? ("\n\tCharges: " + this.maxCharges) : "";
		ret += this.getDuration() > 0? ("\n\tDuration: " + this.getDuration()) : "";
		ret += "\n\tScaler: " + this.getScaler();
		return ret;
	}
}
