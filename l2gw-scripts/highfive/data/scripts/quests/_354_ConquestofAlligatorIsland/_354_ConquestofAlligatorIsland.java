package quests._354_ConquestofAlligatorIsland;

import ru.l2gw.commons.math.Rnd;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _354_ConquestofAlligatorIsland extends Quest
{
	//npc
	public final int KLUCK = 30895;
	//mobs
	public final int CROKIAN_LAD = 20804;
	public final int DAILAON_LAD = 20805;
	public final int CROKIAN_LAD_WARRIOR = 20806;
	public final int FARHITE_LAD = 20807;
	public final int NOS_LAD = 20808;
	public final int SWAMP_TRIBE = 20991;
	//items
	public final int ADENA = 57;
	public final int ALLIGATOR_TOOTH = 5863;
	public final int TORN_MAP_FRAGMENT = 5864;
	public final int PIRATES_TREASURE_MAP = 5915;
	public final int CHANCE = 35;
	public final int CHANCE2 = 10;

	public final int[] MOBLIST = {CROKIAN_LAD, DAILAON_LAD, CROKIAN_LAD_WARRIOR, FARHITE_LAD, NOS_LAD, SWAMP_TRIBE};
	//RANDOM_REWARDS [ITEM_ID, QTY]
	public final int[][] RANDOM_REWARDS = {{736, 15}, //SoE
			{1061, 20}, //Healing Potion
			{734, 10}, //Haste Potion
			{735, 5}, //Alacrity Potion
			{1878, 25}, //Braided Hemp
			{1875, 10}, //Stone of Purity
			{1879, 10}, //Cokes
			{1880, 10}, //Steel
			{956, 1}, //Enchant Armor D
			{955, 1} //Enchant Weapon D
	};

	public _354_ConquestofAlligatorIsland()
	{
		super(354, "_354_ConquestofAlligatorIsland", "Conquest of Alligator Island");

		addStartNpc(30895);
		addTalkId(30895);
		addKillId(MOBLIST);
		addQuestItem(ALLIGATOR_TOOTH, TORN_MAP_FRAGMENT);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		long amount = st.getQuestItemsCount(ALLIGATOR_TOOTH);
		if(event.equalsIgnoreCase("30895-00a.htm"))
			st.exitCurrentQuest(true);
		else if(event.equalsIgnoreCase("1"))
		{
			st.setState(STARTED);
			st.set("cond", "1");
			htmltext = "30895-02.htm";
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30895-06.htm"))
		{
			if(st.getQuestItemsCount(TORN_MAP_FRAGMENT) > 9)
				htmltext = "30895-07.htm";
		}
		else if(event.equalsIgnoreCase("30895-05.htm"))
		{
			if(amount > 0)
				if(amount > 99)
				{
					st.rollAndGive(ADENA, amount * 300, 100);
					st.takeItems(ALLIGATOR_TOOTH, -1);
					st.playSound(SOUND_ITEMGET);
					int random = Rnd.get(RANDOM_REWARDS.length);
					st.rollAndGive(RANDOM_REWARDS[random][0], RANDOM_REWARDS[random][1], 100);
					htmltext = "30895-05b.htm";
				}
				else
				{
					st.giveItems(ADENA, amount * 100);
					st.takeItems(ALLIGATOR_TOOTH, -1);
					st.playSound(SOUND_ITEMGET);
					htmltext = "30895-05a.htm";
				}
		}
		else if(event.equalsIgnoreCase("30895-08.htm"))
		{
			st.giveItems(PIRATES_TREASURE_MAP, 1);
			st.takeItems(TORN_MAP_FRAGMENT, -1);
			st.playSound(SOUND_ITEMGET);
		}
		else if(event.equalsIgnoreCase("30895-09.htm"))
		{
			st.exitCurrentQuest(true);
			st.playSound(SOUND_FINISH);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		if(st.isCreated())
		{
			if(st.getPlayer().getLevel() < 38)
				htmltext = "30895-00.htm";
			else
				htmltext = "30895-01.htm";
		}
		else if(st.getCond() == 1)
			htmltext = "30895-03.htm";

		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		if(st.getCond() == 1)
		{

			if(st.rollAndGive(ALLIGATOR_TOOTH, 1, CHANCE))
				st.playSound(SOUND_ITEMGET);

			if(st.rollAndGiveLimited(TORN_MAP_FRAGMENT, 1, CHANCE2, 10))
			{
				if(st.getQuestItemsCount(TORN_MAP_FRAGMENT) < 10)
					st.playSound(SOUND_ITEMGET);
				else
					st.playSound(SOUND_MIDDLE);
			}
		}
	}
}