package WyattWitemeyer.WarOfGillysburg;
import java.util.*;


//Passive Abilities:
//Unique Passive Ability: "Vengeance Strike"
class VengeanceStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables needed for effects of the Ability
	public HashMap<Character,Integer> counter;
	
	private Condition enemyDamageReduction;
	
	// Constructor
	public VengeanceStrike(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Unique Passive Ability: \\\"Vengeance Strike\\\"\"", source, rank);
		this.owner = source;
		
		// Set scaler and counter
		this.setScaler();
		counter = new HashMap<Character,Integer>();
		
		// Calculate the additional Staus Effects
		this.setEnemyDamageReduction();
	}
	
	// Calculates the basic values for this Ability
	private void setScaler() {
		// Checks based on this Ability's rank
		if (this.rank() >= 3) {
			this.scaler = 1.5;
		}
		else if (this.rank() == 2) {
			this.scaler = 1.3;
		}
		else {
			this.scaler = 1.2;
		}
	}
	
	// Functions for setting Conditions
	private void setEnemyDamageReduction() {
		// Set the amount of Damage Reduction
		int amount = 0;
		// Checks based on this Ability's rank (0 at rank 1, increases to 10% at rank 2, then by 5% each rank
		for (int walker = 2; walker <= this.rank(); walker++) {
			if (walker == 2) {
				amount += 10;
			}
			else if (walker <= 5) {
				amount += 5;
			}
		}
		
		// Create the Dual Requirement needed for it to only be in effect vs the owner
		DualRequirement isOwner = (Character withEffect, Character other) -> {
			return other.equals(this.owner);
		};
		
		// Create the Status Effect
		StatusEffect dmgRed = new StatusEffect(Stat.Version.DAMAGE, -amount, StatusEffect.Type.OUTGOING);
		dmgRed.makePercentage();
		dmgRed.setDualRequirement(isOwner);
		
		// Create the Condition that contains this effect for only 1 turn (ends end of turn)
		this.enemyDamageReduction = new Condition("Vengeance Strike: Damage Reduction", 1);
		this.enemyDamageReduction.setSource(this.owner);
		this.enemyDamageReduction.makeEndOfTurn();
		this.enemyDamageReduction.addStatusEffect(dmgRed);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public Condition getEnemyDamageReduction() {
		return new Condition(this.enemyDamageReduction);
	}
	
	// Clears the counter map (should occur at the beginning of each turn for ranks 1 and 2)
	public void clearCounterMap() {
		this.counter.clear();
	}
	
	// Creates an execute function to make the Vengeance Strike attack, applying the correct Conditions
	public void execute(Enemy enemy) {
		// Start the build of the attack
		AttackBuilder VenStrBuilder = new AttackBuilder()
				.attacker(this.getOwner())
				.defender(enemy)
				.type(Attack.DmgType.SLASHING)
				.range(Attack.RangeType.MELEE)
				.isTargeted()
				.scaler(this.getScaler())
				.addSuccessCondition(this.getEnemyDamageReduction());
		
		// At rank 5, Vengeance Strike has 30% lifesteal DE maybe just do in Flip Version?
		if (this.rank() >= 5) {
			VenStrBuilder.lifestealPercentage(30);
		}
		
		// 30% Chance to add a stun success Condition if Warrior's Might is at least rank 15
		if (this.getOwner().getAbilityRank(SteelLegionWarrior.AbilityNames.WarriorsMight) >= 15) {
			Dice percent = new Dice(100);
			if (percent.roll() <= 30) {
				Stun stunEffect = new Stun("Warrior's Might (Vengeance Strike): Stun", 1);
				stunEffect.makeEndOfTurn();
				VenStrBuilder.addSuccessCondition(stunEffect);
			}
		}
		
		// Build the attack
		Attack VenStr = VenStrBuilder.build();
		
		// If the rank is 4 or 5, the Ability uses a version of Flip Strike for the attack
		if (this.rank() >= 4) {
			this.getOwner().useVengeanceFlipStrike(VenStr);
		}
		else {
			if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
				VenStr = this.getOwner().getDeflectionVersion(VenStr);
			}
			VenStr.execute();
		}
		
		// Then, add the enemy to the counter map
		if (this.counter.containsKey(enemy)) {
			this.counter.put(enemy, this.counter.get(enemy) + 1);
		}
		else {
			this.counter.put(enemy, 1);
		}
	}
	
	// Applies the post attack effects of this Ability
	@Override
	public void applyPostAttackEffects(AttackResult atkRes) {
		// When the owner is defending, the ability can automatically occur:
		if (this.getOwner().equals(atkRes.getDefender())) {
			// The attack cannot occur if it is the owner's turn, if the attack missed, nor if the person that attacked was not an enemy
			if (this.getOwner().inTurn() || !atkRes.didHit() || !(atkRes.getAttacker() instanceof Enemy)) {
				return;
			}
			
			// Keep track of whether or not the attack should occur (as can be overridden by user)
			boolean shouldAttack = true;
			
			// At rank 1, the attack must be melee, and it can only be used once per turn (and shouldn't be used unless the counter map is empty)
			if (this.rank() == 1 && !atkRes.getRangeType().equals(Attack.RangeType.MELEE) && !this.counter.isEmpty()) {
				shouldAttack = false;
			}
			// At rank 2, the attack must be melee, and it can only be used once per enemy
			if (this.rank() == 2 && !atkRes.getRangeType().equals(Attack.RangeType.MELEE) && this.counter.containsKey(atkRes.getAttacker()) && this.counter.get(atkRes.getAttacker()) != 1) {
				shouldAttack = false;
			}
			// At rank 3, the attack has no limitations except Ranged enemies out of range.
			
			// Tell the user if the attack should(n't) occur, and prompt for usage
			String negation = shouldAttack? "" : "NOT ";
			System.out.println(this.getOwner().getName() + " should " + negation + "use Vengeance Strike. Proceed with attack?");
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
			
			// At this point the attack occurs.
			this.execute((Enemy)atkRes.getAttacker());
		}
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\t" + this.getEnemyDamageReduction().toString();
		return ret;
	}
}

// Base Passive Ability 1: "Swordplay Prowess"
// Makes use of Bleed effect first defined here
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
	public BleedDOT(BleedDOT copy) {
		super(copy);
		this.bleedAtk = new Attack(copy.bleedAtk);
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

//The class Swordplay Prowess itself
class SwordplayProwess extends Ability {
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
		return new Condition(this.empoweredEffect);
	}
	
	public Condition getEmpoweredTextCondition() {
		return new Condition(this.empoweredText);
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
				atk.addAttackerCondition(this.getEmpoweredCondition());
				
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
					this.getOwner().removeCondition(this.getEmpoweredTextCondition());
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
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBleed Duration: " + this.getBleedDuration();
		ret += this.getBleedVorpalChance() > 0? ("\n\tBleed Vorpal Chance: " + this.getBleedVorpalChance() + "\n\tBleed Vorpal Multiplier: " + this.getBleedVorpalMultiplier()) : "";
		ret += "\n\tBonus Damage Stat: " + this.getDamageBonus();
		ret += "\n\tBonus Armor Piercing Stat: " + this.getArmorPiercingBonus();
		ret += "\n\t" + this.getEmpoweredCondition();
		return ret;
	}
}

// Base Passive Ability 2: "Warrior's Might"
class WarriorsMight extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables
	private int bonusHealthStat;
	private int bonusArmorStat;
	private int bonusThreatStat;
	private int bonusDamageStat;
	private Stun stun;
	
	// Constructor
	public WarriorsMight(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Warrior's Might\"", source, rank);
		this.owner = source;
		
		// Calculate the additional stat amounts
		this.setStatBonuses();
		
		// Calculate the stun effect (is always the same: 1 turn, end of turn)
		this.stun = new Stun("Warrior's Might (Vengeance Strike): Stun", 1);
		this.stun.makeEndOfTurn();
	}
	// An additional constructor to Calculate the bonus stats in order to create a SteelLegionWarrior
	public WarriorsMight(int rank) {
		super("No Owner: Warrior's Might", Character.EMPTY, rank);
		
		this.setStatBonuses();
	}
	
	// Calculates the flat stat bonuses for Health, Armor, and Threat
	private void setStatBonuses() {
		// Each stat starts at default of 0
		this.bonusHealthStat = 0;
		this.bonusArmorStat = 0;
		this.bonusThreatStat = 0;
		this.bonusDamageStat = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Ranks 1-2 grant +90 Health and +5 Armor per rank
			if (walker <= 2) {
				this.bonusHealthStat += 90;
				this.bonusArmorStat += 5;
			}
			// Ranks 3-5 grant +175 Health, +7 Armor, and +3 Threat per rank
			else if (walker <= 5) {
				this.bonusHealthStat += 175;
				this.bonusArmorStat += 7;
				this.bonusThreatStat += 3;
			}
			// Ranks 6-10 grant +285 Health, +8 Armor, +3 Threat, and +20 Damage per rank
			else if (walker <= 10) {
				this.bonusHealthStat += 285;
				this.bonusArmorStat += 8;
				this.bonusThreatStat += 3;
				this.bonusDamageStat += 20;
			}
			// Ranks 11-14 grant 575 Health, +10 Armor, +5 Threat, and +35 Damage per rank
			else if (walker <= 14) {
				this.bonusHealthStat += 575;
				this.bonusArmorStat += 10;
				this.bonusThreatStat += 5;
				this.bonusDamageStat += 35;
			}
			// Rank 15 grants +1225 Health, +15 Armor, +10 Threat, and +100 Damage
			else if (walker == 15) {
				this.bonusHealthStat += 1225;
				this.bonusArmorStat += 15;
				this.bonusThreatStat += 10;
				this.bonusDamageStat += 100;
			}
		}
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public int getHealthBonus() {
		return this.bonusHealthStat;
	}
	
	public int getArmorBonus() {
		return this.bonusArmorStat;
	}
	
	public int getThreatBonus() {
		return this.bonusThreatStat;
	}
	
	public int getDamageBonus() {
		return this.bonusDamageStat;
	}
	
	public Stun getStun() {
		return new Stun(this.stun);
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tBonus Health Stat: " + this.getHealthBonus();
		ret += "\n\tBonus Armor Stat: " + this.getArmorBonus();
		ret += this.getThreatBonus() > 0? ("\n\tBonus Threat Stat: " + this.getThreatBonus()) : "";
		ret += this.getDamageBonus() > 0? ("\n\tBonus Damage Stat: " + this.getDamageBonus()) : "";
		return ret;
	}
}

