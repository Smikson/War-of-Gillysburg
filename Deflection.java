package WyattWitemeyer.WarOfGillysburg;

import java.util.*;

public class Deflection extends UltimateAbility {
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
		
		// Make this an active ability
		int duration = this.owner.getAbilityDuration(SteelLegionWarrior.AbilityNames.IntimidatingShout) + 1;
		if (this.rank() >= 3) {
			duration += 1;
		}
		this.makeActiveAbility(duration, true);
		
		// Initialize Helper Variables
		this.hasBounced = false;
		this.damageTaken = 0;
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
		return this.stunEffect;
	}
	public Condition getVsArmoredCondition() {
		return this.vsArmored;
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
		AttackBuilder ret = new AttackBuilder(original).addAttachedAttack(electricAttached.attachedAlteration(hitAndCrit).build());
		if (this.rank() >= 2) {
			ret.ignoresArmor();
		}
		return ret.build();
	}
	// Applies pre attack effects of this Ability with the proper attacker accuracy reductions to guarantee a miss
	public void applyDeflectionPreAttackEffects(Attack atk) {
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
	public void applyDeflectionPostAttackEffects(AttackResult atkRes) {
		// When attacking there are stun effects, and at rank 3, electric attacks bounce, allowing additional attacks to get performed
		if (this.getOwner().equals(atkRes.getAttacker()) ) {
			// Checks to see if any electric attacks occurred
			boolean prevElectric = atkRes.getDmgType().equals(Attack.DmgType.ELECTRIC);
			for (AttackResult attached : atkRes.getAttachedAttackResults()) {
				if (attached.getDmgType().equals(Attack.DmgType.ELECTRIC) && attached.didHit()) {
					prevElectric = true;
				}
			}
			
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
				atkRes.getDefender().addCondition(this.getStun());
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
		Attack basicAtk = new AttackBuilder().attacker(owner).type(this.getOwner().getBaseDmgType()).range(this.getOwner().getRangeType()).build();
		BasicAttackCommand altered = new BasicAttackCommand(this.getOwner(), this.getDeflectionVersion(basicAtk));
		this.getOwner().alterBasicAttack(altered);
		
		// Use Intimidating Shout Version 2
		this.getOwner().useAbility(SteelLegionWarrior.AbilityNames.IntimidatingShout, 2);
		
		// Override deactivate function when over or something? Attack attack and the healing thing also restore basic attack command
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
}
