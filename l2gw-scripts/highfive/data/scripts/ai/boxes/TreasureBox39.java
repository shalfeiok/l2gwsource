package ai.boxes;

import ai.base.GTreasureBox;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.commons.math.Rnd;

/**
 * @author: rage
 * @date: 19.12.11 21:02
 */
public class TreasureBox39 extends GTreasureBox
{
	public TreasureBox39(L2Character actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtDead(L2Character killer)
	{
		super.onEvtDead(killer);
		int i0 = Rnd.get(10000);
		if( i0 < 6879 )
		{
			_thisActor.dropItem(killer, 736, 7);
		}
		i0 = Rnd.get(10000);
		if( i0 < 6019 )
		{
			_thisActor.dropItem(killer, 1061, 4);
		}
		i0 = Rnd.get(10000);
		if( i0 < 9630 )
		{
			_thisActor.dropItem(killer, 737, 4);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10260, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10261, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10262, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10263, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10264, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10265, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10266, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10267, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10268, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 6019 )
		{
			_thisActor.dropItem(killer, 5593, 6);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 5594, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2889 )
		{
			_thisActor.dropItem(killer, 10269, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 8346 )
		{
			_thisActor.dropItem(killer, 10134, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 8346 )
		{
			_thisActor.dropItem(killer, 10135, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 8346 )
		{
			_thisActor.dropItem(killer, 10136, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 8346 )
		{
			_thisActor.dropItem(killer, 1538, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 3130 )
		{
			_thisActor.dropItem(killer, 3936, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 2527 )
		{
			_thisActor.dropItem(killer, 69, 1);
		}
		i0 = Rnd.get(10000);
		if( i0 < 815 )
		{
			_thisActor.dropItem(killer, 21747, 1);
		}
	}
}