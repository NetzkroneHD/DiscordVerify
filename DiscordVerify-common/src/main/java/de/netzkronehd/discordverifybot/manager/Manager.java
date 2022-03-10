package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.discordverifybot.DiscordVerifyBot;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.logging.Level;

public abstract class Manager {

    private static final HashMap<String, Manager> managers = new HashMap<>();
    private static int lowestPriority = 6;

    private int priority = 5;

    protected final DiscordVerifyBot discordVerifyBot;

    public Manager(DiscordVerifyBot discordVerifyBot) {
        this.discordVerifyBot = discordVerifyBot;
        registerManager(this);
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
        if(priority >= lowestPriority) lowestPriority = priority+1;
    }

    public InputStream getResource(String filename) {
        if (filename == null) {
            throw new IllegalArgumentException("Filename cannot be null");
        }

        try {
            final URL url = this.getClass().getClassLoader().getResource(filename);

            if (url == null) {
                return null;
            }

            final URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            return connection.getInputStream();
        } catch (IOException ex) {
            return null;
        }
    }

    public void saveResource(String resourcePath, String dataFolder, boolean replace) {
        if (resourcePath == null || resourcePath.equals("")) {
            throw new IllegalArgumentException("ResourcePath cannot be null or empty");
        }

        resourcePath = resourcePath.replace('\\', '/');
        final InputStream in = getResource(resourcePath);

        final File outFile = new File(dataFolder, resourcePath);
        final int lastIndex = resourcePath.lastIndexOf('/');
        final File outDir = new File(dataFolder, resourcePath.substring(0, Math.max(lastIndex, 0)));

        if (!outDir.exists()) {
            outDir.mkdirs();
        }

        try {
            if (!outFile.exists() || replace) {
                OutputStream out = new FileOutputStream(outFile);
                byte[] buf = new byte[1024];
                int len;
                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }
                out.close();
                in.close();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            log(Level.SEVERE, "Could not save " + outFile.getName() + " to " + outFile);
        }

    }

    public int getPriority() {
        return priority;
    }

    public static void loadManagers() {
        for(int i = 0; i < lowestPriority; i++) {
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
        for(int i = 0; i < lowestPriority; i++) {
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
        managers.put(m.getClass().getName().toLowerCase(), m);
    }

    public static HashMap<String, Manager> getManagers() {
        return managers;
    }

}
