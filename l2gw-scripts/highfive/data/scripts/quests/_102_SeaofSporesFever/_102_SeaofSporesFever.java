package quests._102_SeaofSporesFever;

import ru.l2gw.gameserver.model.base.Race;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;
import ru.l2gw.gameserver.serverpackets.ExShowScreenMessage;
import ru.l2gw.gameserver.serverpackets.ExShowScreenMessage.ScreenMessageAlign;

public class _102_SeaofSporesFever extends Quest
{
	int ALBERRYUS_LETTER = 964;
	int EVERGREEN_AMULET = 965;
	int DRYAD_TEARS = 966;
	int COBS_MEDICINE1 = 1130;
	int COBS_MEDICINE2 = 1131;
	int COBS_MEDICINE3 = 1132;
	int COBS_MEDICINE4 = 1133;
	int COBS_MEDICINE5 = 1134;
	int SWORD_OF_SENTINEL = 743;
	int STAFF_OF_SENTINEL = 744;
	int ALBERRYUS_LIST = 746;

	public _102_SeaofSporesFever()
	{
		super(102, "_102_SeaofSporesFever", "Sea of Spores Fever");

		addStartNpc(30284);

		addTalkId(30156);
		addTalkId(30217);
		addTalkId(30219);
		addTalkId(30221);
		addTalkId(30284);
		addTalkId(30285);

		addKillId(20013);
		addKillId(20019);

		addQuestItem(ALBERRYUS_LETTER, EVERGREEN_AMULET, DRYAD_TEARS, COBS_MEDICINE1, COBS_MEDICINE2, COBS_MEDICINE3, COBS_MEDICINE4, COBS_MEDICINE5, ALBERRYUS_LIST);
	}

