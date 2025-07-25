package quests._363_SorrowfulSoundofFlute;

import ru.l2gw.commons.math.Rnd;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _363_SorrowfulSoundofFlute extends Quest
{
	//NPC
	public final int NANARIN = 30956;
	public final int BARBADO = 30959;
	public final int POITAN = 30458;
	public final int HOLVAS = 30058;
	//Mobs

	//Quest Item
	public final int EVENT_CLOTHES = 4318;
	public final int NANARINS_FLUTE = 4319;
	public final int SABRINS_BLACK_BEER = 4320;
	public final int Musical_Score = 4420;

	public _363_SorrowfulSoundofFlute()
	{
		super(363, "_363_SorrowfulSoundofFlute", "Sorrowful Sound of Flute");

		addStartNpc(NANARIN);
		addTalkId(NANARIN);
		addTalkId(POITAN);
		addTalkId(HOLVAS);
		addTalkId(BARBADO);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30956_2.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.takeItems(EVENT_CLOTHES, -1);
			st.takeItems(NANARINS_FLUTE, -1);
			st.takeItems(SABRINS_BLACK_BEER, -1);
		}
		else if(event.equalsIgnoreCase("30956_4.htm"))
		{
			st.giveItems(NANARINS_FLUTE, 1);
			st.playSound(SOUND_MIDDLE);
			st.set("cond", "3");
		}
		else if(event.equalsIgnoreCase("answer1"))
		{
			st.giveItems(EVENT_CLOTHES, 1);
			st.playSound(SOUND_MIDDLE);
			st.set("cond", "3");
			htmltext = "30956_6.htm";
		}
		else if(event.equalsIgnoreCase("answer2"))
		{
			st.giveItems(SABRINS_BLACK_BEER, 1);
			st.playSound(SOUND_MIDDLE);
			st.set("cond", "3");
			htmltext = "30956_6.htm";
		}
		else if(event.equalsIgnoreCase("30956_7.htm"))
		{
			st.rollAndGive(Musical_Score, 1, 100);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getInt("cond");
		if(npcId == NANARIN)
		{
			if(st.isCreated())
			{
				if(st.getPlayer().getLevel() < 15)
				{
					htmltext = "30956-00.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "30956_1.htm";
			}
			else if(cond == 1)
				htmltext = "30956_8.htm";
			else if(cond == 2)
				htmltext = "30956_3.htm";
			else if(cond == 3)
				htmltext = "30956_6.htm";
			else if(cond == 4)
				htmltext = "30956_5.htm";
		}
		else if(npcId == BARBADO)
		{
			if(cond == 3)
			{
				if(st.getQuestItemsCount(EVENT_CLOTHES) > 0)
				{
					st.takeItems(EVENT_CLOTHES, -1);
					htmltext = "30959_2.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getQuestItemsCount(SABRINS_BLACK_BEER) > 0)
				{
					st.takeItems(SABRINS_BLACK_BEER, -1);
					htmltext = "30959_2.htm";
					st.exitCurrentQuest(true);
				}
				else
				{
					st.takeItems(NANARINS_FLUTE, -1);
					st.set("cond", "4");
					st.playSound(SOUND_MIDDLE);
					htmltext = "30959_1.htm";
				}
			}
			else if(cond == 4)
				htmltext = "30959_3.htm";
		}
		else if(npcId == HOLVAS && (cond == 1 || cond == 2))
		{
			st.set("cond", "2");
			if(Rnd.chance(60))
				htmltext = "30058_2.htm";
			else
				htmltext = "30058_1.htm";
		}
		else if(npcId == POITAN && (cond == 1 || cond == 2))
		{
			st.set("cond", "2");
			if(Rnd.chance(60))
				htmltext = "30458_2.htm";
			else
				htmltext = "30458_1.htm";
		}
		return htmltext;
	}
}
