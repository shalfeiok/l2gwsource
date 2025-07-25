package quests._421_LittleWingAdventures;

import javolution.util.FastList;
import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.gameserver.controllers.ThreadPoolManager;
import ru.l2gw.gameserver.clientpackets.Say2C;
import ru.l2gw.gameserver.handler.IItemHandler;
import ru.l2gw.gameserver.handler.ItemHandler;
import ru.l2gw.gameserver.model.*;
import ru.l2gw.gameserver.model.instances.L2ItemInstance;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.instances.L2PetInstance;
import ru.l2gw.gameserver.model.quest.Quest;
import ru.l2gw.gameserver.model.quest.QuestState;
import ru.l2gw.gameserver.serverpackets.SystemMessage;
import ru.l2gw.gameserver.tables.NpcTable;
import ru.l2gw.gameserver.tables.PetDataTable;
import ru.l2gw.gameserver.templates.L2NpcTemplate;
import ru.l2gw.commons.math.Rnd;

/*
 * Author DRiN, Last Updated: 2008/04/13
 */

public class _421_LittleWingAdventures extends Quest
{
	// NPCs
	private static int Cronos = 30610;
	private static int Mimyu = 30747;
	private static int Hatchling_of_the_Wind = 12311;
	// Mobs
	private static int Fairy_Tree_of_Wind = 27185;
	private static int Fairy_Tree_of_Star = 27186;
	private static int Fairy_Tree_of_Twilight = 27187;
	private static int Fairy_Tree_of_Abyss = 27188;
	private static int Soul_of_Tree_Guardian = 27189;
	// Items
	private static short Dragonflute_of_Wind = 3500;
	private static short Dragonflute_of_Star = 3501;
	private static short Dragonflute_of_Twilight = 3502;
	private static short Dragon_Bugle_of_Wind = 4422;
	private static short Dragon_Bugle_of_Star = 4423;
	private static short Dragon_Bugle_of_Twilight = 4424;
	// Quest Items
	private static short Fairy_Leaf = 4325;

	private static int Min_Fairy_Tree_Attaks = 110;

	public _421_LittleWingAdventures()
	{
		super(421, "_421_LittleWingAdventures", "Little Wing's Big Adventures");
		addStartNpc(Cronos);
		addTalkId(Mimyu);
		addKillId(Fairy_Tree_of_Wind);
		addKillId(Fairy_Tree_of_Star);
		addKillId(Fairy_Tree_of_Twilight);
		addKillId(Fairy_Tree_of_Abyss);
		addAttackId(Fairy_Tree_of_Wind);
		addAttackId(Fairy_Tree_of_Star);
		addAttackId(Fairy_Tree_of_Twilight);
		addAttackId(Fairy_Tree_of_Abyss);
		addQuestItem(Fairy_Leaf);
	}

	private static L2ItemInstance GetDragonflute(QuestState st)
	{
		FastList<L2ItemInstance> Dragonflutes = new FastList<L2ItemInstance>();
		for(L2ItemInstance item : st.getPlayer().getInventory().getItems())
			if(item != null && (item.getItemId() == Dragonflute_of_Wind || item.getItemId() == Dragonflute_of_Star || item.getItemId() == Dragonflute_of_Twilight))
				Dragonflutes.add(item);

		if(Dragonflutes.size() == 0)
			return null;
		if(Dragonflutes.size() == 1)
			return Dragonflutes.getFirst();
		if(st.getState().equalsIgnoreCase("CREATED"))
			return null;

		int dragonflute_id = st.getInt("dragonflute");

		for(L2ItemInstance item : Dragonflutes)
			if(item.getObjectId() == dragonflute_id)
				return item;

		return null;
	}

