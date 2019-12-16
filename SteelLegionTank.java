package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

import WyattWitemeyer.WarOfGillysburg.Character;

public class SteelLegionTank extends Character {
	// Passive Abilities
	private HoldItRightThere HoldItRightThere; // Unique Passive Ability (UPA)
	private EnchantedArmor EnchantedArmor;
	private ShieldSkills ShieldSkills;
	private ProfessionalLaughter ProfessionalLaughter;
	
	// Base Abilities
	private ShieldBash ShieldBash;
	private ShieldReflection ShieldReflection;
	private TauntingAttack TauntingAttack;
	private LeaderStrike LeaderStrike;
	private HaHaHaYouCantKillMe HaHaHaYouCantKillMe;
	
	// A List of all Abilities so all Cooldowns can be reduced at once
	private LinkedList<Ability> abilities;
	
	// These first two methods help set up the Steel Legion Tank subclass.
	public SteelLegionTank(String nam, int lvl, int hp, int dmg, int arm, int armp, int acc, int dod, int blk, int crit, int spd, int atkspd, int range, int thrt, int tactthrt, int stdDown, int stdUp, HashSet<String> resis, HashSet<String> vuls, LinkedList<String> aType, int upaRank, int eArmorRank, int sSkillsRank, int profLaughRank, int sBashRank, int sReflectRank, int tAttackRank, int lStrikeRank, int haRank) {
		// Calls the super constructor to create the Character, then initializes all Abilities according to their specifications.
		super(nam, lvl, hp, dmg, arm, armp, acc, dod, blk, crit, spd, atkspd, range, thrt, tactthrt, stdDown, stdUp, resis, vuls, aType);
		this.HoldItRightThere = new HoldItRightThere(upaRank);
		this.EnchantedArmor = new EnchantedArmor(eArmorRank);
		this.ShieldSkills = new ShieldSkills(sSkillsRank);
		this.ProfessionalLaughter = new ProfessionalLaughter(profLaughRank);
		this.ShieldBash = new ShieldBash(sBashRank, sSkillsRank);
		this.ShieldReflection = new ShieldReflection(sReflectRank, sSkillsRank);
		this.TauntingAttack = new TauntingAttack(tAttackRank, sSkillsRank);
		this.LeaderStrike = new LeaderStrike(lStrikeRank, sSkillsRank);
		this.HaHaHaYouCantKillMe = new HaHaHaYouCantKillMe(haRank);
		
		abilities.add(this.HoldItRightThere);
		abilities.add(this.EnchantedArmor);
		abilities.add(this.ShieldSkills);
		abilities.add(this.ProfessionalLaughter);
		abilities.add(this.ShieldBash);
		abilities.add(this.ShieldReflection);
		abilities.add(this.TauntingAttack);
		abilities.add(this.LeaderStrike);
		abilities.add(this.HaHaHaYouCantKillMe);
	}
	public SteelLegionTank(Character copy) {
		super(copy);
	}
	
	// Overrides the begin and end turn function of Character to include reducing the Cooldowns of Abilities.
	@Override
	public String beginTurn(LinkedList<Character> combatants) {
		// Does the same as all Characters
		String ret = super.beginTurn(combatants);
		
		// Checks to see if it is the beginning of the round (Rank 15 of Professional Laughter gives Taunt)
		if (BattleSimulator.getInstance().getRound() == 1 && this.ProfessionalLaughter.rank() >= 15) {
			ret += "You have \"Taunt\" for 2 rounds.\n";
		}
		
		// Reduces the Cooldown of all Abilities that need it.
		for (Ability a : abilities) {
			if (a.onCooldown()) {
				a.incrementTurn();
			}
		}
		
		// Displays possible  actions to be taken
		ret += "Possible Actions:\n";
		for (String option : this.commands()) {
			ret += option + "\n";
		}
		
		return ret;
	}
	