// Base Passive Ability 3: "Agile Fighter"
class AgileFighter extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional variables
	private double maxHealthScaler;
	private double missHealthScaler;
	
	private int speedBonus;
	
	private boolean saveResponse;
	private boolean hasResponseSave;
	
	private Condition basePreAttackBonus;
	private Condition abilityPreAttackBonus;
	private Condition baseBlockBonus;
	private Condition abilityBlockBonus;
	
	// Constructor
	public AgileFighter(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Base Passive Ability: \"Agile Fighter\"", source, rank);
		this.owner = source;
		this.saveResponse = false;
		
		// Set the healing scalers of the ability, default scaler will be the max health scaler
		this.setHealingScalers();
		this.scaler = this.maxHealthScaler;
		
		// Calculate and set the additional Speed and Condition effects
		this.setSpeedBonus();
		this.setBonuses();
	}
	// An additional constructor used to only calculate the bonus stats before a SteelLegionWarrior is created
	public AgileFighter(int rank) {
		super("No Owner: Agile Fighter", Character.EMPTY, rank);
		
		this.setSpeedBonus();
	}
	
	// Calculates the healing scalers
	private void setHealingScalers() {
		// Initialize both healing scalers to 0
		this.maxHealthScaler = 0;
		this.missHealthScaler = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1, the max health scaler is at 1%, and the missing health scaler is not yet active
			if (walker == 1) {
				this.maxHealthScaler += .01;
			}
			// Ranks 2-4 grant +.1% to the max health scaler, and the missing health scaler is not yet active
			else if (walker <= 4) {
				this.maxHealthScaler += .001;
			}
			// Rank 5 grants +.2% to the max health scaler, and the missing health scaler activates starting at 1%
			else if (walker == 5) {
				this.maxHealthScaler += .002;
				this.missHealthScaler += .01;
			}
			// Ranks 6-9 grant +.15% to the max health scaler, and +.1% to the missing health scaler
			else if (walker <= 9) {
				this.maxHealthScaler += .0015;
				this.missHealthScaler += .001;
			}
			// Rank 10 grants +.2% to the max health scaler, and +.1% to the missing health scaler
			else if (walker == 10) {
				this.maxHealthScaler += .002;
				this.missHealthScaler += .001;
			}
			// Ranks 11-14 grant +.2% to the missing health scaler (max unaffected)
			else if (walker <= 14) {
				this.missHealthScaler += .002;
			}
			// Rank 15 grants +.2% to the max health scaler, and +.2% to the missing health scaler
			else if (walker == 15) {
				this.maxHealthScaler += .002;
				this.missHealthScaler += .002;
			}
		}
	}
	
	// Calculates and sets the amount of bonus base Speed the Ability gives
	private void setSpeedBonus() {
		// Initialize the bonus to 0
		this.speedBonus = 0;
		
		// At rank 5 the Ability grants +1 Speed
		if (this.rank() >= 5) {
			this.speedBonus += 1;
		}
		// Rank 10 grants +2 more Speed
		if (this.rank() >= 10) {
			this.speedBonus += 2;
		}
		// Rank 15 grants +3 more Speed
		if (this.rank() >= 15) {
			this.speedBonus += 3;
		}
	}
	
	// Calculates and creates the additional Condition effects
	// Calculate the base accuracy and crit bonus amounts so other calculations are easier
	private double calcBaseAccAmt() {
		// Initialize the amount to 0
		double amount = 0;
		
		for (int walker = 1; walker <= this.rank(); walker++) {
			// Rank 1, starts at 10%
			if (walker == 1) {
				amount = 10;
			}
			// Ranks 2-4 grant +1% Accuracy
			else if (walker <= 4) {
				amount += 1;
			}
			// Ranks 5-10 grant +2% Accuracy
			else if (walker <= 10) {
				amount += 2;
			}
			// Ranks 11-15 grant +5% Accuracy
			else if (walker <= 15) {
				amount += 5;
			}
		}
		
		return amount;
	}
	private double calcCritAmount() {
		// Amount for crit is the same for each version, so this calculates it so we don't code it twice. Starts at rank 11
		double amount = 0;
		for (int walker = 11; walker <= this.rank(); walker++) {
			// Ranks 11-14 grant +5% crit
			if (walker <= 14) {
				amount += 5;
			}
			// Rank 15 grants +10% crit
			else if (walker == 15) {
				amount += 10;
			}
		}
		
		return amount;
	}
	
	// Makes each Condition
	private void setBonuses() {
		// Declare the starting amounts
		double accAmount = this.calcBaseAccAmt();
		double blkAmount = accAmount/2.0;
		double critAmount = this.calcCritAmount();
		
		// The amounts are increased if an Ability was used, though this requires an additional condition
		double abilityAccAmt = accAmount;
		double abilityBlkAmt = blkAmount;
		if (this.rank() >= 10) {
			// Rank 15 grants +50% effectiveness
			if (this.rank() == 15) {
				abilityAccAmt *= 1.5;
				abilityBlkAmt *= 1.5;
			}
			// At ranks 10-14 it grants +30% effectiveness
			else {
				abilityAccAmt *= 1.3;
				abilityBlkAmt *= 1.3;
			}
		}
		
		// Create the Status Effects
		StatusEffect accBonus = new StatusEffect(Stat.Version.ACCURACY, accAmount, StatusEffect.Type.OUTGOING);
		accBonus.makePercentage();
		StatusEffect abilityAccBonus = new StatusEffect(Stat.Version.ACCURACY, abilityAccAmt, StatusEffect.Type.OUTGOING);
		abilityAccBonus.makePercentage();
		StatusEffect blkBonus = new StatusEffect(Stat.Version.BLOCK, blkAmount, StatusEffect.Type.OUTGOING);
		blkBonus.makePercentage();
		StatusEffect abilityBlkBonus = new StatusEffect(Stat.Version.BLOCK, abilityBlkAmt, StatusEffect.Type.OUTGOING);
		abilityBlkBonus.makePercentage();
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		
		// Create all the Conditions
		// Create the Base Pre Attack Condition (duration 0 since used for 1 attack) and add the Accuracy status effect (only add the critical effect if rank 11+)
		this.basePreAttackBonus = new Condition("Agile Fighter: Pre-Attack Bonus", 0);
		this.basePreAttackBonus.setSource(this.owner);
		this.basePreAttackBonus.addStatusEffect(accBonus);
		if (this.rank() >= 11) {
			this.basePreAttackBonus.addStatusEffect(critBonus);
		}
		
		// Create the Ability Pre Attack Condition (duration 0 since used for 1 attack) and add the Accuracy status effect (only add the critical effect if rank 11+)
		this.abilityPreAttackBonus = new Condition("Agile Fighter: Pre-Attack Bonus", 0);
		this.abilityPreAttackBonus.setSource(this.owner);
		this.abilityPreAttackBonus.addStatusEffect(abilityAccBonus);
		if (this.rank() >= 11) {
			this.abilityPreAttackBonus.addStatusEffect(critBonus);
		}
		
		// Create the Base Block Condition with duration of 1
		this.baseBlockBonus = new Condition("Agile Fighter: Block Bonus", 1);
		this.baseBlockBonus.setSource(this.owner);
		this.baseBlockBonus.addStatusEffect(blkBonus);
		
		// Create the Ability Block Condition with duration of 1
		this.abilityBlockBonus = new Condition("Agile Fighter: Block Bonus", 1);
		this.abilityBlockBonus.setSource(this.owner);
		this.abilityBlockBonus.addStatusEffect(abilityBlkBonus);
	}
	
	// Get methods for the Ability
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public double getMaxHealthScaler() {
		return this.maxHealthScaler;
	}
	
	public double getMissingHealthScaler() {
		return this.missHealthScaler;
	}
	
	public int getSpeedBonus() {
		return this.speedBonus;
	}
	
	public Condition getBasePreAttackBonus() {
		return new Condition(this.basePreAttackBonus);
	}
	
	public Condition getAbilityPreAttackBonus() {
		return new Condition(this.abilityPreAttackBonus);
	}
	
	public Condition getBaseBlockBonus() {
		return new Condition(this.baseBlockBonus);
	}
	
	public Condition getAbilityBlockBonus() {
		return new Condition(this.abilityBlockBonus);
	}
	
	// Overrides decrementsTurnsRemaining and endTurnEffect to also reset the saved response to false at the beginning and end of each turn
	@Override
	public void decrementTurnsRemaining() {
		super.decrementTurnsRemaining();
		this.saveResponse = false;
		this.hasResponseSave = false;
	}
	@Override
	public void endTurnEffects() {
		super.endTurnEffects();
		this.saveResponse = false;
		this.hasResponseSave = false;
	}
	
	// Applies the pre-attack bonus when prompted, and heals the respective amount
	@Override
	public void applyPreAttackEffects(Attack atk) {
		// If the owner is the attacker
		if (this.getOwner().equals(atk.getAttacker())) {
			// Prompt if the Ability should activate when there is no current save of response
			boolean savedThisCall = this.hasResponseSave;
			if (!this.hasResponseSave) {
				System.out.println("Did " + this.getOwner().getName() + " move and attack?");
				
				// Save the response
				this.saveResponse = BattleSimulator.getInstance().askYorN();
				this.hasResponseSave = true;
			}
			
			// If not, we are done
			if (!this.saveResponse) {
				return;
			}
			
			// Otherwise, above rank 10 prompt for Ability usage to apply the correct pre-attack bonus
			if (this.rank() >= 10) {
				// If an Ability is used apply Ability version
				System.out.println("Was an Ability used?");
				if (BattleSimulator.getInstance().askYorN()) {
					atk.addAttackerCondition(this.getAbilityPreAttackBonus());
					this.getOwner().addCondition(this.getAbilityBlockBonus());
				}
				// If no Ability used, apply base
				else {
					atk.addAttackerCondition(this.getBasePreAttackBonus());
					this.getOwner().addCondition(this.getBaseBlockBonus());
				}
			}
			// Apply base before rank 10
			else {
				atk.addAttackerCondition(this.getBasePreAttackBonus());
				this.getOwner().addCondition(this.getBaseBlockBonus());
			}
			
			// The rest (healing) only occurs if this instance was not saved before this function was called, thus if it is saved, we are done.
			if (savedThisCall) {
				return;
			}
			
			// Then, heal the correct amount as prompted
			System.out.println("Enter the number of spaces travelled in each area for healing:");
			System.out.print("Ability: ");
			double abilitySpaces = BattleSimulator.getInstance().promptDouble();
			System.out.print("Regular: ");
			double regularSpaces = BattleSimulator.getInstance().promptDouble();
			System.out.println();
			
			// Restore health to the owner, first based on missing health if at least rank 5
			if (this.rank() >= 5) {
				this.getOwner().restoreHealth((int)Math.round(abilitySpaces * this.getMissingHealthScaler() * this.getOwner().getMissingHealth()));
				this.getOwner().restoreHealth((int)Math.round(regularSpaces * this.getMissingHealthScaler() / 2.0 * this.getOwner().getMissingHealth()));
			}
			// Then based on maximum health at all ranks
			this.getOwner().restoreHealth((int)Math.round(abilitySpaces * this.getMaxHealthScaler() * this.getOwner().getHealth()));
			this.getOwner().restoreHealth((int)Math.round(regularSpaces * this.getMaxHealthScaler() / 2.0 * this.getOwner().getHealth()));
		}
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tHealing Scaler (max health): " + this.getMaxHealthScaler();
		ret += this.getMissingHealthScaler() > 0? ("\n\tHealing Scaler (missing health): " + this.getMissingHealthScaler()) : "";
		ret += this.getSpeedBonus() > 0? ("\n\tBonus Speed Stat: " + this.getSpeedBonus()) : "";
		ret += "\n\t" + this.getBasePreAttackBonus();
		ret += this.rank() >= 10? ("\n\t" + this.getAbilityPreAttackBonus()) : "";
		ret += "\n\t" + this.getBaseBlockBonus();
		ret += this.rank() >= 10? ("\n\t" + this.getAbilityBlockBonus()) : "";
		return ret;
	}
}


