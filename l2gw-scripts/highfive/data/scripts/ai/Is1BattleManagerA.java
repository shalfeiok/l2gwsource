package ai;

import ai.base.DefaultNpc;
import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.gameserver.instancemanager.InstanceManager;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.gameserver.model.entity.instance.Instance;
import ru.l2gw.gameserver.model.npcmaker.DefaultMaker;

/**
 * @author: rage
 * @date: 13.12.11 22:06
 */
public class Is1BattleManagerA extends DefaultNpc
{
	public int script_event_count = 0;
	public String my_maker1 = "rumwarsha13_1424_0101";
	public String my_maker2 = "rumwarsha13_1424_0201";
	public String my_maker3 = "rumwarsha13_1424_0301";
	public String my_maker4 = "rumwarsha13_1424_0401";
	public String my_maker5 = "rumwarsha13_1424_0501";
	public String my_maker6 = "rumwarsha13_1424_0601";
	public int start_time = 0;
	public int now_time = 0;
	public int loc_x = -173703;
	public int loc_y = 218097;
	public int loc_z = -9528;

	public Is1BattleManagerA(L2Character actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		_thisActor.i_ai0 = 0;
		_thisActor.i_ai1 = 0;
		_thisActor.i_ai2 = 0;
		_thisActor.i_ai0 = (int) (System.currentTimeMillis() / 1000);
		addTimer(1001, 1000);
		DefaultMaker maker0 = InstanceManager.getInstance().getNpcMaker(_thisActor.getReflection(), my_maker1);
		if( maker0 != null )
		{
			maker0.onScriptEvent(1001, 0, 0);
		}
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if( timerId == 1001 )
		{
			int i0 = (int) (System.currentTimeMillis() / 1000);
			_thisActor.i_ai1 = ( i0 - _thisActor.i_ai0 );
			addTimer(1001, 1000);
		}
	}

	@Override
	protected void onEvtSeeCreature(L2Character creature)
	{
		if( creature.isPlayer() )
		{
			int i0 = 0, i1 = 0;
			if( _thisActor.i_ai1 >= 60 )
			{
				i0 = (_thisActor.i_ai1 / 60);
				i1 = ( _thisActor.i_ai1 - i0 * 60 );
			}
			else if( _thisActor.i_ai1 < 60 )
			{
				i0 = 0;
				i1 = _thisActor.i_ai1;
			}

			Functions.sendUIEventFStr(creature, 0, 0, 0, "1", String.valueOf(i0), String.valueOf(i1), "60", "0", 1911119);
		}
	}

	@Override
	protected void onEvtScriptEvent(int eventId, Object arg1, Object arg2)
	{
		if( eventId == 989809 )
		{
			switch((Integer) arg1)
			{
				case 1:
					DefaultMaker maker0 = InstanceManager.getInstance().getNpcMaker(_thisActor.getReflection(), my_maker2);
					if( maker0 != null )
					{
						maker0.onScriptEvent(1001, getStoredIdFromCreature(_thisActor), 0);
					}
					break;
				case 2:
					maker0 = InstanceManager.getInstance().getNpcMaker(_thisActor.getReflection(), my_maker3);
					if( maker0 != null )
					{
						maker0.onScriptEvent(1001, getStoredIdFromCreature(_thisActor), 0);
					}
					break;
				case 3:
					maker0 = InstanceManager.getInstance().getNpcMaker(_thisActor.getReflection(), my_maker4);
					if( maker0 != null )
					{
						maker0.onScriptEvent(1001, getStoredIdFromCreature(_thisActor), 0);
					}
					break;
				case 4:
					maker0 = InstanceManager.getInstance().getNpcMaker(_thisActor.getReflection(), my_maker5);
					if( maker0 != null )
					{
						maker0.onScriptEvent(1001, getStoredIdFromCreature(_thisActor), 0);
					}
					break;
				case 5:
					maker0 = InstanceManager.getInstance().getNpcMaker(_thisActor.getReflection(), my_maker6);
					if( maker0 != null )
					{
						maker0.onScriptEvent(1001, getStoredIdFromCreature(_thisActor), 0);
					}
					break;
				case 3001:
					_thisActor.createOnePrivate(32530, "Is1RewardGiver", 0, 0, loc_x, loc_y, loc_z, 0, _thisActor.i_ai1, 0, 0);
					Instance inst = _thisActor.getInstanceZone();
					if(inst != null)
						inst.rescheduleEndTask(600);
					//myself.InstantZone_Finish(10);
					break;
			}
		}
	}
}