package WyattWitemeyer.WarOfGillysburg;

public interface DamageOverTime {
	public boolean isExpired();
	public void activate();
	public String displayString();
}
