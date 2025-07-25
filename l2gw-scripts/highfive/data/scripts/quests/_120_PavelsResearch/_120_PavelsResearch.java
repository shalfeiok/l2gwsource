package quests._120_PavelsResearch;

import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;
import ru.l2gw.gameserver.serverpackets.MagicSkillUse;

public class _120_PavelsResearch extends Quest
{
	//NPC
	private static final int Yumi = 32041;
	private static final int Weather1 = 32042;
	private static final int Weather2 = 32043;
	private static final int Weather3 = 32044;
	private static final int BookShelf = 32045;
	private static final int Stones = 32046;
	private static final int Wendy = 32047;
	//ITEMS
	private static final int EarPhoenix = 6324;
	private static final int Report = 8058;
	private static final int Report2 = 8059;
	private static final int Enigma = 8060;
	private static final int Flower = 8290;
	private static final int Heart = 8291;
	private static final int Necklace = 8292;

	public _120_PavelsResearch()
	{
		super(120, "_120_PavelsResearch", "Pavel's Research");

		addStartNpc(Stones);

		addQuestItem(Report);
		addQuestItem(Report2);
		addQuestItem(Enigma);
		addQuestItem(Flower);
		addQuestItem(Heart);
		addQuestItem(Necklace);

		addTalkId(BookShelf);
		addTalkId(Stones);
		addTalkId(Weather1);
		addTalkId(Weather2);
		addTalkId(Weather3);
		addTalkId(Wendy);
		addTalkId(Yumi);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		String htmltext = event;
		if(event.equalsIgnoreCase("32041-03.htm"))
		{
			st.set("cond", "3");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32041-04.htm"))
		{
			st.set("cond", "4");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32041-12.htm"))
		{
			st.set("cond", "8");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32041-16.htm"))
		{
			st.set("cond", "16");
			st.giveItems(Enigma, 1);
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32041-22.htm"))
		{
			st.set("cond", "17");
			st.takeItems(Enigma, 1);
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32041-32.htm"))
		{

			st.takeItems(Necklace, 1);
			st.giveItems(EarPhoenix, 1);
			st.rollAndGive(57, 783729, 100);
			st.addExpAndSp(3447315, 272615);
			st.exitCurrentQuest(false);
			st.playSound(SOUND_FINISH);
		}
		else if(event.equalsIgnoreCase("32042-06.htm"))
		{
			if(st.getInt("cond") == 10)
				if(st.getInt("talk") + st.getInt("talk1") == 2)
				{
					st.set("cond", "11");
					st.set("talk", "0");
					st.set("talk1", "0");
					st.playSound("ItemSound.quest_middle");
				}
				else
					htmltext = "32042-03.htm";
		}
		else if(event.equalsIgnoreCase("32042-10.htm"))
		{
			if(st.getInt("talk") + st.getInt("talk1") + st.getInt("talk2") == 3)
				htmltext = "32042-14.htm";
		}
		else if(event.equalsIgnoreCase("32042-11.htm"))
		{
			if(st.getInt("talk") == 0)
				st.set("talk", "1");
		}
		else if(event.equalsIgnoreCase("32042-12.htm"))
		{
			if(st.getInt("talk1") == 0)
				st.set("talk1", "1");
		}
		else if(event.equalsIgnoreCase("32042-13.htm"))
		{
			if(st.getInt("talk2") == 0)
				st.set("talk2", "1");
		}
		else if(event.equalsIgnoreCase("32042-15.htm"))
		{
			st.set("cond", "12");
			st.set("talk", "0");
			st.set("talk1", "0");
			st.set("talk2", "0");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32043-06.htm"))
		{
			if(st.getInt("cond") == 17)
				if(st.getInt("talk") + st.getInt("talk1") == 2)
				{
					st.set("cond", "18");
					st.set("talk", "0");
					st.set("talk1", "0");
					st.playSound("ItemSound.quest_middle");
				}
				else
					htmltext = "32043-03.htm";
		}
		else if(event.equalsIgnoreCase("32043-15.htm"))
		{
			if(st.getInt("talk") + st.getInt("talk1") == 2)
				htmltext = "32043-29.htm";
		}
		else if(event.equalsIgnoreCase("32043-18.htm"))
		{
			if(st.getInt("talk") == 1)
				htmltext = "32043-21.htm";
		}
		else if(event.equalsIgnoreCase("32043-20.htm"))
		{
			st.set("talk", "1");
			st.playSound("AmbSound.ed_drone_02");
		}
		else if(event.equalsIgnoreCase("32043-28.htm"))
			st.set("talk1", "1");
		else if(event.equalsIgnoreCase("32043-30.htm"))
		{
			st.set("cond", "19");
			st.set("talk", "0");
			st.set("talk1", "0");
		}
		else if(event.equalsIgnoreCase("32044-06.htm"))
		{
			if(st.getInt("cond") == 20)
				if(st.getInt("talk") + st.getInt("talk1") == 2)
				{
					st.set("cond", "21");
					st.set("talk", "0");
					st.set("talk1", "0");
					st.playSound("ItemSound.quest_middle");
				}
				else
					htmltext = "32044-03.htm";
		}
		else if(event.equalsIgnoreCase("32044-08.htm"))
		{
			if(st.getInt("talk") + st.getInt("talk1") == 2)
				htmltext = "32044-11.htm";
		}
		else if(event.equalsIgnoreCase("32044-09.htm"))
		{
			if(st.getInt("talk") == 0)
				st.set("talk", "1");
		}
		else if(event.equalsIgnoreCase("32044-10.htm"))
		{
			if(st.getInt("talk1") == 0)
				st.set("talk1", "1");
		}
		else if(event.equalsIgnoreCase("32044-17.htm"))
		{
			st.set("cond", "22");
			st.set("talk", "0");
			st.set("talk1", "0");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32045-02.htm"))
		{
			st.set("cond", "15");
			st.playSound("ItemSound.quest_middle");
			st.giveItems(Report, 1);
			L2Player player = st.getPlayer();
			if(player != null)
				player.getLastNpc().broadcastPacket(new MagicSkillUse(player.getLastNpc(), player, 5073, 5, 1500, 0));
		}
		else if(event.equalsIgnoreCase("32046-04.htm") || event.equalsIgnoreCase("32046-05.htm"))
			st.exitCurrentQuest(true);
		else if(event.equalsIgnoreCase("32046-06.htm"))
		{
			if(st.getPlayer().getLevel() >= 50)
			{
				st.playSound("ItemSound.quest_accept");
				st.set("cond", "1");
				st.setState(STARTED);
			}
			else
			{
				htmltext = "32046-00.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(event.equalsIgnoreCase("32046-08.htm"))
		{
			st.set("cond", "2");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32046-12.htm"))
		{
			st.set("cond", "6");
			st.playSound("ItemSound.quest_middle");
			st.giveItems(Flower, 1);
		}
		else if(event.equalsIgnoreCase("32046-22.htm"))
		{
			st.set("cond", "10");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32046-29.htm"))
		{
			st.set("cond", "13");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32046-35.htm"))
		{
			st.set("cond", "20");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32046-38.htm"))
		{
			st.set("cond", "23");
			st.playSound("ItemSound.quest_middle");
			st.giveItems(Heart, 1);
		}
		else if(event.equalsIgnoreCase("32047-06.htm"))
		{
			st.set("cond", "5");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32047-10.htm"))
		{
			st.set("cond", "7");
			st.playSound("ItemSound.quest_middle");
			st.takeItems(Flower, 1);
		}
		else if(event.equalsIgnoreCase("32047-15.htm"))
		{
			st.set("cond", "9");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32047-18.htm"))
		{
			st.set("cond", "14");
			st.playSound("ItemSound.quest_middle");
		}
		else if(event.equalsIgnoreCase("32047-26.htm"))
		{
			st.set("cond", "24");
			st.playSound("ItemSound.quest_middle");
			st.takeItems(Heart, 1);
		}
		else if(event.equalsIgnoreCase("32047-32.htm"))
		{
			st.set("cond", "25");
			st.playSound("ItemSound.quest_middle");
			st.giveItems(Necklace, 1);
		}
		else if(event.equalsIgnoreCase("w1_1"))
		{
			st.set("talk", "1");
			htmltext = "32042-04.htm";
		}
		else if(event.equalsIgnoreCase("w1_2"))
		{
			st.set("talk1", "1");
			htmltext = "32042-05.htm";
		}
		else if(event.equalsIgnoreCase("w2_1"))
		{
			st.set("talk", "1");
			htmltext = "32043-04.htm";
		}
		else if(event.equalsIgnoreCase("w2_2"))
		{
			st.set("talk1", "1");
			htmltext = "32043-05.htm";
		}
		else if(event.equalsIgnoreCase("w3_1"))
		{
			st.set("talk", "1");
			htmltext = "32044-04.htm";
		}
		else if(event.equalsIgnoreCase("w3_2"))
		{
			st.set("talk1", "1");
			htmltext = "32044-05.htm";
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getInt("cond");
		if(npcId == Stones)
		{
			QuestState q = st.getPlayer().getQuestState("_114_ResurrectionOfAnOldManager");
			if(q == null)
				return htmltext;
			else if(st.isCreated())
			{
				if(st.getPlayer().getLevel() >= 70 && q.isCompleted())
					htmltext = "32046-01.htm";
				else
				{
					htmltext = "32046-00.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "32046-06.htm";
			else if(cond == 2)
				htmltext = "32046-09.htm";
			else if(cond == 5)
				htmltext = "32046-10.htm";
			else if(cond == 6)
				htmltext = "32046-13.htm";
			else if(cond == 9)
				htmltext = "32046-14.htm";
			else if(cond == 10)
				htmltext = "32046-23.htm";
			else if(cond == 12)
				htmltext = "32046-26.htm";
			else if(cond == 13)
				htmltext = "32046-30.htm";
			else if(cond == 19)
				htmltext = "32046-31.htm";
			else if(cond == 20)
				htmltext = "32046-36.htm";
			else if(cond == 22)
				htmltext = "32046-37.htm";
			else if(cond == 23)
				htmltext = "32046-39.htm";
		}
		else if(npcId == Wendy)
		{
			if(cond == 2 || cond == 3 || cond == 4)
				htmltext = "32047-01.htm";
			else if(cond == 5)
				htmltext = "32047-07.htm";
			else if(cond == 6)
				htmltext = "32047-08.htm";
			else if(cond == 7)
				htmltext = "32047-11.htm";
			else if(cond == 8)
				htmltext = "32047-12.htm";
			else if(cond == 9)
				htmltext = "32047-15.htm";
			else if(cond == 13)
				htmltext = "32047-16.htm";
			else if(cond == 14)
				htmltext = "32047-19.htm";
			else if(cond == 15)
				htmltext = "32047-20.htm";
			else if(cond == 23)
				htmltext = "32047-21.htm";
			else if(cond == 24)
				htmltext = "32047-26.htm";
			else if(cond == 25)
				htmltext = "32047-33.htm";
		}
		else if(npcId == Yumi)
		{
			if(cond == 2)
				htmltext = "32041-01.htm";
			else if(cond == 3)
				htmltext = "32041-05.htm";
			else if(cond == 4)
				htmltext = "32041-06.htm";
			else if(cond == 7)
				htmltext = "32041-07.htm";
			else if(cond == 8)
				htmltext = "32041-13.htm";
			else if(cond == 15)
				htmltext = "32041-14.htm";
			else if(cond == 16)
			{
				if(st.getQuestItemsCount(Report2) == 0)
					htmltext = "32041-17.htm";
				else
					htmltext = "32041-18.htm";
			}
			else if(cond == 17)
				htmltext = "32041-22.htm";
			else if(cond == 25)
				htmltext = "32041-26.htm";
		}
		else if(npcId == Weather1)
		{
			if(cond == 10)
				htmltext = "32042-01.htm";
			else if(cond == 11)
			{
				if(st.getInt("talk") + st.getInt("talk1") + st.getInt("talk2") == 3)
					htmltext = "32042-14.htm";
				else
					htmltext = "32042-06.htm";
			}
			else if(cond == 12)
				htmltext = "32042-15.htm";
		}
		else if(npcId == Weather2)
		{
			if(cond == 17)
				htmltext = "32043-01.htm";
			else if(cond == 18)
			{
				if(st.getInt("talk") + st.getInt("talk1") == 2)
					htmltext = "32043-29.htm";
				else
					htmltext = "32043-06.htm";
			}
			else if(cond == 19)
				htmltext = "32043-30.htm";
		}
		else if(npcId == Weather3)
		{
			if(cond == 20)
				htmltext = "32044-01.htm";
			else if(cond == 21)
				htmltext = "32044-06.htm";
			else if(cond == 22)
				htmltext = "32044-18.htm";
		}
		else if(npcId == BookShelf)
			if(cond == 14)
				htmltext = "32045-01.htm";
			else if(cond == 15)
				htmltext = "32045-03.htm";
		return htmltext;
	}
}