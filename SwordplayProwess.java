package WyattWitemeyer.WarOfGillysburg;

class BleedDOT extends DamageOverTime {
	// Holds the Bleed's attack
	private Attack bleedAtk;
	
	// Constructor
	public BleedDOT(SteelLegionWarrior source, Character affected, int duration, int damage, boolean isCrit, int vorpalChance, double vorpalMultiplier) {
		super(source, "Swordplay Prowess: Bleed Effect", duration);
		AttackBuilder bld = new AttackBuilder()
				.attacker(source)
				.defender(affected)
				.type(Attack.DmgType.BLEED)
				.range(Attack.RangeType.OTHER)
				.isAOE()
				.ignoresArmor()
				.usesFlatDamage()
				.flatDamage(damage);
		if (isCrit) {
			bld.guaranteedCrit().vorpal(vorpalChance, vorpalMultiplier);
		}
		
		this.bleedAtk = bld.build();
	}
	
	// For the bleed effect, activating executes the attack
	@Override
	public void executeDOT() {
		this.bleedAtk.execute();
	}
	
	// Returns the display line (without the tabs) of the Bleed DOT effect in the Condition
	@Override
	public String getDOTString() {
		return "Bleeding: Deals ~" + this.bleedAtk.getFlatDamageAmount() + " damage at the beginning of each turn.";
	}
}

