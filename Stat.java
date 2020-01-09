package WyattWitemeyer.WarOfGillysburg;

enum StatVersion {
	HEALTH, DAMAGE, ARMOR, ARMOR_PIERCING, ACCURACY, DODGE, BLOCK, CRITICAL_CHANCE, SPEED, ATTACK_SPEED, RANGE, THREAT, TACTICAL_THREAT, STANDARD_DEVIATION_DOWN, STANDARD_DEVIATION_UP, EMPTY;
}

public class Stat {
	private StatVersion version; // Used to match the constants.
	private int total;
	public int bonus;
	
	
	public Stat(int value, int extra, StatVersion type) {
		this.total = value;
		this.bonus = extra;
		this.version = type;
	}
	public Stat(int value, StatVersion type) {
		this(value, 0, type);
	}
	public Stat(StatVersion type) {
		this(0, 0, type);
	}
	public Stat() {
		this(0,0, StatVersion.EMPTY);
	}
	
	
	public int getTotal() {
		return this.total;
	}
	public StatVersion getVersion() {
		return this.version;
	}
	
	@Override
	public String toString() {
		String ret = this.version.toString();
		String firstLetter = "" + ret.charAt(0);
		ret = firstLetter + ret.toLowerCase().substring(1);
		return ret + " = " + (this.total + this.bonus);
	}
}
