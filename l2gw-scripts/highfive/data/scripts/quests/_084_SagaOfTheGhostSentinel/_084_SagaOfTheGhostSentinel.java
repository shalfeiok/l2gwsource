package quests._084_SagaOfTheGhostSentinel;

import quests.SagasSuperclass.SagasSuperclass;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;

public class _084_SagaOfTheGhostSentinel extends SagasSuperclass
{
	public _084_SagaOfTheGhostSentinel()
	{
		super(84, "_084_SagaOfTheGhostSentinel", "Saga Of The Ghost Sentinel");

		NPC = new int[]{30702, 31587, 31604, 31640, 31635, 31646, 31649, 31652, 31654, 31655, 31659, 31641};
		Items = new int[]{7080, 7521, 7081, 7499, 7282, 7313, 7344, 7375, 7406, 7437, 7107, 0};
		Mob = new int[]{27298, 27233, 27307};
		classid = 109;
		prevclass = 0x25;
		X = new int[]{161719, 124376, 124376};
		Y = new int[]{-92823, 82127, 82127};
		Z = new int[]{-1893, -2796, -2796};
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