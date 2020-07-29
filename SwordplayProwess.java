package WyattWitemeyer.WarOfGillysburg;

class BleedDOT extends DamageOT {
	// Holds the Bleed's attack
	private Attack bleedAtk;
	
	// Constructor
	public BleedDOT(SteelLegionWarrior source, Character affected, int duration, int damage) {
		super(source, "Swordplay Prowess: Bleed Effect", duration);
		this.bleedAtk = new AttackBuilder()
				.attacker(source)
				.defender(affected)
				.type(Attack.DmgType.BLEED)
				.isAOE()
				.ignoresArmor()
				.usesFlatDamage()
				.flatDamage(damage)
				.build();
	}
	
	// For the bleed effect, activating executes the attack
	@Override
	public void executeDOT() {
		this.bleedAtk.execute();
	}
	
	// Returns the display line (without the tabs) of the Bleed DOT effect in the Condition
	@Override
	public String getDOTString() {
		return "Bleeding: Deals ~" + this.bleedAtk.getFlatDamageAmount() + " damage at the beginning of each round.";
	}
}

public class SwordplayProwess extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables
	private int bonusDamageStat;
	private int bonusArmorPenetrationStat;
	private Condition empoweredEffect;
	
	// Constructor
	public SwordplayProwess(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \\\"Swordplay Prowess\\\"\"", source, rank);
		this.owner = source;
		
		// Set the Bleed Scaler
		this.setScaler();
		
		// Set the flat bonus stats from the Ability
		this.setStatBonuses();
		
		// Set the empowered condition of the ability
		this.setEmpoweredCondition();
	}
	// An additional constructor used to only calculate the bonus stats before a SteelLegionWarrior is created
	public SwordplayProwess(int rank) {
		super("No Owner: Swordplay Prowess", Character.EMPTY, rank);
		
		this.setStatBonuses();
	}
	
	// Calculates the scaler for the bleed effect of the Ability
	private void setScaler() {
		// At rank 1, this scaler starts at .25x
		this.scaler = .25;
		
		for (int walker = 2; walker <= this.rank(); walker++) {
			// Ranks 2-5 grant +.05x per rank
			if (walker <= 5) {
				this.scaler += .05;
			}
			// Ranks 6-10 grant +.1x per rank
			else if (walker <= 10) {
				this.scaler += .1;
			}
			// Ranks 11-14 grant +.2x per rank
			else if (walker <= 14) {
				this.scaler += .2;
			}
			// Rank 15 grants +.6x
			else if (walker == 15) {
				this.scaler += .6;
			}
		}
	}
	
	// Calculates the flat stat bonuses for Damage and Armor Piercing
	private void setStatBonuses() {
		// Each stat starts at 0, but increases every rank (starting at Rank 1)
		this.bonusDamageStat = 0;
		this.bonusArmorPenetrationStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Ranks 1-5 grant +15 Damage and +3 Armor Piercing per rank
			if (walker <= 5) {
				this.bonusDamageStat += 15;
				this.bonusArmorPenetrationStat += 3;
			}
			// Ranks 6-10 grant +32 Damage and +5 Armor Piercing per rank
			else if (walker <= 10) {
				this.bonusDamageStat += 32;
				this.bonusArmorPenetrationStat += 5;
			}
			// Ranks 11-14 grant +60 Damage and +10 Armor Piercing per rank
			else if (walker <= 14) {
				this.bonusDamageStat += 60;
				this.bonusArmorPenetrationStat += 10;
			}
			// Rank 15 grants +175 Damage and +25 Armor Piercing
			else if (walker == 15) {
				this.bonusDamageStat += 175;
				this.bonusArmorPenetrationStat += 25;
			}
		}
	}
	
	// Calculates the amounts and creates the condition for the empowered effect
	private void setEmpoweredCondition() {
		// Declare each stat's starting amount (at rank 0, each stat technically starts at 10%.
		int damageAmount = 10;
		int accuracyAmount = 10;
		int critAmount = 10;
		
		for (int walker = 0; walker < this.rank(); walker++) {
			// Ranks 1-5 grant +1% damage, accuracy, and crit (crit is flat though)
			if (walker <= 5) {
				damageAmount += 1;
				accuracyAmount += 1;
				critAmount += 1;
			}
			// Ranks 6-10 grant +3% damage, +2% accuracy, and +3 crit
			else if (walker <= 10) {
				damageAmount += 3;
				accuracyAmount += 2;
				critAmount += 3;
			}
			// Ranks 11-14 grant +4% damage, +3% accuracy, and +5 crit
			else if (walker <= 14) {
				damageAmount += 4;
				accuracyAmount += 3;
				critAmount += 5;
			}
			// Rank 15 grants +4% damage, +3% accuracy, and +10 crit
			else if (walker == 15) {
				damageAmount += 4;
				accuracyAmount += 3;
				critAmount += 10;
			}
		}
		
		// Create the Status Effects
		StatusEffect dmgBonus = new StatusEffect(Stat.Version.DAMAGE, damageAmount, StatusEffect.Type.OUTGOING);
		dmgBonus.makePercentage();
		
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, accuracyAmount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		// Create the Condition that contains this effect permanently (-1) since it is consumed once used (implemented in Steel Legion Warrior Class)
		this.empoweredEffect = new Condition("Swordplay Prowess: Empowered", -1);
		this.empoweredEffect.setSource(this.owner);
		this.empoweredEffect.addStatusEffect(dmgBonus);
		this.empoweredEffect.addStatusEffect(accBonus);
		this.empoweredEffect.addStatusEffect(critBonus);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public int getDamageBonus() {
		return this.bonusDamageStat;
	}
	
	public int getArmorPiercingBonus() {
		return this.bonusArmorPenetrationStat;
	}
	
	public Condition getEmpoweredCondition() {
		return this.empoweredEffect;
	}
}
