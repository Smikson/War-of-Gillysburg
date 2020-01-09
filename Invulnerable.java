package WyattWitemeyer.WarOfGillysburg;

public class Invulnerable extends Condition {
	// Constructs an "invulnerable" effect
	public Invulnerable(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public Invulnerable(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Invulnerable: " + super.toString();
	}
}
