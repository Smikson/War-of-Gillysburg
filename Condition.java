package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

// Implementation of lambda function for requirements.
interface Requirement {
	// Function to define a requirement for a given Condition action
	boolean evaluate(Character withEffect);
	
	// Default is that the Condition action always occurs
	default boolean evalDefault() {
		return true;
	}
}

// Used more for Status Effects, allows requirements for two Characters (usually one with the effect on them, and one other (usually attacker))
interface DualRequirement {
	// Function to define a requirement for a given Condition action
	boolean evaluate(Character withEffect, Character other);
	
	// Default is that the Condition action always occurs
	default boolean evalDefault() {
		return true;
	}
}

public class Condition {
	// Descriptive Variables
	private Character source;
	private String name;
	private int duration;
	private boolean isSourceIncrementing;
	private boolean isPermanent;
	private boolean isEndOfTurn;
	private boolean isActive;
	
	// Functionality Variables
	private int turnCount;
	private LinkedList<StatusEffect> effects;
	private Requirement activeRequirement;
	private HashSet<Condition> linkedConditions;
	
	
	// Constructors
	public Condition(String name, int duration, Requirement actReq) {
		this.source = Character.EMPTY;
		this.name = name;
		this.duration = duration;
		this.isSourceIncrementing = false;
		this.isPermanent = false;
		this.isEndOfTurn = false;
		this.isActive = false;
		
		this.turnCount = 0;
		this.effects = new LinkedList<StatusEffect>();
		this.activeRequirement = actReq;
		this.linkedConditions = new HashSet<Condition>();
	}
	public Condition(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	public Condition() {
		this("Empty", 0);
	}
	
	// Get methods
	public Character getSource() {
		return this.source;
	}
	public String getName() {
		return this.name;
	}
	public int duration() {
		return this.duration;
	}
	public boolean isSourceIncrementing() {
		return this.isSourceIncrementing;
	}
	public boolean isPermanent() {
		return this.isPermanent;
	}
	public boolean isEndOfTurn() {
		return this.isEndOfTurn;
	}
	public boolean isActive() {
		return this.isActive;
	}
	public LinkedList<StatusEffect> getStatusEffects() {
		return this.effects;
	}
	public Requirement getActiveRequirement() {
		return this.activeRequirement;
	}
	public HashSet<Condition> getLinkedConditions() {
		return this.linkedConditions;
	}
	
	// Add/Set functions
	public void addStatusEffect(StatusEffect se) {
		this.effects.add(se);
	}
	public void addLinkedCondition(Condition added) {
		this.linkedConditions.add(added);
	}
	public void setSource(Character c) {
		this.source = c;
	}
	public void makeSourceIncrementing() {
		this.isSourceIncrementing = true;
	}
	public void makePermanent() {
		this.isPermanent = true;
	}
	public void makeEndOfTurn() {
		this.isEndOfTurn = true;
	}
	public void activate() {
		this.isActive = true;
	}
	// Function to increment the turn count
	public void incrementTurn() {
		this.turnCount++;
	}
	
	// Function to determine if the duration of the condition is expired
	public boolean isExpired() {
		// A duration of -1 implies a permanent condition.
		if (this.duration == -1) {
			return false;
		}
		
		return this.turnCount >= this.duration();
	}
	
	
	// To String override for clarity when playing
	@Override
	public String toString() {
		String ret = this.getName() + ": ";
		if (this.isPermanent()) {
			ret += "Permanent Effect";
		}
		else {
			ret += (this.duration() - this.turnCount) + " turns left - Source: " + this.getSource().getName();
		}
		if (this.isSourceIncrementing) {
			ret += " (Increments on source)";
		}
		if (this.isEndOfTurn()) {
			ret += " (End of Turn Condition)";
		}
		for (StatusEffect se : this.effects) {
			ret += "\n\t\t" + se.toString();
		}
		
		return ret;
	}
}