	private void check(QuestState st)
	{
		if(st.getQuestItemsCount(COBS_MEDICINE2) == 0 && st.getQuestItemsCount(COBS_MEDICINE3) == 0 && st.getQuestItemsCount(COBS_MEDICINE4) == 0 && st.getQuestItemsCount(COBS_MEDICINE5) == 0)
			st.set("cond", "6");
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		String htmltext = event;
		if(event.equalsIgnoreCase("alberryus_q0102_02.htm"))
		{
			st.set("cond", "1");
			st.setState(STARTED);
			st.giveItems(ALBERRYUS_LETTER, 1);
			st.playSound(SOUND_ACCEPT);
		}
		return htmltext;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		if(st.isCompleted())
			return "completed";
		String htmltext = "noquest";
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		if(npcId == 30284)
		{
			if(st.isCreated())
			{
				if(st.getPlayer().getRace() != Race.elf)
				{
					htmltext = "alberryus_q0102_00.htm";
					st.exitCurrentQuest(true);
				}
				else if(st.getPlayer().getLevel() >= 12)
				{
					htmltext = "alberryus_q0102_07.htm";
					return htmltext;
				}
				else
				{
					htmltext = "alberryus_q0102_08.htm";
					st.exitCurrentQuest(true);
				}

			}
			else if(cond == 1 && st.getQuestItemsCount(ALBERRYUS_LETTER) == 1)
				htmltext = "alberryus_q0102_03.htm";
			else if(cond == 2 && st.getQuestItemsCount(EVERGREEN_AMULET) == 1)
				htmltext = "alberryus_q0102_09.htm";
			else if(cond == 4 && st.getQuestItemsCount(COBS_MEDICINE1) == 1)
			{
				st.set("cond", "5");
				st.takeItems(COBS_MEDICINE1, 1);
				st.giveItems(ALBERRYUS_LIST, 1);
				htmltext = "alberryus_q0102_04.htm";
			}
			else if(cond == 5)
				htmltext = "alberryus_q0102_05.htm";
			else if(cond == 6 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1)
			{
				st.takeItems(ALBERRYUS_LIST, 1);
				st.rollAndGive(57, 6331, 100);
				st.addExpAndSp(30202, 1339);

				if(st.getPlayer().getClassId().isMage())
					st.giveItems(STAFF_OF_SENTINEL, 1);
				else
					st.giveItems(SWORD_OF_SENTINEL, 1);

				if(st.getPlayer().getClassId().getLevel() == 1 && !st.getPlayer().getVarB("p1q3"))
				{
					st.getPlayer().setVar("p1q3", "1"); // flag for helper
					st.getPlayer().sendPacket(new ExShowScreenMessage("Now go find the Newbie Guide.", 5000, ScreenMessageAlign.TOP_CENTER, true));
					st.giveItems(1060, 100); // healing potion
					for(int item = 4412; item <= 4417; item++)
						st.giveItems(item, 10); // echo cry
					if(st.getPlayer().getClassId().isMage())
					{
						st.playTutorialVoice("tutorial_voice_027");
						st.giveItems(5790, 3000); // newbie sps
					}
					else
					{
						st.playTutorialVoice("tutorial_voice_026");
						st.giveItems(5789, 6000); // newbie ss
					}
				}

				htmltext = "alberryus_q0102_06.htm";
				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(false);
			}
		}
		else if(npcId == 30156)
		{
			if(cond == 1 && st.getQuestItemsCount(ALBERRYUS_LETTER) == 1)
			{
				st.takeItems(ALBERRYUS_LETTER, 1);
				st.giveItems(EVERGREEN_AMULET, 1);
				st.set("cond", "2");
				htmltext = "cob_q0102_03.htm";
			}
			else if(cond == 2 && st.getQuestItemsCount(EVERGREEN_AMULET) > 0 && st.getQuestItemsCount(DRYAD_TEARS) < 10)
				htmltext = "cob_q0102_04.htm";
			else if(cond > 3 && st.getQuestItemsCount(ALBERRYUS_LIST) > 0)
				htmltext = "cob_q0102_07.htm";
			else if(cond == 3 && st.getQuestItemsCount(EVERGREEN_AMULET) > 0 && st.getQuestItemsCount(DRYAD_TEARS) >= 10)
			{
				st.takeItems(EVERGREEN_AMULET, 1);
				st.takeItems(DRYAD_TEARS, -1);
				st.giveItems(COBS_MEDICINE1, 1);
				st.giveItems(COBS_MEDICINE2, 1);
				st.giveItems(COBS_MEDICINE3, 1);
				st.giveItems(COBS_MEDICINE4, 1);
				st.giveItems(COBS_MEDICINE5, 1);
				st.set("cond", "4");
				htmltext = "cob_q0102_05.htm";
			}
			else if(cond == 4)
				htmltext = "cob_q0102_06.htm";
		}
		else if(npcId == 30217 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1 && st.getQuestItemsCount(COBS_MEDICINE2) == 1)
		{
			st.takeItems(COBS_MEDICINE2, 1);
			htmltext = "sentinel_berryos_q0102_01.htm";
			check(st);
		}
		else if(npcId == 30219 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1 && st.getQuestItemsCount(COBS_MEDICINE3) == 1)
		{
			st.takeItems(COBS_MEDICINE3, 1);
			htmltext = "sentinel_veltress_q0102_01.htm";
			check(st);
		}
		else if(npcId == 30221 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1 && st.getQuestItemsCount(COBS_MEDICINE4) == 1)
		{
			st.takeItems(COBS_MEDICINE4, 1);
			htmltext = "sentinel_rayjien_q0102_01.htm";
			check(st);
		}
		else if(npcId == 30285 && cond == 5 && st.getQuestItemsCount(ALBERRYUS_LIST) == 1 && st.getQuestItemsCount(COBS_MEDICINE5) == 1)
		{
			st.takeItems(COBS_MEDICINE5, 1);
			htmltext = "sentinel_gartrandell_q0102_01.htm";
			check(st);
		}
		return htmltext;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		if((npcId == 20013 || npcId == 20019))
			if(st.getQuestItemsCount(EVERGREEN_AMULET) > 0 && st.rollAndGiveLimited(DRYAD_TEARS, 1, 33, 10))
			{
				if(st.getQuestItemsCount(DRYAD_TEARS) == 10)
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
