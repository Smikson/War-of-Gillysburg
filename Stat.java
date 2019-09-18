package WyattWitemeyer.WarOfGillysburg;
public class Stat {
	// Creates constant universal stats for "version" in order to remember specific versions of stats
	public static final Stat HEALTH = new Stat("Health");
	public static final Stat DAMAGE = new Stat("Damage");
	public static final Stat ARMOR = new Stat("Armor");
	public static final Stat ARMOR_PIERCING = new Stat("Armor Piercing");
	public static final Stat ACCURACY = new Stat("Accuracy");
	public static final Stat DODGE = new Stat("Dodge");
	public static final Stat BLOCK = new Stat("Block");
	public static final Stat CRITICAL_CHANCE = new Stat("Critical Chance");
	public static final Stat SPEED = new Stat("Speed");
	public static final Stat ATTACK_SPEED = new Stat("Attack Speed");
	public static final Stat RANGE = new Stat("Range");
	public static final Stat THREAT = new Stat("Threat");
	public static final Stat TACTICAL_THREAT = new Stat("Tactical Threat");
	public static final Stat STANDARD_DEVIATION_DOWN = new Stat("Standard Deviation Down");
	public static final Stat STANDARD_DEVIATION_UP = new Stat("Standard Deviation Up");
	
	public Stat statVersion; // Used to match the constants.
	private int total;
	public int bonus;
	private String name;
	
	public Stat(int value, int extra, String statName) {
		total = value;
		bonus = extra;
		name = statName;
		statVersion = this;
	}
	public Stat(int value, int extra, Stat version) {
		total = value;
		bonus = extra;
		statVersion = version;
		name = version.name;
	}
	
	public Stat(int value, String statName) {
		this(value, 0, statName);
	}
	public Stat(int value, Stat version) {
		this(value, 0, version);
	}
	
	public Stat(String statName) {
		this(0, 0, statName);
	}
	public Stat(Stat version) {
		this(0, 0, version);
	}
	
	public Stat() {
		this(0,0,"");
	}
	
	
	public int getTotal() {
		return total;
	}
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name + " = " + (this.total + this.bonus);
	}
}
