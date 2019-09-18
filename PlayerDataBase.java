package WyattWitemeyer.WarOfGillysburg;
public class PlayerDataBase {
	// Stats are listed in the form: (Base + bonus (from Passive Abilities or items))
	public static final SteelLegionTank TEST = new SteelLegionTankBuilder()
			.Name("Test")
			.Level(10)
			.bonusHealth(50)
			.bonusDamage(10)
			.setEnchantedArmorRank(15)
			.setShieldSkillsRank(15)
			.build();
	public static final SilentDeathShadow JAIME = new CharacterBuilder()
			.Name("Jaime")
			.Level(1)
			.Health(414)
			.Damage(166+15)
			.Armor(1+1)
			.ArmorPiercing(16+5)
			.Accuracy(105)
			.Dodge(24)
			.CriticalChance(10)
			.Speed(7)
			.AttackSpeed(5)
			.Threat(2)
			.TacticalThreat(25)
			.buildSDS();
	
	public static final KinitchuOrderThaumraturge TREVOR = new CharacterBuilder()
			.Name("Trevor")
			.Level(1)
			.Health(563)
			.Damage(117+20)
			.Armor(10+3)
			.ArmorPiercing(7)
			.Accuracy(115+5+15)
			.Dodge(10+2)
			.CriticalChance(6+5)
			.Speed(4)
			.AttackSpeed(4)
			.Threat(17)
			.TacticalThreat(7)
			.buildKOT();
	
	public static final SteelLegionWarrior JD = new CharacterBuilder()
			.Name("J.D.")
			.Level(1)
			.Health(701)
			.Damage(95+15)
			.Armor(13+5)
			.ArmorPiercing(4)
			.Accuracy(110)
			.Block(23)
			.CriticalChance(5)
			.Speed(5)
			.AttackSpeed(5)
			.Threat(15)
			.TacticalThreat(5)
			.buildSLW();
	
	public static final SteelLegionWarrior IAN = new CharacterBuilder()
			.Name("Ian")
			.Level(1)
			.Health(694)
			.Damage(108)
			.Armor(182)
			.ArmorPiercing(118)
			.Accuracy(125)
			.Block(30)
			.CriticalChance(5)
			.Speed(6)
			.AttackSpeed(5)
			.Threat(15)
			.TacticalThreat(5)
			.buildSLW();
	
	public static final SentinelArcArcher TYLER = new CharacterBuilder()
			.Name("Tyler")
			.Level(1)
			.Health(383)
			.Damage(103)
			.Armor(135)
			.ArmorPiercing(134)
			.Accuracy(160)
			.Dodge(43)
			.CriticalChance(7)
			.Speed(6)
			.AttackSpeed(35)
			.Threat(6)
			.TacticalThreat(19)
			.buildSAA();
	
	public static final KinitchuOrderDragonFireWizard PETER = new CharacterBuilder()
			.Name("Peter")
			.Level(1)
			.Health(430)
			.Damage(181+20)
			.Armor(3+2)
			.ArmorPiercing(14)
			.Accuracy(115+5)
			.Dodge(10)
			.CriticalChance(8)
			.Speed(4)
			.AttackSpeed(4)
			.Threat(4)
			.TacticalThreat(23)
			.buildKODFW();
	
	public static final SilentDeathPoisonSpecialist WYATTWIL = new CharacterBuilder()
			.Name("Wyatt Wilson")
			.Level(1)
			.Health(525)
			.Damage(133)
			.Armor(155)
			.ArmorPiercing(128)
			.Accuracy(115)
			.Dodge(24)
			.CriticalChance(5)
			.Speed(4)
			.AttackSpeed(4)
			.Threat(6)
			.TacticalThreat(15)
			.buildSDPS();
}
