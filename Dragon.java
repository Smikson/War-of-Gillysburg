package WyattWitemeyer.WarOfGillysburg;
import java.util.*;

public class Dragon extends Character{
	// This first method helps set up the Dragon based on an original Kinitchu Order Dragon Fire Wizard.
	public Dragon(KinitchuOrderDragonFireWizard ori) {
		super(ori.getName() + "'s Dragon", ori.getLevel(), ori.getHealth() * 2, ori.getDamage() * 2, ori.getArmor() * 2, ori.getArmorPiercing() * 2, ori.getAccuracy() * 2, 0, ori.getDodge() * 2, ori.getCriticalChance() * 2, ori.getSpeed() * 2, ori.getAttackSpeed(), 1, ori.getThreat() * 2, ori.getTacticalThreat() * 2, ori.getSTDdown(), ori.getSTDup(), ori.getBaseDmgType(), ori.getResistances(), ori.getVulnerabilities(), ori.getType());
	}
	
	// Deals the Damage from a "cleave" basic attack.
	public void useCleave(List<Character> enemies) {
		for (Character enemy : enemies) {
			Attack cleaveAttack = new AttackBuilder()
					.attacker(this)
					.defender(enemy)
					.isTargeted()
					.scaler(1.0)
					.build();
			cleaveAttack.execute(); // Attack, Targeted, 1x Damage
		}
	}
	
	// Deals the Damage from "Dragon Breath"
	public void useDragonBreath(List<Character> enemies) {
		for (Character enemy : enemies) {
			Attack breathAtk = new AttackBuilder()
					.attacker(this)
					.defender(enemy)
					.isAOE()
					.scaler(1.0)
					.build();
			breathAtk.execute(); // Attack, AOE, 1x Damage
		}
	}
}
