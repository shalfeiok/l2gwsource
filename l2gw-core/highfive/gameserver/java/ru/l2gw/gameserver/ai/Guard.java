package ru.l2gw.gameserver.ai;

import ru.l2gw.gameserver.Config;
import ru.l2gw.gameserver.geodata.GeoEngine;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.L2World;
import ru.l2gw.gameserver.model.instances.L2GuardInstance;
import ru.l2gw.gameserver.model.instances.L2MonsterInstance;
import ru.l2gw.gameserver.model.instances.L2RaidBossInstance;

import static ru.l2gw.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;
import static ru.l2gw.gameserver.ai.CtrlIntention.AI_INTENTION_ATTACK;

public class Guard extends Fighter implements Runnable
{
    public Guard(L2Character actor)
    {
        super(actor);
    }

    @Override
    protected boolean randomWalk()
    {
        return false;
    }

    @Override
    protected boolean thinkActive()
    {
        if(_actor.isDead())
            return true;

        L2GuardInstance guard = (L2GuardInstance) _actor;

        if(getIntention() == AI_INTENTION_ACTIVE)
        {
            for(L2Character cha : L2World.getAroundCharacters(_actor, 600, Config.PLAYER_VISIBILITY_Z))
            {
                if(cha == null) continue;
                
                if(Config.DEBUG)
                    _log.debug(_actor + ": PK " + cha + " entered scan range");

                if(autoAttackCondition(cha))
                {
                    guard.addDamageHate(cha, 0, 1);
                    setIntention(AI_INTENTION_ATTACK, cha, null);
                    return true;
                }
            }
        }

        return super.thinkActive();
    }

    @Override
    public boolean checkAggression(L2Character target)
    {
        // Проверка базовых условий
        if(target == null || 
           target.getKarma() <= 0 || 
           _intention != CtrlIntention.AI_INTENTION_ACTIVE || 
           _globalAggro < 0)
            return false;

        // Проверка расстояния и агрессии
        boolean inAggroList = _thisActor.getAggroList().containsKey(target.getObjectId());
        boolean inRange = inAggroList || _thisActor.isInRange(target, 600);
        
        if(!inRange || 
           Math.abs(target.getZ() - _actor.getZ()) > 400 || 
           isSilent(target) || 
           !GeoEngine.canSeeTarget(_actor, target))
            return false;

        // Обработка игроков (включая GM)
        if(target.isPlayer())
        {
            L2Player player = (L2Player) target;
            if(player.isGM() && player.isInvisible())
                return false;
        }

        // Обработка питомцев и прислужников
        if((target.isSummon() || target.isPet()) && target.getPlayer() != null)
            _thisActor.addDamageHate(target.getPlayer(), 0, 1);

        _thisActor.addDamageHate(target, 0, 2);
        startRunningTask(2000);
        setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
        return true;
    }

    protected boolean autoAttackCondition(L2Character target)
    {
        // Базовые проверки
        if(target == null || 
           target.isAlikeDead() || 
           target.isInvul() || 
           !GeoEngine.canSeeTarget(_actor, target))
            return false;

        // Атаковать игроков с кармой
        if(target.getKarma() > 0)
            return true;

        // Атаковать агрессивных мобов
        if(Config.ALLOW_GUARDS && 
           target.isMonster() && 
           !(target instanceof L2RaidBossInstance) && 
           ((L2MonsterInstance) target).isAggressive())
            return true;
        
        return false;
    }
}