// Basic Abilities:
// Ability 1: "Sweep"
class Sweep extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private Slow slow;
	
	// Constructor
	public Sweep(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 1: \"Sweep\"", source, rank);
		this.owner = source;
		
		// Set the damage scaler and Cooldown of the ability
		this.setCooldown();
		this.setScaler();
		
		// Calculate the slow effect
		this.setSlow();
	}
	
	// Sets the Cooldown
	private void setCooldown() {
		// Base Cooldown of 4, reduced to 3 at rank 7
		this.cooldown = 4;
		if (this.rank() >= 7) {
			this.cooldown = 3;
		}
		// The ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Calculates the damage scaler
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = .8;
		
		// Set the scaler based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				this.scaler = .8;
				break;
			case 2:
				this.scaler = 1;
				break;
			case 3:
				this.scaler = 1.2;
				break;
			case 4:
				this.scaler = 1.4;
				break;
			case 5:
				this.scaler = 1.5;
				break;
			case 6:
				this.scaler = 1.7;
				break;
			case 7:
				this.scaler = 1.8;
				break;
			case 8:
				this.scaler = 2;
				break;
			case 9:
				this.scaler = 2.3;
				break;
			case 10:
				this.scaler = 2.5;
				break;
		}
	}
	
	// Calculates and creates the slow condition
	private void setSlow() {
		// Declare the starting amount and duration
		int amount = 0;
		int duration = 0;
		
		// Calculate the slow amount (-2 for rank 3+, -3 for rank 10) and duration (1 for rank 3+, 2 for rank 5+)
		if (this.rank() >= 3) {
			amount = 2;
			duration = 1;
		}
		if (this.rank() >= 5) {
			duration = 2;
		}
		if (this.rank() == 10) {
			amount = 3;
		}
		
		// Create the slow effect
		this.slow = new Slow("Sweep: Slow", duration, amount);
		this.slow.setSource(this.owner);
		this.slow.makeEndOfTurn();
	}
	
	// Get method for the slow
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	public Slow getSlow() {
		return new Slow(this.slow);
	}
	
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Select enemies to attack
		System.out.println("Choose enemies hit by attack:");
    	LinkedList<Character> enemies = BattleSimulator.getInstance().targetMultiple();
        if (enemies.isEmpty()) {
        	return;
        }
        if (enemies.contains(Character.EMPTY)) {
        	enemies.clear();
        }
		
		// Before anything, put Sweep "on Cooldown"
		this.setOnCooldown();
		
		// Make the attack and apply the slow against all enemies affected
		for (Character enemy : enemies) {
			// Applies the attack
			Attack sweepAtk = new AttackBuilder()
					.attacker(this.owner)
					.defender(enemy)
					.isAOE()
					.scaler(this.getScaler())
					.type(Attack.DmgType.SLASHING)
					.range(Attack.RangeType.MELEE)
					.build();
			if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
				sweepAtk = this.getOwner().getDeflectionVersion(sweepAtk);
			}
			sweepAtk.execute();
			
			// Applies the slow
			enemy.addCondition(this.getSlow());
		}
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 3? ("\n\t" + this.getSlow()) : "";
		return ret;
	}
}

// Ability 2: "CHARGE!"
class Charge extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private double AOEScaler;
	private double targetedScaler;
	
	private int speedPercentage;
	private Condition targetedPreAttackBonus;
	
	// Constructor
	public Charge(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 2: \"CHARGE!\"", source, rank);
		this.owner = source;
		
		// Calculate and set the Cooldown and each scaler (base scaler set to targeted scaler)
		this.setCooldown();
		this.setTargetedScaler();
		this.setAOEScaler();
		
		// Calculate the additional Condition effects
		this.setSpeedPercentage();
		this.setTargetedPreAttackBonus();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 5, reduced to 4 at rank 10
		this.cooldown = 5;
		if (this.rank() == 10) {
			this.cooldown = 4;
		}
		// The Ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Calculates each scaler
	private void setTargetedScaler() {
		// Set a default value for the first rank
		this.targetedScaler = 1;
		
		// Set the targeted scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.targetedScaler = 1;
				break;
			case 2:
				this.targetedScaler = 1.2;
				break;
			case 3:
				this.targetedScaler = 1.3;
				break;
			case 4:
				this.targetedScaler = 1.5;
				break;
			case 5:
				this.targetedScaler = 1.5;
				break;
			case 6:
				this.targetedScaler = 1.7;
				break;
			case 7:
				this.targetedScaler = 1.8;
				break;
			case 8:
				this.targetedScaler = 2;
				break;
			case 9:
				this.targetedScaler = 2.3;
				break;
			case 10:
				this.targetedScaler = 2.5;
				break;
		}
		
		// Set the base scaler equal to this scaler for default purposes
		this.scaler = this.targetedScaler;
	}
	
	private void setAOEScaler() {
		// Set a default value for the first rank
		this.AOEScaler = .5;
		
		// Set the AOE scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.AOEScaler = .5;
				break;
			case 2:
				this.AOEScaler = .6;
				break;
			case 3:
				this.AOEScaler = .7;
				break;
			case 4:
				this.AOEScaler = .8;
				break;
			case 5:
				this.AOEScaler = 1;
				break;
			case 6:
				this.AOEScaler = 1.2;
				break;
			case 7:
				this.AOEScaler = 1.3;
				break;
			case 8:
				this.AOEScaler = 1.5;
				break;
			case 9:
				this.AOEScaler = 1.8;
				break;
			case 10:
				this.AOEScaler = 2;
				break;
		}
	}
	
	// Calculate the Speed percentage
	private void setSpeedPercentage() {
		// Set a default value for the first rank (50%)
		this.speedPercentage = 50;
		
		// Rank 3 increases the amount to 100%
		if (this.rank() >= 3) {
			this.speedPercentage = 100;
		}
		
		// Rank 7 increases the amount to 150%
		if (this.rank() >= 7) {
			this.speedPercentage = 150;
		}
		
		// Rank 10 increases the amount to 200%
		if (this.rank() == 10) {
			this.speedPercentage = 200;
		}
	}
	
	// Calculate and create the pre-attack Condition
	private void setTargetedPreAttackBonus() {
		// Declare the starting amounts at Rank 1
		int critAmount = 10;
		int apAmount = 0;
		
		// Calculate the crit amount based on rank
		// Rank 3 increases the amount to 15%
		if (this.rank() >= 3) {
			critAmount = 15;
		}
		// Rank 5 increases the amount to 20%
		if (this.rank() >= 5) {
			critAmount = 20;
		}
		// Rank 7 increases the amount to 25%
		if (this.rank() >= 7) {
			critAmount = 25;
		}
		// At Rank 10, the amount is "a guaranteed crit", so we take current crit from 100 to make 100% chance (though it should be directly accounted for in "use")
		if (this.rank() == 10) {
			critAmount = 100 - this.owner.getCriticalChance();
		}
		
		// Calculate the Armor Piercing amount based on rank
		// Rank 9 the amount is 10%
		if (this.rank() >= 9) {
			apAmount = 10;
		}
		// Rank 10 the amount is 15%
		if (this.rank() == 10) {
			apAmount = 15;
		}
		
		// Create the Status Effects
		StatusEffect critBonus = new StatusEffect(Stat.Version.CRITICAL_CHANCE, critAmount, StatusEffect.Type.OUTGOING);
		critBonus.makeFlat();
		StatusEffect apBonus = new StatusEffect(Stat.Version.ARMOR_PIERCING, apAmount, StatusEffect.Type.OUTGOING);
		apBonus.makePercentage();
		
		// Create the Condition (only add Armor Piercing bonus if at rank 9+) with duration 0 since it immediately disappears.
		this.targetedPreAttackBonus = new Condition("CHARGE!: Targeted Pre-Attack Bonus", 0);
		this.targetedPreAttackBonus.setSource(this.owner);
		this.targetedPreAttackBonus.addStatusEffect(critBonus);
		if (this.rank() >= 9) {
			this.targetedPreAttackBonus.addStatusEffect(apBonus);
		}
	}
	
	// Get methods for each variable
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	public double getTargetedScaler() {
		return this.targetedScaler;
	}
	
	public double getAOEScaler() {
		return this.AOEScaler;
	}
	
	public int getSpeedPercentage() {
		return this.speedPercentage;
	}
	
	public Condition getTargetedPreAttackBonus() {
		return new Condition(this.targetedPreAttackBonus);
	}
	
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
		// Select enemies to attack
		System.out.println("Choose enemies hit by AOE attack:");
    	LinkedList<Character> enemies = BattleSimulator.getInstance().targetMultiple();
        if (enemies.isEmpty()) {
        	return;
        }
        if (enemies.contains(Character.EMPTY)) {
        	enemies.clear();
        }
        
        // Select the target enemy
 		Character targeted = BattleSimulator.getInstance().targetSingle();
         if (targeted.equals(Character.EMPTY)) {
         	return;
         }
		
		// Before anything, put CHARGE! "on Cooldown"
		this.setOnCooldown();
		
		// Make the attack against all AOE enemies
		for (Character enemy : enemies) {
			// Applies the attack
			Attack chargeAtk = new AttackBuilder()
					.attacker(this.owner)
					.defender(enemy)
					.isAOE()
					.scaler(this.getAOEScaler())
					.type(Attack.DmgType.SLASHING)
					.range(Attack.RangeType.MELEE)
					.build();
			if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
				chargeAtk = this.getOwner().getDeflectionVersion(chargeAtk);
			}
			chargeAtk.execute();
		}
		
		// Make the final attack against the targeted enemy with the self-buff
		Attack targetedAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(targeted)
				.isTargeted()
				.scaler(this.getTargetedScaler())
				.type(Attack.DmgType.PIERCING)
				.range(Attack.RangeType.MELEE)
				.addAttackerCondition(this.getTargetedPreAttackBonus())
				.build();
		if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
			targetedAtk = this.getOwner().getDeflectionVersion(targetedAtk);
		}
		// At rank 10, it is a guaranteed crit
		if (this.rank() == 10) {
			targetedAtk = new AttackBuilder(targetedAtk).guaranteedCrit().build();
		}
		targetedAtk.execute();
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tAOE Scaler: " + this.getAOEScaler();
		ret += "\n\tTargeted Scaler: " + this.getTargetedScaler();
		ret += "\n\tSpeed increase (for Ability): " + this.getSpeedPercentage() + "%";
		ret += "\n\t" + this.getTargetedPreAttackBonus();
		return ret;
	}
}

