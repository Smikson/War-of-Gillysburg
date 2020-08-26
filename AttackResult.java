package WyattWitemeyer.WarOfGillysburg;

import java.util.*;


public class AttackResult {
	// Constant for Empty Attacks
	public static final AttackResult EMPTY = new AttackResult();
	
	// Variables for each element of an attack result
	private Character attacker;
	private Character defender;
	private Attack.DmgType type;
	private Attack.RangeType range;
	private boolean isTargeted;
	private boolean didHit;
	private boolean didCrit;
	private int damageDealt;
	private boolean didKill;
	private LinkedList<AttackResult> attachedAttackResults;
	
	// Constructors
	public AttackResult(Character attacker, Character defender, Attack.DmgType atkType, Attack.RangeType range, boolean isTargeted, boolean didHit, boolean didCrit, int damageDealt, boolean didKill, LinkedList<AttackResult> attachedAttackResults) {
		this.attacker = attacker;
		this.defender = defender;
		this.type = atkType;
		this.range = range;
		this.isTargeted = isTargeted;
		this.didHit = didHit;
		this.didCrit = didCrit;
		this.damageDealt = damageDealt;
		this.didKill = didKill;
		this.attachedAttackResults = attachedAttackResults;
	}
	public AttackResult() {
		this.attacker = Character.EMPTY;
		this.defender = Character.EMPTY;
		this.type = Attack.DmgType.TRUE;
		this.range = Attack.RangeType.OTHER;
		this.isTargeted = true;
		this.didHit = false;
		this.didCrit = false;
		this.damageDealt = 0;
		this.didKill = false;
		this.attachedAttackResults = new LinkedList<>();
	}
	
	// Get methods of each element
	public Character getAttacker() {
		return this.attacker;
	}
	public Character getDefender() {
		return this.defender;
	}
	public Attack.DmgType getDmgType() {
		return this.type;
	}
	public Attack.RangeType getRangeType() {
		return this.range;
	}
	public boolean isTargeted() {
		return this.isTargeted;
	}
	public boolean isAOE() {
		return !this.isTargeted();
	}
	public boolean didHit() {
		return this.didHit;
	}
	public boolean didCrit() {
		return this.didCrit;
	}
	public int getDamageDealt() {
		return this.damageDealt;
	}
	public boolean didKill() {
		return this.didKill;
	}
	public LinkedList<AttackResult> getAttachedAttackResults() {
		return this.attachedAttackResults;
	}
	
	// To String methods for an AttackResult when acting as an "attached" attack result, and overriding toString for general usage
	public String attachedToString() {
		// Initialize the return String
		String ret = "";
		
		// Add each piece of this attached attack result to the return String
		ret += this.getAttacker().getName() + "\'s attached attack ";
		if (this.didHit()) {
			if (this.didCrit()) {
				ret += "CRITICALLY HIT " + this.getDefender().getName();
			}
			else {
				ret += "hit " + this.getDefender().getName();
			}
			
			ret += " for a total of " + this.getDamageDealt() + " " + this.getDmgType() + " damage!";
		}
		else {
			ret += "missed " + this.getDefender().getName() + "!";
		}
		
		// Return the result
		return ret;
	}
	@Override
	public String toString() {
		// Initialize the return String
		String ret = "";
		
		// Add each piece of this attack result to the return String
		ret += this.getAttacker().getName();
		if (!this.didHit()) {
			ret += " missed " + this.getDefender().getName() + "!";
			
			// Add each piece of each attached attack result
			for (AttackResult attached : this.getAttachedAttackResults()) {
				ret += "\n" + attached.attachedToString();
			}
			
			return ret;
		}
		
		if (this.didCrit()) {
			ret += " scored a CRITICAL HIT against " + this.getDefender().getName();
		}
		else {
			ret += " hit " + this.getDefender().getName();
		}
		
		ret += " for a total of " + this.getDamageDealt() + " " + this.getDmgType() + " damage!";
		
		// Add each piece of each attached attack result
		for (AttackResult attached : this.getAttachedAttackResults()) {
			ret += "\n" + attached.attachedToString();
		}
		
		// Add the death piece if appropriate
		if (this.didKill()) {
			Dice funDie = new Dice(12);
			String funWord = "";
			
			switch(funDie.roll()) {
				case 1: {
					funWord = " utterly annihilated ";
					break;
				}
				case 2: {
					funWord = " defeated ";
					break;
				}
				case 3: {
					funWord = " totally obliterated ";
					break;
				}
				case 4: {
					funWord = " purged the universe of ";
					break;
				}
				case 5: {
					funWord = " completely destroyed ";
					break;
				}
				case 6: {
					funWord = " slew ";
					break;
				}
				case 7: {
					funWord = " POWERFULLY OVERWHELMED ";
					break;
				}
				case 8: {
					funWord = " vaporized all life from ";
					break;
				}
				case 9: {
					funWord = " thoroughly eradicated ";
					break;
				}
				case 10: {
					funWord = " effectively dominated ";
					break;
				}
				case 11: {
					funWord = " absolutely decimated ";
					break;
				}
				case 12: {
					funWord = " killed ";
					break;
				}
			}
			
			ret += "\n" + this.getAttacker().getName() + funWord + this.getDefender().getName() + "!";
		}
		// Otherwise, tell the amount of Health remaining on the defender
		else {
			ret += "\n" + this.getDefender().getName() + " has " + this.getDefender().getCurrentHealth() + " Health remaining.";
		}
		
		return ret;
	}
}