public class SwordplayProwess extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables
	private int bleedDuration;
	private int bleedVorpalChance;
	private double bleedVorpalMultiplier;
	private int bonusDamageStat;
	private int bonusArmorPiercingStat;
	private Condition empoweredEffect;
	private Condition empoweredText;
	
	// Helper variables to determine when an empowered effect occurs
	private int numAttacks;
	private boolean attackedInTurn;
	private boolean usingEmpowered;
	private boolean countedAOE;
	
	// Constructor
	public SwordplayProwess(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \\\"Swordplay Prowess\\\"\"", source, rank);
		this.owner = source;
		
		// Set the Bleed Scaler
		this.setScaler();
		
		// Sets the Duration of the bleed (always 3 unless rank 15, then 4)
		this.setBleedComponents();
		
		// Set the flat bonus stats from the Ability
		this.setStatBonuses();
		
		// Set the empowered condition of the ability
		this.setEmpoweredConditions();
		
		// Set helper variables to initial values
		this.numAttacks = 0;
		this.attackedInTurn = false;
		this.usingEmpowered = false;
		this.countedAOE = false;
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
	
	// Calculates the duration and vorpal effects of the bleed effect
	private void setBleedComponents() {
		// Initial values for the Duration and Vorpal effects of the bleed
		this.bleedDuration = 3;
		this.bleedVorpalChance = 0;
		this.bleedVorpalMultiplier = 1.0;
		
		// At rank 10, the vorpal effect is 50% chance to triple
		if (this.rank() >= 10) {
			this.bleedVorpalChance = 50;
			this.bleedVorpalMultiplier = 3.0;
		}
		// At rank 15, the duration increases to 4, and the vorpal effect is 75% chance to quadruple.
		if (this.rank() >= 15) {
			this.bleedDuration = 4;
			this.bleedVorpalChance = 75;
			this.bleedVorpalMultiplier = 4.0;
		}
	}
	
	// Calculates the flat stat bonuses for Damage and Armor Piercing
	private void setStatBonuses() {
		// Each stat starts at 0, but increases every rank (starting at Rank 1)
		this.bonusDamageStat = 0;
		this.bonusArmorPiercingStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1 grants +15 Damage and +2 Armor Piercing per rank
			if (walker == 1) {
				this.bonusDamageStat += 15;
				this.bonusArmorPiercingStat += 2;
			}
			// Ranks 2-4 grant +18 Damage and +3 Armor Piercing per rank
			else if (walker <= 4) {
				this.bonusDamageStat += 18;
				this.bonusArmorPiercingStat += 3;
			}
			// Rank 5 grants +25 Damage and +4 Armor Piercing per rank
			else if (walker == 5) {
				this.bonusDamageStat += 25;
				this.bonusArmorPiercingStat += 4;
			}
			// Ranks 6-9 grant +32 Damage and +5 Armor Piercing per rank
			else if (walker <= 9) {
				this.bonusDamageStat += 32;
				this.bonusArmorPiercingStat += 5;
			}
			// Rank 10 grants +50 Damage and +8 Armor Piercing per rank
			else if (walker == 10) {
				this.bonusDamageStat += 50;
				this.bonusArmorPiercingStat += 8;
			}
			// Ranks 11-14 grant +60 Damage and +10 Armor Piercing per rank
			else if (walker <= 14) {
				this.bonusDamageStat += 60;
				this.bonusArmorPiercingStat += 10;
			}
			// Rank 15 grants +175 Damage and +25 Armor Piercing
			else if (walker == 15) {
				this.bonusDamageStat += 175;
				this.bonusArmorPiercingStat += 25;
			}
		}
	}
	
	// Calculates the amounts and creates the condition for the empowered effect
	private void setEmpoweredConditions() {
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
		
		// Create the Condition that contains this effect permanently (-1) since it is consumed once used
		this.empoweredEffect = new Condition("Swordplay Prowess: Empowered Effect", -1);
		this.empoweredEffect.setSource(this.owner);
		this.empoweredEffect.addStatusEffect(dmgBonus);
		this.empoweredEffect.addStatusEffect(accBonus);
		this.empoweredEffect.addStatusEffect(critBonus);
		
		// Creates the text Condition that attaches to the Character and removes itself after a turn
		this.empoweredText = new Condition("Swordplay Prowess: Empowered", 1);
		this.empoweredText.setSource(this.getOwner());
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public int getBleedDuration() {
		return this.bleedDuration;
	}
	
	public int getBleedVorpalChance() {
		return this.bleedVorpalChance;
	}
	
	public double getBleedVorpalMultiplier() {
		return this.bleedVorpalMultiplier;
	}
	
	public int getDamageBonus() {
		return this.bonusDamageStat;
	}
	
	public int getArmorPiercingBonus() {
		return this.bonusArmorPiercingStat;
	}
	
	public Condition getEmpoweredCondition() {
		return this.empoweredEffect;
	}
	
	public Condition getEmpoweredTextCondition() {
		return this.empoweredText;
	}
	
	public void incrementAttacks() {
		this.numAttacks++;
		if (this.numAttacks > 3) {
			this.numAttacks = 3;
		}
	}
	
	public void resetAttacks() {
		this.numAttacks = 0;
	}
	
	public boolean isEmpowered() {
		return this.numAttacks >= 3;
	}
	
	// Overrides decrementTurnsRemaining to apply beginning of turn effects to check on the Empowered status.
	@Override
	public void decrementTurnsRemaining() {
		// Do the normal version of the function as needed
		super.decrementTurnsRemaining();
		
		// If the owner did not succeed in an attack after a complete turn, reset the total to zero
		if (!this.attackedInTurn) {
			this.resetAttacks();
		}
		
		// Reset helper booleans (except usingEmpowered) to initial states
		this.attackedInTurn = false;
		this.countedAOE = false;
		
		// If Empowered, gain the Empowered Condition (just display text) until the beginning of the next turn, so the Character know they are empowered on their turn.
		if (this.isEmpowered()) {
			this.getOwner().addCondition(this.getEmpoweredTextCondition());
		}
	}
	
	// Pre Attack Effects, sets empowered stuff, checks for empowered
	@Override
	public void applyPreAttackEffects(Attack atk) {
		// If the owner is the attacker:
		if (this.getOwner().equals(atk.getAttacker())) {
			// To create an empowered attack, the attack must be Targeted and the Empowered Effect should be ready
			if (atk.isTargeted() && this.isEmpowered()) {
				// Add the Empowered effect to the attack
				atk.addAttackerCondition(this.empoweredEffect);
				
				// Set the boolean for usingEmpowered to true
				this.usingEmpowered = true;
			}
		}
	}
	
	// Post Attack Effects, empowered add Bleed
	@Override
	public void applyPostAttackEffects(AttackResult atkRes) {
		// If the owner is the attacker:
		if (this.getOwner().equals(atkRes.getAttacker())) {
			// First, check to see if this was an Empowered attack
			if (this.usingEmpowered) {
				// First, future attacks are no longer using the Empowered Effect
				this.usingEmpowered = false;
				
				// If the attack hit, apply Bleed effect, and since the ability has been used, the Condition can be removed and the total attacks are reset to zero
				if (atkRes.didHit()) {
					// Apply Bleed with extra effects if the Empowered attack critically struck
					if (atkRes.didCrit()) {
						BleedDOT critBleed = new BleedDOT(this.getOwner(), atkRes.getDefender(), this.getBleedDuration(), (int)Math.round(atkRes.getDamageDealt() * this.getScaler()), true, this.getBleedVorpalChance(), this.getBleedVorpalMultiplier());
						atkRes.getDefender().addCondition(critBleed);
					}
					else {
						BleedDOT normBleed = new BleedDOT(this.getOwner(), atkRes.getDefender(), this.getBleedDuration(), (int)Math.round(atkRes.getDamageDealt() * this.getScaler()), false, 0, 1.0);
						atkRes.getDefender().addCondition(normBleed);
					}
					
					// Reset total attacks to zero and remove the display condition
					this.resetAttacks();
					this.getOwner().removeCondition(this.empoweredText);
				}
				return;
			}
			
			// If the attack was AOE and the ability is below rank 5, each AOE attack in a single turn only counts as one total (so do nothing if these are met)
			if (this.rank() < 5 && atkRes.isAOE() && this.countedAOE) {
				return;
			}
			
			// If the attack hit, increment the numAttacks and set the attackedInTurn to true
			if (atkRes.didHit()) {
				this.incrementAttacks();
				this.attackedInTurn = true;
				
				if (atkRes.isAOE()) {
					this.countedAOE = true;
				}
				
				// If the attack is Targeted, and it Critically Hit, apply a normal use of the Bleed effect
				if (atkRes.isTargeted() && atkRes.didCrit()) {
					BleedDOT normBleed = new BleedDOT(this.getOwner(), atkRes.getDefender(), this.getBleedDuration(), (int)Math.round(atkRes.getDamageDealt() * this.getScaler()), false, 0, 1.0);
					atkRes.getDefender().addCondition(normBleed);
				}
			}
		}
	}
}