// Ability 3: "Flip Strike"
class FlipStrike extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private double noMissChance;
	private Condition preAttackBonus;
	
	// Constructor
	public FlipStrike(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 3: \"Flip Strike\"", source, rank);
		this.owner = source;
		
		// Calculate and set the damage scaler
		this.setCooldown();
		this.setScaler();
		
		// Calculate and set the chance to not miss and the pre-attack Condition
		this.setNoMissChance();
		this.setPreAttackBonus();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 4, reduced to 3 at rank 5
		this.cooldown = 4;
		if (this.rank() >= 5) {
			this.cooldown = 3;
		}
		// The Ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Calculates and sets the damage scaler
	private void setScaler() {
		// Set a default value for the first rank
		this.scaler = 1.5;
		
		// Set the scaler based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.scaler = 1.5;
				break;
			case 2:
				this.scaler = 1.7;
				break;
			case 3:
				this.scaler = 1.8;
				break;
			case 4:
				this.scaler = 2;
				break;
			case 5:
				this.scaler = 2.2;
				break;
			case 6:
				this.scaler = 2.3;
				break;
			case 7:
				this.scaler = 2.5;
				break;
			case 8:
				this.scaler = 2.7;
				break;
			case 9:
				this.scaler = 2.8;
				break;
			case 10:
				this.scaler = 3;
				break;
		}
	}
	
	// Calculates and sets the additional effects
	private void setNoMissChance() {
		// Default value is 0
		this.noMissChance = 0;
		
		// Rank 7 increases the chance to 15%
		if (this.rank() >= 7) {
			this.noMissChance = .15;
		}
		// Rank 9 increases the chance to 20%
		if (this.rank() >= 9) {
			this.noMissChance = .20;
		}
		// Rank 10 increases the chance to 25%
		if (this.rank() == 10) {
			this.noMissChance = .25;
		}
	}
	
	private void setPreAttackBonus() {
		// Calculates the amount of increased Armor Piercing based on rank
		int apAmount = 10;
		switch(this.rank()) {
			case 1:
				apAmount = 10;
				break;
			case 3:
			case 2:
				apAmount = 12;
				break;
			case 4:
				apAmount = 14;
				break;
			case 5:
				apAmount = 15;
				break;
			case 6:
				apAmount = 17;
				break;
			case 7:
				apAmount = 18;
				break;
			case 8:
				apAmount = 20;
				break;
			case 9:
				apAmount = 22;
				break;
			case 10:
				apAmount = 25;
				break;
		}
		
		// Creates the StatusEffect
		StatusEffect apBonus = new StatusEffect(Stat.Version.ARMOR_PIERCING, apAmount, StatusEffect.Type.OUTGOING);
		apBonus.makePercentage();
		
		// Creates the Condition
		this.preAttackBonus = new Condition("Flip Strike: Pre-Attack Bonus", 0);
		this.preAttackBonus.setSource(this.owner);
		this.preAttackBonus.addStatusEffect(apBonus);
	}
	
	// Get methods for additional effects
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public double getNoMissChance() {
		return this.noMissChance;
	}
	
	public Condition getPreAttackBonus() {
		return new Condition(this.preAttackBonus);
	}
	
	
	// Use function called when the action is chosen from the possible Commands
	@Override
	public void use() {
        // Select the target enemy
 		Enemy enemy = BattleSimulator.getInstance().targetSingleEnemy();
	    if (enemy.equals(Enemy.EMPTY)) {
	    	return;
	    }
		
		// Before anything, put Flip Strike "on Cooldown"
		this.setOnCooldown();
		
		// Make the attack against the targeted enemy with the self-buff
		Attack flipAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SLASHING)
				.range(Attack.RangeType.MELEE)
				.addAttackerCondition(this.getPreAttackBonus())
				.build();
		if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
			flipAtk = this.getOwner().getDeflectionVersion(flipAtk);
		}
		// Above rank 7, there is a chance the attack cannot miss
		if (this.rank() >= 7) {
			// Get the chance the attack cannot miss based on the enemy difficulty
			double chance = this.getNoMissChance();
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE) && this.rank() < 10) {
				chance /= 2.0;
			}
			else if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS) && this.rank() < 10) {
				chance = 0;
			}
			
			// Get a random probability
			Random rd = new Random();
			double prob = rd.nextDouble();
			
			// If the probability falls under the chance, it is a success
			if (prob <= chance) {
				flipAtk = new AttackBuilder(flipAtk).cannotMiss().build();
			}
		}
		flipAtk.execute();
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	public void useVengeanceStrikeVersion(Attack original) {
		// Make the attack against the targeted enemy with the self-buff based on the original VengeanceStrike
		Enemy enemy = (Enemy)original.getDefender(); // Guaranteed to be of type Enemy due to Vengeance Strike check
		Attack flipAtk = new AttackBuilder()
				.attacker(this.owner)
				.defender(enemy)
				.isTargeted()
				.scaler(this.getScaler())
				.type(Attack.DmgType.SLASHING)
				.range(Attack.RangeType.MELEE)
				.addAttackerCondition(this.getPreAttackBonus())
				.build();
		if (this.getOwner().isAbilityActive(SteelLegionWarrior.AbilityNames.Deflection)) {
			flipAtk = this.getOwner().getDeflectionVersion(flipAtk);
		}
		
		// Above rank 7, there is a chance the attack cannot miss
		if (this.rank() >= 7) {
			// Get the chance the attack cannot miss based on the enemy difficulty
			double chance = this.getNoMissChance();
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE) && this.rank() < 10) {
				chance /= 2.0;
			}
			else if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS) && this.rank() < 10) {
				chance = 0;
			}
			
			// Get a random probability
			Random rd = new Random();
			double prob = rd.nextDouble();
			
			// If the probability falls under the chance, it is a success
			if (prob <= chance) {
				flipAtk = new AttackBuilder(flipAtk).cannotMiss().build();
			}
		}
		// If Vengeance Strike is rank 5, the scaler is increased by 50% (based on Vengeance Strike scaler) and there is a bonus 30% lifesteal
		if (this.getOwner().getAbilityRank(SteelLegionWarrior.AbilityNames.VengeanceStrike) == 5) {
			flipAtk = new AttackBuilder(flipAtk).scaler(this.getScaler() * original.getScaler()).lifestealPercentage(30).build();
		}
		flipAtk.execute();
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += this.rank() >= 7? ("\n\tCannot Miss Chance: " + this.getNoMissChance()) : "";
		ret += "\n\t" + this.getPreAttackBonus();
		return ret;
	}
}

// Ability 4: "Intimidating Shout"
class IntimidatingShout extends Ability {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private Condition tauntNormal;
	private Condition tauntNormalReducedValue;
	private Condition tauntAdvanced;
	private Condition tauntAdvancedReducedValue;
	private Condition tauntElite;
	private Condition tauntEliteReducedValue;
	private Condition tauntBoss;
	private Condition tauntBossReducedValue;
	private Condition selfDefenseBonus;
	private Condition selfDefenseBonusReducedValue;
	
	// These are used when "Deflection" is activated
	private Condition tauntNormalExtraDuration;
	private Condition tauntAdvancedExtraDuration;
	private Condition tauntEliteExtraDuration;
	private Condition tauntBossExtraDuration;
	private Condition selfDefenseBonusExtraDuration;
	
	// Constructor
	public IntimidatingShout(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("Ability 4: \"Intimidating Shout\"", source, rank);
		this.owner = source;
		
		// Set the Cooldown of the Ability (this Ability has no scaler) and the duration, making the Ability activatable
		this.setCooldown();
		this.setDurationActive();
		
		// Calculate the additional Conditions of the ability
		this.setTauntConditions();
		this.setSelfDefenseBonuses();
	}
	
	// Set the Cooldown
	private void setCooldown() {
		// Base Cooldown of 5, reduced to 4 at rank 5
		this.cooldown = 5;
		if (this.rank() >= 5) {
			this.cooldown = 4;
		}
		// The Ability always starts off Cooldown
		this.setOffCooldown();
	}
	
	// Sets the Duration and makes the Ability activatable
	private void setDurationActive() {
		// Default duration of 1, increased to 2 at rank 10.
		int duration = 1;
		if (this.rank() == 10) {
			duration = 2;
		}
		this.makeActiveAbility(duration, true);
	}
	
	// Calculates the amount of damage reduction for Normal enemies (other enemies are multiples of this number)
	private double normalDmgReduction() {
		// Set a default value for the first rank
		double amount = 30;
		
		// Set the amount based on the rank of the Ability
		switch(this.rank()) {
			case 1:
				amount = 30;
				break;
			case 2:
				amount = 33;
				break;
			case 3:
				amount = 36;
				break;
			case 4:
				amount = 39;
				break;
			case 5:
				amount = 45;
				break;
			case 6:
				amount = 50;
				break;
			case 7:
				amount = 50;
				break;
			case 8:
				amount = 60;
				break;
			case 9:
				amount = 75;
				break;
			case 10:
				amount = 100;
				break;
		}
		
		// Return the amount
		return amount;
	}
	
