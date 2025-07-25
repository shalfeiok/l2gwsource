package ai;

import ai.base.Warrior;
import ru.l2gw.commons.math.Rnd;
import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.gameserver.clientpackets.Say2C;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.gameserver.model.L2Skill;
import ru.l2gw.gameserver.model.entity.category.CategoryManager;
import ru.l2gw.gameserver.tables.SkillTable;

/**
 * @author: rage
 * @date: 10.10.11 13:20
 */
public class AiSolinaLeader extends Warrior
{
	public int MoveAroundSocial = 0;
	public L2Skill Skill01_ID = SkillTable.getInstance().getInfo(413073409);
	public L2Skill Skill02_ID = SkillTable.getInstance().getInfo(413138945);

	public AiSolinaLeader(L2Character actor)
	{
		super(actor);
	}

	@Override
	protected void onEvtSpawn()
	{
		_thisActor.c_ai0 = _thisActor.getStoredId();
		_thisActor.i_ai3 = 0;
		_thisActor.i_ai4 = 0;
		super.onEvtSpawn();
	}

	@Override
	protected void onEvtNoDesire()
	{
		_thisActor.i_ai3 = 0;
	}

	@Override
	protected void onEvtSeeCreature(L2Character creature)
	{
		if(_thisActor.getLifeTime() > 7 && _thisActor.inMyTerritory(_thisActor) && _thisActor.getMostHated() == null)
		{
			if(creature.isPlayer() && creature.getActiveWeaponInstance() != null)
			{
				if(Rnd.get(100) < 3)
				{
					_thisActor.i_ai4 = 1;
					if(Skill02_ID.getMpConsume() < _thisActor.getCurrentMp() && Skill02_ID.getHpConsume() < _thisActor.getCurrentHp() && !_thisActor.isSkillDisabled(Skill02_ID.getId()))
					{
						addUseSkillDesire(creature, Skill02_ID, 0, 1, 1000000);
					}
				}
				else
				{
					addAttackDesire(creature, 1, 200);
				}
			}
		}
		super.onEvtSeeCreature(creature);
	}

	@Override
	protected void onEvtAttacked(L2Character attacker, int damage, L2Skill skill)
	{
		if(attacker == null)
			return;

		if(attacker.isPlayer() || CategoryManager.isInCategory(12, attacker.getNpcId()))
		{
			if(_thisActor.getMostHated() != null)
			{
				if(_thisActor.getMostHated() == attacker)
				{
					if(Rnd.get(100) < 3)
					{
						if(Skill01_ID.getMpConsume() < _thisActor.getCurrentMp() && Skill01_ID.getHpConsume() < _thisActor.getCurrentHp() && !_thisActor.isSkillDisabled(Skill01_ID.getId()))
						{
							addUseSkillDesire(attacker, Skill01_ID, 0, 1, 1000000);
						}
					}
				}
				if(attacker.isPlayer() || CategoryManager.isInCategory(12, attacker.getNpcId()))
				{
					if(damage == 0)
					{
						damage = 1;
					}

					int h = (int) (1.000000 * damage / (_thisActor.getLevel() + 7) * 10000);
					_thisActor.addDamageHate(attacker, 0, h);
					addAttackDesire(attacker, 1, DEFAULT_DESIRE);
				}
			}
		}

		super.onEvtAttacked(attacker, damage, skill);
	}

	@Override
	protected void onEvtFinishCasting(L2Skill skill)
	{
		if(skill == Skill02_ID)
		{
			if(_thisActor.i_ai3 == 0)
			{
				Functions.npcSay(_thisActor, Say2C.ALL, 1121006);
				_thisActor.i_ai3 = 1;
			}
		}
	}
}