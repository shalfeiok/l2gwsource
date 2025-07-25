package npc.model;

import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.templates.L2NpcTemplate;

/**
 * @author rage
 * @date 04.02.11 16:43
 */
public class AsamaInstance extends L2NpcInstance
{
	public AsamaInstance(int objectId, L2NpcTemplate template, long bossIndex, long p1, long p2, long p3)
	{
		super(objectId, template, bossIndex, p1, p2, p3);
	}

	@Override
	public void showChatWindow(L2Player player, int val)
	{
		if(val == 0 && player.isQuestComplete(111))
		{
			super.showChatWindow(player, 3);
			return;
		}

		super.showChatWindow(player, val);
	}
}
