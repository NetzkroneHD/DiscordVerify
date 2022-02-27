package de.netzkronehd.discordverifybot.manager;

import de.netzkronehd.configuration.file.YamlConfiguration;
import de.netzkronehd.discordverifybot.DiscordVerifyBot;
import de.netzkronehd.discordverifybot.group.Group;
import de.netzkronehd.discordverifybot.player.DiscordPlayer;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

public class GroupManager extends Manager {

    private final HashMap<String, Group> groups, groupByRoleId;
    private Group defaultGroup;
    private final File file;
    private YamlConfiguration cfg;

    public GroupManager(DiscordVerifyBot discordVerifyBot) {
        super(discordVerifyBot);
        groups = new HashMap<>();
        groupByRoleId = new HashMap<>();

        file = new File("plugins/DiscordVerifyBot", "groups.yml");
        cfg = YamlConfiguration.loadConfiguration(file);

    }

    @Override
    public void onLoad() {
        createFile();
        readFile();
    }

    @Override
    public void onReload() {
        groups.clear();
        cfg = YamlConfiguration.loadConfiguration(file);
        onLoad();
    }

    @Override
    public void createFile() {
        if(!file.exists()) {
            cfg.set("Owner.name", "&4Owner");
            cfg.set("Owner.roleId", -1);
            cfg.set("Owner.permission", "discordverify.group.owner");
            cfg.set("Owner.priority", 0);

            cfg.set("Admin.name", "&cAdmin");
            cfg.set("Admin.roleId", -1);
            cfg.set("Admin.permission", "discordverify.group.admin");
            cfg.set("Admin.priority", 1);

            cfg.set("Premium.name", "&6Premium");
            cfg.set("Premium.roleId", -1);
            cfg.set("Premium.permission", "discordverify.group.premium");
            cfg.set("Premium.priority", 2);

            cfg.set("default.name", "&7Player");
            cfg.set("default.roleId", -1);
            cfg.set("default.permission", "discordverify.group.default");
            cfg.set("default.priority", 3);

            try {
                cfg.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void readFile() {
        for(String id : cfg.getKeys(false)) {
            final Group group = new Group(id.toLowerCase(), cfg.getString(id+".name"), cfg.getString(id+".roleId"), cfg.getString(id+".permission"), cfg.getInt(id+".priority"));
            groups.put(group.getId(), group);
        }
        defaultGroup = groups.get("groups");
        if(defaultGroup == null) log(Level.SEVERE, "Could not find default group.");

    }

    public void removeGroups(Member member) {
        final List<Group> dcGroups = getGroups(member);
        dcGroups.forEach(group -> {
            Role role = group.getRole();
            if(role == null) {
                role = discordVerifyBot.getBot().getGuild().getRoleById(group.getRoleId());
            }
            assert role != null;
            member.getGuild().removeRoleFromMember(member, role).queue();
        });
    }

    public List<Group> getGroups(Member member) {
        final List<Group> discordGroups = new ArrayList<>();

        for(Role role : member.getRoles()) {
            final Group group = getGroupByRoleId(role.getId());
            if(group != null) discordGroups.add(group);
        }

        return discordGroups;
    }

    public List<Group> getGroups(DiscordPlayer dp) {
        final List<Group> permissionGroup = new ArrayList<>();
        if(discordVerifyBot.getConfigManager().isMultipleGroups()) {
            for(Group group : groups.values()) {
                if(group.equals(defaultGroup)) continue;
                if(dp.hasPermission(group.getPermission())) permissionGroup.add(group);
            }
            if(permissionGroup.isEmpty()) permissionGroup.add(defaultGroup);
        } else {
            Group highestGroup = null;
            for(Group group : groups.values()) {
                if(highestGroup == null) highestGroup = group;
                if(dp.hasPermission(group.getPermission())) {
                    if(group.getPriority() < highestGroup.getPriority()) highestGroup = group;
                }
            }
            permissionGroup.add(highestGroup);
        }

        return permissionGroup;
    }

    public Group getGroupByRoleId(String roleId) {
        return groupByRoleId.get(roleId);
    }

    public Group getDefaultGroup() {
        return defaultGroup;
    }

    public HashMap<String, Group> getGroupByRoleId() {
        return groupByRoleId;
    }

    public HashMap<String, Group> getGroups() {
        return groups;
    }
}
