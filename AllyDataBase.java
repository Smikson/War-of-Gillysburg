package WyattWitemeyer.WarOfGillysburg;
public class AllyDataBase {
	public static final Character INJURED_KING_WARMEN = new CharacterBuilder()
			.Name("Injured King Warmen")
			.Health(500)
			.Armor(15)
			.Block(65)
			.Threat(10)
			.TacticalThreat(5)
			.build();
	
	public static final Character LESMIK_SILOLAS = new CharacterBuilder()
			.Name("Lesmik Silolas")
			.Health(10000)
			.Damage(1000)
			.Armor(120)
			.Dodge(120)
			.Threat(4)
			.TacticalThreat(1)
			.build();
	
	public static final Character STELLOV_ALDECEPT = new CharacterBuilder()
			.Name("Stellov Aldecept")
			.Health(10000)
			.Damage(1000)
			.Armor(120)
			.Dodge(120)
			.Threat(7)
			.TacticalThreat(1)
			.build();
	
	public static final Character PRINCE_JAPHETH = new CharacterBuilder()
			.Name("Prince Japheth")
			.Health(10000)
			.Damage(1000)
			.Armor(120)
			.Dodge(120)
			.Threat(6)
			.TacticalThreat(1)
			.AttackSpeed(16)
			.build();
	
	public static final Character LEYA_MENDACE = new CharacterBuilder()
			.Name("Leya Mendace")
			.Health(10000)
			.Damage(1000)
			.Armor(120)
			.Dodge(120)
			.Threat(20)
			.TacticalThreat(1)
			.AttackSpeed(10)
			.build();
}
