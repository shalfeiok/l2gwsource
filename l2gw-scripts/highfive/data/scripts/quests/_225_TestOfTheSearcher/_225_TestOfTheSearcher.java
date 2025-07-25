package quests._225_TestOfTheSearcher;

import ru.l2gw.gameserver.model.L2ObjectsStorage;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;
import ru.l2gw.gameserver.model.quest.QuestTimer;

/**
 * Квест на вторую профессию Test Of The Searcher
 *
 * @author Sergey Ibryaev aka Artful
 */

public class _225_TestOfTheSearcher extends Quest
{
	//NPC
	private static final int Luther = 30690;
	private static final int Alex = 30291;
	private static final int Tyra = 30420;
	private static final int Chest = 30628;
	private static final int Leirynn = 30728;
	private static final int Borys = 30729;
	private static final int Jax = 30730;
	private static final int Tree = 30627;
	//Quest Items
	private static final int LuthersLetter = 2784;
	private static final int AlexsWarrant = 2785;
	private static final int Leirynns1stOrder = 2786;
	private static final int DeluTotem = 2787;
	private static final int Leirynns2ndOrder = 2788;
	private static final int ChiefKalkisFang = 2789;
	private static final int AlexsRecommend = 2808;
	private static final int LambertsMap = 2792;
	private static final int LeirynnsReport = 2790;
	private static final int AlexsLetter = 2793;
	private static final int StrangeMap = 2791;
	private static final int AlexsOrder = 2794;
	private static final int CombinedMap = 2805;
	private static final int GoldBar = 2807;
	private static final int WineCatalog = 2795;
	private static final int OldOrder = 2799;
	private static final int MalrukianWine = 2798;
	private static final int TyrasContract = 2796;
	private static final int RedSporeDust = 2797;
	private static final int JaxsDiary = 2800;
	private static final int SoltsMap = 2803;
	private static final int MakelsMap = 2804;
	private static final int RustedKey = 2806;
	private static final int TornMapPiece1st = 2801;
	private static final int TornMapPiece2st = 2802;
	//Items
	private static final int MarkOfSearcher = 2809;
	//MOB
	private static final int DeluLizardmanShaman = 20781;
	private static final int DeluLizardmanAssassin = 27094;
	private static final int DeluChiefKalkis = 27093;
	private static final int GiantFungus = 20555;
	private static final int RoadScavenger = 20551;
	private static final int HangmanTree = 20144;
	//Drop Cond
	//# [COND, NEWCOND, ID, REQUIRED, ITEM, NEED_COUNT, CHANCE, DROP]	
	private static final int[][] DROPLIST_COND = {
			{3, 4, DeluLizardmanShaman, 0, DeluTotem, 10, 100, 1},
			{3, 4, DeluLizardmanAssassin, 0, DeluTotem, 10, 100, 1},
			{10, 11, GiantFungus, 0, RedSporeDust, 10, 100, 1}};

	private static boolean QuestProf = true;

