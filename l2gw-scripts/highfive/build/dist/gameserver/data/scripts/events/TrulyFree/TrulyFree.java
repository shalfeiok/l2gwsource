package events.TrulyFree;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import ru.l2gw.commons.arrays.GArray;
import ru.l2gw.commons.utils.DbUtils;
import ru.l2gw.commons.utils.XmlUtil;
import ru.l2gw.database.DatabaseFactory;
import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.extensions.scripts.ScriptFile;
import ru.l2gw.gameserver.cache.Msg;
import ru.l2gw.gameserver.instancemanager.PremiumItemManager;
import ru.l2gw.gameserver.instancemanager.ServerVariables;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.gmaccess.AdminTemplateManager;
import ru.l2gw.gameserver.templates.StatsSet;
import ru.l2gw.util.Files;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: rage
 * @date: 22.01.13 12:24
 */
public class TrulyFree extends Functions implements ScriptFile
{
    private static boolean active;
    private static final Map<Integer, Reward> rewards = new HashMap<>();

    @Override
    public void onLoad()
    {
        // Создаем таблицу, если она не существует
        createTableIfNotExists();
        
        active = ServerVariables.getBool("truly_free", false);
        _log.info("Loaded Event: Truly Free [state: " + (active ? "activated]" : "deactivated]"));

        if(active)
        {
            cleanupOrphanedRecords();
        }

        loadConfig();
    }

    private void createTableIfNotExists()
    {
        Connection con = null;
        Statement stmt = null;
        try
        {
            con = DatabaseFactory.getInstance().getConnection();
            stmt = con.createStatement();
            stmt.executeUpdate(
                "CREATE TABLE IF NOT EXISTS event_truly_free (" +
                "object_id INT NOT NULL, " +
                "level INT NOT NULL, " +
                "PRIMARY KEY (object_id, level)" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8"
            );
            _log.info("TrulyFree: event_truly_free table checked/created");
        }
        catch(Exception e)
        {
            _log.error("TrulyFree: can't create table: " + e, e);
        }
        finally
        {
            DbUtils.closeQuietly(con, stmt);
        }
    }

    private void cleanupOrphanedRecords()
    {
        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            con = DatabaseFactory.getInstance().getConnection();
            stmt = con.prepareStatement(
                "DELETE FROM event_truly_free WHERE object_id NOT IN (SELECT obj_id FROM characters)"
            );
            int deleted = stmt.executeUpdate();
            if(deleted > 0)
            {
                _log.info("TrulyFree: deleted " + deleted + " orphaned records");
            }
        }
        catch(Exception e)
        {
            _log.error("TrulyFree: can't clean event table: " + e, e);
        }
        finally
        {
            DbUtils.closeQuietly(con, stmt);
        }
    }

    private void loadConfig()
    {
        try
        {
            File file = new File("data/scripts/events/TrulyFree/trulyfree.xml");
            if(!file.exists())
            {
                _log.error("TrulyFree: config file trulyfree.xml is missing!");
                active = false;
                return;
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringComments(true);

            Document doc = factory.newDocumentBuilder().parse(file);
            rewards.clear();

            for(Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
            {
                if("trulyfree".equalsIgnoreCase(n.getNodeName()))
                {
                    for(Node r = n.getFirstChild(); r != null; r = r.getNextSibling())
                    {
                        if("reward".equals(r.getNodeName()))
                        {
                            Reward reward = new Reward(XmlUtil.getIntAttribute(r, "level"));
                            for(Node i = r.getFirstChild(); i != null; i = i.getNextSibling())
                            {
                                if("item".equals(i.getNodeName()))
                                {
                                    StatsSet item = new StatsSet();
                                    item.set("item_id", XmlUtil.getIntAttribute(i, "id"));
                                    item.set("count", XmlUtil.getLongAttribute(i, "count"));
                                    reward.getItems().add(item);
                                }
                            }
                            rewards.put(reward.getLevel(), reward);
                        }
                    }
                }
            }
            _log.info("TrulyFree: loaded " + rewards.size() + " reward levels");
        }
        catch(Exception e)
        {
            _log.error("TrulyFree: error loading config: " + e, e);
        }
    }

    // Остальные методы остаются без изменений
    @Override
    public void onReload() {}

    @Override
    public void onShutdown() {}

    public void start()
    {
        L2Player player = (L2Player) self;
        if(!AdminTemplateManager.checkBoolean("eventMaster", player))
            return;

        if(!active)
        {
            ServerVariables.set("truly_free", "true");
            active = true;
            _log.info("TrulyFree event started by " + player.getName());
        }
        else
        {
            player.sendMessage("Event 'Truly Free' already started.");
        }
        show(Files.read("data/html/admin/events2.htm", player), player);
    }

    public void stop()
    {
        L2Player player = (L2Player) self;
        if(!AdminTemplateManager.checkBoolean("eventMaster", player))
            return;

        if(active)
        {
            ServerVariables.unset("truly_free");
            active = false;
            _log.info("Event 'Truly Free' stopped by " + player.getName());
        }
        else
        {
            player.sendMessage("Event 'Truly Free' not started.");
        }
        show(Files.read("data/html/admin/events2.htm", player), player);
    }

    public static void onLevelUp(L2Player player)
    {
        if(!active || !rewards.containsKey((int) player.getLevel()))
            return;

        int objectId = player.getObjectId();
        int level = player.getLevel();

        if(checkRewardAtLevel(objectId, level))
            return;

        Connection con = null;
        PreparedStatement stmt = null;
        try
        {
            con = DatabaseFactory.getInstance().getConnection();
            stmt = con.prepareStatement("INSERT INTO event_truly_free VALUES(?, ?)");
            stmt.setInt(1, objectId);
            stmt.setInt(2, level);
            stmt.execute();
            
            Reward reward = rewards.get(level);
            for(StatsSet item : reward.getItems())
            {
                PremiumItemManager.sendItemToPlayer(
                    objectId, 
                    item.getInteger("item_id"), 
                    item.getLong("count"), 
                    "Play4Free Truly Free"
                );
            }
            
            if(!reward.getItems().isEmpty())
            {
                player.sendPacket(Msg.ExNotifyPremiumItem);
                _log.info("TrulyFree: reward sent to " + player.getName() + " for level " + level);
            }
        }
        catch(Exception e)
        {
            _log.error("TrulyFree: can't update reward status: " + e, e);
        }
        finally
        {
            DbUtils.closeQuietly(con, stmt);
        }
    }

    private static boolean checkRewardAtLevel(int objectId, int level)
    {
        Connection con = null;
        PreparedStatement stmt = null;
        ResultSet rset = null;

        try
        {
            con = DatabaseFactory.getInstance().getConnection();
            stmt = con.prepareStatement("SELECT 1 FROM event_truly_free WHERE object_id = ? AND level = ?");
            stmt.setInt(1, objectId);
            stmt.setInt(2, level);
            rset = stmt.executeQuery();
            return rset.next();
        }
        catch(Exception e)
        {
            _log.error("TrulyFree: can't check reward status: " + e, e);
            return true; // Безопасный возврат в случае ошибки
        }
        finally
        {
            DbUtils.closeQuietly(con, stmt, rset);
        }
    }

    private static class Reward
    {
        private final int level;
        private final GArray<StatsSet> items;

        private Reward(int level)
        {
            this.level = level;
            items = new GArray<>();
        }

        public int getLevel()
        {
            return level;
        }

        public GArray<StatsSet> getItems()
        {
            return items;
        }
    }
}