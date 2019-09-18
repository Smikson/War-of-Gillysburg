package WyattWitemeyer.WarOfGillysburg;

public interface Obstacle {
	public String getName();
	public int getArmorScore();
	public int calculatePercentage(int extraSpaces);
}
