package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;

import java.util.HashMap;
import java.util.logging.Level;

public abstract class Manager {

    private static final HashMap<String, Manager> managers = new HashMap<>();
    private int priority = 5;

    protected final DiscordVerifyBot discordVerifyBot;

    public Manager(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
    }

    public abstract void onLoad();
    public abstract void onReload();

    public void createFile() {}
    public void readFile() {}

    public void log(String msg) {
        log(Level.INFO, msg);
    }
    public void log(Level level, String msg) {
        discordVerifyBot.getLogger().log(level, "("+getClass().getSimpleName()+"): "+msg);
    }

    public void setPriority(int priority) {
        this.priority = priority;
        if (this.priority > 10) this.priority = 10;
    }
    public int getPriority() {
        return priority;
    }

    public static void loadManagers() {
        for(int i = 0; i < 11; i++) {
            for(Manager m : managers.values()) {
                if(m.getPriority() == i) {
                    try {
                        final long before = System.currentTimeMillis();
                        m.log("Loading "+m.getClass().getSimpleName()+"...");
                        m.onLoad();
                        m.log(m.getClass().getSimpleName()+" loaded after "+(System.currentTimeMillis()-before)+"ms");
                    } catch (Exception e) {
                        e.printStackTrace();
                        m.log("An error occurred while loading "+m.getClass().getSimpleName()+": "+e);
                    }
                }
            }
        }
    }

    public static void reloadManagers() {
        for(int i = 0; i < 11; i++) {
            for(Manager m : managers.values()) {
                if(m.getPriority() == i) {
                    try {
                        final long before = System.currentTimeMillis();
                        m.log("Reloading "+m.getClass().getSimpleName()+"...");
                        m.onReload();
                        m.log(m.getClass().getSimpleName()+" reloaded after "+(System.currentTimeMillis()-before)+"ms");
                    } catch (Exception e) {
                        e.printStackTrace();
                        m.log("An error occurred while reloading "+m.getClass().getSimpleName()+": "+e);
                    }
                }
            }
        }
    }

    public static void registerManager(Manager m) {
        managers.put(m.getClass().getSimpleName().toLowerCase(), m);
    }

    public static HashMap<String, Manager> getManagers() {
        return managers;
    }

}
