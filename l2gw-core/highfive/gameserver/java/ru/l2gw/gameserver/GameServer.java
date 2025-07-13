package ru.l2gw.gameserver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import ru.l2gw.commons.network.MMOConnection;
import ru.l2gw.commons.network.MMOSocket;
import ru.l2gw.commons.network.SelectorConfig;
import ru.l2gw.commons.network.SelectorThread;
import ru.l2gw.commons.network.telnet.TelnetServer;
import ru.l2gw.commons.network.telnet.TelnetServerHandler;
import ru.l2gw.commons.versioning.Version;
import ru.l2gw.database.DatabaseFactory;
import ru.l2gw.database.mysql;
import ru.l2gw.extensions.ccpGuard.Protection;
import ru.l2gw.extensions.scripts.Events;
import ru.l2gw.extensions.scripts.ScriptObject;
import ru.l2gw.gameserver.cache.CrestCache;
import ru.l2gw.gameserver.controllers.GameTimeController;
import ru.l2gw.gameserver.controllers.RecipeController;
import ru.l2gw.gameserver.controllers.ThreadPoolManager;
import ru.l2gw.gameserver.controllers.TradeController;
import ru.l2gw.gameserver.geodata.GeoEngine;
import ru.l2gw.gameserver.handler.*;
import ru.l2gw.gameserver.idfactory.IdFactory;
import ru.l2gw.gameserver.instancemanager.*;
import ru.l2gw.gameserver.instancemanager.boss.AntharasManager;
import ru.l2gw.gameserver.instancemanager.boss.BaiumManager;
import ru.l2gw.gameserver.instancemanager.boss.ValakasManager;
import ru.l2gw.gameserver.instancemanager.superpoint.SuperpointManager;
import ru.l2gw.gameserver.loginservercon.LSConnection;
import ru.l2gw.gameserver.model.L2Manor;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.L2World;
import ru.l2gw.gameserver.model.entity.Hero;
import ru.l2gw.gameserver.model.entity.MonsterRace;
import ru.l2gw.gameserver.model.entity.SevenSigns;
import ru.l2gw.gameserver.model.entity.SevenSignsFestival.FestivalManager;
import ru.l2gw.gameserver.model.entity.SevenSignsFestival.SevenSignsFestival;
import ru.l2gw.gameserver.model.entity.category.CategoryManager;
import ru.l2gw.gameserver.model.entity.olympiad.Olympiad;
import ru.l2gw.gameserver.model.instances.L2DoorInstance;
import ru.l2gw.gameserver.model.mail.MailController;
import ru.l2gw.gameserver.network.GameClient;
import ru.l2gw.gameserver.network.GamePacketHandler;
import ru.l2gw.gameserver.network.PacketFloodProtector;
import ru.l2gw.gameserver.network.telnet.commands.*;
import ru.l2gw.gameserver.pservercon.PSConnection;
import ru.l2gw.gameserver.tables.*;
import ru.l2gw.gameserver.taskmanager.AutoSaveManager;
import ru.l2gw.gameserver.taskmanager.ItemsAutoDestroy;
import ru.l2gw.gameserver.taskmanager.TaskManager;
import ru.l2gw.util.DeadLockDetector;
import ru.l2gw.util.Strings;
import ru.l2gw.util.Util;

