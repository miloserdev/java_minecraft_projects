package znn;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.base64.Base64;
import io.netty.util.ResourceLeakDetector;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Proxy;
import java.nio.charset.Charset;
import java.security.KeyPair;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.server.v1_10_R1.BlockPosition;
import net.minecraft.server.v1_10_R1.ChatComponentText;
import net.minecraft.server.v1_10_R1.Chunk;
import net.minecraft.server.v1_10_R1.ChunkProviderServer;
import net.minecraft.server.v1_10_R1.CommandDispatcher;
import net.minecraft.server.v1_10_R1.CommandObjectiveExecutor;
import net.minecraft.server.v1_10_R1.Convertable;
import net.minecraft.server.v1_10_R1.CrashReport;
import net.minecraft.server.v1_10_R1.CrashReportCallable;
import net.minecraft.server.v1_10_R1.CrashReportSystemDetails;
import net.minecraft.server.v1_10_R1.DataConverterManager;
import net.minecraft.server.v1_10_R1.DataConverterRegistry;
import net.minecraft.server.v1_10_R1.DedicatedServer;
import net.minecraft.server.v1_10_R1.DemoWorldServer;
import net.minecraft.server.v1_10_R1.DimensionManager;
import net.minecraft.server.v1_10_R1.DispenserRegistry;
import net.minecraft.server.v1_10_R1.Entity;
import net.minecraft.server.v1_10_R1.EntityHuman;
import net.minecraft.server.v1_10_R1.EntityPlayer;
import net.minecraft.server.v1_10_R1.EntityTracker;
import net.minecraft.server.v1_10_R1.EnumDifficulty;
import net.minecraft.server.v1_10_R1.EnumGamemode;
import net.minecraft.server.v1_10_R1.ExceptionWorldConflict;
import net.minecraft.server.v1_10_R1.GameRules;
import net.minecraft.server.v1_10_R1.IAsyncTaskHandler;
import net.minecraft.server.v1_10_R1.IChatBaseComponent;
import net.minecraft.server.v1_10_R1.ICommandHandler;
import net.minecraft.server.v1_10_R1.ICommandListener;
import net.minecraft.server.v1_10_R1.IDataManager;
import net.minecraft.server.v1_10_R1.IMojangStatistics;
import net.minecraft.server.v1_10_R1.IProgressUpdate;
import net.minecraft.server.v1_10_R1.ITickable;
import net.minecraft.server.v1_10_R1.IWorldAccess;
import net.minecraft.server.v1_10_R1.MathHelper;
import net.minecraft.server.v1_10_R1.MethodProfiler;
import net.minecraft.server.v1_10_R1.MojangStatisticsGenerator;
import net.minecraft.server.v1_10_R1.Packet;
import net.minecraft.server.v1_10_R1.PacketPlayOutUpdateTime;
import net.minecraft.server.v1_10_R1.PlayerConnection;
import net.minecraft.server.v1_10_R1.PlayerList;
import net.minecraft.server.v1_10_R1.PropertyManager;
import net.minecraft.server.v1_10_R1.ReportedException;
import net.minecraft.server.v1_10_R1.Scoreboard;
import net.minecraft.server.v1_10_R1.SecondaryWorldServer;
import net.minecraft.server.v1_10_R1.ServerConnection;
import net.minecraft.server.v1_10_R1.ServerNBTManager;
import net.minecraft.server.v1_10_R1.ServerPing;
import net.minecraft.server.v1_10_R1.SystemUtils;
import net.minecraft.server.v1_10_R1.UserCache;
import net.minecraft.server.v1_10_R1.Vec3D;
import net.minecraft.server.v1_10_R1.World;
import net.minecraft.server.v1_10_R1.WorldData;
import net.minecraft.server.v1_10_R1.WorldManager;
import net.minecraft.server.v1_10_R1.WorldProvider;
import net.minecraft.server.v1_10_R1.WorldServer;
import net.minecraft.server.v1_10_R1.WorldSettings;
import net.minecraft.server.v1_10_R1.WorldType;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.bukkit.craftbukkit.Main;
import org.bukkit.craftbukkit.libs.jline.Terminal;
import org.bukkit.craftbukkit.libs.jline.console.ConsoleReader;
import org.bukkit.craftbukkit.libs.joptsimple.OptionSet;
import org.bukkit.craftbukkit.v1_10_R1.CraftServer;
import org.bukkit.craftbukkit.v1_10_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_10_R1.SpigotTimings;
import org.bukkit.craftbukkit.v1_10_R1.chunkio.ChunkIOExecutor;
import org.bukkit.craftbukkit.v1_10_R1.scheduler.CraftScheduler;
import org.bukkit.craftbukkit.v1_10_R1.scoreboard.CraftScoreboardManager;
import org.bukkit.craftbukkit.v1_10_R1.util.ServerShutdownThread;
import org.bukkit.event.Event;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.generator.ChunkGenerator;
import org.bukkit.plugin.PluginLoadOrder;
import org.bukkit.plugin.PluginManager;
import org.spigotmc.CustomTimingsHandler;
import org.spigotmc.SpigotConfig;
import org.spigotmc.WatchdogThread;

