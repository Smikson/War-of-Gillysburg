package WyattWitemeyer.WarOfGillysburg;

public class Requirement {
	private boolean testsAttachedCharacter;
	private Stat requiredStat;
	private int threshold;
	private int version; // 1 = Greater Than, 0 = Equal To, -1 = Less Than
	
	public Requirement(boolean testsAttachedCharacter, Stat requiredStat, int threshold, int version) {
		this.testsAttachedCharacter = testsAttachedCharacter;
		this.requiredStat = requiredStat;
		this.threshold = threshold;
		this.version = version;
	}
	
	// Methods for passing the requirement (possibly with or without enemy involvement)
	public boolean passRequirement(Character affected, Character attacking) {
		if (testsAttachedCharacter) {
			return this.checkCharacter(affected);
		}
		
		// Otherwise, it affects the attacking Character
		return this.checkCharacter(attacking);
	}
	public boolean passRequirement(Character affected) {
		return this.checkCharacter(affected);
	}
	private boolean checkCharacter(Character c) {
		if (version > 0) {
			return c.getStatValue(requiredStat) > threshold;
		}
		else if (version < 0) {
			return c.getStatValue(requiredStat) < threshold;
		}
		// Otherwise, version == 0
		return c.getStatValue(requiredStat) == threshold;
	}
}
