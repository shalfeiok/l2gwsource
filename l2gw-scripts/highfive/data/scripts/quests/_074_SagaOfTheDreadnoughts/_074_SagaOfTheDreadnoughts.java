package quests._074_SagaOfTheDreadnoughts;

import quests.SagasSuperclass.SagasSuperclass;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;

public class _074_SagaOfTheDreadnoughts extends SagasSuperclass
{
	public _074_SagaOfTheDreadnoughts()
	{
		super(74, "_074_SagaOfTheDreadnoughts", "Saga of the Dreadnoughts");

		NPC = new int[]{30850, 31624, 31298, 31276, 31595, 31646, 31648, 31650, 31654, 31655, 31657, 31522};
		Items = new int[]{7080, 7538, 7081, 7489, 7272, 7303, 7334, 7365, 7396, 7427, 7097, 6480};
		Mob = new int[]{27290, 27223, 27282};
		classid = 89;
		prevclass = 0x03;
		X = new int[]{191046, 46087, 46066};
		Y = new int[]{-40640, -36372, -36396};
		Z = new int[]{-3042, -1685, -1685};
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