package ai;

import ai.base.AiASeedNormalMonster;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.gameserver.model.L2Skill;
import ru.l2gw.gameserver.tables.SkillTable;
import ru.l2gw.commons.math.Rnd;

public class Groykhan extends AiASeedNormalMonster
{
	public L2Skill SpecialSkill01_ID = SkillTable.getInstance().getInfo(418185217);

	public Groykhan(L2Character actor)
	{
		super(actor);
		Skill01_ID = SkillTable.getInstance().getInfo(418512897);
		Skill01_Probability = 3;
		Skill01_Target_Type = 0;
		Skill02_ID = SkillTable.getInstance().getInfo(418578433);
		Skill02_Probability = 10;
		Skill02_Target_Type = 1;
		FieldCycle_ID = 4;
		FieldCycle_point = 1;
	}

	@Override
	protected void onEvtAttacked(L2Character attacker, int damage, L2Skill skill)
	{
		if(attacker == null)
			return;

		if( _thisActor.getLoc().distance3D(attacker.getLoc()) > 300 )
		{
			if( Rnd.get(100) < 5 )
			{
				addUseSkillDesire(attacker, SpecialSkill01_ID, 0, 1, 1000000);
			}
		}

		super.onEvtAttacked(attacker, damage, skill);
	}
}
