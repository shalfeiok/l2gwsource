package quests.Instances;

import ru.l2gw.gameserver.instancemanager.InstanceManager;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.entity.instance.Instance;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.model.quest.Quest;

/**
 * @author admin
 * @date 27.07.2009 17:36:09
 */
public class Kamaloka_23 extends Quest
{
	private static int BOSS_ID = 18554;

	public Kamaloka_23()
	{
		super(22023, "Kamaloka_23", "Kamaloka level 23", true);
		addKillId(BOSS_ID);
	}

	@Override
	public void onKill(L2NpcInstance npc, L2Player player)
	{
		if(npc.getNpcId() == BOSS_ID)
		{
			Instance inst = InstanceManager.getInstance().getInstanceByPlayer(player);
			if(inst == null)
			{
				_log.warn(this + " onKill keller has no instance! " + player + " reflection: " + player.getReflection());
				return;
			}

			inst.successEnd();
		}
	}
}
