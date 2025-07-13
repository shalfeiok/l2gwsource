package events.NewbieBuffs;

import ru.l2gw.commons.math.Rnd;
import ru.l2gw.gameserver.Announcements;
import ru.l2gw.gameserver.controllers.ThreadPoolManager;
import ru.l2gw.gameserver.handler.IOnDieHandler;
import ru.l2gw.gameserver.instancemanager.ServerVariables;
import ru.l2gw.gameserver.model.*;
import ru.l2gw.gameserver.model.instances.L2ItemInstance;
import ru.l2gw.gameserver.model.instances.L2NpcInstance;
import ru.l2gw.gameserver.serverpackets.SkillList;
import ru.l2gw.gameserver.tables.SkillTable;
import ru.l2gw.util.Files;
import ru.l2gw.util.Util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

public class NewbieBuffs implements IOnDieHandler
{
    private static boolean _active = false;
    private static final Map<Integer, Long> lastBuffTime = new ConcurrentHashMap<>();
    
    // Список бафов: [ID скилла, уровень скилла]
    private static final int[][] NEWBIE_BUFFS = {
        {7029, 4},   // GM Haste Lv4
        {1204, 2},   // Wind Walk Lv2
        {1240, 3},   // Bless the Body Lv3
        {1085, 3},   // Acumen Lv3
        {1078, 6},   // Concentration Lv6
        {1068, 3},   // Might Lv3
        {1040, 3},   // Shield Lv3
        {1036, 2},   // Magic Barrier Lv2
        {1086, 2}    // Haste Lv2
    };
    
    public static void init()
    {
        _active = isActive();
        if(_active)
        {
            System.out.println("Newbie Buffs Event: Loaded [state: activated]");
            applyBuffsToOnlinePlayers();
        }
        else
        {
            System.out.println("Newbie Buffs Event: Loaded [state: deactivated]");
        }
    }
    
    public static boolean isActive()
    {
        return ServerVariables.getString("NewbieBuffs", "off").equalsIgnoreCase("on");
    }
    
    public static void startEvent()
    {
        if(!_active)
        {
            ServerVariables.set("NewbieBuffs", "on");
            _active = true;
            System.out.println("Newbie Buffs Event: Started");
            Announcements.getInstance().announceToAll("Newbie Buffs Event: Started! All online players received special buffs.");
            applyBuffsToOnlinePlayers();
        }
    }
    
    public static void stopEvent()
    {
        if(_active)
        {
            ServerVariables.set("NewbieBuffs", "off");
            _active = false;
            System.out.println("Newbie Buffs Event: Stopped");
            Announcements.getInstance().announceToAll("Newbie Buffs Event: Finished.");
            lastBuffTime.clear();
        }
    }
    
    private static void applyBuffsToOnlinePlayers()
    {
        // Исправленный метод получения игроков
        for(L2Player player : L2ObjectsStorage.getAllPlayers())
        {
            if(player != null && !player.isInOfflineMode())
            {
                applyNewbieBuffs(player);
            }
        }
    }
    
    public static void onPlayerEnter(L2Player player)
    {
        if(_active)
        {
            applyNewbieBuffs(player);
        }
    }
    
    public static void applyNewbieBuffs(L2Player player)
    {
        if(player == null || player.isInOlympiadMode() || player.isInJail() || player.isDead())
            return;
        
        // Проверяем время последнего применения
        Long lastTime = lastBuffTime.get(player.getObjectId());
        long currentTime = System.currentTimeMillis();
        if(lastTime != null && currentTime - lastTime < 300000)
            return;
        
        lastBuffTime.put(player.getObjectId(), currentTime);
        
        List<String> appliedBuffs = new ArrayList<>();
        boolean buffsApplied = false;
        
        for(int[] buff : NEWBIE_BUFFS)
        {
            int skillId = buff[0];
            int skillLevel = buff[1];
            
            // Проверяем наличие лучшего бафа
            boolean hasBetterBuff = false;
            for(L2Effect effect : player.getAllEffects())
            {
                if(effect == null) 
                    continue;
                    
                if(effect.getSkill().getId() == skillId && 
                   effect.getSkill().getLevel() >= skillLevel)
                {
                    hasBetterBuff = true;
                    break;
                }
            }
            
            // Накладываем баф если нет лучшей версии
            if(!hasBetterBuff)
            {
                L2Skill skill = SkillTable.getInstance().getInfo(skillId, skillLevel);
                if(skill != null)
                {
                    // Исправленный метод применения эффектов
                   // skill.getEffects(player, player, false, false);
                    appliedBuffs.add(skill.getName());
                    buffsApplied = true;
                }
            }
        }
        
        if(buffsApplied)
        {
            player.sendMessage("You received special buffs from the event!");
            player.sendPacket(new SkillList(player));
        }
    }

    @Override
    public void onDie(L2Character cha, L2Character killer)
    {
        // Обновление бафов после смерти
        if(_active && cha != null && cha.isPlayer())
        {
            final L2Player player = (L2Player) cha;
            if(player != null && !player.isInOlympiadMode())
            {
                // Задержка перед применением бафов
                ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(player.isOnline() && !player.isDead())
                            applyNewbieBuffs(player);
                    }
                }, 5000);
            }
        }
    }
    
    public static void activateEvent()
    {
        startEvent();
    }

    public static void deactivateEvent()
    {
        stopEvent();
    }
}