package WyattWitemeyer.WarOfGillysburg;

public class Slow extends Condition {
	// Added Variable
	private double value;
	
	// Constructs a slow
	public Slow(String name, int duration, double val, Requirement actReq) {
		super(name, duration, actReq);
		this.value = -val;
		
		StatusEffect speedReductionEffect = new StatusEffect(StatVersion.SPEED, this.getValue(), StatusEffectType.BASIC);
		speedReductionEffect.makeFlat();
		this.addStatusEffect(speedReductionEffect);
	}
	public Slow(String name, int duration, double val) {
		this(name, duration, val, (Character withEffect) -> {return true;});
	}
	public Slow() {
		super();
		this.value = 0;
	}
	
	// Returns the value of the slow.
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "Slow: " + super.toString();
	}
}