	@Override
	public LinkedList<String> commands() {
		LinkedList<String> ret = super.commands();
		ret.add(1, "Shield Bash");
		ret.add(2, "Shield Reflection");
		ret.add(3, "Taunting Attack");
		ret.add(4, "Leader Strike");
		ret.add(5, "HaHaHaYouCantKillMe");
		
		return ret;
	}
	
	@Override
	public String endTurn(LinkedList<Character> combatants) {
		String ret = this.useEnchantedArmorHealing();
		ret += super.endTurn(combatants);
		return ret;
	}
	
	// Overrides "avoidAttack" in order to also store the fact that an attack was blocked in "Shield Bash" and "Shield Reflection"
	@Override
	protected void avoidAttack(Attack atk) {
		super.avoidAttack(atk);
		this.ShieldBash.didBlock = true;
		this.ShieldReflection.didBlock = true;
	}
	
	// Methods to use "Hold It Right There" Unique Passive
	public StatusEffect getHoldItRightThereBlockBonus() {
		return this.HoldItRightThere.getBlockBonus();
	}
	public void useHoldItRightThereHaltCondition(Character enemy) {
		enemy.addCondition(this.HoldItRightThere.getDamageBonus());
	}
	
	// Deals with the healing from the "Enchanted Armor" Passive
	public String useEnchantedArmorHealing() {
		// Calculates the amount of healing based on Maximum Health
		int healing = (int) Math.round(this.EnchantedArmor.getScaler() * this.getHealth());
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	
	// Deals with the healing (and rank 15 Cooldown) portion of the "Shield Skills" Passive from hitting multiple enemies with "Shield Reflection"
	private String useShieldSkillsHealing(int numBlinded) {
		// First, non-healing-wise, if 7 enemies are hit at rank 15, the Cooldown of Shield Bash is refreshed.
		if (this.ShieldSkills.rank() >= 15 && numBlinded >= 7) {
			this.ShieldBash.turnCount = ShieldBash.cooldown();
		}
		
		// Calculates the amount of healing based on missing Health and the number of enemies blinded
		int healing = 0;
		if ((this.ShieldSkills.rank() < 11 && numBlinded >= 3) || (this.ShieldSkills.rank() >= 11 && numBlinded >= 3 && numBlinded < 5)) {
			healing = (int) Math.round(this.ShieldSkills.getScalerBlind3() * (this.getHealth() - this.getCurrentHealth()));
		}
		else if (this.ShieldSkills.rank() >= 11 && numBlinded >= 5){
			healing = (int) Math.round((this.ShieldSkills.getScalerBlind3() + this.ShieldSkills.getScalerBlind5()) * (this.getHealth() - this.getCurrentHealth()));
		}
		
		// Restores the health to this character (and stores correct healing amount if over), then returns the effects.
		healing = this.restoreHealth(healing);
		if (healing == 0) {
			return "";
		}
		return this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth();
	}
	
	// Deals the Damage from the "Shield Bash" Ability (Ability 1)
	public String useShieldBash(Character enemy) {
		// Before anything, put Shield Bash "on Cooldown"
		this.ShieldBash.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Apply bonus pre-conditions (will have 0 value if rank is not big enough)
		StatusEffect accuracyBonus = this.ShieldBash.getAccuracyBonus();
		StatusEffect critBonus = this.ShieldBash.getCritBonus();
		this.applySE(accuracyBonus);
		this.applySE(critBonus);
		
		// Make the attack
		ret += this.attack(enemy, this.ShieldBash.getScaler());
		
		// Unapply the bonus pre-conditions
		this.unapplySE(accuracyBonus);
		this.unapplySE(critBonus);
		
		// Change the didCrit of "Shield Bash" to match if the Ability critically struck (this may affect the effects below when received)
		this.ShieldBash.didCrit = this.previousAttack().didCrit();
		
		// If the attack hit, apply all the conditions (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.previousAttack().didHit()) {
			// Add all conditions to enemy hit
			Stun stunEffect = this.ShieldBash.getStunEffect();
			StatusEffect damageBonus = this.ShieldBash.getDamageBonus();
			StatusEffect accuracyReduction = this.ShieldBash.getAccuracyReduction();
			StatusEffect secondAccuracyReduction = this.ShieldBash.getSecondAccuracyReduction();
			enemy.addCondition(stunEffect);
			enemy.addCondition(damageBonus);
			enemy.addConsecutiveCondition(stunEffect, accuracyReduction);
			enemy.addConsecutiveCondition(accuracyReduction, secondAccuracyReduction);
			
			// Change numMisses back to 0
			this.ShieldBash.numMisses = 0;
		}
		// If the attack missed, check to see if its Cooldown is reduced by "Shield Skills" and increment numMisses
		else {
			// Each point in "Shield Skills" causes a reduction of 1 up to a maximum of the Cooldown itself
			int cdr = this.ShieldSkills.rank();
			if (cdr > this.ShieldBash.cooldown()) {
				cdr = this.ShieldBash.cooldown();
			}
			this.ShieldBash.turnCount = cdr;
			
			// Increment numMisses
			this.ShieldBash.numMisses++;
		}
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") and didCrit to false (using this ability consumes the buff if present)
		this.ShieldBash.didBlock = false;
		this.ShieldReflection.didBlock = false;
		this.ShieldBash.didCrit = false;
		
		// Return results
		return ret;
	}
	
