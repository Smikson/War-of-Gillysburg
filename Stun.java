package WyattWitemeyer.WarOfGillysburg;

public class Stun extends Condition{
	// Constructs a stun
	public Stun(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		
		StatusEffect dodgeReduction = new StatusEffect(StatVersion.DODGE, -100, StatusEffectType.BASIC);
		StatusEffect blockReduction = new StatusEffect(StatVersion.BLOCK, -75, StatusEffectType.BASIC);
		this.addStatusEffect(dodgeReduction);
		this.addStatusEffect(blockReduction);
	}
	public Stun(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	
	@Override
	public String toString() {
		return "Stun: " + super.toString();
	}
}
