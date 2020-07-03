package WyattWitemeyer.WarOfGillysburg;

// See WyattWitemeyer.WarOfGillysburg.Condition.java for implementation of: Requirement and DualRequirement.


public class StatusEffect {
	public static enum Type {
		BASIC, INCOMING, OUTGOING;
	}
	
	// Variables
	private Stat.Version altered;
	private double value;
	public int amountAdded;
	
	// Specifications
	private Requirement applyRequirement;
	private DualRequirement applyDualRequirement;
	private Type seType;
	private boolean isFlat;
	private boolean affectsSelf;
	public boolean isApplied;
	
	// Constructors
	public StatusEffect(Stat.Version altered, double value, Type type) {
		this.altered = altered;
		this.value = value;
		this.amountAdded = 0;
		
		this.applyRequirement = (Character withEffect) -> {return true;};
		this.applyDualRequirement = (Character withEffect, Character other) -> {return true;};
		this.seType = type;
		this.isFlat = false;
		this.affectsSelf = true;
		this.isApplied = false;
	}
	public StatusEffect(Stat.Version altered, double value) {
		this(altered, value, Type.BASIC);
	}
	public StatusEffect() {
		this(Stat.Version.EMPTY, 0);
	}
	
	// Get methods
	public Stat.Version getAlteredStat() {
		return this.altered;
	}
	public double getValue() {
		return this.value;
	}
	public Requirement getApplyRequirement() {
		return this.applyRequirement;
	}
	public DualRequirement getApplyDualRequirement() {
		return this.applyDualRequirement;
	}
	public Type getType() {
		return this.seType;
	}
	public boolean isFlat() {
		return this.isFlat;
	}
	public boolean affectsSelf() {
		return this.affectsSelf;
	}
	
	
	
	// Methods to change functionality of Status Effect when applied.
	// Changes the value to a flat change rather than the default: percentage.
	public void makeFlat() {
		this.isFlat = true;
	}
	// A method that changes the value to a percentage if something is flat by default (slows) and is meant as a percentage.
	public void makePercentage() {
		this.isFlat = false;
	}
	// Changes the status effect to affect an "other" enemy (attacking if Incoming, defender if Outgoing)
	public void makeAffectOther() {
		this.affectsSelf = false;
	}
	// Sets the requirement for a basic Status Effect
	public void setBasicRequirement(Requirement appReq) {
		this.applyRequirement = appReq;
	}
	// Sets the dual-requirement for a non-basic status effect (incoming or outgoing)
	public void setDualRequirement(DualRequirement appReq) {
		this.applyDualRequirement = appReq;
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
		if (this.seType != Type.BASIC) {
			ret += " " + this.seType.toString();
		}
		ret += " " + this.getAlteredStat().toString();
		return ret;
	}
	@Override
	public String toString() {
		return this.valueString();
	}
}
