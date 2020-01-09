package WyattWitemeyer.WarOfGillysburg;

public class HaHaHaYouCantKillMe extends Ability {
	// Additional variables for the Ability
	private Condition selfArmorBonus;
	private Condition enemyTauntEffect;
	private Condition allyDamageBonus;
	
	
	// NOTE: This Ability does not yet implement the Character Requirement for decreased Damage.
	public HaHaHaYouCantKillMe(Character source, int rank) {
		// Initialize all Ability variables to defaults
		super();
		this.owner = source;
		this.name = "Ability 5 (ULTIMATE): \"HaHaHa You Can't Kill Me!\"";
		
		// Set the rest based on rank
		this.rank = rank;
		
		// Set the additional effects of the Ability
		this.setSelfArmorBonus();
		this.setEnemyTauntEffect();
		this.setAllyDamageBonus();
	}
	
	// Calculates the values for the additional Effects for this Ability
	public void setSelfArmorBonus() {
		// At rank 1, this increases Armor by 25% for 2 turns
		int amount = 25;
		int duration = 2;
		// Increased to 30% at rank 2
		if (this.rank == 2) {
			amount = 30;
		}
		// Increased to 50% at rank 3 for 3 turns
		else if (this.rank >= 3) {
			amount = 50;
			duration = 3;
		}
		StatusEffect armorBonus = new StatusEffect(StatVersion.ARMOR, amount, StatusEffectType.INCOMING);
		
		// Creates the condition with armor bonus as needed
		this.selfArmorBonus = new Condition("HaHaHa You Can't Kill Me!: Self Armor Bonus", duration);
		this.selfArmorBonus.setSource(this.owner);
		this.selfArmorBonus.addStatusEffect(armorBonus);
	}
	// Sets the Taunt Condition (with the reduced damage status effect as necessary)
	public void setEnemyTauntEffect(Character enemy) {
		int duration = 2;
		if (this.rank >= 3) {
			duration = 3;
		}
		this.enemyTauntEffect = new Condition("HaHaHa You Can't Kill Me!: Taunt Effect", duration);
		this.enemyTauntEffect.setSource(this.owner);
		
		// Adds reduced damage at rank 2 based on enemy affected
		if (this.rank >= 2) {
			// Default of 50% reduction for Normal and Advanced enemies (Level = 1, 2)
			int amount = 50;
			// 25% reduction for Elite enemies (Level = 3)
			if (enemy.getLevel() == 3) {
				amount = 25;
			}
			// 0% reduction for Boss enemies
			else if (enemy.getLevel() == 4) {
				amount = 0;
			}
			StatusEffect damageReduction = new StatusEffect(StatVersion.DAMAGE, -amount, StatusEffectType.OUTGOING);
			
			this.enemyTauntEffect.addStatusEffect(damageReduction);
		}
	}
	public void setEnemyTauntEffect() {
		this.setEnemyTauntEffect(Character.EMPTY);
	}
	
	public void setAllyDamageBonus() {
		// Only occurs at rank 3
		int amount = 0;
		int duration = 0;
		if (this.rank >= 3) {
			amount = 50;
			duration = 1;
		}
		StatusEffect damageBonus = new StatusEffect(StatVersion.DAMAGE, amount, StatusEffectType.OUTGOING);
		
		// Creates the condition with the damage bonus as needed
		this.allyDamageBonus = new Condition("HaHaHa You Can't Kill Me: Damage Bonus", duration);
		this.allyDamageBonus.setSource(this.owner);
		this.allyDamageBonus.addStatusEffect(damageBonus);
	}
	
	// Get methods for the additional variables of this Ability
	public Condition getSelfArmorBonus() {
		return this.selfArmorBonus;
	}
	public Condition getEnemyTauntEffect(Character enemy) {
		this.setEnemyTauntEffect(enemy);
		return this.enemyTauntEffect;
	}
	public Condition getAllyDamageBonus() {
		return this.allyDamageBonus;
	}
}
