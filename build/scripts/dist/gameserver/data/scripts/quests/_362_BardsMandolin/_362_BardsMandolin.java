package quests._362_BardsMandolin;

import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _362_BardsMandolin extends Quest
{
	//NPC
	private static int SWAN = 30957;
	private static int NANARIN = 30956;
	private static int GALION = 30958;
	private static int WOODROW = 30837;
	//Items
	private static int SWANS_FLUTE = 4316;
	private static int SWANS_LETTER = 4317;
	private static int ADENA = 57;
	private static int Musical_Score__Theme_of_Journey = 4410;

	public _362_BardsMandolin()
	{
		super(362, "_362_BardsMandolin", "Bards Mandolin");
		addStartNpc(SWAN);
		addTalkId(NANARIN);
		addTalkId(GALION);
		addTalkId(WOODROW);
		addQuestItem(SWANS_FLUTE);
		addQuestItem(SWANS_LETTER);
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		if(st.isCreated())
		{
			if(npcId != SWAN)
				return htmltext;
			st.set("cond", "0");
		}

		int cond = st.getInt("cond");
		if(npcId == SWAN)
		{
			if(st.isCreated())
				htmltext = "30957_1.htm";
			else if(cond == 3 && st.getQuestItemsCount(SWANS_FLUTE) > 0 && st.getQuestItemsCount(SWANS_LETTER) == 0)
			{
				htmltext = "30957_3.htm";
				st.set("cond", "4");
				st.giveItems(SWANS_LETTER, 1);
			}
			else if(cond == 4 && st.getQuestItemsCount(SWANS_FLUTE) > 0 && st.getQuestItemsCount(SWANS_LETTER) > 0)
				htmltext = "30957_6.htm";
			else if(cond == 5)
				htmltext = "30957_4.htm";
		}
		else if(npcId == WOODROW && cond == 1)
		{
			htmltext = "30837_1.htm";
			st.set("cond", "2");
		}
		else if(npcId == GALION && cond == 2)
		{
			htmltext = "30958_1.htm";
			st.set("cond", "3");
			st.giveItems(SWANS_FLUTE, 1);
			st.playSound(SOUND_ITEMGET);
		}
		else if(npcId == NANARIN && cond == 4 && st.getQuestItemsCount(SWANS_FLUTE) > 0 && st.getQuestItemsCount(SWANS_LETTER) > 0)
		{
			htmltext = "30956_1.htm";
			st.takeItems(SWANS_FLUTE, 1);
			st.takeItems(SWANS_LETTER, 1);
			st.set("cond", "5");
		}
		return htmltext;
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		int cond = st.getInt("cond");
		if(event.equalsIgnoreCase("30957_2.htm") && st.isCreated())
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30957_5.htm") && st.isStarted() && cond == 5)
		{
			st.rollAndGive(ADENA, 10000, 100);
			st.rollAndGive(Musical_Score__Theme_of_Journey, 1, 100);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}
}