import java.io.File;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameServer
{
    private static final Log _log = LogFactory.getLog(GameServer.class.getName());
    public static final Map<String, ScriptObject> scriptsObjects = new HashMap<>();

    private final SelectorThread<GameClient> _selectorThread;
    public static GameServer gameServer;
    public static TelnetServer statusServer;
    public static Events events;
    public static MaintenanceManager _maintenanceManager;

    private static int _serverStarted;
    private static int _serverId;
    private static Version version;

    public long getUsedMemoryMB()
    {
        return (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
    }

    public SelectorThread<GameClient> getSelectorThread()
    {
        return _selectorThread;
    }

    public static int uptime()
    {
        return Util.getCurrentTime() - _serverStarted;
    }

    public GameServer() throws Exception
    {
        _serverStarted = Util.getCurrentTime();
        _log.debug("used mem:" + getUsedMemoryMB() + "MB");

        DeadLockDetector.start();
        Strings.reload();

        IdFactory idFactory = IdFactory.getInstance();
        if(!idFactory.isInitialized())
        {
            _log.fatal("Could not read object IDs from DB. Please Check Your Data.");
            throw new Exception("Could not initialize the ID factory");
        }

        ThreadPoolManager.getInstance();
        CrestCache.load();
        GameTimeController.getInstance();
        CharNameTable.getInstance();
        CategoryManager.load();

        SkillTable skillTable = SkillTable.getInstance();
        if(!skillTable.isInitialized())
        {
            _log.fatal("Could not find the Skills files. Please Check Your Data.");
            throw new Exception("Could not initialize the skill table");
        }

        ItemTable itemTable = ItemTable.getInstance();
        if(!itemTable.isInitialized())
        {
            _log.fatal("Could not find the Items files. Please Check Your Data.");
            throw new Exception("Could not initialize the item table");
        }

        ArmorSetsTable armorSetsTable = ArmorSetsTable.getInstance();
        if(!armorSetsTable.isInitialized())
        {
            _log.fatal("Could not find the ArmorSets files. Please Check Your Data.");
            throw new Exception("Could not initialize the armorSets table");
        }

        VariationData.load();
        OptionData.load();
        EnchantOptionTable.load();

        events = new Events();
        TradeController.getInstance();
        ExtractableItems.getInstance();
        KamaelWeaponExchangeInstance.getInstance();
        RecipeController.load();
        Protection.Init();

        if(Config.PACKET_FLOOD_PROTECTOR)
            PacketFloodProtector.getInstance();

        SkillTreeTable.getInstance();
        CharTemplateTable.getInstance();
        ClassMasterTable.getInstance();

        SevenSigns sevenSignsEngine = SevenSigns.getInstance();
        SevenSignsFestival.getInstance();
        ClanTable.getInstance();
        NpcTable.getInstance();

        if(!NpcTable.isInitialized())
        {
            _log.fatal("Could not find the extracted files. Please Check Your Data.");
            throw new Exception("Could not initialize the npc table");
        }

        SuperpointManager.getInstance();
        PetDataTable.getInstance();
        
        if(Config.MAINTENANCE_DAY > 0)
        {
            _maintenanceManager = MaintenanceManager.getInstance();
            _maintenanceManager.init();
        }

        CubicManager.getInstance(); 
        HennaTable hennaTable = HennaTable.getInstance();
        if(!hennaTable.isInitialized())
            throw new Exception("Could not initialize the Henna Table");
        
        HennaTreeTable.getInstance();
        if(!hennaTable.isInitialized())
            throw new Exception("Could not initialize the Henna Tree Table");

        GeoEngine.loadGeo();
        ResidenceManager.getInstance();
        InstanceManager.getInstance();
        ZoneManager.getInstance();

        if(Config.ALLOW_DOORS)
        {
            Arrays.stream(DoorTable.getInstance().getDoors())
                .filter(door -> !door.isOpen())
                .forEach(door -> {
                    GeoEngine.applyControl(door);
                    door._geoOpen = false;
                });
        }

        FieldCycleManager.load();
        ResidenceManager.getInstance().incrementZones();
        CastleSiegeManager.load();
        FortressSiegeManager.getInstance();
        L2Manor.getInstance();
        CastleManorManager.getInstance();
        TownManager.getInstance();
        SpawnTable.getInstance();
        TerritoryWarManager.load();
        RaidBossSpawnManager.getInstance();
        DimensionalRiftManager.getInstance().init();
        Announcements.getInstance();
        ClanHallSiegeManager.getInstance();
        MapRegionTable.getInstance();
        PlayerMessageStack.getInstance();

        if(Config.AUTODESTROY_ITEM_AFTER > 0)
            ItemsAutoDestroy.getInstance();

        MonsterRace.getInstance();
        DoorTable.getInstance();
        StaticObjectsTable.getInstance();

        sevenSignsEngine.spawnSevenSignsNPC();
        FestivalManager.getInstance().createSpawnSets();
        Hero.loadHeroes();
        Olympiad.load();
        PetitionManager.getInstance();
        CursedWeaponsManager.getInstance();

        if(!Config.ALLOW_WEDDING)
        {
            CoupleManager.getInstance();
            _log.info("CoupleManager initialized");
        }

        ItemHandler itemHandler = ItemHandler.getInstance();
        _log.info("ItemHandler: Loaded " + itemHandler.size() + " handlers.");

        AdminCommandHandler adminCommandHandler = AdminCommandHandler.getInstance();
        _log.info("AdminCommandHandler: Loaded " + adminCommandHandler.size() + " handlers.");

        UserCommandHandler userCommandHandler = UserCommandHandler.getInstance();
        _log.info("UserCommandHandler: Loaded " + userCommandHandler.size() + " handlers.");

        VoicedCommandHandler voicedCommandHandler = VoicedCommandHandler.getInstance();
        _log.info("VoicedCommandHandler: Loaded " + voicedCommandHandler.size() + " handlers.");

        TaskManager.getInstance();
        MercTicketManager.getInstance();
        AutoSaveManager.getInstance();
        PartyRoomManager.getInstance();

        Shutdown shutdown = Shutdown.getInstance();
        Runtime.getRuntime().addShutdownHook(shutdown);
        _log.info("IdFactory: Free ObjectID's remaining: " + IdFactory.getInstance().size());

        AuctionManager.getInstance();
        RequestActionHandler.getInstance();
        AntharasManager.getInstance().init();
        ValakasManager.getInstance().init();
        BaiumManager.getInstance().init();
        ItemAuctionManager.getInstance();
        ProductManager.load();

        if(Config.TM_ENABLED)
            TransferManager.getInstance();

        if(Config.ALLOW_BOAT)
            VehicleManager.getInstance();

        new File("./log/game").mkdirs();

        // Modernized restart time calculation using Java Time API
        long restartTimeSeconds = Config.RESTART_TIME * 3600L;
        long restartAtSeconds = 0;

        if(Config.RESTART_AT_TIME > -1)
        {
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextRestart = now.with(LocalTime.of(Config.RESTART_AT_TIME, 0));
            
            if(now.isAfter(nextRestart))
                nextRestart = nextRestart.plusDays(1);
            
            restartAtSeconds = Duration.between(now, nextRestart).getSeconds();
        }

        if((restartTimeSeconds < restartAtSeconds && restartTimeSeconds > 0) || 
           (restartTimeSeconds > restartAtSeconds && restartAtSeconds == 0))
        {
            Shutdown.getInstance().setAutoRestart((int) restartTimeSeconds);
        }
        else if(restartAtSeconds > 0)
        {
            Shutdown.getInstance().setAutoRestart((int) restartAtSeconds);
        }

        _log.info("GameServer Started");
        _log.info("Maximum Numbers of Connected Players: " + Config.MAXIMUM_ONLINE_USERS);

        ThreadPoolManager.getInstance().scheduleGeneralAtFixedRate(new AutoAnnounce(), 60 * 1000, 60 * 1000);
        L2World.loadTaxSum();
        FakePlayersTable.loadFakeNames();
        MMOSocket.getInstance();

        GamePacketHandler gph = new GamePacketHandler();
        SelectorConfig<GameClient> sc = new SelectorConfig<>(null, gph);
        sc.setMaxSendPerPass(Config.PACKET_MAX_SEND_PER_PASS);
        sc.setSelectorSleepTime(1);
        _selectorThread = new SelectorThread<>(sc, null, gph, gph, gph, null);
        _selectorThread.setAntiFlood(Config.ANTIFLOOD_ENABLE);
        
        if(Config.ANTIFLOOD_ENABLE)
        {
            _selectorThread.setAntiFloodSocketsConf(
                Config.MAX_UNHANDLED_SOCKETS_PER_IP, 
                Config.UNHANDLED_SOCKET_MIN_TTL
            );
        }
        
        InetAddress address = Config.GAMESERVER_HOSTNAME.equals("*") ? 
            null : InetAddress.getByName(Config.GAMESERVER_HOSTNAME);
        _selectorThread.openServerSocket(address, Config.PORT_GAME);
        _selectorThread.start();

        if(Config.SERVICES_OFFLINE_TRADE_ALLOW && Config.SERVICES_OFFLINE_TRADE_RESTORE_AFTER_RESTART)
        {
            mysql.set("DELETE FROM `character_variables` WHERE `name` = 'offline' and expire_time < UNIX_TIMESTAMP()");
            Integer[][] list = mysql.simple_get_int_array(new String[]{"obj_id"}, "character_variables", "name LIKE \"offline\"");
            for(Integer[] id : list)
            {
                GameClient client = new GameClient(new MMOConnection<>(_selectorThread), true);
                client.setConnection(null);
                client.setCharSelection(id[0]);
                L2Player p = client.loadCharFromDisk(0);
                
                if(p == null || p.isDead() || !p.isInStoreMode() || p.getAccessLevel() < 0)
                    continue;
                
                p.spawnMe();
                p.setOnlineStatus(true);
                p.setOfflineMode(true);
                p.setConnected(false);
                p.setNameColor(Config.SERVICES_OFFLINE_TRADE_NAME_COLOR);
                p.restoreEffects();
                p.restoreDisableSkills();
                p.broadcastUserInfo(true);
                client.setLoginName(p.getAccountName());
                client.setPlayer(p);
                LSConnection.getInstance().addOfflineAccount(client);
            }
            _log.info("Restored " + list.length + " offline traders");
        }

        MailController.getInstance();
        PremiumItemManager.startLoadTask();
        LSConnection.getInstance().start();
        
        if(Config.PRODUCT_SHOP_ENABLED)
            PSConnection.getInstance().start();
    }

    public static void main(String[] args) throws Exception
    {
        new File("./log/").mkdirs();
        version = new Version(GameServer.class);
        _log.info("Gameserver revision: " + version.getRevisionNumber() + 
                 " build: " + version.getBuildDate() + " " + version.getBuildBy());
        
        Version commonVersion = new Version(MMOConnection.class);
        _log.info("Commons revision: " + commonVersion.getRevisionNumber() + 
                 " build: " + commonVersion.getBuildDate() + " " + commonVersion.getBuildBy());

        Config.load();
        checkFreePorts();
        Class.forName(Config.DATABASE_DRIVER).getDeclaredConstructor().newInstance();
        DatabaseFactory.getInstance().getConnection().close();
        gameServer = new GameServer();

        if(Config.TELNET_ENABLED)
        {
            TelnetServerHandler telnetHandler = new TelnetServerHandler(Config.TELNET_PASSWORD);
            telnetHandler.addHandler(new AbortCommand());
            telnetHandler.addHandler(new AnnounceCommand());
            telnetHandler.addHandler(new BanallCommand());
            telnetHandler.addHandler(new BanchatCommand());
            telnetHandler.addHandler(new BanipCommand());
            telnetHandler.addHandler(new DatabaseCommand());
            telnetHandler.addHandler(new DumpmemCommand());
            telnetHandler.addHandler(new GcCommand());
            telnetHandler.addHandler(new GiveCommand());
            telnetHandler.addHandler(new GmchatCommand());
            telnetHandler.addHandler(new GmlistCommand());
            telnetHandler.addHandler(new KickCommand());
            telnetHandler.addHandler(new MsgCommand());
            telnetHandler.addHandler(new PerformanceCommand());
            telnetHandler.addHandler(new PurgeCommand());
            telnetHandler.addHandler(new ReloadCommand());
            telnetHandler.addHandler(new ScriptReloadCommand());
            telnetHandler.addHandler(new RestartCommand());
            telnetHandler.addHandler(new SetCommand());
            telnetHandler.addHandler(new ShowCommand());
            telnetHandler.addHandler(new ShutdownCommand());
            telnetHandler.addHandler(new SpawnItemCommand());
            telnetHandler.addHandler(new StatusCommand());
            telnetHandler.addHandler(new UnbanipCommand());
            telnetHandler.addHandler(new VersionCommand());
            telnetHandler.addHandler(new WhoisCommand());
            statusServer = new TelnetServer(Config.TELNET_HOST, Config.TELNET_PORT, telnetHandler);
        }
        else
        {
            _log.info("Telnet server is currently disabled.");
        }

        System.gc();
        long freeMem = (Runtime.getRuntime().maxMemory() - 
                       (Runtime.getRuntime().totalMemory() - 
                        Runtime.getRuntime().freeMemory())) / (1024 * 1024);
        long totalMem = Runtime.getRuntime().maxMemory() / (1024 * 1024);
        _log.info("Free memory " + freeMem + " Mb of " + totalMem + " Mb");
    }

    public static void checkFreePorts()
    {
        try(ServerSocket ignored = Config.GAMESERVER_HOSTNAME.equals("*") ?
            new ServerSocket(Config.PORT_GAME) :
            new ServerSocket(Config.PORT_GAME, 50, InetAddress.getByName(Config.GAMESERVER_HOSTNAME)))
        {
            // Socket will auto-close
        }
        catch(Exception e)
        {
            _log.warn("\nPort " + Config.PORT_GAME + " is already bound. Please free it and restart server.");
            System.exit(0);
        }
    }

    public static void setServerId(int serverId)
    {
        _serverId = serverId;
    }

    public static int getServerId()
    {
        return _serverId;
    }

    public static Version getVersion()
    {
        return version;
    }
}