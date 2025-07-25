package quests._152_ShardsOfGolem;

import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _152_ShardsOfGolem extends Quest
{
	int HARRYS_RECEIPT1 = 1008;
	int HARRYS_RECEIPT2 = 1009;
	int GOLEM_SHARD = 1010;
	int TOOL_BOX = 1011;
	int WOODEN_BP = 23;

	public _152_ShardsOfGolem()
	{
		super(152, "_152_ShardsOfGolem", "152 Shards Of Golem");

		addStartNpc(30035);

		addTalkId(30035);
		addTalkId(30035);
		addTalkId(30283);
		addTalkId(30035);

		addKillId(20016);
		addKillId(20101);

		addQuestItem(HARRYS_RECEIPT1, GOLEM_SHARD, TOOL_BOX, HARRYS_RECEIPT2);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		String htmltext = event;
		if(event.equals("30035-04.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			if(st.getQuestItemsCount(HARRYS_RECEIPT1) == 0)
				st.giveItems(HARRYS_RECEIPT1, 1);
		}
		else if(event.equals("152_2"))
		{
			st.takeItems(HARRYS_RECEIPT1, -1);
			if(st.getQuestItemsCount(HARRYS_RECEIPT2) == 0)
			{
				st.giveItems(HARRYS_RECEIPT2, 1);
				st.set("cond", "2");
			}
			htmltext = "30283-02.htm";
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
		if(npcId == 30035)
		{
			if(st.isCreated())
			{
				if(st.getPlayer().getLevel() >= 10)
				{
					htmltext = "30035-03.htm";
					return htmltext;
				}
				htmltext = "30035-02.htm";
				st.exitCurrentQuest(true);
			}
			else if(cond == 1 && st.getQuestItemsCount(HARRYS_RECEIPT1) != 0)
				htmltext = "30035-05.htm";
			else if(cond == 2 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0)
				htmltext = "30035-05.htm";
			else if(cond == 4 && st.getQuestItemsCount(TOOL_BOX) != 0)
			{
				st.takeItems(TOOL_BOX, -1);
				st.takeItems(HARRYS_RECEIPT2, -1);
				st.set("cond", "0");
				st.playSound(SOUND_FINISH);
				st.giveItems(WOODEN_BP, 1);
				st.addExpAndSp(5000, 250);
				htmltext = "30035-06.htm";
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30283)
		{
			if(cond == 1 && st.getQuestItemsCount(HARRYS_RECEIPT1) != 0)
				htmltext = "30283-01.htm";
			else if(cond == 2 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0 && st.getQuestItemsCount(GOLEM_SHARD) < 5)
				htmltext = "30283-03.htm";
			else if(cond == 3 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0 && st.getQuestItemsCount(GOLEM_SHARD) == 5)
			{
				st.takeItems(GOLEM_SHARD, -1);
				if(st.getQuestItemsCount(TOOL_BOX) == 0)
				{
					st.giveItems(TOOL_BOX, 1);
					st.set("cond", "4");
				}
				htmltext = "30283-04.htm";
			}
		}
		else if(cond == 4 && st.getQuestItemsCount(HARRYS_RECEIPT2) != 0 && st.getQuestItemsCount(TOOL_BOX) != 0)
			htmltext = "30283-05.htm";
		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		if(st.getInt("cond") == 2 && st.rollAndGiveLimited(GOLEM_SHARD, 1, 30, 5))
		{
			if(st.getQuestItemsCount(GOLEM_SHARD) == 5)
			{
				st.set("cond", "3");
				st.playSound(SOUND_MIDDLE);
				st.setState(STARTED);
			}
			else
				st.playSound(SOUND_ITEMGET);
		}
	}
}