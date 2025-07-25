package quests._637_ThroughOnceMore;

import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _637_ThroughOnceMore extends Quest
{
	//Drop rate
	public final int CHANCE = 40;

	//Npc
	public final int FLAURON = 32010;

	//Items
	public final int VISITOR_MARK = 8064;
	public final int FADED_MARK = 8065;
	public final int NECROHEART = 8066;
	public final int PERMANENT_MARK = 8067;
	public final int ANTEROOM_KEY = 8273;

	public _637_ThroughOnceMore()
	{
		super(637, "_637_ThroughOnceMore", "Through the Gate Once More");

		addStartNpc(FLAURON);
		addKillId(21565, 21566, 21567, 21568);
		addQuestItem(NECROHEART);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		if(event.equals("falsepriest_flauron_q0637_06.htm") || event.equals("falsepriest_flauron_q0637_07.htm"))
		{
			if(st.getQuestItemsCount(PERMANENT_MARK) > 0)
				htmltext = "falsepriest_flauron_q0637_05.htm";
		}
		else if(event.equals("falsepriest_flauron_q0637_11.htm"))
		{
			st.setCond(1);
			st.takeItems(FADED_MARK, 1);
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equals("falsepriest_flauron_q0637_09.htm"))
			st.exitCurrentQuest(true);
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext;
		int cond = st.getCond();
		L2Player player = st.getPlayer();
		if(st.isCreated())
		{
			if(player.getLevel() >= 73 && (st.getQuestItemsCount(FADED_MARK) >= 1 || st.getQuestItemsCount(PERMANENT_MARK) >= 1))
			{
				htmltext = "falsepriest_flauron_q0637_01.htm";
			}
			else if(player.getLevel() < 73)
			{
				htmltext = "falsepriest_flauron_q0637_03.htm";
				st.exitCurrentQuest(true);
			}
			else
			{
				htmltext = "falsepriest_flauron_q0637_02.htm";
				st.exitCurrentQuest(true);
			}
		}
		else if(cond == 2 && st.getQuestItemsCount(NECROHEART) >= 10)
		{
			htmltext = "falsepriest_flauron_q0637_13.htm";
			st.takeItems(NECROHEART, -1);
			st.takeItems(FADED_MARK, -1);
			st.takeItems(VISITOR_MARK, -1);
			st.giveItems(PERMANENT_MARK, 1);
			st.giveItems(ANTEROOM_KEY, 10);
			st.playSound(SOUND_FINISH);
			st.exitCurrentQuest(true);
		}
		else
			htmltext = "falsepriest_flauron_q0637_12.htm";
		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		if(st.getInt("cond") == 1 && st.rollAndGiveLimited(NECROHEART, 1, CHANCE, 10))
		{
			if(st.getQuestItemsCount(NECROHEART) == 10)
			{
				st.setCond(2);
				st.setState(STARTED);
				st.playSound(SOUND_MIDDLE);
			}
			else
				st.playSound(SOUND_ITEMGET);
		}
	}
}