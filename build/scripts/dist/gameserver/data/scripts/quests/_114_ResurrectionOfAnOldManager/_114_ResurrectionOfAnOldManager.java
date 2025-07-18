package quests._114_ResurrectionOfAnOldManager;

import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.gameserver.ai.CtrlIntention;
import ru.l2gw.gameserver.clientpackets.Say2C;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.L2Spawn;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;
import ru.l2gw.gameserver.serverpackets.ExShowScreenMessage;
import ru.l2gw.gameserver.serverpackets.ExShowScreenMessage.ScreenMessageAlign;
import ru.l2gw.gameserver.tables.NpcTable;
import ru.l2gw.gameserver.templates.L2NpcTemplate;
import ru.l2gw.util.Location;

public class _114_ResurrectionOfAnOldManager extends Quest
{
	// NPC
	private static final int NEWYEAR = 31961;
	private static final int YUMI = 32041;
	private static final int STONES = 32046;
	private static final int WENDY = 32047;
	private static final int BOX = 32050;

	// MOBS
	private static final int GUARDIAN = 27318;

	// QUEST ITEMS
	private static final int DETECTOR = 8090;
	private static final int DETECTOR2 = 8091;
	private static final int STARSTONE = 8287;
	private static final int LETTER = 8288;
	private static final int STARSTONE2 = 8289;

	L2Spawn guardianSpawn;

