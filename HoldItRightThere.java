package WyattWitemeyer.WarOfGillysburg;

public class HoldItRightThere extends Ability {
	// Additional Effects of Ability
	private Condition selfBlock;
	private Condition enemyHalt;
	
	public HoldItRightThere(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Unique Passive Ability: \"Hold It Right There!\"";
		this.rank = rank;
		
		// Calculate the additional Staus Effects
		this.setSelfBlock();
		this.setEnemyHalt();
	}
	
	
	// Additional Status Effects for this Ability:
	
	// Increased Block when blocking for an ally
	private void setSelfBlock() {
		// Find block amount based on rank
		int amount = 0;
		for (int i = 1; i <= this.rank; i++) {
			// Ranks 2,5 grant +10% increased Block
			if (i == 2 || i == 5) {
				amount += 10;
			}
			// Ranks 3,4 grant +5% increased Block
			if (i == 3 || i == 4) {
				amount += 5;
			}
		}
		
		// Create the block Condition with the correct block Status Effect based on the numbers calculated
		this.selfBlock = new Condition("Hold It Right There!: Self Block Bonus", -1);
		this.selfBlock.makePermanent();
		this.selfBlock.setSource(this.owner);
		
		StatusEffect blockBonus = new StatusEffect(StatVersion.BLOCK, amount, StatusEffectType.INCOMING);
		DualRequirement req = (Character withEffect, Character other) -> {
			// Prompt controller if blocking for an ally
			System.out.println("Blocking for ally? Y or N");
			return BattleSimulator.getInstance().askYorN();
		};
		blockBonus.setDualRequirement(req);
		
		this.selfBlock.addStatusEffect(blockBonus);
		
		// Since it is permanent, this Condition will start attached to the Character if necessary (Rank 2 or above)
		if (this.rank >= 2) {
			this.owner.addCondition(this.selfBlock);
		}
	}
	
	// Provides the Halt condition with increased damage to halted enemies
	private void setEnemyHalt() {
		// Find damage bonus amount based on rank
		int amount = 0;
		if (this.rank == 4) {
			amount = 20;
		}
		if (this.rank == 5) {
			amount = 30;
		}
		
		// Create the Halt Damage Condition with the correct Status Effect(s) based on the numbers calculated
		this.enemyHalt = new Condition("Hold It Right There!: Halted - Enemy Damage Bonus", 2);
		this.enemyHalt.setSource(this.owner);
		
		StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.INCOMING);
		damageBonus.makeAffectOther();
		
		
		// At rank 5, bonus damage from the owner only is increased to an amount of 50
		if (this.rank == 5) {
			// Creates additional statusEffect for owner only
			StatusEffect ownerDamageBonus = new StatusEffect(StatVersion.DAMAGE, 50, StatusEffectType.INCOMING);
			ownerDamageBonus.makeAffectOther();
			DualRequirement ownerReq = (Character withEffect, Character other) -> {
				return other.equals(this.owner);
			};
			ownerDamageBonus.setDualRequirement(ownerReq);
			this.enemyHalt.addStatusEffect(ownerDamageBonus);
			
			// Creates a requirement for the original to exclude the owner (so both do not apply)
			DualRequirement req = (Character withEffect, Character other) -> {
				return !(other.equals(this.owner));
			};
			damageBonus.setDualRequirement(req);
		}
		
		this.enemyHalt.addStatusEffect(damageBonus);
	}
	
	// Get methods for additional effects and owner/source
	public Condition getSelfBlockCondition() {
		return this.selfBlock;
	}
	public Condition getEnemyHaltCondition() {
		return this.enemyHalt;
	}
}
