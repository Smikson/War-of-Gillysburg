package WyattWitemeyer.WarOfGillysburg;

// This class acts as a simpler version of a Fraction to keep track of portions of counting turns in Conditions.
public class TurnCounter {
	// Declares variables
	private int characterTurns;  // Numerator
	private final int totalCombatants; // Denominator
	
	// Constructor
	public TurnCounter() {
		this.characterTurns = 0;
		this.totalCombatants = BattleSimulator.getInstance().getNumCombatants();
	}
	
	
	// Methods to change the number of current character turns
	private void add(int numCharacterTurns) {
		this.characterTurns += numCharacterTurns;
	}
	public void addRounds(int numRounds) {
		this.add(numRounds * this.totalCombatants);
	}
	public void subtractRounds(int numRounds) {
		this.addRounds(-numRounds);
	}
	public void increment() {
		this.add(1);
	}
	public void set(int num) {
		this.characterTurns = num * this.totalCombatants;
	}
	public void reset() {
		this.set(0);
	}
	
	
	// Methods to return value to compare with durations, as well as just the current "Fraction" as a String
	public int value() {
		return this.characterTurns/this.totalCombatants;
	}
	
	@Override
	public String toString() {
		return this.characterTurns + "/" + this.totalCombatants;
	}
}
