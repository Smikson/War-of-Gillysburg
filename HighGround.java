package WyattWitemeyer.WarOfGillysburg;

public class HighGround extends Terrain {
	public HighGround(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("High Ground");
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect("Accuracy Bonus", -1, Stat.ACCURACY, 10 + eAccBonus);
		
		StatusEffect critBonus = new StatusEffect("Critical Chance Bonus", -1, Stat.CRITICAL_CHANCE, 3 + eCritBonus);
		
		StatusEffect dodgeBonus = new StatusEffect("Dodge Bonus", -1, Stat.DODGE, 10 + eDodgeBlockBonus);
		StatusEffect blockBonus = new StatusEffect("Block Bonus", -1, Stat.BLOCK, 10 + eDodgeBlockBonus);
		
		StatusEffect accuracyDeduction = new StatusEffect("Incoming Accuracy Reduction", -1, Stat.ACCURACY, -5 + eEnemyAccDeduction);
		accuracyDeduction.makeIncoming();
		
		this.effects.add(accuracyBonus);
		this.effects.add(critBonus);
		this.effects.add(dodgeBonus);
		this.effects.add(blockBonus);
		this.effects.add(accuracyDeduction);
	}
	public HighGround() {
		this(0,0,0,0);
	}
}