	public _225_TestOfTheSearcher()
	{
		super(225, "_225_TestOfTheSearcher", "Test Of The Searcher");
		addStartNpc(Luther);
		addTalkId(Alex);
		addTalkId(Leirynn);
		addTalkId(Borys);
		addTalkId(Tyra);
		addTalkId(Jax);
		addTalkId(Tree);
		addTalkId(Chest);
		//Mob Drop
		addKillId(DeluChiefKalkis);
		addKillId(RoadScavenger);
		addKillId(HangmanTree);

		for(int[] cond : DROPLIST_COND)
			addKillId(cond[2]);

		addQuestItem(DeluTotem,
				RedSporeDust,
				LuthersLetter,
				AlexsWarrant,
				Leirynns1stOrder,
				Leirynns2ndOrder,
				LeirynnsReport,
				ChiefKalkisFang,
				StrangeMap,
				LambertsMap,
				AlexsLetter,
				AlexsOrder,
				WineCatalog,
				TyrasContract,
				OldOrder,
				MalrukianWine,
				JaxsDiary,
				TornMapPiece1st,
				TornMapPiece2st,
				SoltsMap,
				MakelsMap,
				RustedKey,
				CombinedMap);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30690-05.htm"))
		{
			st.giveItems(LuthersLetter, 1);
			st.set("cond", "1");
			st.setState(STARTED);
			if(!st.getPlayer().getVarB("dd3"))
			{
				st.giveItems(7562, 82);
				st.getPlayer().setVar("dd3", "1");
			}
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30291-07.htm"))
		{
			st.takeItems(LeirynnsReport, -1);
			st.takeItems(StrangeMap, -1);
			st.giveItems(LambertsMap, 1);
			st.giveItems(AlexsLetter, 1);
			st.giveItems(AlexsOrder, 1);
			st.set("cond", "8");
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("30420-01a.htm"))
		{
			st.takeItems(WineCatalog, -1);
			st.giveItems(TyrasContract, 1);
			st.set("cond", "10");
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("30730-01d.htm"))
		{
			st.takeItems(OldOrder, -1);
			st.giveItems(JaxsDiary, 1);
			st.set("cond", "14");
			st.setState(STARTED);
		}
		else if(event.equalsIgnoreCase("30627-01a.htm"))
		{
			L2NpcInstance isQuest = L2ObjectsStorage.getByNpcId(Chest);
			if(isQuest == null)
			{
				if(st.getQuestItemsCount(RustedKey) == 0)
					st.giveItems(RustedKey, 1);
				st.getPcSpawn().addSpawn(Chest);
				st.startQuestTimer("Chest", 300000);
				st.set("cond", "17");
				st.setState(STARTED);
			}
			else
			{
				QuestTimer timer = st.getQuestTimer("Wait1");
				if(timer == null)
					st.startQuestTimer("Wait1", 300000);
				htmltext = "<html><head><body>Plees wait 5 minutes</body></html>";
			}
		}
		else if(event.equalsIgnoreCase("30628-01a.htm"))
		{
			st.takeItems(RustedKey, -1);
			st.giveItems(GoldBar, 20);
			st.set("cond", "18");
		}
		else if(event.equalsIgnoreCase("Wait1") || event.equalsIgnoreCase("Chest"))
		{
			L2NpcInstance isQuest = L2ObjectsStorage.getByNpcId(Chest);
			if(isQuest != null)
				isQuest.deleteMe();
			QuestTimer timer = st.getQuestTimer("Wait1");
			if(timer != null)
				timer.cancel();
			timer = st.getQuestTimer("Chest");
			if(timer != null)
				timer.cancel();
			if(st.getInt("cond") == 17)
				st.set("cond", "16");
			return null;
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getInt("cond");
		if(npcId == Luther)
		{
			if(st.getQuestItemsCount(MarkOfSearcher) != 0)
			{
				htmltext = "completed";
				st.exitCurrentQuest(true);
			}
			else if(st.isCreated())
			{
				if(st.getPlayer().getClassId().getId() == 0x07 || st.getPlayer().getClassId().getId() == 0x16 || st.getPlayer().getClassId().getId() == 0x23 || st.getPlayer().getClassId().getId() == 0x36)
				{
					if(st.getPlayer().getLevel() >= 39)
					{
						if(st.getPlayer().getClassId().getId() == 0x36)
							htmltext = "30690-04.htm";
						else
							htmltext = "30690-03.htm";
					}
					else
					{
						htmltext = "30690-02.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30690-01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30690-06.htm";
			else if(cond > 1 && cond < 16)
				htmltext = "30623-17.htm";
			else if(cond == 19)
			{
				htmltext = "30690-08.htm";
				if(!st.getPlayer().getVarB("q225"))
				{
					st.addExpAndSp(447444, 30704);
					st.rollAndGive(57, 80093, 100);
					st.getPlayer().setVar("q225", "1");
				}
				st.takeItems(AlexsRecommend, -1);
				st.giveItems(MarkOfSearcher, 1);
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == Alex)
		{
			if(cond == 1)
			{
				htmltext = "30291-01.htm";
				st.takeItems(LuthersLetter, -1);
				st.giveItems(AlexsWarrant, 1);
				st.set("cond", "2");
				st.setState(STARTED);
			}
			else if(cond == 2)
				htmltext = "30291-02.htm";
			else if(cond > 2 && cond < 7)
				htmltext = "30291-03.htm";
			else if(cond == 7)
				htmltext = "30291-04.htm";
			else if(cond == 8)
				htmltext = "30291-08.htm";
			else if(cond == 13 || cond == 14)
				htmltext = "30291-09.htm";
			else if(cond == 18)
			{
				st.takeItems(AlexsOrder, -1);
				st.takeItems(CombinedMap, -1);
				st.takeItems(GoldBar, -1);
				st.giveItems(AlexsRecommend, 1);
				htmltext = "30291-11.htm";
				st.set("cond", "19");
				st.setState(STARTED);
			}
			else if(cond == 19)
				htmltext = "30291-12.htm";

		}
		else if(npcId == Leirynn)
		{
			if(cond == 2)
			{
				htmltext = "30728-01.htm";
				st.takeItems(AlexsWarrant, -1);
				st.giveItems(Leirynns1stOrder, 1);
				st.set("cond", "3");
				st.setState(STARTED);
			}
			else if(cond == 3)
				htmltext = "30728-02.htm";
			else if(cond == 4)
			{
				htmltext = "30728-03.htm";
				st.takeItems(DeluTotem, -1);
				st.takeItems(Leirynns1stOrder, -1);
				st.giveItems(Leirynns2ndOrder, 1);
				st.set("cond", "5");
				st.setState(STARTED);
			}
			else if(cond == 5)
				htmltext = "30728-04.htm";
			else if(cond == 6)
			{
				st.takeItems(ChiefKalkisFang, -1);
				st.takeItems(Leirynns2ndOrder, -1);
				st.giveItems(LeirynnsReport, 1);
				htmltext = "30728-05.htm";
				st.set("cond", "7");
				st.setState(STARTED);
			}
			else if(cond == 7)
				htmltext = "30728-06.htm";
			else if(cond == 8)
				htmltext = "30728-07.htm";
		}
		else if(npcId == Borys)
		{
			if(cond == 8)
			{
				st.takeItems(AlexsLetter, -1);
				st.giveItems(WineCatalog, 1);
				htmltext = "30729-01.htm";
				st.set("cond", "9");
				st.setState(STARTED);
			}
			else if(cond == 9)
				htmltext = "30729-02.htm";
			else if(cond == 12)
			{
				st.takeItems(WineCatalog, -1);
				st.takeItems(MalrukianWine, -1);
				st.giveItems(OldOrder, 1);
				htmltext = "30729-03.htm";
				st.set("cond", "13");
				st.setState(STARTED);
			}
			else if(cond == 13)
				htmltext = "30729-04.htm";
			else if(cond >= 8 && cond <= 14)
				htmltext = "30729-05.htm";
		}
		else if(npcId == Tyra)
		{
			if(cond == 9)
				htmltext = "30420-01.htm";
			else if(cond == 10)
				htmltext = "30420-02.htm";
			else if(cond == 11)
			{
				st.takeItems(TyrasContract, -1);
				st.takeItems(RedSporeDust, -1);
				st.giveItems(MalrukianWine, 1);
				htmltext = "30420-03.htm";
				st.set("cond", "12");
				st.setState(STARTED);
			}
			else if(cond == 12 || cond == 13)
				htmltext = "30420-04.htm";
		}
		else if(npcId == Jax)
		{
			if(cond == 13)
				htmltext = "30730-01.htm";
			else if(cond == 14)
				htmltext = "30730-02.htm";
			else if(cond == 15)
			{
				st.takeItems(SoltsMap, -1);
				st.takeItems(MakelsMap, -1);
				st.takeItems(LambertsMap, -1);
				st.takeItems(JaxsDiary, -1);
				st.giveItems(CombinedMap, 1);
				htmltext = "30730-03.htm";
				st.set("cond", "16");
				st.setState(STARTED);
			}
			else if(cond == 16)
				htmltext = "30730-04.htm";
		}
		else if(npcId == Tree)
		{
			if(cond == 16 || cond == 17)
				htmltext = "30627-01.htm";
		}
		else if(npcId == Chest)
		{
			if(cond == 17)
				htmltext = "30628-01.htm";
			else
				htmltext = "<html><head><body>You haven't got a Key for this Chest.</body></html>";
		}
		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		for(int[] aDROPLIST_COND : DROPLIST_COND)
			if(cond == aDROPLIST_COND[0] && npcId == aDROPLIST_COND[2])
				if(aDROPLIST_COND[3] == 0 || st.getQuestItemsCount(aDROPLIST_COND[3]) > 0)
				{
					if(aDROPLIST_COND[5] == 0)
						st.rollAndGive(aDROPLIST_COND[4], aDROPLIST_COND[7], aDROPLIST_COND[6]);
					else if(st.rollAndGiveLimited(aDROPLIST_COND[4], aDROPLIST_COND[7], aDROPLIST_COND[6], aDROPLIST_COND[5]))
					{
						if(st.getQuestItemsCount(aDROPLIST_COND[4]) == aDROPLIST_COND[5] && aDROPLIST_COND[1] != cond && aDROPLIST_COND[1] != 0)
						{
							st.playSound(SOUND_MIDDLE);
							st.setCond(aDROPLIST_COND[1]);
							st.setState(STARTED);
						}
						else
							st.playSound(SOUND_ITEMGET);
					}
				}
		if(cond == 5 && npcId == DeluChiefKalkis)
		{
			if(st.getQuestItemsCount(StrangeMap) == 0)
				st.giveItems(StrangeMap, 1);
			if(st.getQuestItemsCount(ChiefKalkisFang) == 0)
				st.giveItems(ChiefKalkisFang, 1);
			st.playSound(SOUND_MIDDLE);
			st.set("cond", "6");
			st.setState(STARTED);
		}
		else if(cond == 14)
		{
			if(npcId == RoadScavenger && st.getQuestItemsCount(SoltsMap) == 0)
			{
				st.giveItems(TornMapPiece1st, 1);
				if(st.getQuestItemsCount(TornMapPiece1st) >= 4)
				{
					st.takeItems(TornMapPiece1st, -1);
					st.giveItems(SoltsMap, 1);
				}
			}
			else if(npcId == HangmanTree && st.getQuestItemsCount(MakelsMap) == 0)
			{
				st.giveItems(TornMapPiece2st, 1);
				if(st.getQuestItemsCount(TornMapPiece2st) >= 4)
				{
					st.takeItems(TornMapPiece2st, -1);
					st.giveItems(MakelsMap, 1);
				}
			}
			if(st.getQuestItemsCount(SoltsMap) != 0 && st.getQuestItemsCount(MakelsMap) != 0)
			{
				st.set("cond", "15");
				st.setState(STARTED);
			}
		}
	}
}