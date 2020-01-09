package WyattWitemeyer.WarOfGillysburg;

// Terrain are permanent Conditions (until manually removed), and have a set type: HighGround, Hill, Cover, or Tree
public class Terrain extends Condition {
	// List of static Terrains to be used for basic computations
	public static final HighGround HIGH_GROUND = new HighGround();
	public static final Hill HILL = new Hill();
	public static final Cover COVER = new Cover();
	public static final Tree TREE = new Tree();
	
	
	// Constructors - Terrain are permanent bonuses (only removed manually)
	public Terrain(String name) {
		super(name, -1);
		this.makePermanent();
	}
	public Terrain() {
		super();
	}
}
