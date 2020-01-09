package WyattWitemeyer.WarOfGillysburg;

public class Blind extends Condition {
	// Constructs a blind
	public Blind(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		
		StatusEffect dodgeReduction = new StatusEffect(StatVersion.DODGE, -90, StatusEffectType.BASIC);
		StatusEffect blockReduction = new StatusEffect(StatVersion.BLOCK, -70, StatusEffectType.BASIC);
		StatusEffect speedReduction = new StatusEffect(StatVersion.SPEED, -50, StatusEffectType.BASIC);
		this.addStatusEffect(dodgeReduction);
		this.addStatusEffect(blockReduction);
		this.addStatusEffect(speedReduction);
	}
	public Blind(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	public Blind() {
		super();
	}
	
	@Override
	public String toString() {
		return "Blind: " + super.toString();
	}
}