	private static boolean HatchlingSummoned(QuestState st, boolean CheckObjID)
	{
		L2Summon _pet = st.getPlayer().getPet();
		if(!st.getPlayer().isPetSummoned())
			return false;
		if(CheckObjID)
		{
			int dragonflute_id = st.getInt("dragonflute");
			if(dragonflute_id == 0)
				return false;
			if(_pet.getControlItemObjId() != dragonflute_id)
				return false;
		}
		L2ItemInstance dragonflute = GetDragonflute(st);
		if(dragonflute == null)
			return false;
		if(PetDataTable.getControlItemId(_pet.getNpcId()) != dragonflute.getItemId())
			return false;
		return true;
	}

	private static boolean CheckTree(QuestState st, int Fairy_Tree_id)
	{
		return st.getInt(String.valueOf(Fairy_Tree_id)) == 1000000;
	}

	@Override
	public String onEvent(String event, QuestState st)
	{
		L2ItemInstance dragonflute = GetDragonflute(st);
		int dragonflute_id = st.getInt("dragonflute");
		int cond = st.getInt("cond");

		if(event.equalsIgnoreCase("30610_05.htm") && st.isCreated())
		{
			st.setState(STARTED);
			st.set("cond", "1");
			st.playSound(SOUND_ACCEPT);
		}
		else if((event.equalsIgnoreCase("30747_03.htm") || event.equalsIgnoreCase("30747_04.htm")) && st.isStarted() && cond == 1)
		{
			if(dragonflute == null)
				return "noquest";
			if(dragonflute.getObjectId() != dragonflute_id)
			{
				if(Rnd.chance(10))
				{
					st.takeItems(dragonflute.getItemId(), 1);
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
				}
				return "30747_00.htm";
			}
			if(!HatchlingSummoned(st, false))
				return event.equalsIgnoreCase("30747_04.htm") ? "30747_04a.htm" : "30747_02.htm";
			if(event.equalsIgnoreCase("30747_04.htm"))
			{
				st.set("cond", "2");
				st.takeItems(Fairy_Leaf, -1);
				st.giveItems(Fairy_Leaf, 4);
				st.playSound(SOUND_MIDDLE);
			}
		}

		return event;
	}