	public _114_ResurrectionOfAnOldManager()
	{
		super(114, "_114_ResurrectionOfAnOldManager", "Resurrection Of An Old Manager");

		addStartNpc(YUMI);

		addTalkId(WENDY);
		addTalkId(BOX);
		addTalkId(STONES);
		addTalkId(NEWYEAR);
		addFirstTalkId(STONES);

		addKillId(GUARDIAN);

		addQuestItem(DETECTOR);
		addQuestItem(DETECTOR2);
		addQuestItem(STARSTONE);
		addQuestItem(LETTER);
		addQuestItem(STARSTONE2);
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		String htmltext = event;
		int choice;

		if(event.equalsIgnoreCase("head_blacksmith_newyear_q0114_02.htm"))
		{
			st.set("cond", "22");
			st.takeItems(LETTER, 1);
			st.giveItems(STARSTONE2, 1);
			st.playSound(SOUND_MIDDLE);
		}
		if(event.equalsIgnoreCase("collecter_yumi_q0114_04.htm"))
		{
			st.setState(STARTED);
			st.playSound(SOUND_ACCEPT);
			st.set("cond", "1");
			st.set("talk", "0");
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_08.htm"))
			st.set("talk", "1");
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_09.htm"))
		{
			st.set("cond", "2");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_12.htm"))
		{
			choice = st.getInt("choice");
			if(choice == 1)
				htmltext = "collecter_yumi_q0114_12.htm";
			else if(choice == 2)
				htmltext = "collecter_yumi_q0114_13.htm";
			else if(choice == 3)
				htmltext = "collecter_yumi_q0114_14.htm";
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_15.htm"))
			st.set("talk", "1");
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_23.htm"))
			st.set("talk", "2");
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_26.htm"))
		{
			st.set("cond", "6");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_31.htm"))
		{
			st.set("cond", "17");
			st.playSound(SOUND_MIDDLE);
			st.giveItems(DETECTOR, 1);
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_34.htm"))
		{
			st.takeItems(DETECTOR2, 1);
			st.set("talk", "1");
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_38.htm"))
		{
			choice = st.getInt("choice");
			if(choice > 1)
				htmltext = "collecter_yumi_q0114_37.htm";
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_40.htm"))
		{
			st.set("cond", "21");
			st.giveItems(LETTER, 1);
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("collecter_yumi_q0114_39.htm"))
		{
			st.set("cond", "20");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("pavel_atlanta_q0114_03.htm"))
		{
			st.set("cond", "19");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("pavel_atlanta_q0114_07.htm"))
		{
			st.playSound(SOUND_FINISH);
			st.addExpAndSp(410358, 32060);
			st.exitCurrentQuest(false);
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_01.htm"))
		{
			if(st.getInt("talk") + st.getInt("talk1") == 2)
				htmltext = "chaos_secretary_wendy_q0114_05.htm";
			else if(st.getInt("talk") + st.getInt("talk1") + st.getInt("talk2") == 6)
				htmltext = "chaos_secretary_wendy_q0114_06a.htm";
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_02.htm"))
		{
			if(st.getInt("talk") == 0)
				st.set("talk", "1");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_03.htm"))
		{
			if(st.getInt("talk1") == 0)
				st.set("talk1", "1");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_06.htm"))
		{
			st.set("cond", "3");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
			st.set("choice", "1");
			st.unset("talk1");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_07.htm"))
		{
			st.set("cond", "4");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
			st.set("choice", "2");
			st.unset("talk1");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_09.htm"))
		{
			st.set("cond", "5");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
			st.set("choice", "3");
			st.unset("talk1");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_14ab.htm"))
		{
			st.set("cond", "7");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_14b.htm"))
		{
			st.set("cond", "10");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_12c.htm"))
		{
			if(st.getInt("talk") == 0)
				st.set("talk", "1");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_15b.htm"))
		{
			if(guardianSpawn == null || !st.getPlayer().knowsObject(guardianSpawn.getLastSpawn()) || !guardianSpawn.getLastSpawn().isVisible())
			{
				try
				{
					L2NpcTemplate guardian = NpcTable.getTemplate(GUARDIAN);
					guardianSpawn = new L2Spawn(guardian);
					guardianSpawn.setAmount(1);
					guardianSpawn.setLoc(new Location(96977, -110625, -3280));
					guardianSpawn.stopRespawn();
					guardianSpawn.spawnOne();
					Functions.npcSay(guardianSpawn.getLastSpawn(), Say2C.ALL, "You, " + st.getPlayer().getName() + ", you attacked Wendy. Prepare to die!");
					cancelQuestTimer("Guardian_Fail", null, null);
					startQuestTimer("Guardian_Fail", 900000, null, null, true);
					guardianSpawn.getLastSpawn().addDamageHate(st.getPlayer(), 0, 99999);
					guardianSpawn.getLastSpawn().getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, st.getPlayer(), null);
				}
				catch(ClassNotFoundException e)
				{
					System.out.println(getName() + ": Cannot spawn Guardian!");
					e.printStackTrace();
				}
			}
			else
				htmltext = "chaos_secretary_wendy_q0114_17b.htm";
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_20b.htm"))
		{
			st.set("cond", "12");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_17c.htm"))
			st.set("talk", "2");
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_20c.htm"))
		{
			st.set("cond", "13");
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_23c.htm"))
		{
			st.set("cond", "15");
			st.playSound(SOUND_MIDDLE);
			st.takeItems(STARSTONE, 1);
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_16a.htm"))
			st.set("talk", "2");
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_20a.htm"))
		{
			if(st.getInt("cond") == 7)
			{
				st.set("cond", "8");
				st.set("talk", "0");
				st.playSound(SOUND_MIDDLE);
			}
			else if(st.getInt("cond") == 8)
			{
				st.set("cond", "9");
				st.playSound(SOUND_MIDDLE);
				htmltext = "chaos_secretary_wendy_q0114_21a.htm";
			}
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_21a.htm"))
		{
			st.set("cond", "9");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chaos_secretary_wendy_q0114_29c.htm"))
		{
			st.giveItems(STARSTONE2, 1);
			st.takeItems(57, 3000);
			st.set("cond", "26");
			st.playSound(SOUND_MIDDLE);
		}
		else if(event.equalsIgnoreCase("chaos_box2_q0114_01r.htm"))
		{
			st.playSound(SOUND_ARMOR_WOOD_3);
			st.set("talk", "1");
		}
		else if(event.equalsIgnoreCase("chaos_box2_q0114_03.htm"))
		{
			st.set("cond", "14");
			st.giveItems(STARSTONE, 1);
			st.playSound(SOUND_MIDDLE);
			st.set("talk", "0");
		}
		return htmltext;
	}

	@Override
	public String onFirstTalk(L2NpcInstance npc, L2Player player)
	{
		QuestState st = player.getQuestState(getName());
		if(st == null)
			return "";
		if(st.isCompleted())
			return "completed";

		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(npcId == STONES && cond == 17)
		{
			st.playSound(SOUND_MIDDLE);
			st.takeItems(DETECTOR, 1);
			st.giveItems(DETECTOR2, 1);
			st.set("cond", "18");
			player.sendPacket(new ExShowScreenMessage("The radio signal detector is responding. A suspicious pile of stones catches your eye.", 4500, ScreenMessageAlign.TOP_CENTER));
		}
		return "";
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		int talk = st.getInt("talk");
		int talk1 = st.getInt("talk1");
		if(npcId == YUMI)
		{
			if(st.isCreated())
			{
				QuestState Pavel = st.getPlayer().getQuestState("_121_PavelTheGiants");
				if(Pavel == null)
					return "collecter_yumi_q0114_01.htm";
				if(st.getPlayer().getLevel() >= 49 && Pavel.isCompleted())
					htmltext = "collecter_yumi_q0114_02.htm";
				else
				{
					htmltext = "collecter_yumi_q0114_01.htm";
					st.exitCurrentQuest(true);
				}
			}
			else if(cond == 1)
			{
				if(talk == 0)
					htmltext = "collecter_yumi_q0114_04.htm";
				else
					htmltext = "collecter_yumi_q0114_08.htm";
			}
			else if(cond == 2)
				htmltext = "collecter_yumi_q0114_10.htm";
			else if(cond == 3 || cond == 4 || cond == 5)
			{
				if(talk == 0)
					htmltext = "collecter_yumi_q0114_11.htm";
				else if(talk == 1)
					htmltext = "collecter_yumi_q0114_15.htm";
				else
					htmltext = "collecter_yumi_q0114_23.htm";
			}
			else if(cond == 6)
				htmltext = "collecter_yumi_q0114_27.htm";
			else if(cond == 9 || cond == 12 || cond == 16)
				htmltext = "collecter_yumi_q0114_28.htm";
			else if(cond == 17)
				htmltext = "collecter_yumi_q0114_32.htm";
			else if(cond == 19)
			{
				if(talk == 0)
					htmltext = "collecter_yumi_q0114_33.htm";
				else
					htmltext = "collecter_yumi_q0114_34.htm";
			}
			else if(cond == 20)
				htmltext = "collecter_yumi_q0114_39.htm";
			else if(cond == 21)
				htmltext = "collecter_yumi_q0114_40z.htm";
			else if(cond == 22 || cond == 26)
			{
				htmltext = "collecter_yumi_q0114_41.htm";
				st.set("cond", "27");
				st.playSound(SOUND_MIDDLE);
			}
			else if(cond == 27)
				htmltext = "collecter_yumi_q0114_42.htm";
		}
		else if(npcId == WENDY)
		{
			if(cond == 2)
			{
				if(talk + talk1 < 2)
					htmltext = "chaos_secretary_wendy_q0114_01.htm";
				else if(talk + talk1 == 2)
					htmltext = "chaos_secretary_wendy_q0114_05.htm";
			}
			else if(cond == 3)
				htmltext = "chaos_secretary_wendy_q0114_06b.htm";
			else if(cond == 4 || cond == 5)
				htmltext = "chaos_secretary_wendy_q0114_08.htm";
			else if(cond == 6)
			{
				int choice = st.getInt("choice");
				if(choice == 1)
				{
					if(talk == 0)
						htmltext = "chaos_secretary_wendy_q0114_11a.htm";
					else if(talk == 1)
						htmltext = "chaos_secretary_wendy_q0114_17c.htm";
					else
						htmltext = "chaos_secretary_wendy_q0114_16a.htm";
				}
				else if(choice == 2)
					htmltext = "chaos_secretary_wendy_q0114_11b.htm";
				else if(choice == 3)
					if(talk == 0)
						htmltext = "chaos_secretary_wendy_q0114_11c.htm";
					else if(talk == 1)
						htmltext = "chaos_secretary_wendy_q0114_12c.htm";
					else
						htmltext = "chaos_secretary_wendy_q0114_17c.htm";
			}
			else if(cond == 7)
			{
				if(talk == 0)
					htmltext = "chaos_secretary_wendy_q0114_11c.htm";
				else if(talk == 1)
					htmltext = "chaos_secretary_wendy_q0114_12c.htm";
				else
					htmltext = "chaos_secretary_wendy_q0114_17c.htm";
			}
			else if(cond == 8)
				htmltext = "chaos_secretary_wendy_q0114_16a.htm";
			else if(cond == 9)
				htmltext = "chaos_secretary_wendy_q0114_25c.htm";
			else if(cond == 10)
				htmltext = "chaos_secretary_wendy_q0114_18b.htm";
			else if(cond == 11)
				htmltext = "chaos_secretary_wendy_q0114_19b.htm";
			else if(cond == 12)
				htmltext = "chaos_secretary_wendy_q0114_25c.htm";
			else if(cond == 13)
				htmltext = "chaos_secretary_wendy_q0114_20c.htm";
			else if(cond == 14)
				htmltext = "chaos_secretary_wendy_q0114_22c.htm";
			else if(cond == 15)
			{
				htmltext = "chaos_secretary_wendy_q0114_24c.htm";
				st.set("cond", "16");
				st.playSound(SOUND_MIDDLE);
			}
			else if(cond == 16)
				htmltext = "chaos_secretary_wendy_q0114_25c.htm";
			else if(cond == 20)
				htmltext = "chaos_secretary_wendy_q0114_26c.htm";
			else if(cond == 26)
				htmltext = "chaos_secretary_wendy_q0114_32c.htm";
		}
		else if(npcId == BOX)
		{
			if(cond == 13)
			{
				if(talk == 0)
					htmltext = "chaos_box2_q0114_01.htm";
				else
					htmltext = "chaos_box2_q0114_02.htm";
			}
			else if(cond == 14)
				htmltext = "chaos_box2_q0114_04.htm";
		}
		else if(npcId == STONES)
		{
			if(cond == 18)
				htmltext = "pavel_atlanta_q0114_02.htm";
			else if(cond == 19)
				htmltext = "pavel_atlanta_q0114_03.htm";
			else if(cond == 27)
				htmltext = "pavel_atlanta_q0114_04.htm";
		}
		else if(npcId == NEWYEAR)
			if(cond == 21)
				htmltext = "head_blacksmith_newyear_q0114_01.htm";
			else if(cond == 22)
				htmltext = "head_blacksmith_newyear_q0114_03.htm";
		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		if(!st.isStarted())
			return;

		int npcId = npc.getNpcId();
		if(st.getInt("cond") == 10)
			if(npcId == GUARDIAN)
			{
				Functions.npcSay(npc, Say2C.ALL, "This enemy is far too powerful for me to fight. I must withdraw");
				st.set("cond", "11");
				st.playSound(SOUND_MIDDLE);
				st.setState(STARTED);
			}
	}

	@Override
	public String onEvent(String event, L2NpcInstance npc, L2Player player)
	{
		if(event.equalsIgnoreCase("Guardian_Fail"))
		{
			_log.info(getName() + ": Quest Timer Guardian_Fail occured.");
			if(guardianSpawn.getLastSpawn() != null && !guardianSpawn.getLastSpawn().isDead())
			{
				_log.info(getName() + ": Quest Timer Guardian_Fail, despawning Guardian.");
				guardianSpawn.stopRespawn();
				guardianSpawn.despawnAll();
				guardianSpawn = null;
				cancelQuestTimer("Guardian_Fail", null, null);
			}
			guardianSpawn = null;
			cancelQuestTimer("Guardian_Fail", null, null);
		}

		return null;
	}

}