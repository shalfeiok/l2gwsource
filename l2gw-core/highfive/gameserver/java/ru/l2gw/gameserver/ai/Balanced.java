package ru.l2gw.gameserver.ai;

import javolution.util.FastList;
import ru.l2gw.commons.math.Rnd;
import ru.l2gw.gameserver.model.L2Character;
import ru.l2gw.gameserver.model.L2Skill;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.l2gw.gameserver.ai.CtrlIntention.AI_INTENTION_ACTIVE;

/**
 * This class manages AI of L2NpcInstance.<BR><BR>
 */
public class Balanced extends DefaultAI
{
    protected L2Skill[] _dam_skills;

    public Balanced(L2Character actor)
    {
        super(actor);
        _dam_skills = msum(_pdam_skills, _mdam_skills);
    }

    @Override
    protected boolean thinkActive()
    {
        if(super.thinkActive())
            return true;

        // BUFF
        if(_selfbuff_skills.length > 0 && Rnd.chance(10))
        {
            L2Skill r_skill = _selfbuff_skills[Rnd.get(_selfbuff_skills.length)];
            if(_thisActor.getEffectBySkill(r_skill) == null)
            {
                addUseSkillDesire(_thisActor, r_skill, 1, 1, DEFAULT_DESIRE * 2);
                return true;
            }
        }

        return false;
    }

    @Override
    protected boolean createNewTask()
    {
        if(debug)
            _log.info("createNewTask: attackTarget: " + getAttackTarget());
        
        clearTasks();

        L2Character _temp_attack_target = getAttackTarget();
        L2Character hated = _thisActor.isConfused() ? _temp_attack_target : _thisActor.getMostHated();

        if(debug)
            _log.info("createNewTask: hated: " + hated);

        if(hated != null && hated != _thisActor)
            _temp_attack_target = hated;
        else
        {
            _thisActor.setAttackTimeout(Long.MAX_VALUE);
            setAttackTarget(null);
            clientStopMoving();
            setIntention(AI_INTENTION_ACTIVE);
            return false;
        }

        if(_useUD && _ud != null)
        {
            _useUD = false;
            addUseSkillDesire(_thisActor, _ud, 1, 1, DEFAULT_DESIRE * 100);
            return true;
        }
        
        // Базовые параметры
        int phys_per = 20;
        int debuff_per = 35;
        int dam_per = 70;
        int heal_per = 20;

        List<L2Skill> d_skill = new FastList<>();
        L2Skill r_skill = null;

        int distance = (int) _thisActor.getDistance(_temp_attack_target);
        double _def_mp = _thisActor.getCurrentMp();

        if(!Rnd.chance(phys_per))
        {
            Map<L2Skill, Integer> skill_chances = new HashMap<>();

            // DEBUFF
            if(_debuff_skills.length > 0)
            {
                L2Skill skill = getSkillByRange(_debuff_skills, distance);
                if(skill != null)
                    skill_chances.put(skill, debuff_per);
            }

            // Damage skills
            if(_dam_skills.length > 0)
            {
                L2Skill skill = getSkillByRange(_dam_skills, distance);
                if(skill != null)
                    skill_chances.put(skill, dam_per);
            }

            // Защита от деления на ноль
            double maxHp = _thisActor.getMaxHp();
            double currentHp = maxHp > 0 ? _thisActor.getCurrentHp() : 0;
            double hpRatio = maxHp > 0 ? currentHp / maxHp : 0;

            if(_cancel_skills.length > 0 && hpRatio > 0.40)
            {
                List<L2Skill> skills = getEnabledSkills(_cancel_skills);
                if(!skills.isEmpty())
                    skill_chances.put(skills.get(Rnd.get(skills.size())), 1);
            }

            // Self explosion
            if(_selfexplosion_skills.length > 0 && Rnd.chance(5))
            {
                List<L2Skill> skills = getEnabledSkills(_selfexplosion_skills);
                if(!skills.isEmpty())
                {
                    int chance = (int)(10 - hpRatio * 10);
                    if(chance > 0)
                        skill_chances.put(skills.get(Rnd.get(skills.size())), chance);
                }
            }

            // Mana burn
            if(_manaburn_skills.length > 0)
            {
                List<L2Skill> skills = getEnabledSkills(_manaburn_skills);
                if(!skills.isEmpty())
                    skill_chances.put(skills.get(Rnd.get(skills.size())), 10);
            }
            
            // HEAL
            if(_heal_skills.length > 0 && hpRatio < 0.25)
            {
                List<L2Skill> skills = getEnabledSkills(_heal_skills);
                if(!skills.isEmpty())
                    skill_chances.put(skills.get(Rnd.get(skills.size())), heal_per);
            }

            if(!skill_chances.isEmpty())
            {
                for(Map.Entry<L2Skill, Integer> entry : skill_chances.entrySet())
                    if(Rnd.chance(entry.getValue()))
                        d_skill.add(entry.getKey());

                if(!d_skill.isEmpty())
                    r_skill = getSkillByRange(d_skill.toArray(new L2Skill[0]), distance);
            }

            if(r_skill != null && !r_skill.isMuted(_thisActor) && _def_mp >= r_skill.getMpConsume())
            {
                if(r_skill.getAimingTarget(_thisActor) == _thisActor)
                    _temp_attack_target = _thisActor;
                else if(!r_skill.isOffensive())
                    _temp_attack_target = getFriendTarget(r_skill);

                if(_temp_attack_target != null)
                {
                    addUseSkillDesire(_temp_attack_target, r_skill, 1, 1, DEFAULT_DESIRE * 2);
                    return true;
                }
            }
        }

        addAttackDesire(_temp_attack_target, 1, DEFAULT_DESIRE);
        return true;
    }
}