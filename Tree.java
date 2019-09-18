package WyattWitemeyer.WarOfGillysburg;

public class Tree extends Terrain {
	public Tree(int eAccBonus, int eCritBonus, int eDodgeBlockBonus, int eEnemyAccDeduction) {
		super("Tree");
		
		// Add Bonuses
		StatusEffect accuracyBonus = new StatusEffect("Accuracy Bonus", -1, Stat.ACCURACY, 15 + eAccBonus);
		
		StatusEffect critBonus = new StatusEffect("Critical Chance Bonus", -1, Stat.CRITICAL_CHANCE, 5 + eCritBonus);
		
		StatusEffect dodgeBonus = new StatusEffect("Dodge Bonus", -1, Stat.DODGE, 15 + eDodgeBlockBonus);
		StatusEffect blockBonus = new StatusEffect("Block Bonus", -1, Stat.BLOCK, 15 + eDodgeBlockBonus);
		
		StatusEffect accuracyDeduction = new StatusEffect("Incoming Accuracy Reduction", -1, Stat.ACCURACY, -10 + eEnemyAccDeduction);
		accuracyDeduction.makeIncoming();
		
		this.effects.add(accuracyBonus);
		this.effects.add(critBonus);
		this.effects.add(dodgeBonus);
		this.effects.add(blockBonus);
		this.effects.add(accuracyDeduction);
	}
	public Tree() {
		this(0,0,0,0);
	}
}
