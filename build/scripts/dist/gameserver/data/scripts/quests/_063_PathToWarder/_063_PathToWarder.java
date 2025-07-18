package quests._063_PathToWarder;

import ru.l2gw.commons.math.Rnd;
import ru.l2gw.gameserver.model.base.ClassId;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

public class _063_PathToWarder extends Quest
{
	int Sione = 32195;
	int Gobie = 32198;
	int Patrol = 20053;
	int Novice = 20782;
	int Bathis = 30332;
	int Tobias = 30297;
	int Tak = 27337;
	int Maille = 20919;
	int Maille_scout = 20920;
	int Maille_guard = 20921;

	int OlMahumOrders = 9762;
	int OlMahumOrganizationChart = 9763;
	int GobiesOrders = 9764;
	int LettertotheHumans = 9765;
	int HumansReply = 9766;
	int LettertotheDarkElves = 9767;
	int DarkElvesReply = 9768;
	int ReporttoSione = 9769;
	int EmptySoulCrystal = 9770; //empty
	int TaksCapturedSoul = 9771;
	int SteelrazorEvaluation = 9772;

	public _063_PathToWarder()
	{
		super(63, "_063_PathToWarder", "Path To Warder");

		addStartNpc(Sione);
		addTalkId(Sione);
		addTalkId(Gobie);
		addTalkId(Bathis);
		addTalkId(Tobias);
		addKillId(Patrol);
		addKillId(Novice);
		addKillId(Tak);
		addKillId(Maille);
		addKillId(Maille_scout);
		addKillId(Maille_guard);
		addQuestItem(OlMahumOrganizationChart, OlMahumOrders, TaksCapturedSoul, EmptySoulCrystal);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		if(event.equals("master_sione_q0063_06.htm"))
		{
			st.setState(STARTED);
			st.set("cond", "1");
			st.playSound(SOUND_ACCEPT);
		}
		if(event.equals("master_sione_q0063_08.htm"))
			st.set("cond", "2");
		else if(event.equals("captain_bathia_q0063_04.htm"))
		{
			st.takeItems(LettertotheHumans, 1);
			st.giveItems(HumansReply, 1);
			st.set("cond", "6");
		}
		else if(event.equals("master_gobie_q0063_08.htm"))
		{
			st.takeItems(HumansReply, 1);
			st.giveItems(LettertotheDarkElves, 1);
			st.set("cond", "7");
		}
		else if(event.equals("master_tobias_q0063_05.htm"))
		{
			st.takeItems(LettertotheDarkElves, 1);
			st.giveItems(DarkElvesReply, 1);
			st.set("cond", "8");
		}
		else if(event.equals("master_gobie_q0063_11.htm"))
		{
			st.takeItems(DarkElvesReply, 1);
			st.giveItems(ReporttoSione, 1);
			st.set("cond", "9");
		}
		else if(event.equals("master_gobie_q0063_16.htm"))
		{
			st.takeItems(EmptySoulCrystal, 1);
			st.set("cond", "11");
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(npcId == Sione)
			if(st.isCreated())
			{
				if(st.getPlayer().getClassId() != ClassId.femaleSoldier)
				{
					htmltext = "master_sione_q0063_04.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() < 18)
				{
					htmltext = "master_sione_q0063_02.htm";
					st.exitCurrentQuest(true);
				}
				else
					htmltext = "master_sione_q0063_05.htm";
			}
			else
			{
				if(cond == 1)
					htmltext = "master_sione_q0063_06.htm";
				if(cond == 3)
					if(st.getQuestItemsCount(OlMahumOrders) < 10 && st.getQuestItemsCount(OlMahumOrganizationChart) < 5)
						htmltext = "master_sione_q0063_09.htm";
					else
					{
						htmltext = "master_sione_q0063_10.htm";
						st.set("cond", "4");
						st.takeItems(OlMahumOrders, -1);
						st.takeItems(OlMahumOrganizationChart, -1);
						st.giveItems(GobiesOrders, 1);
					}
				if(cond == 9)
				{
					st.takeItems(ReporttoSione, 1);
					st.set("cond", "10");
					htmltext = "master_sione_q0063_13.htm";
				}
			}
		if(npcId == Gobie)
		{
			if(cond == 4)
				if(st.getQuestItemsCount(GobiesOrders) < 1)
					htmltext = "master_gobie_q0063_01.htm";
				else
				{
					htmltext = "master_gobie_q0063_03.htm";
					st.takeItems(GobiesOrders, 1);
					st.giveItems(LettertotheHumans, 1);
					st.set("cond", "5");
				}
			if(cond == 6)
				htmltext = "master_gobie_q0063_05.htm";
			if(cond == 8)
				htmltext = "master_gobie_q0063_10.htm";
			if(cond == 10)
				htmltext = "master_gobie_q0063_14.htm";
			if(cond == 11)
				htmltext = "master_gobie_q0063_17.htm";
			if(cond == 12)
				if(st.getQuestItemsCount(TaksCapturedSoul) > 0)
				{
					st.takeItems(TaksCapturedSoul, 1);
					if(st.getPlayer().getClassId().getLevel() == 1)
					{
						st.giveItems(SteelrazorEvaluation, 1);
						if(!st.getPlayer().getVarB("prof1"))
						{
							st.getPlayer().setVar("prof1", "1");
							if(st.getPlayer().getLevel() >= 20)
								st.addExpAndSp(320534, 22046);
							else if(st.getPlayer().getLevel() == 19)
								st.addExpAndSp(456128, 28744);
							else
								st.addExpAndSp(591724, 35442);
							st.rollAndGive(57, 163800, 100);
						}
					}
					st.showSocial(3);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					htmltext = "master_gobie_q0063_20.htm";
				}
				else
					htmltext = "master_gobie_q0063_19.htm";
		}
		if(npcId == Bathis && cond == 5)
			htmltext = "captain_bathia_q0063_01.htm";
		if(npcId == Tobias && cond == 7)
			htmltext = "master_tobias_q0063_01.htm";
		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(cond == 2)
		{
			if(npcId == Patrol && st.rollAndGiveLimited(OlMahumOrganizationChart, 1, 100, 5))
				st.playSound(SOUND_ITEMGET);
			else if(npcId == Novice && st.rollAndGiveLimited(OlMahumOrders, 1, 100, 10))
				st.playSound(SOUND_ITEMGET);

			if(st.getQuestItemsCount(OlMahumOrders) == 10 && st.getQuestItemsCount(OlMahumOrganizationChart) == 5)
			{
				st.set("cond", "3");
				st.playSound(SOUND_MIDDLE);
				st.setState(STARTED);
			}
		}
		if(cond == 11)
			if((npcId == Maille || npcId == Maille_scout || npcId == Maille_guard) && Rnd.chance(20))
				st.getPcSpawn().addSpawn(Tak);
			else if(npcId == Tak)
			{
				st.takeItems(EmptySoulCrystal, 1);
				st.giveItems(TaksCapturedSoul, 1);
				st.set("cond", "12");
				st.playSound(SOUND_MIDDLE);
				st.setState(STARTED);
			}
	}
}