	// Calculate and create each Condition
	private void setTauntConditions() {
		// Set the amount for each version based on the rank
		double normalBaseAmount = this.normalDmgReduction();
		double normalReducedAmount = normalBaseAmount/2;
		
		double advancedBaseAmount = normalBaseAmount/2;
		double advancedReducedAmount = advancedBaseAmount/2;
		
		double eliteBaseAmount = normalBaseAmount/3;
		double eliteReducedAmount = eliteBaseAmount/2;
		
		// Boss amount is only set to non-zero if rank 7+
		double bossBaseAmount = 0;
		double bossReducedAmount = 0;
		if (this.rank() >= 7) {
			bossBaseAmount = normalBaseAmount/4;
			bossReducedAmount = bossBaseAmount/2;
		}
		
		
		// Create the requirement that the reduced damage only occurs on the owner
		DualRequirement isOwner = (Character withEffect, Character other) -> {
			return other.equals(this.owner);
		};
		
		// Create the Status Effects for each version
		// Normal
		StatusEffect normalBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -normalBaseAmount, StatusEffect.Type.OUTGOING);
		normalBaseDmgReduction.makePercentage();
		normalBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect normalReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -normalReducedAmount, StatusEffect.Type.OUTGOING);
		normalReducedDmgReduction.makePercentage();
		normalReducedDmgReduction.setDualRequirement(isOwner);
		
		// Advanced
		StatusEffect advancedBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -advancedBaseAmount, StatusEffect.Type.OUTGOING);
		advancedBaseDmgReduction.makePercentage();
		advancedBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect advancedReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -advancedReducedAmount, StatusEffect.Type.OUTGOING);
		advancedReducedDmgReduction.makePercentage();
		advancedReducedDmgReduction.setDualRequirement(isOwner);
		
		// Elite
		StatusEffect eliteBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -eliteBaseAmount, StatusEffect.Type.OUTGOING);
		eliteBaseDmgReduction.makePercentage();
		eliteBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect eliteReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -eliteReducedAmount, StatusEffect.Type.OUTGOING);
		eliteReducedDmgReduction.makePercentage();
		eliteReducedDmgReduction.setDualRequirement(isOwner);
		
		// Boss
		StatusEffect bossBaseDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -bossBaseAmount, StatusEffect.Type.OUTGOING);
		bossBaseDmgReduction.makePercentage();
		bossBaseDmgReduction.setDualRequirement(isOwner);
		
		StatusEffect bossReducedDmgReduction = new StatusEffect(Stat.Version.DAMAGE, -bossReducedAmount, StatusEffect.Type.OUTGOING);
		bossReducedDmgReduction.makePercentage();
		bossReducedDmgReduction.setDualRequirement(isOwner);
		
		// Create the base and extra duration Conditions for each version
		// Normal
		this.tauntNormal = new Condition("Intimidating Shout: TAUNT - Damage Reduced", this.getDuration() + 1);
		this.tauntNormal.setSource(this.owner);
		this.tauntNormal.makeSourceIncrementing();
		this.tauntNormal.makeEndOfTurn();
		this.tauntNormal.addStatusEffect(normalBaseDmgReduction);
		
		this.tauntNormalExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", this.getDuration() + 2);
		this.tauntNormalExtraDuration.setSource(this.owner);
		this.tauntNormalExtraDuration.makeSourceIncrementing();
		this.tauntNormalExtraDuration.makeEndOfTurn();
		this.tauntNormalExtraDuration.addStatusEffect(normalBaseDmgReduction);
		
		// Advanced
		this.tauntAdvanced = new Condition("Intimidating Shout: TAUNT - Damage Reduced", this.getDuration() + 1);
		this.tauntAdvanced.setSource(this.owner);
		this.tauntAdvanced.makeSourceIncrementing();
		this.tauntAdvanced.makeEndOfTurn();
		this.tauntAdvanced.addStatusEffect(advancedBaseDmgReduction);
		
		this.tauntAdvancedExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", this.getDuration() + 2);
		this.tauntAdvancedExtraDuration.setSource(this.owner);
		this.tauntAdvancedExtraDuration.makeSourceIncrementing();
		this.tauntAdvancedExtraDuration.makeEndOfTurn();
		this.tauntAdvancedExtraDuration.addStatusEffect(advancedBaseDmgReduction);
		
		// Elite
		this.tauntElite = new Condition("Intimidating Shout: TAUNT - Damage Reduced", this.getDuration() + 1);
		this.tauntElite.setSource(this.owner);
		this.tauntElite.makeSourceIncrementing();
		this.tauntElite.makeEndOfTurn();
		this.tauntElite.addStatusEffect(eliteBaseDmgReduction);
		
		this.tauntEliteExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", this.getDuration() + 2);
		this.tauntEliteExtraDuration.setSource(this.owner);
		this.tauntEliteExtraDuration.makeSourceIncrementing();
		this.tauntEliteExtraDuration.makeEndOfTurn();
		this.tauntEliteExtraDuration.addStatusEffect(eliteBaseDmgReduction);
		
		// Boss
		this.tauntBoss = new Condition("Intimidating Shout: TAUNT - Damage Reduced", this.getDuration() + 1);
		this.tauntBoss.setSource(this.owner);
		this.tauntBoss.makeSourceIncrementing();
		this.tauntBoss.makeEndOfTurn();
		this.tauntBoss.addStatusEffect(bossBaseDmgReduction);
		
		this.tauntBossExtraDuration = new Condition("Intimidating Shout (Deflection): TAUNT - Damage Reduced", this.getDuration() + 2);
		this.tauntBossExtraDuration.setSource(this.owner);
		this.tauntBossExtraDuration.makeSourceIncrementing();
		this.tauntBossExtraDuration.makeEndOfTurn();
		this.tauntBossExtraDuration.addStatusEffect(bossBaseDmgReduction);
		
		// Create the requirement that the reduced Condition must meet to activate (the base Condition (any of them, including the deflection versions) cannot be present)
		Requirement actReq = (Character withEffect) -> {
			return !(withEffect.getAllConditions().contains(this.tauntNormal) || 
					 withEffect.getAllConditions().contains(this.tauntNormalExtraDuration) ||
					 withEffect.getAllConditions().contains(this.tauntAdvanced) ||
					 withEffect.getAllConditions().contains(this.tauntAdvancedExtraDuration) ||
					 withEffect.getAllConditions().contains(this.tauntElite) ||
					 withEffect.getAllConditions().contains(this.tauntEliteExtraDuration) ||
					 withEffect.getAllConditions().contains(this.tauntBoss) ||
					 withEffect.getAllConditions().contains(this.tauntBossExtraDuration));
		};
		
		// Create the reduced effect Condition that always has a duration of 1 for each enemy version
		// Normal
		this.tauntNormalReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntNormalReducedValue.setSource(this.owner);
		this.tauntNormalReducedValue.makeSourceIncrementing();
		this.tauntNormalReducedValue.makeEndOfTurn();
		this.tauntNormalReducedValue.addStatusEffect(normalReducedDmgReduction);
		
		// Advanced
		this.tauntAdvancedReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntAdvancedReducedValue.setSource(this.owner);
		this.tauntAdvancedReducedValue.makeSourceIncrementing();
		this.tauntAdvancedReducedValue.makeEndOfTurn();
		this.tauntAdvancedReducedValue.addStatusEffect(advancedReducedDmgReduction);
		
		// Elite
		this.tauntEliteReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntEliteReducedValue.setSource(this.owner);
		this.tauntEliteReducedValue.makeSourceIncrementing();
		this.tauntEliteReducedValue.makeEndOfTurn();
		this.tauntEliteReducedValue.addStatusEffect(eliteReducedDmgReduction);
		
		// Boss
		this.tauntBossReducedValue = new Condition("Intimidating Shout: TAUNT - Damage Reduced", 1, actReq);
		this.tauntBossReducedValue.setSource(this.owner);
		this.tauntBossReducedValue.makeSourceIncrementing();
		this.tauntBossReducedValue.makeEndOfTurn();
		this.tauntBossReducedValue.addStatusEffect(bossReducedDmgReduction);
	}
	
	private void setSelfDefenseBonuses() {
		// Declare starting amounts (both are the same, so can use one variable) at rank 1
		double amount = 25;
		
		// Default duration of 1, increased to 2 at rank 10.
		int duration = 1;
		if (this.rank() == 10) {
			duration = 2;
		}
		
		// Calculate the amounts based on rank
		switch(this.rank()) {
			case 1:
				amount = 25;
				break;
			case 2:
				amount = 27;
				break;
			case 3:
				amount = 28;
				break;
			case 4:
				amount = 30;
				break;
			case 5:
				amount = 30;
				break;
			case 6:
				amount = 33;
				break;
			case 7:
				amount = 33;
				break;
			case 8:
				amount = 36;
				break;
			case 9:
				amount = 40;
				break;
			case 10:
				amount = 50;
				break;
		}
		
		// Create the Status Effects
		StatusEffect blkBonus = new StatusEffect(Stat.Version.BLOCK, amount, StatusEffect.Type.INCOMING);
		blkBonus.makePercentage();
		
		StatusEffect reducedBlkBonus = new StatusEffect(Stat.Version.BLOCK, amount/2, StatusEffect.Type.INCOMING);
		reducedBlkBonus.makePercentage();
		
		StatusEffect armorBonus = new StatusEffect(Stat.Version.ARMOR, amount, StatusEffect.Type.INCOMING);
		armorBonus.makePercentage();
		
		StatusEffect reducedArmorBonus = new StatusEffect(Stat.Version.ARMOR, amount/2, StatusEffect.Type.INCOMING);
		reducedArmorBonus.makePercentage();
		
		// Create the base Conditions (only difference is duration)
		this.selfDefenseBonus = new Condition("Intimidating Shout: Defense Bonus", duration);
		this.selfDefenseBonus.setSource(this.owner);
		this.selfDefenseBonus.makeEndOfTurn();
		this.selfDefenseBonus.addStatusEffect(blkBonus);
		this.selfDefenseBonus.addStatusEffect(armorBonus);
		
		this.selfDefenseBonusExtraDuration = new Condition("Intimidating Shout (Deflection): Defense Bonus", duration + 1);
		this.selfDefenseBonusExtraDuration.setSource(this.owner);
		this.selfDefenseBonusExtraDuration.makeEndOfTurn();
		this.selfDefenseBonusExtraDuration.addStatusEffect(blkBonus);
		this.selfDefenseBonusExtraDuration.addStatusEffect(armorBonus);
		
		// Create the activation requirement for the reduced Condition (neither base Condition can be present)
		Requirement actReq = (Character withEffect) -> {
			return !(withEffect.getAllConditions().contains(this.selfDefenseBonus) ||
					 withEffect.getAllConditions().contains(this.selfDefenseBonusExtraDuration));
		};
		
		// Create the reduced effect Condition that always has a duration of 1
		this.selfDefenseBonusReducedValue = new Condition("Intimidating Shout: Defense Bonus", 1, actReq);
		this.selfDefenseBonusReducedValue.setSource(this.owner);
		this.selfDefenseBonusReducedValue.makeEndOfTurn();
		this.selfDefenseBonusReducedValue.addStatusEffect(reducedBlkBonus);
		this.selfDefenseBonusReducedValue.addStatusEffect(reducedArmorBonus);
	}
	
	// Get methods for each Condition
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	
	public Condition getTauntNormal() {
		return new Condition(this.tauntNormal);
	}
	public Condition getTauntNormalReducedValue() {
		return new Condition(this.tauntNormalReducedValue);
	}
	public Condition getTauntNormalDeflection() {
		return new Condition(this.tauntNormalExtraDuration);
	}
	
	public Condition getTauntAdvanced() {
		return new Condition(this.tauntAdvanced);
	}
	public Condition getTauntAdvancedReducedValue() {
		return new Condition(this.tauntAdvancedReducedValue);
	}
	public Condition getTauntAdvancedDeflection() {
		return new Condition(this.tauntAdvancedExtraDuration);
	}
	
	public Condition getTauntElite() {
		return new Condition(this.tauntElite);
	}
	public Condition getTauntEliteReducedValue() {
		return new Condition(this.tauntEliteReducedValue);
	}
	public Condition getTauntEliteDeflection() {
		return new Condition(this.tauntEliteExtraDuration);
	}
	
	public Condition getTauntBoss() {
		return new Condition(this.tauntBoss);
	}
	public Condition getTauntBossReducedValue() {
		return new Condition(this.tauntBossReducedValue);
	}
	public Condition getTauntBossDeflection() {
		return new Condition(this.tauntBossExtraDuration);
	}
	
	public Condition getSelfDefenseBonus() {
		return new Condition(this.selfDefenseBonus);
	}
	public Condition getSelfDefenseBonusDeflection() {
		return new Condition(this.selfDefenseBonusExtraDuration);
	}
	
	
	// Default version of Ability
	public void use() {
		// Select enemies affected
		System.out.println("Choose enemies affected:");
    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
    	LinkedList<Enemy> enemies = new LinkedList<>();
        if (targets.isEmpty()) {
        	return;
        }
        if (targets.contains(Character.EMPTY)) {
        	targets.clear();
        }
        for (Character target : targets) {
        	if (!(target instanceof Enemy)) {
        		System.out.println("Cannot use Intimidating Shout against non-enemies. Select again.");
        		return;
        	}
        	enemies.add((Enemy)target);
        }
		
		// First, place Intimidating Shout "on cooldown"
		this.setOnCooldown();
		
		// Apply all basic taunt conditions to respective enemies
		for (Enemy enemy : enemies) {
			// Normal enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL)) {
				enemy.addCondition(this.getTauntNormal());
			}
			// Advanced enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED)) {
				enemy.addCondition(this.getTauntAdvanced());
			}
			// Elite enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE)) {
				enemy.addCondition(this.getTauntElite());
			}
			// Boss enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS)) {
				enemy.addCondition(this.getTauntBoss());
			}
			
			// The reduced effects are also added to each only if above rank 7
			if (this.rank() >= 7) {
				// Normal enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL)) {
					enemy.addCondition(this.getTauntNormalReducedValue());
				}
				// Advanced enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED)) {
					enemy.addCondition(this.getTauntAdvancedReducedValue());
				}
				// Elite enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE)) {
					enemy.addCondition(this.getTauntEliteReducedValue());
				}
				// Boss enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS)) {
					enemy.addCondition(this.getTauntBossReducedValue());
				}
			}
		}
		
		// Apply the self block bonus
		this.owner.addCondition(this.getSelfDefenseBonus());
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	public void useDeflectionVersion() {
		// Select enemies affected
		System.out.println("Choose enemies affected by Intimidating Shout:");
    	LinkedList<Character> targets = BattleSimulator.getInstance().targetMultiple();
    	LinkedList<Enemy> enemies = new LinkedList<>();
        if (targets.isEmpty()) {
        	return;
        }
        if (targets.contains(Character.EMPTY)) {
        	targets.clear();
        }
        for (Character target : targets) {
        	if (!(target instanceof Enemy)) {
        		System.out.println("Cannot use Intimidating Shout against non-enemies. Select again.");
        		return;
        	}
        	enemies.add((Enemy)target);
        }
		
		// Apply all basic taunt conditions to respective enemies
		for (Enemy enemy : enemies) {
			// Normal enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL)) {
				enemy.addCondition(this.getTauntNormalDeflection());
			}
			// Advanced enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED)) {
				enemy.addCondition(this.getTauntAdvancedDeflection());
			}
			// Elite enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE)) {
				enemy.addCondition(this.getTauntEliteDeflection());
			}
			// Boss enemies
			if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS)) {
				enemy.addCondition(this.getTauntBossDeflection());
			}
			
			// The reduced effects are also added to each only if above rank 7
			if (this.rank() >= 7) {
				// Normal enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.NORMAL)) {
					enemy.addCondition(this.getTauntNormalReducedValue());
				}
				// Advanced enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.ADVANCED)) {
					enemy.addCondition(this.getTauntAdvancedReducedValue());
				}
				// Elite enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.ELITE)) {
					enemy.addCondition(this.getTauntEliteReducedValue());
				}
				// Boss enemies
				if (enemy.getDifficulty().equals(Enemy.Difficulty.BOSS)) {
					enemy.addCondition(this.getTauntBossReducedValue());
				}
			}
		}
		
		// Apply the self block bonus
		this.owner.addCondition(this.getSelfDefenseBonusDeflection());
	}
	
	// Use function called either by default or in Deflection
	public void use(int version) {
		// Use the normal version by default
		if (version == 1) {
			this.activate();
			this.use();
			return;
		}
		
		// If the version specified is 2, use the Deflection version
		if (version == 2) {
			this.activate(1);
			this.useDeflectionVersion();
			return;
		}
		
		// Otherwise. print a warning if this function is ever actually directly called
		System.out.println("Warning: The Ability, " + this.getName() + ", does not have a use(" + version + ") function defined, but it was called!");
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\t" + this.getTauntNormal();
		ret += "\n\t" + this.getSelfDefenseBonus();
		return ret;
	}
}