	@Override
	public String onTalk(L2NpcInstance npc, QuestState st)
	{
		int npcId = npc.getNpcId();
		int cond = st.getInt("cond");
		L2ItemInstance dragonflute = GetDragonflute(st);
		int dragonflute_id = st.getInt("dragonflute");

		if(st.isCreated())
		{
			if(npcId != Cronos)
				return "noquest";
			if(st.getPlayer().getLevel() < 45)
			{
				st.exitCurrentQuest(true);
				return "30610_01.htm";
			}
			if(dragonflute == null)
			{
				st.exitCurrentQuest(true);
				return "30610_02.htm";
			}
			if(dragonflute.getEnchantLevel() < 55)
			{
				st.exitCurrentQuest(true);
				return "30610_03.htm";
			}
			st.set("cond", "0");
			st.set("dragonflute", String.valueOf(dragonflute.getObjectId()));
			return "30610_04.htm";
		}

		if(!st.isStarted())
			return "noquest";

		if(npcId == Cronos)
		{
			if(dragonflute == null)
				return "30610_02.htm";
			return dragonflute.getObjectId() == dragonflute_id ? "30610_07.htm" : "30610_06.htm";
		}

		if(npcId == Mimyu)
		{
			if(st.getQuestItemsCount(Dragon_Bugle_of_Wind) + st.getQuestItemsCount(Dragon_Bugle_of_Star) + st.getQuestItemsCount(Dragon_Bugle_of_Twilight) > 0)
				return "30747_00b.htm";
			if(dragonflute == null)
				return "noquest";
			if(cond == 1)
				return "30747_01.htm";
			if(cond == 2)
			{
				if(!HatchlingSummoned(st, false))
					return "30747_09.htm";
				if(st.getQuestItemsCount(Fairy_Leaf) == 0)
				{
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "30747_11.htm";
				}
				return "30747_10.htm";
			}
			if(cond == 3)
			{
				if(dragonflute.getObjectId() != dragonflute_id)
					return "30747_00a.htm";
				if(st.getQuestItemsCount(Fairy_Leaf) > 0)
				{
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "30747_11.htm";
				}
				if(!(CheckTree(st, Fairy_Tree_of_Wind) && CheckTree(st, Fairy_Tree_of_Star) && CheckTree(st, Fairy_Tree_of_Twilight) && CheckTree(st, Fairy_Tree_of_Abyss)))
				{
					st.playSound(SOUND_FINISH);
					st.exitCurrentQuest(true);
					return "30747_11.htm";
				}
				if(st.getInt("welldone") == 0)
				{
					if(!HatchlingSummoned(st, false))
						return "30747_09.htm";
					st.set("welldone", "1");
					return "30747_12.htm";
				}
				if(HatchlingSummoned(st, false) || st.getPlayer().getPet() != null)
					return "30747_13a.htm";

				st.getPlayer().sendPacket(new SystemMessage(SystemMessage.S1_HAS_DISAPPEARED).addItemName(dragonflute.getItemId()));
				dragonflute.setItemId(Dragon_Bugle_of_Wind + dragonflute.getItemId() - Dragonflute_of_Wind);
				dragonflute.setPriceToBuy(dragonflute.getPriceToBuy()); // что бы обновить предмет в БД
				dragonflute.updateDatabase(true);
				st.getPlayer().sendPacket(new SystemMessage(SystemMessage.YOU_HAVE_EARNED__S1).addItemName(dragonflute.getItemId()));
				st.getPlayer().getInventory().sendItemList(true);

				IItemHandler handler = ItemHandler.getInstance().getItemHandler(dragonflute.getItemId());
				if(handler != null)
				{
					handler.useItem(st.getPlayer(), dragonflute);
					L2Summon pet = st.getPlayer().getPet();
					if(st.getPlayer().isPetSummoned() && pet instanceof L2PetInstance)
					{
						L2PetInstance _pet = (L2PetInstance) pet;
						for(L2ItemInstance _item : _pet.getInventory().getItems())
							if(_item.isEquipped())
								_pet.getInventory().unEquipItem(_item);
						_pet.setExp(_pet.getExpForThisLevel());
						_pet.setCurrentHp(_pet.getMaxHp());
						_pet.setCurrentMp(_pet.getMaxMp());
						_pet.setCurrentFed(_pet.getMaxMeal());
						_pet.store(st.getPlayer().getObjectId());
						_pet.sendChanges();
					}
				}

				st.playSound(SOUND_FINISH);
				st.exitCurrentQuest(true);
				return "30747_13.htm";
			}
		}

		return "noquest";
	}

	/*
	 * благодаря ai.Quest421FairyTree вызовется только при атаке от L2PetInstance
	 */
	@Override
	public String onAttack(L2NpcInstance npc, QuestState st, L2Skill skill)
	{
		if(!st.isStarted() || st.getInt("cond") != 2 || !HatchlingSummoned(st, true) || st.getQuestItemsCount(Fairy_Leaf) == 0)
			return null;

		String npcID = String.valueOf(npc.getNpcId());
		Integer attaked_times = st.getInt(npcID);
		if(CheckTree(st, npc.getNpcId()))
			return null;
		if(attaked_times > Min_Fairy_Tree_Attaks && Rnd.chance(attaked_times - Min_Fairy_Tree_Attaks))
		{
			st.set(npcID, "1000000");
			Functions.npcSay(npc, Say2C.ALL, "Give me the leaf!");
			st.takeItems(Fairy_Leaf, 1);
			if(CheckTree(st, Fairy_Tree_of_Wind) && CheckTree(st, Fairy_Tree_of_Star) && CheckTree(st, Fairy_Tree_of_Twilight) && CheckTree(st, Fairy_Tree_of_Abyss))
			{
				st.set("cond", "3");
				st.playSound(SOUND_MIDDLE);
			}
			else
				st.playSound(SOUND_ITEMGET);
		}
		else
			st.set(npcID, String.valueOf(attaked_times + 1));
		return null;
	}

