package WyattWitemeyer.WarOfGillysburg;

public class NonDestructible implements Obstacle {
	// Variables
	private String name;
	private int armorScore;
	
	// Constructors
	public NonDestructible(String name, int armorScore) {
		this.name = name;
		this.armorScore = armorScore;
	}
	public NonDestructible(String name) {
		this(name, 100);
	}
	public NonDestructible(int armorScore) {
		this("NonDestructible Object", armorScore);
	}
	public NonDestructible() {
		this(100);
	}
	
	// Methods to get the "Obstacle" qualities
	@Override
	public String getName() {
		return this.name;
	}
	@Override
	public int getArmorScore() {
		return this.armorScore;
	}
	// Returns the percentage dealt to a Character knocked back based on the number of extra spaces
	@Override
	public int calculatePercentage(int extraSpaces) {
		int percentage = 8 + 3 * extraSpaces;
		if (percentage > 20) {
			percentage = 20;
		}
		return percentage;
	}
}