// ULTIMATE Ability: "Deflection"
class Deflection extends UltimateAbility {
	// Holds the owner of the Ability as a Steel Legion Warrior
	private SteelLegionWarrior owner;
	
	// Additional Variables
	private double healingScaler;
	private Condition vsArmored;
	private Stun stunEffect;
	
	// Helper Variables
	private boolean hasBounced;
	private int damageTaken;
	
	// Constructor
	public Deflection(SteelLegionWarrior source, int rank) {
		// Initialize all Ability variables to defaults
		super("ULTIMATE Ability: \"Deflection\"", source, rank);
		this.owner = source;
		
		// Calculate the damage and healing scalers of the Ability
		this.setScalers();
		
		// Calculate and set the additional effects
		this.setStun();
		this.setVsArmoredCondition();
		
		// Initialize Helper Variables
		this.hasBounced = false;
		this.damageTaken = 0;
	}
	// Additional function that must be called once Intimidating Shout has been declared and initialized in the Steel Legion Warrior constructor
	public void setDuration() {
		// Make this an active ability with duration based on the duration of intimidating shout and active its final turn
		int duration = this.owner.getAbilityDuration(SteelLegionWarrior.AbilityNames.IntimidatingShout) + 1;
		if (this.rank() >= 3) {
			duration += 1;
		}
		this.makeActiveAbility(duration, true);
	}
	
	// Calculates the damage scaler
	private void setScalers() {
		// Set a default value for the first rank
		this.scaler = .3;
		this.healingScaler = .25;
		
		// Set the scalers based on the rank of the ability
		switch(this.rank()) {
			case 1:
				this.scaler = .3;
				this.healingScaler = .25;
				break;
			case 2:
				this.scaler = .5;
				this.healingScaler = 1.0/3.0;
				break;
			case 3:
				this.scaler = .75;
				this.healingScaler = .5;
				break;
		}
	}
	
	// Calculates and creates the additional effects
	private void setStun() {
		this.stunEffect = new Stun("Deflection: Stun", 1);
		this.stunEffect.setSource(this.owner);
		this.stunEffect.makeSourceIncrementing();
		this.stunEffect.makeEndOfTurn();
	}
	
	private void setVsArmoredCondition() {
		// Set the extra damage percentage based on the rank of the ability
		int percentBonus = 30;
		if (this.rank() == 2) {
			percentBonus = 40;
		}
		else if (this.rank() >= 3) {
			percentBonus = 50;
		}
		
		DualRequirement isArmored = (Character withEffect, Character other) -> {
			return other.getType().equals(Character.Type.ARMORED);
		};
		StatusEffect bonusArmoredDamage = new StatusEffect(Stat.Version.DAMAGE, percentBonus);
		bonusArmoredDamage.makePercentage();
		bonusArmoredDamage.setDualRequirement(isArmored);
		this.vsArmored = new Condition("Deflection Bonus Armored Damage", -1);
		this.vsArmored.setSource(this.owner);
		this.vsArmored.addStatusEffect(bonusArmoredDamage);
	}
	
	// Get methods for each variable
	@Override
	public SteelLegionWarrior getOwner() {
		return this.owner;
	}
	public double getHealingScaler() {
		return this.healingScaler;
	}
	
	public Stun getStun() {
		return new Stun(this.stunEffect);
	}
	public Condition getVsArmoredCondition() {
		return new Condition(this.vsArmored);
	}
	
	// Get methods for each extra electric Attack that can be made
	// Private function to return the base builder most electric attacks follow
	private AttackBuilder getElectricAttackBuilder() {
		// Build the electric attack
		AttackBuilder eAtkBuilder = new AttackBuilder()
				.attacker(this.owner)
				.scaler(this.getScaler())
				.type(Attack.DmgType.ELECTRIC)
				.range(Attack.RangeType.OTHER)
				.cannotMiss()
				.cannotCrit()
				.ignoresArmor()
				.addAttackerCondition(this.getVsArmoredCondition());
		
		// Applies stun by chance to the defender struck (0%/10%/25% chance by rank)
		int stunChance = 0;
		if (this.rank() == 2) {
			stunChance = 10;
		}
		if (this.rank() == 3) {
			stunChance = 25;
		}
		Dice percent = new Dice(100);
		if (stunChance >= percent.roll()) {
			eAtkBuilder.addSuccessCondition(this.getStun());
		}
		
		// Return the result
		return eAtkBuilder;
	}
	// Returns a specified attack with the electric attack added on as an attached attack
	public Attack getDeflectionVersion(Attack original) {
		// Gets the electric attack base builder
		AttackBuilder electricAttached = this.getElectricAttackBuilder();
		
		// Creates an alteration so the attached attack following the hit and crit of the original attack
		AttachedAlteration hitAndCrit = (AttackResult atkRes) -> {
			AttackBuilder withEffects = new AttackBuilder(electricAttached.build())
					.canHit(atkRes.didHit());
			if (atkRes.didCrit()) {
				withEffects.guaranteedCrit();
			}
			return withEffects.build();
		};
		
		// Return the resulting new attack with the electric attack attached with the alteration, adding the fact that the attack ignores all armor at rank 2
		Attack attached = electricAttached.attachedAlteration(hitAndCrit).build();
		AttackBuilder ret = new AttackBuilder(original).addAttachedAttack(attached);
		if (this.rank() >= 2) {
			ret.ignoresArmor();
		}
		return ret.build();
	}
	// Applies pre attack effects of this Ability with the proper attacker accuracy reductions to guarantee a miss
	@Override
	public void applyPreAttackEffects(Attack atk) {
		// First, do nothing if inactive.
		if (!this.isActive()) {
			return;
		}
		
		// The pre attack effect only occurs when defending
		if (!atk.getDefender().equals(this.getOwner())) {
			return;
		}
		
		// At all ranks, the attack must be a Targeted attack that can miss for anything to occur
		if (!atk.isTargeted() || !atk.canMiss()) {
			return;
		}
		
		// The accuracy reduction always occurs at rank 3, but at rank 1 and 2, the accuracy reduction only occurs for Ranged SLASHING/SMASHING/PIERCING/FLEX attacks
		if (this.rank() >= 3 || (atk.getRangeType().equals(Attack.RangeType.RANGED) && (atk.getDmgType().equals(Attack.DmgType.SLASHING) || atk.getDmgType().equals(Attack.DmgType.SMASHING) || atk.getDmgType().equals(Attack.DmgType.PIERCING) || atk.getDmgType().equals(Attack.DmgType.FLEX)))) {
			// Apply Accuracy Reduction
			StatusEffect accRed = new StatusEffect(Stat.Version.ACCURACY, -100);
			accRed.makePercentage();
			accRed.makeAffectOther();
			Condition selfPreAttack = new Condition("Deflection: Guaranteed Miss", 0);
			selfPreAttack.setSource(this.getOwner());
			selfPreAttack.addStatusEffect(accRed);
			atk.addDefenderCondition(selfPreAttack);
		}
		
		// At rank 3, the attack also must be executed against the attacker at 20% damage, followed by a trigger of Vengeance Strike
		if (this.rank() >= 3) {
			Attack retribution = new AttackBuilder(atk).defender(atk.getAttacker()).scaler(atk.getScaler() * 0.2).build();
			retribution.execute();
			this.getOwner().useVengeanceStrike(atk.getAttacker());
		}
	}
	// Applies post attack effects of this Ability with the proper electric attacks
	@Override
	public void applyPostAttackEffects(AttackResult atkRes) {
		// First, do nothing if inactive.
		if (!this.isActive()) {
			return;
		}
		
		// When attacking there are stun effects, and at rank 3, electric attacks bounce, allowing additional attacks to get performed
		if (this.getOwner().equals(atkRes.getAttacker()) ) {
			// Checks to see if any electric attacks occurred
			boolean prevElectric = atkRes.getDmgType().equals(Attack.DmgType.ELECTRIC);
			for (AttackResult attached : atkRes.getAttachedAttackResults()) {
				if (attached.getDmgType().equals(Attack.DmgType.ELECTRIC) && attached.didHit()) {
					prevElectric = true;
				}
			}
			
			// At rank 3, electric attacks bounce, allowing additional attacks to get performed
			if (this.rank() >= 3) {
				// If bouncing can occur, first prompt for enemies hit
				if (prevElectric && !this.hasBounced) {
					System.out.println("Enter all Characters that will also be hit by bounce electric attacks:");
					LinkedList<Character> bounceTargets = BattleSimulator.getInstance().targetMultiple();
					
					// Make "hasBounced" true so additional prompts do not occur
					this.hasBounced = true;
					
					// Attack all bounce targets with the appropriate electric attack
					AttackBuilder bounceAttack = this.getElectricAttackBuilder().scaler(.5);
					if (atkRes.didCrit()) {
						bounceAttack.guaranteedCrit();
					}
					for (Character enemy : bounceTargets) {
						bounceAttack.defender(enemy).build().execute();
					}
					// Reset "hasBounced" to false so this portion of the Ability can still be used
					this.hasBounced = false;
				}
			}
			// Return to end the function
			return;
		}
		// Otherwise, when defending, apply the proper electric attack response: Only can trigger on targeted attacks. And store the damage taken for healing
		if (this.getOwner().equals(atkRes.getDefender())) {
			this.damageTaken += atkRes.getDamageDealt();
		}
		if (this.getOwner().equals(atkRes.getDefender()) && atkRes.isTargeted()) {
			// The Ability never occurs for attacks classified as "OTHER"
			if (atkRes.getRangeType().equals(Attack.RangeType.OTHER)) {
				return;
			}
			// At rank 1, the Ability only works for melee attackers
			if (this.rank() == 1 && !atkRes.getRangeType().equals(Attack.RangeType.MELEE)) {
				return;
			}
			// At rank 2, the ranged attacks have to be DmgType Slashing, Smashing, Piercing, or Flex
			if (this.rank() == 2 && atkRes.getRangeType().equals(Attack.RangeType.RANGED) && !(atkRes.getDmgType().equals(Attack.DmgType.SLASHING) || atkRes.getDmgType().equals(Attack.DmgType.SMASHING) || atkRes.getDmgType().equals(Attack.DmgType.PIERCING) || atkRes.getDmgType().equals(Attack.DmgType.FLEX))) {
				return;
			}
			
			// Build the electric attack
			AttackBuilder defendAttack = this.getElectricAttackBuilder().defender(atkRes.getAttacker());
			
			// At ranks 1 and 2, if the melee attack was blocked, the critical strike is guaranteed, otherwise, not possible
			if (this.rank() <= 2 && atkRes.getRangeType().equals(Attack.RangeType.MELEE)) {
				if (atkRes.didHit()) {
					defendAttack.cannotCrit();
				}
				else {
					defendAttack.guaranteedCrit();
				}
			}
			// The result from ranged attacks at rank 2 and all attacks at rank 3 can critically strike per normal
			else {
				defendAttack.canCrit();
			}
			
			// Execute the attack
			defendAttack.build().execute();
		}
	}
	