	@Override
	public void onKill(L2NpcInstance npc, QuestState st)
	{
		if(!st.isStarted() || st.getInt("cond") != 2 || !HatchlingSummoned(st, true) || st.getQuestItemsCount(Fairy_Leaf) == 0 || CheckTree(st, npc.getNpcId()))
			return;
		st.set("cond", "3");
		ThreadPoolManager.getInstance().scheduleGeneral(new GuardiansSpawner(npc, st, Rnd.get(15, 20)), 1000);
	}

	public class GuardiansSpawner implements Runnable
	{
		private L2Spawn _spawn = null;
		private final L2Territory guard_spawn_loc = new L2Territory("guard_spawn_loc");
		private String agressor;
		private String agressors_pet = null;
		private FastList<String> agressors_party = null;
		private int tiks = 0;

		public GuardiansSpawner(L2NpcInstance npc, QuestState st, int _count)
		{
			L2NpcTemplate template = NpcTable.getTemplate(Soul_of_Tree_Guardian);
			if(template == null)
				return;
			try
			{
				_spawn = new L2Spawn(template);
			}
			catch(Exception E)
			{
				return;
			}
			int x0 = npc.getX() - 100;
			int x1 = npc.getX() + 100;
			int y0 = npc.getY() - 100;
			int y1 = npc.getY() + 100;
			int zmin = npc.getZ() - 32;
			int zmax = npc.getZ() + 32;
			guard_spawn_loc.add(x0, y0, zmin, zmax);
			guard_spawn_loc.add(x1, y0, zmin, zmax);
			guard_spawn_loc.add(x1, y1, zmin, zmax);
			guard_spawn_loc.add(x0, y1, zmin, zmax);
			for(int i = 0; i < _count; i++)
			{
				int[] _loc = guard_spawn_loc.getRandomPoint(false);
				if(_loc == null)
					continue;
				_spawn.setLocx(_loc[0]);
				_spawn.setLocy(_loc[1]);
				_spawn.setLocz(_loc[2]);
				_spawn.setHeading(Rnd.get(0, 0xFFFF));
				_spawn.doSpawn(true);

				agressor = st.getPlayer().getName();
				if(st.getPlayer().getPet() != null)
					agressors_pet = st.getPlayer().getPet().getName();
				if(st.getPlayer().getParty() != null)
				{
					agressors_party = new FastList<String>();
					for(L2Player _member : st.getPlayer().getParty().getPartyMembers())
						if(!_member.equals(st.getPlayer()))
							agressors_party.add(_member.getName());
				}
			}
			_spawn.stopRespawn();
			updateAgression();
		}

		private void AddAgression(L2Playable _player, int aggro)
		{
			if(_player == null)
				return;
			for(L2NpcInstance _mob : _spawn.getAllSpawned())
			{
				_mob.addDamageHate(_player, 0, aggro);
				_mob.getAI().checkAggression(_player);
			}
		}

		private void updateAgression()
		{
			L2Player _player = L2ObjectsStorage.getPlayer(agressor);
			if(_player != null)
			{
				if(agressors_pet != null && _player.isPetSummoned() && agressors_pet.equalsIgnoreCase(_player.getPet().getName()))
					AddAgression(_player.getPet(), 10);
				AddAgression(_player, 2);
			}
			if(agressors_party != null)
				for(String _agressor : agressors_party)
					AddAgression(L2ObjectsStorage.getPlayer(_agressor), 1);
		}

		public void run()
		{
			if(_spawn == null)
				return;
			tiks++;
			if(tiks < 600)
			{
				updateAgression();
				ThreadPoolManager.getInstance().scheduleGeneral(this, 1000);
				return;
			}
			_spawn.despawnAll();
		}
	}
}