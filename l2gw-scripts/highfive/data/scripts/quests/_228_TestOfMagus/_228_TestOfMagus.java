package quests._228_TestOfMagus;

import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;

/**
 * Квест на вторую профессию Test Of Magus
 *
 * @author Sergey Ibryaev aka Artful
 */

public class _228_TestOfMagus extends Quest
{
	//NPC
	private static final int Rukal = 30629;
	private static final int Parina = 30391;
	private static final int Casian = 30612;
	private static final int Salamander = 30411;
	private static final int Sylph = 30412;
	private static final int Undine = 30413;
	private static final int Snake = 30409;
	//Quest Items
	private static final int RukalsLetter = 2841;
	private static final int ParinasLetter = 2842;
	private static final int LilacCharm = 2843;
	private static final int GoldenSeed1st = 2844;
	private static final int GoldenSeed2st = 2845;
	private static final int GoldenSeed3st = 2846;
	private static final int ScoreOfElements = 2847;
	private static final int ToneOfWater = 2856;
	private static final int ToneOfFire = 2857;
	private static final int ToneOfWind = 2858;
	private static final int ToneOfEarth = 2859;
	private static final int UndineCharm = 2862;
	private static final int DazzlingDrop = 2848;
	private static final int SalamanderCharm = 2860;
	private static final int FlameCrystal = 2849;
	private static final int SylphCharm = 2861;
	private static final int HarpysFeather = 2850;
	private static final int WyrmsWingbone = 2851;
	private static final int WindsusMane = 2852;
	private static final int SerpentCharm = 2863;
	private static final int EnchantedMonsterEyeShell = 2853;
	private static final int EnchantedStoneGolemPowder = 2854;
	private static final int EnchantedIronGolemScrap = 2855;
	//Items
	private static final int MarkOfMagus = 2840;
	//MOB
	private static final int SingingFlowerPhantasm = 27095;
	private static final int SingingFlowerNightmare = 27096;
	private static final int SingingFlowerDarkling = 27097;
	private static final int Harpy = 20145;
	private static final int Wyrm = 20176;
	private static final int Windsus = 20553;
	private static final int EnchantedMonstereye = 20564;
	private static final int EnchantedStoneGolem = 20565;
	private static final int EnchantedIronGolem = 20566;
	private static final int QuestMonsterGhostFire = 27098;
	private static final int MarshStakatoWorker = 20230;
	private static final int ToadLord = 20231;
	private static final int MarshStakato = 20157;
	private static final int MarshStakatoSoldier = 20232;
	private static final int MarshStakatoDrone = 20234;

	//Drop Cond
	//# [COND, NEWCOND, ID, REQUIRED, ITEM, NEED_COUNT, CHANCE, DROP]	
	private static final int[][] DROPLIST_COND = {
			{3, 0, SingingFlowerPhantasm, LilacCharm, GoldenSeed1st, 10, 100, 1},
			{3, 0, SingingFlowerNightmare, LilacCharm, GoldenSeed2st, 10, 100, 1},
			{3, 0, SingingFlowerDarkling, LilacCharm, GoldenSeed3st, 10, 100, 1},
			{5, 0, Harpy, SylphCharm, HarpysFeather, 20, 50, 2},
			{5, 0, Wyrm, SylphCharm, WyrmsWingbone, 10, 50, 2},
			{5, 0, Windsus, SylphCharm, WindsusMane, 10, 50, 2},
			{5, 0, EnchantedMonstereye, SerpentCharm, EnchantedMonsterEyeShell, 10, 100, 2},
			{5, 0, EnchantedStoneGolem, SerpentCharm, EnchantedStoneGolemPowder, 10, 100, 2},
			{5, 0, EnchantedIronGolem, SerpentCharm, EnchantedIronGolemScrap, 10, 100, 2},
			{5, 0, QuestMonsterGhostFire, SalamanderCharm, FlameCrystal, 5, 50, 1},
			{5, 0, MarshStakatoWorker, UndineCharm, DazzlingDrop, 20, 30, 2},
			{5, 0, ToadLord, UndineCharm, DazzlingDrop, 20, 30, 2},
			{5, 0, MarshStakato, UndineCharm, DazzlingDrop, 20, 30, 2},
			{5, 0, MarshStakatoSoldier, UndineCharm, DazzlingDrop, 20, 40, 2},
			{5, 0, MarshStakatoDrone, UndineCharm, DazzlingDrop, 20, 50, 2}};

	private static boolean QuestProf = true;