	// Override the use function
	@Override
	public void use() {
		// If the Ability is on Cooldown, prompt if this was intended
		if (this.onCooldown()) {
			System.out.println(this.getName() + " is still on Cooldown! Continue with use?");
			if (!BattleSimulator.getInstance().askYorN()) {
				return;
			}
		}
		
		// Activate the Ability's duration.
		this.activate();
		
		// First, before anything, put the Ability on Cooldown
		this.setOnCooldown();
		
		// Alter basic attack
		Attack basicAtk = new AttackBuilder().attacker(this.getOwner()).type(this.getOwner().getBaseDmgType()).range(this.getOwner().getRangeType()).build();
		BasicAttackCommand altered = new BasicAttackCommand(this.getOwner(), this.getDeflectionVersion(basicAtk));
		this.getOwner().alterBasicAttack(altered);
		
		// Use Intimidating Shout Version 2
		this.getOwner().useAbility(SteelLegionWarrior.AbilityNames.IntimidatingShout, 2);
		
		// This Ability uses the Character's turn actions
		this.owner.useTurnActions();
	}
	
	// Override the deactivate function to also restore the basic attack to normal and apply end-of-ability effects
	@Override
	public void deactivate() {
		super.deactivate();
		this.getOwner().restoreBasicAttack();
		
		// At rank 3, a massive final attack is made
		if (this.rank() >= 3) {
			System.out.println("Bonus Action! Choose target to attack, or choose None (0) to not make the bonus action.");
			Character enemy = BattleSimulator.getInstance().targetSingle();
			if (!enemy.equals(Character.EMPTY)) {
				Attack bonus = new AttackBuilder()
						.attacker(this.getOwner())
						.defender(enemy)
						.scaler(2.0)
						.type(Attack.DmgType.ELECTRIC)
						.range(Attack.RangeType.RANGED)
						.isTargeted()
						.ignoresArmor()
						.cannotMiss()
						.cannotCrit()
						.build();
				bonus.execute();
				enemy.addCondition(this.getStun());
			}
		}
		
		this.getOwner().restoreHealth((int)Math.round(this.damageTaken * this.getHealingScaler()));
	}
	
	// Returns the full information about the ability
	public String getDescription() {
		String ret = super.getDescription();
		ret += "\n\tHealing Scaler: " + this.getHealingScaler();
		ret += "\n\t" + this.getVsArmoredCondition();
		ret += "\n\t" + this.getStun();
		return ret;
	}
}



// The Steel Legion Warrior itself:
public class SteelLegionWarrior extends Character {
	// Enumerates the names of the abilities so Cooldown and use functions can be called
	public static enum AbilityNames {
		VengeanceStrike, SwordplayProwess, WarriorsMight, AgileFighter, Sweep, Charge, FlipStrike, IntimidatingShout, Deflection
	}
	
	// Passive Abilities
	private VengeanceStrike VengeanceStrike; // Unique Passive Ability (UPA)
	private SwordplayProwess SwordplayProwess;
	private WarriorsMight WarriorsMight;
	private AgileFighter AgileFighter;  // Add initialization?
	
	// Base Abilities
	private Sweep Sweep;
	private Charge Charge;
	private FlipStrike FlipStrike;
	private IntimidatingShout IntimidatingShout;
	private Deflection Deflection;
	
	// Maps all Abilities so all Cooldowns can be reduced at once
	private HashMap<SteelLegionWarrior.AbilityNames, Ability> abilities;
	
	// These first two methods help set up the Steel Legion Warrior subclass.
	public SteelLegionWarrior(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, Attack.DmgType dmgType, HashMap<Attack.DmgType,Double> resis, HashMap<Attack.DmgType,Double> vuls, Type type, int vsRank, int spRank, int wmRank, int afRank, int sweepRank, int chargeRank, int fsRank, int isRank, int deflectRank) {
		// Calls the super constructor to create the Character, then initializes all Abilities according to their specifications.
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, dmgType, resis, vuls, type);
		this.VengeanceStrike = new VengeanceStrike(this, vsRank);
		this.SwordplayProwess = new SwordplayProwess(this, spRank);
		this.WarriorsMight = new WarriorsMight(this, wmRank);
		this.AgileFighter = new AgileFighter(this, afRank);
		this.Sweep = new Sweep(this, sweepRank);
		this.Charge = new Charge(this, chargeRank);
		this.FlipStrike = new FlipStrike(this, fsRank);
		this.IntimidatingShout = new IntimidatingShout(this, isRank);
		this.Deflection = new Deflection(this, deflectRank);
		
		// Add Abilities to a list for Cooldown purposes
		this.abilities = new HashMap<>();
		this.abilities.put(SteelLegionWarrior.AbilityNames.VengeanceStrike, this.VengeanceStrike);
		this.abilities.put(SteelLegionWarrior.AbilityNames.SwordplayProwess, this.SwordplayProwess);
		this.abilities.put(SteelLegionWarrior.AbilityNames.WarriorsMight, this.WarriorsMight);
		this.abilities.put(SteelLegionWarrior.AbilityNames.AgileFighter, this.AgileFighter);
		this.abilities.put(SteelLegionWarrior.AbilityNames.Sweep, this.Sweep);
		this.abilities.put(SteelLegionWarrior.AbilityNames.Charge, this.Charge);
		this.abilities.put(SteelLegionWarrior.AbilityNames.FlipStrike, this.FlipStrike);
		this.abilities.put(SteelLegionWarrior.AbilityNames.IntimidatingShout, this.IntimidatingShout);
		this.abilities.put(SteelLegionWarrior.AbilityNames.Deflection, this.Deflection);
		
		// Add new commands for Abilities
		if (this.Sweep.rank() > 0) {
			this.addCommand(new AbilityCommand(this.Sweep));
		}
		if (this.Charge.rank() > 0) {
			this.addCommand(new AbilityCommand(this.Charge));
		}
		if (this.FlipStrike.rank() > 0) {
			this.addCommand(new AbilityCommand(this.FlipStrike));
		}
		if (this.IntimidatingShout.rank() > 0) {
			this.addCommand(new AbilityCommand(this.IntimidatingShout));
		}
		if (this.Deflection.rank() > 0) {
			this.addCommand(new AbilityCommand(this.Deflection));
		}
		
		// Finish creating the Deflection Ability (needed Intimidating Shout to be created)
		this.Deflection.setDuration();
	}
	public SteelLegionWarrior(Character copy, int vsRank, int spRank, int wmRank, int afRank, int sweepRank, int chargeRank, int fsRank, int isRank, int deflectRank) {
		this(copy.getName(), copy.getLevel(), copy.getHealth(), copy.getDamage(), copy.getArmor(), copy.getArmorPiercing(), copy.getAccuracy(), copy.getDodge(), copy.getBlock(), copy.getCriticalChance(), copy.getSpeed(), copy.getAttackSpeed(), copy.getRange(), copy.getThreat(), copy.getTacticalThreat(), copy.getSTDdown(), copy.getSTDup(), copy.getBaseDmgType(), copy.getResistances(), copy.getVulnerabilities(), copy.getType(), vsRank, spRank, wmRank, afRank, sweepRank, chargeRank, fsRank, isRank, deflectRank);
	}
	public SteelLegionWarrior(SteelLegionWarrior copy) {
		this(copy, copy.getAbilityRank(SteelLegionWarrior.AbilityNames.VengeanceStrike), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.SwordplayProwess), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.WarriorsMight), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.AgileFighter), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.Sweep), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.Charge), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.FlipStrike), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.IntimidatingShout), copy.getAbilityRank(SteelLegionWarrior.AbilityNames.Deflection));
	}
	
	
	// Functions for interaction between Abilities:
	// Functions to use an Ability
	public void useAbility(SteelLegionWarrior.AbilityNames name, int version) {
		Ability chosen = this.abilities.get(name);
		chosen.use(version);
	}
	public void useAbility(SteelLegionWarrior.AbilityNames name) {
		this.useAbility(name, 1);
	}
	
	// Function to set an ability's Cooldown
	public void setAbilityCD(SteelLegionWarrior.AbilityNames name, int turnsRemaining) {
		Ability chosen = this.abilities.get(name);
		chosen.setTurnsRemaining(turnsRemaining);
	}
	
	// Function to get the rank of an Ability
	public int getAbilityRank(SteelLegionWarrior.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.rank();
	}
	
	// Function to get the duration of an Ability
	public int getAbilityDuration(SteelLegionWarrior.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.getDuration();
	}
	
	// Function to get whether or not an Ability is active
	public boolean isAbilityActive(SteelLegionWarrior.AbilityNames name) {
		Ability chosen = this.abilities.get(name);
		return chosen.isActive();
	}
	
	// Other useful transitions to Ability calls
	// Returns a new attack that is the same as the original with the bonuses from the Deflection Ability
	public Attack getDeflectionVersion(Attack original) {
		return this.Deflection.getDeflectionVersion(original);
	}
	
	public void useVengeanceStrike(Character enemy) {
		if (enemy instanceof Enemy) {
			this.VengeanceStrike.execute((Enemy)enemy);
			return;
		}
		System.out.println("Warning: Vengeance Strike attempted to be called on a non-enemy which should not be possible.");
	}
	
	public void useVengeanceFlipStrike(Attack oriVenStr) {
		this.FlipStrike.useVengeanceStrikeVersion(oriVenStr);
	}
	
	
	// Overrides the prompt to give class conditions
	@Override
	public void promptClassConditionGive(Character other) {
		// Adds class Conditions to a list.
		LinkedList<Condition> classConditions = new LinkedList<>();
		classConditions.add(this.VengeanceStrike.getEnemyDamageReduction());
		classConditions.add(this.SwordplayProwess.getEmpoweredCondition());
		classConditions.add(this.WarriorsMight.getStun());
		classConditions.add(this.AgileFighter.getAbilityBlockBonus());
		classConditions.add(this.AgileFighter.getAbilityPreAttackBonus());
		classConditions.add(this.AgileFighter.getBaseBlockBonus());
		classConditions.add(this.AgileFighter.getBasePreAttackBonus());
		classConditions.add(this.Sweep.getSlow());
		classConditions.add(this.Charge.getTargetedPreAttackBonus());
		classConditions.add(this.IntimidatingShout.getSelfDefenseBonus());
		classConditions.add(this.IntimidatingShout.getTauntNormal());
		classConditions.add(this.IntimidatingShout.getTauntAdvanced());
		classConditions.add(this.IntimidatingShout.getTauntElite());
		classConditions.add(this.IntimidatingShout.getTauntBoss());
		classConditions.add(this.Deflection.getVsArmoredCondition());
		classConditions.add(this.Deflection.getStun());
		
		
		// Make a parallel String list for printing
		LinkedList<String> conditionStringList = new LinkedList<>();
		for (Condition c : classConditions) {
			conditionStringList.add(c.toString());
		}
		
		// Add chosen condition to the Character
		int choice = BattleSimulator.getInstance().promptSelect(conditionStringList);
		if (choice == 0) {
			return;
		}
		other.addCondition(classConditions.get(choice-1));
		
		conditionStringList.clear();
	}
	
	
	// Overrides the begin and end turn function of Character to include reducing the Cooldowns of Abilities.
	// Start of Turn override
	@Override
	protected void beginTurnSetup() {
		// Do the usual setup
		super.beginTurnSetup();
		
		// Reduces the Cooldown and/or duration of all Abilities that need it.
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.decrementTurnsRemaining();
			}
		}
	}
	
	// End of Turn Override
	@Override
	public void endTurnSetup() {
		// Normal Setup
		super.endTurnSetup();
		
		// If we're dead, we're done
		if (this.isDead()) {
			return;
		}
		
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.endTurnEffects();
			}
		}
	}
	
	// Overrides pre and post attack effects to apply Ability effects
	protected void applyPreAttackEffects(Attack atk) {
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.applyPreAttackEffects(atk);
			}
		}
	}
	
	protected void applyPostAttackEffects(AttackResult atkRes) {
		// Ability Effects
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				a.applyPostAttackEffects(atkRes);
			}
		}
	}
	
	// Overrides the getStatStrings to include the classification
	@Override
	public String getStatStrings() {
		return "Class: Steel Legion Warrior\n" + super.getStatStrings();
	}
	
	// Does a full display of the Steel Legion Warrior including Abilities
	public String getDescription() {
		String ret = super.getDescription();
		for (Ability a : abilities.values()) {
			if (a.rank() > 0) {
				ret += "\n" + a.getDescription();
			}
		}
		return ret;
	}
}



