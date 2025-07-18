package ai;

import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.gameserver.clientpackets.Say2C;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.gameserver.model.npcmaker.DefaultMaker;
import ru.l2gw.gameserver.tables.SpawnTable;

/**
 * @author: rage
 * @date: 19.12.11 20:14
 */
public class NpcWarmageArtius extends Citizen
{
	public String my_maker_name = "rumwarsha02_npc1425_lc01m1";
	public int TM_LINDVIOR_SCENE = 78001;
	public int TIME_LINDVIOR_SCENE = 30;

	public NpcWarmageArtius(L2Character actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtScriptEvent(int eventId, Object arg1, Object arg2)
	{
		if(eventId == 78010057 && (Integer) arg1 == 3)
		{
			Functions.npcSay(_thisActor, Say2C.SHOUT, 1800226);
			addTimer(TM_LINDVIOR_SCENE, TIME_LINDVIOR_SCENE * 1000);
		}
		super.onEvtScriptEvent(eventId, arg1, arg2);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if(timerId == TM_LINDVIOR_SCENE)
		{
			DefaultMaker maker0 = SpawnTable.getInstance().getNpcMaker(my_maker_name);
			if(maker0 != null)
			{
				maker0.onScriptEvent(78010057, 2, 0);
			}
		}
		super.onEvtTimer(timerId, arg1, arg2);
	}
}