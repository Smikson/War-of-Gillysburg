package WyattWitemeyer.WarOfGillysburg;

public class Invincible extends Condition {
	// Constructs an "invincible" effect
	public Invincible(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public Invincible(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Invincible: " + super.toString();
	}
}
