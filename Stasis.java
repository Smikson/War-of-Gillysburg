package WyattWitemeyer.WarOfGillysburg;

public class Stasis extends Condition {
	// Constructs a stasis
	public Stasis(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public Stasis(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Stasis: " + super.toString();
	}
}