	// Deals the Damage from the "Shield Reflection" Ability (Ability 2) to multiple enemies
	public String useShieldReflection(List<Enemy> enemies, List<Enemy> blinded) {
		// Before anything, put Shield Reflection "on Cooldown"
		this.ShieldReflection.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Make the attack against all enemies affected
		for (Character enemy : enemies) {
			ret += this.attack(enemy, this.ShieldReflection.getScaler(), false) + "\n"; // false - indicates an AOE attack
		}
		
		// Blind all enemies affected
		for (Character enemy : blinded) {
			enemy.addCondition(this.ShieldReflection.getBlindEffect());
		}
		
		// Add the possible healing string based on bonus effects from the "Shield Skills" Ability (will be empty String if nothing happens)
		ret += this.useShieldSkillsHealing(blinded.size());
		
		// Reset didBlock (of "Shield Bash" and "Shield Reflection") to false (using this ability consumes the buff if present)
		this.ShieldBash.didBlock = false;
		this.ShieldReflection.didBlock = false;
		
		// Return the result
		return ret;
	}
	public String useShieldReflection(List<Enemy> enemies) {
		return this.useShieldReflection(enemies, enemies);
	}
	
	// Deals the Damage from the "Taunting Attack" Ability (Ability 3)
	public String useTauntingAttack(Character enemy) {
		// Before anything, put Tauning Attack "on Cooldown"
		this.TauntingAttack.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		StatusEffect accuracyBonus = this.TauntingAttack.getAccuracyBonus();
		this.applySE(accuracyBonus);
		
		// Make the attack
		ret += this.attack(enemy, this.TauntingAttack.getScaler());
		
		// Unapply the bonus accuracy pre-condition
		this.unapplySE(accuracyBonus);
		
		// If the attack hit, apply all the taunt condition (will be 0 if not effective due to rank) and revert numMisses to 0
		if (this.previousAttack().didHit()) {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.TauntingAttack.getTauntEffectHit();
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				ret += "\n" + enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!";
			}
			
			// Change numMisses back to 0
			this.TauntingAttack.numMisses = 0;
		}
		// If the attack missed, still apply the taunt condition if it is effective (will be 0 if not) and increment numMisses
		else {
			// Add taunt condition to enemy hit
			Condition tauntEffect = this.TauntingAttack.getTauntEffectMiss(enemy.getLevel());
			if (tauntEffect.duration() > 0) {
				enemy.addCondition(tauntEffect);
				ret += "\n" + enemy.getName() + " is also taunted for " + tauntEffect.duration() + " turns!";
			}
			
			// Increment numMisses
			this.TauntingAttack.numMisses++;
		}
		
