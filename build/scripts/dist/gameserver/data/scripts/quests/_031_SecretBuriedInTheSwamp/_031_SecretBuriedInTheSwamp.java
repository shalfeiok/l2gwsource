package quests._031_SecretBuriedInTheSwamp;

import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _031_SecretBuriedInTheSwamp extends Quest
{
	int ABERCROMBIE = 31555;
	int FORGOTTEN_MONUMENT_1 = 31661;
	int FORGOTTEN_MONUMENT_2 = 31662;
	int FORGOTTEN_MONUMENT_3 = 31663;
	int FORGOTTEN_MONUMENT_4 = 31664;
	int CORPSE_OF_DWARF = 31665;

	int KRORINS_JOURNAL = 7252;

	public _031_SecretBuriedInTheSwamp()
	{
		super(31, "_031_SecretBuriedInTheSwamp", "Secret Buried In The Swamp");

		addStartNpc(ABERCROMBIE);

		for(int i = 31661; i <= 31665; i++)
			addTalkId(i);

		addQuestItem(KRORINS_JOURNAL);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		int cond = st.getInt("cond");
		String htmltext = event;
		if(event.equals("31555-1.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equals("31665-1.htm") && cond == 1)
		{
			st.set("cond", "2");
			st.setState(STARTED);
			st.playSound(SOUND_ITEMGET);
			st.giveItems(KRORINS_JOURNAL, 1);
		}
		else if(event.equals("31555-4.htm") && cond == 2)
		{
			st.set("cond", "3");
			st.setState(STARTED);
		}
		else if(event.equals("31661-1.htm") && cond == 3)
		{
			st.set("cond", "4");
			st.setState(STARTED);
		}
		else if(event.equals("31662-1.htm") && cond == 4)
		{
			st.set("cond", "5");
			st.setState(STARTED);
		}
		else if(event.equals("31663-1.htm") && cond == 5)
		{
			st.set("cond", "6");
			st.setState(STARTED);
		}
		else if(event.equals("31664-1.htm") && cond == 6)
		{
			st.set("cond", "7");
			st.playSound(SOUND_MIDDLE);
			st.setState(STARTED);
		}
		else if(event.equals("31555-7.htm") && cond == 7)
		{
			st.takeItems(KRORINS_JOURNAL, -1);
			st.addExpAndSp(490000, 45880);
			st.rollAndGive(57, 120000, 100);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(false);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(npcId == ABERCROMBIE)
		{
			if(st.isCreated())
			{
				if(st.getPlayer().getLevel() >= 66)
					htmltext = "31555-0.htm";
				else
				{
					htmltext = "31555-0a.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "31555-2.htm";
			else if(cond == 2)
				htmltext = "31555-3.htm";
			else if(cond == 3)
				htmltext = "31555-5.htm";
			else if(cond == 7)
				htmltext = "31555-6.htm";
		}
		else if(npcId == CORPSE_OF_DWARF)
		{
			if(cond == 1)
				htmltext = "31665-0.htm";
			else if(cond == 2)
				htmltext = "31665-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_1)
		{
			if(cond == 3)
				htmltext = "31661-0.htm";
			else if(cond > 3)
				htmltext = "31661-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_2)
		{
			if(cond == 4)
				htmltext = "31662-0.htm";
			else if(cond > 4)
				htmltext = "31662-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_3)
		{
			if(cond == 5)
				htmltext = "31663-0.htm";
			else if(cond > 5)
				htmltext = "31663-2.htm";
		}
		else if(npcId == FORGOTTEN_MONUMENT_4)
			if(cond == 6)
				htmltext = "31664-0.htm";
			else if(cond > 6)
				htmltext = "31664-2.htm";
		return htmltext;
	}
}