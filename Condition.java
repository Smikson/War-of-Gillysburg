package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public class Condition {
	// Variables
	private String name;
	private int duration;
	private boolean isPermanent;
	private boolean isEndOfTurn;
	public TurnCounter turnCount;
	
	// Linked conditions are removed when one from the list is removed.
	private HashSet<Condition> linkedConditions;
	
	// Some Conditions have requirements to apply
	private LinkedList<Requirement> requirements = new LinkedList<>();
	
	// Constructors
	public Condition(String name, int duration, HashSet<Condition> linkedConditions) {
		this.name = name;
		this.duration = duration;
		this.linkedConditions = linkedConditions;
		this.isPermanent = false;
		this.isEndOfTurn = false;
		
		this.turnCount = new TurnCounter();
	}
	public Condition(String name, int duration) {
		this.name = name;
		this.duration = duration;
		this.linkedConditions = new HashSet<>();
		this.isPermanent = false;
		this.isEndOfTurn = false;
		
		this.turnCount = new TurnCounter();
	}
	public Condition() {
		this.name = "Empty";
		this.duration = 0;
		this.turnCount = null;
		this.linkedConditions = null;
		this.isPermanent = false;
		this.isEndOfTurn = false;
	}
	
	// Get methods
	public String getName() {
		return this.name;
	}
	public int duration() {
		return this.duration;
	}
	public HashSet<Condition> getLinkedConditions() {
		return this.linkedConditions;
	}
	public boolean isPermanent() {
		return this.isPermanent;
	}
	public boolean isEndOfTurn() {
		return this.isEndOfTurn;
	}
	public LinkedList<Requirement> getRequirements() {
		return this.requirements;
	}
	public boolean hasRequirements() {
		return !this.requirements.isEmpty();
	}
	
	// Function to add an additional linked condition
	public void addLinkedCondition(Condition added) {
		this.linkedConditions.add(added);
	}
	
	// Method to determine if the duration of the condition is expired
	public boolean isExpired() {
		// A duration of -1 implies a permanent condition.
		if (this.duration == -1) {
			return false;
		}
		
		return this.turnCount.value() >= this.duration();
	}
	
	// Methods for requirements to apply the condition based on Stats of Character affected or the one attacking the Character affected
	protected void addRequirementGreaterThan(boolean testsAttachedCharacter, Stat requiredStat, int threshold) {
		this.requirements.add(new Requirement(testsAttachedCharacter, requiredStat, threshold, 1));
	}
	protected void addRequirementLessThan(boolean testsAttachedCharacter, Stat requiredStat, int threshold) {
		this.requirements.add(new Requirement(testsAttachedCharacter, requiredStat, threshold, -1));
	}
	protected void addRequirementEqualTo(boolean testsAttachedCharacter, Stat requiredStat, int threshold) {
		this.requirements.add(new Requirement(testsAttachedCharacter, requiredStat, threshold, 0));
	}
	public boolean passRequirements(Character affected, Character attacking) {
		// Checks all requirements. If one does not pass, it returns false
		for (Requirement req : this.requirements) {
			if (!req.passRequirement(affected, attacking)) {
				return false;
			}
		}
		
		return true;
	}
	// For Conditions that occur before combat (no enemy involved in any of the requirements)
	public boolean passRequirements(Character affected) {
		// Checks all requirements. If one does not pass, it returns false
		for (Requirement req : this.requirements) {
			if (!req.passRequirement(affected)) {
				return false;
			}
		}
		
		return true;
	}
	
	// To make an existing Condition permanent (will never automatically be removed, but can still be removed manually)
	public void makePermanent() {
		this.isPermanent = true;
	}
	
	// To make an existing Condition removed at the end of the Character's turn in which it expires rather than the beginning
	public void makeEndOfTurn() {
		this.isEndOfTurn = true;
	}
	
	// To String override for clarity when playing
	@Override
	public String toString() {
		String ret = this.getName() + ": ";
		if (this.isPermanent()) {
			ret += "Permanent Effect";
		}
		else {
			ret += (this.duration() - this.turnCount.value()) + " turns left";
		}
		
		return ret;
	}
}