	public _228_TestOfMagus()
	{
		super(228, "_228_TestOfMagus", "Test Of Magus");

		addStartNpc(Rukal);

		addTalkId(Parina);
		addTalkId(Casian);
		addTalkId(Sylph);
		addTalkId(Snake);
		addTalkId(Undine);
		addTalkId(Salamander);

		for(int[] cond : DROPLIST_COND)
			addKillId(cond[2]);

		addQuestItem(RukalsLetter,
				ParinasLetter,
				LilacCharm,
				ToneOfWind,
				SylphCharm,
				SerpentCharm,
				ToneOfEarth,
				UndineCharm,
				ToneOfFire,
				SalamanderCharm,
				ToneOfWater,
				ScoreOfElements,
				GoldenSeed1st,
				GoldenSeed2st,
				GoldenSeed3st,
				HarpysFeather,
				WyrmsWingbone,
				WindsusMane,
				EnchantedMonsterEyeShell,
				EnchantedStoneGolemPowder,
				EnchantedIronGolemScrap,
				FlameCrystal,
				DazzlingDrop);
	}

	public void checkBooks(QuestState st)
	{
		if(st.getQuestItemsCount(ToneOfWater) != 0 && st.getQuestItemsCount(ToneOfFire) != 0 && st.getQuestItemsCount(ToneOfWind) != 0 && st.getQuestItemsCount(ToneOfEarth) != 0)
		{
			st.set("cond", "6");
			st.setState(STARTED);
		}
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		String htmltext = event;
		if(event.equalsIgnoreCase("30629-04.htm"))
		{
			st.giveItems(RukalsLetter, 1);
			st.set("cond", "1");
			st.setState(STARTED);
			if(!st.getPlayer().getVarB("dd3"))
			{
				st.giveItems(7562, 64);
				st.getPlayer().setVar("dd3", "1");
			}
			st.playSound(SOUND_ACCEPT);
		}
		else if(event.equalsIgnoreCase("30391-02.htm"))
		{
			st.takeItems(RukalsLetter, -1);
			st.giveItems(ParinasLetter, 1);
			st.set("cond", "2");
			st.setState(STARTED);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30612-02.htm"))
		{
			st.takeItems(ParinasLetter, -1);
			st.giveItems(LilacCharm, 1);
			st.set("cond", "3");
			st.setState(STARTED);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30629-10.htm"))
		{
			st.takeItems(LilacCharm, -1);
			st.takeItems(GoldenSeed1st, -1);
			st.takeItems(GoldenSeed2st, -1);
			st.takeItems(GoldenSeed3st, -1);
			st.giveItems(ScoreOfElements, 1);
			st.set("cond", "5");
			st.setState(STARTED);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30412-02.htm"))
		{
			st.giveItems(SylphCharm, 1);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("30409-03.htm"))
		{
			st.giveItems(SerpentCharm, 1);
			st.playSound(SOUND_MIDDLE);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		String htmltext = "noquest";
		int cond = st.getInt("cond");
		if(npcId == Rukal)
		{
			if(st.getQuestItemsCount(MarkOfMagus) != 0)
			{
				htmltext = "completed";
				st.exitCurrentQuest(true);
			}
			else if(st.isCreated())
			{
				if(st.getPlayer().getClassId().getId() == 0x0b || st.getPlayer().getClassId().getId() == 0x1a || st.getPlayer().getClassId().getId() == 0x27)
				{
					if(st.getPlayer().getLevel() >= 39)
						htmltext = "30629-03.htm";
					else
					{
						htmltext = "30629-02.htm";
						st.exitCurrentQuest(true);
					}
				}
				else
				{
					htmltext = "30629-01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
				htmltext = "30629-05.htm";
			else if(cond == 2)
				htmltext = "30629-06.htm";
			else if(cond == 3)
				htmltext = "30629-07.htm";
			else if(cond == 4)
				htmltext = "30629-08.htm";
			else if(cond == 5)
				htmltext = "30629-11.htm";
			else if(cond == 6)
			{
				st.takeItems(ScoreOfElements, -1);
				st.takeItems(ToneOfWater, -1);
				st.takeItems(ToneOfFire, -1);
				st.takeItems(ToneOfWind, -1);
				st.takeItems(ToneOfEarth, -1);
				st.giveItems(MarkOfMagus, 1);
				htmltext = "30629-12.htm";
				if(!st.getPlayer().getVarB("q227"))
				{
					st.addExpAndSp(1029122, 70620);
					st.rollAndGive(57, 186077, 100);
					st.getPlayer().setVar("q227", "1");
				}
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
			}
		}
		else if(npcId == Parina)
		{
			if(cond == 1)
				htmltext = "30391-01.htm";
			else if(cond == 2)
				htmltext = "30391-03.htm";
			else if(cond == 3 || cond == 4)
				htmltext = "30391-04.htm";
			else if(cond >= 5)
				htmltext = "30391-05.htm";
		}
		else if(npcId == Casian)
		{
			if(cond == 2)
				htmltext = "30612-01.htm";
			else if(cond == 3)
				htmltext = "30612-03.htm";
			else if(cond == 4)
				htmltext = "30612-04.htm";
			else if(cond >= 5)
				htmltext = "30612-05.htm";
		}
		else if(npcId == Salamander && cond == 5)
		{
			if(st.getQuestItemsCount(ToneOfFire) == 0)
			{
				if(st.getQuestItemsCount(SalamanderCharm) == 0)
				{
					htmltext = "30411-01.htm";
					st.giveItems(SalamanderCharm, 1);
					st.playSound(SOUND_MIDDLE);
				}
				else if(st.getQuestItemsCount(FlameCrystal) < 5)
					htmltext = "30411-02.htm";
				else
				{
					st.takeItems(SalamanderCharm, -1);
					st.takeItems(FlameCrystal, -1);
					st.giveItems(ToneOfFire, 1);
					htmltext = "30411-03.htm";
					checkBooks(st);
					st.playSound(SOUND_MIDDLE);
				}
			}
			else
				htmltext = "30411-04.htm";
		}
		else if(npcId == Sylph && cond == 5)
		{
			if(st.getQuestItemsCount(ToneOfWind) == 0)
			{
				if(st.getQuestItemsCount(SylphCharm) == 0)
					htmltext = "30412-01.htm";
				else if(st.getQuestItemsCount(HarpysFeather) < 20 || st.getQuestItemsCount(WyrmsWingbone) < 10 || st.getQuestItemsCount(WindsusMane) < 10)
					htmltext = "30412-03.htm";
				else
				{
					st.takeItems(SylphCharm, -1);
					st.takeItems(HarpysFeather, -1);
					st.takeItems(WyrmsWingbone, -1);
					st.takeItems(WindsusMane, -1);
					st.giveItems(ToneOfWind, 1);
					htmltext = "30412-04.htm";
					checkBooks(st);
					st.playSound(SOUND_MIDDLE);
				}
			}
			else
				htmltext = "30412-05.htm";
		}
		else if(npcId == Snake && cond == 5)
		{
			if(st.getQuestItemsCount(ToneOfEarth) == 0)
			{
				if(st.getQuestItemsCount(SerpentCharm) == 0)
					htmltext = "30409-01.htm";
				else if(st.getQuestItemsCount(EnchantedMonsterEyeShell) < 10 || st.getQuestItemsCount(EnchantedStoneGolemPowder) < 10 || st.getQuestItemsCount(EnchantedIronGolemScrap) < 10)
					htmltext = "30409-04.htm";
				else
				{
					st.takeItems(SerpentCharm, -1);
					st.takeItems(EnchantedMonstereye, -1);
					st.takeItems(EnchantedStoneGolemPowder, -1);
					st.takeItems(EnchantedIronGolemScrap, -1);
					st.giveItems(ToneOfEarth, 1);
					htmltext = "30409-05.htm";
					checkBooks(st);
					st.playSound(SOUND_MIDDLE);
				}
			}
			else
				htmltext = "30409-06.htm";
		}
		else if(npcId == Undine && cond == 5)
		{
			if(st.getQuestItemsCount(ToneOfWater) == 0)
			{
				if(st.getQuestItemsCount(UndineCharm) == 0)
				{
					htmltext = "30413-01.htm";
					st.giveItems(UndineCharm, 1);
					st.playSound(SOUND_MIDDLE);
				}
				else if(st.getQuestItemsCount(DazzlingDrop) < 20)
					htmltext = "30413-02.htm";
				else
				{
					st.takeItems(UndineCharm, -1);
					st.takeItems(DazzlingDrop, -1);
					st.giveItems(ToneOfWater, 1);
					htmltext = "30413-03.htm";
					checkBooks(st);
					st.playSound(SOUND_MIDDLE);
				}
			}
			else
				htmltext = "30413-04.htm";
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
		if(cond == 3 && st.getQuestItemsCount(GoldenSeed1st) != 0 && st.getQuestItemsCount(GoldenSeed2st) != 0 && st.getQuestItemsCount(GoldenSeed3st) != 0)
		{
			st.set("cond", "4");
			st.setState(STARTED);
		}
	}
}