package quests._093_SagaOfTheSpectralMaster;

import quests.SagasSuperclass.SagasSuperclass;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;

public class _093_SagaOfTheSpectralMaster extends SagasSuperclass
{
	public _093_SagaOfTheSpectralMaster()
	{
		super(93, "_093_SagaOfTheSpectralMaster", "Saga Of The Spectral Master");

		NPC = new int[] { 30175, 31287, 31613, 30175, 31632, 31646, 31649, 31653, 31654, 31655, 31656, 31613 };
		Items = new int[] { 7080, 7606, 7081, 7508, 7291, 7322, 7353, 7384, 7415, 7446, 7112, 0 };
		Mob = new int[] { 27315, 27242, 27312 };
		classid = 111;
		prevclass = 0x29;
		X = new int[] { 164650, 47429, 47391 };
		Y = new int[] { -74121, -56923, -56929 };
		Z = new int[] { -2871, -2383, -2370 };
		Text = new String[] {
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
				"....! Fight...Defeat...It...Fight...Defeat...It..." };

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