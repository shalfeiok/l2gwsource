package quests._077_SagaOfTheDominator;

import quests.SagasSuperclass.SagasSuperclass;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;

public class _077_SagaOfTheDominator extends SagasSuperclass
{
	public _077_SagaOfTheDominator()
	{
		super(77, "_077_SagaOfTheDominator", "Saga Of The Dominator");

		NPC = new int[]{31336, 31624, 31371, 31290, 31636, 31646, 31648, 31653, 31654, 31655, 31656, 31290};
		Items = new int[]{7080, 7539, 7081, 7492, 7275, 7306, 7337, 7368, 7399, 7430, 7100, 0};
		Mob = new int[]{27294, 27226, 27262};
		classid = 115;
		prevclass = 0x33;
		X = new int[]{164650, 47429, 47391};
		Y = new int[]{-74121, -56923, -56929};
		Z = new int[]{-2871, -2383, -2370};
		Text = new String[]{
				"PLAYERNAME! Pursued to here! However, I jumped out of the Banshouren boundaries! You look at the giant as the sign of power!",
				"... Oh ... good! So it was ... let's begin!",
				"I do not have the patience ..! I have been a giant force ...! Cough chatter ah ah ah!",
				"Paying homage to those who disrupt the orderly will be PLAYERNAME's death!",
				"Now, my soul freed from the shackles of the millennium, Halixia, to the back side I come ...",
				"Why do you interfere others' battles?",
				"This is a waste of time.. Say goodbye...!",
				"...That is the enemy",
				"...Goodness! PLAYERNAME you are still looking?",
				"PLAYERNAME ... Not just to whom the victory. Only personnel involved in the fighting are eligible to share in the victory.",
				"Your sword is not an ornament. Don't you think, PLAYERNAME?",
				"Goodness! I no longer sense a battle there now.",
				"let...",
				"Only engaged in the battle to bar their choice. Perhaps you should regret.",
				"The human nation was foolish to try and fight a giant's strength.",
				"Must...Retreat... Too...Strong.",
				"PLAYERNAME. Defeat...by...retaining...and...Mo...Hacker",
				"....! Fight...Defeat...It...Fight...Defeat...It..."};

		registerNPCs();
	}

	@Override
	public void onKill(L2NpcInstance npc, L2Player killer)
	{
		for(int id : Mob)
			if(id == npc.getNpcId())
				super.onKill(npc, killer);
	}

}