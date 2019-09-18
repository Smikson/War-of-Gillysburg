package WyattWitemeyer.WarOfGillysburg;

public class Destructible implements Obstacle {
	// Variables
	private String name;
	private int health;
	private int armorScore;
	private boolean stunApplies;
	
	// Constructors
	public Destructible(String name, int hp, int armorScore) {
		this.name = name;
		this.health = hp;
		this.armorScore = armorScore;
		this.stunApplies = false;
	}
	public Destructible(String name, int hp) {
		this(name, hp, 100);
	}
	public Destructible(int hp) {
		this("Destructible Object", hp);
	}
	public Destructible(String name) {
		this(name, 10);
	}
	public Destructible() {
		this("Destructible Object");
	}
	
	// Methods to manipulate the Health value
	public int getHealth() {
		return this.health;
	}
	public void takeDamage(int amount) {
		this.health -= amount;
	}
	public boolean isDestroyed() {
		return this.health <= 0;
	}
	
	// Get method for if the stun should be applied
	public boolean stunApplies() {
		return this.stunApplies;
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
	// Damages the object and returns the percentage dealt to a Character knocked back based on the number of extra spaces
	@Override
	public int calculatePercentage(int extraSpaces) {
		// Resets if the stun applies to false
		this.stunApplies = false;
		
		// When the object will not be destroyed:
		if (extraSpaces < this.getHealth()) {
			this.takeDamage(1 + extraSpaces);
			int percentage = 5 + 2 * extraSpaces;
			if (percentage > 15) {
				this.stunApplies = true;
				percentage = 15;
			}
			return percentage;
		}
		
		// When the object is destroyed:
		this.takeDamage(1 + extraSpaces);
		this.stunApplies = true;
		return 10;
	}
}