// Builds a Steel Legion Warrior
class SteelLegionWarriorBuilder extends CharacterBuilder {
	// Creates all the Ability fields
	private int VengeanceStrikeRank;
	private int SwordplayProwessRank;
	private int WarriorsMightRank;
	private int AgileFighterRank;
	private int SweepRank;
	private int ChargeRank;
	private int FlipStrikeRank;
	private int IntimidatingShoutRank;
	private int DeflectionRank;
	
	// Constructs a SteelLegionTankBuilder (populating the CharacterBuilder variables) based on the constant stats from Character
	public SteelLegionWarriorBuilder(Character base) {
		super(base);
		this.VengeanceStrikeRank = 0;
		this.SwordplayProwessRank = 0;
		this.WarriorsMightRank = 0;
		this.AgileFighterRank = 0;
		this.SweepRank = 0;
		this.ChargeRank = 0;
		this.FlipStrikeRank = 0;
		this.IntimidatingShoutRank = 0;
		this.DeflectionRank = 0;
	}
	public SteelLegionWarriorBuilder(SteelLegionWarrior base) {
		super(base);
		this.VengeanceStrikeRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.VengeanceStrike);
		this.SwordplayProwessRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.SwordplayProwess);
		this.WarriorsMightRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.WarriorsMight);
		this.AgileFighterRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.AgileFighter);
		this.SweepRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.Sweep);
		this.ChargeRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.Charge);
		this.FlipStrikeRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.FlipStrike);
		this.IntimidatingShoutRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.IntimidatingShout);
		this.DeflectionRank = base.getAbilityRank(SteelLegionWarrior.AbilityNames.Deflection);
	}
	public SteelLegionWarriorBuilder() {
		this(Character.STEEL_LEGION_WARRIOR);
	}
	
	// Overrides the functions necessary from CharacterBuilder when constant stats are given.
	@Override
	public SteelLegionWarriorBuilder Name(String name) {
		super.Name(name);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder Level(int level) {
		super.Level(level);
		return this;
	}
	
	@Override
	public SteelLegionWarriorBuilder bonusHealth(int bonusHealth) {
		super.bonusHealth(bonusHealth);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusDamage(int bonusDamage) {
		super.bonusDamage(bonusDamage);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusArmor(int bonusArmor) {
		super.bonusArmor(bonusArmor);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusArmorPiercing(int bonusArmorPiercing) {
		super.bonusArmorPiercing(bonusArmorPiercing);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusAccuracy(int bonusAccuracy) {
		super.bonusAccuracy(bonusAccuracy);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusBlock(int bonusBlock) {
		super.bonusBlock(bonusBlock);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusCriticalChance(int bonusCriticalChance) {
		super.bonusCriticalChance(bonusCriticalChance);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusSpeed(int bonusSpeed) {
		super.bonusSpeed(bonusSpeed);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusAttackSpeed(int bonusAttackSpeed) {
		super.bonusAttackSpeed(bonusAttackSpeed);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusThreat(int bonusThreat) {
		super.bonusThreat(bonusThreat);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusTacticalThreat(int bonusTacticalThreat) {
		super.bonusTacticalThreat(bonusTacticalThreat);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusSTDdown(int bonusSTDdown) {
		super.bonusSTDdown(bonusSTDdown);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder bonusSTDup(int bonusSTDup) {
		super.bonusSTDup(bonusSTDup);
		return this;
	}
	
	@Override
	public SteelLegionWarriorBuilder baseDmgType(Attack.DmgType dmgType) {
		super.baseDmgType(dmgType);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder addResistance(Attack.DmgType resistance, double value) {
		super.addResistance(resistance, value);
		return this;
	}
	@Override
	public SteelLegionWarriorBuilder addVulnerability(Attack.DmgType vulnerability, double value) {
		super.addVulnerability(vulnerability, value);
		return this;
	}
	
	@Override
	public SteelLegionWarriorBuilder Type(Character.Type type) {
		super.Type(type);
		return this;
	}
	
	
	// Sets the ranks of each Ability (then defines the base Cooldown and Scaler based on that)
	// Vengeance Strike (Passive Ability)
	public SteelLegionWarriorBuilder setVengeanceStrikeRank(int newRank) {
		this.VengeanceStrikeRank = newRank;
		return this;
	}
	// Swordplay Prowess (Passive Ability):
	public SteelLegionWarriorBuilder setSwordplayProwessRank(int newRank) {
		this.SwordplayProwessRank = newRank;
		return this;
	}
	// Warriors Might (Passive Ability):
	public SteelLegionWarriorBuilder setWarriorsMightRank(int newRank) {
		this.WarriorsMightRank = newRank;
		return this;
	}
	// Agile Fighter (Passive Ability):
	public SteelLegionWarriorBuilder setAgileFighterRank(int newRank) {
		this.AgileFighterRank = newRank;
		return this;
	}
	
	// Sweep (Ability 1):
	public SteelLegionWarriorBuilder setSweepRank(int newRank) {
		this.SweepRank = newRank;
		return this;
	}
	// Charge (Ability 2):
	public SteelLegionWarriorBuilder setChargeRank(int newRank) {
		this.ChargeRank = newRank;
		return this;
	}
	// Flip Strike (Ability 3):
	public SteelLegionWarriorBuilder setFlipStrikeRank(int newRank) {
		this.FlipStrikeRank = newRank;
		return this;
	}
	// Intimidating Shout (Ability 4):
	public SteelLegionWarriorBuilder setIntimidatingShoutRank(int newRank) {
		this.IntimidatingShoutRank = newRank;
		return this;
	}
	// Taunting Attack (ULTIMATE):
	public SteelLegionWarriorBuilder setDeflectionRank(int newRank) {
		this.DeflectionRank = newRank;
		return this;
	}
	
	
	// Calculates the base stats based on level and stat-increasing passive abilities
	private void setBaseStats() {
		// Each stat is already set to its level 1 base value
		// Note: below only occurs if the specified a level, since the base level is 0.
		// "Level Up" each stat: (Multiply by the given multiplier for each level up to the current level)
		for (int counter = 2; counter <= this.Level; counter++) {
			// All these only increment at intervals of 5
			if (counter % 5 == 0) {
				this.Armor = (int)Math.round(this.Armor * 1.05);
				this.ArmorPiercing = (int)Math.round(this.ArmorPiercing * 1.05);
				this.Accuracy = (int)Math.round(this.Accuracy * 1.05);
				this.Block = (int)Math.round(this.Block * 1.05);
			}
			
			// Dynamically increasing stats
			// Health and Damage have various changes at intervals of 5 and 10
			if (counter % 10 == 0) {
				this.Health = (int)Math.round(this.Health * 1.05);
				this.Damage = (int)Math.round(this.Damage * 1.05);
			}
			else if (counter % 5 == 0) {
				this.Health = (int)Math.round(this.Health * 1.05);
				this.Damage = (int)Math.round(this.Damage * 1.03);
			}
			else {
				this.Health = (int)Math.round(this.Health * 1.03);
				this.Damage = (int)Math.round(this.Damage * 1.03);
			}
			
			// Attack Speed increases with various amounts at the given levels
			if (counter == 20) {
				this.AttackSpeed += 2;
			}
			if (counter == 40) {
				this.AttackSpeed += 2;
			}
			if (counter == 60) {
				this.AttackSpeed += 3;
			}
			if (counter == 80) {
				this.AttackSpeed += 4;
			}
			if (counter == 100) {
				this.AttackSpeed += 5;
			}
			
			// Threat increases with various amounts at the given levels
			if (counter == 10) {
				this.Threat += 3;
			}
			if (counter == 30) {
				this.Threat += 4;
			}
			if (counter == 50) {
				this.Threat += 5;
			}
			if (counter == 70) {
				this.Threat += 6;
			}
			if (counter == 90) {
				this.Threat += 7;
			}
		}
		
		// Calculate the bonus stats given by certain Abilities
		if (this.SwordplayProwessRank > 0) {
			SwordplayProwess sp = new SwordplayProwess(this.SwordplayProwessRank);
			this.bDamage += sp.getDamageBonus();
			this.bArmorPiercing += sp.getArmorPiercingBonus();
		}
		if (this.WarriorsMightRank > 0) {
			WarriorsMight wm = new WarriorsMight(this.WarriorsMightRank);
			this.bHealth += wm.getHealthBonus();
			this.bArmor += wm.getArmorBonus();
			this.bThreat += wm.getThreatBonus();
			this.bDamage += wm.getDamageBonus();
		}
		if (this.AgileFighterRank >= 5) {
			AgileFighter af = new AgileFighter(this.AgileFighterRank);
			this.bSpeed += af.getSpeedBonus();
		}
	}
	
	// Finishes the build by returning a SteelLegionWarrior Character
	public SteelLegionWarrior build() {
		// Set the base stats for the level and ability ranks
		this.setBaseStats();
		
		// Return the Steel Legion Warrior
		return new SteelLegionWarrior(super.build(), this.VengeanceStrikeRank, this.SwordplayProwessRank, this.WarriorsMightRank, this.AgileFighterRank, this.SweepRank, this.ChargeRank, this.FlipStrikeRank, this.IntimidatingShoutRank, this.DeflectionRank);
	}
}
