package quests._10275_ContainingTheAttributePower;

import ru.l2gw.gameserver.model.Inventory;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;
import ru.l2gw.gameserver.tables.SkillTable;

public class _10275_ContainingTheAttributePower extends Quest
{
	private final static int Holly = 30839;
	private final static int Weber = 31307;
	private final static int Yin = 32325;
	private final static int Yang = 32326;
	private final static int Water = 27380;
	private final static int Air = 27381;

	private final static int YinSword = 13845;
	private final static int YangSword = 13881;
	private final static int SoulPieceWater = 13861;
	private final static int SoulPieceAir = 13862;

	public _10275_ContainingTheAttributePower()
	{
		super(10275, "_10275_ContainingTheAttributePower", "Containing The Attribute Power"); // Party true?

		addStartNpc(Holly);
		addStartNpc(Weber);

		addTalkId(Yin);
		addTalkId(Yang);

		addKillId(Air);
		addKillId(Water);

		addQuestItem(YinSword, YangSword, SoulPieceWater, SoulPieceAir);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		if(st.isCompleted())
			return "completed";

		String htmltext = event;
		L2Player player = st.getPlayer();
		if(event.equalsIgnoreCase("30839-02.htm") || event.equalsIgnoreCase("31307-02.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30839-05.htm"))
		{
			st.set("cond", "2");
			st.playSound(SOUND_MIDDLE);
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("31307-05.htm"))
		{
			st.set("cond", "7");
			st.playSound(SOUND_MIDDLE);
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("32325-03.htm"))
		{
			st.set("cond", "3");
			st.giveItems(YinSword, 1);
			st.playSound(SOUND_MIDDLE);
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("32326-03.htm"))
		{
			st.set("cond", "8");
			st.giveItems(YangSword, 1);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("32325-06.htm"))
		{
			if(st.getQuestItemsCount(YinSword) > 0)
			{
				st.takeItems(YinSword, 1);
				htmltext = "32325-07.htm";
			}
			st.giveItems(YinSword, 1);
		}
		else if(event.equalsIgnoreCase("32326-06.htm"))
		{
			if(st.getQuestItemsCount(YangSword) > 0)
			{
				st.takeItems(YangSword, 1);
				htmltext = "32326-07.htm";
			}
			st.giveItems(YangSword, 1);
		}
		else if(event.equalsIgnoreCase("32325-09.htm"))
		{
			st.set("cond", "5");
			if(player.getLastNpc() != null)
				player.getLastNpc().altUseSkill(SkillTable.getInstance().getInfo(2635, 1), player);
			st.giveItems(YinSword, 1);
			st.playSound(SOUND_MIDDLE);
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("32326-09.htm"))
		{
			st.set("cond", "10");
			if(player.getLastNpc() != null)
				player.getLastNpc().altUseSkill(SkillTable.getInstance().getInfo(2636, 1), player);
			st.giveItems(YangSword, 1);
			st.playSound(SOUND_MIDDLE);
			st.setState(STARTED);
		}
		else
		{
			int item = 0;

			if(event.equalsIgnoreCase("1"))
				item = 10521;
			else if(event.equalsIgnoreCase("2"))
				item = 10522;
			else if(event.equalsIgnoreCase("3"))
				item = 10523;
			else if(event.equalsIgnoreCase("4"))
				item = 10524;
			else if(event.equalsIgnoreCase("5"))
				item = 10525;
			else if(event.equalsIgnoreCase("6"))
				item = 10526;

			if(item > 0)
			{
				st.rollAndGive(item, 2, 100);
				st.addExpAndSp(202160, 20375);
				st.exitCurrentQuest(false);
				st.playSound(SOUND_FINISH);
				if(player.getLastNpc() != null)
					htmltext = str(player.getLastNpc().getNpcId()) + "-1" + event + ".htm";
				else
					htmltext = null;
			}
		}

		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int cond = st.getInt("cond");
		int npcId = npc.getNpcId();

		if(st.isCompleted())
		{
			if(npcId == Holly)
				htmltext = "30839-0a.htm";
			else if(npcId == Weber)
				htmltext = "31307-0a.htm";
		}
		else if(st.isCreated())
			if(st.getPlayer().getLevel() >= 76)
				if(npcId == Holly)
					htmltext = "30839-01.htm";
				else
					htmltext = "31307-01.htm";
			else if(npcId == Holly)
				htmltext = "30839-00.htm";
			else
				htmltext = "31307-00.htm";
		else if(npcId == Holly)
		{
			if(cond == 1)
				htmltext = "30839-03.htm";
			else if(cond == 2)
				htmltext = "30839-05.htm";
		}
		else if(npcId == Weber)
		{
			if(cond == 1)
				htmltext = "31307-03.htm";
			else if(cond == 7)
				htmltext = "31307-05.htm";
		}
		else if(npcId == Yin)
		{
			if(cond == 2)
				htmltext = "32325-01.htm";
			else if(cond == 3 || cond == 5)
				htmltext = "32325-04.htm";
			else if(cond == 4)
			{
				htmltext = "32325-08.htm";
				st.takeItems(YinSword, 1);
				st.takeItems(SoulPieceWater, -1);
			}
			else if(cond == 6)
				htmltext = "32325-10.htm";
		}
		else if(npcId == Yang)
			if(cond == 7)
				htmltext = "32326-01.htm";
			else if(cond == 8 || cond == 10)
				htmltext = "32326-04.htm";
			else if(cond == 9)
			{
				htmltext = "32326-08.htm";
				st.takeItems(YangSword, 1);
				st.takeItems(SoulPieceAir, -1);
			}
			else if(cond == 11)
				htmltext = "32326-10.htm";

		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		if(!st.isStarted())
			return;

		int cond = st.getInt("cond");
		int npcId = npc.getNpcId();

		if(npcId == Air)
		{
			if(st.getItemEquipped(Inventory.PAPERDOLL_RHAND) == YangSword && (cond == 8 || cond == 10) && st.getQuestItemsCount(SoulPieceAir) < 6 &&
					st.rollAndGiveLimited(SoulPieceAir, 1, 30, 6))
			{
				if(st.getQuestItemsCount(SoulPieceAir) >= 6)
				{
					st.set("cond", str(cond + 1));
					st.playSound(SOUND_MIDDLE);
					st.setState(STARTED);
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
		}
		else if(npcId == Water)
			if(st.getItemEquipped(Inventory.PAPERDOLL_RHAND) == YinSword && (cond == 3 || cond == 5) && st.getQuestItemsCount(SoulPieceWater) < 6 &&
					st.rollAndGiveLimited(SoulPieceWater, 1, 30, 6))
			{
				if(st.getQuestItemsCount(SoulPieceWater) >= 6)
				{
					st.set("cond", str(cond + 1));
					st.playSound(SOUND_MIDDLE);
					st.setState(STARTED);
				}
				else
				{
					st.playSound(SOUND_ITEMGET);
				}
			}
	}
}