		// Return the result
		return ret;
	}
	
	// Deals the Damage from the "Leader Strike" Ability (Ability 4) and Calculates the amount healed for allies.
	public String useLeaderStrike(Character enemy, List<Character> allies) {
		// Before anything, put Tauning Attack "on Cooldown"
		this.LeaderStrike.resetCounter();
		
		// Initialize return String
		String ret = "";
		
		// Apply bonus accuracy pre-condition (will have 0 value if rank is not big enough)
		StatusEffect accuracyBonus = this.LeaderStrike.getAccuracyBonus();
		this.applySE(accuracyBonus);
		
		// Make the attack
		ret += this.attack(enemy, this.LeaderStrike.getScaler());
		
		// Unapply the bonus accuracy pre-condition
		this.unapplySE(accuracyBonus);
		
		// If the attack hit revert numMisses to 0, if it missed, increment numMisses
		if (this.previousAttack().didHit()) {
			this.LeaderStrike.numMisses = 0;
		}
		else {
			this.LeaderStrike.numMisses++;
		}
		
		
		// Past rank 3, this Character is included for the buffs in "allies", either way, create a copy of the list so the original is unchanged
		LinkedList<Character> alliesCopy = new LinkedList<>();
		for (Character ally : allies) {
			alliesCopy.add(ally);
		}
		if (!alliesCopy.contains(this) && this.LeaderStrike.rank() >= 3) {  // Adds this if not present and should be
			alliesCopy.add(this);
		}
		if (alliesCopy.contains(this) && this.LeaderStrike.rank() < 3) {  // Removes this if present and should not be
			alliesCopy.remove(this);
		}
		
		// Calculates and adds the amount of Healing received for each ally affected by the Ability (in the list) and apply the damage boost
		for (Character ally : alliesCopy) {
			// Calculates the healing amount for the ally in the list
			int healing = (int)Math.round(ally.getHealth() * this.LeaderStrike.getHealingScaler());
			
			healing = ally.restoreHealth(healing);
			ret += ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth() + "\n";
			
			// Applies the damage boost to each ally affected
			ally.addCondition(this.LeaderStrike.getDamageBonus());
			
			// Checks to see if each ally's attack will stun the next target, and adds it to the return if so.
			if (this.LeaderStrike.willStun()) {
				ret += ally.getName() + " will also stun the next target hit\n";
			}
		}
		
		return ret;
	}
	
	
	// Restores the Character to full health and grants bonuses from the ULTIMATE Ability "HaHaHa You Can't Kill Me!"
	public String useHahahaYouCantKillMe() {
		// Initialize the return String
		String ret = "";
		
		// Heal to full Health
		int healing = this.getHealth() - this.getCurrentHealth();
		healing = this.restoreHealth(healing);
		ret += this.getName() + " healed for " + healing + " Health for a new total of " + this.getCurrentHealth() + "\n";
		
		// Apply Additional Conditions
		this.addCondition(this.HaHaHaYouCantKillMe.getArmorBonus());
		this.addCondition(this.HaHaHaYouCantKillMe.getDamageReduction());
		
		return ret;
	}
	
	// Gives all allies the appropriate buffs if you die while "HaHaHa You Can't Kill Me!" is active
	public String useDeathHaHaHaYouCantKillMe(List<Character> allies) {
		// Initialize the return String
		String ret = "";
		
		// All this only happens at rank 3
		if (this.HaHaHaYouCantKillMe.rank() >= 3) {
			// Restore each ally for 25% of their max Health and give each ally the damage buff from the Ability
			for (Character ally : allies) {
				// Calculates the healing amount for the ally in the list
				int healing = (int)Math.round(ally.getHealth() * .25);
				
				healing = ally.restoreHealth(healing);
				ret += ally.getName() + " healed for " + healing + " Health for a new total of " + ally.getCurrentHealth() + "\n";
				
				// Gives each ally the Damage buff and Invincibility for 1 turn
				ally.addCondition(this.HaHaHaYouCantKillMe.getDamageBonus());
				ally.addCondition(new Invincible("HaHaHa You Can't Kill Me Invincibility", 1));
			}
		}
		
		// Return the result
		return ret;
	}
}
