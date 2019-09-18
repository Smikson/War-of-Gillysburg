package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class StatusEffect extends Condition {
	// Variables
	private Stat altered;
	private double value;
	public int amountAdded;
	
	// Specifications
	private boolean isFlat;
	private boolean isIncoming;
	public boolean isApplied;
	
	// Constructors
	public StatusEffect(String name, int duration, HashSet<Condition> linkedConditions, Stat altered, double value) {
		super(name, duration, linkedConditions);
		this.altered = altered;
		this.value = value;
		
		this.amountAdded = 0;
		this.isFlat = false;
		this.isIncoming = false;
		this.isApplied = false;
	}
	public StatusEffect(String name, int duration, Stat altered, double value) {
		super(name, duration);
		this.altered = altered;
		this.value = value;
		
		this.amountAdded = 0;
		this.isFlat = false;
		this.isIncoming = false;
		this.isApplied = false;
	}
	public StatusEffect() {
		super();
		this.altered = null;
		this.value = 0;
		this.amountAdded = 0;
		this.isFlat = false;
		this.isIncoming = false;
		this.isApplied = false;
	}
	
	// Get methods
	public Stat getAlteredStat() {
		return this.altered;
	}
	public double getValue() {
		return this.value;
	}
	public boolean isFlat() {
		return this.isFlat;
	}
	public boolean isIncoming() {
		return this.isIncoming;
	}
	
	
	// Methods to change functionality of Status Effect when applied.
	// Changes the value to a flat change rather than the default: percentage.
	public void makeFlat() {
		this.isFlat = true;
	}
	// Changes the status effect to affect Characters attacking the wearer rather than the default: affecting the wearer
	public void makeIncoming() {
		this.isIncoming = true;
	}
	
	// To String methods for clarity when playing
	public String valueString() {
		String ret = "";
		if (this.value > 0) {
			ret += "+";
		}
		ret += this.value;
		if (!this.isFlat()) {
			ret += "%";
		}
		ret += " " + this.getAlteredStat().getName();
		return ret;
	}
	@Override
	public String toString() {
		String ret = this.getName() + ": ";
		ret += this.valueString();
		ret += ", " + (this.duration() - this.turnCount.value()) + " turns left";
		
		return ret;
	}
}
