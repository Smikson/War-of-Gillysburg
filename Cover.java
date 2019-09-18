package WyattWitemeyer.WarOfGillysburg;

public class Cover extends Terrain {
	public Cover(int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("Cover");
		
		// Add Bonuses
		StatusEffect dodgeBonus = new StatusEffect("Ranged Dodge Bonus", -1, Stat.DODGE, 5 + eDodgeBlockBonus);
		dodgeBonus.addRequirementGreaterThan(false, Stat.RANGE, 1);
		StatusEffect blockBonus = new StatusEffect("Ranged Block Bonus", -1, Stat.BLOCK, 5 + eDodgeBlockBonus);
		blockBonus.addRequirementGreaterThan(false, Stat.RANGE, 1);
		
		StatusEffect accuracyDeduction = new StatusEffect("Incoming Ranged Accuracy Reduction", -1, Stat.ACCURACY, -5 + eEnemyAccDeduction);
		accuracyDeduction.addRequirementGreaterThan(false, Stat.RANGE, 1);
		accuracyDeduction.makeIncoming();
		
		this.effects.add(dodgeBonus);
		this.effects.add(blockBonus);
		this.effects.add(accuracyDeduction);
	}
	public Cover() {
		this(0,0);
	}
}