public abstract class MinecraftServer
implements Runnable,
ICommandListener,
IAsyncTaskHandler,
IMojangStatistics {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final File a = new File("usercache.json");
    public Convertable convertable;
    private final MojangStatisticsGenerator m;
    public File universe;
    private final List<ITickable> o;
    public final ICommandHandler b;
    public final MethodProfiler methodProfiler;
    private ServerConnection p;
    private final ServerPing q;
    private final Random r;
    private final DataConverterManager dataConverterManager;
    private String serverIp;
    private int u;
    public WorldServer[] worldServer;
    private PlayerList v;
    private boolean isRunning;
    private boolean isStopped;
    private int ticks;
    protected final Proxy e;
    public String f;
    public int g;
    private boolean onlineMode;
    private boolean spawnAnimals;
    private boolean spawnNPCs;
    private boolean pvpMode;
    private boolean allowFlight;
    private String motd;
    private int F;
    private int G;
    public final long[] h;
    public long[][] i;
    private KeyPair H;
    private String I;
    private String J;
    private boolean demoMode;
    private boolean M;
    private String N;
    private String O;
    private boolean P;
    private long Q;
    private String R;
    private boolean S;
    private boolean T;
    private final YggdrasilAuthenticationService U;
    private final MinecraftSessionService V;
    private final GameProfileRepository W;
    private final UserCache X;
    private long Y;
    protected final int j;
    private Thread serverThread;
    private long aa;
    public List<WorldServer> worlds;
    public CraftServer server;
    public OptionSet options;
    public ConsoleCommandSender console;
    public RemoteConsoleCommandSender remoteConsole;
    public ConsoleReader reader;
    public static int currentTick = (int)(System.currentTimeMillis() / 50);
    public final Thread primaryThread;
    public Queue<Runnable> processQueue;
    public int autosavePeriod;
    private static final int TPS = 20;
    private static final int TICK_TIME = 50000000;
    private static final int SAMPLE_INTERVAL = 100;
    public final double[] recentTps;
    private boolean hasStopped;
    private final Object stopLock;

    public MinecraftServer(OptionSet options, Proxy proxy, DataConverterManager dataconvertermanager, YggdrasilAuthenticationService yggdrasilauthenticationservice, MinecraftSessionService minecraftsessionservice, GameProfileRepository gameprofilerepository, UserCache usercache) {
        this.m = new MojangStatisticsGenerator("server", this, MinecraftServer.av());
        this.o = Lists.newArrayList();
        this.methodProfiler = new MethodProfiler();
        this.q = new ServerPing();
        this.r = new Random();
        this.u = -1;
        this.isRunning = true;
        this.h = new long[100];
        this.N = "";
        this.O = "";
        this.j = new ConcurrentLinkedQueue();
        this.aa = MinecraftServer.av();
        this.worlds = new ArrayList<WorldServer>();
        this.processQueue = new ConcurrentLinkedQueue<Runnable>();
        this.recentTps = new double[3];
        this.hasStopped = false;
        this.stopLock = new Object();
        ResourceLeakDetector.setEnabled(false);
        this.e = proxy;
        this.U = yggdrasilauthenticationservice;
        this.V = minecraftsessionservice;
        this.W = gameprofilerepository;
        this.X = usercache;
        this.b = this.i();
        this.dataConverterManager = dataconvertermanager;
        this.options = options;
        if (System.console() == null && System.getProperty("org.bukkit.craftbukkit.libs.jline.terminal") == null) {
            System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", "org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal");
            Main.useJline = false;
        }
        try {
            this.reader = new ConsoleReader(System.in, System.out);
            this.reader.setExpandEvents(false);
        }
        catch (Throwable v0) {
            try {
                System.setProperty("org.bukkit.craftbukkit.libs.jline.terminal", "org.bukkit.craftbukkit.libs.jline.UnsupportedTerminal");
                System.setProperty("user.language", "en");
                Main.useJline = false;
                this.reader = new ConsoleReader(System.in, System.out);
                this.reader.setExpandEvents(false);
            }
            catch (IOException ex) {
                LOGGER.warn((String)null, (Throwable)ex);
            }
        }
        //Runtime.getRuntime().addShutdownHook(new ServerShutdownThread(this));
        this.serverThread = this.primaryThread = new Thread((Runnable)this, "Server thread");
    }

    public abstract PropertyManager getPropertyManager();

    //protected CommandDispatcher i() {
    //    return new CommandDispatcher(this);
   // }

    public abstract boolean init() throws IOException;

    protected void a(String s) {
        if (this.getConvertable().isConvertable(s)) {
            LOGGER.info("Converting map!");
            this.b("menu.convertingLevel");
            this.getConvertable().convert(s, new IProgressUpdate(){
                private long b;

                @Override
                public void a(String s) {
                }

                @Override
                public void a(int i) {
                    if (System.currentTimeMillis() - this.b >= 1000) {
                        this.b = System.currentTimeMillis();
                        MinecraftServer.LOGGER.info("Converting... {}%", i);
                    }
                }

                @Override
                public void c(String s) {
                }
            });
        }
    }

    protected synchronized void b(String s) {
        this.R = s;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Lifted jumps to return sites
     */
    public void a(String s, String s1, long i, WorldType worldtype, String s2) {
        this.a(s);
        this.b("menu.loadingLevel");
        this.worldServer = new WorldServer[3];
        int worldCount = 3;
        j = 0;
        while (j < worldCount) {
            int dimension = 0;
            if (j != 1) 
            if (!this.getAllowNether())
            dimension = -1;

            if (j != 2)
            if (this.server.getAllowEnd()) {
                dimension = 1;

                worldType = World.Environment.getEnvironment(dimension).toString().toLowerCase();
                name = dimension == 0 ? s : String.valueOf(s) + "_" + worldType;
                gen = this.server.getGenerator(name);
                worldsettings = new WorldSettings(i, this.getGamemode(), this.getGenerateStructures(), this.isHardcore(), worldtype);
                worldsettings.setGeneratorSettings(s2);
                WorldData worlddata;
				if (j == 0) {
                    idatamanager = new ServerNBTManager(this.server.getWorldContainer(), s1, true, this.dataConverterManager);
                    worlddata = idatamanager.getWorldData();
                    if (worlddata == null) {
                        worlddata = new WorldData(worldsettings, s1);
                    }
                    worlddata.checkName(s1);
                    world = this.V() != false ? (WorldServer)new DemoWorldServer(this, idatamanager, worlddata, dimension, this.methodProfiler).b() : (WorldServer)new WorldServer(this, idatamanager, worlddata, dimension, this.methodProfiler, World.Environment.getEnvironment(dimension), gen).b();
                    world.a(worldsettings);
                    this.server.scoreboardManager = new CraftScoreboardManager(this, world.getScoreboard());
                } else {
                    dim = "DIM" + dimension;
                    newWorld = new File(new File(name), dim);
                    oldWorld = new File(new File(s), dim);
                    if (!newWorld.isDirectory() && oldWorld.isDirectory()) {
                        MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder required ----");
                        MinecraftServer.LOGGER.info("Unfortunately due to the way that Minecraft implemented multiworld support in 1.6, Bukkit requires that you move your " + worldType + " folder to a new location in order to operate correctly.");
                        MinecraftServer.LOGGER.info("We will move this folder for you, but it will mean that you need to move it back should you wish to stop using Bukkit in the future.");
                        MinecraftServer.LOGGER.info("Attempting to move " + oldWorld + " to " + newWorld + "...");
                        if (newWorld.exists()) {
                            MinecraftServer.LOGGER.warn("A file or folder already exists at " + newWorld + "!");
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                        } else if (newWorld.getParentFile().mkdirs()) {
                            if (oldWorld.renameTo(newWorld)) {
                                MinecraftServer.LOGGER.info("Success! To restore " + worldType + " in the future, simply move " + newWorld + " to " + oldWorld);
                                try {
                                    Files.copy(new File(new File(s), "level.dat"), new File(new File(name), "level.dat"));
                                    FileUtils.copyDirectory(new File(new File(s), "data"), new File(new File(name), "data"));
                                }
                                catch (IOException v0) {
                                    MinecraftServer.LOGGER.warn("Unable to migrate world data.");
                                }
                                MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder complete ----");
                            } else {
                                MinecraftServer.LOGGER.warn("Could not move folder " + oldWorld + " to " + newWorld + "!");
                                MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                            }
                        } else {
                            MinecraftServer.LOGGER.warn("Could not create path for " + newWorld + "!");
                            MinecraftServer.LOGGER.info("---- Migration of old " + worldType + " folder failed ----");
                        }
                    }
                    if ((worlddata = (idatamanager = new ServerNBTManager(this.server.getWorldContainer(), name, true, this.dataConverterManager)).getWorldData()) == null) {
                        worlddata = new WorldData(worldsettings, name);
                    }
                    worlddata.checkName(name);
                    world = (WorldServer)new SecondaryWorldServer(this, idatamanager, dimension, this.worlds.get(0), this.methodProfiler, worlddata, World.Environment.getEnvironment(dimension), gen).b();
                }
                this.server.getPluginManager().callEvent(new WorldInitEvent(world.getWorld()));
                world.addIWorldAccess(new WorldManager(this, world));
                if (!this.R()) {
                    world.getWorldData().setGameType(this.getGamemode());
                }
                this.worlds.add(world);
                this.getPlayerList().setPlayerFileData(this.worlds.toArray(new WorldServer[this.worlds.size()]));
            }
lbl67: // 4 sources:
            ++j;
        }
        this.v.setPlayerFileData(this.worldServer);
        this.a(this.getDifficulty());
        this.l();
    }

    protected void l() {
        int i = 0;
        this.b("menu.generatingTerrain");
        int m = 0;
        while (m < this.worlds.size()) {
            WorldServer worldserver = this.worlds.get(m);
            LOGGER.info("Preparing start region for level " + m + " (Seed: " + worldserver.getSeed() + ")");
            if (worldserver.getWorld().getKeepSpawnInMemory()) {
                BlockPosition blockposition = worldserver.getSpawn();
                long j = MinecraftServer.av();
                i = 0;
                int k = -192;
                while (k <= 192 && this.isRunning()) {
                    int l = -192;
                    while (l <= 192 && this.isRunning()) {
                        long i1 = MinecraftServer.av();
                        if (i1 - j > 1000) {
                            this.a_("Preparing spawn area", i * 100 / 625);
                            j = i1;
                        }
                        ++i;
                        worldserver.getChunkProviderServer().getChunkAt(blockposition.getX() + k >> 4, blockposition.getZ() + l >> 4);
                        l += 16;
                    }
                    k += 16;
                }
            }
            ++m;
        }
        for (WorldServer world : this.worlds) {
            this.server.getPluginManager().callEvent(new WorldLoadEvent(world.getWorld()));
        }
        this.t();
    }

    protected void a(String s, IDataManager idatamanager) {
        File file = new File(idatamanager.getDirectory(), "resources.zip");
        if (file.isFile()) {
            this.setResourcePack("level://" + s + "/" + "resources.zip", "");
        }
    }

    public abstract boolean getGenerateStructures();

    public abstract EnumGamemode getGamemode();

    public abstract EnumDifficulty getDifficulty();

    public abstract boolean isHardcore();

    public abstract int q();

    public abstract boolean r();

    public abstract boolean s();

    protected void a_(String s, int i) {
        this.f = s;
        this.g = i;
        LOGGER.info("{}: {}%", s, i);
    }

    protected void t() {
        this.f = null;
        this.g = 0;
        this.server.enablePlugins(PluginLoadOrder.POSTWORLD);
    }

    protected void saveChunks(boolean flag) {
        WorldServer[] aworldserver = this.worldServer;
        aworldserver.length;
        int j = 0;
        while (j < this.worlds.size()) {
            WorldServer worldserver = this.worlds.get(j);
            if (worldserver != null) {
                if (!flag) {
                    LOGGER.info("Saving chunks for level '{}'/{}", worldserver.getWorldData().getName(), worldserver.worldProvider.getDimensionManager().b());
                }
                try {
                    worldserver.save(true, null);
                    worldserver.saveLevel();
                }
                catch (ExceptionWorldConflict exceptionworldconflict) {
                    LOGGER.warn(exceptionworldconflict.getMessage());
                }
            }
            ++j;
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void stop() throws ExceptionWorldConflict {
        Object object = this.stopLock;
        synchronized (object) {
            if (this.hasStopped) {
                return;
            }
            this.hasStopped = true;
        }
        LOGGER.info("Stopping server");
        if (this.server != null) {
            this.server.disablePlugins();
        }
        if (this.am() != null) {
            this.am().b();
        }
        if (this.v != null) {
            LOGGER.info("Saving players");
            this.v.savePlayers();
            this.v.u();
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException v1) {}
        }
        if (this.worldServer != null) {
            LOGGER.info("Saving worlds");
            WorldServer[] aworldserver = this.worldServer;
            int i = aworldserver.length;
            int j = 0;
            while (j < i) {
                WorldServer worldserver = aworldserver[j];
                if (worldserver != null) {
                    worldserver.savingDisabled = false;
                }
                ++j;
            }
            this.saveChunks(false);
            aworldserver = this.worldServer;
            i = aworldserver.length;
        }
        if (this.m.d()) {
            this.m.e();
        }
        if (SpigotConfig.saveUserCacheOnStopOnly) {
            LOGGER.info("Saving usercache.json");
            this.X.c();
        }
    }

    public String getServerIp() {
        return this.serverIp;
    }

    public void c(String s) {
        this.serverIp = s;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void safeShutdown() {
        this.isRunning = false;
    }

    private static double calcTps(double avg, double exp, double tps) {
        return avg * exp + tps * (1.0 - exp);
    }

    @Override
    public void run() {
        block45 : {
            try {
                try {
                    if (this.init()) {
                        this.aa = MinecraftServer.av();
                        this.q.setMOTD(new ChatComponentText(this.motd));
                        this.q.setServerInfo(new ServerPing.ServerData("1.10.2", 210));
                        this.a(this.q);
                        Arrays.fill(this.recentTps, 20.0);
                        long lastTick = System.nanoTime();
                        long catchupTime = 0;
                        long tickSection = lastTick;
                        while (this.isRunning) {
                            long curTime = System.nanoTime();
                            long wait = 50000000 - (curTime - lastTick) - catchupTime;
                            if (wait > 0) {
                                Thread.sleep(wait / 1000000);
                                catchupTime = 0;
                                continue;
                            }
                            catchupTime = Math.min(1000000000, Math.abs(wait));
                            if (currentTick++ % 100 == 0) {
                                double currentTps = 1.0E9 / (double)(curTime - tickSection) * 100.0;
                                this.recentTps[0] = MinecraftServer.calcTps(this.recentTps[0], 0.92, currentTps);
                                this.recentTps[1] = MinecraftServer.calcTps(this.recentTps[1], 0.9835, currentTps);
                                this.recentTps[2] = MinecraftServer.calcTps(this.recentTps[2], 0.9945, currentTps);
                                tickSection = curTime;
                            }
                            lastTick = curTime;
                            this.C();
                            this.P = true;
                        }
                    } else {
                        this.a((CrashReport)null);
                    }
                }
                catch (Throwable throwable) {
                    LOGGER.error("Encountered an unexpected exception", throwable);
                    if (throwable.getCause() != null) {
                        LOGGER.error("\tCause of unexpected exception was", throwable.getCause());
                    }
                    CrashReport crashreport = null;
                    crashreport = throwable instanceof ReportedException ? this.b(((ReportedException)throwable).a()) : this.b(new CrashReport("Exception in server tick loop", throwable));
                    File file = new File(new File(this.A(), "crash-reports"), "crash-" + new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss").format(new Date()) + "-server.txt");
                    if (crashreport.a(file)) {
                        LOGGER.error("This crash report has been saved to: {}", file.getAbsolutePath());
                    } else {
                        LOGGER.error("We were unable to save this crash report to disk.");
                    }
                    this.a(crashreport);
                    try {
                        try {
                            WatchdogThread.doStop();
                            this.isStopped = true;
                            this.stop();
                        }
                        catch (Throwable throwable1) {
                            LOGGER.error("Exception stopping the server", throwable1);
                            try {
                                this.reader.getTerminal().restore();
                            }
                            catch (Exception v0) {}
                            this.B();
                            break block45;
                        }
                    }
                    catch (Throwable var17_13) {
                        try {
                            this.reader.getTerminal().restore();
                        }
                        catch (Exception v1) {}
                        this.B();
                        throw var17_13;
                    }
                    try {
                        this.reader.getTerminal().restore();
                    }
                    catch (Exception v2) {}
                    this.B();
                    break block45;
                }
            }
            catch (Throwable var18_16) {
                block50 : {
                    try {
                        try {
                            WatchdogThread.doStop();
                            this.isStopped = true;
                            this.stop();
                        }
                        catch (Throwable throwable1) {
                            LOGGER.error("Exception stopping the server", throwable1);
                            try {
                                this.reader.getTerminal().restore();
                            }
                            catch (Exception v3) {}
                            this.B();
                            break block50;
                        }
                    }
                    catch (Throwable var17_14) {
                        try {
                            this.reader.getTerminal().restore();
                        }
                        catch (Exception v4) {}
                        this.B();
                        throw var17_14;
                    }
                    try {
                        this.reader.getTerminal().restore();
                    }
                    catch (Exception v5) {}
                    this.B();
                }
                throw var18_16;
            }
            try {
                try {
                    WatchdogThread.doStop();
                    this.isStopped = true;
                    this.stop();
                }
                catch (Throwable throwable1) {
                    LOGGER.error("Exception stopping the server", throwable1);
                    try {
                        this.reader.getTerminal().restore();
                    }
                    catch (Exception v6) {}
                    this.B();
                    break block45;
                }
            }
            catch (Throwable var17_15) {
                try {
                    this.reader.getTerminal().restore();
                }
                catch (Exception v7) {}
                this.B();
                throw var17_15;
            }
            try {
                this.reader.getTerminal().restore();
            }
            catch (Exception v8) {}
            this.B();
        }
    }

    public void a(ServerPing serverping) {
        File file = this.d("server-icon.png");
        if (!file.exists()) {
            file = this.getConvertable().b(this.S(), "icon.png");
        }
        if (file.isFile()) {
            ByteBuf bytebuf = Unpooled.buffer();
            try {
                try {
                    BufferedImage bufferedimage = ImageIO.read(file);
                    Validate.validState(bufferedimage.getWidth() == 64, "Must be 64 pixels wide", new Object[0]);
                    Validate.validState(bufferedimage.getHeight() == 64, "Must be 64 pixels high", new Object[0]);
                    ImageIO.write((RenderedImage)bufferedimage, "PNG", new ByteBufOutputStream(bytebuf));
                    ByteBuf bytebuf1 = Base64.encode(bytebuf);
                    serverping.setFavicon("data:image/png;base64," + bytebuf1.toString(Charsets.UTF_8));
                }
                catch (Exception exception) {
                    LOGGER.error("Couldn't load server icon", (Throwable)exception);
                    bytebuf.release();
                }
            }
            finally {
                bytebuf.release();
            }
        }
    }

    public File A() {
        return new File(".");
    }

    public void a(CrashReport crashreport) {
    }

    public void B() {
    }

    protected void C() throws ExceptionWorldConflict {
        SpigotTimings.serverTickTimer.startTiming();
        long i = System.nanoTime();
        ++this.ticks;
        if (this.S) {
            this.S = false;
            this.methodProfiler.a = true;
            this.methodProfiler.a();
        }
        this.methodProfiler.a("root");
        this.D();
        if (i - this.Y >= 5000000000L) {
            this.Y = i;
            this.q.setPlayerSample(new ServerPing.ServerPingPlayerSample(this.I(), this.H()));
            GameProfile[] agameprofile = new GameProfile[Math.min(this.H(), 12)];
            int j = MathHelper.nextInt(this.r, 0, this.H() - agameprofile.length);
            int k = 0;
            while (k < agameprofile.length) {
                agameprofile[k] = this.v.v().get(j + k).getProfile();
                ++k;
            }
            Collections.shuffle(Arrays.asList(agameprofile));
            this.q.b().a(agameprofile);
        }
        if (this.autosavePeriod > 0 && this.ticks % this.autosavePeriod == 0) {
            SpigotTimings.worldSaveTimer.startTiming();
            this.methodProfiler.a("save");
            this.v.savePlayers();
            this.server.playerCommandState = true;
            for (World world : this.worlds) {
                world.getWorld().save(false);
            }
            this.server.playerCommandState = false;
            this.methodProfiler.b();
            SpigotTimings.worldSaveTimer.stopTiming();
        }
        this.methodProfiler.a("tallying");
        this.h[this.ticks % 100] = System.nanoTime() - i;
        this.methodProfiler.b();
        this.methodProfiler.a("snooper");
        if (this.getSnooperEnabled() && !this.m.d() && this.ticks > 100) {
            this.m.a();
        }
        if (this.getSnooperEnabled() && this.ticks % 6000 == 0) {
            this.m.b();
        }
        this.methodProfiler.b();
        this.methodProfiler.b();
        WatchdogThread.tick();
        SpigotTimings.serverTickTimer.stopTiming();
        CustomTimingsHandler.tick();
    }

    public void D() {
        int i;
        FutureTask<?> entry;
        SpigotTimings.schedulerTimer.startTiming();
        this.server.getScheduler().mainThreadHeartbeat(this.ticks);
        SpigotTimings.schedulerTimer.stopTiming();
        this.methodProfiler.a("jobs");
        int count = this.j.size();
        while (count-- > 0 && (entry = this.j.poll()) != null) {
            SystemUtils.a(entry, LOGGER);
        }
        this.methodProfiler.c("levels");
        SpigotTimings.processQueueTimer.startTiming();
        while (!this.processQueue.isEmpty()) {
            this.processQueue.remove().run();
        }
        SpigotTimings.processQueueTimer.stopTiming();
        SpigotTimings.chunkIOTickTimer.startTiming();
        ChunkIOExecutor.tick();
        SpigotTimings.chunkIOTickTimer.stopTiming();
        SpigotTimings.timeUpdateTimer.startTiming();
        if (this.ticks % 20 == 0) {
            i = 0;
            while (i < this.getPlayerList().players.size()) {
                EntityPlayer entityplayer = this.getPlayerList().players.get(i);
                entityplayer.playerConnection.sendPacket(new PacketPlayOutUpdateTime(entityplayer.world.getTime(), entityplayer.getPlayerTime(), entityplayer.world.getGameRules().getBoolean("doDaylightCycle")));
                ++i;
            }
        }
        SpigotTimings.timeUpdateTimer.stopTiming();
        i = 0;
        while (i < this.worlds.size()) {
            System.nanoTime();
            WorldServer worldserver = this.worlds.get(i);
            this.methodProfiler.a(worldserver.getWorldData().getName());
            this.methodProfiler.a("tick");
            try {
                worldserver.timings.doTick.startTiming();
                worldserver.doTick();
                worldserver.timings.doTick.stopTiming();
            }
            catch (Throwable throwable) {
                CrashReport crashreport;
                try {
                    crashreport = CrashReport.a(throwable, "Exception ticking world");
                }
                catch (Throwable t) {
                    throw new RuntimeException("Error generating crash report", t);
                }
                worldserver.a(crashreport);
                throw new ReportedException(crashreport);
            }
            try {
                worldserver.timings.tickEntities.startTiming();
                worldserver.tickEntities();
                worldserver.timings.tickEntities.stopTiming();
            }
            catch (Throwable throwable1) {
                CrashReport crashreport;
                try {
                    crashreport = CrashReport.a(throwable1, "Exception ticking world entities");
                }
                catch (Throwable t) {
                    throw new RuntimeException("Error generating crash report", t);
                }
                worldserver.a(crashreport);
                throw new ReportedException(crashreport);
            }
            this.methodProfiler.b();
            this.methodProfiler.a("tracker");
            worldserver.timings.tracker.startTiming();
            worldserver.getTracker().updatePlayers();
            worldserver.timings.tracker.stopTiming();
            this.methodProfiler.b();
            this.methodProfiler.b();
            ++i;
        }
        this.methodProfiler.c("connection");
        SpigotTimings.connectionTimer.startTiming();
        this.am().c();
        SpigotTimings.connectionTimer.stopTiming();
        this.methodProfiler.c("players");
        SpigotTimings.playerListTimer.startTiming();
        this.v.tick();
        SpigotTimings.playerListTimer.stopTiming();
        this.methodProfiler.c("tickables");
        SpigotTimings.tickablesTimer.startTiming();
        i = 0;
        while (i < this.o.size()) {
            this.o.get(i).E_();
            ++i;
        }
        SpigotTimings.tickablesTimer.stopTiming();
        this.methodProfiler.b();
    }

    public boolean getAllowNether() {
        return true;
    }

    public void a(ITickable itickable) {
        this.o.add(itickable);
    }

    public static void main(OptionSet options) {
        DispenserRegistry.c();
        try {
            int port;
            String s1 = ".";
            YggdrasilAuthenticationService yggdrasilauthenticationservice = new YggdrasilAuthenticationService(Proxy.NO_PROXY, UUID.randomUUID().toString());
            MinecraftSessionService minecraftsessionservice = yggdrasilauthenticationservice.createMinecraftSessionService();
            GameProfileRepository gameprofilerepository = yggdrasilauthenticationservice.createProfileRepository();
            UserCache usercache = new UserCache(gameprofilerepository, new File(s1, a.getName()));
            DedicatedServer dedicatedserver = new DedicatedServer(options, DataConverterRegistry.a(), yggdrasilauthenticationservice, minecraftsessionservice, gameprofilerepository, usercache);
            if (options.has("port") && (port = ((Integer)options.valueOf("port")).intValue()) > 0) {
                dedicatedserver.setPort(port);
            }
            if (options.has("universe")) {
                dedicatedserver.universe = (File)options.valueOf("universe");
            }
            if (options.has("world")) {
                dedicatedserver.setWorld((String)options.valueOf("world"));
            }
            dedicatedserver.primaryThread.start();
        }
        catch (Exception exception) {
            LOGGER.fatal("Failed to start the minecraft server", (Throwable)exception);
        }
    }

    public void F() {
    }

    public File d(String s) {
        return new File(this.A(), s);
    }

    public void info(String s) {
        LOGGER.info(s);
    }

    public void warning(String s) {
        LOGGER.warn(s);
    }

    public WorldServer getWorldServer(int i) {
        for (WorldServer world : this.worlds) {
            if (world.dimension != i) continue;
            return world;
        }
        return this.worlds.get(0);
    }

    public String getVersion() {
        return "1.10.2";
    }

    public int H() {
        return this.v.getPlayerCount();
    }

    public int I() {
        return this.v.getMaxPlayers();
    }

    public String[] getPlayers() {
        return this.v.f();
    }

    public GameProfile[] K() {
        return this.v.g();
    }

    public boolean isDebugging() {
        return this.getPropertyManager().getBoolean("debug", false);
    }

    public void g(String s) {
        LOGGER.error(s);
    }

    public void h(String s) {
        if (this.isDebugging()) {
            LOGGER.info(s);
        }
    }

    public String getServerModName() {
        return "Spigot";
    }

    public CrashReport b(CrashReport crashreport) {
        crashreport.g().a("Profiler Position", new CrashReportCallable(){

            public String a() throws Exception {
                return MinecraftServer.this.methodProfiler.a ? MinecraftServer.this.methodProfiler.c() : "N/A (disabled)";
            }

            @Override
            public Object call() throws Exception {
                return this.a();
            }
        });
        if (this.v != null) {
            crashreport.g().a("Player Count", new CrashReportCallable(){

                public String a() {
                    return String.valueOf(MinecraftServer.this.v.getPlayerCount()) + " / " + MinecraftServer.this.v.getMaxPlayers() + "; " + MinecraftServer.this.v.v();
                }

                @Override
                public Object call() throws Exception {
                    return this.a();
                }
            });
        }
        return crashreport;
    }

    public List<String> tabCompleteCommand(ICommandListener icommandlistener, String s, @Nullable BlockPosition blockposition, boolean flag) {
        return this.server.tabComplete(icommandlistener, s);
    }

    public boolean M() {
        return true;
    }

    @Override
    public String getName() {
        return "Server";
    }

    @Override
    public void sendMessage(IChatBaseComponent ichatbasecomponent) {
        LOGGER.info(ichatbasecomponent.toPlainText());
    }

    @Override
    public boolean a(int i, String s) {
        return true;
    }

    public ICommandHandler getCommandHandler() {
        return this.b;
    }

    public KeyPair O() {
        return this.H;
    }

    public int P() {
        return this.u;
    }

    public void setPort(int i) {
        this.u = i;
    }

    public String Q() {
        return this.I;
    }

    public void i(String s) {
        this.I = s;
    }

    public boolean R() {
        if (this.I != null) {
            return true;
        }
        return false;
    }

    public String S() {
        return this.J;
    }

    public void setWorld(String s) {
        this.J = s;
    }

    public void a(KeyPair keypair) {
        this.H = keypair;
    }

    public void a(EnumDifficulty enumdifficulty) {
        int i = this.worlds.size();
        int j = 0;
        while (j < i) {
            WorldServer worldserver = this.worlds.get(j);
            if (worldserver != null) {
                if (worldserver.getWorldData().isHardcore()) {
                    worldserver.getWorldData().setDifficulty(EnumDifficulty.HARD);
                    worldserver.setSpawnFlags(true, true);
                } else if (this.R()) {
                    worldserver.getWorldData().setDifficulty(enumdifficulty);
                    worldserver.setSpawnFlags(worldserver.getDifficulty() != EnumDifficulty.PEACEFUL, true);
                } else {
                    worldserver.getWorldData().setDifficulty(enumdifficulty);
                    worldserver.setSpawnFlags(this.getSpawnMonsters(), this.spawnAnimals);
                }
            }
            ++j;
        }
    }

    public boolean getSpawnMonsters() {
        return true;
    }

    public boolean V() {
        return this.demoMode;
    }

    public void b(boolean flag) {
        this.demoMode = flag;
    }

    public void c(boolean flag) {
        this.M = flag;
    }

    public Convertable getConvertable() {
        return this.convertable;
    }

    public String getResourcePack() {
        return this.N;
    }

    public String getResourcePackHash() {
        return this.O;
    }

    public void setResourcePack(String s, String s1) {
        this.N = s;
        this.O = s1;
    }

    @Override
    public void a(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.a("whitelist_enabled", false);
        mojangstatisticsgenerator.a("whitelist_count", 0);
        if (this.v != null) {
            mojangstatisticsgenerator.a("players_current", this.H());
            mojangstatisticsgenerator.a("players_max", this.I());
            mojangstatisticsgenerator.a("players_seen", this.v.getSeenPlayers().length);
        }
        mojangstatisticsgenerator.a("uses_auth", this.onlineMode);
        mojangstatisticsgenerator.a("gui_state", this.ao() ? "enabled" : "disabled");
        mojangstatisticsgenerator.a("run_time", (MinecraftServer.av() - mojangstatisticsgenerator.g()) / 60 * 1000);
        mojangstatisticsgenerator.a("avg_tick_ms", (int)(MathHelper.a(this.h) * 1.0E-6));
        int i = 0;
        if (this.worldServer != null) {
            int j = 0;
            while (j < this.worlds.size()) {
                WorldServer worldserver = this.worlds.get(j);
                if (worldserver != null) {
                    WorldData worlddata = worldserver.getWorldData();
                    mojangstatisticsgenerator.a("world[" + i + "][dimension]", worldserver.worldProvider.getDimensionManager().getDimensionID());
                    mojangstatisticsgenerator.a("world[" + i + "][mode]", (Object)((Object)worlddata.getGameType()));
                    mojangstatisticsgenerator.a("world[" + i + "][difficulty]", (Object)((Object)worldserver.getDifficulty()));
                    mojangstatisticsgenerator.a("world[" + i + "][hardcore]", worlddata.isHardcore());
                    mojangstatisticsgenerator.a("world[" + i + "][generator_name]", worlddata.getType().name());
                    mojangstatisticsgenerator.a("world[" + i + "][generator_version]", worlddata.getType().getVersion());
                    mojangstatisticsgenerator.a("world[" + i + "][height]", this.F);
                    mojangstatisticsgenerator.a("world[" + i + "][chunks_loaded]", worldserver.getChunkProviderServer().g());
                    ++i;
                }
                ++j;
            }
        }
        mojangstatisticsgenerator.a("worlds", i);
    }

    @Override
    public void b(MojangStatisticsGenerator mojangstatisticsgenerator) {
        mojangstatisticsgenerator.b("singleplayer", this.R());
        mojangstatisticsgenerator.b("server_brand", this.getServerModName());
        mojangstatisticsgenerator.b("gui_supported", GraphicsEnvironment.isHeadless() ? "headless" : "supported");
        mojangstatisticsgenerator.b("dedicated", this.aa());
    }

    @Override
    public boolean getSnooperEnabled() {
        return true;
    }

    public abstract boolean aa();

    public boolean getOnlineMode() {
        return this.server.getOnlineMode();
    }

    public void setOnlineMode(boolean flag) {
        this.onlineMode = flag;
    }

    public boolean getSpawnAnimals() {
        return this.spawnAnimals;
    }

    public void setSpawnAnimals(boolean flag) {
        this.spawnAnimals = flag;
    }

    public boolean getSpawnNPCs() {
        return this.spawnNPCs;
    }

    public abstract boolean ae();

    public void setSpawnNPCs(boolean flag) {
        this.spawnNPCs = flag;
    }

    public boolean getPVP() {
        return this.pvpMode;
    }

    public void setPVP(boolean flag) {
        this.pvpMode = flag;
    }

    public boolean getAllowFlight() {
        return this.allowFlight;
    }

    public void setAllowFlight(boolean flag) {
        this.allowFlight = flag;
    }

    public abstract boolean getEnableCommandBlock();

    public String getMotd() {
        return this.motd;
    }

    public void setMotd(String s) {
        this.motd = s;
    }

    public int getMaxBuildHeight() {
        return this.F;
    }

    public void c(int i) {
        this.F = i;
    }

    public boolean isStopped() {
        return this.isStopped;
    }

    public PlayerList getPlayerList() {
        return this.v;
    }

    public void a(PlayerList playerlist) {
        this.v = playerlist;
    }

    public void setGamemode(EnumGamemode enumgamemode) {
        int i = 0;
        while (i < this.worlds.size()) {
            this.worlds.get(i).getWorldData().setGameType(enumgamemode);
            ++i;
        }
    }

    public ServerConnection getServerConnection() {
        return this.p;
    }

    public ServerConnection am() {
        ServerConnection serverConnection = this.p == null ? (this.p = new ServerConnection(this)) : this.p;
        return serverConnection;
    }

    public boolean ao() {
        return false;
    }

    public abstract String a(EnumGamemode var1, boolean var2);

    public int ap() {
        return this.ticks;
    }

    public void aq() {
        this.S = true;
    }

    @Override
    public BlockPosition getChunkCoordinates() {
        return BlockPosition.ZERO;
    }

    @Override
    public Vec3D d() {
        return Vec3D.a;
    }

    @Override
    public World getWorld() {
        return this.worlds.get(0);
    }

    @Override
    public Entity f() {
        return null;
    }

    public int getSpawnProtection() {
        return 16;
    }

    public boolean a(World world, BlockPosition blockposition, EntityHuman entityhuman) {
        return false;
    }

    public void setForceGamemode(boolean flag) {
        this.T = flag;
    }

    public boolean getForceGamemode() {
        return this.T;
    }

    public Proxy au() {
        return this.e;
    }

    public static long av() {
        return System.currentTimeMillis();
    }

    public int getIdleTimeout() {
        return this.G;
    }

    public void setIdleTimeout(int i) {
        this.G = i;
    }

    @Override
    public IChatBaseComponent getScoreboardDisplayName() {
        return new ChatComponentText(this.getName());
    }

    public boolean ax() {
        return true;
    }

    public MinecraftSessionService ay() {
        return this.V;
    }

    public GameProfileRepository getGameProfileRepository() {
        return this.W;
    }

    public UserCache getUserCache() {
        return this.X;
    }

    public ServerPing getServerPing() {
        return this.q;
    }

    public void aC() {
        this.Y = 0;
    }

    @Nullable
    public Entity a(UUID uuid) {
        WorldServer[] aworldserver = this.worldServer;
        aworldserver.length;
        int j = 0;
        while (j < this.worlds.size()) {
            Entity entity;
            WorldServer worldserver = this.worlds.get(j);
            if (worldserver != null && (entity = worldserver.getEntity(uuid)) != null) {
                return entity;
            }
            ++j;
        }
        return null;
    }

    @Override
    public boolean getSendCommandFeedback() {
        return this.worlds.get(0).getGameRules().getBoolean("sendCommandFeedback");
    }

    @Override
    public void a(CommandObjectiveExecutor.EnumCommandResult commandobjectiveexecutor_enumcommandresult, int i) {
    }

    @Override
    public MinecraftServer h() {
        return this;
    }

    public int aD() {
        return 29999984;
    }

    public <V> ListenableFuture<V> a(Callable<V> callable) {
        Validate.notNull(callable);
        if (!this.isMainThread()) {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.create(callable);
            this.j.add(listenablefuturetask);
            return listenablefuturetask;
        }
        try {
            return Futures.immediateFuture(callable.call());
        }
        catch (Exception exception) {
            return Futures.immediateFailedCheckedFuture(exception);
        }
    }

    @Override
    public ListenableFuture<Object> postToMainThread(Runnable runnable) {
        Validate.notNull(runnable);
        return this.a(Executors.callable(runnable));
    }

    @Override
    public boolean isMainThread() {
        if (Thread.currentThread() == this.serverThread) {
            return true;
        }
        return false;
    }

    public int aF() {
        return 256;
    }

    public long aG() {
        return this.aa;
    }

    public Thread aH() {
        return this.serverThread;
    }

    public DataConverterManager getDataConverterManager() {
        return this.dataConverterManager;
    }

    public int a(@Nullable WorldServer worldserver) {
        return worldserver != null ? worldserver.getGameRules().c("spawnRadius") : 10;
    }

    @Deprecated
    public static MinecraftServer getServer() {
        return Bukkit.getServer() instanceof CraftServer ? ((CraftServer)Bukkit.getServer()).getServer() : null;
    }

}

