package WyattWitemeyer.WarOfGillysburg;

public class Snare extends Condition{
	// Constructs a snare
	public Snare(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		
		StatusEffect dodgeReduction = new StatusEffect(StatVersion.DODGE, -90, StatusEffectType.BASIC);
		this.addStatusEffect(dodgeReduction);
	}
	public Snare(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	
	@Override
	public String toString() {
		return "Snare: " + super.toString();
	}
}
