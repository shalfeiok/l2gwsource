package ai;

import ai.base.WarriorUseSkill;
import ru.l2gw.gameserver.ai.CtrlEvent;
import ru.l2gw.gameserver.model.L2Character;

/**
 * @author: rage
 * @date: 06.10.11 19:21
 */
public class Ssq2TombSlave1 extends WarriorUseSkill
{

	public Ssq2TombSlave1(L2Character actor)
	{
		super(actor);
		IsAggressive = 1;
		Aggressive_Time = 1.000000f;
	}

	@Override
	protected void onEvtSpawn()
	{
		super.onEvtSpawn();
		addTimer(2201, 5000);
	}

	@Override
	protected void onEvtTimer(int timerId, Object arg1, Object arg2)
	{
		if(timerId == 2201)
		{
			_thisActor.lookNeighbor(300);
			addTimer(2201, 5000);
		}
	}

	@Override
	protected void onEvtDead(L2Character killer)
	{
		if(_thisActor.getLeader() != null)
		{
			_thisActor.notifyAiEvent(_thisActor.getLeader(), CtrlEvent.EVT_SCRIPT_EVENT, 90206, 0, null);
		}
		super.onEvtDead(killer);
	}
}