package WyattWitemeyer.WarOfGillysburg;

// Classes that extend CrowdControl essentially act as Conditions.
// However this class acts as a way to group these types together and provide static versions to be used by Character's "AddCondition".
// There are 7 types of Crowd Control: Blind, Invincible, Invulnerable, Slow, Snare, Stasis, and Stun.
public class CrowdControl extends Condition {
	// List of static Crowd Controls to be used in Character's "Add Condition".
	public final static Blind BLIND = getBlind();
	public final static Invincible INVINCIBLE = getInvincible();
	public final static Invulnerable INVULNERABLE = getInvulnerable();
	public final static Slow SLOW = getSlow();
	public final static Snare SNARE = getSnare();
	public final static Stasis STASIS = getStasis();
	public final static Stun STUN = getStun();
	
	// Constructors - Acts essentially the same as a Condition
	public CrowdControl(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public CrowdControl(String name, int duration) {
		super(name, duration);
	}
	public CrowdControl() {
		super();
	}
	
	// Creates each static version
	private final static Blind getBlind() {
		Blind staticBlind = new Blind("Static Blind", -1);
    	staticBlind.makePermanent();
		return staticBlind;
	}
	private final static Invincible getInvincible() {
		Invincible staticInvincibility = new Invincible("Static Invincibility", -1);
    	staticInvincibility.makePermanent();
		return staticInvincibility;
	}
	private final static Invulnerable getInvulnerable() {
		Invulnerable staticInvulnerability = new Invulnerable("Static Invulnerability", -1);
    	staticInvulnerability.makePermanent();
		return staticInvulnerability;
	}
	private final static Slow getSlow() {
		Slow staticSlow = new Slow("Static Slow", -1, 2);
    	staticSlow.makePermanent();
		return staticSlow;
	}
	private final static Snare getSnare() {
		Snare staticSnare = new Snare("Static Snare", -1);
    	staticSnare.makePermanent();
		return staticSnare;
	}
	private final static Stasis getStasis() {
		Stasis staticStasis = new Stasis("Static Stasis", -1);
    	staticStasis.makePermanent();
		return staticStasis;
	}
	private final static Stun getStun() {
		Stun staticStun = new Stun("Static Stun", -1);
    	staticStun.makePermanent();
		return staticStun;
	}
}

// Implementation of Blind
class Blind extends CrowdControl {
	// Constructs a blind
	public Blind(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		
		StatusEffect dodgeReduction = new StatusEffect(StatVersion.DODGE, -90, StatusEffectType.BASIC);
		StatusEffect blockReduction = new StatusEffect(StatVersion.BLOCK, -70, StatusEffectType.BASIC);
		StatusEffect speedReduction = new StatusEffect(StatVersion.SPEED, -50, StatusEffectType.BASIC);
		this.addStatusEffect(dodgeReduction);
		this.addStatusEffect(blockReduction);
		this.addStatusEffect(speedReduction);
	}
	public Blind(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	public Blind() {
		super();
	}
	
	@Override
	public String toString() {
		return "Blind: " + super.toString();
	}
}

// Implementation of Invincible
class Invincible extends CrowdControl {
	// Constructs an "invincible" effect
	public Invincible(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public Invincible(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Invincible: " + super.toString();
	}
}

// Implementation of Invulnerable
class Invulnerable extends Condition {
	// Constructs an "invulnerable" effect
	public Invulnerable(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public Invulnerable(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Invulnerable: " + super.toString();
	}
}

// Implementation of Slow
class Slow extends Condition {
	// Added Variable
	private double value;
	
	// Constructs a slow
	public Slow(String name, int duration, double val, Requirement actReq) {
		super(name, duration, actReq);
		this.value = -val;
		
		StatusEffect speedReductionEffect = new StatusEffect(StatVersion.SPEED, this.getValue(), StatusEffectType.BASIC);
		speedReductionEffect.makeFlat();
		this.addStatusEffect(speedReductionEffect);
	}
	public Slow(String name, int duration, double val) {
		this(name, duration, val, (Character withEffect) -> {return true;});
	}
	public Slow() {
		super();
		this.value = 0;
	}
	
	// Returns the value of the slow.
	public double getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return "Slow: " + super.toString();
	}
}

// Implementation of Snare
class Snare extends Condition {
	// Constructs a snare
	public Snare(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		
		StatusEffect dodgeReduction = new StatusEffect(StatVersion.DODGE, -90, StatusEffectType.BASIC);
		this.addStatusEffect(dodgeReduction);
	}
	public Snare(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	
	@Override
	public String toString() {
		return "Snare: " + super.toString();
	}
}

// Implementation of Stasis
class Stasis extends Condition {
	// Constructs a stasis
	public Stasis(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
	}
	public Stasis(String name, int duration) {
		super(name, duration);
	}
	
	@Override
	public String toString() {
		return "Stasis: " + super.toString();
	}
}

// Implementation of Stun
class Stun extends Condition {
	// Constructs a stun
	public Stun(String name, int duration, Requirement actReq) {
		super(name, duration, actReq);
		
		StatusEffect dodgeReduction = new StatusEffect(StatVersion.DODGE, -100, StatusEffectType.BASIC);
		StatusEffect blockReduction = new StatusEffect(StatVersion.BLOCK, -75, StatusEffectType.BASIC);
		this.addStatusEffect(dodgeReduction);
		this.addStatusEffect(blockReduction);
	}
	public Stun(String name, int duration) {
		this(name, duration, (Character withEffect) -> {return true;});
	}
	
	@Override
	public String toString() {
		return "Stun: " + super.toString();
	}
}