class AttackResultBuilder {
	// Variables for each element of an attack result
	private Character attacker;
	private Character defender;
	private Attack.DmgType type;
	private Attack.RangeType range;
	private boolean isTargeted;
	private boolean didHit;
	private boolean didCrit;
	private int damageDealt;
	private boolean didKill;
	private LinkedList<AttackResult> attachedAttackResults;
	
	// Constructors
	public AttackResultBuilder(AttackResult atk) {
		this.attacker = atk.getAttacker();
		this.defender = atk.getDefender();
		this.type = atk.getDmgType();
		this.range = atk.getRangeType();
		this.isTargeted = atk.isTargeted();
		this.didHit = atk.didHit();
		this.didCrit = atk.didCrit();
		this.damageDealt = atk.getDamageDealt();
		this.didKill = atk.didKill();
		this.attachedAttackResults = atk.getAttachedAttackResults();
	}
	public AttackResultBuilder() {
		this(AttackResult.EMPTY);
	}
	
	
	// Methods for build process
	public AttackResultBuilder attacker(Character attacker) {
		this.attacker = attacker;
		return this;
	}
	
	public AttackResultBuilder defender(Character defender) {
		this.defender = defender;
		return this;
	}
	
	public AttackResultBuilder type(Attack.DmgType atkType) {
		this.type = atkType;
		return this;
	}
	
	public AttackResultBuilder range(Attack.RangeType range) {
		this.range = range;
		return this;
	}
	
	public AttackResultBuilder isTargeted(boolean isTargeted) {
		this.isTargeted = isTargeted;
		return this;
	}
	public AttackResultBuilder isTargeted() {
		return this.isTargeted(true);
	}
	public AttackResultBuilder isAOE() {
		return this.isTargeted(false);
	}
	
	public AttackResultBuilder didHit(boolean didHit) {
		this.didHit = didHit;
		return this;
	}
	
	public AttackResultBuilder didCrit(boolean didCrit) {
		this.didCrit = didCrit;
		return this;
	}
	
	public AttackResultBuilder damageDealt(int damageDealt) {
		this.damageDealt = damageDealt;
		return this;
	}
	
	public AttackResultBuilder didKill(boolean didKill) {
		this.didKill = didKill;
		return this;
	}
	
	public AttackResultBuilder addAttachedAttackResult(AttackResult attached) {
		this.attachedAttackResults.add(attached);
		return this;
	}
	
	public AttackResult build() {
		return new AttackResult(this.attacker, this.defender, this.type, this.range, this.isTargeted, this.didHit, this.didCrit, this.damageDealt, this.didKill, this.attachedAttackResults);
	}
}
