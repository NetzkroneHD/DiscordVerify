package de.netzkronehd.discordverifybot.group;

public class Group {

    private final String id, name, roleId, permission;
    private final int priority;

    public Group(String id, String name, String roleId, String permission, int priority) {
        this.id = id;
        this.name = name;
        this.roleId = roleId;
        this.permission = permission;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRoleId() {
        return roleId;
    }

    public String getPermission() {
        return permission;
    }

    public int getPriority() {
        return priority;
    }
}
